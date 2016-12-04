/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.DFS;
import Algorithms.Greedy;

/**
 *
 * @author Krzysztof
 */
public class HeuristicAlgorithm {
    public static int [] solveProblem (float [][]adjacencyMatrix)
    {
        int [] solution = null;
        Greedy greedy = new Greedy();
        greedy.readInGraphData(adjacencyMatrix);
        greedy.GreedySearch();
        solution = greedy.getSolutionVertices();
        return solution;    
    }
}
