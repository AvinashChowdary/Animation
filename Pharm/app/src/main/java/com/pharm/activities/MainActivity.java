package com.pharm.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pharm.R;
import com.pharm.adapters.ViewPagerAdapter;
import com.pharm.fragments.ExploreFragment;
import com.pharm.fragments.InviteFragment;
import com.pharm.fragments.RedeemFragment;
import com.pharm.helper.CircleTransform;
import com.pharm.helper.Constants;
import com.pharm.helper.GPSTracker;
import com.pharm.helper.Numerics;
import com.pharm.helper.Preferences;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView location;

    private View headerView;

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private ImageView search;

    private ImageView notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        location = (TextView) findViewById(R.id.toolbar_title);
        search = (ImageView) findViewById(R.id.search);
        notifications = (ImageView) findViewById(R.id.notifications);
        headerView = navigationView.getHeaderView(0);
        setLocation();
        setUserDetails();
        location.setOnClickListener(this);
        search.setOnClickListener(this);
        notifications.setOnClickListener(this);
        headerView.setOnClickListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabs();
    }

    private void setUserDetails() {
        ImageView img = (ImageView) headerView.findViewById(R.id.imageView);
        TextView txtName = (TextView) headerView.findViewById(R.id.txt_name);
        TextView txtProfile = (TextView) headerView.findViewById(R.id.txt_profile);

        String imageUrl = Preferences.getIns().getUserProfilePicture();
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(MainActivity.this).load(imageUrl).placeholder(R.drawable.person).transform(new CircleTransform()).into(img);
        }

        String name = Preferences.getIns().getUserName();
        if (!TextUtils.isEmpty(name)) {
            txtName.setText(name);
            txtProfile.setVisibility(View.VISIBLE);
        } else {
            txtName.setText("Login");
            txtProfile.setVisibility(View.INVISIBLE);
            Picasso.with(MainActivity.this).load(R.drawable.person).into(img);
        }
    }

    private void setLocation() {
        if (TextUtils.isEmpty(Preferences.getIns().getLocation())) {
            GPSTracker gps = new GPSTracker(MainActivity.this);
            double lat = 0;
            double lon = 0;
            if (gps.canGetLocation()) {
                lat = gps.getLatitude();
                lon = gps.getLongitude();
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    location.setText(addresses.get(0).getAddressLine(2).split(",")[0]);
                    Preferences.getIns().setLocation(addresses.get(0).getAddressLine(2).split(",")[0]);
                }

            } else {
                gps.showSettingsAlert();
            }
        } else {
            location.setText(Preferences.getIns().getLocation());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rupee) {

        } else if (id == R.id.nav_transactions) {

        } else if (id == R.id.nav_wallet) {

        } else if (id == R.id.nav_voucher) {

        } else if (id == R.id.nav_promo_code) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_rate_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_title:
                showLocationActivity();
                break;
            case R.id.header_view:
                gotoSettingsActivity();
                break;
            case R.id.search:
                startActivity(new Intent(MainActivity.this, LocationListActivity.class));
                break;
            case R.id.notifications:
                startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
                break;
        }
    }

    private void showLocationActivity() {
        startActivityForResult(new Intent(MainActivity.this, LocationListActivity.class), 101);
    }

    private void gotoSettingsActivity() {
        String imageUrl = Preferences.getIns().getUserProfilePicture();
        String name = Preferences.getIns().getUserName();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(imageUrl)) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
    }

    private void loginWithFb() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ExploreFragment(), getString(R.string.explore));
        adapter.addFragment(new RedeemFragment(), getString(R.string.redeem));
        adapter.addFragment(new InviteFragment(), getString(R.string.invite));
        viewPager.setAdapter(adapter);
    }

    private void setupTabs() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        tabOne.setText(getString(R.string.explore));
        mTabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        tabTwo.setText(getString(R.string.redeem));
        mTabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        tabThree.setText(getString(R.string.invite));
        mTabLayout.getTabAt(Numerics.TWO).setCustomView(tabThree);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == -1 && data != null) {
            location.setText(data.getStringExtra(Constants.LOCATION));
            Preferences.getIns().setLocation(data.getStringExtra(Constants.LOCATION));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocation();
        setUserDetails();
    }
}
