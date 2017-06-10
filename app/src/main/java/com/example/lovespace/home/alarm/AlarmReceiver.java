package com.example.lovespace.home.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.lovespace.R;

/**
 * Created by stephen on 2017/6/10.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ring(context);

        cancelAlarm(context,intent);
        Toast.makeText(context, "时间到了", Toast.LENGTH_LONG).show();
    }

    private void ring(Context context){
        MediaPlayer player = MediaPlayer.create(context, R.raw.piano);
        player.setVolume(1f, 1f);

        player.start();
    }

    private void cancelAlarm(Context context,Intent intent){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, Integer.parseInt(intent.getStringExtra("alarm_type")), intent, 0);
        manager.cancel(pi);
    }
}