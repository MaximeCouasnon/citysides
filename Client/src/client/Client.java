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
import données.DonneesSalon;
import données.Joueur;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.DeconnexionJoueur;
import messages.Text;

public class Client {

    private static Socket socket = null;
    private static Thread connecteur;
    private static FenetreConnexion fenetreConnexion = new FenetreConnexion();
    private static FenetreJeu fenetreJeu = new FenetreJeu();
    private static Joueur moi;
    private static DonneesSalon donneesSalon;

    public static void main(String[] args) {
        fenetreConnexion.setLocationRelativeTo(null);
        fenetreConnexion.setVisible(true);
    }

    public static void connexion(String login, String pass) {
        try {
            String ip = getIp();

            fenetreConnexion.setTitle("Demande de connexion à " + ip + "...");

            socket = new Socket(ip, 2012);

            connecteur = new Thread(new Connexion(socket, login, pass));
            connecteur.start();
        } catch (UnknownHostException e) {
            System.err.println("Impossible de se connecter à l'adresse " + socket.getLocalAddress());
        } catch (IOException e) {
            System.err.println("Aucun serveur à l'écoute du port " + socket.getLocalPort());
        }
    }
    
    public static void deconnexion() {
        moi.getEmetteur().envoiObjet(new DeconnexionJoueur(moi.getLogin()));
        /*try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("socket fermé");*/
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
        Text t = new Text(moi.getLogin(), s);
        moi.getEmetteur().envoiObjet(t);
    }

    public static void ecrireNomsJoueurs() {
        //Affichage de la liste
        //System.out.print("**" + a.getLogin() + "** : Les " + liste.size() + " joueurs présents sont => ");
        String s = "<strong>**" + Client.getDonneesSalon().getNom() + "**</strong> : Les " + Client.getDonneesSalon().getListeNomsJoueurs().size() + " joueurs présents sont => ";
        Iterator i = Client.getDonneesSalon().getListeNomsJoueurs().iterator();

        while (i.hasNext()) {
            //System.out.print((String) i.next());
            s += "<strong>" + (String) i.next() + "</strong>";
            if (i.hasNext()) {
                //System.out.print(", ");
                s += ", ";
            }
        }
        //System.out.println();
        ecrire(s);
    }

    private static String getIp() {
        String ip = "";

        try {
            Scanner sc = new Scanner(new File("ip.ini"));
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
        Client.moi = moi;
    }

    public static DonneesSalon getDonneesSalon() {
        return donneesSalon;
    }

    public static void setDonneesSalon(DonneesSalon d) {
        Client.donneesSalon = d;
        ecrire("Vous êtes sur <strong>**" + donneesSalon.getNom() + "**</strong>");
        //System.out.println("Vous êtes sur **"+donneesSalon.getNom()+"**");
    }
}
