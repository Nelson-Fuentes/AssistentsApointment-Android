package com.citesoft.epis.attendancetracking.toast;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ShowToast {

    public static void show(Context _context, int _message){
        Toast.makeText(_context,_context.getString(_message), Toast.LENGTH_SHORT).show();
    }

    public static void show(Context _context, String _message){
        Toast.makeText(_context,_message, Toast.LENGTH_SHORT).show();
    }
}
