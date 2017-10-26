package com.luckycode.connectionshelper.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.luckycode.connectionshelper.common.LuckyInteractor;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.presenter.MapPresenter;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter.PlaceAutocomplete;
import com.luckycode.connectionshelper.utils.DatabaseHelper;
import com.luckycode.connectionshelper.utils.SettingsManager;

import java.sql.SQLException;
import java.util.Set;

/**
 * Created by marcelocuevas on 10/1/17.
 */

public class MapInteractor extends LuckyInteractor<MapPresenter> {
    private Listener listener;
    private DatabaseHelper dbHelper;
    private SettingsManager settingsManager;

    public MapInteractor(Context context, DatabaseHelper dbHelper){
        this.dbHelper=dbHelper;
        settingsManager=new SettingsManager(context);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void getPlaceByID(PlaceAutocomplete placeAutocomplete, GoogleApiClient googleApiClient, final int population){
        final String placeID=String.valueOf(placeAutocomplete.placeId);
        PendingResult<PlaceBuffer> placeResult= Places.GeoDataApi.getPlaceById(googleApiClient,placeID);
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if(places.getCount()==1){
                    Place place=places.get(0);
                    TownVertex townVertex=new TownVertex(place.getId(),place.getName().toString(),place.getLocale().getCountry(),
                            population,place.getLatLng().latitude,place.getLatLng().longitude);
                    listener.onSuccessPlace(townVertex);
                }else{
                    listener.onErrorPlace();
                }
            }
        });
    }

    public void storeVertexInDB(TownVertex town) {
        try {
            Dao dao=dbHelper.getDaoVertexes();
            dao.create(town);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void storeEdgeInDB(Edge edge){
        try {
            Dao dao=dbHelper.getDaoEdges();
            dao.create(edge);
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void clearEdgesTable(){
        try {
            TableUtils.clearTable(dbHelper.getConnectionSource(),Edge.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getConnectionCosts(){
        listener.onConnectionCostsReady(getNormalCost(),getExtraDifferentCountries(),
                getExtraLargeDistance());
    }

    public void storeConnectionsCost(double normal, double extraDiff, double extraLargeDistance) {
        settingsManager.storeNormalCost(normal);
        settingsManager.storeExtraCostForDifferentCountries(extraDiff);
        settingsManager.storeExtraCostForDistanceGreaterThan200(extraLargeDistance);
    }

    public double getNormalCost(){
        return settingsManager.getNormalCost();
    }

    public double getExtraDifferentCountries(){
        return settingsManager.getExtraCostForDifferentCountries();
    }

    public double getExtraLargeDistance(){
        return settingsManager.getExtraCostForDistanceGreaterThan200();
    }


    public interface Listener {
        void onSuccessPlace(TownVertex place);
        void onErrorPlace();
        void onConnectionCostsReady(double normalCost,double extraDiff, double extraLargeDistance);
    }
}


