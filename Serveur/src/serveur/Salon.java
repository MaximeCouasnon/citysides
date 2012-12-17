/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import donnees.Joueur;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import serveur.Network.ArriveeJoueurSalon;
import serveur.Network.DepartJoueurSalon;
import serveur.Network.ListeNomsJoueursSalon;

/**
 *
 * @author Watre
 */
public class Salon {

    public String nom;
    private HashSet<String> nomsJoueurs=new HashSet<>();
    //private Iterator i = nomsJoueurs.iterator();

    public Salon() {
    }

    public Salon(String n) {
        nom = n;
    }

    //Envoi d'un objet à tous les joueurs du salon
    public void envoiObjet(Object o) {
        Joueur j;
        Iterator i = nomsJoueurs.iterator();
        while (i.hasNext()) {
            j = CitysidesServeur.getJoueur((String) i.next());
            CitysidesServeur.getServer().sendToTCP(j.getIdConnection(), o);
        }
    }
    
    //Envoi d'un objet à tous les joueurs du salon
    public void envoiObjet(String login, Object o) {
        Joueur j = CitysidesServeur.getJoueur(login);
        CitysidesServeur.getServer().sendToTCP(j.getIdConnection(), o);
    }

    public synchronized void addJoueur(String login) {
        //Envoi de la liste des joueurs au nouvel arrivant
        ListeNomsJoueursSalon l=new ListeNomsJoueursSalon();
        l.nomsJoueurs=nomsJoueurs;
        l.salon=nom;
        envoiObjet(login, l);
        
        //Ajout du login à la liste des joueurs présents
        nomsJoueurs.add(login);
        
        //Envoi du message d'arrivée à tous les gens du salon
        ArriveeJoueurSalon a=new ArriveeJoueurSalon();
        a.login=login;
        a.nomSalon=nom;
        envoiObjet(a);

        if(CitysidesServeur.isVerbose())System.out.println("**" + nom + "** : " + login + " a rejoint le salon");
    }

    public synchronized void removeJoueur(String login) {
        //Suppression du login de la liste des joueurs présents
        nomsJoueurs.remove(login);
        
        //Envoi du message de départ à tous les gens du salon
        DepartJoueurSalon d=new DepartJoueurSalon();
        d.salon=nom;
        d.login=login;
        envoiObjet(d);
        
        if(CitysidesServeur.isVerbose())System.out.println("**" + nom + "** : " +login + " a quitté le salon");
    }

    public Set getNomsJoueurs() {
        return nomsJoueurs;
    }
}
