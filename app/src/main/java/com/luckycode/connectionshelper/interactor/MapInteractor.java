package com.luckycode.connectionshelper.interactor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.j256.ormlite.dao.Dao;
import com.luckycode.connectionshelper.common.LuckyInteractor;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Town;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.presenter.MapPresenter;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter.PlaceAutocomplete;
import com.luckycode.connectionshelper.utils.DatabaseHelper;

import java.sql.SQLException;

/**
 * Created by marcelocuevas on 10/1/17.
 */

public class MapInteractor extends LuckyInteractor<MapPresenter> {
    private PlaceListener listener;
    private DatabaseHelper dbHelper;
    private Dao dao;

    public MapInteractor(DatabaseHelper dbHelper,PlaceListener listener){
        this.dbHelper=dbHelper;
        this.listener=listener;
    }

    public void getPlaceByID(PlaceAutocomplete placeAutocomplete,GoogleApiClient googleApiClient){
        final String placeID=String.valueOf(placeAutocomplete.placeId);
        PendingResult<PlaceBuffer> placeResult= Places.GeoDataApi.getPlaceById(googleApiClient,placeID);
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if(places.getCount()==1){
                    Place place=places.get(0);
                    TownVertex townVertex=new TownVertex(place.getId(),place.getName().toString(),place.getLocale().getCountry(),
                            23232,place.getLatLng().latitude,place.getLatLng().longitude);
                    listener.onSuccessPlace(townVertex);
                }else{
                    listener.onErrorPlace();
                }
            }
        });
    }



    private void storePlaceInDB(Town place){
        try {
            dao.create(place);
        }catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    public void storeVertexInDB(TownVertex town) {
        try {
            Dao dao=dbHelper.getDaoVertexes();
            dao.create(town);
            Log.e("VERTEX",String.valueOf(dao.queryForAll().size()));
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

    public interface PlaceListener{
        void onSuccessPlace(TownVertex place);
        void onErrorPlace();
    }
}


