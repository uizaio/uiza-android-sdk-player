package vn.uiza.core.utilities;

import android.text.TextUtils;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import vn.uiza.core.common.Constants;
import vn.uiza.utils.util.SentryUtils;

/**
 * @author Khanh Le
 * @version 1.0.0
 * @since 6/4/2015
 */
public class LDateUtils {
    private static final String TAG = LDateUtils.class.getSimpleName();
    public final static String FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public final static String FORMAT_2 = "dd/MM/yyyy";
    public final static String FORMAT_3 = "dd/MM/yyyy HH:mm:ss";
    public final static String FORMAT_4 = "h:mm a";
    public static final String FORMAT_5 = "ddMM_HHmm";
    public static final String FORMAT_6 = "yyyy-MM";
    public static final String FORMAT_7 = "MM";
    public static final String FORMAT_8 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_9 = "dd-MM-yyyy";
    public static final String FORMAT_10 = "dd/MM/yyyy hh:mm";
    public static final String FORMAT_11 = "yyyy-MM-dd";
    public static final String FORMAT_12 = "%d:%02d";
    public static final String FORMAT_13 = "%d:%02d:%02d";
    public static final String FORMAT_14 = "dd/MM/yyyy hh:mm aa";
    public static final String UTC = "UTC";

    public static String convertFormatDate(String strDate, String fromFormat, String toFormat) {
        Date date = stringToDate(strDate, fromFormat);
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat(toFormat, Locale.ENGLISH);
            return dateFormat.format(date);
        }
        return null;
    }

    public static Date stringToDate(String text, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            return dateFormat.parse(text);
        } catch (ParseException e) {
            SentryUtils.captureException(e);
            return null;
        }
    }

    public static String dateToString(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            SentryUtils.captureException(e);
            return null;
        }
    }

    public static String dateToString(Date date) {
        return dateToString(date, FORMAT_2);
    }

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public static String formatDatePicker(int year, int month, int day, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getTime(int hr, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        Format formatter = new SimpleDateFormat(FORMAT_4);
        return formatter.format(cal.getTime());
    }

    public static String getCurrent(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(c.getTime());
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_5);
        return df.format(c.getTime());
    }

    public static String getCurrentYearMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_6);
        return df.format(c.getTime());
    }

    public static String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_7);
        return df.format(c.getTime());
    }

    public static String getDate(String dateString) {
        return getDate(dateString, FORMAT_14);
    }

    public static String getDateWithoutTime(String dateString) {
        return getDate(dateString, FORMAT_2);
    }

    public static String getDate(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_8);
        formatter.setTimeZone(TimeZone.getTimeZone(UTC));
        Date value = null;
        try {
            value = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            SentryUtils.captureException(e);
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.ENGLISH);
        dateFormatter.setTimeZone(TimeZone.getDefault());
        return dateFormatter.format(value);
    }

    //date ex: 14-09-2017
    public static long convertDateToTimestamp(String d) {
        DateFormat formatter = new SimpleDateFormat(FORMAT_9);
        Date date;
        try {
            date = formatter.parse(d);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            SentryUtils.captureException(e);
            return Constants.NOT_FOUND;
        }
    }

    public static String convertTimestampToDate(long timestamp) {
        Timestamp time = new Timestamp(timestamp);
        Date date = new Date(time.getTime());
        return dateToString(date, FORMAT_3);
    }

    public static Date zeroTime(final Date date) {
        return setTime(date, 0, 0, 0, 0);
    }

    public static Date setTime(final Date date, final int hourOfDay, final int minute, final int second, final int ms) {
        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(Calendar.HOUR_OF_DAY, hourOfDay);
        gc.set(Calendar.MINUTE, minute);
        gc.set(Calendar.SECOND, second);
        gc.set(Calendar.MILLISECOND, ms);
        return gc.getTime();
    }

    private static long convertDateToTimeStamp(String d, int h, int m) {
        String lstart = d + " " + h + ":" + m;
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_10);
        try {
            Date date = format.parse(lstart);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            SentryUtils.captureException(e);
            return 0;
        }
    }

    public static long convertDateToTimeStamp(String datetime, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
        Date date;
        try {
            date = dateFormat.parse(datetime);
            long time = date.getTime();
            return time;
        } catch (ParseException e) {
            SentryUtils.captureException(e);
            return Constants.NOT_FOUND;
        }
    }

    public static String getDateFromDateTime(String datetime) {
        String[] date = datetime.split(" ");
        return date[0];
    }

    public static Calendar convertStringToCalendar(String yyyymmdd) {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_11);
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = df.parse(yyyymmdd);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            SentryUtils.captureException(e);
        }
        return cal;
    }

    public static Calendar convertStringDate(String yyyymmdd, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = df.parse(yyyymmdd);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            SentryUtils.captureException(e);
        }
        return cal;
    }

    public static String convertSecondsToHMmSs(long seconds) {
        if (seconds <= 0) {
            return "0:00";
        }
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        if (h == 0) {
            return String.format(FORMAT_12, m, s);
        } else {
            return String.format(FORMAT_13, h, m, s);
        }
    }

    public static String convertMlsecondsToHMmSs(long mls) {
        return convertSecondsToHMmSs(mls / 1000);
    }

    /**
     * Convert UTC time string to long value
     * @param timeStr the time with format <code>yyyy-MM-dd'T'HH:mm:ss.SSS'Z'</code>
     * @return UTC time as long value
     */
    public static long convertUTCMs(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) return -1;
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_1);
        dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
        try {
            Date date = dateFormat.parse(timeStr);
            return date == null ? -1 : date.getTime();
        } catch (ParseException e) {
            SentryUtils.captureException(e);
            return -1;
        }
    }
}
