package com.citesoft.epis.attendancetracking.login;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.citesoft.epis.attendancetracking.activities.login.LoginActivity;
import com.citesoft.epis.attendancetracking.preferences.Preferences;

public class LogUser {
    private Activity activity;
    private String file = "logUser";
    public LogUser(Activity _activity){
        this.activity = _activity;
    }
    public void login(String _token){
        (new Preferences(this.file, this.activity)).write("token", _token);
    }

    public boolean isLogged(){
        return (new Preferences(this.file, this.activity)).read("token")!=null;
    }

    public void validatelog(){
        if (!isLogged()){
            Intent intent = new Intent (this.activity, LoginActivity.class);
            this.activity.startActivityForResult(intent, 0);
            this.activity.finish();
        }
    }
}
