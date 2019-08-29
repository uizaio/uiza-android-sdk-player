package com.uiza.demo.v4.live.broadcast;

import android.app.Activity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

public class LPopupMenu {

    public interface CallBack {

        void clickOnItem(MenuItem menuItem);
    }

    public static void show(final Activity activity, View showOnView, int menuRes,
            final CallBack callBack) {
        PopupMenu popup = new PopupMenu(activity, showOnView);
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