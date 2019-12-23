package testlibuiza.sample.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SampleUtils {

    private static final String SHARED_PREFERENCES = "UizaSampleAppV5";
    private static final String PREFERENCES_USER_NAME = "username";
    private static final String PREFERENCES_USER_EMAIL = "email";
    private static final String PREFERENCES_USER_ID = "id";
    private static final String DEFAULT_USER = "User";

    public static final String DBNAME = "uizachatlive";

    private SampleUtils() {
    }

    public static void setVertical(RecyclerView recyclerView) {
        attachLayout(recyclerView, LinearLayout.VERTICAL, 1, false);
    }

    public static void setVertical(RecyclerView recyclerView, int spanCount) {
        attachLayout(recyclerView, LinearLayout.VERTICAL, spanCount, false);
    }

    public static void setVertical(RecyclerView recyclerView, int spanCount, boolean reverse) {
        attachLayout(recyclerView, LinearLayout.VERTICAL, spanCount, reverse);
    }

    public static void setHorizontal(RecyclerView recyclerView, int spanCount, boolean reverse) {
        attachLayout(recyclerView, LinearLayout.HORIZONTAL, spanCount, reverse);
    }

    private static void attachLayout(RecyclerView recyclerView, int orientation, int spanCount, boolean reverse) {
        if (spanCount == 1)
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), orientation, reverse));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount, orientation, reverse));
        recyclerView.setHasFixedSize(true);
    }

    public static void scrollToTop(RecyclerView recyclerView, int elementNum) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int offset = (((LinearLayoutManager) layoutManager).getOrientation() == RecyclerView.VERTICAL) ? recyclerView.getHeight() : recyclerView.getWidth();
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(elementNum, offset);
        }
        if (layoutManager instanceof GridLayoutManager) {
            int offset = (((GridLayoutManager) layoutManager).getOrientation() == RecyclerView.VERTICAL) ? recyclerView.getHeight() : recyclerView.getWidth();
            ((GridLayoutManager) layoutManager).scrollToPositionWithOffset(elementNum, offset);
        }
    }

    public static void closeKeyboard(Context context, View view) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void saveLocalUser(Context context, String email, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(PREFERENCES_USER_NAME, email.split("@")[0])
                .putString(PREFERENCES_USER_EMAIL, email)
                .putString(PREFERENCES_USER_ID, id)
                .apply();

    }

    public static String getLocalUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREFERENCES_USER_NAME, DEFAULT_USER);
    }

    public static String getLocalUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREFERENCES_USER_ID, "");
    }
}
