package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class TestToolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        //get the layout of toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //adding a  NavigationDrawer that goes to chat page, weather forecast, and go back to log page
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_xml, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.menu_goChatID:
                message = "You clicked item 1";
                break;
            case R.id.menu_weatherID:
                message = "You clicked on item 2";
                break;
            case R.id.menu_goLogID:
                message = "You clicked on item 3";
                break;
            case R.id.itemFour:
                message = "You clicked on item 4";
                Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_SHORT).show();
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }

    //this function is for the navigation menu interface onclick
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent goToNextPage = getIntent();

        //we have to go to other page by click on the
        switch(item.getItemId())
        {

            //what to do when the menu item is selected:
            case R.id.menu_goChatID:
                goToNextPage = new Intent(TestToolbar.this,ChatRoomActivity.class);
                break;
            case R.id.menu_weatherID:
                goToNextPage = new Intent(TestToolbar.this,WeatherForecast.class);
                break;
            case R.id.menu_goLogID:
                goToNextPage = new Intent(TestToolbar.this,ProfileActivity.class);
                this.setResult(RESULT_OK,goToNextPage);
                this.finish();  //finish the toolbar activity, it goes back to login activity;
                break;
            default:
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        startActivity(goToNextPage);
        return false;
    }
}