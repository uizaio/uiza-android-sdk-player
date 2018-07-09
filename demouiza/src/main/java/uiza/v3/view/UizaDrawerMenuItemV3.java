package uiza.v3.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import uiza.R;
import uiza.v3.data.HomeDataV3;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Click;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Layout;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Resolve;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.View;

/**
 * Created by janisharali on 19/08/16.
 */
@Layout(R.layout.v3_uiza_drawer_item)
public class UizaDrawerMenuItemV3 {
    //@Position
    // private int mMenuPosition;

    private Context mContext;

    @View(R.id.tv_name)
    private TextView tvName;

    @View(R.id.mainView)
    private LinearLayout mainView;

    @View(R.id.iv_press)
    private ImageView ivPress;

    private List<Data> dataList;
    private int mPos;
    private Callback mCallback;

    public UizaDrawerMenuItemV3(Context context, List<Data> dataList, int pos, Callback callback) {
        this.mContext = context;
        this.dataList = dataList;
        this.mPos = pos;
        this.mCallback = callback;
    }

    @Resolve
    private void onResolved() {
        tvName.setText(dataList.get(mPos).getName());
        if (mPos == HomeDataV3.getInstance().getCurrentPosition()) {
            ivPress.setVisibility(android.view.View.VISIBLE);
            mainView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.drawer_hightlight));
        } else {
            ivPress.setVisibility(android.view.View.INVISIBLE);
            mainView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Click(R.id.mainView)
    private void onMenuItemClick() {
        if (mCallback != null) {
            mCallback.onMenuItemClick(mPos);
        }
    }

    public interface Callback {
        public void onMenuItemClick(int pos);
    }
}