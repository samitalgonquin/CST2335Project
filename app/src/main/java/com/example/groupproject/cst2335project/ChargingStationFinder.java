package com.example.groupproject.cst2335project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class ChargingStationFinder extends AppCompatActivity {

    private static final String TAG = "ChargingStationFinder";
    private ArrayList<JsonDataArray> jsonDataArrayList = new ArrayList<JsonDataArray>();
    private ListView jsonListView;

    EditText txtLat, txtLong;
    double latitude, longitude;
    String strLatitude, strLongitude;
    ProgressBar pBar;
    int jsonArrayListIndexSelectedForContextMenu;
    DatabaseHelper myDb;
    TextView lblTitle;

    /*
      Shared preferences block starts
     */
    ArrayList<String> listLatLong = new ArrayList<>();
    //SharedPrefs sPrefs = new SharedPrefs();
     /*
      Shared preferences block ends
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme( R.style.WithBorder );
        //requestWindowFeature( Window.FEATURE_NO_TITLE);
        //getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.charging_station_finder);

        lblTitle = findViewById(R.id.lblTitle);

        txtLat = findViewById(R.id.txtLat);
        txtLong = findViewById(R.id.txtLong);

        //loading latitude and longitude from saved shared preferences data
        getDataFromSharedPreferences();

        //getSupportActionBar().hide();
        getSupportActionBar().setTitle(null);


        pBar = findViewById(R.id.pBar);


        jsonListView = findViewById(R.id.jsonListView);
        //register jsonListView for context menu
        registerForContextMenu(jsonListView);

        //create instance of DatabaseHelper class
        myDb = new DatabaseHelper(this);



        final Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener( v -> {
            strLatitude = "";
            strLongitude = "";

            strLatitude = txtLat.getText().toString();
            strLongitude = txtLong.getText().toString();

            if(strLatitude != null && !strLatitude.isEmpty() && strLongitude != null && !strLongitude.isEmpty()) {

                // Make and display Snackbar
                Snackbar snackbar = Snackbar.make( btnSearch, getString( R.string.snackBarText1 ), Snackbar.LENGTH_SHORT );
                //code block starts to show Snackbar on top instead of bottom
                View view = snackbar.getView();
                FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                params.gravity = Gravity.TOP;
                view.setLayoutParams(params);
                //code block ends to show Snackbar on top instead of bottom
                // ADD Action Click Retry Listener
                //snackbar.setAction("Undo", new UndoListener());
                // show the Snackbar
                snackbar.show();

                new ChargingStationQuery().execute();
                String lblTitle_all = getString( R.string.lblTitle_all );
                lblTitle.setText(lblTitle_all);
                /** 0 is for VISIBLE
                 * 4 is for INVISIBLE
                 * 8 is for GONE
                 **/
                lblTitle.setVisibility( 0 );
            }

            else{
                //Custom dialog to show message
                AlertDialog.Builder builder = new AlertDialog.Builder(ChargingStationFinder.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog2, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                // this block will dismiss any dialog after 2 Sec. (time can be set to any value
                final Handler mHandler = new Handler();  // Make Main UIWorker Thread to execute this statement

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                },2000L);
                // this block will dismiss any dialog after 2 Sec. (time can be set to any value
            }

        } );



        jsonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intentDetails = new Intent(ChargingStationFinder.this, ChargingStationDetails.class);
                //intentDetails.putExtra("Title", jsonDataArrayList.get(position));
                intentDetails.putExtra("title",jsonDataArrayList.get(position).getTitle());
                intentDetails.putExtra("latitude",jsonDataArrayList.get(position).getLatitude());
                intentDetails.putExtra("longitude",jsonDataArrayList.get(position).getLongitude());
                intentDetails.putExtra("phone",jsonDataArrayList.get(position).getPhone());
                startActivity(intentDetails);
            }
        });

    }

     /*
      Shared preferences block starts
     */

    @Override
    protected void onPause() {
        super.onPause();
        saveDataToSharedPreferences();
    }

    public void getDataFromSharedPreferences()
    {
        ArrayList<String> listLatLong = new ArrayList<>();
        listLatLong= SharedPrefs.getArrayPrefs("LatLong",ChargingStationFinder.this);
        if(!listLatLong.isEmpty() && listLatLong.get(0)!=null && listLatLong.get(1)!=null) {
           txtLat.setText(listLatLong.get(0));
           txtLong.setText(listLatLong.get(1));
        }
    }

    public void saveDataToSharedPreferences()
    {
        ArrayList<String> listLatLong = new ArrayList<>();
        //loading Latitude and Longitude values to an ArrayList<String> to be loaded via Shared Preferences
        listLatLong.add(txtLat.getText().toString());
        listLatLong.add(txtLong.getText().toString());
        SharedPrefs.setArrayPrefs("LatLong",listLatLong,ChargingStationFinder.this);
    }

    /*
      Shared preferences block ends
     */




/**
 * let Android know it is to be used as the XML menu resource that was created as
 * the context menu for the view item registered.
 **/
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    /**
     * This Activity class now detects long-presses on listview item, but we still
     * need to define what should happen when each item in context menu is selected.
     **/
    public boolean onContextItemSelected(MenuItem item) {
        // Getting which listview item is long pressed for context menu
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        jsonArrayListIndexSelectedForContextMenu = info.position;
        //jsonDataArrayList.get(jsonArrayListIndexSelectedForContextMenu).getTitle();
        //Toast.makeText(this, jsonDataArrayList.get(info.position).getTitle(), Toast.LENGTH_SHORT).show();
        //find out which menu item was pressed
        switch (item.getItemId()) {
            case R.id.addFav_item:
                //passing index of selected ArrayList (for context operation) to get all the data for saving to database
                saveToDatabase(jsonArrayListIndexSelectedForContextMenu);
                return true;
            case R.id.removeFav_item:
                //passing index of selected ArrayList (for context operation) to get all the data for saving to database
                removeFromDatabase(jsonArrayListIndexSelectedForContextMenu);
                return true;
            default:
                return false;
        }
    }

    /**
     * getting selected listview item values from jsonDataArrayList by selectedIndex
     * saving the selected arrayList item to database
     **/
    private void saveToDatabase(int selectedIndex) {
        boolean alreadyExists = false;
        Cursor res = myDb.getAllData();
        long id = res.getCount()+1;

        //getting selected listview item values from jsonDataArrayList by selectedIndex
        String title = jsonDataArrayList.get(selectedIndex).getTitle();
        double latitude = jsonDataArrayList.get(selectedIndex).getLatitude();
        double longitude = jsonDataArrayList.get(selectedIndex).getLongitude();
        String phone = jsonDataArrayList.get(selectedIndex).getPhone();

        //checking if the selected item is already in the database or not
        while(res.moveToNext()) {
            //finding match of selected jsonDataArrayList object with retrieved data from database
            if(res.getString(1).equals(title)){
                Toast.makeText(this, "This charging station is already in favourites.", Toast.LENGTH_SHORT).show();
                alreadyExists = true;
            }
        }
        if(!alreadyExists) {
            //saving the selected arrayList item to database
            boolean isInserted = myDb.insertData(id, title, latitude, longitude, phone);
            Log.e( TAG, "isInserted: " + isInserted);
            if (isInserted)
                Toast.makeText(ChargingStationFinder.this, "Data inserted successfully", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ChargingStationFinder.this, "Data insert failed", Toast.LENGTH_LONG).show();
        }
        //res = myDb.getAllData();
        //Toast.makeText(this, "No. of saved data = " + res.getCount(), Toast.LENGTH_SHORT).show();
    }

    /**
     * getting selected listview item values from jsonDataArrayList by selectedIndex
     * removing the selected arrayList item from database
     **/
    private void removeFromDatabase(int selectedIndex) {

        boolean isFound = false;
        boolean hasRemoved = false;
        //getting selected listview item values from jsonDataArrayList by selectedIndex
        String title = jsonDataArrayList.get(selectedIndex).getTitle();
        double latitude = jsonDataArrayList.get(selectedIndex).getLatitude();
        double longitude = jsonDataArrayList.get(selectedIndex).getLongitude();
        String phone = jsonDataArrayList.get(selectedIndex).getPhone();

        //retrieving favourites datasets from ChargingStation database
        Cursor res = myDb.getAllData();
        //checking for no data in database
        if(res.getCount() == 0) {Toast.makeText(ChargingStationFinder.this,"This charging station not found in favourites",Toast.LENGTH_LONG).show();}
        else {
            while (res.moveToNext()) {
                //finding match of selected jsonDataArrayList object with retrieved data from database
                if (res.getString(1).equals(title)) {
                    isFound = true;
                    myDb.deleteData(Long.toString(res.getLong(0)));
                    hasRemoved = true;
                    Toast.makeText(this, "Charging station successfully removed from favourites.", Toast.LENGTH_SHORT).show();

                }
            }
        }
        if(!isFound){
            Toast.makeText(this, "This charging station not found in favourites.", Toast.LENGTH_SHORT).show();
        }

        if(hasRemoved) {
            //refreshing data after deletion of specific charging station from database
            loadFavouritesFromDatabaseToJsonListView();
            //String lblTitle_fav = getString( R.string.lblTitle_fav );
            //lblTitle.setText( lblTitle_fav );
            //lblTitle.setVisibility( 0 );
        }
    }

    /**
     * let Android know it is to be used as the XML option menu resource
     * that was created as the options menu for the view item registered.
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * we still need to define what should happen when each item
     * in options menu is selected.
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favourites_item:
                loadFavouritesFromDatabaseToJsonListView();
                return true;
            case R.id.help_item:
                // will write my code
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFavouritesFromDatabaseToJsonListView(){

        // retrieving favourites datasets from ChargingStation database
        Cursor res = myDb.getAllData();
        //checking for no data in database
        if(res.getCount() == 0) {
            Toast.makeText(ChargingStationFinder.this,"No favourite charging station found",Toast.LENGTH_LONG).show();
        }
        else {
            //clearing jsonListView by clearing jsonArrayList and the listadapter
            ListAdapter listAdapter = new ListAdapter();
            listAdapter.clearAdapter();

            String lblTitle_fav = getString(R.string.lblTitle_fav);
            lblTitle.setText(lblTitle_fav);
            lblTitle.setVisibility(0);

            while (res.moveToNext()) {
                //saving retrieved data from database to JsonDataArrayList
                JsonDataArray jsonDataArray = new JsonDataArray( res.getString( 1 ), res.getDouble( 2 ), res.getDouble( 3 ), res.getString( 4 ) );
                jsonDataArrayList.add( jsonDataArray );
                jsonListView.setAdapter( listAdapter );
                listAdapter.notifyDataSetChanged();
            }
        }
    }


    public class ChargingStationQuery extends AsyncTask<Void,Integer,Void> {
        String data ="";
        String dataParsed = "";
        String singleParsed ="";
        String title, phone;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //URL url = new URL("https://api.openchargemap.io/v3/poi/?output=json&latitude=45.4215&longitude=-75.6972&maxresults=10");
                /** Get user input from EditText latitude and longitude
                 * Convert EditText text to double
                 **/
                latitude = Double.parseDouble(strLatitude);
                longitude = Double.parseDouble(strLongitude);

                Log.e(TAG, "latitude value =" + latitude);
                Log.e(TAG, "longitude value =" + longitude);

                URL url = new URL("https://api.openchargemap.io/v3/poi/?output=json&latitude=" + latitude + "&longitude=" + longitude +"&maxresults=50&compact=true&verbose=false");
                //URL url = new URL("https://api.openchargemap.io/v3/poi/?output=json&countrycode=US&maxresults=40&compact=true&verbose=false");
                Log.e(TAG, "url: " + url );
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while(line != null){
                    line = bufferedReader.readLine();
                    data = data + line;
                }

                //clearing jsonListView by clearing jsonArrayList and the listadapter
                ListAdapter listAdapter = new ListAdapter();
                listAdapter.clearAdapter();

                JSONArray JA = new JSONArray(data);
                for(int i =0 ;i <JA.length(); i++){
                    if(JA!=null)
                    {
                        JSONObject JO = (JSONObject) JA.getJSONObject(i);
                        if(JO!=null)
                        {
                        JSONObject JO2 = JO.getJSONObject("AddressInfo");

                            if(JO2!=null)
                            {
                                title = JO2.getString("Title");
                                publishProgress(20);
                                double latitude = JO2.getDouble("Latitude");
                                publishProgress(40);
                                double longitude = JO2.getDouble("Longitude");
                                publishProgress(60);
                                //phone has null values and need to ask prof how to deal with JSON null
                                //so used Town instead of phone to test the app
                                phone = JO2.getString("Town");

                                publishProgress(80);

                                //saving fetched JSON data to JsonDataArray
                                JsonDataArray jsonDataArray = new JsonDataArray(title,latitude,longitude,phone);
                                jsonDataArrayList.add(jsonDataArray);

                            singleParsed =  "Charging station: " + JO2.getString("Title");

                            dataParsed = dataParsed + singleParsed +"\n" ;

                            }
                        }
                    }

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InputMismatchException e){
                e.printStackTrace();
            } catch (NumberFormatException e){
                e.printStackTrace();
            }

            publishProgress(100);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pBar.setVisibility(View.VISIBLE);
            pBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListAdapter listAdapter = new ListAdapter();
            jsonListView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
            //pBar.setVisibility(View.INVISIBLE);

        }
    }

    private class ListAdapter extends BaseAdapter {

        private static final String TAG = "ListAdapter";
        private Context mContext;
        private String title;
        private int mResource;

        public int getCount() {
            return jsonDataArrayList.size();
        } //This function tells how many objects to show

        public Object getItem(int position) {
            return jsonDataArrayList.get(position);
        }  //This returns the string at position p

        public long getItemId(int p) {
            return p;
        } //This returns the database id of the item at position p

        // to clear listview, we need to clear the ArrayList connected to the adapter
        public void clearAdapter()
        {
            jsonDataArrayList.clear();
            notifyDataSetChanged();
        }


        public View getView(int p, View customView, ViewGroup parent) {
            View thisRow = customView;

            thisRow = getLayoutInflater().inflate(R.layout.list_inflate_view, null);
            TextView title = (TextView) thisRow.findViewById(R.id.title);
            title.setText(jsonDataArrayList.get(p).getTitle());
            return thisRow;
        }
    }


}