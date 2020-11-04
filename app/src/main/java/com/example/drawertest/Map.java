package com.example.drawertest;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Map extends Fragment {
    View vMap;
    GoogleMap mMap;
    static LatLng coordinates;
    static String location;
    MapView mapView;


    /*@Override
    public void onCreate(Bundle savedInstance) { super.onCreate(savedInstance); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       vMap = inflater.inflate(R.layout.activity_maps, container, false);
        Intent i = new Intent(getContext(),Maps.class);
        startActivity(i);
        return vMap;
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMap = inflater.inflate(R.layout.fragment_map, container, false);

        //initialize map
        mapView = (MapView) vMap.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        SharedPreferences preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        location = preferences.getString("address", "Monash") + " " + preferences.getInt("postCode", 3145);
        if (location.isEmpty())
            location = "Monash";
        Geocoder coder = new Geocoder(getContext());
        try {
            Address address = new Address(Locale.getDefault());
            if (coder.getFromLocationName(location, 1).size() > 0)
                address = coder.getFromLocationName(location, 1).get(0);
            coordinates = new LatLng(address.getLatitude(), address.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Check task = new Check();
        task.execute();
        return vMap;
    }

    private class Check extends AsyncTask<Void, Void, List<JSONObject>> {
        @Override
        protected List<JSONObject> doInBackground(Void... params) {
            return RestMethods.getParks(coordinates.latitude, coordinates.longitude);
        }

        @Override
        protected void onPostExecute(List<JSONObject> data) {
            final List<JSONObject> parks = data;
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    Log.i("DEBUG", "onMapReady");
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinates, 12);
                    googleMap.animateCamera(cameraUpdate);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(coordinates).title("Marker in " + location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    try {
                        for (JSONObject o : parks) {
                            LatLng pos = new LatLng(o.getDouble("latitude"), o.getDouble("longitude"));
                            mMap.addMarker(new MarkerOptions().position(pos).title(o.getString("name")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}


