package com.example.groupproject.cst2335project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import block for toolbar
import androidx.appcompat.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.ActionMenuView;
//import block for toolbar

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ImageButton btnCharging, btnRecipe, btnNews, btnCurrency, btnUpdate, btnAbout;
    final Context context = this;
    String info;
    private ActionMenuView amvMenu; //toolbar component

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature( Window.FEATURE_NO_TITLE);
        //getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // this layout includes the custom toolbar our_toolbar.xml
        setContentView(R.layout.activity_main);
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

        btnCharging = findViewById(R.id.btnCharging);
        btnCharging.setOnClickListener( v -> {
            Intent goToChargingStationFinderIntent = new Intent(MainActivity.this,ChargingStationFinder.class);
            startActivity(goToChargingStationFinderIntent);
        } );

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener( v -> Toast.makeText(MainActivity.this,"You already have the latest version of the app.",Toast.LENGTH_LONG).show() );

        btnAbout = findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener( v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } );
        
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

                Intent goToChargingStationFinderIntent = new Intent(MainActivity.this,ChargingStationFinder.class);
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
