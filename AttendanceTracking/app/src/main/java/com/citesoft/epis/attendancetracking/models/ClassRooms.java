package com.citesoft.epis.attendancetracking.models;

/**
 * Created by harold on 2/7/18.
 */

public class ClassRooms {

    private int id;
    private Beacon beacon;
    private String name;
    private int status;

    public ClassRooms() {
    }

    public ClassRooms(int classRoomId, Beacon beacon, String name, int status) {
        this.id = classRoomId;
        this.beacon = beacon;
        this.name = name;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }
}
