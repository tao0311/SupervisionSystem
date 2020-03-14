package com.chtc.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {
	
	public static final String COMMON_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String COMMON_DATE_FORMAT = "yyyy-MM-dd";
	
	public static final String COMMON_TIME_FORMAT = "HH:mm:ss";
	
	public static final String COMMON_DATE_TIME_INDEX_FORMAT = "yyyyMMddHHmmss";
	
	public static final String COMMON_DATE_INDEX_FORMAT = "yyyyMMdd";
	
	public static final String COMMON_TIME_INDEX_FORMAT = "HHmmss";
	
	public static final SimpleDateFormat commonDateTimeFormatter = new SimpleDateFormat(COMMON_DATE_TIME_FORMAT);
	
	public static final SimpleDateFormat commonDateFormatter = new SimpleDateFormat(COMMON_DATE_FORMAT);
	
	public static final SimpleDateFormat commonTimeFormatter = new SimpleDateFormat(COMMON_TIME_FORMAT);
	
	public static final SimpleDateFormat commonDateTimeIndexFormatter = new SimpleDateFormat(COMMON_DATE_TIME_INDEX_FORMAT);
	
	public static final SimpleDateFormat commonDateIndexFormatter = new SimpleDateFormat(COMMON_DATE_INDEX_FORMAT);
	
	public static final SimpleDateFormat commonTimeIndexFormatter = new SimpleDateFormat(COMMON_TIME_INDEX_FORMAT);

	/**
     * 获取现在时间
     * 
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateTime() {
        Date currentTime = new Date();
        return commonDateTimeFormatter.format(currentTime);
    }

    public static Date formatString2Date(String stringDate, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(stringDate);
    }

    public static Date formatString2Date(String stringDate, String format, Locale locale) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.parse(stringDate);
    }

    public static String formatDate2String(Date date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String formatDate2String(Date date, String format, Locale locale) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(date);
    }

    /**
     * 获取现在时间
     * 
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDate() {
    	Date currentTime = new Date();
        return commonDateFormatter.format(currentTime);
    }

    /**
     * 获取时间 小时:分:秒 HH:mm:ss
     * 
     * @return
     */
    public static String getStringTime() {
    	Date currentTime = new Date();
        return commonTimeFormatter.format(currentTime);
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     * 
     * @param strDate
     * @return
     */
    public static Date stringToDateTime(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        return commonDateTimeFormatter.parse(strDate, pos);
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     * 
     * @param dateTime
     * @return
     */
    public static String dateTimeToString(Date dateTime) {
        if (dateTime == null) {
            return "";
        }
        return commonDateTimeFormatter.format(dateTime);
    }

    public static String dateTimeToString(Date dateTime, String format) {
        SimpleDateFormat format2 = new SimpleDateFormat(format);
        if (dateTime == null) {
            return "";
        }
        return format2.format(dateTime);
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     * 
     * @param dateDate
     * @param
     * @return
     */
    public static String dateToString(Date dateDate) {
    	return commonDateFormatter.format(dateDate);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     * 
     * @param strDate
     * @return
     */
    public static Date stringToDate(String strDate) {
    	ParsePosition pos = new ParsePosition(0);
        return commonDateFormatter.parse(strDate, pos);
    }
    
    /**
     * 得到天数
     */
    public static int getDay(Date dateTime) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(dateTime);
    	return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到24小时制的小时数
     */
    public static int getHour(Date dateTime) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(dateTime);
    	return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 得到分钟数
     * 
     * @return
     */
    public static int getMinute(Date dateTime) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(dateTime);
    	return calendar.get(Calendar.MINUTE);
    }
    
    /**
     * 得到秒数
     * 
     * @return
     */
    public static int getSecond(Date dateTime) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(dateTime);
    	return calendar.get(Calendar.SECOND);
    }

    /**
     * 判断是否润年
     * 
     * @param strDate
     * @return
     */
    public static boolean isLeapYear(String strDate) {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        Date d = stringToDate(strDate);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(Calendar.YEAR);
        if ((year % 400) == 0)
            return true;
        else if ((year % 4) == 0) {
            if ((year % 100) == 0)
                return false;
            else
                return true;
        } else
            return false;
    }

    /**
     * 获取一个月的第一天
     * 
     * @param dat
     * @return
     */
    public static String getStringFirstDateOfMonth(String dat) {// yyyy-MM-dd
        String str = dat.substring(0, 8);
        str += "01";
        return str;
    }
    
    /**
     * 获取一个月的最后一天
     * 
     * @param dat
     * @return
     */
    public static String getStringLastDateOfMonth(String dat) {// yyyy-MM-dd
    	Date d = stringToDate(dat);
    	d = getLastDateOfMonth(d);
    	return commonDateFormatter.format(d);
    }
    
    /**
     * 获取一个月的第一天
     * 
     * @param dat
     * @return
     */
    public static Date getFirstDateOfMonth(String dat) {// yyyy-MM-dd
    	Date dateTime = stringToDate(dat);
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(dateTime);
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
    
    /**
     * 获取一个月的最后一天
     * 
     * @param dat
     * @return
     */
    public static Date getLastDateOfMonth(String dat) {// yyyy-MM-dd
    	Date dateTime = stringToDate(dat);
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(dateTime);
        calendar.set(Calendar.DATE, 1);// 设为当前月的1号
        calendar.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        calendar.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        return calendar.getTime();
    }
    
    /**
     * 获取一个月的第一天
     * 
     * @param
     * @return
     */
    public static Date getFirstDateOfMonth(Date dateTime) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(dateTime);
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * 获取一个月的最后一天
     * 
     * @param
     * @return
     */
    public static Date getLastDateOfMonth(Date dateTime) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(dateTime);
        calendar.set(Calendar.DATE, 1);// 设为当前月的1号
        calendar.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        calendar.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(calendar.MINUTE, 59);
        calendar.set(calendar.SECOND, 59);
        calendar.set(calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    /**
     * 得到一个时间延后或前移几天的时间，nowdate为时间，delay为前移或后延的天数
     */
    public static String getStringNextDay(String nowdate, int delay) {
        try {
            String mdate = "";
            Date d = stringToDate(nowdate);
            long myTime = (d.getTime() / 1000) + delay * 24
                    * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = commonDateFormatter.format(d);
            return mdate;
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * 得到一个时间延后或前移几天的时间，nowdate为时间，delay为前移或后延的天数
     */
    public static Date getDateNextDay(String nowdate, int delay) {
    	Date d = stringToDate(nowdate);
        long myTime = (d.getTime() / 1000) + delay * 24
                * 60 * 60;
        d.setTime(myTime * 1000);
        return d;
    }
    
    /**
     * 得到一个时间延后或前移几天的时间，data为时间，delay为前移或后延的天数
     */
    public static String getStringNextDay(Date data, int delay) {
        try {
            String mdate = "";
            Date d = data;
            long myTime = (d.getTime() / 1000) + delay * 24
                    * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = commonDateFormatter.format(d);
            return mdate;
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * 得到一个时间延后或前移几天的时间，data为时间，delay为前移或后延的天数
     */
    public static Date getDateNextDay(Date data, int delay) {
    	Date d = data;
        long myTime = (d.getTime() / 1000) + delay * 24
                * 60 * 60;
        d.setTime(myTime * 1000);
        return d;
    }

    /**
     * 判断二个时间是否在同一个周
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    /**
     * 产生周序列,即得到当前时间所在的年度是第几周
     * 
     * @return
     */
    public static String getSeqWeek() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1)
            week = "0" + week;
        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }

    /**
     ** 将日期转换为星期
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 得到下一分钟或几分钟的时间
     * @param delay
     * @return
     */
    public static Date getDateNextMinute( int delay) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE,delay);
        return now.getTime();
    }
}