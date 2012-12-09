/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

/**
 *
 * @author Watre
 */
public class DepartJoueurSalon extends Message {

    private String nomJoueur;

    public DepartJoueurSalon(String salon, String login) {
        super(salon);
        nomJoueur=login;
    }

    public String getNomJoueur() {
        return nomJoueur;
    }
}
