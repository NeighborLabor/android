package com.durgaslist.andrew.durgaslist.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.durgaslist.andrew.durgaslist.ParseProject;
import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.UI.PagerAdapter.SectionPagerAdapter;
import com.durgaslist.andrew.durgaslist.UI.active.ActiveJobsFragment;
import com.durgaslist.andrew.durgaslist.UI.auth.LoginActivity;
import com.durgaslist.andrew.durgaslist.UI.auth.EditProfileActivity;
import com.durgaslist.andrew.durgaslist.UI.chat.ChatFragment;
import com.durgaslist.andrew.durgaslist.UI.listings.ListingsFragment;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private static final int LOCATION_REFRESH_TIME = 0;
    private static final int LOCATION_REFRESH_DISTANCE = 0; //what units are these

    private String[] choices;
    private DrawerLayout DrawerLayout;
    private ListView DrawerList;
    public static Location location;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "Main Activity Created");

        if(checkLocationPermission()){
            setupLocation();
        }

        if (ParseUser.getCurrentUser() == null) {
            toLogInScreen();
        } else {
            setMenuHeaderText();
            setUpMenuAndToolbar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission. ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                setupLocation();
            }
        }

    }

    void setMenuHeaderText() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView tvName = (TextView) header.findViewById(R.id.tvName);
        tvName.setText(ParseUser.getCurrentUser().getString("name"));
        TextView tvEmail = (TextView) header.findViewById(R.id.tvEmail);
        tvEmail.setText(ParseUser.getCurrentUser().getUsername());
    }

    void setUpMenuAndToolbar() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    default:
                    case 0:
                        ListingsFragment.refreshListings();
                    case 1:
                        ActiveJobsFragment.refresh();
                    case 2:
                        ChatFragment.refresh();
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    //buttons in toolbar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addJob:
                new CreateJobDialog().show(getSupportFragmentManager(), null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //menu items in slide menu
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.profile) {
            openProfile();
        } else if (id == R.id.listings) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.active_jobs) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.messages) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.logout) {
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void setPage(int pageNumber) {
        viewPager.setCurrentItem(pageNumber);
    }

    //actions
    void openProfile() {
        Intent intent = new Intent(ParseProject.getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void logOut() {
        ParseUser.logOut();
        toLogInScreen();
    }

    private void toLogInScreen() {
        Intent intent = new Intent(ParseProject.getContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //LOCATION
    void setupLocation() {
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lastKnownLocation != null){
            location = lastKnownLocation;
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);
        }else{
            lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(lastKnownLocation != null){
                location = lastKnownLocation;
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);
            }
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location newLocation) {
            location = newLocation;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Toast toast = Toast.makeText(ParseProject.getContext(), "I dont know what this means", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast toast = Toast.makeText(ParseProject.getContext(), "Location Service Enabled", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast toast = Toast.makeText(ParseProject.getContext(), "Location Service Disabled", Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    if (ContextCompat.checkSelfPermission(this,  Manifest.permission. ACCESS_FINE_LOCATION)  == PackageManager.PERMISSION_GRANTED) {
                        setupLocation();
                    }
                } else {
                    //quit app
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
                return;
            }

        }
    }

    public boolean checkLocationPermission() {
        Log.i(TAG, "check permission");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission. ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "request permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission. ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            return true;
        }
    }

}
