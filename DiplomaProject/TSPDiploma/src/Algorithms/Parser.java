/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Algorithms.AlgorithmData.Tuple;
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
    
    public static boolean writeDataToFile (AlgorithmData data, String filename)
    {
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
}
