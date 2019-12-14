/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si.algorithm;

import java.util.ArrayList;
import pkg1920_p2si.io.Database;

/**
 *
 * @author Niko
 */
public class GeneradorConjuntos {
    
    final private Database db;
    
    private int porcentajeTrain;
    
    private ArrayList<Conjunto> train;
    private ArrayList<Conjunto> test;
    
    public GeneradorConjuntos(Database db, int porcentajeTrain) {
        this.db = db;
        this.porcentajeTrain = porcentajeTrain;
        boundPorcentajeTrain();
    }
    
    private void boundPorcentajeTrain() {
        if(porcentajeTrain < 1) {
            porcentajeTrain = 1;
        } else if (99 < porcentajeTrain) {
            porcentajeTrain = 99;
        }
    }
    
    public void setPorcentajeTrain(int porcentaje) {
        this.porcentajeTrain = porcentaje;
        boundPorcentajeTrain();
    }
    
    public void generarConjuntos(String modo) {
        switch(modo) {
            case "fijo":
                generarConjuntos_fijo();
                break;
            /*
            case "aleatorio":
                generarConjuntos_aleatorio();
                break;
            */
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
            int limite = porcentajeTrain*total.size()/100;
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
    
    public Conjunto getTrain(int c) {
        return train.get(c);
    }
    
    public Conjunto getTest(int c) {
        return test.get(c);
    }
    
    
}
