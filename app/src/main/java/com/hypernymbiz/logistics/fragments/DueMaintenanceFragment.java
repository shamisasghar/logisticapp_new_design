package com.hypernymbiz.logistics.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypernymbiz.logistics.R;

/**
 * Created by Metis on 19-Mar-18.
 */

public class DueMaintenanceFragment extends Fragment {
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_pager_maintenance_due, container, false);

        return view;

    }

}
