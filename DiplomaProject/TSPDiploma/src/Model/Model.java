/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.DFS;
import Algorithms.DFS;
import Algorithms.Kruskal;
import Algorithms.SolutionOperations;
import de.cm.osm2po.errors.Osm2poException;
import de.cm.osm2po.logging.Log;
import de.cm.osm2po.routing.Graph;
import de.cm.osm2po.routing.PoiRouter;
import de.cm.osm2po.tsp.TspDefaultMatrix;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Properties;
import org.openstreetmap.gui.jmapviewer.Coordinate;


/**
 *
 * @author K
 */
public class Model extends Observable {
    HeuristicAlgorithm ha = new HeuristicAlgorithm();
    SCIPAlgorithm sA = new SCIPAlgorithm();
    BruteForceAlgorithm bA = new BruteForceAlgorithm();
    ApproximationAlgorithm aA = new ApproximationAlgorithm();
    DynamicAlgorithm dynamicAlgorithm = new DynamicAlgorithm();
    Graph graph;
    PoiRouter  router;
    Properties params;
    TspDefaultMatrix timeMatrix;
    int [][][] shortestPaths;
    float[][] shortestPathCostMatrix;
    
    
   public Model() throws Osm2poException
           
   {
   File graphFile = new File("warsaw.gph");
   graph = new Graph(graphFile);
   router = new PoiRouter();
   timeMatrix = new TspDefaultMatrix();

   // additional params for DefaultRouter
   Properties params = new Properties();
   params.setProperty("findShortestPath", "true");
   params.setProperty("ignoreRestrictions", "false");
   params.setProperty("ignoreOneWays", "false");
   params.setProperty("heuristicFactor", "1.0"); // 0.0 Dijkstra, 1.0 good A*
   params.setProperty("matrix.fullSearchLoops", "5");
   test();
   }
   
   public void buildTimeMatrix(ArrayList<Coordinate>coords) throws Osm2poException
   {
       int [] vertexIDs = new int [coords.size()];
       int i = 0;
       for (Coordinate c : coords)
       {
           int id = graph.findClosestVertexId((float)c.getLat(), (float)c.getLon());
           vertexIDs[i++] = id;
       }
       timeMatrix.build(graph, vertexIDs, Float.MAX_VALUE, Log.stdout(Log.LEVEL_LOG), params);
   }
    
    public void buildShortestPaths(ArrayList<Coordinate>coords)
    {
       int [] vertexIDs = new int [coords.size()];
       int i = 0;
       for (Coordinate c : coords)
       {
           int id = graph.findClosestVertexId((float)c.getLat(), (float)c.getLon());
           vertexIDs[i++] = id;
       }
        int n = vertexIDs.length;
        int[] cpVertexIds = Arrays.copyOf(vertexIDs, n);
        shortestPathCostMatrix = new float[n][n];
        shortestPaths = new int [n][n][]; 
        for (int y = 0; y < n; y++) 
        {
            int swap = cpVertexIds[0];
            cpVertexIds[0] = cpVertexIds[y];
            cpVertexIds[y] = swap;
            int sourceId = cpVertexIds[0];
            int[] targetIds = Arrays.copyOfRange(cpVertexIds, 1, n -1);
            router.reset();
            router.traverse(graph, sourceId, targetIds, Float.MAX_VALUE, params);
            
            for (int z = 0; z < n; z++) 
            {
                int x = z + y;
                if (x >= n) x -= n;

                shortestPathCostMatrix[y][x] =Float.MAX_VALUE;
                if (router.isVisited(vertexIDs[x]))
                {
                    int[] path=router.makePath(vertexIDs[x]);
                    shortestPaths [y][x] = path;
                    shortestPathCostMatrix[y][x] = (float)graph.calcPathLength(path);  
                }
            }
        }

    }
   
   public void test () throws Osm2poException
   {
          Coordinate coord = new Coordinate(52.216500, 20.988080);
          Coordinate coord2 = new Coordinate(52.131412, 21.065748);
          Coordinate coord3 = new Coordinate(52.178953, 21.005037);
          Coordinate coord4 = new Coordinate (52.202217, 20.954812);
          float[][]matrix2;
          ArrayList<Coordinate>coords = new ArrayList<Coordinate>();
          coords.add(coord);
          coords.add(coord2);
          coords.add(coord3);
          coords.add(coord4);

          int [] vertexIDs = new int [coords.size()];
          int i = 0;
       for (Coordinate c : coords)
       {
           int id = graph.findClosestVertexId((float)c.getLat(), (float)c.getLon());
           vertexIDs[i++] = id;
       }
       
       buildShortestPaths(coords);
       buildTimeMatrix(coords);
       float[][] mext = getExtendedMatrixForMultipleSalesmen (3, shortestPathCostMatrix);
       float[][] matrixxx = new float [4][4];
       for (int x=0;x<4;x++)
           for (int j=0;j<4;j++)
               matrixxx[x][j]=Float.MAX_VALUE;
       
       matrixxx[0][2] = 1f;
       matrixxx[2][1] = 1f;
       matrixxx[1][3] = 1f;
       Kruskal k = new Kruskal();
       k.readInGraphData(matrixxx);
       k.performKruskal();
       int[][]arr = k.getResultAdjacencyMatrix();
       int[] order = new int [arr.length];
       int idx = 0;
       DFS.DFS(arr, new boolean[arr.length], order, arr.length, 0, idx);
        ArrayList<ArrayList<Integer>> res = SolutionOperations.getCyclesFromSolution(4, new int []{0,4,5,6,1,7,8,9,2,10,11,3,12,13});
   }
   
   float [][] getExtendedMatrixForMultipleSalesmen (int salesmen, float[][]matrix)
   {
       float[][] extended = new float [matrix.length+salesmen-1][matrix.length+salesmen-1];
       for (int extendedY = 0, y = extendedY - salesmen + 1; extendedY < extended.length; extendedY++,y++)
       {
           for (int extendedX=0, x = extendedX - salesmen + 1; extendedX< extended[extendedY].length;extendedX++, x++)
           {
               if (x <= 0 && y <= 0)
               {
                   extended [extendedY][extendedX] = Float.MAX_VALUE;
               }
               else if (x <= 0)
               {
                   extended [extendedY][extendedX] = matrix [y][0];
               }
               else if (y <= 0)
               {
                   extended [extendedY][extendedX] = matrix [0][x];
               }
               else
               {
                  extended [extendedY][extendedX] = matrix [y][x];
               }
           }
       }
       return extended;
   }
}

