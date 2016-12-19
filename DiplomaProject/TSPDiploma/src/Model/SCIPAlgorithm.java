/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Krzysztof
 */
public class SCIPAlgorithm  implements Algorithm {

    @Override
    public int[] solveProblem(float[][] adjacencyMatrix, Object... o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getName() {
      return "SCIP algorithm";
    }
    
}
