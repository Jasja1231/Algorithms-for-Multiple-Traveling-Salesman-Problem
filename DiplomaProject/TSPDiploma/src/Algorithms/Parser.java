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

/**
 *
 * @author Krzysztof
 */
public class Parser {
   
    
    public static AlgorithmData parseFileContent(String fileContent)
    {
        ArrayList<Tuple<Float,Float>> coords = new ArrayList<>();
        String lines[] = fileContent.split("\\r?\\n");
        int numSalesmen = Integer.parseInt(lines[0]);
        for (int i=1;i<lines.length;i++)
        {
            float lat, lon;
            String [] line = lines[i].split("\\s+");
            lon = Float.parseFloat(line[0]);
            lat = Float.parseFloat(line[1]);
            Tuple <Float,Float> t = new Tuple<>(lon,lat);
            coords.add(t);
        }
        return new AlgorithmData(coords,numSalesmen);
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
    
     public static boolean writeAlgorithmSolutionToFile (AlgorithmSolution data, String filename)
    {
          if(!filename.endsWith(".txt")){
            filename += ".txt";
        }
      
        String content = "";
        StringBuilder sb = new StringBuilder();
        sb.append(data.getAlgorithmName());
         sb.append(System.getProperty("line.separator"));
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
     
    
}
