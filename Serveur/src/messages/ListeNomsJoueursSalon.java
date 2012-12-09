/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.util.HashSet;

/**
 *
 * @author Watre
 */
public class ListeNomsJoueursSalon extends Message {
    private HashSet<String> nomsJoueurs;
    
    public ListeNomsJoueursSalon(String nomSalon, HashSet<String> noms) {
        super(nomSalon);
        nomsJoueurs=noms;
    }

    public HashSet<String> getNomsJoueurs() {
        return nomsJoueurs;
    }
}
