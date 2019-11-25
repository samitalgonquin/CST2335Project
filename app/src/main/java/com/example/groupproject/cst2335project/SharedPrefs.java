package com.example.groupproject.cst2335project;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SharedPrefs {

    private static final String SP_NAME ="sharedPrefs";

    public static void setPrefs(String key, String value, Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPrefs(String key, Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, "notfound");
    }

    public static void setArrayPrefs(String arrayName, ArrayList<String> array, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putString(arrayName + "_" + i, array.get(i));
        editor.apply();
    }

    public static ArrayList<String> getArrayPrefs(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        ArrayList<String> array = new ArrayList<>(size);
        for(int i=0;i<size;i++)
            array.add(prefs.getString(arrayName + "_" + i, null));
        return array;
    }
}

/*

Follow these steps as per your requirements in the project.
For saving and retrieving a string:
1. String value;
SharedPrefs.setPrefs("value","info",MainActivity.this);
value = SharedPrefs.getPrefs("value",SomeActivity.this)

2. For saving and retrieving an array:
ArrayList<String> mOrderList = new ArrayList<String>();
SharedPrefs.setArrayPrefs("OrderList",mOrderList,context);
mOrderList= SharedPrefs.getArrayPrefs("OrderList",context);

 */

