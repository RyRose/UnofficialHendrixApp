# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# Butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Picasso
-dontwarn com.squareup.okhttp.**

# Material Tab Strip

# PDF View

# Material Dialogs

# Event Bus
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
    public void onEventMainThread*(**);
    void onEventMainThread*(**);
}

# Logging methods
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}