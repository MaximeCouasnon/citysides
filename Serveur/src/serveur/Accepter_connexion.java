/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Watre
 */
public class Accepter_connexion implements Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    private Thread auth;
    private boolean flag = true;

    public Accepter_connexion(ServerSocket ss) {
        serverSocket = ss;
    }

    @Override
    public void run() {

        while (flag == true) {
            try {
                socket = serverSocket.accept();
                if (Serveur.isVerbose()) {
                    System.out.println("Tentative de connexion de " + socket.getInetAddress());
                }

                auth = new Thread(new Authentification(socket));
                auth.start();

            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            }
        }
    }
}
