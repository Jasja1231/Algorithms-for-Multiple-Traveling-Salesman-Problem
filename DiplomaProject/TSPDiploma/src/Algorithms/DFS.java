/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import java.util.Stack;


public class DFS {

    Stack<Integer> st;
      int vFirst;

      int[][] adjMatrix;
      int[] isVisited;
      int [] results;
      int index = 0;

    public int [] getResults()
    {
        return results;
    }
    public DFS(int[][] Matrix) {

         this.adjMatrix = Matrix;
         results = new int[Matrix.length-1];
         for (int i = 0; i < results.length;i++)
             results[i] = -1;
         isVisited = new int[Matrix.length];
         st = new Stack<Integer>();
         int i;
         int firstNode = 0;
         depthFirst(firstNode, Matrix.length);
         
          }

          private void depthFirst(int vFirst,int n)
          {
          int v,i;

          st.push(vFirst);

          while(!st.isEmpty())
          {
              v = st.pop();
              if(isVisited[v]==0)
              {
                  results[index++] = v;
                  isVisited[v]=1;
              }
              for ( i=0;i<n;i++)
              {
                  if((adjMatrix[v][i] == 1) && (isVisited[i] == 0))
                  {
                      st.push(v);
                      isVisited[i]=1;
                      results[index++] = i;;
                      v = i;
                  }
              }
          }
}}