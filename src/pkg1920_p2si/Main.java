/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1920_p2si;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;
import pkg1920_p2si.io.Database;

/**
 *
 * @author fidel
 */
public class Main {
    
    
    public static int Byte2Unsigned(byte b) {
        return b & 0xFF;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("TEST");
        
        //Cargador MNIST de SI
        MNISTLoader ml = new MNISTLoader();
        ml.loadDBFromPath("./mnist_1000");
        
        //Accedo a las imagenes de dígito 1
        ArrayList d0imgs = ml.getImageDatabaseForDigit(1);
        
        //Y cojo la tercera imagen del dígito 1
        Imagen img = (Imagen) d0imgs.get(2);
        
        //La invierto para ilustrar como acceder a los pixels
        byte imageData[] = img.getImageData();
        for (int i = 0; i < imageData.length; i++){
            
            imageData[i] = (byte) (255 - imageData[i]);
            System.out.print(Byte2Unsigned(imageData[i])+ ",");
        }
        
        //Muestro la imagen invertida
        MostrarImagen imgShow = new MostrarImagen();
        imgShow.setImage(img);
        imgShow.mostrar();
        
        Database db;
        try {
            db = new Database("./2dpoints/params.txt", "./2dpoints/data.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
            System.out.println(e);
            return;
        } catch (IOException e) {
            System.out.println("Read error");
            System.out.println(e);
            return;
        } catch (ParseException e) {
            System.out.println("File badly formated");
            System.out.println(e);
            return;
        }
        
        System.out.println("Dimensions: "+db.dimensions);
        System.out.println("Classes: "+db.clases);
        
    }
    
}
