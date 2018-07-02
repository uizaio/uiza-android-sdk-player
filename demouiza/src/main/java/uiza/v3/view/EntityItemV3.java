package uiza.v3.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;

import uiza.R;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LAnimationUtil;
import vn.loitp.core.utilities.LImageUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Click;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Layout;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.NonReusable;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Position;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Resolve;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.View;

/**
 * Created by www.muathu@gmail.com on 9/16/2017.
 */

//@Animate(Animation.CARD_TOP_IN_DESC)
//@Animate(Animation.CARD_BOTTOM_IN_ASC)
@NonReusable
@Layout(R.layout.v3_uiza_entity_item)
public class EntityItemV3 {

    @View(R.id.image_view)
    private ImageView imageView;
    @View(R.id.pb)
    private ProgressBar progressBar;
    @View(R.id.tv_name)
    private TextView tvName;

    private Item item;
    private Context mContext;
    private Callback mCallback;

    @Position
    private int mPosition;

    private int mSizeW;
    private int mSizeH;

    public EntityItemV3(Context context, Item item, int sizeW, int sizeH, Callback callback) {
        this.mContext = context;
        this.item = item;
        this.mSizeW = sizeW;
        this.mSizeH = sizeH;
        this.mCallback = callback;
    }

    @Resolve
    private void onResolved() {
        imageView.getLayoutParams().width = mSizeW;
        imageView.getLayoutParams().height = mSizeH;
        imageView.requestLayout();

        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(mContext, R.color.White));

        if (item.getThumbnail() == null || item.getThumbnail().isEmpty()) {
            LImageUtil.load((Activity) mContext, Constants.URL_IMG_16x9, imageView, progressBar);
        } else {
            LImageUtil.load((Activity) mContext, Constants.PREFIXS + item.getThumbnail(), imageView, progressBar);
        }

        tvName.setText(item.getName());
        LUIUtil.setTextShadow(tvName);

        if (mCallback != null) {
            mCallback.onPosition(mPosition);
        }
    }

    /*@LongClick(R.id.imageView)
    private void onLongClick() {
        mPlaceHolderView.removeView(this);
    }*/

    @Click(R.id.image_view)
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

        public void onPosition(int position);
    }
}