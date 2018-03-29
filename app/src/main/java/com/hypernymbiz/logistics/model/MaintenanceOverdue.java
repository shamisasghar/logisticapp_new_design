package com.hypernymbiz.logistics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Metis on 29-Mar-18.
 */

public class MaintenanceOverdue {

    @SerializedName("maintenance_name")
    @Expose
    private String maintenanceName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("due_date")
    @Expose
    private String dueDate;
    @SerializedName("maintenance_type")
    @Expose
    private String maintenanceType;
    @SerializedName("assigned_truck")
    @Expose
    private String assignedTruck;

    public String getMaintenanceName() {
        return maintenanceName;
    }

    public void setMaintenanceName(String maintenanceName) {
        this.maintenanceName = maintenanceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getAssignedTruck() {
        return assignedTruck;
    }

    public void setAssignedTruck(String assignedTruck) {
        this.assignedTruck = assignedTruck;
    }

}
