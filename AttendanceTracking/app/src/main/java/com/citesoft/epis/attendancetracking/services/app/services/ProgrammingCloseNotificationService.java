package com.citesoft.epis.attendancetracking.services.app.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.citesoft.epis.attendancetracking.BroadcastRecievers.BroadcastRecieverProgramNotitication;
import com.citesoft.epis.attendancetracking.MainActivity;
import com.citesoft.epis.attendancetracking.login.LogUser;

public class ProgrammingCloseNotificationService extends Service {



    private static final  String CHANNEL_ID = "NOTIFICACION";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String token;
    @Override
    public void onCreate() {
        token = LogUser.currentLogUser.getSession().getToken();
        Context context = getApplicationContext();
        Intent i = new Intent(context, BroadcastRecieverProgramNotitication.class);
        i.putExtra("token", token);
        Log.d("ProgSer", token);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long updateInterval = /*AlarmManager.INTERVAL_HOUR*8;*/AlarmManager.INTERVAL_FIFTEEN_MINUTES/5;

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + updateInterval, updateInterval, pendingIntent);
    }


    @Override
    public void onDestroy() {
        BroadcastRecieverProgramNotitication notitication = new BroadcastRecieverProgramNotitication();
        notitication.cancel(getApplicationContext());
    }
}
