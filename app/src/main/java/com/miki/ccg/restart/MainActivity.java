package com.miki.ccg.restart;

import android.content.Context;
import android.content.DialogInterface;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    Button rebootTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rebootTest = (Button) findViewById(R.id.reboot_test);
        rebootTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("确认重启么？")
                        .setPositiveButton("重启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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

    private void myLog(String msg) {
        Log.d(TAG, msg);
    }
}
