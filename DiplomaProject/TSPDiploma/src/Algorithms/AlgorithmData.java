/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 *
 * @author Krzysztof
 */
public final class AlgorithmData {
    private List<Tuple<Float,Float>> coords; //lon,lat
    private int numSalesmen;
    
    public List<Tuple<Float,Float>> getCoords () {return coords;}
    
    public List<Coordinate> getCoordinatesAsList(){
        ArrayList<Coordinate> coolist = new ArrayList<>();
       for(Tuple tt : coords){
           Coordinate coo = new Coordinate((float)tt.getX(),(float) tt.getY());
           coolist.add(coo);
       }
        return coolist;
    }
    
     public List<Tuple<Float,Float>> getAsTuples(List<Coordinate> cords){
         List<Tuple<Float,Float>> list = new ArrayList<Tuple<Float,Float>>();
       for(Coordinate tt : cords){
           Tuple<Float,Float> coo = new Tuple((float)tt.getLat(),(float) tt.getLon());
           list.add(coo);
       }
       return list;
    }
     
    public int getNumSalesmen (){
        return numSalesmen;
    }
    
    public AlgorithmData( List<Tuple<Float,Float>> coords, int numSalesmen)
    {
        this.numSalesmen = numSalesmen;
        this.coords = coords;    
    }
    
    /**
     *
     * @param coords
     * @param salesmanCount
     */
    public AlgorithmData(int salesmanCount,ArrayList<Coordinate> coords)
    {
        this.numSalesmen = salesmanCount;
        this.coords = getAsTuples(coords);    
    }
    
    public static class Tuple<X, Y> 
    { 
        public final X x; 
        public final Y y; 

        public X getX() {
            return x;
        }

        public Y getY() {
            return y;
        }
        public Tuple(X x, Y y) 
        { 
          this.x = x; 
          this.y = y; 
        } 
    } 
    
}
