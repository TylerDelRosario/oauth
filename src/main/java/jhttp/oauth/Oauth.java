/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package jhttp.oauth;

/**
 *
 * @author delro
 */
import java.io.IOException;
public class Oauth {

    public static void main(String[] args) {
        int port = 8000;
        System.out.println("SERVER OPEN ON PORT: " + port);
        
        server authTest = new server(port);
        try {
            authTest.start();
        } catch (IOException e) {
            System.out.println("ERROR:");
            System.out.println(e.getMessage());
        }
    }
}
