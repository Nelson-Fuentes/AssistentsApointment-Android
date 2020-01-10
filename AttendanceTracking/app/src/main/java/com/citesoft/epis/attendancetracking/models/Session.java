package com.citesoft.epis.attendancetracking.models;

public class Session {

    private String token;

    public Session(String _token){
        this.token = _token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "{token:\""+this.token+"\"}";
    }
}
