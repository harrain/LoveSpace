package com.example.lovespace.home.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.lovespace.common.util.DateUtil;

import java.util.Date;

public class AlarmService extends Service {

    private AlarmManager manager;
    int alarmCount = 0;
    DateUtil dateUtil ;
    private long mes;

    public AlarmService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dateUtil = new DateUtil();
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        if (intent.getStringExtra("time") != null) {
            alarmCount = intent.getIntExtra("alarmCount",0);
            String time = intent.getStringExtra("time");

            mes = getStartTime();
            Intent intent1 = new Intent(this, AlarmReceiver.class);
            intent1.putExtra("alarm_type", alarmCount + "");
            PendingIntent pi = PendingIntent.getBroadcast(this, alarmCount, intent1, 0);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, mes, getIntervalTime(time), pi);

        }
        return super.onStartCommand(intent, flags, startId);
    }

    public long getStartTime(){
        Date date = new Date();
        return dateUtil.date2long(date);
    }

    public long getIntervalTime(String time){
        Date date1 = new Date();
        String time1 = dateUtil.date2string(date1)+"-"+time;
        return dateUtil.date2long(dateUtil.str2date(time1))-mes;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
