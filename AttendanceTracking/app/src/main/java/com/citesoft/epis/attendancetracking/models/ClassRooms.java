package com.citesoft.epis.attendancetracking.models;

/**
 * Created by harold on 2/7/18.
 */

public class ClassRooms {

    private int classRoomId;
    private Beacon beacon;
    private String name;
    private int status;

    public ClassRooms() {
    }

    public ClassRooms(int classRoomId, Beacon beacon, String name, int status) {
        this.classRoomId = classRoomId;
        this.beacon = beacon;
        this.name = name;
        this.status = status;
    }

    public void setClassRoomId(int classRoomId) {
        this.classRoomId = classRoomId;
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

    public int getClassRoomId() {
        return classRoomId;
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
