package com.citesoft.epis.attendancetracking.activities.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.citesoft.epis.attendancetracking.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.login);
        return;
    }
}
