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
    
   public static ArrayList<ArrayList<Integer>> getCyclesFromSolution(int numSalesmen, int[]solution, boolean multipleBases)
    {
         /*ArrayList<ArrayList<Integer>> result = new ArrayList<>(numSalesmen);
        for (int x =0 ; x < numSalesmen ; x++)
            result.add(new ArrayList<Integer>());
        
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
    */
     ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
   /* for (int i=0;i<numSalesmen;i++)
     {
         result.add(new ArrayList<Integer>());
         result.get(i).add(0);
         result.get(i).add(0);
    */
     if (multipleBases)
         replaceBasesAndUpdate(solution, numSalesmen);
     
     boolean cycleOverlap = hasCycleOverlap(solution);
     
     for (int i=0;i<numSalesmen;i++)
         result.add(new ArrayList<Integer>());
     
     int lastZero  = 0;
     int firstZero = 0;
     
     if (cycleOverlap)
     {
         firstZero = findFirstZero (solution);
         lastZero = findLastZero(solution);
         for (int i=lastZero+1; i<solution.length;i++)
             result.get(0).add(solution[i]);
         for (int i=0;i<firstZero;i++)
             result.get(0).add(solution[i]); 
         for (int i=firstZero+1,l=1;i<lastZero;i++)
         {
             int val = solution[i];
             if (val == 0)
                 l++;
             else
                 result.get(l).add(solution[i]);
         }
     }
     else
     {
        for (int i=0,l=0;i<solution.length;i++)
        {
           int val = solution[i];
             if (val == 0)
                 l++;
             else
                 result.get(l).add(solution[i]);
        }
     }
     
              for (int i=0;i<result.size();i++)
     {
         result.get(i).add(0);
         result.get(i).add(0,0);
     }
     
     
    /*
     for (int i=0,k=0,l=1;i<solution.length;i++)
     {
         int val = solution [i];
         if (!multipleBases)
         {
            if (val == 0 && i!= solution.length-1)
            {
                k++;
                l=1;
            }
             result.get(k).add(l++,solution[i]);
         }
         else
         {
          if (val < numSalesmen && i!= solution.length-1)
            {
                k++;
                l=1;
            }
          result.get(k).add(l++,solution[i]+numSalesmen-1); */   
     
     return result;
    }
    
   private static int findLastZero (int [] solution)
   {
       for (int i =solution.length-1;i>=0;i--)
       {
           if (solution[i] == 0)
               return i;
       }         
       return -1;
   }
   
       
   private static int findFirstZero (int [] solution)
   {
       for (int i = 0;i<solution.length;i++)
       {
           if (solution[i] == 0)
               return i;
       }         
       return -1;
   }
   private static boolean hasCycleOverlap(int []solution)
   {
       return (!(solution[0]==0 && solution[solution.length-1]==0));
   }
   private static void replaceBasesAndUpdate (int []solution, int numSalesmen)
   {
       for (int i=0;i<solution.length;i++)
       {
           if (solution[i] < numSalesmen)
               solution[i] = 0;
           else
               solution[i] += numSalesmen-1;
       }
   }
    private static boolean isBase(int vertex, int numSalesmen)
    {
        return (!(vertex>numSalesmen-1));
    }

}
