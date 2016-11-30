package com.pharm.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pharm.R;
import com.pharm.helper.Constants;
import com.pharm.helper.GPSTracker;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Avinash
 */
public class LocationListActivity extends AppCompatActivity {

    private AutoCompleteTextView actv;

    private List<String> cities;

    private LinearLayout currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        cities = Arrays.asList(getResources().getStringArray(R.array.india_cities));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,cities);
        actv.setAdapter(adapter);

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtvw=(TextView) view;
                String str=txtvw.getText().toString();
                int index = cities.indexOf(str);
                findIfWeCoverThisCity(index);
            }
        });

        ListView lst = (ListView) findViewById(R.id.lst);
        lst.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities));

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                findIfWeCoverThisCity(position);
            }
        });

        currentLocation = (LinearLayout) findViewById(R.id.current_location);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker gps = new GPSTracker(LocationListActivity.this);
                double lat = 0;
                double lon = 0;
                if (gps.canGetLocation()) {
                    lat = gps.getLatitude();
                    lon = gps.getLongitude();
                    Geocoder geocoder = new Geocoder(LocationListActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(lat, lon, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        int index = cities.indexOf(addresses.get(0).getAddressLine(2).split(",")[0]);
                    }

                } else {
                    gps.showSettingsAlert();
                }
            }
        });

    }

    private void findIfWeCoverThisCity(int index) {
        if(index == 0) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Constants.LOCATION, cities.get(index));
            setResult(AppCompatActivity.RESULT_OK, resultIntent);
            finish();
        } else {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
