/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Watre
 */
import client.Network.ArriveeJoueurSalon;
import client.Network.DemandeConnexion;
import client.Network.DepartJoueurSalon;
import client.Network.ListeNomsJoueursSalon;
import client.Network.NombreJoueursServeur;
import client.Network.ReponseConnexion;
import client.Network.Text;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import donnees.DonneesSalon;
import java.util.HashSet;

public class ListenerClient extends Listener {

    private String login, pass;

    public ListenerClient(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    @Override
    public void connected(Connection connection) {
        DemandeConnexion d = new DemandeConnexion();
        d.login = login;
        d.pass = pass;
        CitysidesClient.getClient().sendTCP(d);
    }

    @Override
    public void received(Connection connection, Object o) {
        if (o instanceof ReponseConnexion) {
            ReponseConnexion r = (ReponseConnexion) o;
            switch (r.code) {
                case -1:
                    CitysidesClient.setConnectionError(true);
                    CitysidesClient.getFenetreConnexion().erreur("Mauvais login ou mot de passe");
                    CitysidesClient.getClient().close();
                    break;
                case -2:
                    CitysidesClient.setConnectionError(true);
                    CitysidesClient.getFenetreConnexion().erreur("Ce joueur est déjà connecté");
                    CitysidesClient.getClient().close();
                    break;
                case 0:
                    CitysidesClient.jeu();
                    break;
                default:
                    CitysidesClient.setConnectionError(true);
                    CitysidesClient.getFenetreConnexion().erreur("ERREUR INCONNUE");
                    CitysidesClient.getClient().close();
                    break;
            }
        } else if (o instanceof Text) {
            Text t = (Text) o;
            CitysidesClient.ecrire("<strong>" + t.login + "</strong> : " + t.message);
        } else if (o instanceof NombreJoueursServeur) {
            CitysidesClient.ecrire("<strong>***Serveur***</strong> : Il y a " + ((NombreJoueursServeur) o).nombre + " joueurs sur le serveur");
        } else if (o instanceof ArriveeJoueurSalon) {
            ArriveeJoueurSalon a = (ArriveeJoueurSalon) o;
            CitysidesClient.ecrire("<strong>**" + a.nomSalon + "**</strong> : " + a.login + " est arrivé sur le salon");

            HashSet<String> liste = CitysidesClient.getDonneesSalon().getListeNomsJoueurs();
            liste.add(a.login);

            CitysidesClient.ecrireNomsJoueurs();
        } else if (o instanceof DepartJoueurSalon) {
            DepartJoueurSalon d = (DepartJoueurSalon) o;
            CitysidesClient.ecrire("<strong>**" + d.salon + "**</strong> : " + d.login + " a quitté le salon");

            //Actualisation de la liste en local
            HashSet<String> liste = CitysidesClient.getDonneesSalon().getListeNomsJoueurs();
            liste.remove(d.login);

            CitysidesClient.ecrireNomsJoueurs();
        } else if (o instanceof ListeNomsJoueursSalon) {
            ListeNomsJoueursSalon l = (ListeNomsJoueursSalon) o;
            CitysidesClient.setDonneesSalon(new DonneesSalon(l.salon, l.nomsJoueurs));
        }
    }

    @Override
    public void disconnected(Connection connection) {
        if (!CitysidesClient.isExiting() && !CitysidesClient.isConnectionError()) {
            CitysidesClient.getFenetreConnexion().erreur("Déconnecté du serveur");
            CitysidesClient.getClient().close();
            CitysidesClient.getFenetreJeu().setVisible(false);
            CitysidesClient.getFenetreConnexion().setVisible(true);
        }
    }
}
