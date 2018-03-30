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

public class JobFragment extends Fragment implements View.OnClickListener, ToolbarListener {
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private ViewPager mViewPager;
    TextView mNumberOfCartItemsText;
    private SectionsPagerAdapter sectionsPagerAdapter;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Jobs");
        }
        mContext=context;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
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

    @Override
    public void setTitle(String title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ScheduledJobFragment tab1 = new ScheduledJobFragment();
                    return tab1;
                case 1:
                    CompleteJobFragment tab2 = new CompleteJobFragment();
                    return tab2;
                case 2:
                    FailedJobFragment tab3 = new FailedJobFragment();
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
                    String var = "SCHEDULED";

                    return var;
                case 1:
                    return "COMPLETE";
                case 2:
                    return "FAILED";
            }
            return null;
        }

    }

    @Override
    public void onResume() {
        if (mContext instanceof ToolbarListener) {
            ((ToolbarListener) mContext).setTitle("Jobs");
        }

        super.onResume();
    }
}
