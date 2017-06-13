package com.example.lovespace.home.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.lovespace.R;

/**
 * Created by stephen on 2017/6/10.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private MediaPlayer player;

    @Override
    public void onReceive(Context context, Intent intent) {
        ring(context);
        cancelAlarm(context,intent);
        Toast.makeText(context, "时间到了", Toast.LENGTH_LONG).show();
        showDialog(context);
    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("时间到了");
        builder.setMessage("确定关闭闹钟吗？");
        builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                player.stop();
                player.release();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();

        //需要把对话框的类型设为TYPE_SYSTEM_ALERT，否则对话框无法在广播接收器里弹出
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private void ring(Context context){
        player = MediaPlayer.create(context, R.raw.piano);
        player.setVolume(1f, 1f);

        player.start();
    }

    private void cancelAlarm(Context context,Intent intent){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, Integer.parseInt(intent.getStringExtra("alarm_type")), intent, 0);
        manager.cancel(pi);
    }
}