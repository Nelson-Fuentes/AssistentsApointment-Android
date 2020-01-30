package com.citesoft.epis.attendancetracking.models;

import java.util.Date;
import java.sql.Time;
/**
 * Created by harold on 2/7/18.
 */

public class Attendance {
    private User user;
    private ClassRooms classroom;
    private Date date;
    private String entry;
    private String exit;

    public Attendance(User _user, ClassRooms _classRoom, String _entry, String _exit, Date _date){
        this.user = _user;
        this.classroom = _classRoom;
        this.entry = _entry;
        this.exit = _exit;
        this.date = _date;
    }

    public void setUser(User _user){
        this.user = _user;
    }

    public void setClassRoom(ClassRooms _classRooom){
        this.classroom = _classRooom;
    }

    public void setEntry(String _entry){
        this.entry = _entry;

    }

    public void setExit(String _exit){
        this.exit = _exit;
    }

    public void setDate(Date _date) {
        this.date = _date;
    }

    public User getUser(){
        return this.user;
    }

    public ClassRooms getClassRoom(){
        return this.classroom;
    }

    public String getEntry(){
        return this.entry;
    }

    public String getExit(){
        return this.exit;
    }

    public Date getDate() {
        return  this.date;
    }
}
