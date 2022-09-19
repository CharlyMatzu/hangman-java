
package interfaces;

import org.json.simple.JSONObject;


public interface Receiver {
    
    /**
     * Método para recibir información de un Remitente
     * @param obj Mensaje tipo Object
     */
    //public void receiveData(JSONObject obj);
    
    public void receiveData(Object obj);
    
}
