package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
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

public class MaintenanceFragment extends Fragment implements View.OnClickListener, ToolbarListener {
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    View view;
    private ViewPager mViewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    TextView mNumberOfCartItemsText;

    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setTitle(String title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        View view = menu.findItem(R.id.notification_bell).getActionView();
        mNumberOfCartItemsText = (TextView) view.findViewById(R.id.text_number_of_cart_items);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Maintenance");
        }
        mContext=context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maintenance, container, false);
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    DueMaintenanceFragment tab1 = new DueMaintenanceFragment();
                    return tab1;
                case 1:
                    CompleteMaintenanceFragment tab2 = new CompleteMaintenanceFragment();
                    return tab2;
                case 2:
                    OverdueMaintenanceFragment tab3 = new OverdueMaintenanceFragment();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    String var = "DUE";

                    return var;
                case 1:
                    return "COMPLETED";
                case 2:
                    return "OVERDUE";
            }
            return null;
        }

    }

    @Override
    public void onResume()
    {
        if (mContext instanceof ToolbarListener) {
            ((ToolbarListener) mContext).setTitle("Maintenance");
        }
        super.onResume();
    }
}
