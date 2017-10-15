package com.luckycode.connectionshelper.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.luckycode.connectionshelper.common.LuckyInteractor;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Town;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.presenter.SplashPresenter;
import com.luckycode.connectionshelper.utils.DatabaseHelper;

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

    private static final String PREF_NAME="MyPreferences";
    private static final String FIRST_TIME="firstTime";
    private static final String TOWN_JSON_FILE_NAME="towns.json";

    private Context context;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private InteractorListener listener;

    public MainInteractor(Context context, DatabaseHelper dbHelper, InteractorListener listener){
        this.dbHelper=dbHelper;
        this.context=context;
        this.listener=listener;
        prefs=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor=prefs.edit();
    }

    public void loadDatabaseData() {
        if(isFirstTime()){
            storeLocalJSONIntoDatabase();
            setNoFirstTime();
        }else{
            Set<TownVertex> townVertexes= new HashSet<>(getTowns());
            Set<Edge> edges= new HashSet<>(getEdges());
            listener.onDataLoaded(townVertexes,edges);
        }
    }

    public void setNoFirstTime(){
        editor.putBoolean(FIRST_TIME,false);
        editor.commit();
    }

    private boolean isFirstTime(){
        return prefs.getBoolean(FIRST_TIME,true);
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

    public interface InteractorListener{
        void onLocalJSONStored(List<TownVertex> towns);
        void onDataLoaded(Set<TownVertex> vertexes,Set<Edge> edges);
    }

}
