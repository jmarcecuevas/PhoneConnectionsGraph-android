package com.luckycode.connectionshelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marcelocuevas on 10/16/17.
 */

public class SettingsManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME="MyPreferences";
    private static final String FIRST_TIME="firstTime";
    private static final String NORMAL_COST="normalCost";
    private static final String EXTRA_DIFFERENT_COUNTRIES="extraDifferentCountries";
    private static final String EXTRA_DISTANCE_GREATER_THAN_200="extraDistanceGreaterThan200";

    public SettingsManager(Context context){
        prefs=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor=prefs.edit();
    }

    public void setNoFirstTime(){
        editor.putBoolean(FIRST_TIME,false);
        editor.commit();
    }

    public void storeNormalCost(double cost){
        editor.putFloat(NORMAL_COST, (float) cost);
        editor.commit();
    }

    public void storeExtraCostForDifferentCountries(double cost){
        editor.putFloat(EXTRA_DIFFERENT_COUNTRIES, (float) cost);
        editor.commit();
    }

    public void storeExtraCostForDistanceGreaterThan200(double cost){
        editor.putFloat(EXTRA_DISTANCE_GREATER_THAN_200, (float) cost);
        editor.commit();
    }

    public boolean isFirstTime(){
        return prefs.getBoolean(FIRST_TIME,true);
    }

    public double getNormalCost(){
        return prefs.getFloat(NORMAL_COST,10);
    }

    public double getExtraCostForDifferentCountries(){
        return prefs.getFloat(EXTRA_DIFFERENT_COUNTRIES,50);
    }

    public double getExtraCostForDistanceGreaterThan200(){
        return prefs.getFloat(EXTRA_DISTANCE_GREATER_THAN_200,85);
    }

}
