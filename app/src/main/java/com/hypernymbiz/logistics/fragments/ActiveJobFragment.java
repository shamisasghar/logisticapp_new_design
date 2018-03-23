package com.hypernymbiz.logistics.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;

/**
 * Created by Metis on 20-Mar-18.
 */

public class ActiveJobFragment extends Fragment implements View.OnClickListener, ToolbarListener {
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    Dialog complete,cancl;
    Button complte,cancel;
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
        View view = inflater.inflate(R.layout.fragment_active_job, container, false);
        complte = (Button) view.findViewById(R.id.btn_dialog_complete);
        cancel = (Button) view.findViewById(R.id.btn_dialog_cancel);




                complte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                complete = new Dialog(getContext());
                complete.setContentView(R.layout.dialog_complete_job);
                complete.show();
                complete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancl = new Dialog(getContext());
                cancl.setContentView(R.layout.dialog_cancel_job);
                cancl.show();
                cancl.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
        });


        return   view;

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
}
