package com.pettify;

import android.Manifest;
import android.content.ClipData;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pettify.model.PettifyApplication;
import com.pettify.ui.auth.AuthViewModel;

public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    private AuthViewModel authViewModel;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.reportslist_list, R.id.nav_login, R.id.nav_register, R.id.create_report, R.id.nav_new_chat, R.id.account_tab)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //startActivity(new Intent(this,MapsActivity.class));

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.textView_navHeader);
        Button authButton = headerView.findViewById(R.id.navheader_button);

        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.onUserChange(data -> {
            if (data == null) {
                setNotLoggedIn(drawer, navController, navUsername, authButton);
            } else {
                setLoggedIn(navUsername, authButton, data.getDisplayName(), navController);
            }
        });
        setCreateReport();
    }

    private void setLoggedIn(TextView navUsername, Button authButton, String currentUserName, NavController navController) {
        setCreateReport();
        if (currentUserName == null) {
            navUsername.setText("Welcome to Pettify!");
        } else {
            navUsername.setText("Hello " + currentUserName);
        }
        setTabsVisibility(true);
        authButton.setText("Logout");
        authButton.setOnClickListener(buttonView -> {
            authViewModel.logout();
            navUsername.setText("");
            authButton.setText("Login / Register");
            navController.navigate(R.id.nav_home);
        });
    }

    private void setNotLoggedIn(DrawerLayout drawer, NavController navController, TextView navUsername, Button authButton) {
        setCreateReport();
        navUsername.setText("");
        authButton.setText("Login / Register");

        authButton.setOnClickListener(butt -> {
            navController.navigate(R.id.nav_login);
            drawer.closeDrawer(GravityCompat.START);
        });

        setTabsVisibility(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setTabsVisibility(boolean isVisible) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        nav_Menu.findItem(R.id.account_tab).setVisible(isVisible);
    }

    private void setCreateReport() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        if(checkLocationEnabled() && authViewModel.getCurrentUser() != null) {
            nav_Menu.findItem(R.id.create_report).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.create_report).setVisible(false);
        }
    }

    private boolean checkLocationEnabled() {
        return ContextCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}