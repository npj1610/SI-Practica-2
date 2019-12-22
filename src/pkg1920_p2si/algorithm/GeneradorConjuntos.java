/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import pkg1920_p2si.io.Database;

/**
 *
 * @author Niko
 */
public class GeneradorConjuntos {
    
    final private Database db;
    
    private ArrayList<Conjunto> train;
    private ArrayList<Conjunto> test;
    
    public GeneradorConjuntos(Database db) {
        this.db = db;
    }
    
    public void generarConjuntos() {
        String modo = Parametros.getModoGenerador();
        switch(modo) {
            case "fijo":
                generarConjuntos_fijo();
                break;
            case "aleatorio":
                generarConjuntos_aleatorio();
                break;
            default:
                throw new IllegalArgumentException("No existe el modo "+modo+" para generar conjuntos.");
        }
    }
    
    //Repetir para generarConjuntos_aleatorio, usar idx
    private void generarConjuntos_fijo() {
        train = new ArrayList<>();
        test = new ArrayList<>();
        for(int i=0; i<db.getNumClases(); i++) {
            Conjunto c_train = new Conjunto();
            Conjunto c_test = new Conjunto();
            Conjunto total = db.getPuntos(i);
            int limite = Parametros.getPorcentajeTrain()*total.size()/100;
            for(int p=0; p<total.size(); p++) {
                if(p < limite) {
                    c_train.add(total.get(p));
                } else {
                    c_test.add(total.get(p));
                }
            }
            train.add(c_train);
            test.add(c_test);
        }
    }
    
    //Repetir para generarConjuntos_aleatorio, usar idx
    private void generarConjuntos_aleatorio() {
        train = new ArrayList<>();
        test = new ArrayList<>();
        for(int i=0; i<db.getNumClases(); i++) {
            Conjunto c_train = new Conjunto();
            Conjunto c_test = new Conjunto();
            Conjunto total = db.getPuntos(i);
            //Crea el indice
            ArrayList<Integer> idx = new ArrayList<>();
            for (int j=0; j<total.size(); j++) {
                idx.add(j);
            }
            //Lo desordena
            Random rndgen = new Random();
            for (int j=0; j<total.size(); j++) {
                int rnd = j + rndgen.nextInt(total.size() - j);
                int aux = idx.get(rnd);
                idx.set(rnd, idx.get(j));
                idx.set(j, aux);
            }
            int limite = Parametros.getPorcentajeTrain()*total.size()/100;
            for(int p=0; p<total.size(); p++) {
                if(p < limite) {
                    c_train.add(total.get(idx.get(p)));
                } else {
                    c_test.add(total.get(idx.get(p)));
                }
            }
            train.add(c_train);
            test.add(c_test);
        }
    }
    
    public Conjunto getTrain(int c) {
        return train.get(c);
    }
    
    public ArrayList<Conjunto> getTrain() {
        return train;
    }
    
    public Conjunto getTest(int c) {
        return test.get(c);
    }
    
    public ArrayList<Conjunto> getTest() {
        return test;
    }
    
}
