package com.citesoft.epis.attendancetracking.activities.classrooms;

/**
 * Created by harold on 2/7/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import com.citesoft.epis.attendancetracking.R;
import com.citesoft.epis.attendancetracking.models.ClassRooms;

public class ClassRoomListAdapter  extends RecyclerView.Adapter<ClassRoomListAdapter.ViewHolder> {

    private ArrayList<ClassRooms> dataset;
    //private Context context;

    public ClassRoomListAdapter() {
      //  this.context = context;
        dataset = new ArrayList<>();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classroom, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ClassRooms classRoom = dataset.get(position);
        holder.classRoomName.setText(classRoom.getName());
        holder.classRoomBeacon.setText(classRoom.getBeacon().getBeaconId());
    }

    public int getItemCount() {
        return dataset.size();
    }

    public void addClassRooms(ArrayList<ClassRooms> classRooms) {
        dataset.addAll(classRooms);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView classRoomName;
        private TextView classRoomBeacon;

        public ViewHolder(View itemView) {
            super(itemView);

            classRoomName = (TextView) itemView.findViewById(R.id.ClassRoomName);
            classRoomBeacon = (TextView) itemView.findViewById(R.id.ClassRoomBeacon);
        }
    }
}
