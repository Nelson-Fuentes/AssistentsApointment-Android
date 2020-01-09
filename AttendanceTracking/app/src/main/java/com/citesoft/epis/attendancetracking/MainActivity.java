package com.citesoft.epis.attendancetracking;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;

import com.citesoft.epis.attendancetracking.activities.login.LoginActivity;
import com.citesoft.epis.attendancetracking.activities.attendance.AttendanceFragment;
import com.citesoft.epis.attendancetracking.activities.classrooms.ClassRoomFragment;
import com.citesoft.epis.attendancetracking.activities.settings.SettingsFragment;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MainActivity extends AppCompatActivity  implements BeaconConsumer, RangeNotifier {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private BeaconDetector beaconDetector;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        beaconDetector = new BeaconDetector(this);
        //this.startDetection();

        Intent intent = new Intent (this, LoginActivity.class);
        startActivityForResult(intent, 0);
        this.finish();

    }

    public void startDetection(View _view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                beaconDetector.askForLocationPermissions();


            } else {
                beaconDetector.prepareDetection();
            }
        } else {
            beaconDetector.prepareDetection();
        }
    }

    public Button getStartButton() {
        return (Button) findViewById(R.id.startReadingBeaconsButton);
    }

    public Button getStopButton() {
        return (Button) findViewById(R.id.stopReadingBeaconsButton);
    }

    public void stopDetection(View _view){
        beaconDetector.stopDetectingBeacons();

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
    }

    @Override
    public void onBeaconServiceConnect() {

        try {
            // Empezar a buscar los beacons que encajen con el el objeto Región pasado, incluyendo
            // actualizaciones en la distancia estimada
            this.beaconDetector.mBeaconManager.startRangingBeaconsInRegion(this.beaconDetector.mRegion);

            this.beaconDetector.showToastMessage(getString(R.string.start_looking_for_beacons));

        } catch (RemoteException e) {
            this.beaconDetector.showToastMessage("Se ha producido una excepción al empezar a buscar beacons " + e.getMessage());

        }

        this.beaconDetector.mBeaconManager.addRangeNotifier(this);
    }


    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

        if (beacons.size() == 0) {
            this.beaconDetector.showToastMessage(getString(R.string.no_beacons_detected));
        }

        for (Beacon beacon : beacons) {
            this.beaconDetector.showToastMessage(getString(R.string.beacon_detected, beacon.getId1())+" a una distancia de "+beacons.iterator().next().getDistance()+" metros.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            Log.d("position", "" + position);

            switch (position) {
                case 0:
                    fragment = new AttendanceFragment();
                    break;
                case 1:
                    fragment = new ClassRoomFragment();
                    break;
                case 2:
                    fragment = new SettingsFragment();
                    break;
            }

            return fragment;

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ASISTENCIAS";
                case 1:
                    return "AULAS";
                case 2:
                    return "CONFIGURACION";
            }
            return null;
        }
    }


}
