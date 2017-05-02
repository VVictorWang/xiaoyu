# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\victor\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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



#小鱼sdk
-keep enum com.ainemo.sdk.NemoSDKListener** {
    **[] $VALUES;
    public *;
}
-keepclassmembers enum * { *; }
-keepnames class * implements java.io.Serializable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class com.ainemo.sdk.model.Settings{*;}
-keep class com.ainemo.sdk.NemoSDK{
  public *;
}
-keep class com.google.gson.stream.** {*;}
-keep class com.google.gson.** {*;}
-keep class com.google.gson.Gson {*;}
-keep class com.google.gson.examples.android.model.** {*;}
-keep class rx.internal.util.**{*;}

-keep class com.ainemo.sdk.utils.SignatureHelper{*;}
-keep class com.ainemo.sdk.module.ConnectNemoCallback{*;}
-keep class com.ainemo.sdk.NemoReceivedCallListener{*;}
-keep class com.ainemo.sdk.NemoSDKListener{*;}
-keep class com.ainemo.sdk.module.data.VideoInfo{*;}
-keep class com.ainemo.sdk.module.push.**{*;}
-keep class com.ainemo.sdk.module.rest.**{*;}
-keep class com.ainemo.sdk.NemoSDKErrorCode{*;}
