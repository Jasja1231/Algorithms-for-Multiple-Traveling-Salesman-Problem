/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.DFS;
import Algorithms.Kruskal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Krzysztof
 */
public class ApproximationAlgorithm implements Algorithm {

    @Override
    public String getName() {
      return "ApproximationAlgorithm";
    }
    
    private class EdgeCostPair
    {
        private int fromIndex;
        private int toIndex;
        private float cost;
        public int getfomIndex () {return fromIndex;}
        public int gettoIndex () {return toIndex;}
        public float getCost(){return cost;}
        public EdgeCostPair(int from, int to, float cost)
        {
            this.fromIndex = from;
            this.toIndex = to;
            this.cost = cost;
        }
        public void setCost(float cost){this.cost = cost;}
        public void setfromIndex(int index){this.fromIndex = index;}
        public void settoIndex(int index){this.toIndex = index;}
    }
    
    class whyIsThis_a_Class implements Comparator<EdgeCostPair>
    {
        public int compare(EdgeCostPair o1, EdgeCostPair o2)
        {
            float res = o1.cost - o2.cost;
            if (res > 0)
                return 1;
            else if (res == 0)
                return 0;
            else
                return -1;
        }
    }
    
    public int [] solveProblem (float [][]adjacencyMatrix, Object... o) //adjacencyMatrix 
     //has to be without without additional bases, integer number of salesmen as first optional parameter
    {
        int [] solution;
        int numSalesmen = (int)o[0]; //0 if not changed  FIX IT
       // float bestBaseReturnCosts [] = new float [numSalesmen+1];
        EdgeCostPair[]BestReturnEdges = new EdgeCostPair[numSalesmen];
        for (int i=0;i<BestReturnEdges.length;i++)
            BestReturnEdges[i] = new EdgeCostPair(-1,-1,Float.MAX_VALUE);
        //int bestBaseReturnPoints [] = new int[numSalesmen+1];
        Kruskal k = new Kruskal();
        k.ReadInGraphDataIgnoreBase(adjacencyMatrix);
        k.performKruskal();
        int[][] kruskalMatrix = k.getResultAdjacencyMatrix();
        int idx = 0;
        int n = kruskalMatrix.length;
        ArrayList<Integer> order = new ArrayList<>();
        boolean [] visited = new boolean [n];
        //order.add(0);
        DFS.DFS(kruskalMatrix, visited, n, 0, order);
        //float[] backToBaseCostsForEdges = new float [n];
        EdgeCostPair[]AllReturnEdgesCosts = new EdgeCostPair[n];
        idx = 0;
        for (int i=0;i<order.size();i++)
        {
            int from = 0, to = 0;
            float cost;
            if (i<order.size()-1)
            {
                from = order.get(i);
                to = order.get(i+1);
            }
            else if (i==order.size()-1)
            {
                from = order.get(i);
                to = order.get(0);
            }
            cost = (adjacencyMatrix[from+1][0] + adjacencyMatrix[0][to+1]) - adjacencyMatrix[from+1][to+1];
            AllReturnEdgesCosts[idx++] = new EdgeCostPair(from,to,cost);
        }
       // order.remove(order.size()-1);
        Arrays.sort(AllReturnEdgesCosts, new whyIsThis_a_Class());

        
       solution = new int [n+numSalesmen];
       
       for (int i=0,i2=0;i<order.size();i++)
       {
           boolean isReturnEdge = false;
           int from,to;
           if (i<order.size()-1)
            {
                from  = order.get(i);
                to = order.get(i+1);
            }
            else
            {
                from  = order.get(i);
                to = order.get(0);
            }
           for (int j=0;j<numSalesmen;j++)
           {
               EdgeCostPair current = AllReturnEdgesCosts[j];
               if (from == current.getfomIndex() && to == current.gettoIndex())
                   isReturnEdge = true;
           }  
           solution[i2++] = order.get(i)+1;
           if (isReturnEdge)
              i2++;
       }
/*
       for (int i=0,i2=0;i<order.size();i++)
       {
          boolean contains = false;
          for (int j=0;j<numSalesmen;j++)
          {
           if (order.get(i)==AllReturnEdgesCosts[j].getIndex())
               contains = true;
          }
          if(contains)
              i2++;
          
          solution[i2++] = order.get(i)+1;
       }*/
       return solution;
    }
}