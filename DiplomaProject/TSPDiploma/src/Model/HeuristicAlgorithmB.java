/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.DFS;
import Algorithms.GreedyHeuristic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 *
 * @author Yaryna
 */
public class HeuristicAlgorithmB implements Algorithm {
    @Override
    public  int [] solveProblem (float [][]adjacencyMatrix, Object... a)
    {
        int numSalesmen = (int)a[0];
        
        EdgeCostPair[]BestReturnEdges = new EdgeCostPair[numSalesmen];
        for (int i=0;i<BestReturnEdges.length;i++)
            BestReturnEdges[i] = new EdgeCostPair(-1,-1,Float.MAX_VALUE);
        int n = adjacencyMatrix[0].length;
        int[][] adjMatrix = new int[adjacencyMatrix.length][adjacencyMatrix.length];
        int [] solution = null;
        GreedyHeuristic greedy = new GreedyHeuristic();
        greedy.ReadInGraphDataIgnoreBase(adjacencyMatrix);
        greedy.GreedySearch();
        Vector edges = greedy.getSolutionEdges();
        for (int i = 0; i < edges.size();i++)
        {
            GreedyHeuristic.Edge edge = (GreedyHeuristic.Edge)edges.get(i);
            adjMatrix[edge.from+1][edge.to+1] = 1;
            adjMatrix[edge.to+1][edge.from+1] = 1;
        }
        
        ArrayList<Integer> order = new ArrayList<>();
        boolean [] visited = new boolean [n];
        visited[0] = true;
        DFS.DFS(adjMatrix, visited, n, 1, order);
        EdgeCostPair[]AllReturnEdgesCosts = new EdgeCostPair[n-1];
        int idx = 0;
        for (int i=0;i<order.size();i++)
        {
            int from = 0, to = 0;
            float cost;
            if (i<order.size()-1)
            {
                from = order.get(i);
                to = order.get(i+1);
            }
            else if (i==order.size()-1)
            {
                from = order.get(i);
                to = order.get(0);
            }
            cost = (adjacencyMatrix[from][0] + adjacencyMatrix[0][to]) - adjacencyMatrix[from][to];
            AllReturnEdgesCosts[idx++] = new EdgeCostPair(from,to,cost);
        }
       // order.remove(order.size()-1);
        Arrays.sort(AllReturnEdgesCosts, new EdgeComparator());

        
       solution = new int [n+numSalesmen-1];
       
       for (int i=0,i2=0;i<order.size();i++)
       {
           boolean isReturnEdge = false;
           int from,to;
           if (i<order.size()-1)
            {
                from  = order.get(i);
                to = order.get(i+1);
            }
            else
            {
                from  = order.get(i);
                to = order.get(0);
            }
           for (int j=0;j<numSalesmen;j++)
           {
               EdgeCostPair current = AllReturnEdgesCosts[j];
               if (from == current.getfomIndex() && to == current.gettoIndex())
                   isReturnEdge = true;
           }  
           solution[i2++] = order.get(i);
           if (isReturnEdge)
              i2++;
       }
/*
       for (int i=0,i2=0;i<order.size();i++)
       {
          boolean contains = false;
          for (int j=0;j<numSalesmen;j++)
          {
           if (order.get(i)==AllReturnEdgesCosts[j].getIndex())
               contains = true;
          }
          if(contains)
              i2++;
          
          solution[i2++] = order.get(i)+1;
       }*/
       return solution;
    }

    @Override
    public String getName() {
        return "HeuristicAlgorithmB";
    }
    
  }
    
