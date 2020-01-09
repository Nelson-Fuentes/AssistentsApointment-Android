package com.citesoft.epis.attendancetracking;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;

public class BeaconDetector {
    private MainActivity view;

    public BeaconManager mBeaconManager;
    public ArrayList<Identifier> identifiers;
    public Region mRegion;

    private static final String ALL_BEACONS_REGION = "AllBeaconsRegion";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final long DEFAULT_SCAN_PERIOD_MS = 200l;



    public BeaconDetector (MainActivity _view){
        this.view = _view;
        mBeaconManager = BeaconManager.getInstanceForApplication(view);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        identifiers = new ArrayList<>();
        mRegion = new Region(ALL_BEACONS_REGION, identifiers);
    }

    public void askForLocationPermissions() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.view);
        builder.setTitle(R.string.location_access_needed);
        builder.setMessage(R.string.grant_location_access);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onDismiss(DialogInterface dialog) {
                view.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        });
        builder.show();
    }

    public void prepareDetection() {
        if (!isLocationEnabled()) {
            askToTurnOnLocation();
        } else {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                showToastMessage(this.view.getString(R.string.not_support_bluetooth_msg));
            } else if (mBluetoothAdapter.isEnabled()) {
                startDetectingBeacons();
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.view.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }

    private void startDetectingBeacons() {
        mBeaconManager.setForegroundScanPeriod(DEFAULT_SCAN_PERIOD_MS);
        mBeaconManager.bind(this.view);
        this.view.getStartButton().setEnabled(false);
        this.view.getStartButton().setAlpha(.5f);
        this.view.getStopButton().setEnabled(true);
        this.view.getStopButton().setAlpha(1);
        this.view.getStopButton().setAlpha(1);
    }

    private void askToTurnOnLocation() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.view);
        dialog.setMessage(R.string.location_disabled);
        dialog.setPositiveButton(R.string.location_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                view.startActivity(myIntent);
            }
        });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) this.view.getSystemService(Context.LOCATION_SERVICE);
        boolean networkLocationEnabled = false;
        boolean gpsLocationEnabled = false;
        try {
            networkLocationEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            gpsLocationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            showToastMessage("Excepción al obtener información de localización");
        }
        return networkLocationEnabled || gpsLocationEnabled;
    }

    public void stopDetectingBeacons() {
        try {
            mBeaconManager.stopMonitoringBeaconsInRegion(mRegion);
            showToastMessage(this.view.getString(R.string.stop_looking_for_beacons));
        } catch (RemoteException e) {
            showToastMessage("Se ha producido una excepción al parar de buscar beacons ");
        }

        mBeaconManager.removeAllRangeNotifiers();

        mBeaconManager.unbind(this.view);

        this.view.getStartButton().setEnabled(true);
        this.view.getStartButton().setAlpha(1);

        this.view.getStopButton().setEnabled(false);
        this.view.getStopButton().setAlpha(.5f);

    }

    public void showToastMessage(String message) {
        Toast toast = Toast.makeText(this.view, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
