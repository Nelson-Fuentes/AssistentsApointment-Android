package com.citesoft.epis.attendancetracking.activities.classrooms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.models.ClassRooms;
import com.citesoft.epis.attendancetracking.services.AssistentsAppointmentRetrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by harold on 2/7/18.
 */

public class ClassRoomFragment extends Fragment {

    private AssistentsAppointmentRetrofit retrofit;

    private RecyclerView recyclerView;
    private ClassRoomListAdapter classRoomListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_classrooms, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        this.recyclerView = (RecyclerView) this.getActivity().findViewById(R.id.recyclerView);
        this.classRoomListAdapter = new ClassRoomListAdapter();
        this.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this.getActivity());
        this.recyclerView.setLayoutManager(linearLayout);
        this.recyclerView.setAdapter(classRoomListAdapter);



        this.retrofit = new AssistentsAppointmentRetrofit();
        this.retrofit.getClassRooms().enqueue(new Callback<ArrayList<ClassRooms>>() {
            @Override
            public void onResponse(Call<ArrayList<ClassRooms>> call, Response<ArrayList<ClassRooms>> response) {
                if (response.isSuccessful()){
                    ArrayList<ClassRooms> classRooms = response.body();
                    System.out.println(classRooms);
                    classRoomListAdapter.addClassRooms(classRooms);

                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ClassRooms>> call, Throwable t) {
                System.out.println("Sin conexion a internet");
            }
        });
    }

    private void getData(){

    }
}
