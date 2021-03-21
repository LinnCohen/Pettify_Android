package com.pettify.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pettify.R;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;

import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeFragmentViewModel homeViewModel;
    GoogleMap map;
    LiveData<List<Report>> liveData;
    List<Report> data = new LinkedList<>();
    String lastClicked = "";

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
          //  setMarkers();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.0461, 34.8516), 8));
        }
    };

    private void setMarkers() {
    map.clear();

    }

    @Override


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        liveData = homeViewModel.getData();
//        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
//            @Override
//            public void onChanged(List<Recipe> recipes) {
//                List<Recipe> reversedData = reverseData(recipes);
//                data = reversedData;
//                setMarkers();
//            }
//
//        });
        return view;
    }
}