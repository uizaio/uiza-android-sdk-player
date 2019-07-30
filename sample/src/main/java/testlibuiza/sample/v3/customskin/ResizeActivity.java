package testlibuiza.sample.v3.customskin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.util.Random;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 5/3/2019.
 */

public class ResizeActivity extends AppCompatActivity implements UZCallback, UZItemClick {
    private UZVideo uzVideo;
    private Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resize);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        uzVideo.addUZCallback(this);
        uzVideo.addItemClick(this);

        final String entityId = LSApplication.entityIdDefaultVODportrait;
        UZUtil.initEntity(activity, uzVideo, entityId);

        findViewById(R.id.bt_bkg_ran).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.setBackgroundColorBkg(getRandomColor());
            }
        });
        findViewById(R.id.bt_bkg_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.setBackgroundColorBkg(Color.RED);
            }
        });
        findViewById(R.id.bt_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
            }
        });
        findViewById(R.id.bt_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            }
        });
        findViewById(R.id.bt_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            }
        });
        findViewById(R.id.bt_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            }
        });
        findViewById(R.id.bt_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            }
        });
        findViewById(R.id.bt_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uzVideo == null) {
                    return;
                }
                uzVideo.setFreeSize(true);
            }
        });
        findViewById(R.id.bt_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uzVideo == null) {
                    return;
                }
                int w = LScreenUtil.getScreenWidth();
                int h = w * 9 / 16;
                uzVideo.setFreeSize(false);
                uzVideo.setSize(w, h);
            }
        });
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzVideo.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uzVideo.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uzVideo.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
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
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onSkinChange() {
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
    }

    @Override
    public void onError(UZException e) {
    }

    @Override
    public void onBackPressed() {
        if (uzVideo.isLandscape()) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}
