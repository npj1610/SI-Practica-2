/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si.algorithm;

import java.util.ArrayList;

/**
 *
 * @author Niko
 */

//Recibirá los conjuntos de train, pero luego tendrá que ajustarlos para cada clasificador
//      AKA coger los primeros y cuadrar las proporciones de ejemplos

public class Adaboost {
    ArrayList<ClaFuerte> clasificadores;
    ArrayList<String> nombres;
    
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
                    for(int img=0; img<numEjemplos && img<train.size(); img++) {
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
    
    public String getNombreClase(int clase) {
        if(clase == -1) {
            return "No Clasificable";
        }
        return nombres.get(clase);
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
        System.out.println("El porcentaje de aciertos es "+100*(total-errores)/(double) total+"%");
    }
    
}
