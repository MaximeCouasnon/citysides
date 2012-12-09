/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Watre
 */
import messages.Text;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Emetteur extends Thread {

    private ObjectOutputStream out;
    private Scanner sc = null;
    private boolean flag=true;
    private String login;

    public Emetteur(ObjectOutputStream out, String l) {
        this.out = out;
        login=l;
    }

    public void run() {

        sc = new Scanner(System.in);

        while (flag) {
            /*System.out.println("Votre message :");
            message = sc.nextLine();
            out.println(message);
            out.flush();*/
            
            //System.out.print("Votre message : ");
            String s = sc.nextLine();
            Text m=new Text(login,s);
            
            envoiObjet(m);
        }
    }
    public void envoiObjet(Object o) {
        try {
            out.writeObject(o);
            out.flush();
            //System.out.println("message envoyé"+o.getClass().getName());
        } catch (IOException ex) {
            Logger.getLogger(Emetteur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
