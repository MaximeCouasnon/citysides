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
    private int idSalon;
    private int idConnection;

    public Joueur(String l, int idConnection) {
        login = l;
        this.idConnection = idConnection;
        idSalon = 0;
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

    public int getIdConnection() {
        return idConnection;
    }

    public void setIdConnection(int idConnection) {
        this.idConnection = idConnection;
    }
}
