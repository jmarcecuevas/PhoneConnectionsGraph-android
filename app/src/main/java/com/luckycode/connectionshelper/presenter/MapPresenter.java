package com.luckycode.connectionshelper.presenter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.luckycode.connectionshelper.common.LuckyPresenter;
import com.luckycode.connectionshelper.interactor.MapInteractor;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Graph;
import com.luckycode.connectionshelper.utils.Kruskal;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.ui.viewModel.MapActivityView;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter.PlaceAutocomplete;
import com.luckycode.connectionshelper.utils.DatabaseHelper;

import java.util.List;
import java.util.Set;

/**
 * Created by marcelocuevas on 10/1/17.
 */

public class MapPresenter extends LuckyPresenter<MapActivityView> implements MapInteractor.Listener,Graph.Listener {
    private MapInteractor mapInteractor;
    private Graph graph;

    public MapPresenter(Context context, MapActivityView mView, DatabaseHelper dbHelper, Graph graph) {
        super(mView);
        mapInteractor=new MapInteractor(context,dbHelper,this);
        this.graph=graph;
        this.graph.setListener(this);
    }

    @Override
    public void onEdgesWeightsRecalculated(Set<Edge> edges) {
        mapInteractor.clearEdgesTable();
        mapInteractor.storeEdgesInDatabase(edges);
        getView().showRecalculatedGraph();
    }

    @Override
    public void onConnectionCostsReady(double normalCost, double extraDiff, double extraLargeDistance) {
        getView().showSettingsDialog(String.valueOf(normalCost),String.valueOf(extraDiff),
                String.valueOf(extraLargeDistance));
    }

    @Override
    public void onEdgeAdded(Edge edge) {
        mapInteractor.storeEdgeInDB(edge);
    }

    @Override
    public void onVertexAdded(TownVertex townVertex) {
        mapInteractor.storeVertexInDB(townVertex);
    }

    public void getConnectionCosts(){
        mapInteractor.getConnectionCosts();
    }

    public void changeConnectionsCost(String normalCost,String extraDiff,String extraLargeDistance){
        mapInteractor.storeConnectionsCost(Double.valueOf(normalCost),Double.valueOf(extraDiff),
                Double.valueOf(extraLargeDistance));
    }

    public void recalculateWeights(Graph graph,String normalCost,String extraDiff,String extraLargeDistance) {
        graph.recalculateWeights(Double.valueOf(normalCost),Double.valueOf(extraDiff),
                Double.valueOf(extraLargeDistance));
    }

    public void handlePlaceClick(PlaceAutocomplete placeAutocomplete,GoogleApiClient googleApiClient,int population){
        mapInteractor.getPlaceByID(placeAutocomplete,googleApiClient,population);
    }

    private void sendMarkers(Set<TownVertex> vertexes){
        for(TownVertex vertex:vertexes)
            getView().drawMarker(new LatLng(vertex.getLat(),vertex.getLng()),vertex.getName());
    }

    private void sendRoutes(List<Edge> edges){
        for(Edge edge:edges)
            getView().drawRoute(new LatLng(edge.getOrigin().getLat(), edge.getOrigin().getLng()), new LatLng(edge.
                    getDestination().getLat(), edge.getDestination().getLng()), Color.YELLOW);
    }

    private void sendRoutes(Set<Edge> edges){
        for(Edge edge:edges)
            getView().drawRoute(new LatLng(edge.getOrigin().getLat(), edge.getOrigin().getLng()), new LatLng(edge.
                    getDestination().getLat(), edge.getDestination().getLng()), Color.RED);
    }

    public void getMapInfoToDraw(Graph graph,boolean withRoutes) {
        if(withRoutes)
            sendRoutes(graph.getEdges());
        sendMarkers(graph.getVertexes());
    }

    public void calculateSolution(Graph graph) {
        sendMarkers(graph.getVertexes());
        List<Edge> solution= Kruskal.execute(graph);
        double totalCost=Kruskal.calculateMSTCost(solution);
        sendRoutes(solution);
        getView().showTotalCost(String.format("%.2f", totalCost));
    }

    @Override
    public void onErrorPlace() {
        Log.e("Something went ","wrong");
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

    @Override
    public void onSuccessPlace(TownVertex town) {
        graph.addVertex(town);
        graph.updateEdges(town,mapInteractor.getNormalCost(),
                mapInteractor.getExtraDifferentCountries(),
                mapInteractor.getExtraLargeDistance());
        getView().hideRecyclerView();
        getView().updateMap(town);
    }
}
