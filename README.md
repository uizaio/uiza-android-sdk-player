
# Welcome to UizaSDK

# Importing the Library
**Step 1. Add the JitPack repository to your build file**  

    allprojects {  
          repositories {  
             ...  
             maven { url 'https://jitpack.io' }  
          }   }
**Step 2. Add the dependency**  

    defaultConfig {  
      ...  
      multiDexEnabled  true  
    }
    ...
    dependencies {  
      compile 'com.github.uizaio:uiza-android-sdk-player:1.0.0'  
    }

# Init:
**Put these code below into onCreate() of Apllication class:**  

    RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2);
    RestClientV2.addAuthorization(auth.getData().getToken());
    LPref.setAuth(getContext(), auth, gson); 
    //Contact UIZA to get Auth.

# How to call API?:
**Step1: You must extend your activity/fragment like this**  

    public class YourActivity extends BaseActivity{
    ...
    }

or

    public class YourFragment extends BaseFragment{
    }
    
**Step 2: Call api by using this function** 

    private void getListAllMetadata() {  
        UizaService service = RestClientV2.createService(UizaService.class);  
        int limit = 50;  
        String orderBy = "name";  
        String orderType = "ASC";  
      
        JsonBodyMetadataList jsonBodyMetadataList = new JsonBodyMetadataList();  
        jsonBodyMetadataList.setLimit(limit);  
        jsonBodyMetadataList.setOrderBy(orderBy);  
        jsonBodyMetadataList.setOrderType(orderType);  
      
        subscribe(service.listAllMetadataV2(jsonBodyMetadataList), new ApiSubscriber<ListAllMetadata>() {  
            @Override  
            public void onSuccess(ListAllMetadata listAllMetadata) {  
                //do sth  
            }  
      
            @Override  
            public void onFail(Throwable e) {    
                handleException(e);  
            }  
        });  
    }

**API doc**
https://dev-web.uiza.io/api-reference/#create-output-storage

This class help you know how to use all Uiza API, please refer to
https://github.com/uizaio/uiza-android-sdk-player/blob/master/dummy/src/main/java/testlibuiza/uiza/com/dummy/TestAPIActivity.java

# How to play the video?:
**XML**

    <vn.loitp.uizavideo.view.rl.video.UizaIMAVideo  
      android:id="@id/uiza_video"  
      android:layout_width="match_parent"  
      android:layout_height="wrap_content" />
**JAVA**

    uizaIMAVideo = (UizaIMAVideo) findViewById(R.id.uiza_video);  
    String entityId = "Put the entityId here";  
    uizaIMAVideo.setEntityId(entityId, new UizaIMAVideo.Callback() {  
        @Override  
      public void isInitResult(boolean isInitSuccess) {  
            setListener();  
        }  
    });  
    uizaIMAVideo.setTitle("Add video title here");

Dont forget to add in activity life cycle event:

    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        uizaIMAVideo.onDestroy();  
    }  
      
    @Override  
    protected void onResume() {  
        super.onResume();  
        uizaIMAVideo.onResume();  
    }  
      
    @Override  
    protected void onPause() {  
        super.onPause();  
        uizaIMAVideo.onPause();  
    }
    
    @Override  
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == UizaIMAVideo.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {  
		   if (resultCode == RESULT_OK) 
                uizaIMAVideo.initializePiP();
        } else {  
            super.onActivityResult(requestCode, resultCode, data);  
        }  
    }

All listener

    private void setListener() {  
        LLog.d(TAG, TAG + " addListener");  
        uizaIMAVideo.getPlayer().addListener(new Player.EventListener() {  
            @Override  
      public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {  
                LLog.d(TAG, "onTimelineChanged");  
            }  
      
            @Override  
      public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {  
                LLog.d(TAG, "onTimelineChanged");  
            }  
      
            @Override  
      public void onLoadingChanged(boolean isLoading) {  
                LLog.d(TAG, "onTimelineChanged");  
            }  
      
            @Override  
      public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {  
                LLog.d(TAG, "onTimelineChanged");  
            }  
      
            @Override  
      public void onRepeatModeChanged(int repeatMode) {  
                LLog.d(TAG, "onTimelineChanged");  
            }  
      
            @Override  
      public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {  
                LLog.d(TAG, "onTimelineChanged");  
            }  
      
            @Override  
      public void onPlayerError(ExoPlaybackException error) {  
                LLog.d(TAG, "onTimelineChanged");  
            }  
      
            @Override  
      public void onPositionDiscontinuity(int reason) {  
                LLog.d(TAG, "onTimelineChanged");  
            }  
      
            @Override  
      public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {  
                LLog.d(TAG, "onTimelineChanged");  
            }  
      
            @Override  
      public void onSeekProcessed() {  
                LLog.d(TAG, "onTimelineChanged");  
            }  
        });  
        uizaIMAVideo.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {  
            @Override  
      public void onAudioEnabled(DecoderCounters counters) {  
                LLog.d(TAG, "onAudioEnabled");  
            }  
      
            @Override  
      public void onAudioSessionId(int audioSessionId) {  
                LLog.d(TAG, "onAudioSessionId");  
            }  
      
            @Override  
      public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {  
                LLog.d(TAG, "onAudioDecoderInitialized");  
            }  
      
            @Override  
      public void onAudioInputFormatChanged(Format format) {  
                LLog.d(TAG, "onAudioInputFormatChanged");  
            }  
      
            @Override  
      public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {  
                LLog.d(TAG, "onAudioSinkUnderrun");  
            }  
      
            @Override  
      public void onAudioDisabled(DecoderCounters counters) {  
                LLog.d(TAG, "onAudioDisabled");  
            }  
        });  
        uizaIMAVideo.setProgressCallback(new ProgressCallback() {  
            @Override  
      public void onAdProgress(float currentMls, float duration, int percent) {  
                LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");  
            }  
      
            @Override  
      public void onVideoProgress(float currentMls, float duration, int percent) {  
                LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");  
            }  
        });  
        uizaIMAVideo.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {  
            @Override  
      public void onVideoEnabled(DecoderCounters counters) {  
                LLog.d(TAG, "onVideoEnabled");  
            }  
      
            @Override  
      public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {  
                LLog.d(TAG, "onVideoDecoderInitialized");  
            }  
      
            @Override  
      public void onVideoInputFormatChanged(Format format) {  
                LLog.d(TAG, "onVideoInputFormatChanged");  
            }  
      
            @Override  
      public void onDroppedFrames(int count, long elapsedMs) {  
                LLog.d(TAG, "onDroppedFrames");  
            }  
      
            @Override  
      public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {  
                LLog.d(TAG, "onAudioDisabled");  
            }  
      
            @Override  
      public void onRenderedFirstFrame(Surface surface) {  
                LLog.d(TAG, "onRenderedFirstFrame");  
            }  
      
            @Override  
      public void onVideoDisabled(DecoderCounters counters) {  
                LLog.d(TAG, "onVideoDisabled");  
            }  
        });  
        uizaIMAVideo.getPlayer().addMetadataOutput(new MetadataOutput() {  
            @Override  
      public void onMetadata(Metadata metadata) {  
                LLog.d(TAG, "onMetadata");  
            }  
        });  
        uizaIMAVideo.getPlayer().addTextOutput(new TextOutput() {  
            @Override  
      public void onCues(List<Cue> cues) {  
                LLog.d(TAG, "onCues");  
            }  
        });  
    }

## Docs
[Docs](https://uizaio/uiza-android-sdk-player/)


## Support

Please feel free to contact me anytime: loitp@uiza.io

## License

UizaSDK is released under the BSD license. See  [LICENSE](https://github.com/uizaio/uiza-android-sdk-player/blob/master/LICENSE)  for details.