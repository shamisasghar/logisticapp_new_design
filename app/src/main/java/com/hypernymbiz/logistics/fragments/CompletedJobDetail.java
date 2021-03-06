package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.model.JobDetail;
import com.hypernymbiz.logistics.model.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Metis on 28-Mar-18.
 */

public class CompletedJobDetail extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    View view;
    TextView fromaddress,toaddress,departtime,actual_departtime,endtime,actual_endtime,jobstatus;
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    GoogleApiClient googleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Job Details");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_complete_job, container, false);

        jobstatus=(TextView)view.findViewById(R.id.txt_job_status);
        fromaddress=(TextView)view.findViewById(R.id.txt_from_address);
        departtime=(TextView)view.findViewById(R.id.txt_job_starttime);
        actual_departtime=(TextView)view.findViewById(R.id.txt_actual_starttime);
        toaddress=(TextView)view.findViewById(R.id.txt_to_address);
        endtime=(TextView)view.findViewById(R.id.txt_job_endtime);
        actual_endtime=(TextView)view.findViewById(R.id.txt_actual_endtime);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

        buildGoogleApiClient();
        initMap();


        Intent getintent = getActivity().getIntent();
        String id = getintent.getStringExtra("jobid");


//        ApiInterface.retrofit.getalldata(Integer.parseInt(id)).enqueue(new Callback<WebAPIResponse<JobDetail>>() {
//            @Override
//            public void onResponse(Call<WebAPIResponse<JobDetail>> call, Response<WebAPIResponse<JobDetail>> response) {
//
//                if(response.isSuccessful())
//                {
//                    try {
//                        jobstatus.setText(response.body().response.getJobStatus());
//
//                    }
//                    catch (Exception ex)
//                    {
//
//                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));
//
//
//                    }
//
//
//                }
//
//                else {
//
//                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<WebAPIResponse<JobDetail>> call, Throwable t) {
//
//            }
//        });






        return view;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapStyleOptions mapStyleOptions=MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.map);
        googleMap.setMapStyle(mapStyleOptions);

    }


    private void initMap() {
        supportMapFragment.getMapAsync(this);

    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
    }





}
