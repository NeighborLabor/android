package com.example.andrew.neighborlabour;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseUser;

/**
 * Created by andrew on 3/3/17.
 */

public class DrawerWindow extends AppCompatActivity{
    private String[] choices;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;

    private MainActivity mainActivity;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.drawer_window);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null) {
            toLogInScreen();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mTitle = mDrawerTitle = getTitle();

        choices = getResources().getStringArray(R.array.item_selection);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, choices));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                null,
                R.string.drawer_open,
                R.string.drawer_close
        ){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                 // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                 // creates call to onPrepareOptionsMenu()
            }
        };

        if(savedInstance == null){
            selectItem(0);
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch(item.getItemId()){
            case R.id.add_job:
                createCreateJobActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);


        return super.onPrepareOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    private void selectItem(int position){
        switch (position) {



            case 2:

                 mainActivity.changeTab(1);
                 break;

            case 3:


                mainActivity.changeTab(2);
                break;

            case 4:
                logOut();
                break;
            case 1:
            default:
                Fragment fragment = new MainActivity();

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();
                mainActivity = (MainActivity) fragment;



        }

    }

    public void setTitle(CharSequence title){
        mTitle = title;
        getActionBar().setTitle(mTitle);

    }
}
