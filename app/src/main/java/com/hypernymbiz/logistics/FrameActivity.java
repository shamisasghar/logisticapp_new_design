package com.hypernymbiz.logistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.hypernymbiz.logistics.dialog.SimpleDialog;
import com.hypernymbiz.logistics.enumerations.AnimationEnum;
import com.hypernymbiz.logistics.fragments.ActiveJobFragment;
import com.hypernymbiz.logistics.fragments.HomeFragment;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.Constants;

/**
 * Created by Bilal Rashid on 10/11/2017.
 */

public class FrameActivity extends AppCompatActivity implements ToolbarListener {
    private Toolbar mToolbar;
    private SimpleDialog mSimpleDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
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
//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }

    public void addFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void toolbarSetup() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(" ");
        ActivityUtils.centerToolbarTitle(mToolbar,true);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
//            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if (isTaskRoot()) {
            ActivityUtils.startHomeActivity(this, HomeActivity.class, HomeFragment.class.getName());

        }
        else{
                FrameActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        if (fragment instanceof ActiveJobFragment)
        {
            FrameActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                onBackPressed();
                return true;
            default:
                // ...
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
