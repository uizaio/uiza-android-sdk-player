


# Welcome to UizaSDK

Simple Streaming at scale.

Uiza is the complete toolkit for building a powerful video streaming application with unlimited scalability. We design Uiza so simple that you only need a few lines of codes to start streaming, but sophisticated enough for you to build complex products on top of it.

Read [CHANGELOG here](https://github.com/uizaio/uiza-android-sdk-player/blob/dev/CHANGELOG.md).

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
		  //for playing video VOD, LIVE
          implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:[lasted-release-number]'  
          //for live broadcaster
		  implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:[lasted-release-number]'
    }

Get lasted release number [HERE](https://github.com/uizaio/uiza-android-sdk-player/blob/dev/CHANGELOG.md).

# Init SDK

1. appId : get in email at registration
2. token : generate [HERE](https://docs.uiza.io/#get-api-key).
3. api : get in email at registration
-


     public class App extends MultiDexApplication {
            @Override
            public void onCreate() {
                super.onCreate();
                UZUtil.initWorkspace(this, api, token, appId);
            }
        }

  Manifest


    <application
      android:name=".App "  <!-- important -->
    >


# How to call API?:
**Step1: You need to extend your activity/fragment like this**

    public class YourActivity extends BaseActivity{
    }

or

    public class YourFragment extends BaseFragment{
    }

**make sure add this line below**

    UZUtil.setCasty(this);

before super.onCreate(savedInstanceState);  in onCreate() of your activity.

**Step 2: Call api by using this function**

    UZService service = UZRestClient.createService(UZService.class);
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
[THIS](https://github.com/uizaio/uiza-android-sdk-player/blob/dev/sample/src/main/java/testlibuiza/sample/v3/api/UZTestAPIActivity.java)

# How to play the video?:
**XML**

    <uizacoresdk.view.rl.video.UZVideo
      android:id="@id/uiza_video"
      android:layout_width="match_parent"
      android:layout_height="wrap_content" />

**JAVA**

Create java file MainActivity:

    public class MainActivity extends BaseActivity implements UZCallback{
       public void isInitResult(...);
       public void onClickListEntityRelation(...);
       public void onClickBack(...);
       public void onClickPip(...);
       public void onClickPipVideoInitSuccess(...);
       public void onSkinChange(...);
       public void onError(...);
    }
Manifest

    <activity
      android:name=".MainActivity "
      android:configChanges="keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize|uiMode" />



In onCreate()
Play with entity:

    uzVideo = (UZVideo) findViewById(R.id.uiza_video);
    uzVideo.setUZCallback(this);
    UZUtil.initEntity(activity, uzVideo, "put the entity id here");

Play with playlist/folder:

    UZUtil.initPlaylistFolder(activity, uzVideo, "put the playlist/folder id here");



Dont forget to add in activity life cycle event:

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
        if (requestCode == Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                uzVideo.initializePiP();
            } 
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

Then put

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            uzVideo.setEventBusMsgFromActivityIsInitSuccess();
        }
    }

All listener  (If you want to listen all events)

    private void setListener() {
        if (uzVideo == null || uzVideo.getPlayer() == null) {
            return;
        }
        uzVideo.getPlayer().addListener(new Player.EventListener() {
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
        uzVideo.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
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
        uzVideo.setProgressCallback(new ProgressCallback() {
            @Override
           public void onAdProgress(float currentMls, int s, float duration, int percent) {
           }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
            }
        });
        uzVideo.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
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
        uzVideo.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
           public void onMetadata(Metadata metadata) {
           }
        });
        uzVideo.getPlayer().addTextOutput(new TextOutput() {
            @Override
           public void onCues(List<Cue> cues) {
           }
        });
    }
Listener touch event

    uzVideo.setOnTouchEvent(new UZPlayerView.OnTouchEvent() {
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

**More informations for AndroidTV, AndroidBox:**
You can use this SDK for AndroidTV, AndroidBox as well, but limited some features.
We also provide some functions for AndroidTV like:

    uzVideo.setUZTVCallback(this);//listen event onFocusChange of components.
Please take a look at module [sampletv](https://github.com/uizaio/uiza-android-sdk-player/tree/dev/sampletv) for more details.
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
On function onCreate() of Activity, put this code:

    UZUtil.setCurrentPlayerId(R.layout.uiza_controller_skin_custom_main); 

Ex:

    @Override  
    protected void onCreate(@Nullable Bundle savedInstanceState) {  
        UZUtil.setCasty(this);  
        UZUtil.setCurrentPlayerId(R.layout.uiza_controller_skin_custom_main);  
        super.onCreate(savedInstanceState);
    }

Ex: findView from your custom layout:

    TextView tvSample = uzVideo.findViewById(R.id.tv_sample);

That's enough! This code above will change the player's skin quickly. You can build and run your app now.

But if you wanna change the player's skin when the player is playing, please you this function:

    uzVideo.changeSkin(R.layout.uiza_controller_skin_custom_main);

This sample help you know how to customize player's skin, please refer to  [THIS](https://github.com/uizaio/uiza-android-sdk-player/tree/dev/sample/src/main/java/testlibuiza/sample/v3/customskin)

***Note:***
- You should not change the id of the view.
Ex: android:id="@id/player_view"
Do not change android:id="@id/player_view_0" or android:id="@+id/player_view_0"...

# How to livestream with UizaSDK?:
It's very easy, plz follow these steps below to implement:

XML:

    <uizalivestream.uiza.UZLivestream
      android:id="@+id/uiza_livestream"  
      android:layout_width="match_parent"  
      android:layout_height="match_parent" />

In class LivestreamBroadcasterActivity:

    public class LivestreamBroadcasterActivity extends BaseActivity implements UZLivestream.Callback {
    ...
    }

func onCreate():

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    uzLivestream = (UZLivestream) findViewById(R.id.uiza_livestream);  
    uzLivestream.setCallback(this);
    uzLivestream.setId("Put the entity id for livestream here");

Then put this line on surfaceChanged(UZLivestream.StartPreview startPreview);

    startPreview.onSizeStartPreview(1280, 720);

Start a livestream:

    if (uzLivestream.prepareAudio() && uzLivestream.prepareVideoHD(false)) {  
        uzLivestream.startStream(uzLivestream.getMainStreamUrl());  
    }

Start a livestream and save to MP4 file:

    if (uzLivestream.prepareAudio() && uzLivestream.prepareVideoHD(false)) {  
        uzLivestream.startStream(uzLivestream.getMainStreamUrl(), true);  
    }

Stop streaming (It auto saves mp4 file in your gallery if you start a livestream with option save local file)

    uzLivestream.stopStream();

Switch camera:

    uzLivestream.switchCamera();

This sample help you know how to use all Uiza SDK for livestream, please refer to  [THIS](https://github.com/uizaio/uiza-android-sdk-player/tree/dev/sample/src/main/java/testlibuiza/sample/livestream)

## Docs
[Docs](https://uizaio.github.io/uiza-android-sdk-player/)

## Supported devices

Support all devices which have ***Android 4.4 (API level 19) above.***
For a given use case, we aim to support UizaSDK on all Android devices that satisfy the minimum version requirement. 

**Note:** Some Android emulators do not properly implement components of Android’s media stack, and as a result do not support UizaSDK. This is an issue with the emulator, not with UizaSDK. Android’s official emulator (“Virtual Devices” in Android Studio) supports UizaSDK provided the system image has an API level of at least 23. System images with earlier API levels do not support UizaSDK. The level of support provided by third party emulators varies. Issues running UizaSDK on third party emulators should be reported to the developer of the emulator rather than to the UizaSDK team. Where possible, we recommend testing media applications on physical devices rather than emulators.

## Support

If you've found an error in this sample, please file an [issue ](https://github.com/uizaio/uiza-android-sdk-player/issues)

Patches are encouraged, and may be submitted by forking this project and submitting a pull request through GitHub. Please feel free to contact me anytime: loitp@uiza.io for more details.

Address: _33 Ubi Avenue 3 #08- 13, Vertex Tower B, Singapore 408868_  
Cell: _+84 76408 8864_  
Email: _[loitp@uiza.io](https://www.facebook.com/loitp93)_  
Website: _[uiza.io](http://uiza.io/)_

## License

UizaSDK is released under the BSD license. See  [LICENSE](https://github.com/uizaio/uiza-android-sdk-player/blob/master/LICENSE)  for details.


