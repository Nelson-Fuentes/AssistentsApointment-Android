package com.citesoft.epis.attendancetracking.BroadcastRecievers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.models.Attendance;
import com.citesoft.epis.attendancetracking.notifications.CloseNotification;
import com.citesoft.epis.attendancetracking.services.attendanceTracking.AttendanceTrackingRetrofit;
import com.citesoft.epis.attendancetracking.toast.ShowToast;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BroadcastRecieverCloseNotification extends BroadcastReceiver {

    private AttendanceTrackingRetrofit retrofit;
    private BroadcastRecieverProgramNotitication notitication;

    @Override
    public void onReceive(final Context context, Intent intent) {
        notitication = new BroadcastRecieverProgramNotitication();

        retrofit = new AttendanceTrackingRetrofit();

        retrofit.getClosedAttendance().enqueue(new Callback<ArrayList<Attendance>>() {
            @Override
            public void onResponse(Call<ArrayList<Attendance>> call, Response<ArrayList<Attendance>> response) {
                if (!response.isSuccessful()){
                    try {
                        ShowToast.show(context, response.errorBody().string());
                    } catch (IOException e) {
                        ShowToast.show(context, e.getMessage());
                    }

                } else  if (response.body().size()==0){
                   notitication.cancel(context);
                } else {
                    retrofit.closeAttendances().enqueue(new Callback<ArrayList<Attendance>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Attendance>> call, Response<ArrayList<Attendance>> response) {
                            if (response.isSuccessful()){
                                ShowToast.show(context, R.string.attendance_closed);
                                NotificationManager notificationManager = ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE));
                                notificationManager.cancel(CloseNotification.NOTIFICATION_ID);
                                notitication.cancel(context);

                            } else {
                                try {
                                    ShowToast.show(context, response.errorBody().string());
                                } catch (IOException e) {
                                    ShowToast.show(context, e.getMessage());
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Attendance>> call, Throwable t) {
                            ShowToast.show(context, t.getMessage());

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Attendance>> call, Throwable t) {
                ShowToast.show(context, t.getMessage());
            }
        });



    }
}
