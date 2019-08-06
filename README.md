
## Welcome to UizaSDK

Simple Streaming at scale.

Uiza is the complete toolkit for building a powerful video streaming application with unlimited scalability. We design Uiza so simple that you only need a few lines of codes to start streaming, but sophisticated enough for you to build complex products on top of it.

Read [CHANGELOG here](https://github.com/uizaio/uiza-android-sdk-player/blob/v4/CHANGELOG.md).

## Importing the Library
**Step 1. Add the `JitPack` repository to your `build.gradle` file**

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
        // for playing VOD, LIVE video  
        implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:[latest-release-number]'        
        
        // for broadcasting / live streaming
        implementation 'com.github.uizaio.uiza-android-sdk-player:uizalivestream:[latest-release-number]'  
    }

Get latest release number [HERE](https://github.com/uizaio/uiza-android-sdk-player/releases).

If you are using uiza_android_sdk_player (Version 4.0.9 and above), you will need to import dependencies:

    // for playing VOD, LIVE video
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:4.0.9'
    implementation 'com.google.android.exoplayer:exoplayer:2.9.5'
    implementation 'com.google.android.exoplayer:exoplayer-dash:2.9.5'
    implementation 'com.google.android.exoplayer:exoplayer-ui:2.9.5'

- Additionally, if you want to use the Chromecast feature, add the following dependencies to your project:

        // for ChromeCast
        implementation 'com.android.support:mediarouter-v7:28.0.0'
        implementation 'com.google.android.gms:play-services-cast-framework:16.1.2'

- If advertising support should be enabled, also add the following dependencies to your project:

        // for IMA Ads
        implementation 'com.google.android.exoplayer:extension-ima:2.9.5'
        implementation 'com.google.android.gms:play-services-ads:17.1.3'

**Note:**
- The version of the ExoPlayer Extension IMA must match the version of the ExoPlayer library being used.
- If you are using both ChromeCast and IMA Ads dependencies, we recommend using dependency 'com.google.android.gms:play-services-cast-framework:$version' with version >= 16.0.3 to avoid dependency version conflicts

If you are using uiza_android_sdk_player (Version < 4.0.9), you only need to import dependencies:

    // for playing video VOD, LIVE, ChromeCast and advertising support
    implementation 'com.github.uizaio.uiza-android-sdk-player:uizacoresdk:X.X.X'

***Please note if your project uses firebase***:
**firebase-core** & **firebase-database** ... should be same version:
Basically, you need to bump all  _Play Services_  and  _Firebase_  libraries to their latest version (which may be different for each since version 15).

You may use  [https://mvnrepository.com/](https://mvnrepository.com/)  to find the latest version for each library.
See also:  [https://firebase.google.com/support/release-notes/android#20180523](https://firebase.google.com/support/release-notes/android#20180523)

Check [example here](https://github.com/uizaio/uiza-android-sdk-player/blob/v4/sample/build.gradle).

## Init SDK

1. appId : get in email at registration
2. token : generate [HERE](https://docs.uiza.io/v4/#get-api-key).
3. api : default is `ap-southeast-1-api.uiza.co`

     ```
     public class App extends MultiDexApplication {
            @Override
            public void onCreate() {
                super.onCreate();
                UZUtil.initWorkspace(this, Constants.API_VERSION_4, api, token, appId);
            }
     }
     ```

  Manifest

    <application
      android:name=".App "  <!-- important -->
    >

## How to call API?:
Call api by using this function

    UZService service = UZRestClient.createService(UZService.class);
    UZAPIMaster.getInstance().subscribe(service.getListMetadata(), new ApiSubscriber<ResultGetListMetadata>() {
        @Override
        public void onSuccess(ResultGetListMetadata resultGetListMetadata) {
        }

        @Override
        public void onFail(Throwable e) {
        }
    });
  Other API can be used with the same function above.

**API doc**
[APIDOC](https://docs.uiza.io/v4/#introduction)

This class help you know how to use all Uiza API, please refer to
[THIS](https://github.com/uizaio/uiza-android-sdk-player/blob/v4/sample/src/main/java/testlibuiza/sample/v3/api/UZTestAPIActivity.java)

## How to play the video?:
**XML**

    <uizacoresdk.view.rl.video.UZVideo
      android:id="@id/uiza_video"
      android:layout_width="match_parent"
      android:layout_height="wrap_content" />

**JAVA**

Create java file MainActivity:

    public class MainActivity extends AppCompatActivity implements UZCallback {
       ...
    }
Manifest

    <activity
      android:name=".MainActivity "
      android:configChanges="keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize|uiMode" />

In your `activity` or `fragment`

- Play with entity:
    ```
    uzVideo = (UZVideo) findViewById(R.id.uiza_video);
    uzVideo.setUZCallback(this);
    UZUtil.initEntity(activity, uzVideo, "put the entity id here");
    ```
- Play with playlist/folder:
    ```
    UZUtil.initPlaylistFolder(activity, uzVideo, "put the playlist/folder id here");
    ```
- Play with livestream entity:
    ```
    UZUtil.initLiveEntity(activity, uzVideo, "put the livestream entity id here");
    ```

Don't forget to add in activity life cycle event:

    @Override
    public void onDestroy() {
        uzVideo.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        uzVideo.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        uzVideo.onPause();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

If you wanna listen all events of SDK, check the [sample here](https://github.com/uizaio/uiza-android-sdk-player/blob/v4/sample/src/main/java/testlibuiza/sample/v3/event/EventActivity.java).

This sample help you know how to use all Uiza SDK, please refer to  [THIS](https://github.com/uizaio/uiza-android-sdk-player/tree/v4/sample)

**More information for AndroidTV, AndroidBox:**

You can use this SDK for `AndroidTV`, `AndroidBox` as well, but limited some features.
We also provide some functions for `AndroidTV` like:

    uzVideo.addUZTVCallback(this); // listen event onFocusChange of components.

Please take a look at module [sampletv](https://github.com/uizaio/uiza-android-sdk-player/tree/v4/sampletv) for more details.

## How to customize your skin?
Only 3 steps, you can customize everything about player skin.

**Step 1:**
Create layout ***uiza_controller_skin_custom_main.xml*** like [THIS](https://github.com/uizaio/uiza-android-sdk-player/blob/v4/sample/src/main/res/layout/uiza_controller_skin_custom_main.xml):

Please note *`app:controller_layout_id="@layout/uiza_controller_skin_custom_detail"`*

**Step 2:**
Create layout ***uiza_controller_skin_custom_detail.xml*** like [THIS](https://github.com/uizaio/uiza-android-sdk-player/blob/v4/sample/src/main/res/layout/uiza_controller_skin_custom_detail.xml):
- In this xml file, you can edit anything you like: position, color, drawable resouces...
- You can add more view (TextView, Button, ImageView...).
- You can remove any component which you dont like.
- Please note: Don't change any view `id`s  if you are using it.

**Step 3:**
On function `onCreate()` of `Activity`, put this code:

    UZUtil.setCurrentPlayerId(R.layout.uiza_controller_skin_custom_main); 

Ex:

    @Override  
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCurrentPlayerId(R.layout.uiza_controller_skin_custom_main);  
        super.onCreate(savedInstanceState);
    }

**Note:** If you are using Chromecast, please use UZUtil.setCasty(Activity activity) on function onCreate() of Activity

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

This sample help you know how to customize player's skin, please refer to  [THIS](https://github.com/uizaio/uiza-android-sdk-player/tree/v4/sample/src/main/java/testlibuiza/sample/v3/customskin)

***Note:***
- You should not change the id of the view.
Ex: android:id="@id/player_view"
Do not change android:id="@id/player_view_0" or android:id="@+id/player_view_0"...

## How to livestream with UizaSDK?:
It's very easy, plz follow these steps below to implement:

XML:

    <io.uiza.broadcast.UzLivestream
      android:id="@+id/uiza_livestream"  
      android:layout_width="match_parent"  
      android:layout_height="match_parent" />

In class [`LivePortraitActivity`](https://github.com/uizaio/uiza-android-sdk-player/blob/v4/samplelivestream/src/main/java/test/loitp/samplelivestream/LivePortraitActivity.java):

    public class LivePortraitActivity extends AppCompatActivity implements UZLivestreamCallback {
        // ...
    }

In `onCreate()`:

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    uzLivestream = (UZLivestream) findViewById(R.id.uiza_livestream);  
    uzLivestream.setUzLivestreamCallback(this);
    

In `onResume()`:

    @Override  
    protected void onResume() {  
        uzLivestream.onResume();  
        super.onResume();  
    }

Then put this line on `surfaceChanged(UZLivestream.StartPreview startPreview);`

    int[] result = uzLivestream.getBestSizePreview();  
    int width = result[0];  
    int height = result[1];  
    startPreview.onSizeStartPreview(width, height);

In `onPermission()`:

    @Override  
    public void onPermission(boolean areAllPermissionsGranted) {  
        if (areAllPermissionsGranted) {  
           uzLivestream.setId("Put the entity id for livestream here");
        }
    }

Start a `portrait` livestream:

    if (uzLivestream.prepareAudio() && uzLivestream.prepareVideo(false)) {  
        uzLivestream.startStream(uzLivestream.getMainStreamUrl());  
    }

To stream in landscape mode, use `uzLivestream.prepareVideo(true)` instead.

Start a livestream and save to MP4 file:

    if (uzLivestream.prepareAudio() && uzLivestream.prepareVideo(false)) {  
        uzLivestream.startStream(uzLivestream.getMainStreamUrl(), true);  
    }

Stop streaming (It auto saves mp4 file in your gallery if you start a livestream with option save local file)

    uzLivestream.stopStream();

Switch camera:

    uzLivestream.switchCamera();
    
Allows streaming again after back from background:

    uzLivestream.setBackgroundAllowedDuration(YOUR_ALLOW_TIME); // default time is 2 minutes

This sample help you know how to use all Uiza SDK for livestream, please refer to  [THIS](https://github.com/uizaio/uiza-android-sdk-player/tree/v4/samplelivestream/src/main/java/test/loitp/samplelivestream)

## For contributors

 Uiza Checkstyle configuration is based on the Google coding conventions from Google Java Style
 that can be found at https://google.github.io/styleguide/javaguide.html.
 
 Your code must be followed the rules that defined in our [`uiza_style.xml` rules](https://github.com/uizaio/uiza-android-sdk-player/tree/v4/configs/codestyle/uiza_style.xml)
 
 You can setting the rules after import project to Android Studio follow below steps:
 
 1. **File** > **Settings** > **Editor** > **Code Style**
 2. Right on the `Scheme`, select the setting icon > **Import Scheme** > **Intellij IDEA code style XML**
 3. Select the `uiza_style.xml` file path
 4. Click **Apply** > **OK**, then ready to go
 
 For apply check style, install [CheckStyle-IDEA plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea), then 
 
 1. **File** > **Settings** > **Other Settings** > **Checkstyle**
 2. In Configuration file, select the **`+`** icon
 3. Check `Use local checkstyle file` & select path to `uiza_check.xml` file
 4. Select **OK** & you're ready to go

 To run checkstyle for project
 
 1. Right click on project
 2. Select **Analyze** > **Inspect Code**     


## Docs
[Docs](https://uizaio.github.io/uiza-android-sdk-player/)

## Supported devices

Support all devices which have ***Android 4.4 (API level 19) above.***
For a given use case, we aim to support UizaSDK on all Android devices that satisfy the minimum version requirement. 

**Note:** Some Android emulators do not properly implement components of Android’s media stack, and as a result do not support UizaSDK. This is an issue with the emulator, not with UizaSDK. Android’s official emulator (“Virtual Devices” in Android Studio) supports UizaSDK provided the system image has an API level of at least 23. System images with earlier API levels do not support UizaSDK. The level of support provided by third party emulators varies. Issues running UizaSDK on third party emulators should be reported to the developer of the emulator rather than to the UizaSDK team. Where possible, we recommend testing media applications on physical devices rather than emulators.

## Error message
Check this [class](https://github.com/uizaio/uiza-android-sdk-player/blob/v4/uizabase/src/main/java/vn/uiza/core/exception/UZException.java) you can know error code and error message when use UizaSDK.

## Support

If you've found an error in this sample, please file an [issue ](https://github.com/uizaio/uiza-android-sdk-player/issues)

Patches are encouraged, and may be submitted by forking this project and submitting a pull request through GitHub. Please feel free to contact me anytime: developer@uiza.io for more details.

Address: _33 Ubi Avenue 3 #08- 13, Vertex Tower B, Singapore 408868_
Email: _developer@uiza.io_
Website: _[uiza.io](http://uiza.io/)_

## License

UizaSDK is released under the BSD license. See  [LICENSE](https://github.com/uizaio/uiza-android-sdk-player/blob/v4/LICENSE)  for details.


