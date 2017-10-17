package com.luckycode.connectionshelper.utils;

import com.luckycode.connectionshelper.model.TownVertex;
import java.util.Set;

/**
 * Created by marcelocuevas on 9/30/17.
 */

/**
 * This is a helper class which allows a given Set<TownVertex> to be turned into a disjoint set
 * @param vertices The vertices which will make up the set
 */

public class DisjointSet {
    private Set<TownVertex> vertices;

    public DisjointSet(Set<TownVertex> vertices){
        this.vertices = vertices;

        for (TownVertex v: vertices)
            v.setParent(v);
    }

    public TownVertex find(TownVertex v){
        // If the node is it's own parent, the root has been found
        if (v.getParent().equals(v)){
            return v;
        }
        // If not, recursively travel up the root
        return find(v.getParent());
    }

    public void union(TownVertex v1, TownVertex v2){
        // Finds the two roots of the given vertices
        TownVertex v1Root = find(v1);
        TownVertex v2Root = find(v2);

        // If the roots are the same, do not merge
        if (v1Root.equals(v2Root)){
            return;
        }
        // Merge the roots together and remove the redundant node from the list
        v1Root.setParent(v2Root);
        vertices.remove(v1);
    }
}
