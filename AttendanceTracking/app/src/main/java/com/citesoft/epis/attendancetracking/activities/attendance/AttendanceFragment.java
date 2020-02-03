package com.citesoft.epis.attendancetracking.activities.attendance;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.citesoft.epis.attendancetracking.BroadcastRecievers.BroadcastRecieverProgramNotitication;
import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.exceptions.BluetoothDisabledException;
import com.citesoft.epis.attendancetracking.exceptions.BluetoothNullPointerException;
import com.citesoft.epis.attendancetracking.exceptions.LocationException;
import com.citesoft.epis.attendancetracking.exceptions.LocationPermissionException;
import com.citesoft.epis.attendancetracking.models.Attendance;
import com.citesoft.epis.attendancetracking.models.ClassRooms;
import com.citesoft.epis.attendancetracking.notifications.Channel;
import com.citesoft.epis.attendancetracking.notifications.CloseNotification;
import com.citesoft.epis.attendancetracking.services.attendanceTracking.AttendanceTrackingRetrofit;
import com.citesoft.epis.attendancetracking.services.beaconDetector.BeaconDetectorService;
import com.citesoft.epis.attendancetracking.toast.ShowToast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.IOException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by harold on 2/7/18.
 */

public class AttendanceFragment extends Fragment  implements BeaconConsumer, RangeNotifier {

    private Button startDetection;
    private Button stopDetection;
    private BeaconDetectorService beaconService;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private AttendanceTrackingRetrofit retrofit;

    private AttendanceAdapter closedAttendanceAdapter;
    private RecyclerView recyclerViewClosedAttendance;
    private AttendanceAdapter openAttendanceAdapter;
    private RecyclerView recyclerViewOpenAttendance;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attendance, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.closedAttendanceAdapter = new AttendanceAdapter(R.layout.list_attendance_item);
        this.recyclerViewClosedAttendance = (RecyclerView) this.getActivity().findViewById(R.id.attendance_close);
        this.recyclerViewClosedAttendance.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this.getActivity());
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        this.recyclerViewClosedAttendance.setLayoutManager(linearLayout);
        this.recyclerViewClosedAttendance.setAdapter(closedAttendanceAdapter);

        this.openAttendanceAdapter = new AttendanceAdapter(R.layout.list_attendance_item_open);
        this.recyclerViewOpenAttendance = (RecyclerView) this.getActivity().findViewById(R.id.attendance_open);
        this.recyclerViewOpenAttendance.setHasFixedSize(true);
        this.recyclerViewOpenAttendance.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        this.recyclerViewOpenAttendance.setAdapter(openAttendanceAdapter);


        this.startDetection = this.getActivity().findViewById(R.id.startReadingBeaconsButton);
        this.stopDetection = this.getActivity().findViewById(R.id.stopReadingBeaconsButton);
        this.beaconService = new BeaconDetectorService(this.getActivity(), this);
        this.startDetection.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {



                startDetectionBeacons();
            }
        });
        this.stopDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopDetectionBeacons();
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                }

                startDetection.setEnabled(true);
                startDetection.setAlpha(1);

                stopDetection.setEnabled(false);
                stopDetection.setAlpha(.5f);
            }
        });
        this.retrofit = new AttendanceTrackingRetrofit();
        this.updateAttendance();

    }


    private void updateAttendance (){
        this.retrofit.getClosedAttendance().enqueue(new Callback<ArrayList<Attendance>>() {
            @Override
            public void onResponse(Call<ArrayList<Attendance>> call, Response<ArrayList<Attendance>> response) {
                if (response.isSuccessful()){

                    ArrayList<Attendance> attendances = response.body();
                    closedAttendanceAdapter.addAttendances(attendances);

                } else {
                    try {
                        ShowToast.show(getActivity(), response.errorBody().string());
                    } catch (IOException e) {
                        ShowToast.show(getActivity(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Attendance>> call, Throwable t) {
                ShowToast.show(getActivity(), t.getMessage());            }
        });


        this.retrofit.getOpenAttendance().enqueue(new Callback<ArrayList<Attendance>>() {
            @Override
            public void onResponse(Call<ArrayList<Attendance>> call, Response<ArrayList<Attendance>> response) {
                if (response.isSuccessful()){

                    ArrayList<Attendance> attendances = response.body();
                    openAttendanceAdapter.addAttendances(attendances);

                } else {
                    try {
                        ShowToast.show(getActivity(), response.errorBody().string());
                    } catch (IOException e) {
                        ShowToast.show(getActivity(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Attendance>> call, Throwable t) {
                ShowToast.show(getActivity(), t.getMessage());            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private  void startDetectionBeacons(){

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        try {
            this.beaconService.start(bluetoothAdapter);

            this.startDetection.setEnabled(false);
            this.startDetection.setAlpha(.5f);
            this.stopDetection.setEnabled(true);
            this.stopDetection.setAlpha(1);
            this.stopDetection.setAlpha(1);



        } catch (LocationPermissionException e) {
            this.askForLocationPermissions();

        } catch (LocationException e) {
            this.askToTurnOnLocation();
        } catch (BluetoothNullPointerException e) {
            ShowToast.show(this.getActivity().getApplicationContext(), R.string.not_support_bluetooth_msg);
        } catch (BluetoothDisabledException e) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.getActivity().startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);

        }


    }

    private  void stopDetectionBeacons(){
        try {
            this.beaconService.stopDetectingBeacons();

        } catch (RemoteException e) {
            ShowToast.show(this.getActivity(), R.string.error_stop_beacon);
        }


    }

    private void askForLocationPermissions() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.location_access_needed);
        builder.setMessage(R.string.grant_location_access);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onDismiss(DialogInterface dialog) {
                getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        });
        builder.show();
    }

    private void askToTurnOnLocation() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
        dialog.setMessage(R.string.location_disabled);
        dialog.setPositiveButton(R.string.location_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(myIntent);
            }
        });
        dialog.show();
    }

    @Override
    public void onBeaconServiceConnect() {

        try {
            this.beaconService.startRangingBeaconsInRegion();
            ShowToast.show(this.getActivity(), R.string.start_looking_for_beacons);

        } catch (RemoteException e) {
            ShowToast.show(this.getActivity(), "Se ha producido una excepción al empezar a buscar beacons " + e.getMessage());
        }

        this.beaconService.addRangeNotifier(this);
    }

    @Override
    public Context getApplicationContext() {
        return this.getActivity().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        this.getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return this.getActivity().bindService(intent, serviceConnection, i);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        if (collection.size() == 0) {
            ShowToast.show(this.getActivity(), R.string.no_beacons_detected);
        } else {
            stopDetectionBeacons();


            for (Beacon beacon : collection) {
                String id = beacon.getId1().toString();
                final String uuid = id.substring(id.indexOf('x') + 1, id.length());
                final boolean[] noContinue = {false};
                this.retrofit.getClassRoomByBeacon(uuid).enqueue(new Callback<ClassRooms>() {
                    @Override
                    public void onResponse(Call<ClassRooms> call, Response<ClassRooms> response) {
                        if (response.isSuccessful()) {
                            noContinue[0] = true;
                            final ClassRooms classRooms = response.body();
                            //ShowToast.show(getActivity(), getString(R.string.beacon_detected, uuid) + " del salon " + classRooms.getName());
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setTitle(classRooms.getName());
                            dialog.setMessage(R.string.attendance_confirmation);

                            dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    stopDetection.callOnClick();

                                    retrofit.takeAttendance(classRooms.getId()).enqueue(new Callback<Attendance>() {
                                        @Override
                                        public void onResponse(Call<Attendance> call, Response<Attendance> response) {

                                            if (response.isSuccessful()){
                                                Attendance attendance = response.body();
                                                if (attendance.getExit()==null){
                                                    openAttendanceAdapter.addAttendaceLast(attendance);

                                                    BroadcastRecieverProgramNotitication notitication = new BroadcastRecieverProgramNotitication();

                                                    notitication.create(getApplicationContext());
                                                    

                                                    /*
                                                    Channel.makeChanel(CHANNEL_ID, getActivity());
                                                    NotificationCompat.Builder notification = new CloseNotification(getApplicationContext(), CHANNEL_ID);
                                                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                                                    notificationManagerCompat.notify(CloseNotification.NOTIFICATION_ID, notification.build());
                                                    */



                                                } else {
                                                    openAttendanceAdapter.makeEmpty();
                                                    closedAttendanceAdapter.addAttendance(0, response.body());
                                                }

                                            } else {

                                                try {


                                                    ShowToast.show(getActivity(), response.errorBody().string());

                                                } catch (IOException e) {

                                                    ShowToast.show(getActivity(), e.getMessage());
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Attendance> call, Throwable t) {
                                            ShowToast.show(getActivity(), t.getMessage());
                                        }
                                    });
                                }
                            });
                            dialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startDetectionBeacons();
                                }
                            });
                            dialog.show();


                        }

                    }


                    @Override
                    public void onFailure(Call<ClassRooms> call, Throwable t) {
                        ShowToast.show(getActivity(), R.string.no_internet_conection);
                    }
                });

            }
        }
    }
}