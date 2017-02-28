package com.example.andrew.neighborlabour;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
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

import com.example.andrew.neighborlabour.UI.PagerAdapter.SectionPagerAdapter;
import com.parse.Parse;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private String[] choices;
    private DrawerLayout DrawerLayout;
    private ListView DrawerList;
    ViewPager viewPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "Main Activity Created");

        if(ParseUser.getCurrentUser() == null){
            toLogInScreen();
        }else{
            setmenuHeaderText();
        }

        setUpMenuAndToolbar();
    }

    void setmenuHeaderText(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView tvName = (TextView) header.findViewById(R.id.tvName);
        tvName.setText(ParseUser.getCurrentUser().getString("name"));
        TextView tvEmail = (TextView) header.findViewById(R.id.tvEmail);
        tvEmail.setText(ParseUser.getCurrentUser().getUsername());
    }

    void setUpMenuAndToolbar(){
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.log_out:
                logOut();
                return true;
            case R.id.add_job:
                createCreateJobActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.profile) {

        } else if (id == R.id.listings) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.active_jobs) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.messages) {
            viewPager.setCurrentItem(2);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void createCreateJobActivity(){
        Intent intent = new Intent(ParseProject.getContext(), CreateJobActivity.class);
        startActivity(intent);
    }

    private void logOut(){
        ParseUser.logOut();
        toLogInScreen();
    }

    private void toLogInScreen(){
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }






}
