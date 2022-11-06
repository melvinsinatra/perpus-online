package com.example.project_lab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapsFragment extends Fragment {

    private double latitude = 0;
    private double longitude = 0;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng defaultLocation = new LatLng(-6.2194855116779495, 106.99980230402171);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

            try{
                googleMap.clear();
                latitude = getArguments().getDouble("Latitude");
                longitude = getArguments().getDouble("Longitude");
                LatLng location = new LatLng(latitude, longitude);

                if(latitude != 0 && longitude != 0) {
                    MarkerOptions marker = new MarkerOptions()
                            .title("Location")
                            .position(location)
                            .flat(true);
                    googleMap.addMarker(marker);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 19.0f));
                    return;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull LatLng latLng) {
                    googleMap.clear();
                    String snippet = String.format(Locale.getDefault(),
                            "Lat: " + latLng.latitude + " Long: " + latLng.longitude);

                    googleMap.addMarker(new MarkerOptions().position(latLng).title(snippet));
                    Intent i = new Intent("Location Data");
                    i.putExtra("Latlng", latLng);
                    getActivity().sendBroadcast(i);
                }
            });

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

}