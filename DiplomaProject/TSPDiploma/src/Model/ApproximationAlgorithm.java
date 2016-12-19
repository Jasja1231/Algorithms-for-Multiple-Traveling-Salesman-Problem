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
      return "Approximation algorithm";
    }
    
    private class EdgeCostPair
    {
        private int index;
        private float cost;
        public int getIndex () {return index;}
        public float getCost(){return cost;}
        public EdgeCostPair(int index, float cost)
        {
            this.index = index;
            this.cost = cost;
        }
        public void setCost(float cost){this.cost = cost;}
        public void setIndex(int index){this.index = index;}
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
        float [][] reducedMatrix = new float[adjacencyMatrix.length-1][adjacencyMatrix.length-1];
        for (int i=1;i<adjacencyMatrix.length;i++)
        {
            for (int j=1;j<adjacencyMatrix.length;j++)
            {
                reducedMatrix[i-1][j-1] = adjacencyMatrix[i][j];
            }
        }
        int numSalesmen = (int)o[0]; //0 if not changed  FIX IT
        float bestBaseReturnCosts [] = new float [numSalesmen+1];
        EdgeCostPair[]BestReturnEdges = new EdgeCostPair[numSalesmen+1];
        for (int i=0;i<BestReturnEdges.length;i++)
            BestReturnEdges[i] = new EdgeCostPair(-1,Float.MAX_VALUE);
        int bestBaseReturnPoints [] = new int[numSalesmen+1];
        Kruskal k = new Kruskal();
        k.readInGraphData(reducedMatrix);
        k.performKruskal();
        int[][] kruskalMatrix = k.getResultAdjacencyMatrix();
        int idx = 0;
        int n = kruskalMatrix.length;
        ArrayList<Integer> order = new ArrayList<>();
        boolean [] visited = new boolean [n];
        //visited[0] = true; //we don't want to visit the base
        DFS.DFS(kruskalMatrix, visited, n, 0, order);
        float[] backToBaseCostsForEdges = new float [n];
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
            cost = adjacencyMatrix[from+1][0] + adjacencyMatrix[0][to+1];
            AllReturnEdgesCosts[idx++] = new EdgeCostPair(from,cost);
        }
        for (int i=0;i<AllReturnEdgesCosts.length;i++)
        {
            BestReturnEdges[numSalesmen] =  AllReturnEdgesCosts[i];
            Arrays.sort(BestReturnEdges, new whyIsThis_a_Class());
        }
        
       solution = new int [n+numSalesmen];
    //   for (int i=0;i<solution.length;i++)
      //     solution[i] = -1;
       
      /* for (int i=0,placedPoints=0;i<BestReturnEdges.length-1;i++)
       {
           solution[order.indexOf(BestReturnEdges[i].getIndex())+placedPoints++] = 0;
       }
       for (int i=0,i2=0;i<solution.length;i++)
       {
           if(solution[i]!=0 && i!=solution.length-1)
           {
               solution[i] = (order.get(i2++))+1;
           }
       }
       */
       for (int i=0,i2=0;i<order.size();i++)
       {
          boolean contains = false;
          for (int j=0;j<BestReturnEdges.length-1;j++)
          {
           if (order.get(i)==BestReturnEdges[j].getIndex())
               contains = true;
          }
          if(contains)
              i2++;
          
          solution[i2++] = order.get(i)+1;
       }
       //solution[solution.length-1] = solution[0];
       return solution;
    }
}