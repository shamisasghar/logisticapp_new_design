package com.hypernymbiz.logistics.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypernymbiz.logistics.R;

/**
 * Created by Metis on 20-Mar-18.
 */

public class CompleteMaintenanceFragment extends Fragment {

    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_pager_maintenance_complete, container, false);

        return view;

    }

}
