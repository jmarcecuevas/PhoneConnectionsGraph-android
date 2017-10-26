package com.luckycode.connectionshelper.model;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by marcelocuevas on 10/16/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class GraphTest {
    private Graph graph;
    private TownVertex vertex,otherVertex;

    @Before
    public void setUp() throws Exception {
        graph=new Graph();
        vertex=new TownVertex("ChIJb-3Xjn-9vJURUR122z3xQaE","San Miguel","Argentina",424224,
                -34.5430549,-58.71185);
        otherVertex=new TownVertex("ChIJ_54UwluUvJUR-rOSS_J29HI","Moreno","Argentina",24244,
                -34.634009899,-58.791382);
    }

    @Test
    public void addVertexTest(){
        TownVertex vertex=new TownVertex("ChIJb-3Xjn-9vJURUR122z3xQaE","San Miguel","Argentina",424224,
                -34.5430549,-58.71185);
        int size= graph.getVertexes().size();
        graph.addVertex(vertex);
        assertEquals(graph.getVertexes().size(),size+1);

        TownVertex other= new TownVertex("ChIJb-3Xjn-9vJURUR122z3xQaE","Moreno","Argentina",424224,
                -34.5430549,-58.71185);
        graph.addVertex(other);
        //El size no cambia porque estamos en un Set y no se permiten elementos repetidos(en nuestro caso, con mismo ID)
        assertEquals(graph.getVertexes().size(),size+1);

        TownVertex otherVertex=new TownVertex("ChIxQaE","San Miguel","Argentina",424224,
                -34.5430549,-58.71185);
        graph.addVertex(otherVertex);
        //En este caso agrega el nuevo Vertex porque no se encuentra en el Set un elemento con ese ID y crece el size.
        assertEquals(graph.getVertexes().size(),size+2);
    }

    @Test
    public void addEdgeTest(){
        Edge edge=new Edge();
        edge.setOrigin(vertex);
        edge.setDestination(otherVertex);
        int size=graph.getEdges().size();
        graph.getEdges().add(edge);
        assertEquals(graph.getEdges().size(),size+1);

        graph.getEdges().add(edge);
        //No agrega: mismo edge y se trata de un Set
        assertEquals(graph.getEdges().size(),size+1);

        Edge otherVertex=new Edge();
        otherVertex.setOrigin(new TownVertex("242mddf","Moreno","Argentina",32424,-34.55,-58.9290));
        otherVertex.setDestination(new TownVertex("434","San Miguel","Argentina",343562,-34.55,-58.9290));
        graph.getEdges().add(otherVertex);
        //Lo agrega
        assertEquals(graph.getEdges().size(),size+2);
    }

}