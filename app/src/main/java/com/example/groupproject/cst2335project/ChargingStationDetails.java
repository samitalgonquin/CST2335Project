package com.example.groupproject.cst2335project;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

//import block for toolbar
import androidx.appcompat.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.ActionMenuView;
//import block for toolbar


public class ChargingStationDetails extends AppCompatActivity {

    private static final String TAG = "ChargingStationDetails";
    String receivedData = "";
    TextView txtStationDetails;
    Button btnMap;
    private ActionMenuView amvMenu; //toolbar component

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.charging_station_details);
        //getSupportActionBar().hide();

        /**
         * Block for toolbar menu
         **/

        Toolbar ourToolbar = findViewById(R.id.ourToolbar);
        amvMenu = ourToolbar.findViewById(R.id.amvMenu);

        amvMenu.setOnMenuItemClickListener( menuItem -> onOptionsItemSelected(menuItem) );

        setSupportActionBar(ourToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**
         * Block for toolbar menu
         **/

        Intent intentReceived = getIntent();
        String title = intentReceived.getExtras().getString("title");
        receivedData = "Location Title" + "\n" + title + "\n";
        double latitude = intentReceived.getExtras().getDouble("latitude");
        receivedData = receivedData + "Latitude" + "\n" + latitude + "\n";
        double longitude = intentReceived.getExtras().getDouble("longitude");
        receivedData = receivedData + "Longitude" + "\n" + longitude + "\n";
        String phone = intentReceived.getExtras().getString("phone");
        receivedData = receivedData + "Contact Telephone number" + "\n" + phone + "\n";

        txtStationDetails = findViewById(R.id.txtStationDetails);
        txtStationDetails.setText(receivedData);

        btnMap = findViewById(R.id.btnMap);
        
        btnMap.setOnClickListener(clickt ->{
            // code block to load the latitude and longitude in the google maps activity
            Uri myUri = Uri.parse("geo:" + latitude + "," + longitude +"");
            Intent googleMapIntent = new Intent(Intent.ACTION_VIEW, myUri);
            googleMapIntent.setPackage("com.google.android.apps.maps");
            if (googleMapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(googleMapIntent);
            }
        });

    }

    /**
     * Block for toolbar menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // use amvMenu here
        inflater.inflate(R.menu.menu_items, amvMenu.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.car_charging:

                Intent goToChargingStationFinderIntent = new Intent(ChargingStationDetails.this,ChargingStationFinder.class);
                startActivity(goToChargingStationFinderIntent);
                return true;

            case R.id.recipe_search:

                // code for the selected menu item's function
                Toast.makeText(getApplicationContext(),"Recipe search is selected",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.currency_conversion:

                // code for the selected menu item's function
                Toast.makeText(getApplicationContext(),"Currency conversion is selected",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.news_headlines:

                // code for the selected menu item's function
                Toast.makeText(getApplicationContext(),"News headlines API is selected",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    /**
     * Block for toolbar menu
     **/



}

