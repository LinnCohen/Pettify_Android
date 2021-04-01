package com.pettify.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.pettify.ui.auth.AuthViewModel;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.pettify.R;
import com.pettify.model.PettifyApplication;
import com.pettify.model.report.Report;

import com.pettify.utilities.LocationUtils;

import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private int LOCATION_PERMISSION_CODE = 1;
    private HomeFragmentViewModel homeViewModel;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private List<Report> data = new LinkedList<>();
    private Spinner spinner;
    private LiveData<List<Report>> liveData;
    private String lastClicked = "";
    private View view;
    private Button buttonRequest;
    private Boolean hasLocationPermission;
    private AuthViewModel authViewModel;


    public HomeFragment() { // must default ctor for map

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        liveData = homeViewModel.getReports();
        data = liveData.getValue();
        buttonRequest = view.findViewById(R.id.location_permission_button);

        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);

        // --------------------------------- Location premission section ----------------------//
        if (ContextCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.hasLocationPermission = true;
            buttonRequest.setVisibility(View.INVISIBLE);
        } else {
            this.hasLocationPermission = false;

        }
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(PettifyApplication.context, "You already granted this permission", Toast.LENGTH_SHORT).show();

                } else {
                    requestLocationPermission();
                }

            }
        });

        //----------------------------Spinner actions to choose the map types----------------------------------//
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
        reloadData();
        setupMap();
        return view;
    }


    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this.getActivity()).setTitle("Permission needed").setMessage("This permission is needed for location services").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                this.buttonRequest.setVisibility(View.INVISIBLE);
                this.hasLocationPermission = true;
                setMarkers();
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                Menu menu = navigationView.getMenu();
                if (authViewModel.getCurrentUser() != null) {
                    menu.findItem(R.id.create_report).setVisible(true);
                } else {
                    menu.findItem(R.id.create_report).setVisible(false);
                }
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void reloadData() {
        homeViewModel.refreshAllReports(() -> {
        });
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
            setMarkers();

        }
    };

    private void setMarkers() {
        if (map == null || data == null) {
            return;
        }
        if (hasLocationPermission) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationUtils.instance.getCurrentLocation(), 8));
            //    Marker myLocation = map.addMarker(new MarkerOptions().position(LocationUtils.instance.getCurrentLocation()));
//            myLocation.setTag("my_location");
//            myLocation.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
//            myLocation.setTitle("IM HERE");
        }
        for (Report report : liveData.getValue()) {
            Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(report.getLat()), Double.parseDouble(report.getLng()))));
            marker.setTitle(report.getDescription());
            marker.setTag(report.getId());
        }


        map.setOnMarkerClickListener(clickedMarker -> {

            String tag = clickedMarker.getTag().toString();
            Log.d("TAG", "Clicked! title: " + tag);

            if (lastClicked.equals(tag) && clickedMarker.getTag() != "my_location") {
                lastClicked = "";
                Log.d("TAG", "Window true");

                Report clonedReport = new Report();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getId().equals(tag)) {
                        clonedReport = data.get(i);
                        Log.d("TAG", clonedReport.toString());
                    }
                }

//              NEED TO REDRIRECT TO WANTED REPORT
                HomeFragmentDirections.ActionNavHomeToViewReport direc = HomeFragmentDirections.actionNavHomeToViewReport().setReportId(clonedReport.getId());
                Navigation.findNavController(view).navigate(direc);


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