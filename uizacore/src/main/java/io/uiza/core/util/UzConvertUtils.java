package io.uiza.core.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class UzConvertUtils {

    private UzConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int dp2px(float dpValue) {
        final float scale = UzCoreUtil.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(float pxValue) {
        final float scale = UzCoreUtil.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getFormattedDouble(double value, int precision) {
        return new DecimalFormat(
                "#0." + (precision <= 1 ? "0" : precision == 2 ? "00" : "000")).format(value);
    }

    public static String groupingSeparatorLong(long value) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("###,###", decimalFormatSymbols);
        return decimalFormat.format(value);
    }

    public static String humanReadableByteCount(long bytes, boolean si, boolean isBits) {
        int unit = !si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " KB";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return isBits ? String
                .format(Locale.getDefault(), "%.1f %sb", bytes / Math.pow(unit, exp), pre)
                : String.format(Locale.getDefault(), "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
