package com.miki.ccg.restart;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miki.ccg.restart.util.MobileInfoUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.getSimpleName();
    Button rebootTest;
    Button resetTimes;
    Button resetInterval;
    EditText rebootInterval;
    EditText rebootTimes;
    TextView haveCount;
    /*Button setting;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*setting = (Button) findViewById(R.id.setting);*/
        rebootTest = (Button) findViewById(R.id.reboot_test);
        resetTimes = (Button) findViewById(R.id.reset_times);
        resetInterval = (Button) findViewById(R.id.reset_interval);
        rebootInterval = (EditText) findViewById(R.id.reboot_interval);
        rebootTimes = (EditText) findViewById(R.id.reboot_times);
        haveCount = (TextView) findViewById(R.id.have_count);

        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        int rebootHaveCount = preferences.getInt("rebootCount", 0);
        haveCount.setText(String.valueOf(rebootHaveCount));
        /*
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpStartInterface();
            }
        });*/
        resetTimes.setOnClickListener(this);
        resetInterval.setOnClickListener(this);
        rebootTest.setOnClickListener(this);

        Intent intent = getIntent();
        int rebootCount = intent.getIntExtra("rebootCount", 0);
        if(rebootCount != 0) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("重启次数为:" + String.valueOf(rebootCount))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_times:
                rebootTimes.setText(null);
                break;
            case R.id.reset_interval:
                rebootInterval.setText(null);
                break;
            case R.id.reboot_test:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("确认重启么？")
                        .setPositiveButton("重启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 重启次数
                                String REBOOT_TOTAL_NUM = rebootTimes.getText().toString();
                                // 重启间隔
                                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE)
                                        .edit();
                                String REBOOT_INERVAL = rebootInterval.getText().toString();
                                editor.putInt("rebootCount", 1);
                                if(!REBOOT_TOTAL_NUM.isEmpty()) {
                                    editor.putInt("REBOOT_TOTAL_NUM", Integer.parseInt(REBOOT_TOTAL_NUM));
                                } else {
                                    editor.putInt("REBOOT_TOTAL_NUM", 5000);
                                }
                                if(!REBOOT_INERVAL.isEmpty()) {
                                    editor.putInt("REBOOT_INERVAL", Integer.parseInt(REBOOT_INERVAL) * 1000);
                                } else {
                                    editor.putInt("REBOOT_INERVAL", 3000);
                                }
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
                break;
            default:
                break;
        }
    }
}
