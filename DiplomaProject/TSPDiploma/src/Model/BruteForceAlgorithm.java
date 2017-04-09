/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.Permutations;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Krzysztof
 */
public class BruteForceAlgorithm  implements Algorithm {

    @Override
    public String getName() {
      return "BruteForceAlgorithm";
    }
    
    @Override
    public int[] solveProblem(float[][] adjacencyMatrix, Object... o) {
        //read optional parameter
         int salesmenCount = (int)o[0];
         int numOfDestinations = adjacencyMatrix.length-1;
         
         //best path
         int [] bestResult = null;
         double bestLength = Double.MAX_VALUE;
         //Generate all partitions of the number of points with the number of elements equal to the number of salesmen. 
        ArrayList<ArrayList<Integer>> partitions = generatePartitions(adjacencyMatrix.length-1, salesmenCount, adjacencyMatrix.length-1);
        Integer[] indexes = new Integer[numOfDestinations];
        for(int i=0; i < numOfDestinations ;i++)
            indexes[i] = i+1;
        //Calculate all possible permutations of destination points
        Permutations<Integer> perm = new Permutations<>(indexes);
        
        //For every permutation  of destination points 
        while(perm.next()!=null)
        {
            Integer [] permutation = perm.currentPerm;
             //For every points division (partition) between salesmen 
            for (int i=0;i<partitions.size();i++)
            {
                //ignore partitions into less than (salesmen count) parts
                ArrayList<Integer> currentPartition = partitions.get(i);
                if ((!currentPartition.contains(0))&&currentPartition.size()==salesmenCount)
                {
                    ArrayList<Integer>currentPath = new ArrayList<>();
                    for (int v=0;v<permutation.length;v++)
                        currentPath.add(permutation[v]);
                                
                    int sum =0;
                    
                    //For every points division (partition) between salesmen 
                    for (int x=0;x<currentPartition.size();x++)
                    {
                        //separates paths with 0 point
                       currentPath.add(sum+currentPartition.get(x),0);
                       sum +=currentPartition.get(x)+1;
                    }
                    
                    double length = calcLength(currentPath,adjacencyMatrix);
                    if (length < bestLength)
                    {
                        bestLength = length;
                        bestResult = BruteForceAlgorithm.convertIntegers(currentPath);
                    }
                }
            }
        }
        
        return bestResult;
    }
    
    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    /**
     * TODO: do not generate where 0 is detected 
     * @param total number of coordinates to visit without bases. 
     * @param stacks
     * @param max
     * @return
     */
    public static  ArrayList<ArrayList<Integer>> generatePartitions(int total, int stacks, int max)
    {
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

    private double calcLength(ArrayList<Integer> currentPath, float[][] adjacencyMatrix) {
        double result =0.0;
        for (int i=0;i<currentPath.size()-1;i++)
        {
            result += adjacencyMatrix[currentPath.get(i)][currentPath.get(i+1)];
        }
        result += adjacencyMatrix[currentPath.get(currentPath.size()-1)][currentPath.get(0)];
        return result==0? Double.MAX_VALUE : result;
    }

    
}
