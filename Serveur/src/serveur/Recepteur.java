/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import données.Joueur;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.Text;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Watre
 */
public class Recepteur extends Thread {

    private ObjectInputStream in;
    private String message, login;
    private boolean flag = true;
    private Salon salon;

    public Recepteur(ObjectInputStream in, String login) {
        this.in = in;
        this.login = login;
    }

    @Override
    public void run() {

        while (flag) {
            Object o;
            try {
                o = in.readObject();
                String s = o.getClass().getName();

                //On échappe les messages envoyés par les joueurs
                if (s.equals("messages.Text")) {
                    Text t = (Text) o;
                    t.setMessage(StringEscapeUtils.escapeHtml4(t.getMessage()));
                    if(Serveur.isVerbose())System.out.println(t.getLogin() + " : " + t.getMessage());
                }

                //Forward du message à tous les gens du salon
                if (!s.equals("messages.DeconnexionJoueur")) {
                    Serveur.getSalon(Serveur.getJoueur(login).getIdSalon()).envoiObjet(o);
                }
                else {
                    deconnexion();
                    flag=false;
                }

                /*
                 * switch (o.getClass().getName()) { case "messages.Text":
                 * //System.out.println(m.getDate() + m.getMessage());
                 * //salon.envoiObjet(o);
                 *
                 * Text m = (Text) o;
                 *
                 * break; }
                 */

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Recepteur.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException e) {
                //Cette exception se lance en cas de timeout
                System.err.println(login + " : connection timeout");
                deconnexion();
                //On éteint le récepteur
                flag = false;
            }
        }
    }

    public void deconnexion() {
        //Infos du joueur
        Joueur j = Serveur.getJoueur(login);

        //Le joueur quitte son salon
        Serveur.getSalon(j.getIdSalon()).removeJoueur(login);

        //Le joueur quitte le jeu
        Serveur.removeJoueur(login);
    }
}
