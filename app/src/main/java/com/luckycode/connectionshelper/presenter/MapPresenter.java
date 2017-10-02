package com.luckycode.connectionshelper.presenter;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.luckycode.connectionshelper.common.LuckyPresenter;
import com.luckycode.connectionshelper.interactor.MapInteractor;
import com.luckycode.connectionshelper.ui.viewModel.MapActivityView;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter.PlaceAutocomplete;
import com.luckycode.connectionshelper.utils.DatabaseHelper;

/**
 * Created by marcelocuevas on 10/1/17.
 */

public class MapPresenter extends LuckyPresenter<MapActivityView> implements MapInteractor.PlaceListener {
    private MapInteractor mapInteractor;
    private DatabaseHelper dbHelper;

    public MapPresenter(MapActivityView mView,DatabaseHelper dbHelper) {
        super(mView);
        mapInteractor=new MapInteractor(dbHelper,this);
    }

    public void onTextChanged(CharSequence s,int count){
        if(count>0) {
            getView().showClearButton();
            getView().setAdapter();
        }else
            getView().hideClearButton();
        if(s.toString()!="")
            getView().filter(s.toString());
    }

    public void handlePlaceClick(PlaceAutocomplete placeAutocomplete,GoogleApiClient googleApiClient){
        mapInteractor.getPlaceByID(placeAutocomplete,googleApiClient);
    }

    @Override
    public void onSuccessPlace(Place place) {
        getView().hideRecyclerView();
        getView().updateMap(place);
    }

    @Override
    public void onErrorPlace() {
        Log.e("Something went ","wrong");
    }
}
