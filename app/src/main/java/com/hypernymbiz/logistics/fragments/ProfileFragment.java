package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hypernymbiz.logistics.LoginActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.model.Profile;
import com.hypernymbiz.logistics.model.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.hypernymbiz.logistics.utils.LoginUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Metis on 19-Mar-18.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener, ToolbarListener {

    private ViewHolder mHolder;
    TextView email, drivername, drivercnic, martialstatus, dof;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private SwipeRefreshLayout swipelayout;
    SharedPreferences pref;
    String getUserAssociatedEntity, Email, Driver_name, Driver_id, Driver_photo;
    CircleImageView img_profile;
    Context mContext;
//    LoadingDialog dialog;

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
        mContext=context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        email = (TextView) view.findViewById(R.id.txt_Email);
        drivername = (TextView) view.findViewById(R.id.txt_drivername);
        drivercnic = (TextView) view.findViewById(R.id.txt_driverid);
        martialstatus = (TextView) view.findViewById(R.id.txt_gender);
        dof = (TextView) view.findViewById(R.id.txt_dateofjoin);
        img_profile = (CircleImageView) view.findViewById(R.id.img_driver_profile);
        swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe);
        pref = getActivity().getSharedPreferences("TAG", MODE_PRIVATE);
        Email = pref.getString("Email", "");
        email.setText(Email);

//        dialog = new LoadingDialog(getActivity(), getString(R.string.msg_loading));
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

//        swipelayout();
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getContext());
        if (getUserAssociatedEntity != null) {
            ApiInterface.retrofit.getprofile(Integer.parseInt(getUserAssociatedEntity)).enqueue(new Callback<WebAPIResponse<Profile>>() {
                @Override
                public void onResponse(Call<WebAPIResponse<Profile>> call, Response<WebAPIResponse<Profile>> response) {
                  //  dialog.dismiss();

                    if (response.isSuccessful()) {
                        try {
                            if (response.body().status) {

                                String driverName, driverCnic, drivergender, driverdof, url;
                                url = response.body().response.getPhoto();
                                driverName = response.body().response.getName();
                                driverCnic = String.valueOf(response.body().response.getCnic());
                                drivergender = response.body().response.getMaritalStatus();
                                driverdof = response.body().response.getDateOfJoining();
                                drivername.setText(driverName);
                                drivercnic.setText(driverCnic);
                                martialstatus.setText(drivergender);
                                dof.setText(driverdof);
                                Glide.with(getContext()).load(url).into(img_profile);
                            }
                        }
                        catch (Exception ex)
                        {

                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                        }
                    }else {
                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                    }

                }

                @Override
                public void onFailure(Call<WebAPIResponse<Profile>> call, Throwable t) {
                //    dialog.dismiss();
                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));
//                Snackbar snackbar = Snackbar.make(swipelayout, "Establish Network Connection!", Snackbar.LENGTH_SHORT);
//                View sbView = snackbar.getView();
//                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//                sbView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDialogToolbarText));
//                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                snackbar.show();
                }
            });
        } else {

            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        //   EventBus.getDefault().post(new DrawerItemSelectedEvent(getString(R.string.drawer_profile)));
        mHolder.btn_sgnout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signout:
                LoginUtils.clearUser(getContext());
                startActivity(new Intent(this.getContext(), LoginActivity.class));
                getActivity().finish();
                break;
        }

    }

    @Override
    public void setTitle(String title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }

    @Override
    public void onResume() {
        if (mContext instanceof ToolbarListener) {
            ((ToolbarListener) mContext).setTitle("Profile");
        }
        super.onResume();
        //  EventBus.getDefault().post(new DrawerItemSelectedEvent(getString(R.string.drawer_profile)));
    }

    public static class ViewHolder {

        Button btn_sgnout;

        public ViewHolder(View view) {
            btn_sgnout = (Button) view.findViewById(R.id.btn_signout);

        }
    }
}
