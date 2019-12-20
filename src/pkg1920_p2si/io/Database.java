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
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray; 
import org.json.simple.parser.*; 
import pkg1920_p2si.algorithm.Conjunto;
import pkg1920_p2si.algorithm.Punto;

/**
 *
 * @author Niko
 */
public class Database {
    
    final private ArrayList<String> clases;
    final private ArrayList<Conjunto> puntos;
    final private int dimensiones;
    
    public int getDimensiones() {
        return dimensiones;
    }
    
    public String getClase(int c) {
        return clases.get(c);
    }
    
    public int getClase(String c) {
        int output = -1;
        for (int i=0; i<clases.size(); i++) {
            if (clases.get(i).equals(c)) {
                output = i;
            }
        }
        return output;
    }
    
    public ArrayList<String> getClases() {
        return clases;
    }
    
    public int getNumClases() {
        return clases.size();
    }
    
    public Conjunto getPuntos(int c) {
        return puntos.get(c);
    }
    
    public Database (String paramsFile, String dataFile) throws FileNotFoundException, IOException, ParseException {
        JSONObject params = (JSONObject) new JSONParser().parse(new FileReader(paramsFile));
        JSONObject data = (JSONObject) new JSONParser().parse(new FileReader(dataFile));
        
        //dimensiones
        dimensiones = (int) Math.floor((Long) params.get("dimensions"));
        
        //clases
        JSONArray JSONclasses = (JSONArray) params.get("class_names");
        clases = new ArrayList<>();
        
        Iterator itr1 = JSONclasses.iterator();
        while (itr1.hasNext())  
        {
            clases.add((String) itr1.next());
        }
        
        //puntos
        puntos = new ArrayList<>();
        for (int i=0; i<clases.size(); i++) {
            puntos.add(new Conjunto());
        }
        
        Map datos = (Map) data;
        Iterator<Map.Entry> itr2 = datos.entrySet().iterator();
        
        while (itr2.hasNext()) {
            Map.Entry pair = itr2.next();
            puntos.get((int) Math.floor((Long) pair.getValue())).add(new Punto((String) pair.getKey()));
        }
    }
}
