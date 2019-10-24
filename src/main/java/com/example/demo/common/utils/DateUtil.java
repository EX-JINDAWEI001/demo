package com.example.demo.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static final String default_pattern1 = "yyyy-MM-dd HH:mm:ss";

    private static final String default_pattern2 = "yyyy-MM-dd";

    private static final String default_pattern3 = "yyyy-MM";

    private static final long millis = 24 * 60 * 60 * 1000;

    /**
     * 将字符串转为日期
     * @param dateStr
     * @param format
     * @return
     */
    public static Date str2Date(String dateStr, String format) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        if (StringUtils.isBlank(format)) {
            format = default_pattern1;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将日期转为字符串
     * @param date
     * @param format
     * @return
     */
    public static String date2Str(Date date, String format) {
        if(date == null){
            return null;
        }
        if (StringUtils.isBlank(format)) {
            format = default_pattern1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 将日期字符串转换为另一种格式的日期字符串
     * @param dateStr
     * @param inputFormat
     * @param outputFormat
     * @return
     */
    public static String str2Str(String dateStr, String inputFormat, String outputFormat){
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        if (StringUtils.isBlank(inputFormat)) {
            inputFormat = default_pattern1;
        }
        if (StringUtils.isBlank(outputFormat)) {
            outputFormat = default_pattern1;
        }
        SimpleDateFormat sdfi = new SimpleDateFormat(inputFormat);
        SimpleDateFormat sdfo = new SimpleDateFormat(outputFormat);
        try {
            Date date = sdfi.parse(dateStr);
            return sdfo.format(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将日期增加或减少interval天
     * @param date
     * @param interval
     * @return
     */
    public static Date addIntervalDay(Date date, int interval){
        if(date == null){
            return null;
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DAY_OF_MONTH, interval);
        return cl.getTime();
    }

    /**
     * 将日期增加或减少interval月
     * @param date
     * @param interval
     * @return
     */
    public static Date addIntervalMonth(Date date, int interval){
        if(date == null){
            return null;
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.MONTH, interval);
        return cl.getTime();
    }

    /**
     * 获取离第interval天凌晨相差多少秒
     * @param interval
     * @return
     */
    public static int getSeconds20(int interval){
        Calendar cl = Calendar.getInstance();
        long now = cl.getTimeInMillis();

        cl.add(Calendar.DAY_OF_MONTH, interval);
        cl.set(Calendar.HOUR_OF_DAY, 0);
        cl.set(Calendar.MINUTE, 0);
        cl.set(Calendar.SECOND, 0);
        cl.set(Calendar.MILLISECOND, 0);

        long millis = cl.getTimeInMillis();
        return (int) (millis-now)/1000;
    }

    /**
     * 获取前n个月的每个月日期集
     * @param n
     * @return
     */
    public static List<String> getMonthList(int n){
        SimpleDateFormat sdf = new SimpleDateFormat(default_pattern3);
        List<String> monthList = new ArrayList<>();
        Calendar cal = null;
        for (int i=1; i<=n; i++){
            cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);
            monthList.add(sdf.format(cal.getTime()));
        }
        return monthList;
    }

    /**
     * 获取前n天的每一天日期集
     * @param n
     * @return
     */
    public static List<String> getDayList(int n){
        SimpleDateFormat sdf = new SimpleDateFormat(default_pattern2);
        List<String> dayList = new ArrayList<>();
        Calendar cal = null;
        for(int i=1; i<=n; i++){
            cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -i);
            dayList.add(sdf.format(cal.getTime()));
        }
        return dayList;
    }

    /**
     * 判断dateStr表示的日期是否为月底
     * @param dateStr
     * @param format
     * @return
     */
    public static boolean isLastDayOfMonth(String dateStr, String format){
        if(StringUtils.isBlank(format)){
            format = default_pattern1;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(dateStr));
            cal.add(Calendar.DAY_OF_MONTH, 1);
            return cal.get(Calendar.DAY_OF_MONTH)  == 1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取dateStr表示的日期所在月份的每一天日期集
     * @param dateStr
     * @return
     */
    public static List<String> getDayListOfMonth(String dateStr){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(default_pattern3);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(dateStr));
            int days = cal.getActualMaximum(Calendar.DATE);
            List<String> dayList = new ArrayList<>();
            for (int i=1; i<=days; i++){
                dayList.add(dateStr + "-" + String.format("%02d", i));
            }
            return dayList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取start到end之间的日期集(一)
     * @param start
     * @param end
     * @param format
     * @return
     */
    public static List<String> getBetweenDays(String start, String end, String format){
        Date startDate = str2Date(start, format);
        Date endDate = str2Date(end, format);
        List<String> dateList = new ArrayList<>();
        dateList.add(end);
        while(endDate.getTime() - startDate.getTime() >= millis){
            endDate = new Date(endDate.getTime() - millis);
            dateList.add(date2Str(endDate, format));
        }
        return dateList;
    }

    /**
     * 获取start到end之间的日期集(二)
     * @param start
     * @param end
     * @return
     */
    public static List<String> getBetweenDays(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(default_pattern2);
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(sdf.parse(start));
        max.setTime(sdf.parse(end));
        max.add(Calendar.DAY_OF_YEAR, 1);

        Calendar cal = min;
        List<String> dayList = new ArrayList<>();
        while(cal.before(max)){
            dayList.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return dayList;
    }

    /**
     * 获取start到end之间的月份集
     * @param start
     * @param end
     * @return
     */
    public static List<String> getBetweenMonths(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(default_pattern3);
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(sdf.parse(start));
        max.setTime(sdf.parse(end));
        max.add(Calendar.MONTH, 1);

        Calendar cal = min;
        List<String> dayList = new ArrayList<>();
        while(cal.before(max)){
            dayList.add(sdf.format(cal.getTime()));
            cal.add(Calendar.MONTH, 1);
        }
        return dayList;
    }

    /**
     * 获取dateStr所在的季度起止日期
     * @param dateStr
     * @param format
     * @return
     * @throws ParseException
     */
    public static Map<String, String> getStartAndEndDateForQ(String dateStr, String format) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        cal.setTime(sdf.parse(dateStr));
        int x = cal.get(Calendar.YEAR);
        int y = cal.get(Calendar.MONTH) + 1;
        String start = "";
        String end = "";
        if(y >= 1 && y <= 3){
            start = x + "-" + "01-01";
            end = x + "-" + "03-31";
        }else if(y >=4 && y <= 6){
            start = x + "-" + "04-01";
            end = x + "-" + "06-30";
        }else if(y >= 7 && y <=9){
            start = x + "-" + "07-01";
            end = x + "-" + "09-30";
        }else if(y >= 10 && y <= 12){
            start = x + "-" + "10-01";
            end = x + "-" + "12-31";
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("start", start);
        resultMap.put("end", end);
        return resultMap;
    }

    /**
     * 获取dateStr所在的月份起止日期
     * @param dateStr
     * @param format
     * @return
     * @throws ParseException
     */
    public static Map<String, String> getStartAndEndDateForM(String dateStr, String format) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        cal.setTime(sdf.parse(dateStr));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        String start = sdf.format(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String end = sdf.format(cal.getTime());

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("start", start);
        resultMap.put("end", end);
        return resultMap;
    }

    /**
     * 获取dateStr所在的年份起止日期
     * @param dateStr
     * @param format
     * @return
     * @throws ParseException
     */
    public static Map<String, String> getStartAndEndDateForY(String dateStr, String format) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        cal.setTime(sdf.parse(dateStr));
        int x = cal.get(Calendar.YEAR);
        String start = x + "-01-01";
        String end = x + "-12-31";

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("start", start);
        resultMap.put("end", end);
        return resultMap;
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(str2Date("2019-12-12 12:12:12", null));
        System.out.println(date2Str(new Date(), null));
        System.out.println(str2Str("2019-10", "yyyy-MM", null));
        System.out.println(addIntervalDay(new Date(), 3));
        System.out.println(addIntervalMonth(new Date(), 3));
        System.out.println(getSeconds20(1));
        System.out.println(getMonthList(3));
        System.out.println(getDayList(3));
        System.out.println(getDayList(3));
        System.out.println(getDayListOfMonth("2019-12"));
        System.out.println(getBetweenDays("2019-12-10", "2020-01-01"));
        System.out.println(getBetweenMonths("2019-01", "2020-01"));
        System.out.println(getStartAndEndDateForQ("2019-01", default_pattern3));
        System.out.println(getStartAndEndDateForM("2019-01-03", default_pattern2));
        System.out.println(getStartAndEndDateForY("2019-01-03", default_pattern2));
    }

}
