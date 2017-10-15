package com.luckycode.connectionshelper.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by marcelocuevas on 9/30/17.
 */

@DatabaseTable (tableName = "vertexes")
public class Town implements Serializable {
    @DatabaseField(generatedId = true)
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

    public Town(String name, String country, long population, double lat, double lng){
        this.name=name;
        this.country=country;
        this.population=population;
        this.lat=lat;
        this.lng=lng;
    }

    public Town(){}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public String getCountry() {
        return country;
    }

    public long getPopulation() {
        return population;
    }

    public void setParent(TownVertex parent) {
        this.parent = parent;
    }

    public TownVertex getParent() {
        return parent;
    }
}

