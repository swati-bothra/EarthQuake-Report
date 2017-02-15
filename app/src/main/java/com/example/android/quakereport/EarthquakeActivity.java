/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

//    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL =
        "http://earthquake.usgs.gov/fdsnws/event/1/query";
    //?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private EarthquakeAdapter mAdapter;
    private static final int EARTHQUAKE_LOADER_ID= 1;
    private TextView mEmptyStateView;

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPref.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPref.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby",orderBy);

        return new EarthquakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateView.setText(R.string.no_earthquakes);
        mAdapter.clear();
        if (earthquakes!=null || !earthquakes.isEmpty()){
         mAdapter.addAll(earthquakes);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.clear();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mEmptyStateView = (TextView)findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateView);




//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, earthquakes);

        mAdapter = new EarthquakeAdapter(this,new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthquake = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(websiteIntent);
            }
        });


        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork !=null && activeNetwork.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        }
        else{
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateView.setText("NO Internet Connection");
        }

        // Create a fake list of earthquake locations.
//        ArrayList<Earthquake> earthquakes = new ArrayList<>();
//        earthquakes.add(new Earthquake("7.2","San Francisco","Feb 2, 2016"));
//        earthquakes.add(new Earthquake("6.1","San Francisco","Feb 2, 2016"));
//        earthquakes.add(new Earthquake("7.2","San Francisco","Feb 2, 2016"));
//        earthquakes.add(new Earthquake("7.2","San Francisco","Feb 2, 2016"));
//        earthquakes.add(new Earthquake("7.2","San Francisco","Feb 2, 2016"));
//        earthquakes.add(new Earthquake("7.2","San Francisco","Feb 2, 2016"));





//        EarthquakeAsynctask task = new EarthquakeAsynctask();
//        task.execute(USGS_REQUEST_URL);
    }



//    private class EarthquakeAsynctask extends AsyncTask<String,Void,List<Earthquake>>{
//
//        @Override
//        protected List<Earthquake> doInBackground(String... urls) {
//            if (urls.length<1 || urls[0]==null){
//                return null;
//            }
//
//            return earthquakes;
//        }
//
//        @Override
//        protected void onPostExecute(List<Earthquake> earthquakes) {
//            mAdapter.clear();
//            if (earthquakes!=null || !earthquakes.isEmpty()){
//                mAdapter.addAll(earthquakes);
//            }
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_settings){
            Intent settingIntent= new Intent(this,SettingActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
