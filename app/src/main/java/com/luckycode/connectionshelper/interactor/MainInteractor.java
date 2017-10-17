package com.luckycode.connectionshelper.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.luckycode.connectionshelper.common.LuckyInteractor;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.presenter.SplashPresenter;
import com.luckycode.connectionshelper.utils.DatabaseHelper;
import com.luckycode.connectionshelper.utils.SettingsManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by marcelocuevas on 10/14/17.
 */

public class MainInteractor extends LuckyInteractor<SplashPresenter> {
    private static final String TOWN_JSON_FILE_NAME="towns.json";
    private Context context;
    private DatabaseHelper dbHelper;
    private SettingsManager settingsManager;
    private InteractorListener listener;

    public MainInteractor(Context context, DatabaseHelper dbHelper, InteractorListener listener){
        this.dbHelper=dbHelper;
        this.context=context;
        this.listener=listener;
        settingsManager=new SettingsManager(context);
    }

    public void loadDatabaseData() {
        if(settingsManager.isFirstTime()){
            storeLocalJSONIntoDatabase();
            settingsManager.setNoFirstTime();
        }else{
            Set<TownVertex> townVertexes= new HashSet<>(getTowns());
            listener.onDataLoaded(townVertexes);
        }
    }

    public void storeEdgesInDatabase(Set<Edge> edges){
        try {
            Dao dao=dbHelper.getDaoEdges();
            for(Edge edge:edges){
                dao.create(edge);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void storeVertexesInDatabase(Set<TownVertex> vertexes){
        try {
            Dao dao=dbHelper.getDaoVertexes();
            for(TownVertex vertex:vertexes){
                dao.create(vertex);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void storeLocalJSONIntoDatabase(){
        TownVertex[] towns=loadTownsFromJSON();
        try {
            Dao dao=dbHelper.getDaoVertexes();
            for(TownVertex town:towns){
                dao.create(town);
            }
            listener.onLocalJSONStored(Arrays.asList(towns));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private TownVertex[] loadTownsFromJSON(){
        Gson gson=new Gson();
        AssetManager assetManager=context.getAssets();
        try {
            InputStream ims=assetManager.open(TOWN_JSON_FILE_NAME);
            Reader reader=new InputStreamReader(ims);
            return gson.fromJson(reader,TownVertex[].class);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<TownVertex> getTowns(){
        try {
            Dao dao=dbHelper.getDaoVertexes();
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Edge> getEdges(){
        try {
            Dao dao=dbHelper.getDaoEdges();
            return dao.queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public double getNormalCost(){
        return settingsManager.getNormalCost();
    }

    public double getExtraLargeDistance(){
        return settingsManager.getExtraCostForDistanceGreaterThan200();
    }

    public double getExtraDifferentCountries(){
        return settingsManager.getExtraCostForDifferentCountries();
    }

    public interface InteractorListener{
        void onLocalJSONStored(List<TownVertex> towns);
        void onDataLoaded(Set<TownVertex> vertexes);
    }
}
