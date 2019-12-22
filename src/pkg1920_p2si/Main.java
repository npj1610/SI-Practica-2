/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si;

import pkg1920_p2si.algorithm.Adaboost;
import pkg1920_p2si.algorithm.GeneradorConjuntos;
import pkg1920_p2si.algorithm.Parametros;
import pkg1920_p2si.io.Adaboost2JSON;
import pkg1920_p2si.io.Database;
import pkg1920_p2si.io.IO;

/**
 *
 * @author fidel
 */
public class Main {

    public static void main(String[] args) {
        
        Database db;
        db = new Database("./2dpoints/params.txt", "./2dpoints/data.txt");
        
        
        //Establece los parametros de la imagen
        Parametros.setDimensiones(db.getDimensiones());
        Parametros.setRangoValores(255);
        //Establece los parametros de los sets
        Parametros.setModoGenerador("aleatorio");
        Parametros.setPorcentajeTrain(80);
        //Establece los parametros de adaboost
        Parametros.setIntentosAleatorios(600);
        Parametros.setNumeroClasificadores(300);
        
        GeneradorConjuntos gc = new GeneradorConjuntos(db);
        gc.generarConjuntos();
        
        Adaboost a = new Adaboost(gc.getTrain(), db.getClases());
        
        
        
        System.out.println("TRAIN:");
        a.test(gc.getTrain(), false);
        System.out.println("TEST:");
        a.test(gc.getTest(), false);
        
        
        
        String archivo = "./2dpoints/adaboost.txt";
        
        IO.writeJSON(archivo, Adaboost2JSON.serializar(a));
        
    }
    
}
