/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Algorithms.DFS;
import Algorithms.Parser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 *
 * @author Krzysztof
 */
public class SCIPAlgorithm  implements Algorithm {
    
    public static String dataDir =".\\Files\\";
    public static String ZIMPLExec = "zimpl-3.3.0.win.x86_64.vc10.normal.opt.exe";
    public static String SCIPExec = "scip.mingw.x86_64.msvc.opt.spx.exe";
    public static String ZimplFile ="tsp";
    public static String ZimplExtension= ".zpl";
    public static String dataFilename ="tsp.dat";
    public static String data2Filename ="tsp2.dat";
    public static String SCIPExtension = ".lp";


    @Override
    public int[] solveProblem(float[][] adjacencyMatrix, Object... o)
    {
              
        int [] solution = null;
        String newLine = System.getProperty("line.separator");
        int numSalesmen = (int)o[0];
        ArrayList<Coordinate>coords = (ArrayList<Coordinate>)o[1];
        String result = "";
        String data ="";
        String data2 = "";
        
        for (int i=0;i<coords.size()+numSalesmen-1;i++)
            data+= i + newLine;
        //data += i + " " + coords.get(i).getLon() + " " + coords.get(i).getLat() + newLine;
        
        for (int i=0;i<adjacencyMatrix.length -1;i++)
        {
            for (int j=i+1;j<adjacencyMatrix[i].length;j++)
            {
                if (adjacencyMatrix[i][j]<Float.MAX_VALUE)
                {
                     data2+= adjacencyMatrix[i][j] + newLine;
                }
                else
                {
                    data2+= "999" + newLine;
                }
            }
        }
        
        if (!Algorithms.Parser.writeFile(dataDir+dataFilename, data))
            return null;
        if (!Algorithms.Parser.writeFile(dataDir+data2Filename, data2))
            return null;
        
        try 
        {            
            ProcessBuilder pb = new ProcessBuilder(dataDir + ZIMPLExec , ZimplFile + ZimplExtension);
            pb.directory(new File(dataDir));
            Process p = pb.start(); // Start the process.
            p.waitFor();
        
        } 
        catch (Exception ex)
        {
            return null;
        }
        
        Path path = Paths.get(dataDir+ZimplFile + SCIPExtension);

        ProcessBuilder scipProcess;
        
        
        if (Files.exists(path)) 
        {
            scipProcess = new ProcessBuilder();
            scipProcess.directory(new File(dataDir));
            scipProcess.command(dataDir+SCIPExec);
            
            try {
                Process process = scipProcess.start();

                OutputStream stdin = process.getOutputStream(); // <- Eh?
                InputStream stdout = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
                
                writer.write("read" + " " + ZimplFile + SCIPExtension +newLine);
                writer.flush();
                writer.write("optimize"+newLine);
                writer.flush();
                writer.write("display solution"+newLine);
                writer.flush();
                writer.close();
                 StringBuilder everything = new StringBuilder();
                 String line;
                 while( (line = reader.readLine()) != null) 
                 {
                     everything.append(line+newLine);
                 }
                 
                result = everything.toString();
                ArrayList<Integer> res = (ArrayList<Integer>)Parser.parseZimpl(result);
                solution = getCyclefromZimplPairs(res, adjacencyMatrix.length);
                Files.delete(FileSystems.getDefault().getPath(dataDir , ZimplFile + SCIPExtension));
                Files.delete(FileSystems.getDefault().getPath(dataDir , ZimplFile + ".tbl"));


            } catch (IOException ex) {
                Logger.getLogger(SCIPAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            
        }
        else
        {
            return null;
        }
        return solution;

    }
    
    @Override
    public String getName() {
      return "SCIP algorithm";
    }

    private int[] getCyclefromZimplPairs(ArrayList<Integer> res, int n) {
        int [][] adjMatrix = new int [n][n];
        int[] result;
        for (int i=0;i<res.size()-1;i+=2)
        {
            adjMatrix[res.get(i)][res.get(i+1)] = 1;
            adjMatrix[res.get(i+1)][res.get(i)] = 1;
        }
        //int startIdx = findFirstDegree(adjMatrix);
     /*   if (startIdx<0)
            return null;
       */
       int startIdx = 0; ////////////////////////////TERAZ TYLKO BO POTEM BEDZIE WIELE SALESMANOW
        ArrayList<Integer> order = new ArrayList<>();
        boolean [] visited = new boolean [adjMatrix.length];
        DFS.DFS(adjMatrix, visited, adjMatrix.length, startIdx, order);
        result = new int[order.size()];

        //order.add(0);
        for (int i=0;i<order.size();i++)
            result[i] = order.get(i);
        
        return result;
    }
    
      private int findFirstDegree(int[][] adjMatrix) 
    {
        int rowCount = 0, columnCount = 0;
        
        // check the matrix row
        
        for (int j=0;j<adjMatrix.length;j++)
        {
         
            for (int i=0;i<adjMatrix[j].length;i++)
            {
                if (adjMatrix[j][i]>0)
                    rowCount++;
                if (adjMatrix[i][j]>0)
                    columnCount++;
            }    
            
            if (!(rowCount>=2&&columnCount>=2))
                return j;
        }
        return -1;
    }
}
