package com.luckycode.connectionshelper.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by marcelocuevas on 10/16/17.
 */
public class TownVertexTest {
    private TownVertex sanMiguelVertex,morenoVertex,
        pilarVertex;

    @Before
    public void setUp() throws Exception {
        sanMiguelVertex=new TownVertex("ChIJb-3Xjn-9vJURUR122z3xQaE","San Miguel","Argentina",424224,
                -34.5430549,-58.71185);

        morenoVertex=new TownVertex("ChIJ_54UwluUvJUR-rOSS_J29HI","Moreno","Argentina",24244,
                -34.634009899,-58.791382);

        pilarVertex=new TownVertex("ChIJAyA9ZLacvJURNrY09gIE2qg","Pilar","Argentina",244244,
                -34.4778620,-58.9091671);
    }

    @Test
    public void sameCountryTest() throws Exception {
        assertTrue(morenoVertex.hasSameCountryAs(sanMiguelVertex));
        assertTrue(sanMiguelVertex.hasSameCountryAs(pilarVertex));
        assertTrue(pilarVertex.hasSameCountryAs(morenoVertex));
    }

    @Test
    public void equalsTest() throws Exception {
        assertFalse(pilarVertex.equals(morenoVertex));
        assertFalse(morenoVertex.equals(sanMiguelVertex));
        assertFalse(sanMiguelVertex.equals(pilarVertex));
    }
}