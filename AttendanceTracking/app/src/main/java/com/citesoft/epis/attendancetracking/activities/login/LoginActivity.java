package com.citesoft.epis.attendancetracking.activities.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.citesoft.epis.attendancetracking.MainActivity;
import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.login.LogUser;
import com.citesoft.epis.attendancetracking.models.Session;
import com.citesoft.epis.attendancetracking.services.AssistentsAppointmentRetrofit;
import com.citesoft.epis.attendancetracking.toast.ShowToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private AssistentsAppointmentRetrofit retrofit;
    private LogUser logUser;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.login);
        this.retrofit = new AssistentsAppointmentRetrofit();
        return;
    }

    public void login(View _view){
        String username = ((EditText)this.findViewById(R.id.text_username)).getText().toString();
        String password = ((EditText)this.findViewById(R.id.text_password)).getText().toString();
        logUser = new LogUser(this);
        intent = new Intent (this, MainActivity .class);
        this.retrofit.login(username, password).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (response.isSuccessful()){
                    Session session = response.body();
                    LogUser.currentLogUser.login(session.getToken());
                    startActivityForResult(intent, 0);
                    finish();
                } else {
                    ShowToast.show(getApplicationContext(), R.string.bad_user);
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                ShowToast.show(getApplicationContext(), R.string.no_internet_conection);
            }
        });
    }

}
