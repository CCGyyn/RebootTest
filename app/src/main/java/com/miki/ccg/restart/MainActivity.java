package com.miki.ccg.restart;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.miki.ccg.restart.util.MobileInfoUtils;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    Button rebootTest;
    /*Button setting;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*setting = (Button) findViewById(R.id.setting);*/
        rebootTest = (Button) findViewById(R.id.reboot_test);
        /*
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpStartInterface();
            }
        });*/
        rebootTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("确认重启么？")
                        .setPositiveButton("重启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("MainActivity", "onClick");
                                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE)
                                        .edit();
                                editor.putInt("rebootCount", 1);
                                editor.putBoolean("rebootFlag", true);
                                editor.commit();
                                // 重启
                                PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                                powerManager.reboot("重启");
                                /*String cmd = "su -c reboot";
                                try {
                                    myLog("进入try启动runtime");
                                    Runtime.getRuntime().exec("reboot");
                                    myLog("执行完成");
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error! Fail to reboot.", Toast.LENGTH_SHORT).show();
                                }*/
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 取消对话框
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }
    /**
     * Jump Start Interface
     * 提示是否跳转设置自启动界面
     */
    private void jumpStartInterface() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("提示");
            builder.setPositiveButton("立即设置",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MobileInfoUtils.jumpStartInterface(MainActivity.this);
                        }
                    });
            builder.setNegativeButton("暂时不设置",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Exception e) {
        }
    }

    private void myLog(String msg) {
        Log.d(TAG, msg);
    }
}
