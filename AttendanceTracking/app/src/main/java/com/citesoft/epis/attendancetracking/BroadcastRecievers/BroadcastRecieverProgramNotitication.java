package com.citesoft.epis.attendancetracking.BroadcastRecievers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.citesoft.epis.attendancetracking.login.LogUser;
import com.citesoft.epis.attendancetracking.notifications.Channel;
import com.citesoft.epis.attendancetracking.notifications.CloseNotification;

public class BroadcastRecieverProgramNotitication  extends BroadcastReceiver {
    private static final  String CHANNEL_ID = "NOTIFICACION";
    private String token;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras == null)
            Log.d("Service","null");
        else {
            Log.d("Service", "not null");
            token = (String) extras.get("token");
            Log.d("Service", token);

            Channel.makeChanel(CHANNEL_ID, context);
            NotificationCompat.Builder notification = new CloseNotification(context, CHANNEL_ID, token);
            Log.d("Broadcast", token);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(CloseNotification.NOTIFICATION_ID, notification.build());
        }

    }


    public void create(Context context){
        Intent i = new Intent(context, BroadcastRecieverProgramNotitication.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long updateInterval = /*AlarmManager.INTERVAL_HOUR*8;*/AlarmManager.INTERVAL_FIFTEEN_MINUTES/AlarmManager.INTERVAL_FIFTEEN_MINUTES;

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + updateInterval, updateInterval, pendingIntent);
    }

    public void cancel(Context context){
        Intent intent = new Intent(context, BroadcastRecieverProgramNotitication.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
