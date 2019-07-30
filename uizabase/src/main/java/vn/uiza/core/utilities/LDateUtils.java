package vn.uiza.core.utilities;

import android.text.TextUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import vn.uiza.core.common.Constants;
import vn.uiza.utils.util.SentryUtils;

public class LDateUtils {

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

    public static String getCurrent(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(c.getTime());
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

    public static long convertDateToTimeStamp(String datetime, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
        Date date;
        try {
            date = dateFormat.parse(datetime);
            return date.getTime();
        } catch (ParseException e) {
            SentryUtils.captureException(e);
            return Constants.NOT_FOUND;
        }
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

    public static String convertMillisecondsToHMmSs(long mls) {
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
