package uiza.v4.helper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import uiza.R;
import vn.uiza.core.utilities.LAnimationUtil;

/**
 * Created by www.muathu@gmail.com on 5/13/2017.
 */

public class UizaActionBar extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();
    private ImageView ivIconLeft;
    private ImageView ivIconRight;
    private TextView tvTitle;
    private View shadowView;

    public ImageView getIvIconLeft() {
        return ivIconLeft;
    }

    public ImageView getIvIconRight() {
        return ivIconRight;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public UizaActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UizaActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.uiza_action_bar, this);

        this.ivIconLeft = (ImageView) findViewById(R.id.iv_icon_left);
        this.ivIconRight = (ImageView) findViewById(R.id.iv_icon_right);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.shadowView = (View) findViewById(R.id.shadow_view);

        ivIconLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LAnimationUtil.play(v, Techniques.Pulse);
                if (callback != null) {
                    callback.onClickLeft();
                }
            }
        });
        ivIconRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LAnimationUtil.play(v, Techniques.Pulse);
                if (callback != null) {
                    callback.onClickRight();
                }
            }
        });
    }

    public interface Callback {
        public void onClickLeft();

        public void onClickRight();
    }

    private Callback callback;

    public void setOnClickBack(Callback onClickBack) {
        this.callback = onClickBack;
    }

    public void setTvTitle(String title) {
        tvTitle.setText(title);
    }

    public void hideTvTitle() {
        tvTitle.setVisibility(GONE);
    }

    public void hideBackIcon() {
        ivIconLeft.setVisibility(GONE);
    }

    public void inviBackIcon() {
        ivIconLeft.setVisibility(INVISIBLE);
    }

    public void hideMenuIcon() {
        ivIconRight.setVisibility(GONE);
    }

    public void showMenuIcon() {
        ivIconRight.setVisibility(VISIBLE);
    }

    public void setTvTitlePositionLeft() {
        tvTitle.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    public void setTvTitlePositionCenter() {
        tvTitle.setGravity(Gravity.CENTER);
    }

    public void setImageRightIcon(int drawableRes) {
        ivIconRight.setImageResource(drawableRes);
    }

    public void setImageLeftIcon(int drawableRes) {
        ivIconLeft.setImageResource(drawableRes);
    }

    public void hideBlurView() {
    }

    public void hideShadowView() {
        shadowView.setVisibility(GONE);
    }
}