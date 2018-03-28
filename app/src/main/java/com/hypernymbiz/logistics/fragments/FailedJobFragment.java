package com.hypernymbiz.logistics.fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.adapter.FailedJobAdapter;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.model.JobInfo_;
import com.hypernymbiz.logistics.model.Respone_Completed_job;
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

public class FailedJobFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FailedJobAdapter failedJobAdapter;
    private List<JobInfo_> jobInfo_s;
    String getUserAssociatedEntity;
    ConstraintLayout rootlayout;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_pager_job_failed, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view_failed);
        rootlayout = (ConstraintLayout) view.findViewById(R.id.layout_contraint);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        getUserAssociatedEntity= LoginUtils.getUserAssociatedEntity(getContext());
        if (getUserAssociatedEntity!=null) {
            ApiInterface.retrofit.getalldata(Integer.parseInt(getUserAssociatedEntity), 54).enqueue(new Callback<WebAPIResponse<Respone_Completed_job>>() {
                @Override
                public void onResponse(Call<WebAPIResponse<Respone_Completed_job>> call, Response<WebAPIResponse<Respone_Completed_job>> response) {

                    if (response.isSuccessful()) {
                        try {


                            if (response.body().status) {

                                // Toast.makeText(getContext(), "List Detail"+Integer.toString(response.body().response.job_info.size()), Toast.LENGTH_SHORT).show();
                                jobInfo_s = response.body().response.job_info;
                                failedJobAdapter = new FailedJobAdapter(jobInfo_s);
                                recyclerView.setAdapter(failedJobAdapter);
                            }
                        }
                        catch (Exception ex)
                        {
                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));


                        }
                    }else {

                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                    }
                }

                @Override
                public void onFailure(Call<WebAPIResponse<Respone_Completed_job>> call, Throwable t) {
                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                }
            });
        }
        else
        {
            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

        }

        return view;
    }

}
