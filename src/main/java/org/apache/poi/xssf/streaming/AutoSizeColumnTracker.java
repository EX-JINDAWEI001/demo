/*     */ package org.apache.poi.xssf.streaming;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.poi.ss.usermodel.Cell;
/*     */ import org.apache.poi.ss.usermodel.DataFormatter;
/*     */ import org.apache.poi.ss.usermodel.Row;
/*     */ import org.apache.poi.ss.usermodel.Sheet;
/*     */ import org.apache.poi.ss.util.SheetUtil;
/*     */ import org.apache.poi.util.Internal;
/*     */ 
/*     */ @Internal
/*     */ class AutoSizeColumnTracker
/*     */ {
/*     */   private final int defaultCharWidth;
/*  51 */   private final DataFormatter dataFormatter = new DataFormatter();
/*     */ 
/*  58 */   private final Map<Integer, ColumnWidthPair> maxColumnWidths = new HashMap();
/*     */ 
/*  61 */   private final Set<Integer> untrackedColumns = new HashSet();
/*     */   private boolean trackAllColumns;
/*     */ 
/*     */   public AutoSizeColumnTracker(Sheet sheet)
/*     */   {
/* 117 */     this.defaultCharWidth = 5;
/*     */   }
/*     */ 
/*     */   public SortedSet<Integer> getTrackedColumns()
/*     */   {
/* 129 */     SortedSet sorted = new TreeSet(this.maxColumnWidths.keySet());
/* 130 */     return Collections.unmodifiableSortedSet(sorted);
/*     */   }
/*     */ 
/*     */   public boolean isColumnTracked(int column)
/*     */   {
/* 141 */     return (this.trackAllColumns) || (this.maxColumnWidths.containsKey(Integer.valueOf(column)));
/*     */   }
/*     */ 
/*     */   public boolean isAllColumnsTracked()
/*     */   {
/* 151 */     return this.trackAllColumns;
/*     */   }
/*     */ 
/*     */   public void trackAllColumns()
/*     */   {
/* 160 */     this.trackAllColumns = true;
/* 161 */     this.untrackedColumns.clear();
/*     */   }
/*     */ 
/*     */   public void untrackAllColumns()
/*     */   {
/* 170 */     this.trackAllColumns = false;
/* 171 */     this.maxColumnWidths.clear();
/* 172 */     this.untrackedColumns.clear();
/*     */   }
/*     */ 
/*     */   public void trackColumns(Collection<Integer> columns)
/*     */   {
/* 185 */     for (Iterator localIterator = columns.iterator(); localIterator.hasNext(); ) { int column = ((Integer)localIterator.next()).intValue();
/* 186 */       trackColumn(column); }
/*     */ 
/*     */   }
/*     */ 
/*     */   public boolean trackColumn(int column)
/*     */   {
/* 200 */     this.untrackedColumns.remove(Integer.valueOf(column));
/* 201 */     if (!this.maxColumnWidths.containsKey(Integer.valueOf(column))) {
/* 202 */       this.maxColumnWidths.put(Integer.valueOf(column), new ColumnWidthPair());
/* 203 */       return true;
/*     */     }
/* 205 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean implicitlyTrackColumn(int column)
/*     */   {
/* 217 */     if (!this.untrackedColumns.contains(Integer.valueOf(column))) {
/* 218 */       trackColumn(column);
/* 219 */       return true;
/*     */     }
/* 221 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean untrackColumns(Collection<Integer> columns)
/*     */   {
/* 235 */     this.untrackedColumns.addAll(columns);
/* 236 */     return this.maxColumnWidths.keySet().removeAll(columns);
/*     */   }
/*     */ 
/*     */   public boolean untrackColumn(int column)
/*     */   {
/* 249 */     this.untrackedColumns.add(Integer.valueOf(column));
/* 250 */     return this.maxColumnWidths.keySet().remove(Integer.valueOf(column));
/*     */   }
/*     */ 
/*     */   public int getBestFitColumnWidth(int column, boolean useMergedCells)
/*     */   {
/* 263 */     if (!this.maxColumnWidths.containsKey(Integer.valueOf(column)))
/*     */     {
/* 265 */       if (this.trackAllColumns) {
/* 266 */         if (!implicitlyTrackColumn(column)) {
/* 267 */           Throwable reason = new IllegalStateException("Column was explicitly untracked after trackAllColumns() was called.");
/*     */ 
/* 269 */           throw new IllegalStateException("Cannot get best fit column width on explicitly untracked column " + column + ". Either explicitly track the column or track all columns.", reason);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 275 */         Throwable reason = new IllegalStateException("Column was never explicitly tracked and isAllColumnsTracked() is false (trackAllColumns() was never called or untrackAllColumns() was called after trackAllColumns() was called).");
/*     */ 
/* 278 */         throw new IllegalStateException("Cannot get best fit column width on untracked column " + column + ". Either explicitly track the column or track all columns.", reason);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 283 */     double width = ((ColumnWidthPair)this.maxColumnWidths.get(Integer.valueOf(column))).getMaxColumnWidth(useMergedCells);
/* 284 */     return (int)(256.0D * width);
/*     */   }
/*     */ 
/*     */   public void updateColumnWidths(Row row)
/*     */   {
/* 297 */     implicitlyTrackColumnsInRow(row);
/*     */ 
/* 302 */     if (this.maxColumnWidths.size() < row.getPhysicalNumberOfCells())
/*     */     {
/* 304 */       for (Entry e : this.maxColumnWidths.entrySet()) {
/* 305 */         int column = ((Integer)e.getKey()).intValue();
/* 306 */         Cell cell = row.getCell(column);
/*     */ 
/* 314 */         if (cell != null) {
/* 315 */           ColumnWidthPair pair = (ColumnWidthPair)e.getValue();
/* 316 */           updateColumnWidth(cell, pair);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/* 322 */       for (Cell cell : row) {
/* 323 */         int column = cell.getColumnIndex();
/*     */ 
/* 331 */         if (this.maxColumnWidths.containsKey(Integer.valueOf(column))) {
/* 332 */           ColumnWidthPair pair = (ColumnWidthPair)this.maxColumnWidths.get(Integer.valueOf(column));
/* 333 */           updateColumnWidth(cell, pair);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private void implicitlyTrackColumnsInRow(Row row)
/*     */   {
/* 350 */     if (!this.trackAllColumns)
/*     */       return;
/* 352 */     for (Cell cell : row) {
/* 353 */       int column = cell.getColumnIndex();
/* 354 */       implicitlyTrackColumn(column);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateColumnWidth(Cell cell, ColumnWidthPair pair)
/*     */   {
/* 367 */     double unmergedWidth = SheetUtil.getCellWidth(cell, this.defaultCharWidth, this.dataFormatter, false);
/* 368 */     double mergedWidth = SheetUtil.getCellWidth(cell, this.defaultCharWidth, this.dataFormatter, true);
/* 369 */     pair.setMaxColumnWidths(unmergedWidth, mergedWidth);
/*     */   }
/*     */ 
/*     */   private static class ColumnWidthPair
/*     */   {
/*     */     private double withSkipMergedCells;
/*     */     private double withUseMergedCells;
/*     */ 
/*     */     public ColumnWidthPair()
/*     */     {
/*  79 */       this(-1.0D, -1.0D);
/*     */     }
/*     */ 
/*     */     public ColumnWidthPair(double columnWidthSkipMergedCells, double columnWidthUseMergedCells) {
/*  83 */       this.withSkipMergedCells = columnWidthSkipMergedCells;
/*  84 */       this.withUseMergedCells = columnWidthUseMergedCells;
/*     */     }
/*     */ 
/*     */     public double getMaxColumnWidth(boolean useMergedCells)
/*     */     {
/*  94 */       return (useMergedCells) ? this.withUseMergedCells : this.withSkipMergedCells;
/*     */     }
/*     */ 
/*     */     public void setMaxColumnWidths(double unmergedWidth, double mergedWidth)
/*     */     {
/* 104 */       this.withUseMergedCells = Math.max(this.withUseMergedCells, mergedWidth);
/* 105 */       this.withSkipMergedCells = Math.max(this.withUseMergedCells, unmergedWidth);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\.m2\repository\org\apache\poi\poi-ooxml\4.0.1\poi-ooxml-4.0.1.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.AutoSizeColumnTracker
 * JD-Core Version:    0.5.4
 */