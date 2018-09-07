

# Welcome to UizaSDK

Simple Streaming at scale.

Uiza is the complete toolkit for building a powerful video streaming application with unlimited scalability. We design Uiza so simple that you only need a few lines of codes to start streaming, but sophisticated enough for you to build complex products on top of it.

# Importing the Library
**Step 1. Add the JitPack repository to your build file**

    allprojects {
          repositories {
             maven { url 'https://jitpack.io' }
          }
    }
**Step 2. Add the dependency**

    defaultConfig {
      multiDexEnabled  true
    }
    dependencies {
      compile 'com.github.uizaio:uiza-android-sdk-player:[lasted-release-number]'
    }

Get lasted relea number [HERE](https://jitpack.io/#uizaio/uiza-android-sdk-player).

# Init SDK

1. appId : get in email at registration
2. token : generate [HERE](https://docs.uiza.io/#get-api-key).
3. api : get in email at registration
-


     public class App extends MultiDexApplication {
            @Override
            public void onCreate() {
                super.onCreate();
                UizaUtil.initWorkspace(this, api, token, appId);
            }
        }

  Manifest


    <application
      android:name=".App "  <!-- important -->
      ........
      >


# How to call API?:
**Step1: You must extend your activity/fragment like this**

    public class YourActivity extends BaseActivity{
    }

or

    public class YourFragment extends BaseFragment{
    }

**make sure add this line below**

    UizaUtil.setCasty(this);

before super.onCreate(savedInstanceState);  in onCreate() of your activity.

**Step 2: Call api by using this function**

    UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
    subscribe(service.getListMetadata(), new ApiSubscriber<ResultGetListMetadata>() {
        @Override
      public void onSuccess(ResultGetListMetadata resultGetListMetadata) {
        }

        @Override
      public void onFail(Throwable e) {
        }
    });
  Other API can be used with the same function above.

**API doc**
[APIDOC](https://docs.uiza.io/#introduction)

This class help you know how to use all Uiza API, please refer to
[THIS](https://github.com/uizaio/uiza-android-sdk-player/blob/dev/sample/src/main/java/testlibuiza/sample/v3/api/V3TestAPIActivity.java)

# How to play the video?:
**XML**

    <vn.loitp.uizavideov3.view.rl.video.UizaIMAVideoV3
      android:id="@id/uiza_video"
      android:layout_width="match_parent"
      android:layout_height="wrap_content" />

**JAVA**

Create java file MainActivity:

    public class MainActivity extends BaseActivity implements UizaCallback{
       public void isInitResult();
       public void onClickListEntityRelation();
       public void onClickBack();
       public void onClickPip();
       public void onClickPipVideoInitSuccess();
       public void onSkinChange();
       public void onError();
    }
Manifest

    <activity
      android:name=".MainActivity "
      android:configChanges="keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize|uiMode" />



In onCreate()
Play with entity:

    uizaIMAVideoV3 = (UizaIMAVideoV3) findViewById(R.id.uiza_video);
    uizaIMAVideoV3.setUizaCallback(this);
    UizaUtil.initEntity(activity, uizaIMAVideoV3, "put the entity id here");

Play with playlist/folder:

    UizaUtil.initPlaylistFolder(activity, uizaIMAVideoV3, "put the playlist/folder id here");



Dont forget to add in activity life cycle event:

    @Override
    public void onDestroy() {
        super.onDestroy();
        uizaIMAVideoV3.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uizaIMAVideoV3.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uizaIMAVideoV3.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        uizaIMAVideoV3.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        uizaIMAVideoV3.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UizaIMAVideo.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                uizaIMAVideoV3.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

Then put

    @Override
    public void isInitResult(boolean b, ResultGetLinkPlay resultGetLinkPlay, ResultRetrieveAnEntity resultRetrieveAnEntity) {
        if (b) {
            uizaIMAVideoV3.setEventBusMsgFromActivityIsInitSuccess();
        }
    }

All listener  (If you want to listen all events)

    private void setListener() {
        if (uizaIMAVideoV3 == null || uizaIMAVideoV3.getPlayer() == null) {
            return;
        }
        uizaIMAVideoV3.getPlayer().addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {
            }
        });
        uizaIMAVideoV3.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
            @Override
            public void onAudioEnabled(DecoderCounters counters) {
            }

            @Override
            public void onAudioSessionId(int audioSessionId) {
            }

            @Override
            public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
            }

            @Override
            public void onAudioInputFormatChanged(Format format) {
            }

            @Override
            public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
            }

            @Override
          public void onAudioDisabled(DecoderCounters counters) {
           }
        });
        uizaIMAVideoV3.setProgressCallback(new ProgressCallback() {
            @Override
           public void onAdProgress(float currentMls, int s, float duration, int percent) {
           }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
            }
        });
        uizaIMAVideoV3.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
            @Override
           public void onVideoEnabled(DecoderCounters counters) {
           }

            @Override
           public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
           }

            @Override
           public void onVideoInputFormatChanged(Format format) {
           }

            @Override
           public void onDroppedFrames(int count, long elapsedMs) {
           }

            @Override
           public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
           }

            @Override
           public void onRenderedFirstFrame(Surface surface) {
           }

            @Override
          public void onVideoDisabled(DecoderCounters counters) {
           }
        });
        uizaIMAVideoV3.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
           public void onMetadata(Metadata metadata) {
           }
        });
        uizaIMAVideoV3.getPlayer().addTextOutput(new TextOutput() {
            @Override
           public void onCues(List<Cue> cues) {
           }
        });
    }
Listener touch event

    uizaIMAVideoV3.setOnTouchEvent(new UizaPlayerView.OnTouchEvent() {
        @Override
        public void onSingleTapConfirmed() {
        }

        @Override
        public void onLongPress() {
        }

        @Override
        public void onDoubleTap() {
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
    });

This sample help you know how to use all Uiza SDK, please refer to  [THIS](https://github.com/uizaio/uiza-android-sdk-player/tree/master/sample)

# How to customize your skin?:
Only 3 steps, you can customize everything about player skin.

**Step 1:**
Create layout ***uiza_controller_skin_custom_main.xml*** like [THIS](https://github.com/uizaio/uiza-android-sdk-player/blob/dev/sample/src/main/res/layout/uiza_controller_skin_custom_main.xml):

Please note *app:controller_layout_id="@layout/uiza_controller_skin_custom_detail"*

**Step 2:**
Create layout ***uiza_controller_skin_custom_detail.xml*** like [THIS](https://github.com/uizaio/uiza-android-sdk-player/blob/dev/sample/src/main/res/layout/uiza_controller_skin_custom_detail.xml):
- In this xml file, you can edit anything you like: position, color, drawable resouces...
- You can add more view (TextView, Button, ImageView...).
- You can remove any component which you dont like.
- Please note: Dont change any id's view if you are using it.

**Step 3:**'
On function onStart() of Activity, put this code:

    UizaUtil.setCurrentPlayerId(R.layout.uiza_controller_skin_custom_main);

That's enough! This code above will change the player's skin quickly. You can build and run your app now.

But if you wanna change the player's skin when the player is playing, please you this function:

    uizaIMAVideoV3.changeSkin(R.layout.uiza_controller_skin_custom_main);

This sample help you know how to customize player's skin, please refer to  [THIS](https://github.com/uizaio/uiza-android-sdk-player/tree/dev/sample/src/main/java/testlibuiza/sample/v3/customskin)

***Note:***
- You should not change the id of the view.
Ex: android:id="@id/player_view"
Dont change android:id="@id/player_view_0" or android:id="@+id/player_view_0"...

# How to livestream with UizaSDK?:
It's very easy, plz follow these step below:
XML:

    <vn.loitp.uizavideov3.view.rl.livestream.UizaLivestream  
      android:id="@+id/uiza_livestream"  
      android:layout_width="match_parent"  
      android:layout_height="match_parent" />

In class LivestreamBroadcasterActivity:

    public class LivestreamBroadcasterActivity extends BaseActivity implements  UizaLivestream.Callback {
    ...
    }

func onCreate():

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    uizaLivestream = (UizaLivestream) findViewById(R.id.uiza_livestream);  
    uizaLivestream.setCallback(this);
    uizaLivestream.setId("Put the entity id for livestream here");

Then put this line on surfaceChanged(UizaLivestream.StartPreview startPreview);

    startPreview.onSizeStartPreview(1280, 720);

Start a livestream:

    if (uizaLivestream.prepareAudio() && uizaLivestream.prepareVideoHD(false)) {  
        uizaLivestream.startStream(uizaLivestream.getMainStreamUrl());  
    }

Start a livestream and save to mp4 file:

    if (uizaLivestream.prepareAudio() && uizaLivestream.prepareVideoHD(false)) {  
        uizaLivestream.startStream(uizaLivestream.getMainStreamUrl(), true);  
    }

Stop streaming (It auto save mp4 file in your gallery if you start a livestream with option save local file)

    uizaLivestream.stopStream();

Switch camera:

    uizaLivestream.switchCamera();

This sample help you know how to use all Uiza SDK for livestream, please refer to  [THIS](https://github.com/uizaio/uiza-android-sdk-player/tree/dev/sample/src/main/java/testlibuiza/sample/livestream)

## Docs
[Docs](https://uizaio.github.io/uiza-android-sdk-player/)


## Support

Please feel free to contact me anytime: loitp@uiza.io

## License

UizaSDK is released under the BSD license. See  [LICENSE](https://github.com/uizaio/uiza-android-sdk-player/blob/master/LICENSE)  for details.

