package uizacoresdk.view.rl.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import java.util.ArrayList;
import java.util.List;
import uizacoresdk.R;
import uizacoresdk.chromecast.Casty;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.exception.UZExceptionUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.utils.util.SentryUtils;
import vn.uiza.utils.util.ViewUtils;
import vn.uiza.views.autosize.UZImageButton;
import vn.uiza.views.seekbar.UZVerticalSeekBar;

/**
 * Created by loitp on 2/27/2019.
 */

public class UZVideoChromeCast extends UZVideo {

    //chromecast https://github.com/DroidsOnRoids/Casty
    private UZMediaRouteButton uzMediaRouteButton;
    private boolean isCastingChromecast = false;
    private boolean isCastPlayerPlayingFirst = false;
    private RelativeLayout rlChromeCast;
    private UZImageButton ibsCast;

    public UZVideoChromeCast(Context context) {
        super(context);
        setupChromeCast();
    }

    public UZVideoChromeCast(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupChromeCast();
    }

    public UZVideoChromeCast(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupChromeCast();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UZVideoChromeCast(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupChromeCast();
    }

    private void setupChromeCast() {
        if (isTV) return;
        if (!UZUtil.isDependencyAvailable("com.google.android.gms.cast.framework.OptionsProvider")
                || !UZUtil.isDependencyAvailable("android.support.v7.app.MediaRouteButton")) {
            throw new NoClassDefFoundError(UZException.ERR_505);
        }
        uzMediaRouteButton = new UZMediaRouteButton(getContext());
        if (llTop != null) {
            llTop.addView(uzMediaRouteButton);
        }
        setUpMediaRouteButton();
        addUIChromecastLayer();
    }

    public void setTintMediaRouteButton(final int color) {
        if (uzMediaRouteButton != null) {
            uzMediaRouteButton.post(new Runnable() {
                @Override
                public void run() {
                    uzMediaRouteButton.applyTint(color);
                }
            });
        }
    }

    public UZMediaRouteButton getUzMediaRouteButton() {
        return uzMediaRouteButton;
    }

    //tự tạo layout chromecast và background đen
    //Gen layout chromecast with black backgroudn programmatically
    private void addUIChromecastLayer() {
        //listener check state of chromecast
        CastContext castContext = null;
        try {
            castContext = CastContext.getSharedInstance(getContext());
        } catch (Exception e) {
            Log.e(TAG, "Error addUIChromecastLayer: " + e.toString());
            SentryUtils.captureException(e);
        }
        if (castContext == null) {
            ViewUtils.goneViews(uzMediaRouteButton);
            return;
        }
        updateMediaRouteButtonVisibility(castContext.getCastState());
        castContext.addCastStateListener(new CastStateListener() {
            @Override
            public void onCastStateChanged(int state) {
                updateMediaRouteButtonVisibility(state);
            }
        });
        rlChromeCast = new RelativeLayout(getContext());
        LayoutParams rlChromeCastParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rlChromeCast.setLayoutParams(rlChromeCastParams);
        rlChromeCast.setVisibility(GONE);
        rlChromeCast.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.Black));
        ibsCast = new UZImageButton(getContext());
        ibsCast.setBackgroundColor(Color.TRANSPARENT);
        ibsCast.setImageResource(R.drawable.cast);
        LayoutParams ibsCastParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ibsCastParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        ibsCast.setLayoutParams(ibsCastParams);
        ibsCast.setRatioPort(5);
        ibsCast.setRatioLand(5);
        ibsCast.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ibsCast.setColorFilter(Color.WHITE);
        rlChromeCast.addView(ibsCast);
        rlChromeCast.setOnClickListener(this);
        if (llTop != null) {
            if (llTop.getParent() instanceof ViewGroup) {
                ((RelativeLayout) llTop.getParent()).addView(rlChromeCast, 0);
            }
        } else if (rlLiveInfo != null) {
            if (rlLiveInfo.getParent() instanceof ViewGroup) {
                ((RelativeLayout) rlLiveInfo.getParent()).addView(rlChromeCast, 0);
            }
        }
    }

    private void updateMediaRouteButtonVisibility(int state) {
        if (state == CastState.NO_DEVICES_AVAILABLE) {
            ViewUtils.goneViews(uzMediaRouteButton);
        } else {
            ViewUtils.visibleViews(uzMediaRouteButton);
        }
    }

    //TODO improve this func
    private boolean isRefreshFromChangeSkin;
    private long currentPositionBeforeChangeSkin;
    private boolean isCalledFromChangeSkin;

    /*
     ** change skin of player (realtime)
     * return true if success
     */
    public boolean changeSkin(int skinId) {
        if (getContext() == null || uzPlayerManager == null) {
            return false;
        }
        if (UZData.getInstance().isUseWithVDHView()) {
            throw new IllegalArgumentException(getContext().getString(R.string.error_change_skin_with_vdhview));
        }
        if (uzPlayerManager.isPlayingAd()) {
            notifyError(UZExceptionUtil.getExceptionChangeSkin());
            return false;
        }
        UZUtil.setCurrentPlayerId(skinId);
        isRefreshFromChangeSkin = true;
        isCalledFromChangeSkin = true;
        rootView.removeView(uzPlayerView);
        rootView.requestLayout();
        uzPlayerView = (UZPlayerView) ((Activity) getContext()).getLayoutInflater().inflate(skinId, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        uzPlayerView.setLayoutParams(lp);
        rootView.addView(uzPlayerView);
        rootView.requestLayout();
        findViews();
        resizeContainerView();
        updateUIEachSkin();
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        //setup chromecast
        if (!isTV) {
            uzMediaRouteButton = new UZMediaRouteButton(getContext());
            if (llTop != null) {
                llTop.addView(uzMediaRouteButton);
            }
            setUpMediaRouteButton();
            addUIChromecastLayer();
        }
        currentPositionBeforeChangeSkin = getCurrentPosition();
        releaseUzPlayerManager();
        updateUIDependOnLivestream();
        setTitle();
        checkToSetUpResource();
        updateUISizeThumbnail();
        if (uzCallback != null) {
            uzCallback.onSkinChange();
        }
        return true;
    }

    public void setProgressSeekbar(final UZVerticalSeekBar uzVerticalSeekBar, final int progressSeekbar) {
        if (uzVerticalSeekBar == null) {
            return;
        }
        uzVerticalSeekBar.setProgress(progressSeekbar);
    }

    //=============================================================================================START CHROMECAST
    @UiThread
    private void setUpMediaRouteButton() {
        if (isTV) {
            return;
        }
        UZData.getInstance().getCasty().setUpMediaRouteButton(uzMediaRouteButton);
        UZData.getInstance().getCasty().setOnConnectChangeListener(new Casty.OnConnectChangeListener() {
            @Override
            public void onConnected() {
                if (uzPlayerManager != null) {
                    lastCurrentPosition = getCurrentPosition();
                }
                handleConnectedChromecast();
            }

            @Override
            public void onDisconnected() {
                handleDisconnectedChromecast();
            }
        });
    }

    private void handleConnectedChromecast() {
        isCastingChromecast = true;
        isCastPlayerPlayingFirst = false;
        playChromecast();
        updateUIChromecast();
    }

    private void handleDisconnectedChromecast() {
        isCastingChromecast = false;
        isCastPlayerPlayingFirst = false;
        updateUIChromecast();
    }

    //last current position lúc từ exoplayer switch sang cast player
    private long lastCurrentPosition;

    private void playChromecast() {
        if (UZData.getInstance().getData() == null || uzPlayerManager == null || uzPlayerManager.getPlayer() == null) {
            return;
        }
        showProgress();
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, UZData.getInstance().getData().getDescription());
        movieMetadata.putString(MediaMetadata.KEY_TITLE, UZData.getInstance().getData().getEntityName());
        movieMetadata.addImage(new WebImage(Uri.parse(UZData.getInstance().getData().getThumbnail())));

        //TODO add subtitle vtt to chromecast
        List<MediaTrack> mediaTrackList = new ArrayList<>();
        long duration = getDuration();
        if (duration < 0) {
            LLog.e(TAG, "invalid duration -> cannot play chromecast");
            return;
        }

        MediaInfo mediaInfo = new MediaInfo.Builder(
                uzPlayerManager.getLinkPlay())
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setMetadata(movieMetadata)
                .setMediaTracks(mediaTrackList)
                .setStreamDuration(duration)
                .build();

        //play chromecast with full screen control
        //UZData.getInstance().getCasty().getPlayer().loadMediaAndPlay(mediaInfo, true, lastCurrentPosition);

        //play chromecast without screen control
        UZData.getInstance().getCasty().getPlayer().loadMediaAndPlayInBackground(mediaInfo, true, lastCurrentPosition);

        UZData.getInstance().getCasty().getPlayer().getRemoteMediaClient().addProgressListener(new RemoteMediaClient.ProgressListener() {
            @Override
            public void onProgressUpdated(long currentPosition, long duration) {
                if (currentPosition >= lastCurrentPosition && !isCastPlayerPlayingFirst) {
                    hideProgress();
                    isCastPlayerPlayingFirst = true;
                }

                if (currentPosition > 0) {
                    uzPlayerManager.seekTo(currentPosition);
                }
            }
        }, 1000);

    }

    /*khi click vào biểu tượng casting
     * thì sẽ pause local player và bắt đầu loading lên cast player
     * khi disconnect thì local player sẽ resume*/
    private void updateUIChromecast() {
        if (uzPlayerManager == null || rlChromeCast == null || isTV) {
            return;
        }
        if (isCastingChromecast) {
            uzPlayerManager.pauseVideo();
            uzPlayerManager.setVolume(0f);
            ViewUtils.visibleViews(rlChromeCast, ibPauseIcon);
            ViewUtils.goneViews(ibSettingIcon, ibCcIcon, ibBackScreenIcon, ibPlayIcon, ibPauseIcon, ibVolumeIcon);
            //casting player luôn play first với volume not mute
            //UZData.getInstance().getCasty().setVolume(0.99);

            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(0);
            }
        } else {
            uzPlayerManager.resumeVideo();
            uzPlayerManager.setVolume(0.99f);
            ViewUtils.goneViews(rlChromeCast, ibPlayIcon);
            ViewUtils.visibleViews(ibSettingIcon, ibCcIcon, ibBackScreenIcon, ibPauseIcon);
            //TODO iplm volume mute on/off o cast player
            //khi quay lại exoplayer từ cast player thì mặc định sẽ bật lại âm thanh (dù cast player đang mute hay !mute)
            //uzPlayerManager.setVolume(0.99f);

            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT);
            }
        }
    }

    public boolean isCastingChromecast() {
        return isCastingChromecast;
    }
    //=============================================================================================END CHROMECAST

    @Override
    protected void checkCastAndHandle() {
        if (isCastingChromecast) {
            lastCurrentPosition = 0;
            handleConnectedChromecast();
            showController();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCastingChromecast = false;
        isCastPlayerPlayingFirst = false;
    }

    public RelativeLayout getRlChromeCast() {
        return rlChromeCast;
    }

    public UZImageButton getIbsCast() {
        return ibsCast;
    }

    @Override
    public void onClick(View v) {
        if (v == rlChromeCast){
            //dangerous to remove
        } else super.onClick(v);
    }
}
