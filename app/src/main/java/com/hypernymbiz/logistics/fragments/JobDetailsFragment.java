package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;

/**
 * Created by Metis on 21-Mar-18.
 */

public class JobDetailsFragment extends Fragment implements View.OnClickListener, ToolbarListener {
    private Toolbar mToolbar;
    private TextView mToolbarTitle;


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setTitle(String title) {

    }

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
        View view = inflater.inflate(R.layout.fragment_job_detials, container, false);

    return view;
    }





}
