package uizacoresdk.view.rl.video;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Surface;
import android.view.View;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import java.lang.ref.WeakReference;
import uizacoresdk.BuildConfig;
import uizacoresdk.R;
import uizacoresdk.util.UZData;
import uizacoresdk.view.rl.videoinfo.StatsForNerdsView;
import vn.uiza.core.common.Constants;
import vn.uiza.utils.util.AppUtils;
import vn.uiza.utils.util.ConvertUtils;
import vn.uiza.utils.util.ViewUtils;

final class UZVideoVisualizeInfoHelper {
    private UZVideo uzVideo;
    private StatsForNerdsView statsForNerdsView;
    private Context context;
    private SimpleExoPlayer player;

    private UiUpdateHandler statsUIHandler = new UiUpdateHandler(this);
    private SettingsContentObserver volumeObserver;
    private long bufferedDurationUs;
    private long bitrateEstimate;
    private long bytesLoaded;
    private int droppedFrames;
    private int viewPortWidth, viewPortHeight;
    private int currentResWidth, currentResHeight;
    private int optimalResWidth, optimalResHeight;
    private static final int MSG_UPDATE_STATS = 10005;
    private static final int MSG_UPDATE_STATS_NW_ONLY = 10006;

    UZVideoVisualizeInfoHelper(UZVideo uzVideo, StatsForNerdsView statsForNerdsView) {
        if (uzVideo == null || statsForNerdsView == null) {
            throw new IllegalStateException("You can not active visualize info when UZVideo is null");
        }
        this.uzVideo = uzVideo;
        this.statsForNerdsView = statsForNerdsView;
        this.context = uzVideo.getContext();
        this.player = uzVideo.getPlayer();
        this.volumeObserver = new SettingsContentObserver(this, new Handler());
    }

    // ===== Stats For Nerds =====
    void initStatsForNerds() {
        if (player != null) {
            player.addAnalyticsListener(nerdAnalyticsListener);
        }
    }
    void releaseStatsForNerds() {
        if (player != null) {
            player.removeAnalyticsListener(nerdAnalyticsListener);
        }
        context.getContentResolver().unregisterContentObserver(volumeObserver);
    }

    void toggleStatsForNerds() {
        if (player == null || statsForNerdsView == null) return;
        boolean isHidden = statsForNerdsView.getVisibility() != View.VISIBLE;
        if (isHidden) {
            ViewUtils.visibleViews(statsForNerdsView);
        } else {
            ViewUtils.goneViews(statsForNerdsView);
        }
    }

    void setBufferedDurationUs(long bufferedDurationUs) {
        this.bufferedDurationUs = bufferedDurationUs;
    }

    private AnalyticsListener nerdAnalyticsListener = new AnalyticsListener() {

        @Override
        public void onBandwidthEstimate(EventTime eventTime, int totalLoadTimeMs,
                long totalBytesLoaded, long bitrateEst) {
            bytesLoaded = totalBytesLoaded;
            bitrateEstimate = bitrateEst;
        }

        @Override
        public void onSurfaceSizeChanged(EventTime eventTime, int width, int height) {
            viewPortWidth = width;
            viewPortHeight = height;
            depictViewPortFrameInfo();
        }

        @Override
        public void onLoadCompleted(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo,
                MediaSourceEventListener.MediaLoadData mediaLoadData) {
            Format downloadFormat = mediaLoadData.trackFormat;
            if (downloadFormat != null && downloadFormat.width != -1 && downloadFormat.height != -1) {
                if (downloadFormat.width != optimalResWidth && downloadFormat.height != optimalResHeight) {
                    optimalResWidth = downloadFormat.width;
                    optimalResHeight = downloadFormat.height;
                    depictVideoResolution();
                }
            }
        }

        @Override
        public void onVideoSizeChanged(EventTime eventTime, int width, int height,
                int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            if (width <= 0 || height <= 0) return;
            if (width != currentResWidth && height != currentResHeight) {
                currentResWidth = width;
                currentResHeight = height;
                depictVideoResolution();
            }
        }

        @Override
        public void onVolumeChanged(EventTime eventTime, float volume) {
            depictVolumeInfo(Math.round(volume * 100));
        }

        @Override
        public void onDecoderInputFormatChanged(EventTime eventTime, int trackType, Format format) {
            depictVideoDetailInfo(format);
        }

        @Override
        public void onDroppedVideoFrames(EventTime eventTime, int droppedF, long elapsedMs) {
            droppedFrames += droppedF;
            depictViewPortFrameInfo();
        }

        @Override
        public void onRenderedFirstFrame(EventTime eventTime, @Nullable Surface surface) {
            startPlayerStats();
            context.getContentResolver()
                    .registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, volumeObserver);
        }
    };

    private void startPlayerStats() {
        statsUIHandler.removeMessages(MSG_UPDATE_STATS);
        statsUIHandler.removeMessages(MSG_UPDATE_STATS_NW_ONLY);
        depictVideoInfo();
        depictDeviceInfo();
        depictVersionInfo();
        depictPlayerStats();
        depictPlayerNWStats();
        depictViewPortFrameInfo();
        depictVolumeInfo(AppUtils.getVolumePercentage(context, AudioManager.STREAM_MUSIC));
    }

    private void depictVolumeInfo(int volumePercentage) {
        statsForNerdsView.setTextVolume(context.getString(R.string.format_volume, volumePercentage));
    }

    private void depictVideoDetailInfo(Format format) {
        if (format == null || TextUtils.isEmpty(format.sampleMimeType)) return;
        if (format.sampleMimeType.startsWith("audio")) {
            statsForNerdsView.setTextAudioFormat(
                    context.getString(R.string.format_audio_format, format.sampleMimeType, format.sampleRate));
        } else if (format.sampleMimeType.startsWith("video")) {
            statsForNerdsView.setTextVideoFormat(
                    context.getString(R.string.format_video_format, format.sampleMimeType, format.width,
                            format.height, Math.round(format.frameRate)));
        }
    }

    private void depictVideoResolution() {
        statsForNerdsView.setTextResolution(context.getString(R.string.format_resolution,
                currentResWidth, currentResHeight, optimalResWidth, optimalResHeight));
    }

    private void depictViewPortFrameInfo() {
        if (viewPortWidth == 0 && viewPortHeight == 0) {
            // at first time, surface view size or viewport equals to player size
            viewPortWidth = uzVideo.getPlayerWidth();
            viewPortHeight = uzVideo.getPlayerHeight();
        }
        statsForNerdsView.setTextViewPortFrame(
                context.getString(R.string.format_viewport_frame, viewPortWidth, viewPortHeight, droppedFrames));
    }

    private void depictVideoInfo() {
        statsForNerdsView.setEntityInfo(UZData.getInstance().getEntityId());
        statsForNerdsView.setTextHost(Constants.PREFIXS + UZData.getInstance().getDomainAPI());
    }

    private void depictVersionInfo() {
        statsForNerdsView.setTextVersion(context.getString(R.string.format_version,
                BuildConfig.VERSION_NAME, BuildConfig.EXO_VERSION, Constants.API_VERSION_3));
    }

    private void depictDeviceInfo() {
        statsForNerdsView.setTextDeviceInfo(
                context.getString(R.string.format_device_info, Build.MODEL, Build.VERSION.RELEASE));
    }

    private void depictPlayerStats() {
        String formattedValue;
        if (bitrateEstimate < 1e6) {
            formattedValue = context.getString(R.string.format_connection_speed_k,
                    ConvertUtils.getFormattedDouble((bitrateEstimate / Math.pow(10, 3)), 2));
        } else {
            formattedValue = context.getString(R.string.format_connection_speed_m,
                    ConvertUtils.getFormattedDouble((bitrateEstimate / Math.pow(10, 6)), 2));
        }
        statsForNerdsView.setTextConnectionSpeed(formattedValue);

        // Re-update after 0.5 second
        statsUIHandler.sendEmptyMessageDelayed(MSG_UPDATE_STATS, 500);
    }

    private void depictPlayerNWStats() {
        statsForNerdsView.setTextNetworkActivity(
                ConvertUtils.humanReadableByteCount(bytesLoaded, true, false));
        if (player != null) {
            long remainingUs = (uzVideo.getDuration() - uzVideo.getCurrentPosition()) * 1000;
            remainingUs = remainingUs >= 0 ? remainingUs : 0;
            double buffered = bufferedDurationUs >= remainingUs ? remainingUs : bufferedDurationUs;
            String buffer = ConvertUtils.getFormattedDouble((buffered / Math.pow(10, 6)), 1);
            statsForNerdsView.setTextBufferHealth(context.getString(R.string.format_buffer_health, buffer));
        }

        // Re-update after 1 second
        statsUIHandler.sendEmptyMessageDelayed(MSG_UPDATE_STATS_NW_ONLY, 1000);
    }

    private static class UiUpdateHandler extends Handler {
        private WeakReference<UZVideoVisualizeInfoHelper> weakHelper;
        UiUpdateHandler(UZVideoVisualizeInfoHelper helper) {
            weakHelper = new WeakReference<>(helper);
        }
        @Override
        public void handleMessage(Message msg) {
            UZVideoVisualizeInfoHelper helper = weakHelper.get();
            if (null == helper) return;
            switch (msg.what) {
                case MSG_UPDATE_STATS:
                    helper.depictPlayerStats();
                    break;
                case MSG_UPDATE_STATS_NW_ONLY:
                    helper.depictPlayerNWStats();
                    break;
            }
        }
    }

    private static class SettingsContentObserver extends ContentObserver {
        private WeakReference<UZVideoVisualizeInfoHelper> weakHelper;

        SettingsContentObserver(UZVideoVisualizeInfoHelper helper, Handler handler) {
            super(handler);
            weakHelper = new WeakReference<>(helper);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean selfChange) {
            UZVideoVisualizeInfoHelper helper = weakHelper.get();
            if (helper != null) {
                AudioManager audioManager = (AudioManager) helper.context.getSystemService(Context.AUDIO_SERVICE);
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                helper.uzVideo.setVolume(currentVolume * 1.0f / maxVolume);
            }
        }
    }
}
