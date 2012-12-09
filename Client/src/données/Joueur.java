/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package données;

import client.Emetteur;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import client.Recepteur;


/**
 *
 * @author Watre
 */
public class Joueur {

    private String login;
    //private Thread emetteur, recepteur;
    private Emetteur emetteur;
    private Recepteur recepteur;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int idSalon;

    public Joueur(String l, ObjectInputStream ois, ObjectOutputStream oos) {
        login = l;
        in=ois;
        out=oos;
        idSalon = 0;

        startCommunication();
    }

    private void startCommunication() {
        recepteur = new Recepteur(in);
        recepteur.start();
        emetteur = new Emetteur(out, login);
        emetteur.start();
    }
    
    public String getLogin() {
        return login;
    }

    public Emetteur getEmetteur() {
        return emetteur;
    }
}
