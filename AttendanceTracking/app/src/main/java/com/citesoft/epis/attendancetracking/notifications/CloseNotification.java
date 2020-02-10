package com.citesoft.epis.attendancetracking.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.citesoft.epis.attendancetracking.BroadcastRecievers.BroadcastRecieverCloseNotification;
import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.services.app.services.CloseAttendanceService;

public class CloseNotification extends NotificationCompat.Builder {

    public static final int NOTIFICATION_ID = 0;

    public CloseNotification(Context context, String CHANNEL_ID){
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


        Intent service = new Intent(context, CloseAttendanceService.class);
        PendingIntent closePendingService= PendingIntent.getService(context, 0, service, 0);
        this.addAction(R.drawable.logo,context.getString(R.string.close), closePendingService);

    }

}
