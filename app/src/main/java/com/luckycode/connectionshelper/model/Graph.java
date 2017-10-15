package com.luckycode.connectionshelper.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void addVertex(TownVertex townVertex){
        if(vertexes.add(townVertex))
            listener.onVertexAdded(townVertex);
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public void setVertexes(Set<TownVertex> vertexes) {
        this.vertexes = vertexes;
    }

    public void updateEdges(TownVertex vertex) {
        for(TownVertex mVertex: vertexes){
            if(!vertex.equals(mVertex)){
                int weight=distance(vertex,mVertex);
                addEdge(vertex,mVertex,weight);
            }
        }
    }

    private int distance(TownVertex vertex, TownVertex mVertex) {
        return ((int) SimpleLocation.calculateDistance(vertex.getLat(),vertex.getLng(),
                mVertex.getLat(),mVertex.getLng()));
    }

    private void addEdge(TownVertex n1, TownVertex n2,int weight){
        Edge edge=new Edge(n1,n2,weight);
        if(edges.add(edge))
            listener.onEdgeAdded(edge);
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
    }
}