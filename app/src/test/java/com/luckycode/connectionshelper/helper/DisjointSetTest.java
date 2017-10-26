package com.luckycode.connectionshelper.helper;

import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.utils.DisjointSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by marcelocuevas on 10/24/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class DisjointSetTest {
    private DisjointSet disjointSet;
    private Set<TownVertex> vertexes;

    @Before
    public void setUp() throws Exception {
        vertexes=new HashSet<>();
        TownVertex sanMiguelVertex=new TownVertex("ChIJb-3Xjn-9vJURUR122z3xQaE","San Miguel","Argentina",424224,
                -34.5430549,-58.71185);
        TownVertex morenoVertex=new TownVertex("ChIJ_54UwluUvJUR-rOSS_J29HI","Moreno","Argentina",24244,
                -34.634009899,-58.791382);
        TownVertex pilarVertex=new TownVertex("ChIJAyA9ZLacvJURNrY09gIE2qg","Pilar","Argentina",244244,
                -34.4778620,-58.9091671);
        TownVertex otherVertex=new TownVertex("ChIJAyA9ZLacvJURNrY09gIE2qg","Asunción","Paraguay",2424224,-34.66,-58.9959);
        vertexes.add(sanMiguelVertex);
        vertexes.add(morenoVertex);
        vertexes.add(pilarVertex);
        vertexes.add(otherVertex);

        disjointSet=new DisjointSet(vertexes);
    }

    @Test
    public void findTest(){
        DisjointSet disjointSet=Mockito.mock(DisjointSet.class);
        TownVertex vertex=new TownVertex("ChIJAyA9ZLacvJURNrY09gIE2qg","Pilar","Argentina",244244,
                -34.4778620,-58.9091671);
        vertex.setParent(vertex);
        disjointSet.find(vertex);
        verify(disjointSet,times(1)).find(vertex);

        vertex.setParent(new TownVertex("ChIJAyA9ZLacvJURNrY09gIE2qg","Pilar","Argentina",244244,
                -34.4778620,-58.9091671));
        disjointSet.find(vertex);
        verify(disjointSet,atLeast(2)).find(vertex);
    }

    @Test
    public void unionTest(){
        TownVertex v1=new TownVertex("ChIJAyA9ZLacvJURNrY09gIE2qg","Pilar","Argentina",244244,
                -34.4778620,-58.9091671);
        TownVertex v2=new TownVertex("dfsf43r43r","San Miguel","Argentina",353532,
                -33.4778620,-57.651671);
        v1.setParent(v1);
        v2.setParent(v1);
        disjointSet.union(v1,v2);
        assertThat(v1.getParent().equals(v2.getParent()),is(true));

        int size=disjointSet.getVertices().size();
        v1.setParent(v1);
        v2.setParent(v2);
        disjointSet.union(v1,v2);
        assertEquals(disjointSet.getVertices().size(),size-1);
    }
}
