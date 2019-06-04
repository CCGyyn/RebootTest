package com.miki.ccg.restart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class IsRebootTestContinueActivity extends AppCompatActivity {

    AlertDialog dialog;
    Context mContext;
    TimerTask task;
    Timer timer;
//    public final static int REBOOT_TOTAL_NUM = 3;
    private final String TAG = IsRebootTestContinueActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_reboot_test_continue);

        mContext = this;
        final SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        int rebootCount = preferences.getInt("rebootCount", 0);
        AlertDialog.Builder isSure = new AlertDialog.Builder(this);
        isSure.setTitle("警告");
        isSure.setMessage("已重启  "+ String.valueOf(rebootCount) + "次，是否确定停止重启测试?");
        isSure.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(IsRebootTestContinueActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        int REBOOT_INERVAL = preferences.getInt("REBOOT_INERVAL", 3000);
        boolean rebootFlag = preferences.getBoolean("rebootFlag", false);
        // 对话框显示
        dialog = isSure.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        task = new TimerTask() {
            @Override
            public void run() {
                int REBOOT_TOTAL_NUM = preferences.getInt("REBOOT_TOTAL_NUM", 5000);
                int rebootCount = preferences.getInt("rebootCount", 0);
                dialog.dismiss();
                myLog("rebootCount=" + String.valueOf(rebootCount));
                if(rebootCount == 0) {

                } else if(rebootCount < REBOOT_TOTAL_NUM) {
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE)
                            .edit();
                    editor.putInt("rebootCount", ++rebootCount);
                    editor.commit();
                    // 重启
                    PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    powerManager.reboot("重启");

                } else {
                    myLog(String.valueOf(rebootCount) + "次重启测试结束，启动app");
                    Intent bootBroadcastIntent = new Intent(IsRebootTestContinueActivity.this, MainActivity.class);
                    bootBroadcastIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    bootBroadcastIntent.putExtra("rebootCount", rebootCount);
                    startActivity(bootBroadcastIntent);
                }
                finish();
            }
        };
        timer = new Timer();
        timer.schedule(task, REBOOT_INERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(task != null) {
            task.cancel();
        }
        if(timer != null) {
            timer.cancel();
        }
    }

    private void myLog(String msg) {
        Log.d(TAG, msg);
    }
}
