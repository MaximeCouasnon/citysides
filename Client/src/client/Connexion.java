/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import données.Joueur;
import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Watre
 */
public class Connexion implements Runnable {

    private Socket socket = null;
    public static Thread messager;
    public static String login, pass;
    //private PrintWriter out;
    private ObjectOutputStream out;
    //private BufferedReader in;
    private ObjectInputStream in;
    private Scanner sc;
    private boolean connect;

    public Connexion(Socket s, String l, String p) {
        socket = s;
        login = l;
        pass = p;
    }

    public void run() {

        try {
            /*
             * out = new PrintWriter(socket.getOutputStream()); in = new
             * BufferedReader(new InputStreamReader(socket.getInputStream()));
             * sc = new Scanner(System.in);
             */

            InputStream is = socket.getInputStream();
            in = new ObjectInputStream(is);
            //in = new BufferedReader(new InputStreamReader(ois));

            OutputStream os = socket.getOutputStream();
            out = new ObjectOutputStream(os);



            //while (!connect) {
                /*
             * System.out.println(in.readLine()); login = sc.nextLine();
             * out.println(login); out.flush();
             *
             * System.out.println(in.readLine()); pass = sc.nextLine();
             * out.println(pass); out.flush();
             */

            out.writeUTF(login + " " + pass);
            out.flush();

            int retour=in.readInt();
            if (retour == 0) {
                //Lecture du numéro du salon
                //int salon = in.readInt();
                //System.out.println(salon);

                /*
                 * try { Set nomsJoueurs = (Set) in.readObject();
                 * //System.out.println(nomsJoueurs.size());
                 *
                 * Iterator i = nomsJoueurs.iterator(); while (i.hasNext()) {
                 * System.out.println(i.next()); } } catch
                 * (ClassNotFoundException e) { e.printStackTrace(); }
                 */

                Client.setMoi(new Joueur(login, in, out));

                Client.jeu();

                /*
                 * if (connect) { //System.out.println("Démarrage messager...");
                 * messager = new Thread(new Messager(in, out));
                 * messager.start(); }
                 */
            }
            else if (retour == -1) {
                System.err.println("Mauvais identifiants");
                Client.getFenetreConnexion().erreur("Mauvais identifiants");
            } else if (retour == -2) {
                System.err.println("Compte déjà actif");
                Client.getFenetreConnexion().erreur("Compte déjà actif");
            }
            else {
                System.err.println("Erreur inconnue !");
                Client.getFenetreConnexion().erreur("Erreur inconnue !");
            }




        } catch (IOException e) {
            System.err.println("Le serveur ne répond plus ");
            Client.getFenetreConnexion().erreur("Le serveur ne répond plus ");
            e.printStackTrace();
        }
    }
}
