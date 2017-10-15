package com.luckycode.connectionshelper.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by marcelocuevas on 9/30/17.
 */

@DatabaseTable (tableName = "edges")
public class Edge implements Comparable<Edge>,Serializable{
    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private TownVertex origin;
    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private TownVertex destination;
    @DatabaseField
    private int weight;

    public Edge(){}

    public Edge(TownVertex origin, TownVertex destination, int weight) {
        this.weight = weight;
        this.origin = origin;
        this.destination = destination;
    }

    public int getWeight() {
        return weight;
    }

    public TownVertex getDestination() {
        return destination;
    }

    public TownVertex getOrigin() {
        return origin;
    }

    @Override
    public int compareTo(Edge e) {
        return this.weight - e.weight;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Edge){
            Edge edge=(Edge)o;
            if((origin.equals(edge.getOrigin())&&destination.equals(edge.getDestination()))
                    || (destination.equals(edge.getOrigin()) && origin.equals(edge.getDestination())))
                return true;
            else
                return false;
        }else
            return false;
    }

    @Override
    public int hashCode() {
        int result = origin.hashCode();
        result = 31 * result + destination.hashCode();
        return result;
    }
}