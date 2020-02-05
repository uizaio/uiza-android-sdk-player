package uizacoresdk.widget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.annotation.NonNull;

public class WidgetUtils {

    private static final String COPY_LABEL = "Copy";

    private WidgetUtils() {
    }

    public static void setClipboard(@NonNull Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(COPY_LABEL, text);
        clipboard.setPrimaryClip(clip);
        LToast.show(context, "Copied!");
    }
}
