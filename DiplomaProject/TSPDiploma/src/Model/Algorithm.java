/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Yaryna
 */
public interface Algorithm {
    public/*static*/ int [] solveProblem (float [][]adjacencyMatrix, Object... o);
    
    public String getName();
}
