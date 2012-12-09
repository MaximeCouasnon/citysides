/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package données;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import serveur.Emetteur;
import serveur.Recepteur;

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
    private int idSalon=0;

    public Joueur(String l, ObjectInputStream ois, ObjectOutputStream oos) {
        login = l;
        in = ois;
        out = oos;
        /*
         * messager = new Thread(m); messager.start();
         */
        startCommunication();
    }

    public Joueur() {
    }

    private void startCommunication() {
        /*
         * recepteur = new Thread(new Reception(in, login)); recepteur.start();
         * emetteur = new Thread(new Emission(out)); emetteur.start();
         */
        recepteur = new Recepteur(in, login);
        recepteur.start();
        emetteur = new Emetteur(out);
        emetteur.start();
    }

    public String getLogin() {
        return login;
    }

    public int getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(int idSalon) {
        this.idSalon = idSalon;
    }

    public Emetteur getEmetteur() {
        return emetteur;
    }
}
