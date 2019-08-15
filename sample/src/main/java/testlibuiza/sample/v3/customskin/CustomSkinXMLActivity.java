package testlibuiza.sample.v3.customskin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDialogUtil;
import io.uiza.core.util.UzAnimationUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.LToast;
import testlibuiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZVideo;

/**
 * Created by loitp on 9/1/2019.
 */

public class CustomSkinXMLActivity extends AppCompatActivity implements UZCallback, UZItemClick {
    private UZVideo uzVideo;
    private Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        activity = this;
        UZUtil.setCurrentPlayerId(R.layout.uiza_controller_skin_custom_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_skin_xml);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        uzVideo.addUZCallback(this);
        uzVideo.addItemClick(this);

        final String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
        UZUtil.initEntity(activity, uzVideo, entityId);

        findViewById(R.id.bt_change_skin_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uzVideo != null) {
                    uzVideo.changeSkin(R.layout.uiza_controller_skin_custom_main);
                }
            }
        });
        findViewById(R.id.bt_change_skin_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uzVideo != null) {
                    uzVideo.changeSkin(R.layout.uz_player_skin_0);
                }
            }
        });
        findViewById(R.id.bt_change_skin_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uzVideo != null) {
                    uzVideo.changeSkin(R.layout.uz_player_skin_1);
                }
            }
        });
        findViewById(R.id.bt_change_skin_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uzVideo != null) {
                    uzVideo.changeSkin(R.layout.uz_player_skin_2);
                }
            }
        });
        findViewById(R.id.bt_change_skin_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uzVideo != null) {
                    uzVideo.changeSkin(R.layout.uz_player_skin_3);
                }
            }
        });
        handleClickSampeText();
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
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, LinkPlay linkPlay, VideoData data) {
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
        handleClickSampeText();
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
    }

    @Override
    public void onError(UzException e) {
        if (e == null) {
            return;
        }
        UzDialogUtil.showDialog1(activity, e.getMessage(), new UzDialogUtil.Callback1() {
            @Override
            public void onClick1() {
                onBackPressed();
            }

            @Override
            public void onCancel() {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (UzDisplayUtil.isFullScreen(activity)) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }

    private void handleClickSampeText() {
        TextView tvSample = uzVideo.findViewById(R.id.tv_sample);
        if (tvSample != null) {
            tvSample.setText("This is a view from custom skin.\nTry to tap me.");
            UzDisplayUtil.setTextShadow(tvSample);
            tvSample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UzAnimationUtil.play(v, Techniques.Pulse);
                    LToast.show(activity, "Click!");
                }
            });
        }
    }
}
