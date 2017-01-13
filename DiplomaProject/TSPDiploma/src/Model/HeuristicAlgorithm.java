/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.DFS;
import Algorithms.Greedy;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Krzysztof
 */
public class HeuristicAlgorithm implements Algorithm {
    @Override
    public  int [] solveProblem (float [][]adjacencyMatrix, Object... a)
    {
        int numSalesmen = (int)a[0];
        
        float [][] reducedMatrix = new float[adjacencyMatrix.length-1][adjacencyMatrix.length-1];
        for (int i=1;i<adjacencyMatrix.length;i++)
        {
            for (int j=1;j<adjacencyMatrix.length;j++)
            {
                reducedMatrix[i-1][j-1] = adjacencyMatrix[i][j];
            }
        }
        
        int[][] adjMatrix = new int[adjacencyMatrix.length][adjacencyMatrix.length];
        int [] solution = null;
        Greedy greedy = new Greedy();
        greedy.readInGraphData(reducedMatrix);
        greedy.GreedySearch();
        Vector edges = greedy.getSolutionEdges();
        for (int i = 0; i < edges.size()-numSalesmen+1;i++)//there are n-1 edges already
        {
            Greedy.Edge edge = (Greedy.Edge)edges.get(i);
            adjMatrix[edge.from+1][edge.to+1] = 1;
            adjMatrix[edge.to+1][edge.from+1] = 1;
        }
        
        ArrayList<Integer> order = new ArrayList<>();
        boolean [] visited = new boolean [adjMatrix.length];
        boolean [] firstDegree = new boolean[adjMatrix.length];
        for (int i=0;i<firstDegree.length;i++)
        {
            if (isFirstDegree(i,adjMatrix))
                firstDegree[i]=true;
            else firstDegree[i] = false;
        }       
        //visited[0] = true; //we don't want to visit the base
        
        DFS.DFS(adjMatrix, visited, adjMatrix.length, 1, order);
        order.add(0);
        visited[0] = true;
        boolean unvisited = true;
        while(unvisited)
        {
            int index = findFirstDegreeIndex(visited,firstDegree);
            if (index>0)
            {
            DFS.DFS(adjMatrix, visited, adjMatrix.length, index, order);
            order.add(0);
            }
            else
                unvisited = false;
        }
        solution = new int [adjMatrix.length+numSalesmen];
        for (int i=0;i<order.size();i++)
            solution[i] = order.get(i);
        return solution;    
    }
    
    private int findIndex (boolean[]visited)
    {
        for (int i=0;i<visited.length;i++)
            if (!visited[i])
                return i;
        return -1;
    }
    
    
    @Override
    public String getName() {
      return "Heuristic algorithm";
    }

    private boolean isFirstDegree(int index, int[][] adjMatrix) 
    {
        int rowCount = 0, columnCount = 0;
        
        // check the matrix row
        for (int i=0;i<adjMatrix[index].length;i++)
        {
            if (adjMatrix[index][i]>0)
                rowCount++;
        }
        // check the matrix column
        for (int i=0;i<adjMatrix.length;i++)
        {
            if (adjMatrix[i][index]>0)
                columnCount++;
        }    
        return (!(rowCount>=2&&columnCount>=2));
    }

    private int findFirstDegreeIndex(boolean[] visited, boolean[] firstDegree) {
        for (int i=0;i<visited.length;i++)
            if (visited[i]==false&&firstDegree[i]==true)
                return i;
        return -1;
    }
}