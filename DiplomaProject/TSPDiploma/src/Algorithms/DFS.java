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
public class DFS {
   
public static void DFS(int[][] adjMatrix, boolean [] visited, int [] results, int n, int i, int idx){
        results[idx++] = i;
        visited[i]= true;
        for (int j = 0; j<n;j++){
            if(!(visited[j]) && adjMatrix[i][j]==1){
                DFS(adjMatrix, visited, results, n, j, idx);
            }
        }
    }
public static void DFS(int[][] adjMatrix, boolean [] visited, int [] results, int n, int i, ArrayList<Integer>dupa){
        System.err.println("Visiting vertex  : " + i);
        dupa.add(i);
        visited[i]= true;
        for (int j = 0; j<n;j++){
            if(!(visited[j]) && adjMatrix[i][j]==1){
                System.err.println("Visiting vertex  : " + i +  " Found adjacent : " + j);
                DFS(adjMatrix, visited, results, n, j, dupa);
            }
        }
    }
}
