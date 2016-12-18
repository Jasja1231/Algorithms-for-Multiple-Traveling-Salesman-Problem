/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.List;

/**
 *
 * @author Krzysztof
 */
public class AlgorithmData {
    private List<Tuple<Float,Float>> coords; //lon,lat
    private int numSalesmen;
    
    public List<Tuple<Float,Float>> getCoords () {return coords;}
    public int getNumSalesmen (){return numSalesmen;}
    
    public AlgorithmData( List<Tuple<Float,Float>> coords, int numSalesmen)
    {
        this.numSalesmen = numSalesmen;
        this.coords = coords;    
    }
    
    public static class Tuple<X, Y> 
    { 
        public final X x; 
        public final Y y; 
        public Tuple(X x, Y y) 
        { 
          this.x = x; 
          this.y = y; 
        } 
    } 
    
}
