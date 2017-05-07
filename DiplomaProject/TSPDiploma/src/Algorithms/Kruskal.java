package Algorithms;

import java.util.*;


public class Kruskal {
  private final int MAX_NODES = 25000;
  private HashSet nodes[];               // Array of connected components
  private TreeSet allEdges;              // Priority queue of Edge objects
  private Vector allNewEdges;            // Edges in Minimal-Spanning Tree
  int vertexCount;


  public Kruskal() {
    // Constructor
    nodes = new HashSet[MAX_NODES];      // Create array for components
    allEdges = new TreeSet(new Edge());  // Create empty priority queue
    allNewEdges = new Vector(MAX_NODES); // Create vector for MST edges
  }

  
  
      public void ReadInGraphDataIgnoreBase(float[][] matrix) {
        vertexCount = matrix.length-1;
      
      for (int y=1;y<matrix.length;y++)
      {
          for (int x=1;x<matrix[y].length;x++)
          {
            int from = x;
            int to   = y;
            int cost = (int)(1000*matrix[x][y]);
            if(cost!=0 && from != to)
            allEdges.add(new Edge(from-1, to-1, cost));  // Update priority queue
            if (nodes[from-1] == null) {
              // Create set of connect components [singleton] for this node
              nodes[from-1] = new HashSet(2*MAX_NODES);
              nodes[from-1].add(new Integer(from-1));
            }

            if (nodes[to-1] == null) {
              // Create set of connect components [singleton] for this node
              nodes[to-1] = new HashSet(2*MAX_NODES);
              nodes[to-1].add(new Integer(to-1));
            }
          }   
      }
    }
  public void readInGraphData(float[][]matrix)
  {
      vertexCount = matrix.length;
      
      for (int y=0;y<matrix.length;y++)
      {
          for (int x=0;x<matrix[y].length;x++)
          {
            int from = y;
            int to   = x;
            int cost = (int)(1000*matrix[y][x]);
            if (cost!=0 && from!=to)
            allEdges.add(new Edge(from, to, cost));  // Update priority queue
            if (nodes[from] == null) {
              // Create set of connect components [singleton] for this node
              nodes[from] = new HashSet(2*MAX_NODES);
              nodes[from].add(new Integer(from));
            }

            if (nodes[to] == null) {
              // Create set of connect components [singleton] for this node
              nodes[to] = new HashSet(2*MAX_NODES);
              nodes[to].add(new Integer(to));
            }
          }   
      }
  }

  public void performKruskal() {
    int size = allEdges.size();
    for (int i=0; i<size; i++) {
      Edge curEdge = (Edge) allEdges.first();
      if (allEdges.remove(curEdge)) {
        // successful removal from priority queue: allEdges

        if (nodesAreInDifferentSets(curEdge.from, curEdge.to)) {
          // System.out.println("Nodes are in different sets ...");
          HashSet src, dst;
          int dstHashSetIndex;

          if (nodes[curEdge.from].size() > nodes[curEdge.to].size()) {
            // have to transfer all nodes including curEdge.to
            src = nodes[curEdge.to];
            dst = nodes[dstHashSetIndex = curEdge.from];
          } else {
            // have to transfer all nodes including curEdge.from
            src = nodes[curEdge.from];
            dst = nodes[dstHashSetIndex = curEdge.to];
          }

          Object srcArray[] = src.toArray();
          int transferSize = srcArray.length;
          for (int j=0; j<transferSize; j++) {
            // move each node from set: src into set: dst
            // and update appropriate index in array: nodes
            if (src.remove(srcArray[j])) {
              dst.add(srcArray[j]);
              nodes[((Integer) srcArray[j]).intValue()] = nodes[dstHashSetIndex];
            } else {
              // This is a serious problem
              System.out.println("Something wrong: set union");
              System.exit(1);
            }
          }

          allNewEdges.add(curEdge);
          // add new edge to MST edge vector
        } else {
          // System.out.println("Nodes are in the same set ... nothing to do here");
        }

      } else {
        // This is a serious problem
        System.out.println("TreeSet should have contained this element!!");
        System.exit(1);
      }
    }
  }

  private boolean nodesAreInDifferentSets(int a, int b) {
    // returns true if graph nodes (a,b) are in different
    // connected components, ie the set for 'a' is different
    // from that for 'b'
    return(!nodes[a].equals(nodes[b]));
  }

  
  public int [][] getResultAdjacencyMatrix ()
  {
   int [][] edges = new int[vertexCount][vertexCount];
   for (Object e : allNewEdges)
   {
       Edge edge = (Edge)e;
       edges [edge.to][edge.from] = 1;
       edges[edge.from][edge.to] = 1;
   }
   return edges;
  }

  class Edge implements Comparator {
    // Inner class for representing edge+end-points
    public int from, to, cost;
    public Edge() {}
    
    public Edge(int f, int t, int c) {
      // Inner class constructor
      from = f; to = t; cost = c;
    }
    @Override
    public int compare(Object o1, Object o2) {
      // Used for comparisions during add/remove operations
      int cost1 = ((Edge) o1).cost;
      int cost2 = ((Edge) o2).cost;
      int from1 = ((Edge) o1).from;
      int from2 = ((Edge) o2).from;
      int to1   = ((Edge) o1).to;
      int to2   = ((Edge) o2).to;

      if (cost1<cost2)
        return(-1);
      else if (cost1==cost2 && from1==from2 && to1==to2)
        return(0);
      else if (cost1==cost2)
        return(-1);
      else if (cost1>cost2)
        return(1); 
      else
        return(0);
    }
    @Override
    public boolean equals(Object obj) {
      // Used for comparisions during add/remove operations
      Edge e = (Edge) obj;
      return (cost==e.cost && from==e.from && to==e.to);
    }
  }

}