package com.luckycode.connectionshelper.utils;

import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Graph;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.utils.DisjointSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public class Kruskal{

    public static ArrayList<Edge> execute(Graph g) {
        Graph mGraph=new Graph(g.getVertexes(),g.getEdges());
        ArrayList<Edge> mst = new ArrayList<>();

        // Sorts the edges from smallest to largest
        ArrayList<Edge> edges = new ArrayList<>(mGraph.getEdges());
        Collections.sort(edges);

        DisjointSet ds = new DisjointSet(mGraph.getVertexes());

        // Iterates through the edges (still in order)
        for (Edge e : edges) {
            TownVertex n1 = e.getOrigin();
            TownVertex n2 = e.getDestination();

            // If the two vertices are NOT in the same disjoint set, merge the two and add the current edge to MST
            if (!(ds.find(n1).equals(ds.find(n2)))) {
                mst.add(e);
                ds.union(n1, n2);
            }
        }
        return mst;
    }


    public static double calculateMSTCost(List<Edge> edges){
        double count=0;
        for(Edge edge:edges){
            count+=edge.getWeight();
        }
        return count;
    }

}