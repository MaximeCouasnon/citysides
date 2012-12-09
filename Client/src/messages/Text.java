/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.util.Date;



/**
 *
 * @author Watre
 */
public class Text extends Message {
    public String message;
    
    public Text(String login, String m) {
        super(login);
        message=m;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
