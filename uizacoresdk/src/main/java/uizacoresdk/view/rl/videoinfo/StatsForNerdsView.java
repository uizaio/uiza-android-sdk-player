package uizacoresdk.view.rl.videoinfo;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import uizacoresdk.R;
import vn.uiza.utils.util.ViewUtils;

public class StatsForNerdsView extends RelativeLayout {
    private TextView textEntityId, textBufferHealth, textNetworkActivity, textVolume, textViewPortFrame,
            textConnectionSpeed, textHost, textVersion, textDeviceInfo, textVideoFormat, textAudioFormat,
            textResolution, textLiveStreamLatency, textLiveStreamLatencyTitle;

    public StatsForNerdsView(Context context) {
        super(context);
        init();
    }

    public StatsForNerdsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatsForNerdsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatsForNerdsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_stats_for_nerds, this);
        textEntityId = findViewById(R.id.text_entity_id);
        textBufferHealth = findViewById(R.id.text_buffer_health);
        textNetworkActivity = findViewById(R.id.text_network_activity);
        textVolume = findViewById(R.id.text_volume);
        textViewPortFrame = findViewById(R.id.text_viewport_frame);
        textConnectionSpeed = findViewById(R.id.text_connection_speed);
        textHost = findViewById(R.id.text_host);
        textVersion = findViewById(R.id.text_versions);
        textDeviceInfo = findViewById(R.id.text_device_info);
        textVideoFormat = findViewById(R.id.text_video_format);
        textAudioFormat = findViewById(R.id.text_audio_format);
        textResolution = findViewById(R.id.text_current_optimal_res);
        textLiveStreamLatency = findViewById(R.id.text_live_stream_latency);
        textLiveStreamLatencyTitle = findViewById(R.id.text_live_stream_latency_title);

        // close button
        findViewById(R.id.btn_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.goneViews(StatsForNerdsView.this);
            }
        });
    }

    /**
     * Depict Entity id
     *
     * @param value should be formatted like below
     * EX: c62a5409-0e8a-4b11-8e0d-c58c43d81b60
     */
    public void setEntityInfo(String value) {
        textEntityId.setText(value);
    }

    /**
     * Depict Buffer health
     *
     * @param value should be formatted like below
     * EX: 20 s
     */
    public void setTextBufferHealth(String value) {
        textBufferHealth.setText(value);
    }

    /**
     * Depict Network Activity
     *
     * @param value should be formatted like below
     * EX: 5 kB or 5 MB
     */
    public void setTextNetworkActivity(String value) {
        textNetworkActivity.setText(value);
    }

    /**
     * Depict Volume
     *
     * @param value should be formatted like below
     * EX: 50%
     */
    public void setTextVolume(String value) {
        textVolume.setText(value);
    }

    /**
     * Depict Viewport and dropped frames
     *
     * @param value should be formatted like below
     * EX: 806x453 / 0 dropped frames
     */
    public void setTextViewPortFrame(String value) {
        textViewPortFrame.setText(value);
    }

    /**
     * Depict connection speed
     *
     * @param value should be formatted like below
     * EX: 40 mbps or 40 kbps
     */
    public void setTextConnectionSpeed(String value) {
        textConnectionSpeed.setText(value);
    }

    /**
     * Depict host
     *
     * @param value should be formatted like below
     * EX: https://uiza.io
     */
    public void setTextHost(String value) {
        textHost.setText(value);
    }

    /**
     * Depict version
     *
     * @param value should be formatted like below
     * EX: SDK Version / Player Version / API Version
     */
    public void setTextVersion(String value) {
        textVersion.setText(value);
    }

    /**
     * Depict Device Info
     *
     * @param value should be formatted like below
     * EX: Device Name / OS Version
     */
    public void setTextDeviceInfo(String value) {
        textDeviceInfo.setText(value);
    }

    /**
     * Depict Video format
     *
     * @param value should be formatted like below
     * Ex: video/avc 1280x720@30
     */
    public void setTextVideoFormat(String value) {
        textVideoFormat.setText(value);
    }

    /**
     * Depict audio format
     *
     * @param value should be formatted like below
     * Ex: audio/mp4a-latm 48000Hz
     */
    public void setTextAudioFormat(String value) {
        textAudioFormat.setText(value);
    }

    /**
     * Depict current / optimal (download) resolution
     *
     * @param value should be formatted like below
     * Ex: 1280x720 /
     */
    public void setTextResolution(String value) {
        textResolution.setText(value);
    }

    /**
     * Depict latency of live stream
     *
     * @param value should be formatted like below
     * Ex: 1000 ms /
     */
    public void setTextLiveStreamLatency(String value) {
        textLiveStreamLatency.setText(getContext().getString(R.string.format_live_stream_latency, value));
    }

    /**
     * Hide TextView latency of live stream
     */
    public void hideTextLiveStreamLatency() {
        ViewUtils.goneViews(textLiveStreamLatency, textLiveStreamLatencyTitle);
    }

    /**
     * Show TextView latency of live stream
     */
    public void showTextLiveStreamLatency() {
        ViewUtils.visibleViews(textLiveStreamLatency, textLiveStreamLatencyTitle);
    }
}
