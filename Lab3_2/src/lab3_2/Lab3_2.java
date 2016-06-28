/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab3_2;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Ada
 */
public class Lab3_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            try{ ServerSocket serwer=new ServerSocket(1234);
                while(true){
                   Socket lacze = serwer.accept();
                   Odbieranie x= new Odbieranie(lacze);
                   new Thread(x).start();
                }
        }
       catch (Exception ex){
           System.err.println("Blad serwera");
       }
    }
    
}
