package com.luckycode.connectionshelper.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public class Kruskal{

    public static ArrayList<Edge> execute(Graph g) {

        // The minimum spanning tree that is the result of our algorithm
        ArrayList<Edge> mst = new ArrayList<>();

        // Sorts the edges from smallest to largest
        ArrayList<Edge> edges = g.getEdges();
        Collections.sort(edges);

        // Creates a new Disjoint Set helper class, passing it all the vertices in the graph
        DisjointSet ds = new DisjointSet(g.vertices);

        // Iterates through the edges (still in order)
        for (Edge e : edges) {

            // For each edges, get the two vertices
            Vertex n1 = e.getOrigin();
            Vertex n2 = e.getDestination();

            // If the two vertices are NOT in the same disjoint set, merge the two and add the current edge to MST
            if (!(ds.find(n1).equals(ds.find(n2)))) {
                mst.add(e);
                ds.union(n1, n2);
            }
        }
        return mst;
    }

}