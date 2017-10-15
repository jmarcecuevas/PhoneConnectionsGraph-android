package com.luckycode.connectionshelper.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.TownVertex;

import java.sql.SQLException;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "places.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<TownVertex,Integer> daoVertexes;
    private Dao<Edge,Integer> daoEdges;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,TownVertex.class);
            TableUtils.createTable(connectionSource,Edge.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(database, connectionSource);
    }

    public Dao<TownVertex,Integer> getDaoVertexes() throws SQLException{
        if(daoVertexes == null){
            daoVertexes = getDao(TownVertex.class);
        }
        return daoVertexes;
    }

    public Dao<Edge,Integer> getDaoEdges() throws SQLException{
        if(daoEdges == null){
            daoEdges = getDao(Edge.class);
        }
        return daoEdges;
    }

    @Override
    public void close() {
        super.close();
        daoVertexes=null;
        daoEdges=null;
    }
}
