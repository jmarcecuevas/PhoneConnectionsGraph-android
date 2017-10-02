package com.luckycode.connectionshelper.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by marcelocuevas on 9/30/17.
 */

@DatabaseTable(tableName = "Places")
public class PlaceModel {
    @DatabaseField
    private String id;
    @DatabaseField
    private CharSequence name;
    @DatabaseField
    private CharSequence address;
    @DatabaseField
    private String country;
    @DatabaseField
    private double lat;
    @DatabaseField
    private double lng;

    public PlaceModel(CharSequence name, CharSequence address, String country, double lat, double lng){
        this.name=name;
        this.address=address;
        this.country=country;
        this.lat=lat;
        this.lng=lng;
    }

    public String getId() {
        return id;
    }

    public CharSequence getName() {
        return name;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public CharSequence getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }
}

