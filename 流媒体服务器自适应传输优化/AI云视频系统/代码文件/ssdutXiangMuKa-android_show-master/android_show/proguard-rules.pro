# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/android-sdk/tools/proguard/proguard-android.txt
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

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-ignorewarnings


#-libraryjars ./libs/android-support-v4.jar
#-libraryjars ./libs/butterknife-7.0.1.jar
#-libraryjars ./libs/fastjson-android-1.1.34.jar
#-libraryjars ./libs/locSDK_5.2.jar
#-libraryjars ./libs/okhttp-2.2.0.jar
#-libraryjars ./libs/picasso-2.5.0.jar
#-libraryjars ./libs/ormlite-android-4.49.jar
#-libraryjars ./libs/ormlite-core-4.49.jar


##################below is for common android
-keep class org.**{*;}
#-keep class com.ksyun.media.streamer.**{ *; }
#-keep class com.ksyun.media.**{ *; }
#-keep class com.ksyun.statlibrary.**{ *; }
-keep class com.ksyun.** { *; }
-keep class com.lht.paintview.PaintView
-keep class com.ksy.statlibrary.** { *; }
-keep class com.facebook.**{ *; }
-keep class android.support.**{ *; }
-keep class com.squareup.leakcanary.**{ *; }

-keep public class * extends android.support.v7.**
#-keep class com.yuzhi.*{ *; }
-keep class com.live.feilong.R
-keep public class **.R$* { public static final int *; }
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.view.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep class * implements android.os.Parcelable {public static final android.os.Parcelable$Creator *; }
##################upper is for common android

-keep public interface com.yuzhi.fine.common.NotObfuscateInterface{public *;}
-keep class * implements com.yuzhi.fine.common.NotObfuscateInterface{
	<methods>;
	<fields>;
}

# OrmLite uses reflection
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }


# butterknife uses reflection
-keep class android.widget.**{
    *;
}
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keep class com.ksyun.** {
  *;
}
-keep class com.live.feilong.model.**{ *;}
-keepclasseswithmembernames class * {
    native <methods>;
}

#ksymediaplayer
-keep class com.ksy.statlibrary.** {
  *;
}
-keep class com.ksyun.media.player.**{ *; }
-keep class com.ksy.statlibrary.**{ *;}

#fastjson
-keep class javax.ws.rs.** { *; }
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}
-keepattributes InnerClasses,Singature      #避免混淆泛型
-keepattributes *Annotation     #不混淆注释

#qianwen
-keep class com.qianwen.**{
    *;
}

#jikpalyer
-keep class tv.danmaku.ijk.media.player.**{*;}

# Facebook
-keep class com.facebook.** {*;}
-keep interface com.facebook.** {*;}
-keep enum com.facebook.** {*;}

#squareup
-dontwarn com.squareup.okhttp.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

#yuzhi
-keep class com.live.feilong.ui.**{*;}



