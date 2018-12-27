package testlibuiza.sample.v3.customskin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;

import testlibuiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.LToast;

/**
 * Created by loitp on 7/16/2018.
 */

public class CustomSkinXMLActivity extends BaseActivity implements UZCallback, UZItemClick {
    private UZVideo uzVideo;

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_uiza_custom_skin_xml;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.uiza_controller_skin_custom_main);
        super.onCreate(savedInstanceState);
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
    public void onStart() {
        super.onStart();
        uzVideo.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        uzVideo.onStop();
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
        handleClickSampeText();
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
    }

    @Override
    public void onError(UZException e) {
        if (e == null) {
            return;
        }
        LLog.e(TAG, "onError: " + e.toString());
        LDialogUtil.showDialog1(activity, e.getMessage(), new LDialogUtil.Callback1() {
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
        if (LScreenUtil.isFullScreen(activity)) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }

    private void handleClickSampeText() {
        TextView tvSample = uzVideo.findViewById(R.id.tv_sample);
        if (tvSample != null) {
            tvSample.setText("This is a view from custom skin.\nTry to tap me.");
            LUIUtil.setTextShadow(tvSample);
            tvSample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LAnimationUtil.play(v, Techniques.Pulse);
                    LToast.show(activity, "Click!");
                }
            });
        }
    }
}
