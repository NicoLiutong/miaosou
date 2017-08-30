package com.example.animation.PushSever;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.xiaomi.mipush.sdk.MiPushClient;

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

        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
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

    /*public static DemoHandler getHandler() {
        return sHandler;
    }*/

    /*public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }*/

    /*public static class DemoHandler extends Handler {

        private Context context;

        public DemoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            if (sMainActivity != null) {
                sMainActivity.refreshLogInfo();
            }
            if (!TextUtils.isEmpty(s)) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        }
    }*/
}