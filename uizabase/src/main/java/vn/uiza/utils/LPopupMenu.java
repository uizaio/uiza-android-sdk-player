package vn.uiza.utils;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

/**
 * Created by www.muathu@gmail.com on 5/13/2017.
 */

public class LPopupMenu {
    public interface CallBack {
        void clickOnItem(MenuItem menuItem);
    }

    public static void show(final Activity activity, View showOnView, int menuRes, final CallBack callBack) {
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
