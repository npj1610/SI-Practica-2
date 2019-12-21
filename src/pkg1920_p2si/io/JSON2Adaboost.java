/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si.io;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pkg1920_p2si.algorithm.Adaboost;
import pkg1920_p2si.algorithm.ClaDebil;
import pkg1920_p2si.algorithm.ClaFuerte;

/**
 *
 * @author Niko
 */
public class JSON2Adaboost {
    static public Adaboost parsear(JSONObject adaboost) {
        //clases
        JSONArray JSONclasses = (JSONArray) adaboost.get("nombres");
        ArrayList<String> nombres = new ArrayList<>();

        Iterator itr1 = JSONclasses.iterator();
        while (itr1.hasNext()) {
            nombres.add((String) itr1.next());
        }

        //clasificadores
        JSONArray JSONclassifiers = (JSONArray) adaboost.get("clasificadores");
        ArrayList<ClaFuerte> clasificadores = new ArrayList<>();

        Iterator itr2 = JSONclassifiers.iterator();
        while(itr2.hasNext()) {
            clasificadores.add(parsearFuerte((JSONObject) itr2.next()));
        }

        return Adaboost.generate(clasificadores, nombres);
    }
    
    static public ClaFuerte parsearFuerte(JSONObject clasificador) {
        //confianzas
        JSONArray JSONconfidences = (JSONArray) clasificador.get("confianzas");
        ArrayList<Double> confianzas = new ArrayList<>();

        Iterator itr1 = JSONconfidences.iterator();
        while(itr1.hasNext()) {
            confianzas.add((Double) itr1.next());
        }

        //clasificadores
        JSONArray JSONclassifiers = (JSONArray) clasificador.get("clasificadores");
        ArrayList<ClaDebil> clasificadores = new ArrayList<>();
        
        Iterator itr2 = JSONclassifiers.iterator();
        while(itr2.hasNext()) {
            clasificadores.add(parsearDebil((JSONObject) itr2.next()));
        }

        return ClaFuerte.generate(clasificadores, confianzas);
    }
    
    
    protected int dimension;
    protected int valor;
    protected boolean direccion;
    
    static public ClaDebil parsearDebil(JSONObject clasificador) {
        return ClaDebil.generate(
                (int) Math.floor((Long) clasificador.get("dimension")),
                (int) Math.floor((Long) clasificador.get("valor")),
                (Boolean) clasificador.get("direccion")
        );
    }
    
}
