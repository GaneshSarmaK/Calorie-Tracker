package com.example.drawertest;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    static LatLng coordinates;
    static String location;
    static List<JSONObject> parks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        parks = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        location = preferences.getString("address","Monash") + " " + preferences.getInt("postCode",3145 );
        if(location.isEmpty())
            location = "Monash";
        Geocoder coder = new Geocoder(getApplicationContext());
        try {
            Address address = new Address(Locale.getDefault());
            if(coder.getFromLocationName(location,1).size() > 0)
                address = coder.getFromLocationName(location,1).get(0);
            coordinates = new LatLng(address.getLatitude(),address.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Check task = new Check();
        task.execute();
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(Maps.this);
*/
    }


    private class Check extends AsyncTask<Void, Void, List<JSONObject>> {
        @Override
        protected List<JSONObject> doInBackground(Void... params) {
            List<JSONObject> data = RestMethods.getParks(coordinates.latitude,coordinates.longitude);
            return data;
        }

        @Override
        protected void onPostExecute(List<JSONObject> data) {
            parks = data;
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(Maps.this);
        }
    }


    public void markers(GoogleMap mMap) throws JSONException {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(coordinates).title("Marker in " + location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        for(JSONObject o : parks) {
            LatLng pos = new LatLng(o.getDouble("latitude"),o.getDouble("longitude"));
            mMap.addMarker(new MarkerOptions().position(pos).title(o.getString("name")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.clear();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        mMap.setMinZoomPreference(12);
        mMap.setMaxZoomPreference(12);
        try {
            markers(mMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}