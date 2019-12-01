/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package h1.pkg1920_p2si.algorithm;

/**
 *
 * @author Niko
 */
public class Parametros {
    
    private static Parametros param = null;
    
    private Parametros() {
        //Default
        
    }
    
    public static Parametros param() {
        if (param  == null) {
            param = new Parametros();
        }
        return param;
    }
    
}
