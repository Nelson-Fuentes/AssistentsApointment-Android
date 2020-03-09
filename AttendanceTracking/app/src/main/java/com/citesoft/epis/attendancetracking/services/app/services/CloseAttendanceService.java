package com.citesoft.epis.attendancetracking.services.app.services;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.citesoft.epis.attendancetracking.BroadcastRecievers.BroadcastRecieverProgramNotitication;
import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.activities.attendance.AttendanceAdapter;
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
    private String token;


    private AttendanceAdapter closedAttendanceAdapter;
    private RecyclerView recyclerViewClosedAttendance;
    private AttendanceAdapter openAttendanceAdapter;
    private RecyclerView recyclerViewOpenAttendance;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle extras = intent.getExtras();
        if(extras == null)
            Log.d("Service","null");
        else
        {
            Log.d("Service","not null");
            token = (String) extras.get("token");
            Log.d("Service", token);

            notitication = new BroadcastRecieverProgramNotitication();

            retrofit = new AttendanceTrackingRetrofit();
            retrofit.getClosedAttendance(this.token).enqueue(new Callback<ArrayList<Attendance>>() {
                @Override
                public void onResponse(Call<ArrayList<Attendance>> call, Response<ArrayList<Attendance>> response) {
                    if (!response.isSuccessful()){
                        try {
                            ShowToast.show(getApplicationContext(), response.errorBody().string());
                        } catch (IOException e) {
                            ShowToast.show(getApplicationContext(), e.getMessage());
                        }

                    } else  if (response.body().size()==0){
                        cancelServices();
                    } else {
                        retrofit.closeAttendances(token).enqueue(new Callback<ArrayList<Attendance>>() {
                            @Override
                            public void onResponse(Call<ArrayList<Attendance>> call, Response<ArrayList<Attendance>> response) {
                                if (response.isSuccessful()){
                                    ShowToast.show(getApplicationContext(), R.string.attendance_closed);
                                    cancelServices();
                                 //   updateAttendances();




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

    @Override
    public void onCreate() {




    }

    private  void cancelServices(){
        NotificationManager notificationManager = ((NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE));
        notificationManager.cancel(CloseNotification.NOTIFICATION_ID);
        notitication.cancel(getApplicationContext());
        Intent service1 = new Intent(getApplicationContext(), ProgrammingCloseNotificationService.class);
        getApplicationContext().stopService(service1);
        Intent service = new Intent(getApplicationContext(), CloseAttendanceService.class);
        getApplicationContext().stopService(service);

    }

    @SuppressLint("ResourceType")
    private void updateAttendances (){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fragment_attendance, null);
        this.closedAttendanceAdapter = new AttendanceAdapter(R.layout.list_attendance_item);
        this.recyclerViewClosedAttendance = (RecyclerView) layout.findViewById(R.id.attendance_close); //inflater.inflate(R.id.attendance_close, null);  // this.getActivity().findViewById(R.id.attendance_close);
        this.recyclerViewClosedAttendance.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        this.recyclerViewClosedAttendance.setLayoutManager(linearLayout);
        this.recyclerViewClosedAttendance.setAdapter(closedAttendanceAdapter);

        this.openAttendanceAdapter = new AttendanceAdapter(R.layout.list_attendance_item_open);
        this.recyclerViewOpenAttendance = (RecyclerView)  layout.findViewById(R.id.attendance_open);//inflater.inflate(R.id.attendance_open, null); //this.getActivity().findViewById(R.id.attendance_open);

        this.recyclerViewOpenAttendance.setHasFixedSize(true);
        this.recyclerViewOpenAttendance.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerViewOpenAttendance.setAdapter(openAttendanceAdapter);
        this.retrofit.getClosedAttendance().enqueue(new Callback<ArrayList<Attendance>>() {
            @Override
            public void onResponse(Call<ArrayList<Attendance>> call, Response<ArrayList<Attendance>> response) {
                if (response.isSuccessful()){

                    ArrayList<Attendance> attendances = response.body();
                    closedAttendanceAdapter.makeEmpty();
                    closedAttendanceAdapter.addAttendances(attendances);

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
                ShowToast.show(getApplicationContext(), t.getMessage());            }
        });


        this.retrofit.getOpenAttendance().enqueue(new Callback<ArrayList<Attendance>>() {
            @Override
            public void onResponse(Call<ArrayList<Attendance>> call, Response<ArrayList<Attendance>> response) {
                if (response.isSuccessful()){

                    ArrayList<Attendance> attendances = response.body();

                    openAttendanceAdapter.makeEmpty();
                    openAttendanceAdapter.addAttendances(attendances);

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
                ShowToast.show(getApplicationContext(), t.getMessage());            }
        });

    }
}
