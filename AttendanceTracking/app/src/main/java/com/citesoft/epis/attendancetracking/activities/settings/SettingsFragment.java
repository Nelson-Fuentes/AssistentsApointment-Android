package com.citesoft.epis.attendancetracking.activities.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.models.User;
import com.citesoft.epis.attendancetracking.services.attendanceTracking.AttendanceTrackingRetrofit;
import com.citesoft.epis.attendancetracking.toast.ShowToast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by harold on 2/7/18.
 */

public class SettingsFragment extends Fragment {

    private AttendanceTrackingRetrofit retrofit;
    private EditText dni;
    private EditText first_name;
    private EditText last_name;
    private EditText email;
    private EditText phone;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.dni= (EditText) getActivity().findViewById(R.id.dni);
        this.first_name = (EditText) getActivity().findViewById(R.id.first_name);
        this.last_name = (EditText) getActivity().findViewById(R.id.last_name);
        this.email = (EditText) getActivity().findViewById(R.id.email);
        this.phone = (EditText) getActivity().findViewById(R.id.phone);

        Button btn = (Button) this.getActivity().findViewById(R.id.saveBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });


        this.retrofit = new AttendanceTrackingRetrofit();
        this.retrofit.getCurrentUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    currentUser = response.body();
                    updateUser();



                } else {
                    ShowToast.show(getActivity().getApplicationContext(), R.string.bad_user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ShowToast.show(getActivity().getApplicationContext(), R.string.no_internet_conection);

            }
        });

    }


    protected void updateUser(){
        this.dni.setText(this.currentUser.getDni());
        this.first_name.setText(this.currentUser.getFirstName());
        this.last_name.setText(this.currentUser.getLastname());
        this.email.setText(this.currentUser.getEmail());
        this.phone.setText(this.currentUser.getPhone());
    }

    public void saveUser(){
        this.currentUser.setDni(this.dni.getText().toString());
        this.currentUser.setFirstName(this.first_name.getText().toString());
        this.currentUser.setLastname(this.last_name.getText().toString());
        this.currentUser.setEmail(this.email.getText().toString());
        this.currentUser.setPhone(this.phone.getText().toString());


        this.retrofit.updateCurrentUser(this.currentUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){

                    currentUser = response.body();

                    updateUser();
                    ShowToast.show(getActivity().getApplicationContext(), R.string.save_sucessfully);

                } else {
                    try {
                        ShowToast.show(getActivity().getApplicationContext(), response.errorBody().string());
                    } catch (IOException e) {
                        ShowToast.show(getActivity().getApplicationContext(), e.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ShowToast.show(getActivity().getApplicationContext(), R.string.no_internet_conection);

            }
        });
    }


}
