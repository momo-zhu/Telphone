package cn.zicoo.ir2teledemo.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Tom
 */
public class DateUtils {
    private StringBuffer buffer = new StringBuffer();
    private static DateUtils date;
    public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat format1 = new SimpleDateFormat(
            "yyyyMMdd HH:mm:ss");
    public static SimpleDateFormat common_format = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dayFormat = new SimpleDateFormat(
            "yyyy-MM-dd");
    public final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    public final static SimpleDateFormat sdfMS = new SimpleDateFormat("mm:ss");
    public final static SimpleDateFormat sdfMinute = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public final static SimpleDateFormat sdfHM = new SimpleDateFormat("HH:mm");

    public String getNowString() {
        Calendar calendar = getCalendar();
        buffer.delete(0, buffer.capacity());
        buffer.append(getYear(calendar));

        String ZERO = "0";
        if (getMonth(calendar) < 10) {
            buffer.append(ZERO);
        }
        buffer.append(getMonth(calendar));

        if (getDate(calendar) < 10) {
            buffer.append(ZERO);
        }
        buffer.append(getDate(calendar));
        if (getHour(calendar) < 10) {
            buffer.append(ZERO);
        }
        buffer.append(getHour(calendar));
        if (getMinute(calendar) < 10) {
            buffer.append(ZERO);
        }
        buffer.append(getMinute(calendar));
        if (getSecond(calendar) < 10) {
            buffer.append(ZERO);
        }
        buffer.append(getSecond(calendar));
        return buffer.toString();
    }

    private static int getDateField(Date date, int field) {
        Calendar c = getCalendar();
        c.setTime(date);
        return c.get(field);
    }

    public static int getYearsBetweenDate(Date begin, Date end) {
        int bYear = getDateField(begin, Calendar.YEAR);
        int eYear = getDateField(end, Calendar.YEAR);
        return eYear - bYear;
    }

    public static int getMonthsBetweenDate(Date begin, Date end) {
        int bMonth = getDateField(begin, Calendar.MONTH);
        int eMonth = getDateField(end, Calendar.MONTH);
        return eMonth - bMonth;
    }

    public static int getWeeksBetweenDate(Date begin, Date end) {
        int bWeek = getDateField(begin, Calendar.WEEK_OF_YEAR);
        int eWeek = getDateField(end, Calendar.WEEK_OF_YEAR);
        return eWeek - bWeek;
    }

    public static int getDaysBetweenDate(Date begin, Date end) {
        return (int) ((end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24));
    }


    /**
     * @return
     */
    public static Date getDistanceDate(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, days);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 获取date年后的amount年的第一天的开始时间
     *
     * @param amount 可正、可负
     * @return
     */
    public static Date getSpecficYearStart(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, amount);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return getStartDate(cal.getTime());
    }

    /**
     * 获取date年后的amount年的第一天的开始时间
     *
     * @param amount 可正、可负
     * @return
     */
    public static Date getSpecficYear(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, amount);
        return getStartDate(cal.getTime());
    }

    /**
     * 获取date年后的amount年的最后一天的终止时间
     *
     * @param amount 可正、可负
     * @return
     */
    public static Date getSpecficYearEnd(Date date, int amount) {
        Date temp = getStartDate(getSpecficYearStart(date, amount + 1));
        Calendar cal = Calendar.getInstance();
        cal.setTime(temp);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return getFinallyDate(cal.getTime());
    }

    /**
     * 获取date月后的amount月的第一天的开始时间
     *
     * @param amount 可正、可负
     * @return
     */
    public static Date getSpecficMonthStart(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, amount);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getStartDate(cal.getTime());
    }

    /**
     * 获取当前自然月后的amount月的最后一天的终止时间
     *
     * @param amount 可正、可负
     * @return
     */
    public static Date getSpecficMonthEnd(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getSpecficMonthStart(date, amount + 1));
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return getFinallyDate(cal.getTime());
    }

    /**
     * 获取date周后的第amount周的开始时间（这里星期一为一周的开始）
     *
     * @param amount 可正、可负
     * @return
     */
    public static Date getSpecficWeekStart(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY); /* 设置一周的第一天为星期一 */
        cal.add(Calendar.WEEK_OF_MONTH, amount);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getStartDate(cal.getTime());
    }

    /**
     * 获取date周后的第amount周的最后时间（这里星期日为一周的最后一天）
     *
     * @param amount 可正、可负
     * @return
     */
    public static Date getSpecficWeekEnd(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY); /* 设置一周的第一天为星期一 */
        cal.add(Calendar.WEEK_OF_MONTH, amount);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getFinallyDate(cal.getTime());
    }

    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date getSpecficDateStart(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, amount);
        return getStartDate(cal.getTime());
    }

    public static Date otherToDate(Object obj) {
        if (obj instanceof Number)
            return new Date(((Number) obj).longValue());
        if (obj instanceof Date) return (Date) obj;
        if (obj instanceof Calendar) return ((Calendar) obj).getTime();
        return null;
    }

    public static String otherToString(Object obj) {
        Date date = otherToDate(obj);
        if (date == null) return null;
        return dayFormat.format(date);
    }

    public static Date getSpecficDateEnd(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, amount);
        return getFinallyDate(cal.getTime());
    }

    /**
     * 得到指定日期的一天的的最后时刻23:59:59
     *
     * @param date
     * @return
     */
    public static Date getFinallyDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        calendar.setTime(date);
        end.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        end.set(Calendar.MILLISECOND, 0);
        return end.getTime();
    }

    /**
     * 得到指定日期的一天的开始时刻00:00:00
     *
     * @param date
     * @return
     */
    public static Date getStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        calendar.setTime(date);
        start.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTime();
    }


    /**
     * @param date        指定比较日期
     * @param compareDate
     * @return
     */
    public static boolean isInDate(Date date, Date compareDate) {
        return compareDate.after(getStartDate(date))
                && compareDate.before(getFinallyDate(date));

    }

    /**
     * 获取两个时间的差值秒
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Integer getSecondBetweenDate(Date d1, Date d2) {
        Long second = (d2.getTime() - d1.getTime()) / 1000;
        return second.intValue();
    }

    private int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    private int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    private int getDate(Calendar calendar) {
        return calendar.get(Calendar.DATE);
    }

    private int getHour(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private int getMinute(Calendar calendar) {
        return calendar.get(Calendar.MINUTE);
    }

    private int getSecond(Calendar calendar) {
        return calendar.get(Calendar.SECOND);
    }

    private static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static Date parseDayStrToDate(String dateStr) {
        try {
            return dayFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(Object date) {
        if (date == null) return null;
        if (date instanceof Number)
            return new Date(((Number) date).longValue());
        else if (date instanceof String) {
            String s = ((String) date).replace('T', ' ');
            try {
                switch (s.length()) {
                    case 10:
                        return dayFormat.parse(s);
                    case 16:
                        return sdfMinute.parse(s);
                    case 19:
                        return common_format.parse(s);
                    default:
                        return null;
                }
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    public static String parseDateToTimeStr(Date date) {
        return common_format.format(date);
    }

    public static String parseDateToDateStr(Date date) {
        return dayFormat.format(date);
    }


    public static DateUtils getDateInstance() {
        if (date == null) {
            date = new DateUtils();
        }
        return date;
    }
}
