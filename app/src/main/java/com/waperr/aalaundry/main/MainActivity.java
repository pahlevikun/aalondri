package com.waperr.aalaundry.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.waperr.aalaundry.R;
import com.waperr.aalaundry.adapter.ViewPagerAdapter;
import com.waperr.aalaundry.database.DatabaseHandler;
import com.waperr.aalaundry.main.handle_fragment.HelpFragment;
import com.waperr.aalaundry.main.handle_fragment.HistoryFragment;
import com.waperr.aalaundry.main.handle_fragment.MessageFragment;
import com.waperr.aalaundry.main.handle_fragment.ProfileFragment;
import com.waperr.aalaundry.pojo.Profil;
import com.waperr.aalaundry.main.handle_fragment.MainFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    public AppBarLayout appBarLayout;

    private int[] tabIcons = {
            R.drawable.ic_tab_home,
            R.drawable.ic_tab_history,
            R.drawable.ic_tab_message,
            R.drawable.ic_tab_profile,
            R.drawable.ic_tab_help
    };

    private DatabaseHandler dataSource;
    private ArrayList<Profil> valuesProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataSource = new DatabaseHandler(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    if (tab.getPosition() == 0) {
                        appBarLayout.setElevation(7);
                    } else if (tab.getPosition() == 1) {
                        appBarLayout.setElevation(0);
                    } else if (tab.getPosition() == 2) {
                        appBarLayout.setElevation(7);
                    } else if (tab.getPosition() == 3) {
                        appBarLayout.setElevation(7);
                    } else if (tab.getPosition() == 4) {
                        appBarLayout.setElevation(7);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        requestPermissionSMS();
    }

    private void setupTabIcons() {
        TextView tabHome = (TextView) LayoutInflater.from(this).inflate(R.layout.adapter_tab, null);
        tabHome.setText("Beranda");
        tabHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_home, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabHome);

        TextView tabHistory = (TextView) LayoutInflater.from(this).inflate(R.layout.adapter_tab, null);
        tabHistory.setText("Riwayat");
        tabHistory.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_history, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabHistory);

        TextView tabMessage = (TextView) LayoutInflater.from(this).inflate(R.layout.adapter_tab, null);
        tabMessage.setText("Pesan");
        tabMessage.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_message, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabMessage);

        TextView tabProfile = (TextView) LayoutInflater.from(this).inflate(R.layout.adapter_tab, null);
        tabProfile.setText("Profil");
        tabProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_profile, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabProfile);

        TextView tabHelp = (TextView) LayoutInflater.from(this).inflate(R.layout.adapter_tab, null);
        tabHelp.setText("Bantuan");
        tabHelp.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_help, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabHelp);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "Beranda");
        adapter.addFragment(new HistoryFragment(), "Riwayat");
        adapter.addFragment(new MessageFragment(), "Pesan");
        adapter.addFragment(new ProfileFragment(), "Profil");
        adapter.addFragment(new HelpFragment(), "Bantuan");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Tekan lagi untuk keluar!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

    }

    public void requestPermissionSMS(){
        final int PERMISSION_REQUEST_CODE = 1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {

                String[] permissions = {Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }
    }


    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Set the fragment initially
            MainFragment fragment = new MainFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(MainActivity.this, LondriOrderHistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_faq) {
            //Set the fragment initially
            LondriFAQFragment fragment = new LondriFAQFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_news) {
            Intent intent = new Intent(MainActivity.this, BeritaActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            LondriSettingFragment fragment = new LondriSettingFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_signout) {
            dataSource.hapusDbaseProfil();
            dataSource.close();

            SharedPreferences sharedPreferences =  getSharedPreferences("DATA",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("session",false);
            editor.commit();

            dataSource.hapusDbaseRiwayat();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            MainActivity.this.startActivity(intent);
            MainActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
