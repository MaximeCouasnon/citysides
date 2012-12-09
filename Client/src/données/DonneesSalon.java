/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package données;

import java.util.HashSet;

/**
 *
 * @author Watre
 */
public class DonneesSalon {
    private String nom;
    private HashSet<String> listeNomsJoueurs=new HashSet<>();
    
    public DonneesSalon(String n, HashSet<String> liste) {
        nom=n;
        listeNomsJoueurs=liste;
    }

    public HashSet<String> getListeNomsJoueurs() {
        return listeNomsJoueurs;
    }

    public void setListeNomsJoueurs(HashSet<String> listeNomsJoueurs) {
        this.listeNomsJoueurs = listeNomsJoueurs;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
