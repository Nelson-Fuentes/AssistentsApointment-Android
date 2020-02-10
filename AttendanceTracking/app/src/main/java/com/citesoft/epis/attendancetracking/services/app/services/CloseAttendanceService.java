package com.citesoft.epis.attendancetracking.services.app.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.citesoft.epis.attendancetracking.BroadcastRecievers.BroadcastRecieverProgramNotitication;
import com.citesoft.epis.attendancetracking.MainActivity;
import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.activities.attendance.AttendanceFragment;
import com.citesoft.epis.attendancetracking.models.Attendance;
import com.citesoft.epis.attendancetracking.notifications.CloseNotification;
import com.citesoft.epis.attendancetracking.services.attendanceTracking.AttendanceTrackingRetrofit;
import com.citesoft.epis.attendancetracking.toast.ShowToast;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloseAttendanceService extends Service {

    private AttendanceTrackingRetrofit retrofit;
    private BroadcastRecieverProgramNotitication notitication;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {



        notitication = new BroadcastRecieverProgramNotitication();

        retrofit = new AttendanceTrackingRetrofit();

        retrofit.getClosedAttendance().enqueue(new Callback<ArrayList<Attendance>>() {
            @Override
            public void onResponse(Call<ArrayList<Attendance>> call, Response<ArrayList<Attendance>> response) {
                if (!response.isSuccessful()){
                    try {
                        ShowToast.show(getApplicationContext(), response.errorBody().string());
                    } catch (IOException e) {
                        ShowToast.show(getApplicationContext(), e.getMessage());
                    }

                } else  if (response.body().size()==0){
                    notitication.cancel(getApplicationContext());
                    Intent service1 = new Intent(getApplicationContext(), ProgrammingCloseNotificationService.class);
                    getApplicationContext().stopService(service1);
                    notitication.cancel(getApplicationContext());
                } else {
                    retrofit.closeAttendances().enqueue(new Callback<ArrayList<Attendance>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Attendance>> call, Response<ArrayList<Attendance>> response) {
                            if (response.isSuccessful()){
                                ShowToast.show(getApplicationContext(), R.string.attendance_closed);
                                NotificationManager notificationManager = ((NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE));
                                notificationManager.cancel(CloseNotification.NOTIFICATION_ID);
                                notitication.cancel(getApplicationContext());
                                Intent service1 = new Intent(getApplicationContext(), ProgrammingCloseNotificationService.class);
                                getApplicationContext().stopService(service1);
                                Intent service = new Intent(getApplicationContext(), CloseAttendanceService.class);
                                getApplicationContext().stopService(service);


                                AttendanceFragment view = new AttendanceFragment();




                            } else {
                                try {
                                    ShowToast.show(getApplicationContext(), response.errorBody().string());
                                } catch (IOException e) {
                                    ShowToast.show(getApplicationContext(), e.getMessage());
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Attendance>> call, Throwable t) {
                            ShowToast.show(getApplicationContext(), t.getMessage());

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Attendance>> call, Throwable t) {
                ShowToast.show(getApplicationContext(), t.getMessage());
            }
        });

    }
}
