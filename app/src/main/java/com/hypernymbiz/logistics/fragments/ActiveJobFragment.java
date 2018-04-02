package com.hypernymbiz.logistics.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.capur16.digitspeedviewlib.DigitSpeedView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.model.DirectionsJSONParser;
import com.hypernymbiz.logistics.model.JobEnd;
import com.hypernymbiz.logistics.model.StartJob;
import com.hypernymbiz.logistics.model.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.ActiveJobUtils;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.LoginUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Metis on 20-Mar-18.
 */

public class ActiveJobFragment extends Fragment implements View.OnClickListener, ToolbarListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    Dialog complete, cancl;
    Button btn_completejob, btn_failedjob;
    SharedPreferences pref;
    MapView mMapView;
    private GoogleMap googleMap;
    LocationManager locationManager;
    GoogleApiClient googleApiClient;
    Double slat, slng, elat, elng;
    SharedPreferences.Editor editor;
    String getUserAssociatedEntity, actual_end_time;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 1000;  /* 1 sec */
    private long FASTEST_INTERVAL = 500; /* 1/2 sec */
    public static int counter = 0;
    String driverlocation;
    LatLng ll;
    DigitSpeedView digitSpeedView;
    SupportMapFragment supportMapFragment;
    LocationRequest locationRequest;
    Marker marker;
    Boolean check = true;
    ImageView btn_dialog_okk, btn_dialog_cls, btn_dialog_fail_ok, btn_dialog_fail_cls;
    TextView truckspeed, truckstatus,jobStarttime,yourstarttime,jobendtime,jobfromaddress,jobendaddress;
    ImageButton mylocation;
    View view;
    Calendar c;
    String driverendtime;

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Active Job");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_active_job, container, false);
        btn_completejob = (Button) view.findViewById(R.id.btn_complete);
        btn_failedjob = (Button) view.findViewById(R.id.btn_cancel);
        truckspeed = (TextView) view.findViewById(R.id.txt_speed);
        digitSpeedView = (DigitSpeedView) view.findViewById(R.id.digitspeed1);
        truckstatus = (TextView) view.findViewById(R.id.txt_truck_status);
       jobStarttime = (TextView) view.findViewById(R.id.txt_starttime);
       jobendtime = (TextView) view.findViewById(R.id.txt_endtime);
        yourstarttime = (TextView) view.findViewById(R.id.txt_actual_starttime);
        jobfromaddress = (TextView) view.findViewById(R.id.txt_from_address);
        jobendaddress = (TextView) view.findViewById(R.id.txt_to_address);

        pref = getActivity().getSharedPreferences("TAG", MODE_PRIVATE);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getContext());
        context = getContext();
        mylocation = (ImageButton) view.findViewById(R.id.img_location);
        initMap();
        startLocationUpdates();

        c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        actual_end_time = df.format(c.getTime());
        driverendtime = DateFormat.getDateTimeInstance().format(c.getTime());

        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFmt.setTimeZone(TimeZone.getTimeZone("gmt"));
        actual_end_time = outputFmt.format(new Date());

        String starttime,endtime,actualstarttime;

       starttime= pref.getString("Startjob", "");
        endtime=pref.getString("Startend", "");
        actualstarttime=pref.getString("drivertime", "");

        Toast.makeText(context, ""+starttime, Toast.LENGTH_SHORT).show();
//        SimpleDateFormat actualdateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = dateFormat.format(actualstarttime).toString();
//        System.out.println(formattedDate);

        jobStarttime.setText(starttime);
        jobendtime.setText(endtime);
        yourstarttime.setText(AppUtils.getTimedate(actualstarttime));



        btn_completejob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                complete = new Dialog(getContext());
                complete.setContentView(R.layout.dialog_complete_job);
                btn_dialog_okk = (ImageView) complete.findViewById(R.id.img_ok);
                btn_dialog_cls = (ImageView) complete.findViewById(R.id.img_cancel);

                complete.setCanceledOnTouchOutside(false);
                complete.show();
                complete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_dialog_okk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        complete.hide();
                        ActiveJobUtils.clearJobResumed(getContext());
                        String id;
                        id = pref.getString("jobid", "");
//        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
                        HashMap<String, Object> body = new HashMap<>();
                        body.put("job_id", id);
                        body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                        body.put("actual_end_time", actual_end_time);
                        body.put("end_lat_long", driverlocation);

                        ApiInterface.retrofit.endjob(body).enqueue(new Callback<WebAPIResponse<JobEnd>>() {
                            @Override
                            public void onResponse(Call<WebAPIResponse<JobEnd>> call, Response<WebAPIResponse<JobEnd>> response) {
                                if (response.isSuccessful()) {

                                    if (response.body().status) {

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<WebAPIResponse<JobEnd>> call, Throwable t) {

                            }
                        });

//                        ActivityUtils.startActivity(getActivity(), FrameActivity.class, JobNotificationFragment.class.getName(), null);
                        getActivity().onBackPressed();
                        getActivity().finish();

                    }
                });

                btn_dialog_cls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        complete.hide();

                    }
                });


            }
        });
        btn_failedjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancl = new Dialog(getContext());
                cancl.setContentView(R.layout.dialog_cancel_job);
                btn_dialog_fail_ok = (ImageView) cancl.findViewById(R.id.img_failed_ok);
                btn_dialog_fail_cls = (ImageView) cancl.findViewById(R.id.img_failed_cancel);
                cancl.setCanceledOnTouchOutside(false);
                cancl.show();
                cancl.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_dialog_fail_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancl.hide();
                        String id = pref.getString("jobid", "");

                        HashMap<String, Object> body = new HashMap<>();
                        if (id != null) {
                            body.put("job_id", Integer.parseInt(id));
                            body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                            body.put("flag", 54);
                        }
                        ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                            @Override
                            public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().status) {

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<WebAPIResponse<StartJob>> call, Throwable t) {
//

                            }
                        });
                        getActivity().onBackPressed();
                        getActivity().finish();
                        ActiveJobUtils.clearJobResumed(getContext());


                    }
                });

                btn_dialog_fail_cls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancl.hide();
                    }
                });


            }
        });

        return view;

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setTitle(String title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        final MarkerOptions option;

        if (marker != null) {

            marker.remove();
        }
        ll = new LatLng(location.getLatitude(), location.getLongitude());

        if (check == true) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15.8f);
            googleMap.animateCamera(update);
            check = false;
        }
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15.8f);
                googleMap.animateCamera(update);
            }
        });




        option = new MarkerOptions().title("Driver Location").position(new LatLng(location.getLatitude(), location.getLongitude())).icon((BitmapDescriptorFactory.fromResource(R.drawable.ic_location)));
//        option=new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Driver Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        marker = googleMap.addMarker(option);

        Double lat, lng;

        lat = location.getLatitude();
        lng = location.getLongitude();
        driverlocation = lat.toString() + "," + lng.toString();

        int currentspeed = (int) ((location.getSpeed() * 3600) / 1000);

        if(currentspeed<1)
        {


            truckstatus.setText("Idel");
            digitSpeedView.updateSpeed(currentspeed);

        }
        else {
            digitSpeedView.updateSpeed(currentspeed);
            truckspeed.setText(currentspeed);
            truckstatus.setText("Moving");
        }


        Toast.makeText(getContext(), "" + driverlocation, Toast.LENGTH_SHORT).show();




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        slat = Double.parseDouble(pref.getString("Startlat", ""));
        slng = Double.parseDouble(pref.getString("Startlng", ""));
        elat = Double.parseDouble(pref.getString("Endlat", ""));
        elng = Double.parseDouble(pref.getString("Endlng", ""));


        jobfromaddress.setText(AppUtils.getAddress(slat,slng,getContext()));
        jobendaddress.setText(AppUtils.getAddress(elat,elng,getContext()));

        MapsInitializer.initialize(getContext());
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
        final MarkerOptions startmarker, destinationmaker;
        LatLng start = new LatLng(slat, slng);
        LatLng destination = new LatLng(elat, elng);
        destinationmaker = new MarkerOptions().title("Destination Location").position(destination).icon((BitmapDescriptorFactory.fromResource(R.drawable.ic_location)));

        startmarker = new MarkerOptions().title("start Location").position(start).icon((BitmapDescriptorFactory.fromResource(R.drawable.ic_location)));
        googleMap.addMarker(destinationmaker);
        googleMap.addMarker(startmarker);


        String url = getDirectionsUrl(start, destination);
        FetchUrl FetchUrl = new FetchUrl();
        FetchUrl.execute(url);

    }


    private String getDirectionsUrl(LatLng start, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + start.latitude + "," + start.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

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

            ParserTask parserTask = new ParserTask();

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
            } else {

                if (check == true) {
                }
                check = false;

            }
        }
    }

    private void initMap() {
        supportMapFragment.getMapAsync(this);

    }


    @Override
    public void onResume() {
        supportMapFragment.onResume();

        super.onResume();
    }


    @SuppressLint("RestrictedApi")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        final LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        counter++;
                        try {
                            if (counter > 1) {
                                onLocationChanged(locationResult.getLastLocation());
                                LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(this);
                            }
                        } catch (Exception ex) {

                        }
                    }
                },
                Looper.myLooper());
    }


}
