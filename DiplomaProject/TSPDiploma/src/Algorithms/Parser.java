/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Algorithms.AlgorithmData.Tuple;
import Model.AlgorithmSolution;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 *
 * @author Krzysztof
 */
public class Parser {

    public static double minLat = 52.04053;
    public static double minLon = 20.65320;
    public static double maxLat = 52.44973;
    public static double maxLon = 21.39557;

    public static AlgorithmData parseFileContent(String fileContent)
    {
        ArrayList<Tuple<Float,Float>> coords = new ArrayList<>();
        String lines[] = fileContent.split("\\r?\\n");
        int numSalesmen = Integer.parseInt(lines[0]);
        for (int i=1;i<lines.length;i++)
        {
            float lat, lon;
            String [] line = lines[i].split("\\s+");
            lat = Float.parseFloat(line[0]);
            lon = Float.parseFloat(line[1]);
            Tuple <Float,Float> t = new Tuple<>(lat,lon);
            coords.add(t);
        }
        return new AlgorithmData(coords,numSalesmen);
    }
    
    public static AlgorithmData generateRandomData(int numSalesmen, int numPoints) 
    {
        Random r = new Random(System.currentTimeMillis());
        String newLine = System.getProperty("line.separator");
        List<AlgorithmData.Tuple<Float,Float>> coords = new ArrayList<>();
        for (int i=0;i<numPoints;i++)
        {
            double lat = ThreadLocalRandom.current().nextDouble(minLat,maxLat);
            double lon = ThreadLocalRandom.current().nextDouble(minLon,maxLon);
            coords.add(new Tuple<Float,Float>((float)lat,(float)lon));
        }
        return new AlgorithmData(coords, numSalesmen);
    }
    
    public boolean writeRandomFile(String filename, int numSalesmen, int numPoints)
    {
        return(writeAlgorithnDataToFile(generateRandomData(numSalesmen,numPoints),filename));
    }
    
    public static String readFile (String filename) throws IOException 
    {
     return  new String(Files.readAllBytes(Paths.get(filename)));
    }
    
    public static AlgorithmData parseFile (String filename) throws IOException
    {
        return parseFileContent(readFile(filename));
    }
    
    public static boolean writeAlgorithnDataToFile (AlgorithmData data, String filename)
    {
        if(!filename.endsWith(".txt")){
            filename += ".txt";
        }
        String content = "";
        String newLine = System.getProperty("line.separator");
        content +=data.getNumSalesmen() +  newLine;
        List<Tuple<Float,Float>> coords = data.getCoords();
        for (Tuple<Float,Float> tuple : coords)
        {
            String t = tuple.x + " " + tuple.y + newLine;
            content += t;
        }
        try(BufferedWriter w = new BufferedWriter(new FileWriter(filename)))
        {
            w.write(content);
        }
        catch(IOException e)
        {
            return false;
        }
        
        return true;
    }
    
    
    
    public static List<Integer> parseZimpl (String zimplOut)
    {
        ArrayList<Integer>result = new ArrayList<>();
        String newLine = System.getProperty("line.separator");
        String []parts = zimplOut.split("objective value");
        String meaningfulPart = parts[parts.length-1];
        String [] lines = meaningfulPart.split(newLine);
        for (int i=1;i<lines.length;i++)
        {
            try
            {
                String line = lines[i];
                String [] split = line.split("\\$");
                int idx1,idx2;
                idx1 = Integer.parseInt(split[1]);
                idx2 = Integer.parseInt(split[2].split(" ")[0]);
                result.add(idx1);
                result.add(idx2);
            }
            catch (Exception e)
            {
                break;
            }
        }
        return result;
    }
    
    public static boolean writeReportFileForMultipleAlgorithmsHorizontal (List<AlgorithmSolution>data, String filename)
    {
        String content = "";
        StringBuilder sb = new StringBuilder();
        String newLine = (System.getProperty("line.separator"));
        for (AlgorithmSolution s : data)
        {
            sb.append(s.getAlgorithmName()).append(" time: ");
            sb.append(s.getCalculationTime()).append(" s");
            sb.append(" distance: ").append(s.getAllDistance()).append(" km").append(newLine);
        }
        content = sb.toString();
        try(BufferedWriter w = new BufferedWriter(new FileWriter(filename)))
        {
            w.write(content);
        }
        catch(IOException e)
        {
            return false;
        }

        return true;
    }
    
    
     public static boolean writeAlgorithmSolutionToFile (AlgorithmSolution data, List<Coordinate>coords, String filename)
    {
          if(!filename.endsWith(".txt")){
            filename += ".txt";
        }
      
        String content = "";
        StringBuilder sb = new StringBuilder();
        sb.append(data.getAlgorithmName());
         sb.append(System.getProperty("line.separator"));
         int count = 0;
         for (Coordinate c : coords)
         {
             sb.append(count++).append(" ").append(c.getLat()).append(" ").append(c.getLon()).append(System.getProperty("line.separator"));
             
         }
        //add routes information
        int salesmen = 0 ; 
        for(ArrayList<Integer> singleRoute : data.getCycles()){
            sb.append(" ").append(Integer.toString(salesmen)).append(": ");
           
            for(Integer i : singleRoute){
                sb.append("-->");
                sb.append(i.toString());
            }
            sb.append("  Distance : ").append(data.getCyclesLenth().get(salesmen));
            salesmen++;
            sb.append(System.getProperty("line.separator"));;
        }
        sb.append("All distance : " + data.getAllDistance());
        sb.append(System.getProperty("line.separator"));
        
        content = sb.toString();
        try(BufferedWriter w = new BufferedWriter(new FileWriter(filename)))
        {
            w.write(content);
        }
        catch(IOException e)
        {
            return false;
        }
        
        return true;
    }
     
    public static boolean writeFile (String filename, String content)
    {
        try(BufferedWriter w = new BufferedWriter(new FileWriter(filename)))
        {
            w.write(content);
        }
        catch(IOException e)
        {
            return false;
        }
        
        return true;
    }
    
    
}
