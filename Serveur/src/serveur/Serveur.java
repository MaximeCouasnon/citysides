/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import données.Joueur;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import messages.NombreJoueursServeur;

/**
 *
 * @author Watre
 */
public class Serveur {

    private static ServerSocket serverSocket;
    private static Thread socketAccept;
    private static ArrayList<Salon> salons=new ArrayList<>();
    //La HashMap n'étant pas synchronisée elle doit être modifiée dans un contexte synchronisé
    private static HashMap<String,Joueur> joueurs=new HashMap<>();

    public static void main(String[] args) {
        try {
            System.out.println("Démarrage du serveur sur " + InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            serverSocket = new ServerSocket(2012);
            System.out.println("ServerSocket créé sur le port " + serverSocket.getLocalPort());

            Salon hub = new Salon("MétaHub");
            salons.add(hub);
            System.out.println("MétaHub créé");

            socketAccept = new Thread(new Accepter_connexion(serverSocket));
            socketAccept.start();
            System.out.println("Thread d'acceptation lancé");

        } catch (IOException e) {
            System.err.println("Le port " + serverSocket.getLocalPort() + " est déjà utilisé.");
        }
    }

    //Les modifications de la HashMap 'joueurs' doivent être obligatoirement synchronisées
    public synchronized static void addJoueur(Joueur j) {
        //Ajout du joueur dans les liste des joueurs en ligne
        joueurs.put(j.getLogin(), j);
        
        //On lui envoit le nombre de joueurs présents actuellement sur le serveur
        j.getEmetteur().envoiObjet(new NombreJoueursServeur(joueurs.size()));
        
        System.out.println(j.getLogin() + " est connecté");
    }

    public synchronized static void removeJoueur(String login) {
        //Suppression du joueur de la liste des joueurs en ligne
        joueurs.remove(login);
        
        System.out.println(login + " s'est déconnecté");
    }
    //-----------

    public static Joueur getJoueur(String login) {
        return (Joueur) joueurs.get(login);
    }

    public static List getSalons() {
        return salons;
    }

    public static Salon getSalon(int id) {
        return (Salon) salons.get(id);
    }
}