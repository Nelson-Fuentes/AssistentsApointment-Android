package com.citesoft.epis.attendancetracking.models;

/**
 * Created by harold on 2/7/18.
 */

public class Beacon {

    private String beaconId;
    private String name;
    private int status;

    public Beacon() {
    }

    public Beacon(String beaconId, String name, int status) {
        this.beaconId = beaconId;
        this.name = name;
        this.status = status;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }
}
