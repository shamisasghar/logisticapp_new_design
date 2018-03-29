package com.hypernymbiz.logistics.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.adapter.MaintenanceCompleteAdapter;
import com.hypernymbiz.logistics.adapter.MaintenanceDueAdapter;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.model.MaintenanceOverdue;
import com.hypernymbiz.logistics.model.WebAPIResponse;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.hypernymbiz.logistics.utils.LoginUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Metis on 19-Mar-18.
 */

public class DueMaintenanceFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MaintenanceDueAdapter maintenanceOverdueAdapter;
    private List<MaintenanceOverdue> maintenanceOverdues;
    String getUserAssociatedEntity;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_pager_maintenance_due, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_maintenance_due);

        layoutManager = new LinearLayoutManager(getContext());


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getContext());
        if (getUserAssociatedEntity != null) {

            ApiInterface.retrofit.getallmaintenanceoverdue(Integer.parseInt(getUserAssociatedEntity),73).enqueue(new Callback<WebAPIResponse<List<MaintenanceOverdue>>>() {
                @Override
                public void onResponse(Call<WebAPIResponse<List<MaintenanceOverdue>>> call, Response<WebAPIResponse<List<MaintenanceOverdue>>> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().status) {
                                // Toast.makeText(getContext(), "List Detail"+Integer.toString(response.body().response.job_info.size()), Toast.LENGTH_SHORT).show();
                                maintenanceOverdues = response.body().response;
                                maintenanceOverdueAdapter = new MaintenanceDueAdapter(maintenanceOverdues);
                                recyclerView.setAdapter(maintenanceOverdueAdapter);
                            }
                        } catch (Exception ex) {
                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));
                        }
                    } else {

                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                    }
                }

                @Override
                public void onFailure(Call<WebAPIResponse<List<MaintenanceOverdue>>> call, Throwable t) {

                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                }
            });
        } else {
            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

        }

        return view;
    }

}


