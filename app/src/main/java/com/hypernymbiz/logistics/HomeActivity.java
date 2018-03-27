package com.hypernymbiz.logistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.hypernymbiz.logistics.dialog.SimpleDialog;
import com.hypernymbiz.logistics.fragments.HomeFragment;
import com.hypernymbiz.logistics.fragments.JobFragment;
import com.hypernymbiz.logistics.fragments.JobNotificationFragment;
import com.hypernymbiz.logistics.fragments.MaintenanceFragment;
import com.hypernymbiz.logistics.fragments.ProfileFragment;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by Bilal Rashid on 10/10/2017.
 */

public class HomeActivity extends AppCompatActivity implements ToolbarListener,OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private SimpleDialog mSimpleDialog;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbarSetup();
        String fragmentName = getIntent().getStringExtra(Constants.FRAGMENT_NAME);
        Bundle bundle = getIntent().getBundleExtra(Constants.DATA);
        if (!TextUtils.isEmpty(fragmentName)) {
            Fragment fragment = Fragment.instantiate(this, fragmentName);
            if (bundle != null)
                fragment.setArguments(bundle);
            addFragment(fragment);
        } else {
            addFragment(new HomeFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }

    public void addFragment(final Fragment fragment) {
        if(fragment.getClass().getName().equals(HomeFragment.class.getName())){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(fragment.getClass().getName()
            ).commit();
        }
    }

    public void toolbarSetup() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(" ");
        ActivityUtils.centerToolbarTitle(mToolbar,false);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if(fragment instanceof HomeFragment){
                mSimpleDialog = new SimpleDialog(this, null, getString(R.string.msg_exit),
                    getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.button_positive:
                            mSimpleDialog.dismiss();
                            HomeActivity.this.finish();
                            break;
                        case R.id.button_negative:
                            mSimpleDialog.dismiss();
                            break;
                    }
                }
            });
            mSimpleDialog.show();
            }else {
                super.onBackPressed();
            }

//            mSimpleDialog = new SimpleDialog(this, null, getString(R.string.msg_exit),
//                    getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    switch (view.getId()) {
//                        case R.id.button_positive:
//                            mSimpleDialog.dismiss();
//                            HomeActivity.this.finish();
//                            break;
//                        case R.id.button_negative:
//                            mSimpleDialog.dismiss();
//                            break;
//                    }
//                }
//            });
//            mSimpleDialog.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.home) {

            if (!AppUtils.isInternetAvailable(this)) {
                mSimpleDialog = new SimpleDialog(this, getString(R.string.title_internet), getString(R.string.msg_internet),
                        getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                mSimpleDialog.dismiss();
                                ActivityUtils.startWifiSettings(HomeActivity.this);
                                break;
                            case R.id.button_negative:
                                mSimpleDialog.dismiss();
                                break;
                        }
                    }
                });
                mSimpleDialog.show();
                return true;
            }
            addFragment(new HomeFragment());
        }
        else if (id == R.id.jobs) {

            if (!AppUtils.isInternetAvailable(this)) {
                mSimpleDialog = new SimpleDialog(this, getString(R.string.title_internet), getString(R.string.msg_internet),
                        getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                mSimpleDialog.dismiss();
                                ActivityUtils.startWifiSettings(HomeActivity.this);
                                break;
                            case R.id.button_negative:
                                mSimpleDialog.dismiss();
                                break;
                        }
                    }
                });
                mSimpleDialog.show();
                return true;
            }
            addFragment(new JobFragment());

        }

        else if (id == R.id.profile) {
        if (!AppUtils.isInternetAvailable(this)) {
            mSimpleDialog = new SimpleDialog(this, getString(R.string.title_internet), getString(R.string.msg_internet),
                    getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.button_positive:
                            mSimpleDialog.dismiss();
                            ActivityUtils.startWifiSettings(HomeActivity.this);
                            break;
                        case R.id.button_negative:
                            mSimpleDialog.dismiss();
                            break;
                    }
                }
            });
            mSimpleDialog.show();
            return true;
        }
        addFragment(new ProfileFragment());
    }
        else if (id == R.id.maintenance) {
                if (!AppUtils.isInternetAvailable(this)) {
                    mSimpleDialog = new SimpleDialog(this, getString(R.string.title_internet), getString(R.string.msg_internet),
                            getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.button_positive:
                                    mSimpleDialog.dismiss();
                                    ActivityUtils.startWifiSettings(HomeActivity.this);
                                    break;
                                case R.id.button_negative:
                                    mSimpleDialog.dismiss();
                                    break;
                            }
                        }
                    });
                    mSimpleDialog.show();
                    return true;
                }
                addFragment(new MaintenanceFragment());
        }
        else if (id == R.id.notification) {
            if (!AppUtils.isInternetAvailable(this)) {
                mSimpleDialog = new SimpleDialog(this, getString(R.string.title_internet), getString(R.string.msg_internet),
                        getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                mSimpleDialog.dismiss();
                                ActivityUtils.startWifiSettings(HomeActivity.this);
                                break;
                            case R.id.button_negative:
                                mSimpleDialog.dismiss();
                                break;
                        }
                    }
                });
                mSimpleDialog.show();
                return true;
            }
            ActivityUtils.startActivity(this,FrameActivity.class,JobNotificationFragment.class.getName(),null);

        }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
