package com.waperr.aalaundry.main.handle_fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.waperr.aalaundry.R;
import com.waperr.aalaundry.main.handle_fragment.handle_history_fragment.CompletedHistoryFragment;
import com.waperr.aalaundry.main.handle_fragment.handle_history_fragment.OngoingHistoryFragment;
import com.waperr.aalaundry.main.handle_fragment.handle_history_fragment.RequestedHistoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

     public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        //((MainActivity) getActivity()).setTitle("Bantuan");

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            tabLayout.setElevation(7);
        }

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new RequestedHistoryFragment(), "Permintaan");
        adapter.addFragment(new OngoingHistoryFragment(), "Diproses");
        adapter.addFragment(new CompletedHistoryFragment(), "Selesai");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
