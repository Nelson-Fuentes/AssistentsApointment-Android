package com.citesoft.epis.attendancetracking.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.citesoft.epis.attendancetracking.toast.ShowToast;

public class Preferences {

//    public static final String file = "login";

    private SharedPreferences preferences;
    private String file;
    public Preferences (String _file, Activity _activity){
        preferences = _activity.getApplicationContext().getSharedPreferences(_file, Context.MODE_PRIVATE);//_activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void write(String _key, String _value){
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(_key, _value);
        editor.commit();
    }

    public String read (String _key){
        return this.preferences.getString(_key, null);
    }
}
