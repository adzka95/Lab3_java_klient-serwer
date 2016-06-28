/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab3_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javafx.concurrent.Task;

/**
 *
 * @author Ada
 */
public class Wysylacz  extends Task<Void>{
    File plik;
    String serwer;
    int port;
    
    public Wysylacz(File pliczek, String ser, int po){
        plik=pliczek;
        serwer=ser;
        port=po;
        
    }
     
    
    protected Void call() throws Exception {
         updateMessage("Nawiazywanie polaczenia");
         Thread.sleep(100);
         Socket lacze= new Socket(serwer,port);
         OutputStream wyjscie=lacze.getOutputStream();
         ObjectOutputStream obiektWyjsciowy= new ObjectOutputStream(wyjscie);
         obiektWyjsciowy.writeObject(plik.getName());
         updateMessage("Zaczecie ladowania");
         Thread.sleep(100);
         
         FileInputStream wejsciowy=new  FileInputStream(plik);
         
         byte[] buffor=new byte[1024];
         
         int rozmiar;
         int wyslano=0;
         while ((rozmiar = wejsciowy.read(buffor)) > -1) {
             wyslano=wyslano+rozmiar;
             obiektWyjsciowy.write(buffor,0, rozmiar);
             updateProgress(wyslano, plik.length());
             updateMessage("Wyslano " + wyslano + " bitow");
              Thread.sleep(50);
        }
         obiektWyjsciowy.close();
            updateMessage("Zakonczono wysylanie " + wyslano + " bajtow");
            updateProgress(1, 1);

         
         
        return null;
    }
}
