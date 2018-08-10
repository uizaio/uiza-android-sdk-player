package testlibuiza.sample.v3.demoui.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import testlibuiza.R;

/**
 * Created by loitp on 2/26/17.
 */

public class WWLVideoControlsOverlay extends FrameLayout implements View.OnClickListener {
    private final RelativeLayout controlsLayout;
    private final ImageView collapseBtn;
    private final ViewGroup bottomEndContainer;
    private final ImageView fullscreenBtn;
    private Listener listener;
    private boolean isHided;

    public WWLVideoControlsOverlay(Context context) {
        super(context);

        setClipToPadding(false);

        LayoutInflater.from(context).inflate(R.layout.wwl_video_default_controls_overlay, this);
        this.controlsLayout = (RelativeLayout) findViewById(R.id.controls_layout);
        this.collapseBtn = (ImageView) findViewById(R.id.player_collapse_button);
        this.collapseBtn.setOnClickListener(this);
        this.bottomEndContainer = (ViewGroup) findViewById(R.id.bottom_end_container);
        this.fullscreenBtn = (ImageView) this.bottomEndContainer.findViewById(R.id.fullscreen_button);
        this.fullscreenBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (this.listener != null) {
            if (v == collapseBtn) {
                this.listener.doCOCollapse();
            } else if (v == fullscreenBtn) {
                this.listener.doCOFullscreen(!this.fullscreenBtn.isSelected());
            }
        }
    }

    public void showControls() {
        this.controlsLayout.setVisibility(VISIBLE);
        this.isHided = false;
    }

    public void hideControls() {
        this.controlsLayout.setVisibility(GONE);
        this.isHided = true;
    }

    public void setListener(Listener l) {
        this.listener = l;
    }

    public void switchFullscreen(boolean selected) {
        this.fullscreenBtn.setSelected(selected);
    }

    public void toggleControls() {
        if (this.isHided) {
            showControls();
        } else {
            hideControls();
        }
    }

    public interface Listener {
        void doCOCollapse();

        void doCOFullscreen(boolean selected);
    }
}
