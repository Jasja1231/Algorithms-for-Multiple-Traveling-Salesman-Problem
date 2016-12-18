/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Yaryna
 */
public class HeldKarp {
    
    
    
    /***
     * Implementation of Held-Karp, an algorithm that solves the Traveling
     * Salesman Problem using dynamic programming with memoization.
     * @param dists distance matrix
     */
    public static int [] performHeldKarp(float[][]dists){//def held_karp(dists):
        int n = dists.length;//n = len(dists)
        //Maps each subset of the nodes to the cost to reach that subset, as well
        //as what node it passed before reaching this subset.
        //Node subsets are represented as set bits.
        
        /***
         * Python dictionary → Java HashMap
         * A HashMap is like a Python dictionary, but uses only object syntax. There are no literals. Declaration and creation look like:
         * HashMap<keyType, valueType> variable = new HashSet<keyType, valueType>();
         * Some methods: type put(key, value) (returns previous value), type get(key), boolean containsKey(key).
         */
         HashMap<Key,Value> C = new HashMap<>();//C = {}
        //Set transition cost from initial state
        for(int k = 1; k < n ; k++){// for k  in range(1, n):
           C.put(new Key(1 << k,k),new Value(dists[0][k],0)); //C[(1 << k, k)] = (dists[0][k], 0)
        }
    //Iterate subsets of increasing length and store intermediate results
    //in classic dynamic programming manner
        for(int subset_size=2;subset_size < n ; subset_size++){//for subset_size in range(2, n):
           for(int[] subset : Combinations.getCombinations(1, n-1, subset_size)){//for subset in itertools.combinations(range(1, n), subset_size):
                // Set bits for all nodes in this subset
                int bits = 0;
                for (int bit : subset)
                    bits |= 1 << bit;

                //Find the lowest cost to get to this subset
                int prev = 0; 
                for (int k : subset){
                    prev = ~(1 << k) & bits;

                   ArrayList<Value> res = new ArrayList<>(); ///^?????
                    for(int m : subset){
                        if (m == 0 || m == k)
                            continue;
                        
                       /* Value resValue = new Value(C.get(new Key(prev,m)).getDistance()+dists[m][k],m);
                        res.add(resValue);//res.add((C[(prev, m)][0] + dists[m][k], m));*/
                        float dist = dists[m][k];
                        Key key = new Key(prev,m);
                        float distanceFromGetDistance = C.get(key).getDistance();
                        Value resValue = new Value(distanceFromGetDistance+dist,m);
                        
                        res.add(resValue);
                    }
                    C.put(new Key(bits, k),getMinimumResult(res));//C[(bits, k)] = min(res);
                }
            }
        }
    // We're interested in all bits but the least significant (the start state)
    int bits = (int) ((Math.pow(2, n) - 1) - 1);

    // Calculate optimal cost
   ArrayList<Value> res = new ArrayList<>();
    for (int k = 1; k < n ; k++)
        res.add(new Value(C.get(new Key(bits,k)).getDistance() + dists[k][0], k));
    Value opt = getMinimumResult(res);
    Value parent = getMinimumResult(res);
    
    //Backtrack to find full path
    ArrayList<Integer> path = new ArrayList<>();//path = []
    for (int i = 0 ; i < n-1 ; i++){//i in range(n - 1):
        path.add(parent.getVertexID());
        int new_bits = bits & ~(1 << parent.getVertexID());
        parent = C.get(new Key(bits,parent.getVertexID()));
        bits = new_bits;
    }
    // Add implicit start state
    path.add(0);
    Collections.reverse(path);
    return /*opt,*/convertIntegers(path);
 }

    private static Value getMinimumResult(ArrayList<Value> res) {
        Value minVal = new Value(Float.MAX_VALUE,-1);
        for(Value v : res)
            if(v.getDistance() < minVal.getDistance())
                minVal = v;
        
        return minVal;
    }
    
    
    private static class Key {
        private int fromIndexId;
        private int toIndexId;
        
        public Key(int from, int to){
            this.fromIndexId = from;
            this.toIndexId = to;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + this.fromIndexId;
            hash = 23 * hash + this.toIndexId;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if (this.fromIndexId != other.fromIndexId) {
                return false;
            }
            if (this.toIndexId != other.toIndexId) {
                return false;
            }
            return true;
        }
        
        
    }
    
    private static class Value {
       private float distance;
       private int vertexId;
        
        public Value(float d, int v){
            this.distance = d;
            this.vertexId = v;
        }
        
        public float getDistance(){return this.distance;}
        public int getVertexID(){return this.vertexId;}
    }
    
    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next();
        }
        return ret;
    }
    
}
