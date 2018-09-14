package testlibuiza.sample.v3.customskin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv1.view.rl.video.UZVideoV1;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.uzv3.view.rl.video.UZCallback;
import vn.uiza.uzv3.view.rl.video.UZVideo;
import vn.uiza.views.LToast;

/**
 * Created by loitp on 7/16/2018.
 */

public class CustomSkinActivity extends BaseActivity implements UZCallback {
    private UZVideo UZVideo;

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
        return R.layout.activity_uiza_custom_skin;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.uiza_controller_skin_custom_main);
        super.onCreate(savedInstanceState);
        UZVideo = (UZVideo) findViewById(R.id.uiza_video);
        UZVideo.setUZCallback(this);

        final String entityId = LSApplication.entityIdDefaultVOD;
        UZUtil.initEntity(activity, UZVideo, entityId);

        findViewById(R.id.bt_change_skin_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UZVideo != null) {
                    UZVideo.changeSkin(R.layout.uiza_controller_skin_custom_main);
                }
            }
        });
        findViewById(R.id.bt_change_skin_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UZVideo != null) {
                    UZVideo.changeSkin(R.layout.uz_player_skin_0);
                }
            }
        });
        findViewById(R.id.bt_change_skin_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UZVideo != null) {
                    UZVideo.changeSkin(R.layout.uz_player_skin_1);
                }
            }
        });
        findViewById(R.id.bt_change_skin_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UZVideo != null) {
                    UZVideo.changeSkin(R.layout.uz_player_skin_2);
                }
            }
        });
        findViewById(R.id.bt_change_skin_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UZVideo != null) {
                    UZVideo.changeSkin(R.layout.uz_player_skin_3);
                }
            }
        });

        handleClickSampeText();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UZVideo.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        UZVideo.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        UZVideo.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        UZVideo.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        UZVideo.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UZVideoV1.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                UZVideo.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            UZVideo.setEventBusMsgFromActivityIsInitSuccess();
        }
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {
    }

    @Override
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onClickPip(Intent intent) {
    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        if (isInitSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onSkinChange() {
        handleClickSampeText();
    }

    @Override
    public void onError(Exception e) {
    }

    @Override
    public void onBackPressed() {
        if (LScreenUtil.isFullScreen(activity)) {
            UZVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }

    private void handleClickSampeText() {
        TextView tvSample = UZVideo.getPlayerView().findViewById(R.id.tv_sample);
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
