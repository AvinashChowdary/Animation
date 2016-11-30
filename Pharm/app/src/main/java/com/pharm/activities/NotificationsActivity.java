package com.pharm.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.pharm.R;
import com.pharm.adapters.ViewPagerAdapter;
import com.pharm.fragments.PromotionsFragment;
import com.pharm.fragments.UpdatesFragment;

/**
 * Created by Avinash
 */
public class NotificationsActivity extends AppCompatActivity {

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabs();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UpdatesFragment(), getString(R.string.updates));
        adapter.addFragment(new PromotionsFragment(), getString(R.string.promotions));
        viewPager.setAdapter(adapter);
    }

    private void setupTabs() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        tabOne.setText(getString(R.string.updates));
        mTabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        tabTwo.setText(getString(R.string.promotions));
        mTabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
