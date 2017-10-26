package com.luckycode.connectionshelper.model;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import im.delight.android.location.SimpleLocation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by marcelocuevas on 10/17/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class EdgeTest {
    private Edge edge;
    private TownVertex vertex,otherVertex;

    @Before
    public void setUp() throws Exception {
        edge=new Edge();

        vertex=new TownVertex("ChIJb-3Xjn-9vJURUR122z3xQaE","San Miguel","Argentina",424224,
                -34.5430549,-58.71185);
        otherVertex=new TownVertex("ChIJ_54UwluUvJUR-rOSS_J29HI","Moreno","Argentina",24244,
                -34.634009899,-58.791382);

        edge.setOrigin(vertex);
        edge.setDestination(otherVertex);
    }

    @Test
    public void compareToTest(){
        Edge other=new Edge();
        edge.setWeight(65);
        other.setWeight(45);
        assertEquals(edge.compareTo(other),20);

        other.setWeight(65);
        assertEquals(edge.compareTo(other),0);
    }

    @Test
    public void equalsTest(){
        String object="";
        assertFalse(edge.equals(object));

        Edge other=new Edge();
        assertFalse(edge.equals(other));

        other.setOrigin(vertex);
        other.setDestination(otherVertex);
        assertTrue(edge.equals(other));

        other.setOrigin(otherVertex);
        other.setDestination(vertex);
        assertTrue(edge.equals(other));

        other.setOrigin(new TownVertex("sdfs23d","Pilar","Argentina",34344,-35.232,-58.66));
        assertFalse(edge.equals(other));
    }

    @Test
    public void vertexesHaveSameCountry(){
        assertTrue(edge.vertexesHaveSameCountry());

        TownVertex other=new TownVertex("ChIJb-3Xjn-9vJURUR122z3xQaE","San Miguel","Brasil",424224,
                -34.5430549,-58.71185);
        edge.setDestination(other);
        assertFalse(edge.vertexesHaveSameCountry());
    }



}