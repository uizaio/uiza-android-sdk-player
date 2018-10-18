package uiza.v4.helper.view;

import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import uiza.R;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.Layout;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.NonReusable;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.Resolve;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.View;

/**
 * Created by www.muathu@gmail.com on 9/16/2017.
 */

//@Animate(Animation.CARD_TOP_IN_DESC)
//@Animate(Animation.CARD_BOTTOM_IN_ASC)
@NonReusable
@Layout(R.layout.uiza_loading_view)
public class LoadingView {
    @View(R.id.pb)
    private ProgressBar progressBar;

    public LoadingView() {
    }

    @Resolve
    private void onResolved() {
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(progressBar.getContext(), R.color.White));
    }

    /*@LongClick(R.id.imageView)
    private void onLongClick() {
        mPlaceHolderView.removeView(this);
    }*/

    /*@Click(R.id.imageView)
    private void onClick() {

    }*/

    /*public interface UZCallback {
        public void onClick(Item item, int position);
    }*/
}