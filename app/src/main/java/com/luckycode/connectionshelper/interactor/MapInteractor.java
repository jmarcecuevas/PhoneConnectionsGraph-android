package com.luckycode.connectionshelper.interactor;

import android.database.SQLException;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.j256.ormlite.dao.Dao;
import com.luckycode.connectionshelper.common.LuckyInteractor;
import com.luckycode.connectionshelper.common.LuckyPresenter;
import com.luckycode.connectionshelper.model.PlaceModel;
import com.luckycode.connectionshelper.presenter.MapPresenter;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter.PlaceAutocomplete;
import com.luckycode.connectionshelper.utils.DatabaseHelper;

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
        setDaoPlace();
    }

    public void getPlaceByID(PlaceAutocomplete placeAutocomplete,GoogleApiClient googleApiClient){
        final String placeID=String.valueOf(placeAutocomplete.placeId);
        PendingResult<PlaceBuffer> placeResult= Places.GeoDataApi.getPlaceById(googleApiClient,placeID);
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if(places.getCount()==1){
                    Place place=places.get(0);
                    PlaceModel placeModel=new PlaceModel(place.getName(),place.getAddress(),place.getLocale().getCountry(),
                            place.getLatLng().latitude,place.getLatLng().longitude);
                    storePlaceInDB(placeModel);
                    listener.onSuccessPlace(places.get(0));
                }else{
                    listener.onErrorPlace();
                }
            }
        });
    }

    private void setDaoPlace() {
        try {
            dao=dbHelper.getDaoPlace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private void storePlaceInDB(PlaceModel place){
        try {
            dao.create(place);
        }catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    public interface PlaceListener{
        void onSuccessPlace(Place place);
        void onErrorPlace();
    }
}


