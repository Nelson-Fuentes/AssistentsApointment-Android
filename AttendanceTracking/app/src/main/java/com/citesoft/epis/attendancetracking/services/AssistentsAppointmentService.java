package com.citesoft.epis.attendancetracking.services;
import com.citesoft.epis.attendancetracking.models.ClassRooms;
import com.citesoft.epis.attendancetracking.models.Session;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AssistentsAppointmentService {
    @GET("classroom/")
    public Call<ArrayList<ClassRooms>> getClassRooms();

    @POST("auth/")
    @FormUrlEncoded
    public Call<Session> login(@Field("username") String _username, @Field("password") String _password);

}
