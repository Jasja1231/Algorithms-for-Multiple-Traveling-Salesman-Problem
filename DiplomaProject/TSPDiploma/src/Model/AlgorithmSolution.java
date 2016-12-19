/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 * Class to represent the solution of a single algorithm.
 * @author Yaryna
 */
public class AlgorithmSolution {
    private  ArrayList<ArrayList<Integer>> cycles;
    private String AlgorithmName;
    private int salesmenCount;
    private double allDistance;
    private ArrayList<Double> cyclesLenth;
    
    public AlgorithmSolution(){
    
    }

    public ArrayList<ArrayList<Integer>> getCycles() {
        return cycles;
    }

    public void setCycles(ArrayList<ArrayList<Integer>> cycles) {
        this.cycles = cycles;
    }

    public String getAlgorithmName() {
        return AlgorithmName;
    }

    public void setAlgorithmName(String AlgorithmName) {
        this.AlgorithmName = AlgorithmName;
    }

    public int getSalesmenCount() {
        return salesmenCount;
    }

    public void setSalesmenCount(int salesmenCount) {
        this.salesmenCount = salesmenCount;
    }

    public double getAllDistance() {
        return allDistance;
    }

    public void setAllDistance(double allDistance) {
        this.allDistance = allDistance;
    }

    public ArrayList<Double> getCyclesLenth() {
        return cyclesLenth;
    }

    public void setCyclesLenth(ArrayList<Double> cyclesLenth) {
        this.cyclesLenth = cyclesLenth;
    }
    
    
}
