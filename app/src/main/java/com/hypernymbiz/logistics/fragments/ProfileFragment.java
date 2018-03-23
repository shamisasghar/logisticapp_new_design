package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;

/**
 * Created by Metis on 19-Mar-18.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener, ToolbarListener {

    TextView mNumberOfCartItemsText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Profile");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        View view = menu.findItem(R.id.notification_bell).getActionView();
        mNumberOfCartItemsText = (TextView) view.findViewById(R.id.text_number_of_cart_items);
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setTitle(String title) {

    }
}
