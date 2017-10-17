package com.luckycode.connectionshelper.presenter;

import android.content.Context;

import com.luckycode.connectionshelper.common.LuckyPresenter;
import com.luckycode.connectionshelper.interactor.MainInteractor;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Graph;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.ui.viewModel.SplashView;
import com.luckycode.connectionshelper.utils.DatabaseHelper;

import java.util.List;
import java.util.Set;

/**
 * Created by marcelocuevas on 10/14/17.
 */

public class SplashPresenter extends LuckyPresenter<SplashView> implements MainInteractor.InteractorListener,Graph.Listener{
    private MainInteractor interactor;

    public SplashPresenter(SplashView mView, Context context,DatabaseHelper dbHelper) {
        super(mView);
        interactor=new MainInteractor(context,dbHelper,this);
    }

    public void loadData() {
        interactor.loadDatabaseData();
    }

    @Override
    public void onLocalJSONStored(List<TownVertex> towns) {
        Graph graph=new Graph();

        for(TownVertex town:towns){
            graph.addVertex(town);
            graph.updateEdges(town,interactor.getNormalCost(),
                    interactor.getExtraDifferentCountries(),
                    interactor.getExtraLargeDistance());
        }

        interactor.storeEdgesInDatabase(graph.getEdges());
        interactor.storeVertexesInDatabase(graph.getVertexes());
        getView().onGraphReady(graph);
    }

    @Override
    public void onDataLoaded(Set<TownVertex> vertexes) {
        Graph graph=new Graph();
        graph.setVertexes(vertexes);
        for(TownVertex vertex:vertexes)
            graph.updateEdges(vertex,interactor.getNormalCost(),
                    interactor.getExtraDifferentCountries(),
                    interactor.getExtraLargeDistance());

        getView().onGraphReady(graph);
    }

    @Override
    public void onEdgeAdded(Edge edge) {

    }

    @Override
    public void onVertexAdded(TownVertex townVertex) {

    }

    @Override
    public void onEdgesWeightsRecalculated(Set<Edge> edges) {

    }
}
