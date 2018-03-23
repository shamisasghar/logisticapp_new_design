package com.hypernymbiz.logistics.fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
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

import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.hypernymbiz.logistics.utils.ActivityUtils;

/**
 * Created by Bilal Rashid on 10/10/2017.
 */

public class HomeFragment extends Fragment implements  View.OnClickListener {



//    private ViewHolder mHolder;
View view;
//    implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener
    MapView mMapView;
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    CoordinatorLayout coordinatorLayout;
    Dialog complete,cancl;
    GoogleApiClient googleApiClient;
    Marker marker, marker2, marker3;
    EditText editText;
    ImageButton img;

    Location location;
    LatLng ll;
    boolean check = true;
    boolean chk = true;
    CameraUpdate update;
    LatLng dest = new LatLng(33.6689488, 72.9939884);
    private final int REQ_CODE_SPEECH_INPUT = 100;
    TextView mNumberOfCartItemsText;
    LinearLayout linear_job,linear_maintenance,linear_violation,linear_inprogress;


    LocationRequest locationRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Dashboard");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        View view = menu.findItem(R.id.notification_bell).getActionView();
        mNumberOfCartItemsText = (TextView) view.findViewById(R.id.text_number_of_cart_items);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(getActivity(), FrameActivity.class, JobDetailsFragment.class.getName(), null);
            }
        });
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_home, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);

        linear_job=(LinearLayout)view.findViewById(R.id.layout_linear_job);
        linear_maintenance=(LinearLayout)view.findViewById(R.id.layout_linear_maintenance);
        linear_violation=(LinearLayout)view.findViewById(R.id.layout_linear_violation);
        linear_inprogress=(LinearLayout)view.findViewById(R.id.layout_linear_inprogress);

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


//        buildGoogleApiClient();
//        initMap();


//        mMapView.getMapAsync(this);






        return view;



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
    }

//    @Override
//    public void onClick(View view) {
//        ActivityUtils.startActivity(getActivity(), FrameActivity.class, HomeFragment.class.getName(), null);
//
//    }
//    protected synchronized void buildGoogleApiClient() {
//        googleApiClient = new GoogleApiClient.Builder(getContext())
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this).build();
//        googleApiClient.connect();
//    }
//    @Override
//    public void onMapReady(final GoogleMap googleMap) {
////        Toast.makeText(getActivity(), "alksdfj", Toast.LENGTH_SHORT).show();
////        init();
//        final MarkerOptions option;
//       googleMap.clear();
//        if (marker != null) {
//
//            marker.remove();
//        }
//        MapsInitializer.initialize(getContext());
//        this.googleMap = googleMap;
////        googleMap.clear();
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//
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
//        MarkerOptions sss;
//        sss = new MarkerOptions().title("fuel").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).position(new LatLng(33.6689488, 72.9939884));
//        marker3 = googleMap.addMarker(sss);
//
//        if (ll!=null) {
//            if(chk==true) {
//                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15.8f);
//                googleMap.animateCamera(update);
//                chk =false;
//            }
//            option = new MarkerOptions().title("location").position(new LatLng(ll.latitude, ll.longitude));
//            marker = googleMap.addMarker(option);
////            Toast.makeText(getActivity(), "location has been get"+Double.toString(ll.latitude), Toast.LENGTH_SHORT).show();
//            String url = getDirectionsUrl(ll, dest);
//            FetchUrl FetchUrl = new FetchUrl();
//            FetchUrl.execute(url);
//        }
//
//
//
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(1000);
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(final Location location) {
//
//
//
//       ll = new LatLng(location.getLatitude(), location.getLongitude());
////        Toast.makeText(getActivity(), "location changed"+Double.toString(ll.latitude), Toast.LENGTH_SHORT).show();
//
//        supportMapFragment.getMapAsync(this);
//
//
//    }
//    private String getDirectionsUrl(LatLng option, LatLng sss) {
//
//        // Origin of route
//        String str_origin = "origin=" + option.latitude + "," + option.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + sss.latitude + "," + sss.longitude;
//
//        // Sensor enabled
//        String sensor = "sensor=false";
//
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor;
//
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//
//        return url;
//    }
//    private String downloadUrl(String strUrl) throws IOException {
//        String data = "";
//        InputStream iStream = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strUrl);
//
//            // Creating an http connection to communicate with url
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//            // Connecting to url
//            urlConnection.connect();
//
//            // Reading data from url
//            iStream = urlConnection.getInputStream();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//            StringBuffer sb = new StringBuffer();
//
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            data = sb.toString();
//            br.close();
//
//        } catch (Exception e) {
//        } finally {
//            iStream.close();
//            urlConnection.disconnect();
//        }
//        return data;
//    }
//


//    private class FetchUrl extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... url) {
//
//            // For storing data from web service
//            String data = "";
//
//            try {
//                // Fetching the data from web service
//                data = downloadUrl(url[0]);
//            } catch (Exception e) {
//
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            HomeFragment.ParserTask parserTask = new HomeFragment.ParserTask();
//
//            // Invokes the thread for parsing the JSON data
//            parserTask.execute(result);
//
//        }
//    }
//
//    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//            JSONObject jObject;
//            List<List<HashMap<String, String>>> routes = null;
//
//            try {
//                jObject = new JSONObject(jsonData[0]);
//                DirectionsJSONParser parser = new DirectionsJSONParser();
//
//                // Starts parsing data
//                routes = parser.parse(jObject);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return routes;
//        }
//
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//            ArrayList<LatLng> points;
//
//            PolylineOptions lineOptions = null;
//            if (result != null) {
//
//                // Traversing through all the routes
//                for (int i = 0; i < result.size(); i++) {
//                    points = new ArrayList<>();
//                    lineOptions = new PolylineOptions();
//                    // Fetching i-th route
//                    List<HashMap<String, String>> path = result.get(i);
//                    // Fetching all the points in i-th route
//                    for (int j = 0; j < path.size(); j++) {
//                        HashMap<String, String> point = path.get(j);
//
//                        double lat = Double.parseDouble(point.get("lat"));
//                        double lng = Double.parseDouble(point.get("lng"));
//                        LatLng position = new LatLng(lat, lng);
//
//                        points.add(position);
//                    }
//
//                    // Adding all the points in the route to LineOptions
//                    lineOptions.addAll(points);
//                    lineOptions.width(10);
//                    lineOptions.color(R.color.colorPrimary);
//                }
//
//                // Drawing polyline in the Google Map for the i-th route
//                if (lineOptions != null) {
//                    googleMap.addPolyline(lineOptions);
//                } else {
//                }
//            }
//            else {
//
//                if(check==true) {
//                }
//                check=false;
//
//            }
//        }
//    }


//    private void initMap() {
//        supportMapFragment.getMapAsync(this);
//
//    }
