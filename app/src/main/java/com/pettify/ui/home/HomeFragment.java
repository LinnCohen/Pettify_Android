package com.pettify.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pettify.R;
import com.pettify.model.PettifyApplication;
import com.pettify.model.report.Report;

import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private HomeFragmentViewModel homeViewModel;
    SupportMapFragment mapFragment;
    private GoogleMap map;
    List<Report> data = new LinkedList<>();
    Spinner spinner;
    LiveData<List<Report>> liveData;
    String lastClicked = "";



    public HomeFragment(){
        Log.d("TAG","CTOR");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        liveData = homeViewModel.getReports();
        data = liveData.getValue();



        //----------------------------Spinner actions to choose map types----------------------------------//
        spinner = view.findViewById(R.id.spinner3);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(PettifyApplication.context,
                R.array.map_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //----------------------------Reports actions to render map markers--------------------------------//
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                data = reports;
                setMarkers();
                Log.d("location", String.valueOf(data.size()));
            }
        });
        setupMap();
        return view;
    }

    private void setupMap() {
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setMapType(googleMap.MAP_TYPE_NORMAL);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.0461, 34.8516), 8));
            setMarkers();

        }
    };

    private void setMarkers() {
        if (map == null || data == null) {
        return;
        }
        map.clear();
        Log.d("location", data.toString());

        for (Report report : liveData.getValue()) {
            Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(report.getLat()), Double.parseDouble(report.getLng()))));
            marker.setTitle(report.getDescription());
            marker.setTag(report.getId());
        }


        map.setOnMarkerClickListener(clickedMarker -> {
            String tag = clickedMarker.getTag().toString();
            Log.d("TAG", "Clicked! title: " + tag);

            if (lastClicked.equals(tag)) {
                lastClicked = "";
                Log.d("TAG", "Window true");

                Report clonedReport = null;
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getId().equals(tag)) {
                        clonedReport = data.get(i);
                    }
                }

//              NEED TO REDRIRECT TO WANTED REPORT

            } else {
                lastClicked = tag;
                Log.d("TAG", "Window false");
            }
            return false;
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String string = parent.getItemAtPosition(position).toString();
        switch (string) {
            case "Normal Map":
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case "Hybrid Map":
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case "Satellite Map":
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case "Terrain Map":
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            default:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Need to implment
    }
}