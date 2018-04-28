package uiza.activity.home.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.daimajia.androidanimations.library.Techniques;

import uiza.R;
import uiza.activity.home.model.Item;
import vn.loitp.core.utilities.LAnimationUtil;
import vn.loitp.core.utilities.LImageUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.views.placeholderview.lib.placeholderview.Animation;
import vn.loitp.views.placeholderview.lib.placeholderview.PlaceHolderView;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Animate;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Click;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Layout;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.NonReusable;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Resolve;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.View;

/**
 * Created by www.muathu@gmail.com on 9/16/2017.
 */

//@Animate(Animation.CARD_TOP_IN_DESC)
@Animate(Animation.CARD_BOTTOM_IN_ASC)
@NonReusable
@Layout(R.layout.uiza_channel_item)
public class ChannelItem {

    @View(R.id.imageView)
    private ImageView imageView;
    @View(R.id.pb)
    private ProgressBar progressBar;

    private Item item;
    private Context mContext;
    private PlaceHolderView mPlaceHolderView;
    private Callback mCallback;
    private int mPosition;

    public ChannelItem(Context context, PlaceHolderView placeHolderView, Item item, int position, Callback callback) {
        mContext = context;
        mPlaceHolderView = placeHolderView;
        this.item = item;
        mPosition = position;
        mCallback = callback;
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(mContext, R.color.White));
    }

    @Resolve
    private void onResolved() {
        String[] urls = new String[2];
        urls[0] = item.getPoster();
        urls[1] = item.getThumbnail();
        LImageUtil.load((Activity) mContext, urls, imageView, progressBar);
    }

    /*@LongClick(R.id.imageView)
    private void onLongClick() {
        mPlaceHolderView.removeView(this);
    }*/

    @Click(R.id.imageView)
    private void onClick() {
        LAnimationUtil.play(imageView, Techniques.Pulse, new LAnimationUtil.Callback() {
            @Override
            public void onCancel() {
                //do nothing
            }

            @Override
            public void onEnd() {
                if (mCallback != null) {
                    mCallback.onClick(item, mPosition);
                }
            }

            @Override
            public void onRepeat() {
                //do nothing
            }

            @Override
            public void onStart() {
                //do nothing
            }
        });
    }

    public interface Callback {
        public void onClick(Item item, int position);
    }
}