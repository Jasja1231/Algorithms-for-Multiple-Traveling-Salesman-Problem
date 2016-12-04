/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.ArrayList;

/**
 *
 * @author Krzysztof
 */
public class SolutionOperations {
    
    public static ArrayList<ArrayList<Integer>> getCyclesFromSolution(int numSalesmen, int[]solution)
    {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>(numSalesmen);
        for (int x =0 ; x < numSalesmen ; x++)
            result.add(new ArrayList<>());
        
        for(int i=0,j=0;i<solution.length;i++)
        {
            int vertex = solution[i];
            if (isBase (vertex, numSalesmen))
            {
                if (i!=0 && i!= solution.length-1)
                {
                    j++;
                }
                if (j!=0 && result.get(j-1).get(result.get(j-1).size()-1)!=0)
                     result.get(j-1).add(0);
                
                result.get(j).add(0);
            }
            else
            {
                result.get(j).add(vertex-(numSalesmen-1));
                if (i==solution.length - 1)
                    result.get(j).add(0);
            }
        }
        return result;
    }
    
    private static boolean isBase(int vertex, int numSalesmen)
    {
        return (!(vertex>numSalesmen-1));
    }
    
}
