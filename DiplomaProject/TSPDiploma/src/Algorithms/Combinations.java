/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to get a permutation of a certain size of elements.
 * Order does not matter.
 * Without repetitions.
 * @author Yaryna
 */
public class Combinations {
   
    /**
     *
     * @param inputArrray array of the elements to perform combinations on.
     * @param k length of the combination
     * @return
     */
    @SuppressWarnings("empty-statement")
    public static List<int[]> getCombinations(int[] inputArrray,int k){
        int[] s = new int[k];
        List<int[]> subsets = new ArrayList<>();
        if (k <= inputArrray.length) {
    // first index sequence: 0, 1, 2, ...
        for (int i = 0; (s[i] = i) < k - 1; i++);  
            subsets.add(getSubset(inputArrray,s));
            for(;;) {
                int i;
                // find position of item that can be incremented
                for (i = k - 1; i >= 0 && s[i] == inputArrray.length - k + i; i--); 
                if (i < 0) {
                    break;
                } else {
                    s[i]++;                    // increment this item
                    for (++i; i < k; i++) {    // fill up remaining items
                        s[i] = s[i - 1] + 1; 
                    }
                    subsets.add(getSubset(inputArrray, s));
                }
            }
        }
        return subsets;
    
    }
    
    // generate actual subset by index sequence
    private static int[] getSubset(int[] input, int[] subset) {
        int[] result = new int[subset.length];
        for (int i = 0; i < subset.length; i++) 
            result[i] =  input[subset[i]];
        return result;
    }
    
    /***
     * Returns the combination of a simple integer array consisting of 
     * sequence of integers from @lowerNum(included) to @param topNum(included)
     * @param lowerNum smallest number in the array
     * @param topNum largest number in the array  
     * @param k length of the combination
     * @return 
     */
     public static List<int[]> getCombinations(int lowerNum, int topNum ,int k){
        int[] seq;
        seq = generateSequenceArray( lowerNum,  topNum);
        return getCombinations(seq,k);
    }

    private static int [] generateSequenceArray(int lowerNum, int topNum) {
       int[] res = new int[topNum-lowerNum+1];
       int curCount = 0;
       for(int i = lowerNum; i <= topNum; i++){
          res[curCount] = i;
          curCount++;
       }
       return res;
    }
}
