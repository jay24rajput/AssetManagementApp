package com.example.jay.assetmanagementapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

public class NavDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    String mUid="23045";
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent startpurchaseActivity=new Intent(NavDrawer.this, PurchaseAsset.class);
                startActivity(startpurchaseActivity);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout cardViewLinearLayout=(LinearLayout)findViewById(R.id.cardview_linear_layout);
        for(int i=0;i<=10;i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.cardview_layout, null);
            cardViewLinearLayout.addView(rowView, cardViewLinearLayout.getChildCount() - 1);
        }


            SharedPreferences prefs = getSharedPreferences("MyPrefs",MODE_PRIVATE);
            String name = prefs.getString("Name", "");
            String email=prefs.getString("Email","");
            String imgUrl=prefs.getString("Photo URL","");

            Log.e("Jay",name+" "+email+" "+imgUrl);

        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_nav_drawer);
        TextView nvName = (TextView)hView.findViewById(R.id.nav_drawer_name);
        nvName.setText(name);

        TextView nvEmail=(TextView)hView.findViewById(R.id.nav_drawer_email);
        nvEmail.setText(email);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageView nvImg=(ImageView)hView.findViewById(R.id.nav_drawer_imageView);
        try {
            URL url = new URL(imgUrl);

            Log.e("hello",url.toString());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            nvImg.setImageBitmap(bmp);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        checkRole();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.purchase_asset) {
            Intent purchaseAsset=new Intent(this,PurchaseAsset.class);
            startActivity(purchaseAsset);
            // Handle the camera action
        }
        else if (id == R.id.request_asset_maintainance) {
            Intent maintainAsset = new Intent(this, AssetMaintenance.class);
            startActivity(maintainAsset);

        } else if (id == R.id.request_asset) {
            Intent requestAsset = new Intent(this, RequestAsset.class);
            startActivity(requestAsset);
        }

        else if(id==R.id.generate_reports){
            Intent generateReports=new Intent(this,GenerateReports.class);
            startActivity(generateReports);
        }

        else if(id==R.id.distribute_asset){
            Intent distributeAsset=new Intent(this,activity_distribute.class);
            startActivity(distributeAsset);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void cardViewClicked(View view){
        Intent intent=new Intent(this,AssetOwned.class);
        startActivity(intent);
    }

    public void checkRole(){
        int roleId=Character.getNumericValue(mUid.charAt(1));
        switch (roleId){
            case 1: role="Staff";
            setStaffMenu();
                    break;
            case 2:role="Lab Assistant";
            setStaffMenu();
                    break;
            case 3:role="Department Purchase Officer";
            setDPO();
                    break;
            case 4:role="HOD";
            setHOD();
            break;
            case 5:role="Purchasing Officer";
            setPO();
            break;
            case 6:role="Admin";
            break;

        }
    }

    public void setStaffMenu(){
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.generate_reports).setVisible(false);
        nav_Menu.findItem(R.id.track_asset).setVisible(false);
        nav_Menu.findItem(R.id.distribute_asset).setVisible(false);
        nav_Menu.findItem(R.id.nav_role).setTitle(role);
    }

    public void setDPO(){
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.request_asset_maintainance).setVisible(false);
        nav_Menu.findItem(R.id.request_asset).setVisible(false);
        nav_Menu.findItem(R.id.nav_role).setTitle(role);
    }

    public void setHOD(){
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.request_asset_maintainance).setVisible(false);
        nav_Menu.findItem(R.id.request_asset).setVisible(false);
        nav_Menu.findItem(R.id.track_asset).setVisible(false);
        nav_Menu.findItem(R.id.distribute_asset).setVisible(false);
        nav_Menu.findItem(R.id.nav_role).setTitle(role);
    }

    public void setPO(){
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.request_asset_maintainance).setVisible(false);
        nav_Menu.findItem(R.id.request_asset).setVisible(false);
        nav_Menu.findItem(R.id.track_asset).setVisible(false);
        nav_Menu.findItem(R.id.generate_reports).setVisible(false);
        nav_Menu.findItem(R.id.nav_role).setTitle(role);
    }
}
