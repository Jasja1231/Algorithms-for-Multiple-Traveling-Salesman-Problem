/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.AlgorithmData;
import Algorithms.Haversine;
import Algorithms.Parser;
import Algorithms.SolutionOperations;
import View.MapPanel;
import de.cm.osm2po.errors.Osm2poException;
import de.cm.osm2po.logging.Log;
import de.cm.osm2po.routing.Graph;
import de.cm.osm2po.routing.PoiRouter;
import de.cm.osm2po.tsp.TspDefaultMatrix;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.openstreetmap.gui.jmapviewer.Coordinate;


/**
 *
 * @author Yaryna
 */
public class Model extends Observable {
    
    //Algorithms
    final static String graphFilePath = ".\\warsaw3.gph";
    
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
     * List of coordinates algorithms will calculate for.
     */
    List<Coordinate> coordinates;
    
    /***
     * Number of salesman
     */
    int salesmanCount = 0 ;
    
    /***
     * List of involved algorithms
     */
    List<Algorithm> algorithms;
    
    File[] inputFiles = null;
    
    private int selectedMetric;
    private float[][] extendedEuclideanMatrix;
    
    /***
     * Boolean value representing whether user loaded 
     * a single file or a directory with files.
     * In the beginning treats start computation as on the single file. 
     * When no file is loaded but provided manually - also treated as single file.
     */
    private boolean loadedSingleFile = true;
    public ArrayList<Integer> vertexIDS;
    
    public void setLoadedSingleFile(boolean b){
        this.loadedSingleFile = b;
    }

   public Model(){
    init();
    // additional params for DefaultRouter where do we set it?
    Properties parameters = new Properties();
    parameters.setProperty("findShortestPath", "true");
    parameters.setProperty("ignoreRestrictions", "false");
    parameters.setProperty("ignoreOneWays", "false");
    parameters.setProperty("heuristicFactor", "0.0"); // 0.0 Dijkstra, 1.0 good A*
    parameters.setProperty("matrix.fullSearchLoops", "10");
   }
   
   //TODO: think about minimize values to be initialized
   public void init(){
       coordinates = new ArrayList<>();
       algorithms = new ArrayList<>();
       vertexIDS = new ArrayList<>();
       //Routing  initialization
       File graphFile = new File(graphFilePath);  //TODO: make it static or whatever
       graph = new Graph(graphFile);
       router = new PoiRouter();
       timeMatrix = new TspDefaultMatrix();
   }

   public void addCoordinate(Coordinate coo){
       if(loadedSingleFile==false){
           this.setLoadedSingleFile(true);
           this.setChanged();
           //notify uset that now he switched to provide input manually
           this.notifyObservers(4);
       }
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
      timeMatrix.build(graph, vertexIDs, Float.MAX_VALUE, Log.stderr(Log.LEVEL_LOG), params);
       
   }
    
   public void buildEuclideanMatrix(ArrayList<Coordinate>coords)
   {
       this.euclideanDistanceMatrix = new float[coords.size()][coords.size()];
       for (int i=0;i<coords.size();i++)
       {
           for (int j=0;j<coords.size();j++)
           {
               double lat1 = coords.get(i).getLat();
               double lat2 = coords.get(j).getLat();
               double lon1 = coords.get(i).getLon();;
               double lon2 = coords.get(j).getLon();
               
               //double distance = Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
               double distance = Haversine.haversine(lat1, lon1, lat2, lon2);
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

   /***
    * 
    * @param i index of the algorithms where : 
    * 0 - SCIP 
    * 1 - dynamic 
    * 2 - bruteForce
    * 3 - heuristic
    * 4 - approximating 
    */
    public void addAlgorithm(int i) {
        Algorithm newInvolvedAlgorithm = null;
        switch(i){
            case 0 :newInvolvedAlgorithm = new SCIPAlgorithm();
                break;
            case 1 :newInvolvedAlgorithm = new DynamicAlgorithm();
                break;
            case 2 :newInvolvedAlgorithm = new BruteForceAlgorithm();
                break;
            case 3 :newInvolvedAlgorithm = new HeuristicAlgorithmA(); 
                break;
            case 4 :newInvolvedAlgorithm = new HeuristicAlgorithmB(); 
                break;
            case 5 :newInvolvedAlgorithm = new ApproximationAlgorithm();
                break;         
        }
        this.algorithms.add(newInvolvedAlgorithm);
    }

    public void clearAlgorithms() {
       this.algorithms.clear();
    }
    
    private List<Integer> getUnreachableVertices (float[][] ... matrices)
    {
        ArrayList<Integer> vertices = new ArrayList<>();
        for (float[][] m : matrices)
        {
            for (int idx = 0 ; idx < m.length ; idx++)
            {
                int cnt = 0;
                
                for (int i=0;i<m.length;i++)
                {
                   if (m[i][idx]==Float.MAX_VALUE && idx!=i)
                       cnt++;
                   if (m[idx][i]==Float.MAX_VALUE && idx!=i)
                       cnt++;
                }
                if (cnt >= 2 * m.length-2)
                    vertices.add(idx);
            }   
        }
        return vertices;
    }

    /***
     * The first function that gets called when computation started.
     * Reads whether computation started for a file or for the directory. 
     */
    public void startComputation() {
        if(this.loadedSingleFile==true){
            ArrayList<AlgorithmSolution> singleFileSoluition = startComputationForSingleFile(null);
            if(null!=this.inputFiles[0])
                Parser.writeReportForSingleProblem(singleFileSoluition,this.inputFiles[0].getAbsolutePath()+"_report.txt");
            else{
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
                Date date = new Date();
                String directoryName =  ".."+"\\"+ "RandomSetTSP"+ dateFormat.format(date);
                Parser.writeReportForSingleProblem(singleFileSoluition,directoryName+"_report.txt");
            }
            if(singleFileSoluition == null){
                this.setChanged();
                this.notifyObservers(2);
            }
        }
        else{
            ArrayList<AlgorithmSolution> solutions = new ArrayList<>();
            File dirFile  = this.inputFiles[0].getParentFile();
            for(File f : this.inputFiles){
                loadInputFile(f);
                ArrayList<AlgorithmSolution> singleFileSoluition = startComputationForSingleFile(solutions,f.getAbsolutePath());
                 if(singleFileSoluition!=null){
                     Parser.writeReportForSingleProblem(singleFileSoluition,dirFile.getAbsolutePath()+"\\"+ f.getName() +"_report.txt");
                 }
                 else{
                     //Osm2poException
                     Parser.writeFile(dirFile.getAbsolutePath()+"\\"+ f.getName() +"NOSOLUTION.txt", "File is invalid or may containg equal vertex id");
                 } 
            }
            Parser.writeReportFileForMultipleAlgorithmsHorizontal(solutions,dirFile.getAbsolutePath()+"\\"+"Solution_report.txt");
        }
        //Clears involved algorithm - release the memory 
        this.algorithms.clear();
    }
    
    /***
     * Computation of a single file and building all need elements of a problem instance.
     * @param solutionList in case of loaded directory this list is there in 
     *                     order to save the solutions into it and later will be user to generate a report file.
     * @param filenameToSaveAS optional parameter 
     */
   private ArrayList<AlgorithmSolution> startComputationForSingleFile(List<AlgorithmSolution>  solutionList,String ... filenameToSaveAS) {
        float[][] timeMatrixWithoutUnreachableVertices = null; 
        ArrayList<AlgorithmSolution> algorithmSolution = new ArrayList<>();
        try {
            this.buildTimeMatrix((ArrayList)this.coordinates); 
            this.buildShortestPaths((ArrayList)this.coordinates);
            this.buildEuclideanMatrix((ArrayList)this.coordinates);
            timeMatrixWithoutUnreachableVertices = this.timeMatrix.getCosts(); 
            List<Integer> unreachableVerticesDistance = getUnreachableVertices(this.shortestPathCostMatrix);
            List<Integer> unreachableVerticesTime = getUnreachableVertices(this.timeMatrix.getCosts()); 
            
            if (unreachableVerticesDistance.size()>0)//-UNCOMMENT
            {
                for (int unreachable : unreachableVerticesDistance)
                {
                    for (int i = 0 ; i < this.shortestPathCostMatrix.length; i ++)
                    {
                        shortestPathCostMatrix[unreachable][i] = euclideanDistanceMatrix [unreachable][i];
                        shortestPathCostMatrix[i][unreachable] = euclideanDistanceMatrix [i][unreachable];
                    }
                }
            }
            if (unreachableVerticesTime.size()>0)
            {
               for (int unreachable : unreachableVerticesDistance)
                {
                    for (int i = 0 ; i < this.shortestPathCostMatrix.length; i ++)
                    {
                        timeMatrixWithoutUnreachableVertices[unreachable][i] = euclideanDistanceMatrix [unreachable][i];
                        timeMatrixWithoutUnreachableVertices[i][unreachable] = euclideanDistanceMatrix [i][unreachable];
                    }
                }
            }
        } catch (Exception/*Osm2poException */ex) {
            System.out.println("exception\n");
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            //return null;
        }
        
        if(salesmanCount>1)
        {
            extendedTimeMatrix = getExtendedMatrixForMultipleSalesmen(salesmanCount, timeMatrixWithoutUnreachableVertices); //-UNCOMMENT
            extendedShortestPathMatrix = getExtendedMatrixForMultipleSalesmen(salesmanCount, shortestPathCostMatrix); //-UNCOMMENT
            extendedEuclideanMatrix = getExtendedMatrixForMultipleSalesmen(salesmanCount, euclideanDistanceMatrix);
        }else if(salesmanCount==1){
            extendedTimeMatrix = timeMatrixWithoutUnreachableVertices;//-UNCOMMENT
            extendedShortestPathMatrix = shortestPathCostMatrix; //-UNCOMMENT
            extendedEuclideanMatrix = euclideanDistanceMatrix;
        }
        float[][] extendedSizeTable = null; // extended data matrix
        float[][] normalSizeTable = null; //normal size data matrix
         
        if (selectedMetric == 0) //euclidean
        {//TODO: rewrite it , rewrite separating by 0  
            extendedSizeTable = extendedEuclideanMatrix;
            normalSizeTable = euclideanDistanceMatrix; 
        }else if (selectedMetric ==1) //actual distance
        {
            extendedSizeTable = extendedShortestPathMatrix;
            normalSizeTable = shortestPathCostMatrix;
        }else if (selectedMetric == 2) //time
        {
            extendedSizeTable = extendedTimeMatrix;
            normalSizeTable = this.timeMatrix.getCosts();
        }
        //For every selected algorithm
         for(Algorithm a : this.algorithms){
               AlgorithmSolution solution = getAlgorithmSolution(a,extendedSizeTable,normalSizeTable);
               //Adds algorithm soulution to list of solutions for a single input file. 
               algorithmSolution.add(solution);
               
               if(this.loadedSingleFile){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    Parser.writeAlgorithmSolutionToFile(solution, coordinates,solution.getAlgorithmName()+dateFormat.format(new Date()));
                    this.setChanged();
                    this.notifyObservers(solution);
               }
               else{
                   solutionList.add(solution);
                   String[] filename = filenameToSaveAS[0].split("\\.");
                   Parser.writeAlgorithmSolutionToFile(solution, coordinates,filename[0]+"SOL_"+solution.getAlgorithmName()+".txt");
               }
            }
         return algorithmSolution;
    }
   
    /***
     * Problem instance
     */
    private AlgorithmSolution getAlgorithmSolution(Algorithm a,float[][] table1,float[][] table2){
        int [] result;
        ArrayList<ArrayList<Integer>> cycles;
         long elapsedTimeMS  = 0;
        if(a instanceof SCIPAlgorithm || a instanceof DynamicAlgorithm){
                    long startTime = System.nanoTime();
                        result  = a.solveProblem(table1, this.salesmanCount, this.coordinates);
                    long stopTime = System.nanoTime();
                    elapsedTimeMS = stopTime - startTime;
                    cycles = SolutionOperations.getCyclesFromSolution(salesmanCount, result, true);
               }
               else
               {
                    long startTime = System.nanoTime();
                        result = a.solveProblem(table2,this.salesmanCount);
                    long stopTime = System.nanoTime();
                    elapsedTimeMS = stopTime - startTime;
                    
                    cycles = new  ArrayList<>();
                    ArrayList<Integer> temp =  new ArrayList<>();
                    for(int k : result)
                        temp.add(k);
                    
                    cycles.add(temp);
                    cycles = SolutionOperations.getCyclesFromSolution(salesmanCount, result,false);
               }
               
               //constucting solution
               AlgorithmSolution solution = new AlgorithmSolution();
               solution.setAlgorithmName(a.getName());
               solution.setCycles(cycles);
               solution.setCalculationTime(elapsedTimeMS) ;
               solution.setSalesmenCount(salesmanCount);
               solution.setPointsCount(this.coordinates.size());
               solution.setCyclesLenth(calculateCyclesLengths(table2, cycles)); 
               solution.setAllDistance(this.calculateSolutionLength(solution.getCyclesLenth()));
               return solution;
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
           2 timeMetric(int selectedMetric) {
        this.selectedMetric = selectedMetric;
    }
        */
    public void setSelectedMetric(int selectedMetric) {
        this.selectedMetric = selectedMetric;
    }

    public int getSelectedMetric() {
        return this.selectedMetric;
    }
    
    /***
     * @return number of salesman for a given problem instance.
     */
    public int getSalesmenCount(){
       return this.salesmanCount;
    }


    public void resetData() {
       this.coordinates.clear();
       this.algorithms.clear();
    }
    
    public void setNewStartingPoint(Coordinate co) {
        int index = this.coordinates.indexOf(co); 
        Collections.swap(this.coordinates, 0, index);
    }
    
    int count = 0;
    public void saveSolutionScreenShot(MapPanel panel, File selectedFile) {
        String filePath = selectedFile.toString();  //reading filename
        filePath += ".";
        filePath += File.separator;
        filePath += count; //add  unique name, just count element now
        count++;
        filePath +=".png";
        //Generate bitmap 
       BufferedImage bufImage = new BufferedImage(panel.getSize().width, panel.getSize().height,BufferedImage.TYPE_INT_RGB);
       panel.paint(bufImage.createGraphics());
       File imageFile = new File(filePath);
        try{
            imageFile.createNewFile();
            ImageIO.write(bufImage, "png", imageFile);
        }catch(Exception ex){}
    }   

    /***
     * 
     * @param file file to be input to be loaded from.
     * @return -1 file not parsed
     *          0 exception
     *          1 parsed
     */
    public int loadInputFile(File file) {
        this.inputFiles = new File[1];
        this.inputFiles[0] = file;
        try {
            AlgorithmData algorithmData = Parser.parseFile(file.toString());
            //check if loaded correctly
            if(algorithmData == null){
                //notify user about file not loaded
                return -1;
            }
            else{
                this.coordinates = algorithmData.getCoordinatesAsList();
                this.salesmanCount = algorithmData.getNumSalesmen();
                if(loadedSingleFile == true){ //single file loaded
                     this.setChanged();
                     this.notifyObservers(1); 
                }
                return 1;
            }
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public void loadDirectory(File selectedDirectory) {
        this.inputFiles = selectedDirectory.listFiles();
        //NOtify user that directory selected
        this.setChanged();
        this.notifyObservers(5);
    }
    
    public ArrayList<Double> calculateCyclesLengths (float [][] adjMatrix, ArrayList<ArrayList<Integer>>solution)
    {
        ArrayList<Double> res = new ArrayList<>();
        for (ArrayList<Integer>cycle:solution)
        {
            double sum =0;
            for (int i=0;i<cycle.size()-1;i++)
            {
                sum += adjMatrix[cycle.get(i)][cycle.get(i+1)];
            }
            res.add(sum);
        }
        return res;
    }
    
    public double calculateSolutionLength(ArrayList<Double>solution)
    {
        double len = 0;
        for (double c : solution)
            len += c;
        return len;
    } 
    
    public void saveInputFile(File selectedFile) {
        //read num of salesman to model
        Parser.writeAlgorithnDataToFile(new AlgorithmData(salesmanCount,(ArrayList)coordinates), selectedFile.getPath());
    }

    public void generateAndSaveFiles(int numOffiles, String directoryName, int maxPoints, int maxSalesmen) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
        Date date = new Date();
        directoryName = directoryName + "\\"+ "RandomSetTSP"+ dateFormat.format(date);
        //Create directory
        new File(directoryName).mkdir();
        
        for(int i=0;i<numOffiles;i++){
            Random r = new Random(System.currentTimeMillis());
            int salesmen = maxSalesmen;// r.nextInt(maxSalesmen);
            //int points = ThreadLocalRandom.current().nextInt(1, maxPoints + 1);//r.nextInt(maxPoints);
            
            AlgorithmData algData  = Parser.generateRandomData(salesmen,maxPoints); 
            
            String filename = directoryName + "\\" + "randomFile" + i + ".txt";
            Parser.writeAlgorithnDataToFile(algData, filename);
        }
    }

    /**
     * Check if identical vertex already exists
     * @param coo the coordinate for being tested.
     * @return true- if same vertex id exists
     */
    public boolean identicaVertexIDexists(Coordinate coo) {
        int vertexID = graph.findClosestVertexId((float)coo.getLat(), (float)coo.getLon());
        if(!this.vertexIDS.contains(vertexID)){
            this.vertexIDS.add(vertexID);
            return false;
        }
        else
            return true;
    }

    
}

