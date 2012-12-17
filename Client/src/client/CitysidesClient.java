/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Watre
 */
import UI.FenetreConnexion;
import UI.FenetreJeu;
import client.Network.Text;
import com.esotericsoftware.kryonet.Client;
import donnees.DonneesSalon;
import donnees.Joueur;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class CitysidesClient {

    private static String version = "a0.2";
    private static FenetreConnexion fenetreConnexion = new FenetreConnexion();
    private static FenetreJeu fenetreJeu = new FenetreJeu();
    private static Joueur moi;
    private static DonneesSalon donneesSalon;
    private static Client client;
    private static boolean exiting = false, connectionError = false;

    public static void main(String[] args) {
        fenetreConnexion.setLocationRelativeTo(null);
        fenetreConnexion.setVisible(true);
    }

    public static void connexion(String login, String pass) {
        client = new Client();
        //Enregistrement des objets pour raison de sécurité (probablement)
        Network.register(client);
        client.start();

        //Nouveau Récepteur
        ListenerClient recepteur = new ListenerClient(login, pass);
        client.addListener(recepteur);

        final String ip = getIp();
        fenetreConnexion.setTitle("Demande de connexion à " + ip + "...");
        try {
            client.connect(5000, ip, Network.port);
        } catch (IOException ex) {
            fenetreConnexion.erreur("Serveur indisponible");
        }
        CitysidesClient.setMoi(new Joueur(login));


        // This listener is called when the send button is clicked.
        //=> Listener important
        /*
         * chatFrame.setSendListener(new Runnable() {
         *
         * public void run() { ChatMessage chatMessage = new ChatMessage();
         * chatMessage.text = chatFrame.getSendText();
         * client.sendTCP(chatMessage); } });
         */
    }

    public static void jeu() {
        fenetreConnexion.setVisible(false);

        fenetreJeu.setLocationRelativeTo(null);
        fenetreJeu.setVisible(true);
    }

    public static void ecrire(String s) {
        fenetreJeu.ecrire(s);
    }

    public static void envoyerText(String s) {
        Text t = new Text();
        t.login = moi.getLogin();
        t.message = s;
        client.sendTCP(t);
    }

    public static void ecrireNomsJoueurs() {
        //Affichage de la liste
        String s = "<strong>**" + CitysidesClient.getDonneesSalon().getNom() + "**</strong> : Les " + CitysidesClient.getDonneesSalon().getListeNomsJoueurs().size() + " joueurs présents sont => ";
        Iterator i = CitysidesClient.getDonneesSalon().getListeNomsJoueurs().iterator();

        while (i.hasNext()) {
            s += "<strong>" + (String) i.next() + "</strong>";
            if (i.hasNext()) {
                s += ", ";
            }
        }
        ecrire(s);
    }

    private static String getIp() {
        String ip = "";

        try (Scanner sc = new Scanner(new File("ip.ini"))) {
            ip = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("Le fichier ip.ini n'existe pas !");
        }

        return ip;
    }

    public static Joueur getMoi() {
        return moi;
    }

    public static void setMoi(Joueur moi) {
        CitysidesClient.moi = moi;
    }

    public static DonneesSalon getDonneesSalon() {
        return donneesSalon;
    }

    public static void setDonneesSalon(DonneesSalon d) {
        CitysidesClient.donneesSalon = d;
        ecrire("Vous êtes sur <strong>**" + donneesSalon.getNom() + "**</strong>");
    }

    public static String getVersion() {
        return version;
    }

    public static FenetreConnexion getFenetreConnexion() {
        return fenetreConnexion;
    }

    public static Client getClient() {
        return client;
    }

    public static boolean isExiting() {
        return exiting;
    }

    public static void setExiting(boolean exiting) {
        CitysidesClient.exiting = exiting;
    }

    public static boolean isConnectionError() {
        return connectionError;
    }

    public static void setConnectionError(boolean connectionError) {
        CitysidesClient.connectionError = connectionError;
    }

    public static FenetreJeu getFenetreJeu() {
        return fenetreJeu;
    }
}
