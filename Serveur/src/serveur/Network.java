package serveur;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import java.util.HashSet;

// This class is a convenient place to keep things common to both the client and server.
public class Network {

    public static final int port = 2012;

    // This registers objects that are going to be sent over the network.
    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(ArriveeJoueurSalon.class);
        kryo.register(DemandeConnexion.class);
        kryo.register(DepartJoueurSalon.class);
        kryo.register(ListeNomsJoueursSalon.class);
        kryo.register(NombreJoueursServeur.class);
        kryo.register(ReponseConnexion.class);
        kryo.register(Text.class);
        
        kryo.register(HashSet.class);
    }

    public static class ArriveeJoueurSalon {

        public String login;
        public String nomSalon;
    }

    public static class DemandeConnexion {

        public String login, pass;
    }
    
    public static class DepartJoueurSalon {

        public String login, salon;
    }
    
    public static class ListeNomsJoueursSalon {

        public String salon;
        public HashSet<String> nomsJoueurs;
    }
    
    public static class NombreJoueursServeur {

        public int nombre;
    }
    
    public static class ReponseConnexion {

        public int code;
    }
    
    public static class Text {

        public String login, message;
    }
}
