package com.luckycode.connectionshelper.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import im.delight.android.location.SimpleLocation;

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
    private double weight;

    public Edge(){}

    public Edge(TownVertex origin, TownVertex destination,double normalCost,
                double extraDiff,double extraLargeDistance) {
        this.origin = origin;
        this.destination = destination;
        setWeight(normalCost,extraDiff,extraLargeDistance);
    }

    public Edge(TownVertex origin,TownVertex destination,double weight){
        this.origin=origin;
        this.destination=destination;
        this.weight=weight;
    }

    public double getDistanceInKms() {
        return SimpleLocation.calculateDistance(origin.getLat(),origin.getLng(),
                destination.getLat(),destination.getLng())/1000;
    }

    public boolean vertexesHaveSameCountry(){
        return origin.hasSameCountryAs(destination);
    }

    public boolean distanceIsGreaterThan200(){
        return getDistanceInKms()>200.0;
    }

    public void setWeight(double normalCost,double extraDiff,double extraLargeDistance){
        weight=getDistanceInKms()*normalCost;
        if(!vertexesHaveSameCountry())
            weight+=extraDiff;
        if(distanceIsGreaterThan200())
            weight+=extraLargeDistance;
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

    @Override
    public int compareTo(Edge e) {
        return (int)(this.weight - e.weight);
    }

    public void setWeight(double weight){
        this.weight=weight;
    }

    public double getWeight() {
        return weight;
    }

    public TownVertex getDestination() {
        return destination;
    }

    public TownVertex getOrigin() {
        return origin;
    }

    public void setOrigin(TownVertex origin) {
        this.origin = origin;
    }

    public void setDestination(TownVertex destination) {
        this.destination = destination;
    }
}