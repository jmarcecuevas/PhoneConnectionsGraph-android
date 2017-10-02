package com.luckycode.connectionshelper.model;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public class Edge implements Comparable<Edge>{
    private Vertex origin;
    private Vertex destination;
    private int weight;

    public Edge(int weight, Vertex origin, Vertex destination){
        this.weight = weight;
        this.origin = origin;
        this.destination = destination;
    }

    public int getWeight() {
        return weight;
    }

    public Vertex getDestination() {
        return destination;
    }

    public Vertex getOrigin() {
        return origin;
    }

    @Override
    public int compareTo(Edge e) {
        return this.weight - e.weight;
    }
}