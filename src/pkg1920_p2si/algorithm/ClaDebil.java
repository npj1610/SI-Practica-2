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
 
    final protected int dimension;
    final protected int valor;
    protected boolean direccion;
    
    @Override
    public String toString() {
        return "[dimension: "+dimension+"; valor: "+valor+"; direccion: "+direccion+"]";
    }
    
    //Could not go full Adaboost workaround ughhhhh
    //sticking to the "generate" thing tho
    private ClaDebil(int dimension, int valor, boolean direccion) {
        this.dimension = dimension;
        this.valor = valor;
        this.direccion = direccion;
    }    
    static public ClaDebil generate(int dimension, int valor, boolean direccion) {
        ClaDebil output = new ClaDebil(dimension, valor, direccion);
        return output;
    }
    
    public int getDimension() {
        return dimension;
    }
    
    public int getValor() {
        return valor;
    }
    
    public boolean getDireccion() {
        return direccion;
    }
    
    public ClaDebil () {
        Random rndgen = new Random();
        dimension = rndgen.nextInt(Parametros.getDimensiones());
        valor = rndgen.nextInt(Parametros.getRangoValores());
        direccion = rndgen.nextInt(2) == 1;
    }
    
    //visibilidad de paquete para evitar que caiga **en malas manos**
    void invertir() {
        direccion = !direccion;
    }
    
    public int clasificar(Punto p) {
        return direccion ^ (0 < p.get(dimension)-valor) ?
                1 : -1;
    }
    
}
