package vn.uiza.utils;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;

/**
 * Created by www.muathu@gmail.com on 5/13/2017.
 */

public class LPopupMenu {
    public interface CallBack {
        void clickOnItem(MenuItem menuItem);
    }

    public static void show(@NonNull Context context, View showOnView, int menuRes, final CallBack callBack) {
        PopupMenu popup = new PopupMenu(context, showOnView);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            if (callBack != null) {
                callBack.clickOnItem(menuItem);
            }
            return true;
        });
        popup.show();
    }
}
