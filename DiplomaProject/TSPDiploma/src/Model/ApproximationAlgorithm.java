/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.DFS;
import Algorithms.Kruskal;
import java.util.ArrayList;
import org.omg.CORBA.ACTIVITY_COMPLETED;

/**
 *
 * @author Krzysztof
 */
public class ApproximationAlgorithm implements Algorithm {
     public int [] solveProblem (float [][]adjacencyMatrix, Object... o)
    {
        int [] solution;
        Kruskal k = new Kruskal();
        k.readInGraphData(adjacencyMatrix);
        k.performKruskal();
        int[][] kruskalMatrix = k.getResultAdjacencyMatrix();
      /*  for (int y=0;y<kruskalMatrix.length;y++)
        {
            for (int x=0;x<kruskalMatrix[y].length;x++)
            {
                if (kruskalMatrix[x][y] == 1 || kruskalMatrix[y][x] ==1)
                {
                    kruskalMatrix[x][y] = 1;
                    kruskalMatrix[y][x] = 1;
                }    
            }
        }*/
        solution = new int [kruskalMatrix.length+1];
        int idx = 0;
        ArrayList<Integer> dupa = new ArrayList<Integer>();
        DFS.DFS(kruskalMatrix, new boolean[kruskalMatrix.length], solution, kruskalMatrix.length, 0, dupa);
        for (int i=0;i<dupa.size();i++)
            solution[i]=dupa.get(i);
        return solution;
    }
}