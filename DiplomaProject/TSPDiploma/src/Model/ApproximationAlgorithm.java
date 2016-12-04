/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.DFS;
import Algorithms.Kruskal;

/**
 *
 * @author Krzysztof
 */
public class ApproximationAlgorithm {
     public static int [] solveProblem (float [][]adjacencyMatrix)
    {
        int [] solution;
        Kruskal k = new Kruskal();
        k.readInGraphData(adjacencyMatrix);
        k.performKruskal();
        int[][] kruskalMatrix = k.getResultAdjacencyMatrix();
        for (int y=0;y<kruskalMatrix.length;y++)
        {
            for (int x=0;x<kruskalMatrix[y].length;x++)
            {
                if (kruskalMatrix[x][y] == 1 || kruskalMatrix[y][x] ==1)
                {
                    kruskalMatrix[x][y] = 1;
                    kruskalMatrix[y][x] = 1;
                }    
            }
        }
        solution = new int [kruskalMatrix.length+1];
        int idx = 0;
        DFS.DFS(kruskalMatrix, new boolean[kruskalMatrix.length], solution, kruskalMatrix.length, 0, idx);
        return solution;
    }
}
