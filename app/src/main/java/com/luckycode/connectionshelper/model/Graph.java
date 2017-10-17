package com.luckycode.connectionshelper.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import im.delight.android.location.SimpleLocation;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public class Graph implements Serializable{
    private Listener listener;
    private Set<TownVertex> vertexes;
    private Set<Edge> edges;

    public Graph(){
        vertexes = new HashSet<>();
        edges = new HashSet<>();
    }

    public void addVertex(TownVertex townVertex){
        if(vertexes.add(townVertex)) {
            if(listener!=null)
                listener.onVertexAdded(townVertex);
        }
    }

    private void addEdge(TownVertex n1, TownVertex n2,double normalCost,double extraDiff,
                         double extraLargeDistance){
        Edge edge=new Edge(n1,n2,normalCost,extraDiff,extraLargeDistance);
        if(edges.add(edge)) {
            if(listener!=null)
                listener.onEdgeAdded(edge);
        }
    }

    public void updateEdges(TownVertex vertex,double normalCost,double extraDiff,double extraLargeDistance) {
        for(TownVertex mVertex: vertexes){
            if(!vertex.equals(mVertex)){
                addEdge(vertex,mVertex,normalCost,extraDiff,extraLargeDistance);
            }
        }
    }

    public void recalculateWeights(double normalCost,double extraDiff,double extraLargeDistance){
        for(Edge edge:edges)
            edge.setWeight(normalCost,extraDiff,extraLargeDistance);
        listener.onEdgesWeightsRecalculated(edges);
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public void setVertexes(Set<TownVertex> vertexes) {
        this.vertexes = vertexes;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Set<Edge> getEdges(){
        return edges;
    }

    public Set<TownVertex> getVertexes() {
        return vertexes;
    }

    public interface Listener{
        void onEdgeAdded(Edge edge);
        void onVertexAdded(TownVertex townVertex);
        void onEdgesWeightsRecalculated(Set<Edge> edges);
    }
}