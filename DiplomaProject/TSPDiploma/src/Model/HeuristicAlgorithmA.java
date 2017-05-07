/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.DFS;
import Algorithms.GreedyHeuristic;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Krzysztof
 */
public class HeuristicAlgorithmA implements Algorithm {
    @Override
    public  int [] solveProblem (float [][]adjacencyMatrix, Object... a)
    {
        int numSalesmen = (int)a[0];
        
        int[][] adjMatrix = new int[adjacencyMatrix.length][adjacencyMatrix.length];
        int [] solution = null;
        GreedyHeuristic greedy = new GreedyHeuristic();
        greedy.ReadInGraphDataIgnoreBase(adjacencyMatrix);
        greedy.GreedySearch();
        Vector edges = greedy.getSolutionEdges();
        for (int i = 0; i < edges.size()-numSalesmen+1;i++)//there are n-1 edges already
        {
            GreedyHeuristic.Edge edge = (GreedyHeuristic.Edge)edges.get(i);
            adjMatrix[edge.from+1][edge.to+1] = 1;
            adjMatrix[edge.to+1][edge.from+1] = 1;
        }
        
        ArrayList<Integer> order = new ArrayList<>();
        boolean [] visited = new boolean [adjMatrix.length];
        int [] firstDegree = new int[numSalesmen*2];
        for (int i=0, k=0;i<adjMatrix[0].length;i++)
        {
            if (isFirstDegree(i,adjMatrix))
                firstDegree[k++]=i;
        }       
        //visited[0] = true; 
        
        //DFS.DFS(adjMatrix, visited, adjMatrix.length, 1, order);
        //order.add(0);
        visited[0] = true; //we don't want to visit the base
        boolean unvisited = true;
        while(unvisited)
        {
            int index = findFirstDegreeIndex(visited,firstDegree);
            if (index>0)
            {
            order.add(0);
            DFS.DFS(adjMatrix, visited, adjMatrix.length, index, order);
            }
            else
                unvisited = false;
        }
        solution = new int [adjMatrix.length+numSalesmen];
        for (int i=0;i<order.size();i++)
            solution[i] = order.get(i);
        return solution;    
    }
 
    
    
    @Override
    public String getName() {
      return "HeuristicAlgorithmA";
    }

    private boolean isFirstDegree(int index, int[][] adjMatrix) 
    {
        int rowCount = 0, columnCount = 0;
        if (index == 0)
            return false;
        for (int i=0;i<adjMatrix[index].length;i++)
        {
            if (adjMatrix[index][i]>0)        // check the matrix row
                rowCount++;
            if (adjMatrix[i][index]>0)        // check the matrix column
                columnCount++;
            
           if (rowCount>=2&&columnCount>=2)
               return false;           
        }
        
        
        return true;
    }

    private int findFirstDegreeIndex(boolean[] visited, int[] firstDegree) {
        for (int i=0;i<firstDegree.length;i++)
            if (visited[firstDegree[i]]==false)
                return firstDegree[i];
        return -1;
    }
}