/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import messages.Text;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Watre
 */
public class Emetteur extends Thread {

    private ObjectOutputStream out;

    public Emetteur(ObjectOutputStream out) {
        this.out = out;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        String commande;

        while (true) {
            //System.out.println("Votre message :");
            commande = sc.nextLine();
            //envoiObjet(commande);
        }
    }

    /*
     * public void envoiMessage(Text m) { //Message m=new Text() try {
     * out.writeObject(m); out.flush(); } catch (IOException ex) {
     * Logger.getLogger(Emetteur.class.getName()).log(Level.SEVERE, null, ex); }
    }
     */
    public void envoiObjet(Object o) {
        try {
            out.writeObject(o);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Emetteur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
