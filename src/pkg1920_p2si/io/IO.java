/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si.io;


import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;

/**
 *
 * @author Niko
 */
public class IO {
    
    static public JSONObject readJSON(String file) {
        try{
            return (JSONObject) new JSONParser().parse(new FileReader(file));
        }  catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    static public void writeJSON (String file, JSONObject json) {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(json.toJSONString());   
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
