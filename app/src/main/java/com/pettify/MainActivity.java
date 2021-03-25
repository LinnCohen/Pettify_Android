package com.pettify;

import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.reportslist_list, R.id.userslist_list)
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
                setLoggedIn(navUsername, authButton, data.getDisplayName());
            }
        });
    ;
    }

    private void setLoggedIn(TextView navUsername, Button authButton, String currentUserName) {
        navUsername.setText("Hello " + currentUserName);
        authButton.setText("Logout");
        authButton.setOnClickListener(buttonView -> {
            authViewModel.logout();
            navUsername.setText("");
            authButton.setText("Login / Register");
        });
    }

    private void setNotLoggedIn(DrawerLayout drawer, NavController navController, TextView navUsername, Button authButton) {
        navUsername.setText("");
        authButton.setText("Login / Register");
        authButton.setOnClickListener(butt -> {
            navController.navigate(R.id.nav_login);
            drawer.closeDrawer(GravityCompat.START);
        });
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
}