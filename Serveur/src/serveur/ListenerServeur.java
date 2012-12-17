/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import donnees.Joueur;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.commons.lang3.StringEscapeUtils;
import serveur.Network.DemandeConnexion;
import serveur.Network.ReponseConnexion;
import serveur.Network.Text;

/**
 *
 * @author Watre
 */
public class ListenerServeur extends Listener {

    @Override
    public void received(Connection c, Object o) {
        CitysidesServeur.ChatConnection connection = (CitysidesServeur.ChatConnection) c;

        if (o instanceof DemandeConnexion) {
            if (connection.name != null) {
                System.err.println("Demande de connexion illégale de " + connection.name);
                c.close();
            } else {
                String login = ((DemandeConnexion) o).login;
                String pass = ((DemandeConnexion) o).pass;

                ReponseConnexion reponse = new ReponseConnexion();

                if (!isValid(login, pass)) {
                    reponse.code = -1;
                    CitysidesServeur.getServer().sendToTCP(c.getID(), reponse);
                    c.close();
                } else if (CitysidesServeur.getJoueur(login) != null) {
                    reponse.code = -2;
                    CitysidesServeur.getServer().sendToTCP(c.getID(), reponse);
                    c.close();
                } else {
                    reponse.code = 0;
                    CitysidesServeur.getServer().sendToTCP(c.getID(), reponse);

                    connection.name = login;
                    Joueur j = new Joueur(login, connection.getID());
                    CitysidesServeur.addJoueur(j);

                    Salon hub = (Salon) CitysidesServeur.getSalons().get(0);
                    hub.addJoueur(j.getLogin());
                }
            }
        } else if (o instanceof Text) {
            Text t = (Text) o;
            t.message = StringEscapeUtils.escapeHtml4(t.message);
            
            CitysidesServeur.getSalon(CitysidesServeur.getJoueur(t.login).getIdSalon()).envoiObjet(o);
            
            if (CitysidesServeur.isVerbose()) {
                System.out.println(t.login + " : " + t.message);
            }
        }
    }

    @Override
    public void disconnected(Connection c) {
        CitysidesServeur.ChatConnection connection = (CitysidesServeur.ChatConnection) c;
        if (connection.name != null) {
            //Le joueur quitte le salon
            Joueur j = CitysidesServeur.getJoueur(connection.name);
            CitysidesServeur.getSalon(j.getIdSalon()).removeJoueur(j.getLogin());

            //Le pseudo est retiré de la liste des joueurs
            CitysidesServeur.removeJoueur(connection.name);
        }
    }

    private boolean isValid(String login, String pass) {

        boolean connexion = false;
        try {
            Scanner sc = new Scanner(new File("comptes.txt"));

            while (sc.hasNext()) {
                if (sc.nextLine().equals(login + " " + pass)) {
                    connexion = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Le fichier n'existe pas !");
        }
        return connexion;
    }
}
