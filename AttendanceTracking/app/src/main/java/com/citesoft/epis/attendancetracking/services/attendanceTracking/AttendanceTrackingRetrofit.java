package com.citesoft.epis.attendancetracking.services.attendanceTracking;

import com.citesoft.epis.attendancetracking.login.LogUser;
import com.citesoft.epis.attendancetracking.models.ClassRooms;
import com.citesoft.epis.attendancetracking.models.Session;
import com.citesoft.epis.attendancetracking.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttendanceTrackingRetrofit {
    Retrofit retrofit;
    String baseURL = "http://attendancetracking.herokuapp.com/api/";
    AttendanceTrackingService service;

    public AttendanceTrackingRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(AttendanceTrackingService.class);
    }

    public Call<ArrayList<ClassRooms>> getClassRooms(){
        return service.getClassRooms("token " + LogUser.currentLogUser.getSession().getToken());
    }

    public Call<Session> login(String _username, String _password) {
        return this.service.login(_username, _password);
    }

    public Call<User> getCurrentUser(){
        return service.getCurrentUser("token " + LogUser.currentLogUser.getSession().getToken());
    }

    public Call<User> updateCurrentUser(User user){
        return service.updateCurrentUser(
                "token " + LogUser.currentLogUser.getSession().getToken(),
                user.getDni(),
                user.getFirstName(),
                user.getLastname(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public Call<ClassRooms> getClassRoomByBeacon(String _uuid){
        return service.getClasssRoomByBeacon(_uuid);
    }

}
