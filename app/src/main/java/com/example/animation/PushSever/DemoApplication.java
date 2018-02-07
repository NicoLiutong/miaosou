package com.example.animation.PushSever;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mistatistic.sdk.MiStatInterface;
import com.xiaomi.mistatistic.sdk.URLStatsRecorder;
import com.xiaomi.mistatistic.sdk.controller.HttpEventFilter;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;

import org.litepal.LitePalApplication;

import java.util.List;

/**
 * 1、为了打开客户端的日志，便于在开发过程中调试，需要自定义一个 Application。
 * 并将自定义的 application 注册在 AndroidManifest.xml 文件中。<br/>
 * 2、为了提高 push 的注册率，您可以在 Application 的 onCreate 中初始化 push。你也可以根据需要，在其他地方初始化 push。
 *
 * @author wangkuiwei
 */
public class DemoApplication extends LitePalApplication {

    // user your appid the key.
    private static final String APP_ID = "2882303761517607255";
    // user your appid the key.
    private static final String APP_KEY = "5351760768255";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo
    public static final String TAG = "com.example.animation";

    //private static DemoHandler sHandler = null;
    //private static MainActivity sMainActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // regular stats.
        MiStatInterface.initialize(this,"2882303761517607255","5351760768255", "kuan");

        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }

        //MiStatInterface.setUploadPolicy(MiStatInterface.UPLOAD_POLICY_WHILE_INITIALIZE, 0);
        MiStatInterface.setUploadPolicy(MiStatInterface.UPLOAD_POLICY_WHILE_INITIALIZE,0);
        MiStatInterface.triggerUploadManually();
        MiStatInterface.enableLog();

        // enable exception catcher.
        MiStatInterface.enableExceptionCatcher(true);
        // enable network monitor
        URLStatsRecorder.enableAutoRecord();

        URLStatsRecorder.setEventFilter(new HttpEventFilter() {

            @Override
            public HttpEvent onEvent(HttpEvent event) {
                // returns null if you want to drop this event.
                // you can modify it here too.
                return event;
            }
        });

        //Log.d("MI_STAT", MiStatInterface.getDeviceID(this) + " is the device.");
        /*if (sHandler == null) {
            sHandler = new DemoHandler(getApplicationContext());
        }*/
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


}