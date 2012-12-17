/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package donnees;


/**
 *
 * @author Watre
 */
public class Joueur {

    private String login;
    //private Thread emetteur, recepteur;
    /*private Emetteur emetteur;
    private Recepteur recepteur;
    private ObjectInputStream in;
    private ObjectOutputStream out;*/
    private int idSalon;

    public Joueur(String l) {
        login = l;
        idSalon = 0;
    }
    
    public String getLogin() {
        return login;
    }
}
