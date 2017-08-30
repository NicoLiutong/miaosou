package com.example.animation.PushSever;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.animation.BasicWebActivity;
import com.example.animation.ComicNumberActivity;
import com.example.animation.DownloadActivity;
import com.example.animation.Fragment.AnimationFragment;
import com.example.animation.Fragment.ComicFragment;
import com.example.animation.MainActivity;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。<br/>
 * 2、需要将自定义的 DemoMessageReceiver 注册在 AndroidManifest.xml 文件中：
 * <pre>
 * {@code
 *  <receiver
 *      android:name="com.xiaomi.mipushdemo.DemoMessageReceiver"
 *      android:exported="true">
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.ERROR" />
 *      </intent-filter>
 *  </receiver>
 *  }</pre>
 * 3、DemoMessageReceiver 的 onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。<br/>
 * 4、DemoMessageReceiver 的 onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发。<br/>
 * 5、DemoMessageReceiver 的 onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。<br/>
 * 6、DemoMessageReceiver 的 onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。<br/>
 * 7、DemoMessageReceiver 的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。<br/>
 * 8、以上这些方法运行在非 UI 线程中。
 *
 * @author mayixiang
 */
public class DemoMessageReceiver extends PushMessageReceiver {
    private String mRegId;
    private String mTopic;
    private String mAlias;
    private String mAccount;
    private String mStartTime;
    private String mEndTime;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        //String log = context.getString(R.string.recv_passthrough_message, message.getContent());
        //MainActivity.logList.add(0, getSimpleDate() + " " + log);

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }

        /*Message msg = Message.obtain();
        msg.obj = log;
        DemoApplication.getHandler().sendMessage(msg);*/
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {

        //String log = context.getString(R.string.click_notification_message, miPushMessage.getContent());
        //MainActivity.logList.add(0, getSimpleDate() + " " + log);

        if (!TextUtils.isEmpty(miPushMessage.getTopic())) {
            mTopic = miPushMessage.getTopic();
        } else if (!TextUtils.isEmpty(miPushMessage.getAlias())) {
            mAlias = miPushMessage.getAlias();
        }

        String s = miPushMessage.getContent();
        switch (s.split("--")[0]){
            case "DownloadActivity" :
                Intent intent1 = new Intent(context, DownloadActivity.class);
                intent1.putExtra(AnimationFragment.ANIMATION_NAME,s.split("--")[1]);
                intent1.putExtra(AnimationFragment.ANIMATION_URL,s.split("--")[2]);
                context.startActivity(intent1);
                break;
            case "WebActivity" :
                Intent intent2 = new Intent(context, BasicWebActivity.class);
                intent2.putExtra(AnimationFragment.ANIMATION_URL,s.split("--")[1]);
                context.startActivity(intent2);
                break;
            case "ComicActivity" :
                Intent intent3 = new Intent(context,ComicNumberActivity.class);
                intent3.putExtra(ComicFragment.COMICURL,s.split("--")[1]);
                context.startActivity(intent3);
                break;
            default:
                Intent intent4 = new Intent(context,MainActivity.class);
                context.startActivity(intent4);
                break;
        }
        /*Intent intent = new Intent(context,Second.class);
        intent.putExtra("11",miPushMessage.getContent());
        context.startActivity(intent);*/

        /*Message msg = Message.obtain();
        msg.obj = log;
        DemoApplication.getHandler().sendMessage(msg);*/
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {

        //String log = context.getString(R.string.arrive_notification_message, message.getContent());
        //MainActivity.logList.add(0, getSimpleDate() + " " + log);

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }

        /*Message msg = Message.obtain();
        msg.obj = log;
        DemoApplication.getHandler().sendMessage(msg);*/
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {

        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                //log = context.getString(R.string.register_success);
            } else {
                //log = context.getString(R.string.register_fail);
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                //log = context.getString(R.string.set_alias_success, mAlias);
            } else {
                //log = context.getString(R.string.set_alias_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                //log = context.getString(R.string.unset_alias_success, mAlias);
            } else {
                //log = context.getString(R.string.unset_alias_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAccount = cmdArg1;
                //log = context.getString(R.string.set_account_success, mAccount);
            } else {
                //log = context.getString(R.string.set_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAccount = cmdArg1;
                //log = context.getString(R.string.unset_account_success, mAccount);
            } else {
                //log = context.getString(R.string.unset_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
                //log = context.getString(R.string.subscribe_topic_success, mTopic);
            } else {
                //log = context.getString(R.string.subscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
                //log = context.getString(R.string.unsubscribe_topic_success, mTopic);
            } else {
                //log = context.getString(R.string.unsubscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
                //log = context.getString(R.string.set_accept_time_success, mStartTime, mEndTime);
            } else {
                //log = context.getString(R.string.set_accept_time_fail, message.getReason());
            }
        } else {
            //log = message.getReason();
        }
        //MainActivity.logList.add(0, getSimpleDate() + "    " + log);

        /*Message msg = Message.obtain();
        msg.obj = log;
        DemoApplication.getHandler().sendMessage(msg);*/
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        Log.v(DemoApplication.TAG,
                "onReceiveRegisterResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                //Log.d("register","success");
                //log = context.getString(R.string.register_success);
            } else {
                //log = context.getString(R.string.register_fail);
            }
        } else {
            //log = message.getReason();
        }

        /*Message msg = Message.obtain();
        msg.obj = log;
        DemoApplication.getHandler().sendMessage(msg);*/
    }

}
