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

public class FailedJobFragment extends Fragment {
    View view;
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof ToolbarListener) {
//            ((ToolbarListener) context).setTitle("Jobs");
//        }
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_pager_job_failed, container, false);

        return view;

    }
}
