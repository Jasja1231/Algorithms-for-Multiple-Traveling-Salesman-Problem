/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 *
 * @author Krzysztof
 */
public class BruteForceAlgorithm  implements Algorithm {

    @Override
    public int[] solveProblem(float[][] adjacencyMatrix, Object... o) {
        //read optional parameter
         int salesmenCount = (int)o[0];
         int [] bestResult = null;
         
        ArrayList<ArrayList<Integer>> partitions = generatePartitions(adjacencyMatrix.length-1, salesmenCount, salesmenCount);
        
        
        return bestResult;
    }
    

    /**
     * TODO: do not generate where 0 is detected 
     * @param total number of coordinates to visit without bases. 
     * @param stacks
     * @param max
     * @return
     */
    public static  ArrayList<ArrayList<Integer>> generatePartitions(int total, int stacks, int max){
            ArrayList<ArrayList<Integer>> partitions = new ArrayList<>();

            if (total <= 1 || stacks == 1)
            {
                if (total <= max)
                {
                    partitions.add(new ArrayList<Integer>());
                    partitions.get(0).add(total);
                }

                return partitions;
            }
            for (int y = Math.min(total, max); y >= 1; y--)
            {
                ArrayList<ArrayList<Integer>> w = generatePartitions(total - y, stacks - 1, y);
                for (int i = 0; i < w.size(); i++)
                {
                    w.get(i).add(y);
                    partitions.add(w.get(i));
                }
            }
            return partitions;
        }   
    
}
