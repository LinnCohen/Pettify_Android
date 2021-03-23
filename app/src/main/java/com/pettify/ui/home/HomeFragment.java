package com.pettify.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.protobuf.StringValue;
import com.pettify.R;
import com.pettify.Utilities.LocationUtils;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;

import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

public class HomeFragment extends Fragment {

    private HomeFragmentViewModel homeViewModel;
    GoogleMap map;
    List<Report> data;
    LiveData<List<Report>> liveData;
    String lastClicked = "";

    public HomeFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
            setMarkers();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.0461, 34.8516), 8));
        }
    };

    private void setMarkers() {
        Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(32.0711775, 34.8135377)));
        marker.setTitle("Injured Dog");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        homeViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        liveData = homeViewModel.getReports();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                LinkedList<Report> list = new LinkedList<>();
                for (Report report : reports)
                    list.add(report);
                data = list;
                Log.d("location", String.valueOf(data.size()));

            }

        });

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
        String coordinates = LocationUtils.instance.getCurrentLocationAsString();
        if (coordinates != null)
            Log.d("location", coordinates);
        else
            Log.d("location", "Location return has null");
    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.map, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Log.d("location", String.valueOf(item.getItemId()));
//
//        switch (item.getItemId()) {
//            case R.id.action_normal:
//                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                break;
//            case R.id.action_hybrid:
//                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                break;
//            case R.id.action_satellite:
//                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                break;
//            case R.id.action_terrain:
//                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//                break;
//        }
//
//        return true;
//    }
}