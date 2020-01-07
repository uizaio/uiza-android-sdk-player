# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-dontwarn javax.naming.**
-dontwarn javax.servlet.**
-dontwarn org.joda.**
-dontwarn org.slf4j.**

-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }
-dontwarn com.google.firebase.database.**
-keep class com.google.firebase.database.** { *; }

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

-ignorewarnings

# timber
-dontwarn org.jetbrains.annotations.NonNls
# joda
-keep class org.joda.time.** { *; }
-keep class org.joda.convert.** { *; }

#exo
-keep class com.google.android.exoplayer2.** { public *; }
-keep class com.google.android.gms.cast.** { public *; }
-keep public class com.google.android.gms.ads.** { public *;}
# For old ads classes
-keep public class com.google.ads.** { public *;}

-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class vn.uiza.models.** { <fields>; }
-keep class testlibuiza.sample.utils.** { <fields>; }
# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
 }
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }

-dontwarn retrofit2.**