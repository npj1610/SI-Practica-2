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
public class Punto extends ArrayList<Integer> {
    
    public Punto(String datos) {
        String[] valores = datos.split(" ");
        for (String valor : valores) {
            this.add((int) Double.parseDouble(valor));
        }
    }

}
