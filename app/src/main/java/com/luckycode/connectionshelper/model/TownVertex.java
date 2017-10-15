package com.luckycode.connectionshelper.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by marcelocuevas on 9/30/17.
 */

@DatabaseTable(tableName = "vertexes")
public class TownVertex implements Serializable{
    @DatabaseField
    private String id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String country;
    @DatabaseField
    private long population;
    @DatabaseField
    private double lat;
    @DatabaseField
    private double lng;
    private TownVertex parent;

    public TownVertex(String id,String name, String country, long population, double lat, double lng){
        this.id=id;
        this.name=name;
        this.country=country;
        this.population=population;
        this.lat=lat;
        this.lng=lng;
    }

    public TownVertex(){

    }

    public long getPopulation() {
        return population;
    }

    public String getCountry() {
        return country;
    }

    public double getLat() {
        return lat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TownVertex that = (TownVertex) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public double getLng() {
        return lng;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TownVertex getParent() {
        return parent;
    }

    public void setParent(TownVertex parent) {
        this.parent = parent;
    }
}