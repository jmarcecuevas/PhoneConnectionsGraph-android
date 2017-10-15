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

public class SplashPresenter extends LuckyPresenter<SplashView> implements MainInteractor.InteractorListener{
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
            graph.updateEdges(town);
        }
        interactor.storeEdgesInDatabase(graph.getEdges());
        getView().onGraphReady(graph);
    }

    @Override
    public void onDataLoaded(Set<TownVertex> vertexes, Set<Edge> edges) {
        Graph graph=new Graph();
        graph.setVertexes(vertexes);
        graph.setEdges(edges);
        getView().onGraphReady(graph);
    }
}
