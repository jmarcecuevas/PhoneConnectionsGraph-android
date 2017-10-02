package com.luckycode.connectionshelper.model;

import java.util.ArrayList;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public class Graph {

    ArrayList<Vertex> vertices;
    ArrayList<Edge> edges;

    public Graph(){
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void addVertex(char value){
        vertices.add(new Vertex(value));
    }

    public void addEdge(int weight, Vertex n1, Vertex n2){
        edges.add(new Edge(weight, n1, n2));
    }

    public Vertex getNode(char value){
        Vertex target = null;

        for (Vertex n : vertices)
            if (n.getValue() == value)
                target = n;

        return target;
    }

    public ArrayList<Edge> getEdges(){
        return edges;
    }

}