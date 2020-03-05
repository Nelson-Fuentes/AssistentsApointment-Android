package com.citesoft.epis.attendancetracking.services.attendanceTracking;

import android.content.Context;

import com.citesoft.epis.attendancetracking.toast.ShowToast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallBackTemplate<T> implements Callback<T> {

    private T data;
    private Context context;

    public CallBackTemplate(Context context){
        super();
        this.context = context;
    }
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()){
            this.data = response.body();
            System.out.println(this.data + "--------------------------------------------------------------------------------------------------------");
        } else {
            try {
                ShowToast.show(this.context, response.errorBody().string());
            } catch (IOException e) {
                ShowToast.show(this.context, e.getMessage());
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        ShowToast.show(this.context, t.getMessage());
    }

    public T getData(){
        return this.data;
    }
}
