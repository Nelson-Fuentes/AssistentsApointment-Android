package com.citesoft.epis.attendancetracking.services;
import com.citesoft.epis.attendancetracking.models.ClassRooms;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AssistentsAppointmentService {
    @GET("classroom/")
    public Call<ArrayList<ClassRooms>> getClassRooms();

}
