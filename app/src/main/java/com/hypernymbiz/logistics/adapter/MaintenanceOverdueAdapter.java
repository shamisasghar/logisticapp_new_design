package com.hypernymbiz.logistics.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.model.MaintenanceOverdue;
import com.hypernymbiz.logistics.utils.AppUtils;

import java.util.List;

/**
 * Created by Metis on 29-Mar-18.
 */

public class MaintenanceOverdueAdapter extends RecyclerView.Adapter<MaintenanceOverdueAdapter.MyViewHolder> {

    public List<MaintenanceOverdue> maintenanceOverdues;
    public MaintenanceOverdueAdapter(List<MaintenanceOverdue> maintenanceOverdues)
    {

        this.maintenanceOverdues=maintenanceOverdues;
    }


    @Override
    public MaintenanceOverdueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_overdue_maintenance, parent, false);
        return new MaintenanceOverdueAdapter.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MaintenanceOverdueAdapter.MyViewHolder holder, int position) {

        holder.maintenancename.setText(maintenanceOverdues.get(position).getMaintenanceName());
        holder.maintenancestatus.setText(maintenanceOverdues.get(position).getStatus());
        holder.date.setText(AppUtils.getFormattedDate(maintenanceOverdues.get(position).getDueDate()));
        holder.time.setText(AppUtils.getTimedate(maintenanceOverdues.get(position).getDueDate()));
        holder.maintenance_assignedtruck.setText(maintenanceOverdues.get(position).getAssignedTruck());
//        holder.endtime.setTex
        holder.maintenancetype.setText(maintenanceOverdues.get(position).getMaintenanceType());

    }

    @Override
    public int getItemCount() {
        return maintenanceOverdues.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView maintenancename, maintenancestatus, date,time, maintenancetype,maintenance_assignedtruck;
        public MyViewHolder(View itemView) {
            super(itemView);

            maintenancename = (TextView) itemView.findViewById(R.id.txt_maintenance_name);
            maintenancestatus = (TextView) itemView.findViewById(R.id.txt_maintenance_status);
            date = (TextView) itemView.findViewById(R.id.txt_maintenance_date);
            time = (TextView) itemView.findViewById(R.id.txt_maintenance_time);
            maintenancetype = (TextView) itemView.findViewById(R.id.txt_maintenance_type);
            maintenance_assignedtruck = (TextView) itemView.findViewById(R.id.txt_assigned_truck);


        }
    }
}
