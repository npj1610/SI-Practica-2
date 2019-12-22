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
public class ClaFuerte {
    ArrayList<ClaDebil> clasificadores;
    ArrayList<Double> confianzas;
    
    //Imma stick with the Adaboost thing cuz OCD
    private ClaFuerte() {}    
    static public ClaFuerte generate(ArrayList<ClaDebil> clasificadores, ArrayList<Double> confianzas) {
        ClaFuerte output = new ClaFuerte();
        output.clasificadores = clasificadores;
        output.confianzas = confianzas;
        return output;
    }
    
    public int getNumClasificadores() {
        return clasificadores.size();
    }
    
    public ClaDebil getClasificador(int i) {
        return clasificadores.get(i);
    }
    
    public Double getConfianza(int i) {
        return confianzas.get(i);
    }
    
    //recibe las etiquetas como 1 o -1
    public ClaFuerte(Conjunto imagenes, ArrayList<Integer> etiquetas) {
        clasificadores = new ArrayList<>();
        confianzas = new ArrayList<>();
        ArrayList<Double> pesos = new ArrayList<>(
                Collections.nCopies(imagenes.size(), 1.0/imagenes.size())
        );
        
        for(int t=0; t<Parametros.getNumeroClasificadores(); t++) {
            //Genera A debiles
            ClaDebil mejor = new ClaDebil();
            double error_mejor = calcularErrorClasificador(mejor, imagenes, etiquetas, pesos);
            for(int a=1; a<Parametros.getIntentosAleatorios(); a++) {
                ClaDebil nuevo = new ClaDebil();
                double error_nuevo = calcularErrorClasificador(nuevo, imagenes, etiquetas, pesos);
                
                if (error_nuevo < error_mejor) {
                    mejor = nuevo;
                    error_mejor = error_nuevo;
                }
            }
            
            //Guarda el mejor clasificador debil
            clasificadores.add(mejor);
            confianzas.add(calcularConfianza(error_mejor));
            
            //Actualiza los pesos
            double acumular = 0.0;
            for(int p=0; p<pesos.size(); p++) {
                
                double multiplicador = Math.exp(
                        -confianzas.get(t) *
                        etiquetas.get(p) *
                        clasificadores.get(t).clasificar(imagenes.get(p))
                );
                double nuevoPeso = pesos.get(p) * multiplicador;
                pesos.set(p, nuevoPeso);
                acumular += nuevoPeso;
            }
            
            //Normaliza los pesos
            for(int p=0; p<pesos.size(); p++) {
                pesos.set(p, pesos.get(p)/acumular);
            }
        }
    }
    
    //Se encarga también de invertir el clasificador debil si el error es mayor de 0.5
    private double calcularErrorClasificador(ClaDebil clasificador, ArrayList<Punto> imagenes, ArrayList<Integer> etiquetas, ArrayList<Double> pesos) {
        double error = 0.0;
        for(int p=0; p<imagenes.size(); p++) {
            if(etiquetas.get(p) != clasificador.clasificar(imagenes.get(p))) {
                error += pesos.get(p);
            }
        }
        //Pone un limite al error para evitar confianza infinita
        double min = Math.pow(10, -16.2556);
        if (error <= min) {
            error = min;
        } else if (1-min <= error) {
            error = 1.0 - min;
        }
        
        //Un clasificador malo es uno bueno en la direccion que no toca
        if (0.5 < error) {
            clasificador.invertir();
            error = 1-error;
        }
        return error;
    }
    
    private double calcularConfianza(double error) {
        double output = (1-error)/error;
        //logaritmo base 2
        output = Math.log10(output)/Math.log10(2.0);
        output = output/2;
        return output;
    }
    
    public double clasificar(Punto imagen) {
        double output = 0.0;
        for(int c=0; c<clasificadores.size(); c++) {
            output += confianzas.get(c) * clasificadores.get(c).clasificar(imagen);
        }
        return output;
    }
}
