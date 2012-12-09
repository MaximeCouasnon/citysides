/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

/**
 *
 * @author Watre
 */
public class NombreJoueursServeur extends Message {

    private int nombre;

    public NombreJoueursServeur(int nombre) {
        super("Serveur");
        this.nombre = nombre;
    }

    public int getNombre() {
        return nombre;
    }
}
