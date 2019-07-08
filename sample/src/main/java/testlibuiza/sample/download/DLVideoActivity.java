package testlibuiza.sample.download;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.offline.DownloadService;

import testlibuiza.R;
import uizacoresdk.cache.DownloadTracker;
import uizacoresdk.interfaces.UZInitDownloadCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import uizacoresdk.view.vdh.VDHView;
import vn.uiza.core.common.Constants;

public class DLVideoActivity extends AppCompatActivity implements View.OnClickListener, UZItemClick,
        UZInitDownloadCallback, DownloadTracker.Listener,
        UZPlayerView.ControllerStateCallback, UZPlayerView.OnTouchEvent {

    private final String TAG = "TAG" + getClass().getSimpleName();
    private VDHView vdhv;
    private UZVideo uzVideo;
    String linkPlay;
    String name;
    String extension;
    Button btnDownload;
    private DownloadTracker downloadTracker;
    private String entityId;

    private void setupUzVideo() {
        uzVideo = findViewById(R.id.uiza_video);
        vdhv = findViewById(R.id.vdhv);
        vdhv.setOnTouchEvent(this);
        vdhv.setScreenRotate(false);
        uzVideo.setUZInitDownloadCallback(this);
        uzVideo.addItemClick(this);
        uzVideo.addControllerStateCallback(this);
        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setEnabled(false);
        btnDownload.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        UZUtil.setUseWithVDHView(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        setupUzVideo();
        setupContent();
        downloadTracker = uzVideo.getDownloadTracker();
        downloadTracker.addListener(this);
        try {
            DownloadService.start(this, UZDownloadService.class);
        } catch (IllegalStateException e) {
            DownloadService.startForeground(this, UZDownloadService.class);
        }
    }

    private void setupContent() {
        String metadataId = getIntent().getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
        if (metadataId == null) {
            entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            if (entityId == null) {
                boolean isInitWithPlaylistFolder = UZUtil.isInitPlaylistFolder(this);
                if (isInitWithPlaylistFolder) {
                    UZUtil.initPlaylistFolder(this, uzVideo, metadataId);
                } else {
                    UZUtil.initEntity(this, uzVideo, entityId);
                }
            } else {
                UZUtil.initEntity(this, uzVideo, entityId);
            }
        } else {
            UZUtil.initPlaylistFolder(this, uzVideo, metadataId);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        uzVideo.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzVideo.onDestroy();
        UZUtil.setUseWithVDHView(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (vdhv.isAppear()) {
            uzVideo.onResume();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDownloadsChanged() {
        btnDownload.setText("DOWNLOADED");
        btnDownload.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDownload) {
            downloadTracker.toggleDownload(getSupportFragmentManager(), UZDownloadService.class, name, Uri.parse(linkPlay), extension);
        }
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onInitDownload(String name, String link, String extension) {
        if (!downloadTracker.isDownloaded(Uri.parse(link))) {
            this.linkPlay = link;
            this.name = name;
            this.extension = extension;
            btnDownload.setEnabled(!TextUtils.isEmpty(linkPlay));
            btnDownload.setText("DOWNLOAD");
        } else {
            btnDownload.setText("DOWNLOADED");
        }
    }


    @Override
    public void onBackPressed() {
        if (uzVideo.isLandscape()) {
            uzVideo.getIbBackScreenIcon().performClick();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onVisibilityChange(boolean isShow) {
        vdhv.setVisibilityChange(isShow);
    }

    @Override
    public void onSingleTapConfirmed(float x, float y) {
        uzVideo.toggleShowHideController();
    }

    @Override
    public void onLongPress(float x, float y) {

    }

    @Override
    public void onDoubleTap(float x, float y) {

    }

    @Override
    public void onSwipeRight() {

    }

    @Override
    public void onSwipeLeft() {

    }

    @Override
    public void onSwipeBottom() {

    }

    @Override
    public void onSwipeTop() {

    }
}
