### 3.3.2 (2019-3-13)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.3.2'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.3.2'

- Edit error message for Chromecast.
- VDHView: 
	+ Add func for checking current state is maximize or not.
	+ Add func set maximize view programmatically.
	+ Add func set margin of header view.
	+ Fixed bugs scroll to incorrect position when the player is played finish.
	
### 3.3.1 (2019-3-6)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.3.1'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.3.1'

- Update sample [Slide0Activity](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/slide/Slide0Activity.java)
- Update core [VDHView](https://github.com/uizaio/uiza-android-sdk-player/blob/master/uizacoresdk/src/main/java/uizacoresdk/view/vdh/VDHView.java):
	+ Fixed incorrect position when view state changed.
	+ Fixed touch area.
	+ Add more listener.
- Update sample [ResizeActivity](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/customskin/ResizeActivity.java)


### 3.3.0 (2019-3-1)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.3.0'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.3.0'

- Add new [sample for sliding](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/slide/Slide0Activity.java)
- Removed button share in player controller.
- Optimized sample [custom skin.](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/customskin/CustomSkinCodeUZTimebarActivity.java)
### 3.2.8 (2019-2-25)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.2.8'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.2.8'

- Added live tracking.
- Fixed error cannot play on Android 9.

### 3.2.5 (2019-1-28)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.2.5'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.2.5'

- UZVideo removed onStart() and onStop().
- Optimize mini player.
- Optimize Muiza tracking.

### 3.2.3 (2019-1-18)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.2.3'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.2.3'

- Update icon launcher.
- Iplm ping HeartBeat.
- Add func [set size](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/fb/MiniPlayerSettingActivity.java#L319) of mini player.
- Add func make mini player [appear/disappear](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/fb/MiniPlayerSettingActivity.java#L241)
- Add func make mini player switch to full-player by [tapping.](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/fb/MiniPlayerSettingActivity.java#L255)
### 3.1.9 (2019-1-14)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.1.9'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.1.9'

- Add func uzLivestream.stopPreview();
- Add UZAPIMaster.
- Remove BaseActivity and BaseFragment, use AppCompatActivity and Fragment instead.
- Fixed bug divide by zero.
### 3.1.1 (2019-1-4)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.1.1'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.1.1'

- Add first position config for mini player, check [here](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/fb/MiniPlayerSettingActivity.java#L230).
- Add func set property margin for mini player, check [here](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/fb/MiniPlayerSettingActivity.java#L243).
- Add func control mini player, check [here](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/fb/MiniPlayerSettingActivity.java).

### 3.0.8 (2018-12-27)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.0.8'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.0.8'

- Update logic livestreaming.
- Add permission draw over other app for mini player.
- Update func [uzVide.onActivityResult();](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/uzv3/UZPlayerActivity.java#L227)
- Update logic mini player.
- Change name of func onClickPipVideoInitSuccess(boolean isInitSuccess) to void onStateMiniPlayer(boolean isInitMiniPlayerSuccess), check [here](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/uzv3/UZPlayerActivity.java#L293).
- Add setting mini player, check [here](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/fb/MiniPlayerSettingActivity.java).
- Add callback onUICreate() for livestreaming, check [here](https://github.com/uizaio/uiza-android-sdk-player/blob/master/samplelivestream/src/main/java/test/loitp/samplelivestream/LivePortraitActivity.java#L371).
- Add func uzLivestream.hideTvLiveStatus().
- Update screen EventActivity help you understand how to use func [uzVideo.addUZLiveContentCallback()](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/event/EventActivity.java#L331).

### 3.0.3 (2018-12-19)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.0.3'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.0.3'

- Update UZLivestream view.
- Add sample [landscape live broadcaster](https://github.com/uizaio/uiza-android-sdk-player/blob/master/samplelivestream/src/main/java/test/loitp/samplelivestream/LiveLandscapeActivity.java).

### 3.0.0 (2018-12-17)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:3.0.0'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:3.0.0'

- Remove white space when using floating player.
- Update [CustomSkinCodeUZTimebarUTubeWithSlideActivity](https://github.com/uizaio/uiza-android-sdk-player/blob/dev/sample/src/main/java/testlibuiza/sample/v3/utube/CustomSkinCodeUZTimebarUTubeWithSlideActivity.java).
- Add feature [mini player like Facebook]()https://github.com/uizaio/uiza-android-sdk-player/blob/dev/sample/src/main/java/testlibuiza/sample/v3/fb/FBVideoActivity.java).
- Single tap to show/hide controller of mini player


### 2.9.8 (2018-12-10)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.9.8'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.9.8'

- Change function name uzVideo.setVideoListener() -> uzVideo.addVideoListener().
- Update [screen](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/utube/CustomSkinCodeUZTimebarUTubeActivity.java) custom UI like Youtube without sliding.
- Add [sample](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/utube/CustomSkinCodeUZTimebarUTubeWithSlideActivity.java) custom UI like Youtube with UZTimebar and DraggablePanel. 
- Update screen [EventActivity](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/event/EventActivity.java).
- Update screen [CustomHQActivity](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/customhq/CustomHQActivity.java).

### 2.9.5 (2018-12-5)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.9.5'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.9.5'

- Fixed auto screen rotation.


### 2.9.3 (2018-12-3)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.9.3'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.9.3'

- Change id of some components (exo_position -> uz_position, exo_duration -> uz_duration).
- Removed unused resources.
- Iplm DVR, Timeshift (The UZTimebar will be display, user  can seeks in live HLS content).
- Update UI (default skin).
- Fixed uzLivestreamCallback isInitResult() is called more times.

### 2.9.1 (2018-11-30)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.9.1'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.9.1'

- Add func getVideoFormat().
- Add func getAudioFormat().
- Add func getVideoProfileH().
- Add func getVideoProfileW().
- Add func onVideoSizeChanged().
- Add func setBackgroundColorBkg
- Update sample custom ui like youtube, UZTimeBar.
- Add sample ResizeActivity.
- Change id previewFrameLayout->preview_frame_layout.
- Update func ProgressCallback.
- Iplm DRM fearture.

### 2.8.8 (2018-11-19)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.8.8'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.8.8'

- Update sample customize skin like [Youtube](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/customskin/CustomSkinCodeUZTimebarUTubeActivity.java).
- Add func `uzVideo.addCallbackUZTimebar();`
- Remove some uzLivestreamCallback in `UZCallback` such as `onClickBack`,  `onClickListEntityRelation`,   `onClickPip`. Use this func `addItemClick();` instead.
- Ex:
- 

    @Override  
    public void onItemClick(View view) {  
        switch (view.getId()) {  
            case R.id.exo_back_screen:  
                //onClick exo_back_screen
                break;  
        }  
    }


### 2.8.6 (2018-11-16)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.8.6'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.8.6'

- Add func `addAudioListener();`
- Add sample help you know how to listen all event of SDK, [sample here](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/event/EventActivity.java).
- Update some methods name:
+ `setUZCallback` -> `addUZCallback`
+ `setUZTVCallback` -> `addUZTVCallback`
+ `setControllerStateCallback` -> `addControllerStateCallback`
+ `setProgressCallback` -> `addProgressCallback`
+ `setOnTouchEvent` -> `addOnTouchEvent`
+ `hideControllerOnTouch` -> `setHideControllerOnTouch`
- Update sample how to customize skin like [Youtube player](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/customskin/CustomSkinCodeUZTimebarUTubeActivity.java).

### 2.8.2 (2018-11-14)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.8.2'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.8.2'

- Update default skin.
- Update UI state of `ibRewIcon`, `ibFfwdIcon`.
- Add func let you can set speed of player `uzVideo.setSpeed(value);`
- Add new Speed Button (id `exo_speed`) of player controller, check [here](https://github.com/uizaio/uiza-android-sdk-player/blob/master/uizacoresdk/src/main/res/layout/uz_controller_skin_0.xml) for more details.
- Update sample demo.


### 2.8.0 (2018-11-12)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.8.0'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.8.0'

- Fixed onFocusChange for AndroidTV, AndroidBox.
- Add func `getHeightUZVideo()` of uzVideo.
- Update sample TV with custom HQ, custom audio view.
- Update sample using UZTimebar.


### 2.7.8 (2018-11-09)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.7.8'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.7.8'

- Now you can put the UZTimebar on the bottom of the video view, please check this [sample](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/customskin/CustomSkinCodeUZTimebarActivity.java) for more details.
- Fixed func volume toggle.
- Add `onScreenRotate()` in UZCallback.
- Removed some useless components (like volume seekbar, brightness seekbar).
- Update func `setOnTouchEvent()`.
- Add [sample demo volume of video](https://github.com/uizaio/uiza-android-sdk-player/blob/master/sample/src/main/java/testlibuiza/sample/v3/volume/VolumeActivity.java).
- Add func `uzVideo.setVolumeCallback();`
- Fixed adaptive, uzVideo will be played with best profile based on the device's network.



### 2.7.4 (2018-11-03)

    //for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.7.4'
         
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.7.4'

- Update func `uzVideo.setProgressCallback(...);`
- Add feature play any custom linkplay (`uzVideo.initLinkPlay(...)`).
- Solved https://github.com/uizaio/uiza-android-sdk-player/issues/47

### 2.7.3 (2018-11-01)

	//for playing video VOD, LIVE  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.7.3'
     
    //for live broadcaster  
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.7.3'

- Update runtime permission for UZLivestream.
- Update error message and error code.
- Update sample.

### 2.7.0 (2018-10-29)

     //for playing video VOD, LIVE  
     implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:2.7.0'
     
     //for live broadcaster  
     implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:2.7.0'

- Refactor all.

### 2.4.7 (2018-10-16)

    implementation 'com.github.uizaio:uiza-android-sdk-player:2.4.7'
- Update logic for Android TV.
- Fixed bugs cannot touch controller components when replay the video.
- Update clearly error message.

### 2.4.6 (2018-10-15)

    implementation 'com.github.uizaio:uiza-android-sdk-player:2.4.6'
- Customize skin for Android TV.
- Update module sampletv.

### 2.4.5 (2018-10-12)

    implementation 'com.github.uizaio:uiza-android-sdk-player:2.4.5'
- Support IMA Ads.
- Support [SnappySmoothScroller](https://github.com/nshmura/SnappySmoothScroller
).
- Fixed bug: onFocusChange if using for Android Box, Android TV.
- Customize skin for Android TV.


### 2.4.3 (2018-10-05)

    implementation 'com.github.uizaio:uiza-android-sdk-player:2.4.3'
- Fixed issue controller playback.


### 2.4.2 (2018-10-02)

    implementation 'com.github.uizaio:uiza-android-sdk-player:2.4.2'
- Fixed mute function.
- Fixed issue controller show time out.

### 2.3.9 (2018-10-02)

    implementation 'com.github.uizaio:uiza-android-sdk-player:2.3.9'
- Add more API for UZVideo.
- Improve performance.


### 2.3.6 (2018-09-28)

    implementation 'com.github.uizaio:uiza-android-sdk-player:2.3.6'
- Add sample custom skin via using code.
- Add more API for UZVideo.

### 2.3.5 (2018-09-27)

    implementation 'com.github.uizaio:uiza-android-sdk-player:2.3.5'

**New Features**
- Support Android TV now (check sample AndroidTV)
- Add sample demo tv
- Public more API of UZVideo.
- Update skin logic.



### 2.3.2 (2018-09-17)

    implementation 'com.github.uizaio:uiza-android-sdk-player:2.3.2'

**New Features**
- Support vector drawable resources for UZImageButton.
- Add attr useDefaultIB for UZImageButton and useDefaultTV  for UZTextView. In xml layout, if you set true, Uiza will calculate size for you. But if you set false, you can customize size as you like.