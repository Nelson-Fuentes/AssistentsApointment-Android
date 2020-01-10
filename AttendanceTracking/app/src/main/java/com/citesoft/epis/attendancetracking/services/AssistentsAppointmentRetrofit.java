package com.citesoft.epis.attendancetracking.services;

import com.citesoft.epis.attendancetracking.login.LogUser;
import com.citesoft.epis.attendancetracking.models.ClassRooms;
import com.citesoft.epis.attendancetracking.models.Session;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.converter.gson.GsonConverterFactory;

public class AssistentsAppointmentRetrofit {
    Retrofit retrofit;
    String baseURL = "http://assistentsappointment.herokuapp.com/api/";
    AssistentsAppointmentService service;

    public AssistentsAppointmentRetrofit (){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(AssistentsAppointmentService.class);
    }

    public Call<ArrayList<ClassRooms>> getClassRooms(){
        return service.getClassRooms("token " + LogUser.currentLogUser.getSession().getToken());
    }

    public Call<Session> login(String _username, String _password) {
        return this.service.login(_username, _password);
    }

}
