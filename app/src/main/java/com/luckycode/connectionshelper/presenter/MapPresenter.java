package com.luckycode.connectionshelper.presenter;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.luckycode.connectionshelper.common.LuckyPresenter;
import com.luckycode.connectionshelper.interactor.MapInteractor;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Graph;
import com.luckycode.connectionshelper.model.Town;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.ui.viewModel.MapActivityView;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter.PlaceAutocomplete;
import com.luckycode.connectionshelper.utils.DatabaseHelper;

/**
 * Created by marcelocuevas on 10/1/17.
 */

public class MapPresenter extends LuckyPresenter<MapActivityView> implements MapInteractor.PlaceListener,Graph.Listener {
    private MapInteractor mapInteractor;
    private DatabaseHelper dbHelper;
    private Graph graph;

    public MapPresenter(MapActivityView mView,DatabaseHelper dbHelper,Graph graph) {
        super(mView);
        mapInteractor=new MapInteractor(dbHelper,this);
        this.graph=graph;
        this.graph.setListener(this);
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
    public void onSuccessPlace(TownVertex town) {
        graph.addVertex(town);
        graph.updateEdges(town);
        getView().hideRecyclerView();
        getView().updateMap(town);
    }

    @Override
    public void onErrorPlace() {
        Log.e("Something went ","wrong");
    }

    @Override
    public void onEdgeAdded(Edge edge) {
        mapInteractor.storeEdgeInDB(edge);
    }

    @Override
    public void onVertexAdded(TownVertex townVertex) {
        mapInteractor.storeVertexInDB(townVertex);
    }
}
