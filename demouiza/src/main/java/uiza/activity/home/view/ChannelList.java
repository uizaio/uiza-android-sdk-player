package uiza.activity.home.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import java.util.List;

import uiza.R;
import uiza.activity.home.model.ChannelObject;
import uiza.activity.home.model.Item;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.views.placeholderview.lib.placeholderview.PlaceHolderView;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Layout;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.NonReusable;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Resolve;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.View;

/**
 * Created by www.muathu@gmail.com on 9/16/2017.
 */

//@Animate(Animation.CARD_TOP_IN_DESC)
@NonReusable
@Layout(R.layout.uiza_channel_list)
public class ChannelList {

    @View(R.id.placeholderview)
    private PlaceHolderView mPlaceHolderView;

    @View(R.id.tv_channel_name)
    private TextView tvChannelName;

    private Context mContext;
    private ChannelObject mChannelObject;

    private ChannelItem.Callback mCallback;

    public ChannelList(Context context, ChannelObject channelObject, ChannelItem.Callback callback) {
        mContext = context;
        mChannelObject = channelObject;
        mCallback = callback;
    }

    @Resolve
    private void onResolved() {
        tvChannelName.setText(mChannelObject.getChannelName());

        mPlaceHolderView.getBuilder()
                .setHasFixedSize(false)
                .setItemViewCacheSize(10)
                .setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        LUIUtil.setPullLikeIOSHorizontal(mPlaceHolderView);

        List<Item> itemList = mChannelObject.getItemList();
        for (int i = 0; i < itemList.size(); i++) {
            mPlaceHolderView.addView(new ChannelItem(mContext, mPlaceHolderView, itemList.get(i), i, mCallback));
        }
    }
}