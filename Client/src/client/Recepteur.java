/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Watre
 */
import données.DonneesSalon;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.*;

public class Recepteur extends Thread {

    private ObjectInputStream in;
    private boolean flag = true;

    public Recepteur(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {

        while (flag == true) {
            try {
                Object o;

                try {
                    o = in.readObject();
                    //System.out.println(o.getClass().getName());
                    String s = o.getClass().getName();
                    if (s.equals("messages.Text")) {

                        Text t = (Text) o;
                        //System.out.println(t.getLogin() + " : " + t.getMessage());
                        Client.ecrire("<strong>" + t.getLogin() + "</strong> : " + t.getMessage());
                    } else if (s.equals("messages.NombreJoueursServeur")) {
                        NombreJoueursServeur n = (NombreJoueursServeur) o;
                        //System.out.println("***" + n.getLogin() + "*** : Il y a " + n.getNombre() + " joueurs sur le serveur");
                        Client.ecrire("<strong>***" + n.getLogin() + "***</strong> : Il y a " + n.getNombre() + " joueurs sur le serveur");
                    } else if (s.equals("messages.ArriveeJoueurSalon")) {
                        ArriveeJoueurSalon a = (ArriveeJoueurSalon) o;
                        //System.out.println("**" + a.getLogin() + "** : " + a.getNomJoueur() + " est arrivé sur le salon");
                        Client.ecrire("<strong>**" + a.getLogin() + "**</strong> : " + a.getNomJoueur() + " est arrivé sur le salon");
                        {

                            //Actualisation de la liste en local
                            HashSet<String> liste = Client.getDonneesSalon().getListeNomsJoueurs();
                            liste.add(a.getNomJoueur());

                            Client.ecrireNomsJoueurs();
                        }
                    } else if (s.equals("messages.DepartJoueurSalon")) {

                        DepartJoueurSalon d = (DepartJoueurSalon) o;
                        //System.out.println("**" + d.getLogin() + "** : " + d.getNomJoueur() + " a quitté le salon");
                        Client.ecrire("<strong>**" + d.getLogin() + "**</strong> : " + d.getNomJoueur() + " a quitté le salon");

                        //Actualisation de la liste en local
                        HashSet<String> liste = Client.getDonneesSalon().getListeNomsJoueurs();
                        liste.remove(d.getNomJoueur());

                        Client.ecrireNomsJoueurs();
                    } else if (s.equals("messages.ListeNomsJoueursSalon")) {
                        ListeNomsJoueursSalon l = (ListeNomsJoueursSalon) o;
                        Client.setDonneesSalon(new DonneesSalon(l.getLogin(), l.getNomsJoueurs()));
                    }

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Recepteur.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.out.println(message);
            } catch (IOException e) {
                //System.err.println("Le serveur ne répond plus.");
                //e.printStackTrace();
                flag = false;
            }
        }
    }
}
