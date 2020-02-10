package com.citesoft.epis.attendancetracking.services.app.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.citesoft.epis.attendancetracking.BroadcastRecievers.BroadcastRecieverProgramNotitication;

public class ProgrammingCloseNotificationService extends Service {



    private static final  String CHANNEL_ID = "NOTIFICACION";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Context context = getApplicationContext();
        Intent i = new Intent(context, BroadcastRecieverProgramNotitication.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long updateInterval = /*AlarmManager.INTERVAL_HOUR*8;*/AlarmManager.INTERVAL_FIFTEEN_MINUTES/(AlarmManager.INTERVAL_FIFTEEN_MINUTES);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + updateInterval, updateInterval, pendingIntent);
    }


    @Override
    public void onDestroy() {
        BroadcastRecieverProgramNotitication notitication = new BroadcastRecieverProgramNotitication();
        notitication.cancel(getApplicationContext());
    }
}
