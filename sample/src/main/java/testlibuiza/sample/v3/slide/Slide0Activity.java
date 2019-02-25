package testlibuiza.sample.v3.slide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class Slide0Activity extends AppCompatActivity implements VDHView.Callback, UZCallback, UZItemClick {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private VDHView vdhv;
    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private UZVideo uzVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_0);
        if (LSApplication.DF_DOMAIN_API.equals("input")) {
            showDialogInitWorkspace();
        }
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        vdhv = (VDHView) findViewById(R.id.vdhv);
        tv0 = (TextView) findViewById(R.id.tv_0);
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        vdhv.setCallback(this);
        uzVideo.addUZCallback(this);
        uzVideo.addItemClick(this);
        findViewById(R.id.bt_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Click", Toast.LENGTH_SHORT);
            }
        });
        findViewById(R.id.bt_maximize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.maximize();
            }
        });
        findViewById(R.id.bt_minimize_bottom_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.minimizeBottomLeft();
            }
        });
        findViewById(R.id.bt_minimize_bottom_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.minimizeBottomRight();
            }
        });
        findViewById(R.id.bt_minimize_top_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.minimizeTopRight();
            }
        });
        findViewById(R.id.bt_minimize_top_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.minimizeTopLeft();
            }
        });
        findViewById(R.id.bt_alpha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.setEnableAlpha(!vdhv.isEnableAlpha());
            }
        });
        findViewById(R.id.bt_show_hide_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.toggleShowHideHeaderView();
            }
        });
        findViewById(R.id.bt_show_hide_body).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.toggleShowHideBodyView();
            }
        });
        findViewById(R.id.bt_slide_to_position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.smoothSlideTo(300, 600);
            }
        });
        findViewById(R.id.bt_revert_max).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vdhv.isEnableRevertMaxSize()) {
                    vdhv.setEnableRevertMaxSize(false);
                    findViewById(R.id.bt_maximize).setVisibility(View.GONE);
                    if (vdhv.isMinimized()) {
                        findViewById(R.id.bt_minimize_top_right).setVisibility(View.VISIBLE);
                        findViewById(R.id.bt_minimize_top_left).setVisibility(View.VISIBLE);
                    }
                } else {
                    vdhv.setEnableRevertMaxSize(true);
                    findViewById(R.id.bt_maximize).setVisibility(View.VISIBLE);
                    findViewById(R.id.bt_minimize_top_right).setVisibility(View.GONE);
                    findViewById(R.id.bt_minimize_top_left).setVisibility(View.GONE);
                }
            }
        });
        findViewById(R.id.bt_appear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.appear();
            }
        });
        findViewById(R.id.bt_disappear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vdhv.dissappear();
            }
        });
        String metadataId = getIntent().getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
        if (metadataId == null) {
            String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            if (entityId == null) {
                boolean isInitWithPlaylistFolder = UZUtil.isInitPlaylistFolder(activity);
                if (isInitWithPlaylistFolder) {
                    UZUtil.initPlaylistFolder(activity, uzVideo, metadataId);
                } else {
                    UZUtil.initEntity(activity, uzVideo, entityId);
                }
            } else {
                UZUtil.initEntity(activity, uzVideo, entityId);
            }
        } else {
            UZUtil.initPlaylistFolder(activity, uzVideo, metadataId);
        }
    }

    private void showDialogInitWorkspace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Please correct your workspace's information first..");
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onStateChange(VDHView.State state) {
        tv0.setText("onStateChange: " + state.name());
    }

    @Override
    public void onPartChange(VDHView.Part part) {
        tv2.setText("onPartChange: " + part.name());
    }

    @Override
    public void onViewPositionChanged(int left, int top, float dragOffset) {
        tv1.setText("onViewPositionChanged left: " + left + ", top: " + top + ", dragOffset: " + dragOffset);
    }

    @Override
    public void onOverScroll(VDHView.State state, VDHView.Part part) {
        vdhv.dissappear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vdhv.onPause();
        uzVideo.onPause();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setListener() {
        if (uzVideo == null || uzVideo.getPlayer() == null) {
            return;
        }
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            setListener();
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
