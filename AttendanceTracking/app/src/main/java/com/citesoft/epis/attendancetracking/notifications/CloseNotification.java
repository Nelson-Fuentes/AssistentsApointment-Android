package com.citesoft.epis.attendancetracking.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.citesoft.epis.attendancetracking.BroadcastRecievers.BroadcastRecieverCloseNotification;
import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.services.app.services.CloseAttendanceService;

public class CloseNotification extends NotificationCompat.Builder {

    public static final int NOTIFICATION_ID = 0;
    private String token;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public CloseNotification(Context context, String CHANNEL_ID, String token){
        super(context, CHANNEL_ID);
        this.setSmallIcon(R.drawable.logo);
        this.setContentTitle(context.getString(R.string.open_Session));
        this.setContentText(context.getString(R.string.notification_message));
        this.setColor(context.getColor(R.color.colorPrimary));
        this.setPriority(NotificationCompat.PRIORITY_HIGH);
        this.setLights(Color.MAGENTA, 1000, 1000);
        this.setVibrate(new long[]{1000,1000,1000,1000,1000});
        this.setDefaults(Notification.DEFAULT_SOUND);
        this.setOngoing(true);
        this.token = token;


        Intent service = new Intent(context, CloseAttendanceService.class);
        service.putExtra("token", token);
        Log.d("Notifiacion", token);
        PendingIntent closePendingService= PendingIntent.getService(context, 0, service, PendingIntent.FLAG_UPDATE_CURRENT);
        this.addAction(R.drawable.logo,context.getString(R.string.close), closePendingService);

    }

}
