package com.citesoft.epis.attendancetracking.activities.attendance;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.models.Attendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by harold on 2/7/18.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {


    private int idLayout;
    private ArrayList<Attendance> dataset;


    public AttendanceAdapter(int _idLayout){
        this.dataset = new ArrayList<Attendance>();
        this.idLayout = _idLayout;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.idLayout, parent, false);
        return new AttendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Attendance attendance = dataset.get(position);
        holder.attendanceExit.setText(attendance.getExit());
        holder.attendanceEntry.setText(attendance.getEntry());
        Locale loc = new Locale("es", "PE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy zzzz", loc);
        holder.attendanceDate.setText(dateFormat.format(attendance.getDate()));
        holder.attendanceClasroom.setText(attendance.getClassRoom().getName());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addAttendances(ArrayList<Attendance> attendances) {
        dataset.addAll(attendances);
        notifyDataSetChanged();
    }

    public void makeEmpty(){
        this.dataset = new ArrayList<Attendance>();
        notifyDataSetChanged();
    }

    public void addAttendance(int i, Attendance _attendance){
        this.dataset.add(i, _attendance);
        notifyDataSetChanged();
    }

    public void addAttendaceLast(Attendance _attendance){
        this.addAttendance(this.dataset.size(), _attendance);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView attendanceClasroom;
        private TextView attendanceDate;
        private TextView attendanceEntry;
        private TextView attendanceExit;

        public ViewHolder(View itemView) {
            super(itemView);
            this.attendanceClasroom = (TextView) itemView.findViewById(R.id.assistance_classroom);
            this.attendanceDate = (TextView) itemView.findViewById(R.id.assistance_date);
            this.attendanceEntry = (TextView) itemView.findViewById(R.id.assistance_entry);
            this.attendanceExit = (TextView) itemView.findViewById(R.id.assistance_exit);
        }
    }

}
