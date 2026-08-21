##Obfuscate Dictioonary
-obfuscationdictionary dictionary.txt
-classobfuscationdictionary dictionary.txt
-packageobfuscationdictionary dictionary.txt

-optimizationpasses 7
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,code/removal/advanced,code/simplification/cast,code/simplification/field,code/simplification/string

-repackageclasses x
-allowaccessmodification
-overloadaggressively

-keepattributes !SourceFile,!LineNumberTable

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int d(...);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
    public static int wtf(...);
}
-assumenosideeffects class java.io.PrintStream {
    public void println(...);
    public void print(...);
}

-ignorewarnings
-dontnote
-dontwarn

-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**
-keep class com.injector.ultrasshservice.tunnel.vpn.TProxyService { *; }

-keep class com.journeyapps.barcodescanner.** { *; }
-dontwarn com.journeyapps.barcodescanner.**

-keep class org.** { *; }
-keep class com.trilead.** { *; }
-keep class com.jcraft.jzlib.** { *; }
-keep class net.** { *; }
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
