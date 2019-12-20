/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si.algorithm;

import java.util.Random;

/**
 *
 * @author Niko
 */
public class ClaDebil {
 
    final private int dimension;
    final private int valor;
    private boolean direccion;
    
    public ClaDebil () {
        Random rndgen = new Random();
        dimension = rndgen.nextInt(Parametros.getDimensiones());
        valor = rndgen.nextInt(Parametros.getRangoValores());
        direccion = rndgen.nextInt(2) == 1;
    }
    
    public void invertir() {
        direccion = !direccion;
    }
    
    public int clasificar(Punto p) {
        return direccion ^ (0 < p.get(dimension)-valor) ?
                1 : -1;
    }
}
