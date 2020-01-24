package com.citesoft.epis.attendancetracking.services.beaconDetector;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Range;

import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.exceptions.BluetoothDisabledException;
import com.citesoft.epis.attendancetracking.exceptions.BluetoothNullPointerException;
import com.citesoft.epis.attendancetracking.exceptions.LocationException;
import com.citesoft.epis.attendancetracking.exceptions.LocationPermissionException;
import com.citesoft.epis.attendancetracking.toast.ShowToast;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class BeaconDetectorService {
    private Activity activity;
    private BeaconManager beaconManager;
    private ArrayList<Identifier> identifiers;
    private Region region;
    private BeaconConsumer beaconConsumer;

    private static final String ALL_BEACONS_REGION = "AllBeaconsRegion";
    private static final long DEFAULT_SCAN_PERIOD_MS = 200l;

    public  BeaconDetectorService(Activity _activity, BeaconConsumer _beaconConsumer){
        this.activity = _activity;
        this.beaconManager = BeaconManager.getInstanceForApplication(this.activity);
        this.beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        this.identifiers = new ArrayList<>();
        this.region = new Region(ALL_BEACONS_REGION, identifiers);
        this.beaconConsumer = _beaconConsumer;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void start(BluetoothAdapter _bluetoothAdapter) throws LocationPermissionException, LocationException, BluetoothNullPointerException, BluetoothDisabledException {

         if (this.activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED){
             throw new LocationPermissionException("Location Permission Denied");
        }/* else if (isLocationEnabled()){
             throw new LocationException("Location Denied");
        }*/ else if (_bluetoothAdapter==null){
             ShowToast.show(this.activity, "hola saludos");
             throw new BluetoothNullPointerException("Este dispositivo no soporta bluetooth, no es posible buscar beacons");
        } else if (!_bluetoothAdapter.isEnabled()) {
            throw new BluetoothDisabledException("Bluetooth Disabled");
        } else {
            this.beaconManager.setForegroundScanPeriod(DEFAULT_SCAN_PERIOD_MS);
            this.beaconManager.bind(this.beaconConsumer);
        }
    }

    public void startRangingBeaconsInRegion() throws RemoteException {
        this.beaconManager.startRangingBeaconsInRegion(this.region);
    }

    public void addRangeNotifier(RangeNotifier rangeNotifier){
        this.beaconManager.addRangeNotifier(rangeNotifier);
    }




    private boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) this.activity.getSystemService(Context.LOCATION_SERVICE);
        boolean networkLocationEnabled = false;
        boolean gpsLocationEnabled = false;
        try {
            networkLocationEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            gpsLocationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ShowToast.show(this.activity.getApplicationContext(), R.string.error_localization);
        }
        return networkLocationEnabled || gpsLocationEnabled;
    }

    public void stopDetectingBeacons() throws RemoteException {
        this.beaconManager.stopMonitoringBeaconsInRegion(this.region);
        this.beaconManager.removeAllRangeNotifiers();
        this.beaconManager.unbind(this.beaconConsumer);

    }
}
