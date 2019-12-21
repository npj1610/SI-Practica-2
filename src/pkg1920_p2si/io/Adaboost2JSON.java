/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si.io;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pkg1920_p2si.algorithm.Adaboost;
import pkg1920_p2si.algorithm.ClaDebil;
import pkg1920_p2si.algorithm.ClaFuerte;

/**
 *
 * @author Niko
 */
public class Adaboost2JSON {
    
    /*public ClaDebil(String json) {
        
    }*/

    static public JSONObject serializar(ClaDebil cla) {
        JSONObject clasificadorJSON = new JSONObject();
        
        clasificadorJSON.put("dimension", cla.getDimension());
        clasificadorJSON.put("valor", cla.getValor());
        clasificadorJSON.put("direccion", cla.getDireccion());
        
        return clasificadorJSON;
    }
    
    static public JSONObject serializar(ClaFuerte cla) {
        JSONObject clasificadorJSON = new JSONObject();
        
        JSONArray clasificadores = new JSONArray();
        JSONArray confianzas = new JSONArray();
        
        for(int c=0; c<cla.getNumClasificadores(); c++) {
            clasificadores.add(serializar(cla.getClasificador(c)));
            confianzas.add(cla.getConfianza(c));
        }
          
        clasificadorJSON.put("clasificadores", clasificadores);
        clasificadorJSON.put("confianzas", confianzas);
   
        return clasificadorJSON;
    }
    
    static public JSONObject serializar(Adaboost ada) {
        JSONObject adaboostJSON = new JSONObject();
        
        JSONArray nombres = new JSONArray();
        JSONArray clasificadores = new JSONArray();
        
        for(int c=0; c<ada.getNumClases(); c++) {
            nombres.add(ada.getNombreClase(c));
            clasificadores.add(serializar(ada.getClasificador(c)));
            
        }
        
        adaboostJSON.put("nombres", nombres);
        adaboostJSON.put("clasificadores", clasificadores);
        
        return adaboostJSON;
    }
}
