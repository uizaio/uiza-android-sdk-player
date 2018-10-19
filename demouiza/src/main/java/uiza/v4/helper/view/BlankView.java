package uiza.v4.helper.view;

import uiza.R;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.Layout;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.NonReusable;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.Resolve;

/**
 * Created by www.muathu@gmail.com on 9/16/2017.
 */

//@Animate(Animation.CARD_TOP_IN_DESC)
//@Animate(Animation.CARD_BOTTOM_IN_ASC)
@NonReusable
@Layout(R.layout.uiza_blank_view)
public class BlankView {

    public BlankView() {
    }

    @Resolve
    private void onResolved() {
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