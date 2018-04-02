package com.hypernymbiz.logistics.fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.capur16.digitspeedviewlib.DigitSpeedView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.dialog.LoadingDialog;
import com.hypernymbiz.logistics.model.ActiveJobResume;
import com.hypernymbiz.logistics.model.DirectionsJSONParser;
import com.hypernymbiz.logistics.model.JobCount;
import com.hypernymbiz.logistics.model.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.hypernymbiz.logistics.utils.ActiveJobUtils;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bilal Rashid on 10/10/2017.
 */

public class HomeFragment extends Fragment  implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    //    private ViewHolder mHolder;
    View view;
    Dialog lastjob;
    MapView mMapView;
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    CoordinatorLayout coordinatorLayout;
    Dialog complete, cancl;
    GoogleApiClient googleApiClient;
    Marker marker, marker2, marker3;
    EditText editText;
    ImageButton mylocation;
    LoadingDialog dialog;
    String size;
    TextView fromaddress_inprogress,toaddress_inprogress,starttime_inprogress,endtime_inprogress,jobname_inprogress;

    Location location;
    LatLng ll;
    boolean check = true;
    boolean checklocation = true;
    CameraUpdate update;
    LatLng dest = new LatLng(33.6689488, 72.9939884);
    TextView mNumberOfCartItemsText,turckstatus;
    LinearLayout linear_job, linear_maintenance, linear_violation, linear_inprogress,linear_lastjob;
    LocationRequest locationRequest;
    Context mContext;
    CardView mapcardalayout;
    DigitSpeedView digitSpeedView;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HomeFragment.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        boolean checkJobResume=ActiveJobUtils.isJobResumed(getContext());
//        Toast.makeText(mContext, "checkjob resume status"+Boolean.toString(checkJobResume), Toast.LENGTH_SHORT).show();

//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Dashboard");
        }
        mContext = context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        View view = menu.findItem(R.id.notification_bell).getActionView();
        mNumberOfCartItemsText = (TextView) view.findViewById(R.id.text_number_of_cart_items);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(getActivity(), FrameActivity.class, JobNotificationFragment.class.getName(), null);
            }
        });
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        linear_job = (LinearLayout) view.findViewById(R.id.layout_linear_job);
        mapcardalayout = (CardView) view.findViewById(R.id.cardviewhome_map);
        linear_maintenance = (LinearLayout) view.findViewById(R.id.layout_linear_maintenance);
        linear_violation = (LinearLayout) view.findViewById(R.id.layout_linear_violation);
        linear_inprogress = (LinearLayout) view.findViewById(R.id.layout_linear_inprogress);
        linear_lastjob=(LinearLayout) view.findViewById(R.id.layout_linear_lastjob);

        fromaddress_inprogress=(TextView)view.findViewById(R.id.txt_from_address);
        toaddress_inprogress=(TextView)view.findViewById(R.id.txt_to_address);
        turckstatus=(TextView)view.findViewById(R.id.txt_truck_status);
        starttime_inprogress=(TextView)view.findViewById(R.id.txt_starttime);
        endtime_inprogress=(TextView)view.findViewById(R.id.txt_endtime);
        jobname_inprogress=(TextView)view.findViewById(R.id.txt_job_name);
        mylocation=(ImageButton) view.findViewById(R.id.btn_mylocation);
        digitSpeedView=(DigitSpeedView) view.findViewById(R.id.digitspeed1);

        dialog = new LoadingDialog(getActivity(), getString(R.string.msg_loading));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();





        if (!ActiveJobUtils.isJobResumed(getContext())){
            linear_inprogress.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    800
            );
            mapcardalayout.setLayoutParams(params);
        }
        else{
            linear_inprogress.setVisibility(View.VISIBLE);
            ActiveJobResume activeJobResume=ActiveJobUtils.getJobResume(getContext());
            String startAddress = AppUtils.getAddress(activeJobResume.getSlat(), activeJobResume.getSlong(), getContext());
            String endAddress = AppUtils.getAddress(activeJobResume.getElat(), activeJobResume.getElong(), getContext());
            String departtime=activeJobResume.getStart_time();
            String arrivaltime=activeJobResume.getEnd_time();
            String jobname=activeJobResume.getJobname();

            fromaddress_inprogress.setText(startAddress);
            toaddress_inprogress.setText(endAddress);
            starttime_inprogress.setText(departtime);
            endtime_inprogress.setText(arrivaltime);
            jobname_inprogress.setText(jobname);


        }



        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        //      init_persistent_bottomsheet();

        linear_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(getActivity(), FrameActivity.class, JobFragment.class.getName(), null);

//                Toast.makeText(getContext(), "hiii", Toast.LENGTH_SHORT).show();
            }
        });
        linear_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(getActivity(), FrameActivity.class, MaintenanceFragment.class.getName(), null);

//                Toast.makeText(getContext(), "hiii", Toast.LENGTH_SHORT).show();
            }
        });

        linear_violation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityUtils.startActivity(getActivity(), FrameActivity.class, JobFragment.class.getName(), null);

                Toast.makeText(getContext(), "violations", Toast.LENGTH_SHORT).show();
            }
        });
        linear_inprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(getActivity(), FrameActivity.class, ActiveJobFragment.class.getName(), null);

//                Toast.makeText(getContext(), "violations", Toast.LENGTH_SHORT).show();
            }
        });

        linear_lastjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "lastjob", Toast.LENGTH_SHORT).show();
                complete = new Dialog(getContext());
                complete.setContentView(R.layout.dialog_complete_job);
                complete.setCanceledOnTouchOutside(false);
                complete.show();
                complete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });


        buildGoogleApiClient();
        initMap();


//        mMapView.getMapAsync(this);


        return view;


    }


    @Override
    public void onResume() {
        super.onResume();
        if (!ActiveJobUtils.isJobResumed(getContext())){
            linear_inprogress.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    800
            );
            mapcardalayout.setLayoutParams(params);

        }
        else{
            linear_inprogress.setVisibility(View.VISIBLE);
            ActiveJobResume activeJobResume=ActiveJobUtils.getJobResume(getContext());
            String startAddress = AppUtils.getAddress(activeJobResume.getSlat(), activeJobResume.getSlong(), getContext());
            String endAddress = AppUtils.getAddress(activeJobResume.getElat(), activeJobResume.getElong(), getContext());
            String departtime=activeJobResume.getStart_time();
            String arrivaltime=activeJobResume.getEnd_time();
            String jobname=activeJobResume.getJobname();

            fromaddress_inprogress.setText(startAddress);
            toaddress_inprogress.setText(endAddress);
            starttime_inprogress.setText(departtime);
            endtime_inprogress.setText(arrivaltime);
            jobname_inprogress.setText(jobname);
        }
        if (mContext instanceof ToolbarListener) {
            ((ToolbarListener) mContext).setTitle("Dashboard");
        }
        ApiInterface.retrofit.getcount().enqueue(new Callback<WebAPIResponse<List<JobCount>>>() {
            @Override
            public void onResponse(Call<WebAPIResponse<List<JobCount>>> call, Response<WebAPIResponse<List<JobCount>>> response) {
                 dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        size = String.valueOf(response.body().response.get(0).getCount());
                        if (size != null) {
                            try {
                                mNumberOfCartItemsText.setText(size);
                            } catch (Exception ex) {

                            }
                        }

                    }

                } else {
                     dialog.dismiss();
                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                }
                //   Toast.makeText(getActivity(),response.body().response.get(0).getCount(), Toast.LENGTH_SHORT).show();
//                mNumberOfCartItemsText.setText("000");
            }

            @Override
            public void onFailure(Call<WebAPIResponse<List<JobCount>>> call, Throwable t) {
                  dialog.dismiss();
                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

            }
        });


    }


    public void init_persistent_bottomsheet() {
        View persistentbottomSheet = coordinatorLayout.findViewById(R.id.bottomsheet);
//        iv_trigger = (ImageView) persistentbottomSheet.findViewById(R.id.iv_fab);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(persistentbottomSheet);


//        iv_trigger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
//            }
//        });
        if (behavior != null)
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    //showing the different states
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    // React to dragging events

                }
            });


    }

    @Override
    public void onClick(View v) {

    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mHolder = new ViewHolder(view);
//
//        mHolder.button.setOnClickListener(this);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        toolbar.setOnClickListener(this);
//    }


//    @Override
//    public void onClick(View view) {
//        ActivityUtils.startActivity(getActivity(), FrameActivity.class, HomeFragment.class.getName(), null);
//
//    }
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
//        Toast.makeText(getActivity(), "alksdfj", Toast.LENGTH_SHORT).show();

        final MarkerOptions option;
       googleMap.clear();
        if (marker != null) {

            marker.remove();
        }
        MapsInitializer.initialize(getContext());
        this.googleMap = googleMap;
//        googleMap.clear();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15.8f);
//                googleMap.animateCamera(update);
//            }
//        });


//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "hhh", Toast.LENGTH_SHORT).show();
//                String txt=editText.getText().toString();
//                if(txt.equals("abc"))
//                {
//                    MarkerOptions sss;
//                    sss =new MarkerOptions().title("violation").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
//                            .position(new LatLng(location.getLatitude(),location.getLongitude()));
//                    marker2= googleMap.addMarker(sss);
//
//                }
//            }
//        });
        if (ActiveJobUtils.isJobResumed(getContext())){
            MarkerOptions destinationaddress,startaddress;
            destinationaddress = new MarkerOptions().title("destination Address").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).position(new LatLng(ActiveJobUtils.getJobResume(getContext()).getElat(), ActiveJobUtils.getJobResume(getContext()).getElong()));
            googleMap.addMarker(destinationaddress);
            startaddress = new MarkerOptions().title("start Address").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).position(new LatLng(ActiveJobUtils.getJobResume(getContext()).getSlat(), ActiveJobUtils.getJobResume(getContext()).getSlong()));
            googleMap.addMarker(startaddress);

        }

        if (ll!=null) {
            if(checklocation==true) {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15.8f);
                googleMap.animateCamera(update);
                checklocation =false;
            }
            mylocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15.8f);
                    googleMap.animateCamera(update);
                }
            });



            option = new MarkerOptions().title("location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).position(new LatLng(ll.latitude, ll.longitude));
            marker = googleMap.addMarker(option);
//            Toast.makeText(getActivity(), "location has been get"+Double.toString(ll.latitude), Toast.LENGTH_SHORT).show();
            if (ActiveJobUtils.isJobResumed(getContext())){
                ActiveJobResume activeJobResume=ActiveJobUtils.getJobResume(getContext());

//                String url = getDirectionsUrl(new LatLng(activeJobResume.getSlat(),activeJobResume.getSlong()), new LatLng(activeJobResume.getElat(),activeJobResume.getElong()));
                String url = getDirectionsUrl(new LatLng(activeJobResume.getSlat(),activeJobResume.getSlong()), new LatLng(activeJobResume.getElat(),activeJobResume.getElong()));
                FetchUrl FetchUrl = new FetchUrl();
                FetchUrl.execute(url);
            }

        }



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(final Location location) {



        int currentspeed = (int) ((location.getSpeed() * 3600) / 1000);

        if(currentspeed<1)
        {

//            truckspeed.setText("0");
            turckstatus.setText("Idel");
            digitSpeedView.updateSpeed(currentspeed);

        }
        else {
            digitSpeedView.updateSpeed(currentspeed);
//            truckspeed.setText(currentspeed);
            turckstatus.setText("Moving");
        }



       ll = new LatLng(location.getLatitude(), location.getLongitude());
//        Toast.makeText(getActivity(), "location changed"+Double.toString(ll.latitude), Toast.LENGTH_SHORT).show();

        supportMapFragment.getMapAsync(this);



    }
    private String getDirectionsUrl(LatLng option, LatLng sss) {

        // Origin of route
        String str_origin = "origin=" + option.latitude + "," + option.longitude;

        // Destination of route
        String str_dest = "destination=" + sss.latitude + "," + sss.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            HomeFragment.ParserTask parserTask = new HomeFragment.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;

            PolylineOptions lineOptions = null;
            if (result != null) {

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);
                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(R.color.colorPrimary);
                }

                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    googleMap.addPolyline(lineOptions);
                } else {
                }
            }
            else {

                if(check==true) {
                }
                check=false;

            }
        }
    }


    private void initMap() {
        supportMapFragment.getMapAsync(this);

    }

}