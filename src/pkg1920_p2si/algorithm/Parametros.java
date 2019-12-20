/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si.algorithm;

/**
 *
 * @author Niko
 */
public class Parametros {
/*    
    //Constructor default
    private Parameters() {
        
    }
    
    private static Parameters params = null;
    
    public static Parameters get() {
        if (params == null) {
            params = new Parameters();
        }
        return params;
    }
*/    
    
    //Parametros del problema
    
    //Los pixeles toman valores del 0 al ...
    static private int rangoValores;
    
    static public int getRangoValores() {
        return rangoValores;
    }
    
    static public void setRangoValores(int newRangoValores) {
        rangoValores = newRangoValores;
    }
    
    //Cuantos pixeles tiene cada imagen
    static private int dimensiones;
    
    static public int getDimensiones() {
        return dimensiones;
    }
    
    static public void setDimensiones(int newDimensiones) {
        dimensiones = newDimensiones;
    }
    
    //Cuantos Clasificadores Debiles se generan (A)
    static private int intentosAleatorios;
    
    static public int getIntentosAleatorios() {
        return intentosAleatorios;
    }
    
    static public void setIntentosAleatorios(int newIntentosAleatorios) {
        intentosAleatorios = newIntentosAleatorios;
    }
    
    //Cuandos Clasificadores Debiles contiene un Clasificador Fuerte (T)
    static private int numeroClasificadores;
    
    static public int getNumeroClasificadores() {
        return numeroClasificadores;
    }
    
    static public void setNumeroClasificadores(int newNumeroClasificadores) {
        numeroClasificadores = newNumeroClasificadores;
    }
    
    //Modo de generacion de conjuntos
    static private String modoGenerador;
    
    static public String getModoGenerador() {
        return modoGenerador;
    }
    
    static public void setModoGenerador(String newModoGenerador) {
        modoGenerador = newModoGenerador;
    }
    
    //Porcentaje train
    static private int porcentajeTrain;
    
    static public int getPorcentajeTrain() {
        return porcentajeTrain;
    }
    
    static public void setPorcentajeTrain(int newPorcentajeTrain) {
        porcentajeTrain = newPorcentajeTrain;
    }
}
