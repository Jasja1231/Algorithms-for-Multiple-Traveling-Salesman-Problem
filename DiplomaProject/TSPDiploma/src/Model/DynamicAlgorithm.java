/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.HeldKarp;

/**
 *
 * @author Krzysztof
 */
public class DynamicAlgorithm  implements Algorithm{

    @Override
    public String getName() {
      return "Dynamic algorithm";
    }
    
    @Override
    public int[] solveProblem(float[][] adjacencyMatrix, Object... o) {
        return HeldKarp.performHeldKarp(adjacencyMatrix);
    }
    

}
