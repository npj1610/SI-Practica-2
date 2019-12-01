/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray; 
import org.json.simple.parser.*; 

/**
 *
 * @author Niko
 */
public class Database {
    
    public ArrayList<String> clases;
    public int dimensions;
    
    public Database (String paramsFile, String dataFile) throws FileNotFoundException, IOException, ParseException {
        JSONObject params = (JSONObject) new JSONParser().parse(new FileReader(paramsFile));
        dimensions = (int) Math.floor((Long) params.get("dimensions"));
        JSONArray JSONclasses = (JSONArray) params.get("class_names");
        clases = new ArrayList<>();
        
        Iterator itr = JSONclasses.iterator();
        while (itr.hasNext())  
        {
            clases.add((String) itr.next());
        } 
    }
}
