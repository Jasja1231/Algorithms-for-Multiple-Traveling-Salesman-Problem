/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.ApproximationAlgorithm;
import Algorithms.SolutionOperations;
import de.cm.osm2po.errors.Osm2poException;
import de.cm.osm2po.logging.Log;
import de.cm.osm2po.routing.Graph;
import de.cm.osm2po.routing.PoiRouter;
import de.cm.osm2po.tsp.TspDefaultMatrix;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.gui.jmapviewer.Coordinate;


/**
 *
 * @author K
 */
public class Model extends Observable {
    
    //Algorithms
    //TODO: change that caz these classes are static with one static method
    final HeuristicAlgorithm heuristicAlgorithm = new HeuristicAlgorithm();
    final SCIPAlgorithm SCIPAlgorithm = new SCIPAlgorithm();
    final BruteForceAlgorithm bruteForceAlgorithm = new BruteForceAlgorithm();
    final ApproximationAlgorithm approximationAlgorithm = new ApproximationAlgorithm();
    final DynamicAlgorithm dynamicAlgorithm = new DynamicAlgorithm();
    
    Graph graph;
    PoiRouter  router;
    Properties params;
    TspDefaultMatrix timeMatrix;
    int [][][] shortestPaths;
    float[][] shortestPathCostMatrix;
    float[][] extendedShortestPathMatrix;
    float[][]extendedTimeMatrix;
    float[][]euclideanDistanceMatrix;
    
    
    /***
     * Table with all possible algorithms
     */
    List<Algorithm> allAlgorithms;
    
    /***
     * List of coordinates algorithms will calculate for.
     */
    List<Coordinate> coordinates;
    
    int salesmanCount = 0 ;
    
    /***
     * List of involved algorithms
     */
    List<Algorithm> algorithms;
    private int selectedMetric;
    private float[][] extendedEuclideanMatrix;
    

   public Model(){
    init();
     

    // additional params for DefaultRouter where do we set it?
    Properties params = new Properties();
    params.setProperty("findShortestPath", "true");
    params.setProperty("ignoreRestrictions", "false");
    params.setProperty("ignoreOneWays", "false");
    params.setProperty("heuristicFactor", "1.0"); // 0.0 Dijkstra, 1.0 good A*
    params.setProperty("matrix.fullSearchLoops", "5");
   }
   
   //TODO: think about minimize values to be initialized
   public void init(){
       coordinates = new ArrayList<>();
       algorithms = new ArrayList<Algorithm>();
       
       allAlgorithms = new ArrayList<Algorithm>();
       //KEEP THE ORDER
       allAlgorithms.add(SCIPAlgorithm);
       allAlgorithms.add(dynamicAlgorithm);
       allAlgorithms.add(bruteForceAlgorithm);
       allAlgorithms.add(heuristicAlgorithm);
       allAlgorithms.add(approximationAlgorithm);
       
       //Routing stuff initialization
       File graphFile = new File("warsaw.gph");  //TODO: make it static or whatever
       graph = new Graph(graphFile);
       router = new PoiRouter();
       timeMatrix = new TspDefaultMatrix();
   }

   public void addCoordinate(Coordinate coo){
       this.coordinates.add(coo);
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
    
   public void buildEuclideanMatrix(ArrayList<Coordinate>coords)
   {
       int [] vertexIDs = new int [coords.size()];
       this.euclideanDistanceMatrix = new float[coords.size()][coords.size()];
       for (int i=0;i<coords.size();i++)
       {
           for (int j=0;j<coords.size();j++)
           {
               double x1 = coords.get(i).getLat();
               double x2 = coords.get(j).getLat();
               double y1 = coords.get(i).getLon();;
               double y2 = coords.get(j).getLon();
               
               double distance = Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
               
               euclideanDistanceMatrix[j][i] =(float) distance;
               euclideanDistanceMatrix[i][j] =(float) distance;
           }
       }
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

    public void addAlgorithm(int i) {
        this.algorithms.add(this.allAlgorithms.get(i));
    }

    public void clearAlgorithms() {
       this.algorithms.clear();
    }

    public void startComputation() {
        try {
            this.buildTimeMatrix((ArrayList)this.coordinates);
            this.buildShortestPaths((ArrayList)coordinates);
            this.buildEuclideanMatrix((ArrayList)coordinates);
        } catch (Osm2poException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(salesmanCount>1)
        {
            extendedTimeMatrix = getExtendedMatrixForMultipleSalesmen(salesmanCount, timeMatrix.getCosts());
            extendedShortestPathMatrix = getExtendedMatrixForMultipleSalesmen(salesmanCount, shortestPathCostMatrix);
            extendedEuclideanMatrix = getExtendedMatrixForMultipleSalesmen(salesmanCount, euclideanDistanceMatrix);
        }
        else if(salesmanCount==1){
            extendedTimeMatrix = timeMatrix.getCosts();
            extendedShortestPathMatrix = shortestPathCostMatrix;
            extendedEuclideanMatrix = euclideanDistanceMatrix;
        }
        
         int [] result;
         float[][] table1 = null;
         float[][] table2 = null;
        if (selectedMetric == 0) //euclidean
        {//TODO: rewrite it , rewrite separating by 0  
            table1 = extendedEuclideanMatrix;
            table2 = euclideanDistanceMatrix; 
        }
        else if (selectedMetric ==1) //actual distance
        {
            table1 = extendedShortestPathMatrix;
            table2 = shortestPathCostMatrix;
        }
        else if (selectedMetric == 2) //time
        {
            table1 = extendedTimeMatrix;
            table2 = this.timeMatrix.getCosts();
        }
        
        //Alg
         for(Algorithm a : this.algorithms){
                ArrayList<ArrayList<Integer>> cycles;
               if(!(a instanceof BruteForceAlgorithm || a instanceof ApproximationAlgorithm)){
                    result = a.solveProblem(table1,this.salesmanCount);
                    cycles = SolutionOperations.getCyclesFromSolution(salesmanCount, result,true);
               }
               else {
                    result = a.solveProblem(table2,this.salesmanCount);
                    cycles = new  ArrayList<ArrayList<Integer>>();
                    ArrayList<Integer> temp =  new ArrayList<>();
                    for(int k : result)
                        temp.add(k);
                    
                    cycles.add(temp);
                    cycles = SolutionOperations.getCyclesFromSolution(salesmanCount, result,false);
               }

                this.setChanged();
                this.notifyObservers(cycles);
            }

    }

    public List<Coordinate> getCoordinates(){
        return this.coordinates;
    }
    
    public void setSalesmenCount(int salesmanCount) {
       this.salesmanCount = salesmanCount;
    }

    public Graph getGraph() {
       return this.graph;
    }
    
      public int [][][] getShortestPaths(){
        return this.shortestPaths;
    }
      
    public int[][][] getTimeMatrix(){
         return this.timeMatrix.getPaths();
    }

     /* 0 eucledian
           1 distance 
           2 time
        */
    public void setSelectedMetric(int selectedMetric) {
        this.selectedMetric = selectedMetric;
    }

    public int getSelectedMetric() {
        return this.selectedMetric;
    }

    public void resetData() {
       this.coordinates.clear();
       this.algorithms.clear();
    }
    
}

