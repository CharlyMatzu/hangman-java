
package broker;

import interfaces.Receiver;
import interfaces.Sender;
import mensajeria.Messenger;
import org.json.simple.JSONObject;


public class Mensajes implements Sender, Receiver{
    
    private final Messenger mensajeria;
    private final Broker broker;

    public Mensajes(Broker broker, int puertoLocal) {
        this.broker = broker;
        this.mensajeria = new Messenger(this, this, puertoLocal);
    }
    

    @Override
    public void sendData(String ip, int purto, Object obj) {
        this.mensajeria.sendData(ip, purto, obj);
    }

    @Override
    public void handleSendingError(String ip, int puerto) {
        System.out.println("NOOOO SE ENVIO NADA A: "+ip+":"+puerto);
    }

    @Override
    public void receiveData(Object o) {
        JSONObject json = (JSONObject) o;
        this.broker.recibirMensaje(json);
    }
    
}
