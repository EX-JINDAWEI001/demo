//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.demo.point.proxy.asm;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.util.Internal;

import java.util.*;
import java.util.Map.Entry;

@Internal
class AutoSizeColumnTracker {
    private final int defaultCharWidth;
    private final DataFormatter dataFormatter = new DataFormatter();
    private final Map<Integer, ColumnWidthPair> maxColumnWidths = new HashMap();
    private final Set<Integer> untrackedColumns = new HashSet();
    private boolean trackAllColumns;

    public AutoSizeColumnTracker(Sheet sheet) {
        this.defaultCharWidth = SheetUtil.getDefaultCharWidth(sheet.getWorkbook());
    }

    public SortedSet<Integer> getTrackedColumns() {
        SortedSet<Integer> sorted = new TreeSet(this.maxColumnWidths.keySet());
        return Collections.unmodifiableSortedSet(sorted);
    }

    public boolean isColumnTracked(int column) {
        return this.trackAllColumns || this.maxColumnWidths.containsKey(column);
    }

    public boolean isAllColumnsTracked() {
        return this.trackAllColumns;
    }

    public void trackAllColumns() {
        this.trackAllColumns = true;
        this.untrackedColumns.clear();
    }

    public void untrackAllColumns() {
        this.trackAllColumns = false;
        this.maxColumnWidths.clear();
        this.untrackedColumns.clear();
    }

    public void trackColumns(Collection<Integer> columns) {
        Iterator var2 = columns.iterator();

        while(var2.hasNext()) {
            int column = (Integer)var2.next();
            this.trackColumn(column);
        }

    }

    public boolean trackColumn(int column) {
        this.untrackedColumns.remove(column);
        if (!this.maxColumnWidths.containsKey(column)) {
            this.maxColumnWidths.put(column, new ColumnWidthPair());
            return true;
        } else {
            return false;
        }
    }

    private boolean implicitlyTrackColumn(int column) {
        if (!this.untrackedColumns.contains(column)) {
            this.trackColumn(column);
            return true;
        } else {
            return false;
        }
    }

    public boolean untrackColumns(Collection<Integer> columns) {
        this.untrackedColumns.addAll(columns);
        return this.maxColumnWidths.keySet().removeAll(columns);
    }

    public boolean untrackColumn(int column) {
        this.untrackedColumns.add(column);
        return this.maxColumnWidths.keySet().remove(column);
    }

    public int getBestFitColumnWidth(int column, boolean useMergedCells) {
        if (!this.maxColumnWidths.containsKey(column)) {
            IllegalStateException reason;
            if (!this.trackAllColumns) {
                reason = new IllegalStateException("Column was never explicitly tracked and isAllColumnsTracked() is false (trackAllColumns() was never called or untrackAllColumns() was called after trackAllColumns() was called).");
                throw new IllegalStateException("Cannot get best fit column width on untracked column " + column + ". Either explicitly track the column or track all columns.", reason);
            }

            if (!this.implicitlyTrackColumn(column)) {
                reason = new IllegalStateException("Column was explicitly untracked after trackAllColumns() was called.");
                throw new IllegalStateException("Cannot get best fit column width on explicitly untracked column " + column + ". Either explicitly track the column or track all columns.", reason);
            }
        }

        double width = ((ColumnWidthPair)this.maxColumnWidths.get(column)).getMaxColumnWidth(useMergedCells);
        return (int)(256.0D * width);
    }

    public void updateColumnWidths(Row row) {
        this.implicitlyTrackColumnsInRow(row);
        Iterator var2;
        int column;
        if (this.maxColumnWidths.size() < row.getPhysicalNumberOfCells()) {
            var2 = this.maxColumnWidths.entrySet().iterator();

            while(var2.hasNext()) {
                Entry<Integer, ColumnWidthPair> e = (Entry)var2.next();
                column = (Integer)e.getKey();
                Cell cell = row.getCell(column);
                if (cell != null) {
                    ColumnWidthPair pair = (ColumnWidthPair)e.getValue();
                    this.updateColumnWidth(cell, pair);
                }
            }
        } else {
            var2 = row.iterator();

            while(var2.hasNext()) {
                Cell cell = (Cell)var2.next();
                column = cell.getColumnIndex();
                if (this.maxColumnWidths.containsKey(column)) {
                    ColumnWidthPair pair = (ColumnWidthPair)this.maxColumnWidths.get(column);
                    this.updateColumnWidth(cell, pair);
                }
            }
        }

    }

    private void implicitlyTrackColumnsInRow(Row row) {
        if (this.trackAllColumns) {
            Iterator var2 = row.iterator();

            while(var2.hasNext()) {
                Cell cell = (Cell)var2.next();
                int column = cell.getColumnIndex();
                this.implicitlyTrackColumn(column);
            }
        }

    }

    private void updateColumnWidth(Cell cell, ColumnWidthPair pair) {
        double unmergedWidth = SheetUtil.getCellWidth(cell, this.defaultCharWidth, this.dataFormatter, false);
        double mergedWidth = SheetUtil.getCellWidth(cell, this.defaultCharWidth, this.dataFormatter, true);
        pair.setMaxColumnWidths(unmergedWidth, mergedWidth);
    }

    private static class ColumnWidthPair {
        private double withSkipMergedCells;
        private double withUseMergedCells;

        public ColumnWidthPair() {
            this(-1.0D, -1.0D);
        }

        public ColumnWidthPair(double columnWidthSkipMergedCells, double columnWidthUseMergedCells) {
            this.withSkipMergedCells = columnWidthSkipMergedCells;
            this.withUseMergedCells = columnWidthUseMergedCells;
        }

        public double getMaxColumnWidth(boolean useMergedCells) {
            return useMergedCells ? this.withUseMergedCells : this.withSkipMergedCells;
        }

        public void setMaxColumnWidths(double unmergedWidth, double mergedWidth) {
            this.withUseMergedCells = Math.max(this.withUseMergedCells, mergedWidth);
            this.withSkipMergedCells = Math.max(this.withUseMergedCells, unmergedWidth);
        }
    }
}
