/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import données.Joueur;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import messages.ArriveeJoueurSalon;
import messages.DepartJoueurSalon;
import messages.ListeNomsJoueursSalon;

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
            j = Serveur.getJoueur((String) i.next());
            j.getEmetteur().envoiObjet(o);
        }
    }
    
    //Envoi d'un objet à tous les joueurs du salon
    public void envoiObjet(String login, Object o) {
        Joueur j = Serveur.getJoueur(login);
        j.getEmetteur().envoiObjet(o);
    }

    /*public void envoiNomsJoueurs(ListeNomsJoueursSalon liste) {
        Joueur j = Serveur.getJoueur(login);
        j.getEmetteur().envoiObjet(nomsJoueurs);
    }*/

    public synchronized void addJoueur(String login) {
        //Envoi de la liste des joueurs au nouvel arrivant
        envoiObjet(login, new ListeNomsJoueursSalon(nom,nomsJoueurs));
        
        //Ajout du login à la liste des joueurs présents
        nomsJoueurs.add(login);
        
        //Envoi du message d'arrivée à tous les gens du salon
        envoiObjet(new ArriveeJoueurSalon(nom,login));

        if(Serveur.isVerbose())System.out.println("**" + nom + "** : " + login + " a rejoint le salon");
    }

    public synchronized void removeJoueur(String login) {
        //Suppression du login de la liste des joueurs présents
        nomsJoueurs.remove(login);
        
        //Envoi du message de départ à tous les gens du salon
        envoiObjet(new DepartJoueurSalon(nom,login));
        
        if(Serveur.isVerbose())System.out.println("**" + nom + "** : " +login + " a quitté le salon");
    }

    public Set getNomsJoueurs() {
        return nomsJoueurs;
    }
}
