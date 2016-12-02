/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Observable;

/**
 *
 * @author K
 */
public class Model extends Observable {
    HeuristicAlgorithm ha = new HeuristicAlgorithm();
    SCIPAlgorithm sA = new SCIPAlgorithm();
    BruteForceAlgorithm bA = new BruteForceAlgorithm();
    ApproximationAlgorithm aA = new ApproximationAlgorithm();
    DynamicAlgorithm dynamicAlgorithm = new DynamicAlgorithm();
}
