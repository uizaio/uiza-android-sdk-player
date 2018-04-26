package vn.loitp.core.utilities;

import android.content.Context;

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

import vn.loitp.core.common.Constants;

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

    public static String convertFormatDate(String strDate, String fromFormat, String toFormat) {
        Date date = stringToDate(strDate, fromFormat);
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toFormat, Locale.ENGLISH);
            //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            DateFormat dateFormat = simpleDateFormat;
            return dateFormat.format(date);
        }
        return null;
    }

    public static Date stringToDate(String text, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            return dateFormat.parse(text);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToString(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String dateToString(Date date, Context context) {
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
        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getTime(int hr, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        Format formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(cal.getTime());
    }

    public static String getCurrent(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(c.getTime());
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("ddMM_HHmm");
        return df.format(c.getTime());
    }

    public static String getCurrentYearMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(c.getTime());
    }

    public static String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM");
        return df.format(c.getTime());
    }

    /*public static void getListDayOfMonth(int month, int year) {
        String TAG = "@@";
        LLog.d(TAG, ">>>>>>>>>>month: " + month);
        List<LSchedule> lScheduleArrayList = new ArrayList<>();
        if (month >= Calendar.JANUARY && month <= Calendar.DECEMBER) {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            Calendar cal = Calendar.getInstance(timeZone);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.YEAR, year);
            SimpleDateFormat dfShort = new SimpleDateFormat("EEE*dd/MM", Locale.getDefault());
            SimpleDateFormat dfLong = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            while (month == cal.get(Calendar.MONTH)) {
                LSchedule lSchedule = new LSchedule();
                //LLog.d(TAG, "genListDayOfMonth>>> " + dfLong.format(cal.getTime()));

                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                lSchedule.setTimestamp(cal.getTimeInMillis());
                lSchedule.setDayShort(dfShort.format(cal.getTime()).replace("*", "\n"));
                lSchedule.setDayLong(dfLong.format(cal.getTime()).replace("*", "\n"));
                lScheduleArrayList.add(lSchedule);

                cal.add(Calendar.DAY_OF_MONTH, 1);//important line
            }
        }

        if (!lScheduleArrayList.isEmpty()) {
            for (int i = 0; i < lScheduleArrayList.size(); i++) {
                if (DateUtils.isToday(lScheduleArrayList.get(i).getTimestamp())) {
                    //LLog.d(TAG, lScheduleArrayList.get(i).getDayLong() + " isToday");
                    LSApplication.getInstance().setTodayTimestamp(lScheduleArrayList.get(i).getTimestamp());
                } else {
                    //LLog.d(TAG, lScheduleArrayList.get(i).getDayLong() + " !isToday");
                }
            }
        }

        return lScheduleArrayList;
    }*/

    /*public static List<LSchedule> genListDayOfMonth(int month, int year) {
        LLog.d(TAG, ">>>>>>>>>>month: " + month + ", year: " + year);
        List<LSchedule> lScheduleArrayList = new ArrayList<>();
        if (month >= Calendar.JANUARY && month <= Calendar.DECEMBER) {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            Calendar cal = Calendar.getInstance(timeZone);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);
            SimpleDateFormat dfShort = new SimpleDateFormat("EEE*dd/MM", Locale.getDefault());
            SimpleDateFormat dfLong = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            LLog.d(TAG, "cal.get(Calendar.MONTH): " + cal.get(Calendar.MONTH));
            while (month == cal.get(Calendar.MONTH)) {
                LSchedule lSchedule = new LSchedule();
                LLog.d(TAG, "genListDayOfMonth>>> " + dfLong.format(cal.getTime()));

                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                lSchedule.setTimestamp(cal.getTimeInMillis());
                lSchedule.setDayShort(dfShort.format(cal.getTime()).replace("*", "\n"));
                lSchedule.setDayLong(dfLong.format(cal.getTime()).replace("*", "\n"));
                lScheduleArrayList.add(lSchedule);

                cal.add(Calendar.DAY_OF_MONTH, 1);//important line
            }
        } else {
            LLog.d(TAG, "else");
        }

        if (!lScheduleArrayList.isEmpty()) {
            for (int i = 0; i < lScheduleArrayList.size(); i++) {
                if (DateUtils.isToday(lScheduleArrayList.get(i).getTimestamp())) {
                    //LLog.d(TAG, lScheduleArrayList.get(i).getDayLong() + " isToday");
                    LSApplication.getInstance().setTodayTimestamp(lScheduleArrayList.get(i).getTimestamp());
                } else {
                    //LLog.d(TAG, lScheduleArrayList.get(i).getDayLong() + " !isToday");
                }
            }
        }

        return lScheduleArrayList;
    }*/

    public static String getDate(String dateString) {
        return getDate(dateString, "dd/MM/yyyy hh:mm aa");
    }

    public static String getDateWithoutTime(String dateString) {
        return getDate(dateString, "dd/MM/yyyy");
    }

    public static String getDate(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.ENGLISH);
        dateFormatter.setTimeZone(TimeZone.getDefault());
        return dateFormatter.format(value);
    }

    //date ex: 14-09-2017
    public static long convertDateToTimestamp(String d) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(d);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            LLog.d(TAG, "convertDateToTimestamp ParseException " + e.toString());
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

    /*private static boolean isSetAlarm(long serverTimestamp, ArrayList<Alarm> alarmArrayList) {
        if (alarmArrayList == null) {
            return false;
        }
        for (int i = 0; i < alarmArrayList.size(); i++) {
            if (serverTimestamp == alarmArrayList.get(i).getDate()) {
                return true;
            }
        }
        return false;
    }*/

    //input 2017-09-13T07:11:00.000Z
    //output [02:11, PM]
    /*public static LDate convertToAMorPM(String date, ArrayList<Alarm> alarmArrayList) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date valueDate = null;
        try {
            valueDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        dateFormatter.setTimeZone(TimeZone.getDefault());
        //dateFormatter.setTimeZone(Locale.ENGLISH);
        String tmp = dateFormatter.format(valueDate);
        //LLog.d(TAG, ">>>tmp " + tmp);
        String arr[] = tmp.split(" ");
        LDate lDate = new LDate();
        lDate.setHhmm(arr[0]);
        lDate.setAmOrPm(arr[1]);

        long serverTimestamp = valueDate.getTime();

        lDate.setIsSetAlarm(isSetAlarm(serverTimestamp, alarmArrayList));

        lDate.setTimestamp(serverTimestamp);
        Long currentTimestamp = System.currentTimeMillis();
        lDate.setToday(DateUtils.isToday(serverTimestamp));

        if (serverTimestamp > currentTimestamp) {
            lDate.setTimeInPast(false);
            //LLog.d(TAG, "server timestamp " + serverTimestamp + ", current timestamp " + currentTimestamp + " >>> setTimeInPast false, " + DateUtils.isToday(serverTimestamp));
        } else {
            lDate.setTimeInPast(true);
            //LLog.d(TAG, "server timestamp " + serverTimestamp + ", current timestamp " + currentTimestamp + " >>> setTimeInPast true, " + DateUtils.isToday(serverTimestamp));
        }

        return lDate;
    }*/

    private static long convertDateToTimeStamp(String d, int h, int m) {
        //String lstart = dd + "/" + mm + "/" + yyyy + " " + h + ":" + m;
        String lstart = d + " " + h + ":" + m;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        try {
            Date date = format.parse(lstart);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long convertDateToTimeStamp(String datetime, String format) {
        //LLog.d(TAG, "convertDateToTimeStamp datetime " + datetime);
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(datetime);
            long time = date.getTime();
            LLog.d(TAG, "time:" + time);
            return time;
        } catch (ParseException e) {
            LLog.e(TAG, "convertDateToTimeStamp " + e.toString());
            return Constants.NOT_FOUND;
        }
    }

    public static String getDateFromDateTime(String datetime) {
        String[] date = datetime.split(" ");
        return date[0];
    }

    public static Calendar convertStringToCalendar(String yyyymmdd) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = df.parse(yyyymmdd);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static Calendar convertStringDate(String yyyymmdd, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = df.parse(yyyymmdd);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }
}
