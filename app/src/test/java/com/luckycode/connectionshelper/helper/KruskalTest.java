package com.luckycode.connectionshelper.helper;

import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Graph;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.utils.DisjointSet;
import com.luckycode.connectionshelper.utils.Kruskal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by marcelocuevas on 10/25/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class KruskalTest{
    private Graph graph;
    private TownVertex sanMiguelVertex,morenoVertex,pilarVertex;
    private Set<TownVertex> vertexes;
    private Set<Edge> edges;

    @Before
    public void setUp() throws Exception {
        vertexes=new HashSet<>();
        edges=new HashSet<>();

        sanMiguelVertex=new TownVertex("ChIJb-3Xjn-9vJURUR122z3xQaE","San Miguel","Argentina",424224,
                -34.5430549,-58.71185);
        morenoVertex=new TownVertex("ChIJ_54UwluUvJUR-rOSS_J29HI","Moreno","Argentina",24244,
                -34.634009899,-58.791382);
        pilarVertex=new TownVertex("ChIJAyA9ZLacvJURNrY09gIE2qg","Pilar","Argentina",244244,
                -34.4778620,-58.9091671);
        vertexes.add(sanMiguelVertex);
        vertexes.add(morenoVertex);

        Edge smToMoreno=new Edge(sanMiguelVertex,morenoVertex,3);
        edges.add(smToMoreno);

        graph=new Graph(vertexes,edges);
    }

    @Test
    public void kruskalTest(){
        //Unica arista(dos vértices)
        List<Edge> result=Kruskal.execute(graph);
        assertThat(result.size(),is(1));
        assertTrue(result.get(0).getOrigin().equals(sanMiguelVertex));
        assertTrue(result.get(0).getDestination().equals(morenoVertex));

        //Mas de una arista
        vertexes.add(pilarVertex);
        Edge smToPilar=new Edge(sanMiguelVertex,pilarVertex,2);
        Edge morenoToPilar=new Edge(morenoVertex,pilarVertex,1);
        edges.add(smToPilar);
        edges.add(morenoToPilar);
        Graph graph=new Graph(vertexes,edges);
        List<Edge> newResult=Kruskal.execute(graph);
        assertEquals(newResult.size(),graph.getEdges().size()-1);

        //No hay ciclos em el MST
        for(Edge edge:newResult){
            assertFalse(edge.getOrigin().equals(edge.getDestination()));
        }
    }
}