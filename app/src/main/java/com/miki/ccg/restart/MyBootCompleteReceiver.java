package com.miki.ccg.restart;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyBootCompleteReceiver extends BroadcastReceiver{
//    public final static int REBOOT_TOTAL_NUM = 3;
    private final String TAG = MyBootCompleteReceiver.class.getSimpleName();

    public MyBootCompleteReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        myNotify(context);
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        int rebootCount = preferences.getInt("rebootCount", 0);
        int REBOOT_TOTAL_NUM = preferences.getInt("REBOOT_TOTAL_NUM", 5000);
        boolean rebootFlag = preferences.getBoolean("rebootFlag", false);
        myLog("rebootCount=" + String.valueOf(rebootCount));
        if(rebootCount == 0) {

        } else if(rebootCount < REBOOT_TOTAL_NUM) {
                Intent bootBroadcastIntent = new Intent(context, IsRebootTestContinueActivity.class);
                bootBroadcastIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 防止context.startActivity出现异常
                context.startActivity(bootBroadcastIntent);
        } else { // 达到最大重启次数
            Intent bootBroadcastIntent = new Intent(context, MainActivity.class);
            bootBroadcastIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bootBroadcastIntent.putExtra("rebootCount", rebootCount);
            context.startActivity(bootBroadcastIntent);
        }
    }
    private void myLog(String msg) {
        Log.d(TAG, msg);
    }
    /**
     * 发送一个通知
     */
    private void myNotify(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "notification_simple";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "simple",
                    NotificationManager.IMPORTANCE_DEFAULT
                    );
            manager.createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle("开机广播")
                    .setContentText("测试开机广播")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            manager.notify(1, notification);
        }

    }
}
