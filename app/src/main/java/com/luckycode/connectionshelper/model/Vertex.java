package com.luckycode.connectionshelper.model;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public class Vertex {

    private char value;
    private PlaceModel town;
    private Vertex parent;

    Vertex(char value){
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setParent(Vertex parent) {
        this.parent = parent;
    }

    public Vertex getParent() {
        return parent;
    }
}