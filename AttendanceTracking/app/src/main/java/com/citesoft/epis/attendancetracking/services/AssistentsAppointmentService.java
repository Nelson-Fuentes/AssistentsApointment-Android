package com.citesoft.epis.attendancetracking.services;
import com.citesoft.epis.attendancetracking.login.LogUser;
import com.citesoft.epis.attendancetracking.models.ClassRooms;
import com.citesoft.epis.attendancetracking.models.Session;
import com.citesoft.epis.attendancetracking.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AssistentsAppointmentService {
    @GET("classroom/")
    public Call<ArrayList<ClassRooms>> getClassRooms(
            @Header("Authorization") String _token
    );

    @POST("auth/")
    @FormUrlEncoded
    public Call<Session> login(
            @Field("username") String _username,
            @Field("password") String _password
    );

    @GET("user/current/")
    public Call<User> getCurrentUser(
            @Header("Authorization") String _token
    );


    @PUT("user/current/")
    @FormUrlEncoded
    public Call<User> updateCurrentUser(
            @Header("Authorization") String _token,
            @Field("dni") String _dni,
            @Field("first_name") String _first_name,
            @Field("last_name") String _last_name,
            @Field("email") String _email,
            @Field("phone") String _phone
    );


}
