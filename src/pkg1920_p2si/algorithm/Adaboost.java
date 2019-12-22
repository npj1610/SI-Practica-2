/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si.algorithm;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Niko
 */

//Recibirá los conjuntos de train, pero luego tendrá que ajustarlos para cada clasificador
//      AKA coger los primeros y cuadrar las proporciones de ejemplos

public class Adaboost {
    ArrayList<ClaFuerte> clasificadores;
    ArrayList<String> nombres;
    
    //Weird workaround to avoid same erasure with the other constructor
    //Also weird workaround to avoid coupling with input/output module
    //I wanted to do some reflection stuff for the JSON parsing but no time
    private Adaboost() {}    
    static public Adaboost generate(ArrayList<ClaFuerte> clasificadores, ArrayList<String> nombres) {
        Adaboost output = new Adaboost();
        output.clasificadores = clasificadores;
        output.nombres = nombres;
        return output;
    }
    
    public Adaboost(ArrayList<Conjunto> train, ArrayList<String> nombres) {
        this.nombres = nombres;
        clasificadores = new ArrayList<>();
        
        //Genera los clasificadores
        for(int clase=0; clase<train.size(); clase++) {
            //proporciona un conjunto con 50% de positivos y 50% de negativos
            Conjunto imagenes = new Conjunto();
            ArrayList<Integer> etiquetas = new ArrayList<>();
            
            for(Punto img : train.get(clase)) {
                imagenes.add(img);
                etiquetas.add(1);
            }
            int numEjemplos = train.get(clase).size()/(train.size()-1);
            for(Conjunto c : train) {
                if(c != train.get(clase)) {
                    for(int img=0; img<numEjemplos && img<c.size(); img++) {
                        imagenes.add(c.get(img));
                        etiquetas.add(-1);
                    }
                }
            }
            
            //Crea un clasificador fuerte con esas imagenes
            ClaFuerte clasificador = new ClaFuerte(imagenes, etiquetas);
            clasificadores.add(clasificador);
        }
    }
    
    public int getNumClases() {
        return nombres.size();
    }
    
    public String getNombreClase(int clase) {
        if(clase == -1) {
            return "No Clasificable";
        }
        return nombres.get(clase);
    }
    
    public ClaFuerte getClasificador(int clase) {
        return clasificadores.get(clase);
    }
    
    //Devuelve -1 si ningun clasificador es positivo
    public int clasificar(Punto imagen) {
        int resultado = -1;
        double valor = 0.0;
        for(int clase=0; clase<clasificadores.size(); clase++) {
            double nuevoValor = clasificadores.get(clase).clasificar(imagen);
            if(valor < nuevoValor) {
                resultado = clase;
                valor = nuevoValor;
            }
        }
        
        return resultado;
    }
    
    /*
    public void test(ArrayList<Conjunto> test) {
        int errores = 0;
        int total = 0;
        for(int conjunto=0; conjunto<test.size(); conjunto++) {
            for(Punto img : test.get(conjunto)) {
                int result = clasificar(img);
                if(result != conjunto) {
                    System.out.println("El punto con coordenadas: "+img);
                    System.out.println("Es un "+getNombreClase(conjunto)+" pero ha sido clasificado como "+getNombreClase(result));
                    errores++;
                }
                total++;
            }
        }
        System.out.println("El porcentaje de aciertos es "+100*(total-errores)/(double) total+"% de "+total+" imagenes");
    }
    */
    
    public void test(ArrayList<Conjunto> test, boolean verbose) {
        int errores = 0;
        int total = 0;
        ArrayList<Integer> erroresFuerte = new ArrayList<>(
                Collections.nCopies(test.size(), 0)
        );
        for(int conjunto=0; conjunto<test.size(); conjunto++) {
            for(Punto img : test.get(conjunto)) {
                //Clasificar con extra steps
                int result = -1;
                double valor = 0.0;
                for(int clase=0; clase<clasificadores.size(); clase++) {
                    double nuevoValor = clasificadores.get(clase).clasificar(img);
                    if(valor < nuevoValor) {
                        result = clase;
                        valor = nuevoValor;
                    }
                    if (0 < nuevoValor && clase!=conjunto) {
                        erroresFuerte.set(clase, erroresFuerte.get(clase)+1);
                        if (verbose) {
                            System.out.println("\tEl punto "+getNombreClase(conjunto)+" con coordenadas: "+img);
                            System.out.println("\tProvoca un falso positivo en el clasificador fuerte de "+getNombreClase(clase));
                        }
                    } else if (nuevoValor <= 0 && clase == conjunto) {
                        erroresFuerte.set(clase, erroresFuerte.get(clase)+1);
                        if (verbose) {
                            System.out.println("\tEl punto "+getNombreClase(conjunto)+" con coordenadas: "+img);
                            System.out.println("\tProvoca un falso negativo en su clasificador fuerte");
                        }
                    }
                }
                if(result != conjunto) {
                    if (verbose) {
                        System.out.println("El punto con coordenadas: "+img);
                        System.out.println("Es un "+getNombreClase(conjunto)+" pero ha sido clasificado como "+getNombreClase(result));
                    }
                    errores++;
                }
                total++;
            }
        }
        System.out.println("El porcentaje de aciertos es "+100*(total-errores)/(double) total+"% de "+total+" imagenes");
        for(int i=0; i<erroresFuerte.size(); i++) {
            int error = erroresFuerte.get(i);
            System.out.println("\tEl porcentaje de aciertos del clasificador fuerte "+getNombreClase(i)+" es "+100*(total-error)/(double) total+"%");
        }
        
        
    }
}
