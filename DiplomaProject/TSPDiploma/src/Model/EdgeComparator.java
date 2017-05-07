/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Comparator;

/**
 *
 * @author Krzysztof
 */
   class EdgeComparator implements Comparator<EdgeCostPair>
    {
        @Override
        public int compare(EdgeCostPair o1, EdgeCostPair o2)
        {
            float res = o1.getCost() - o2.getCost();
            if (res > 0)
                return 1;
            else if (res == 0)
                return 0;
            else
                return -1;
        }
    }
    
