/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Watre
 */
public class Message implements Serializable {
    protected String login;
    protected Date date;
    
    public Message(String l) {
        login=l;
        date=new Date();
    }

    public Date getDate() {
        return date;
    }

    public String getLogin() {
        return login;
    }
}
