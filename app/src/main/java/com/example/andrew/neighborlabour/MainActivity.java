package com.example.andrew.neighborlabour;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.andrew.neighborlabour.UI.PagerAdapter.SectionPagerAdapter;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String[] choices;
    private DrawerLayout DrawerLayout;
    private ListView DrawerList;

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
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
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
