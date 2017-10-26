package com.luckycode.connectionshelper.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by marcelocuevas on 10/16/17.
 */
public class TownVertexTest {
    private TownVertex sanMiguelVertex,morenoVertex,
        pilarVertex,otherVertex;

    @Before
    public void setUp() throws Exception {
        sanMiguelVertex=new TownVertex("ChIJb-3Xjn-9vJURUR122z3xQaE","San Miguel","Argentina",424224,
                -34.5430549,-58.71185);

        morenoVertex=new TownVertex("ChIJ_54UwluUvJUR-rOSS_J29HI","Moreno","Argentina",24244,
                -34.634009899,-58.791382);

        pilarVertex=new TownVertex("ChIJAyA9ZLacvJURNrY09gIE2qg","Pilar","Argentina",244244,
                -34.4778620,-58.9091671);

        otherVertex=new TownVertex("ChIJAyA9ZLacvJURNrY09gIE2qg","Asunción","Paraguay",2424224,-34.66,-58.9959);
    }

    @Test
    public void sameCountryTest() {
        assertTrue(morenoVertex.hasSameCountryAs(sanMiguelVertex));
        assertTrue(sanMiguelVertex.hasSameCountryAs(pilarVertex));
        assertTrue(pilarVertex.hasSameCountryAs(morenoVertex));
        assertFalse(pilarVertex.hasSameCountryAs(otherVertex));
    }

    @Test
    public void equalsTest()  {
        assertFalse(pilarVertex.equals(morenoVertex));
        assertFalse(morenoVertex.equals(sanMiguelVertex));
        assertFalse(sanMiguelVertex.equals(pilarVertex));
        assertTrue(pilarVertex.equals(otherVertex)); //Son iguales porque tienen el mismo ID
    }
}