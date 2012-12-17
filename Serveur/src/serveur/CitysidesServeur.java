/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import donnees.Joueur;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import serveur.Network.NombreJoueursServeur;

/**
 *
 * @author Watre
 */
public class CitysidesServeur {

    private static Server server;
    private static ArrayList<Salon> salons = new ArrayList<>();
    //La HashMap n'étant pas synchronisée elle doit être modifiée dans un contexte synchronisé
    private static HashMap<String, Joueur> joueurs = new HashMap<>();
    private static boolean verbose = false;

    public static void main(String[] args) throws IOException {
        boolean start = true;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--help")) {
                System.out.print("===Créé par Maxime Couasnon===\n");
                System.out.print("Liste des arguments :\n");
                System.out.print("   --help\tAffiche ce texte\n");
                System.out.print("-v --verbose \tActive le mode verbeux\n");
                System.out.println();
            } else if (args[i].equals("-v") || args[i].equals("--verbose")) {
                verbose = true;
            }
        }

        if (start) {
            Salon hub = new Salon("MétaHub");
            salons.add(hub);
            if (verbose) {
                System.out.println("MétaHub créé");
            }

            server = new Server() {
                @Override
                protected Connection newConnection() {
                    // By providing our own connection implementation, we can store per
                    // connection state without a connection ID to state look up.
                    return new ChatConnection();
                }
            };

            //Enregistrement des classes par sécurité
            Network.register(server);

            server.addListener(new ListenerServeur());
            server.bind(Network.port);
            server.start();

            System.out.println("Serveur en route");
        }
    }

    public static Server getServer() {
        return server;
    }

    //Les modifications de la HashMap 'joueurs' doivent être obligatoirement synchronisées
    public synchronized static void addJoueur(Joueur j) {
        //Ajout du joueur dans les liste des joueurs en ligne
        joueurs.put(j.getLogin(), j);

        //On lui envoit le nombre de joueurs présents actuellement sur le serveur
        NombreJoueursServeur n=new NombreJoueursServeur();
        n.nombre=joueurs.size();
        server.sendToTCP(j.getIdConnection(), n);

        if (verbose) {
            System.out.println(j.getLogin() + " est connecté");
        }
    }

    public synchronized static void removeJoueur(String login) {
        //Suppression du joueur de la liste des joueurs en ligne
        joueurs.remove(login);

        if (verbose) {
            System.out.println(login + " s'est déconnecté");
        }
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

    public static boolean isVerbose() {
        return verbose;
    }
    
    
    // This holds per connection state.
    static class ChatConnection extends Connection {
        public String name;
    }
}