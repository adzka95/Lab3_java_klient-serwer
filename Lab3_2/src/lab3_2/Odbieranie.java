/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab3_2;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ada
 */
public class Odbieranie implements Runnable {
    Socket lacze;
    Odbieranie(Socket gniazdo){
        lacze=gniazdo;
    }
    
    public void run(){
    
    Object o = null;
        try {
            InputStream wejscie = lacze.getInputStream();
            ObjectInputStream wyslanyPlik = new ObjectInputStream(wejscie);

            o = wyslanyPlik.readObject();
            FileOutputStream plik = new FileOutputStream(o.toString());



            byte[] buffer = new byte[1024];
            int rozmiar=500;
            while ((rozmiar = wyslanyPlik.read(buffer)) > -1)
            {
                plik.write(buffer, 0, rozmiar);
            }

            
            System.out.println("Zakonczono pobieranie");
            wyslanyPlik.close();
            plik.close();
    
        
        } catch (Exception ex) {
            System.err.println("Nie udalo sie odebrac");
        }
}
}
