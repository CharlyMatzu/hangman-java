
package mensajeria;



import exception.TransmissionException;
import interfaces.ITransmission;
import interfaces.Receiver;
import interfaces.Sender;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import transmission.FTransmision;



public class Messenger implements Sender, Receiver{
    
    private final ITransmission transmicion;
    private final MessageParser parser;
    private final Receiver destino;
    private final Sender mensajero;
    
    
    /**
     * Constructor inicializa puerto para el servidor y el destinatarios
     * se le regresará un JSONObject
     * @param destino a quien se regresarán los mensajes recibido por el servidor
     * @param mensajero
     * @param puertoLocal
     * TODO: inicializar servidor en otro punto después del constructor
     */
    public Messenger(Receiver destino, Sender mensajero, int puertoLocal) {
        parser = new MessageParser();
        this.transmicion = new FTransmision();
        this.destino = destino;
        this.mensajero = mensajero;
        this.transmicion.startServer(puertoLocal, this);
    }
    
    
    
    

    /**
     * Métod que funciona como un Observer el cual recibe  mensajes de la 
     * transmición y los transforma (parsea) en un JSON para regresarlo
     * a la aplicación que utiliza a mensajeria (Destinatario)
     * @param msg mensaje recibido como objeto
     */
    @Override
    public synchronized void receiveData(Object msg) {
        //Se castea a String
        String msj = (String) msg;
        try {    
            //Se transforma String a JSON y se envia
            JSONObject json = parser.stringToJson( msj );
            destino.receiveData( json );
        } catch (ParseException e) {
            System.out.println("-Mensajeria- Error al transoformar a JSON: " + msj +" --- "+ e.getMessage());
            //Se envia un null
            //destino.recibirMensaje( null );
        }
    }

    /**
     * Método que transoforma un JSON a un String y lo envia a la transmición
     * para que sea enviado al sevidor
     * @param ipServidor IP del servidor al que se conectará
     * @param puertoServidor puerto del servidor al que se conectará
     * @param obj objeto con información para el servidor
     */
    @Override
    public synchronized void sendData(String ipServidor, int puertoServidor, Object obj) {
        //Se castea a JSON
        JSONObject json = (JSONObject) obj;
        try {
            this.transmicion.sendData(ipServidor, puertoServidor, this, parser.jsonToString(json));
        } catch (TransmissionException e) {
            System.out.println("Error: "+ e.getMessage());
        }
        
    }
    
    /**
     * Método que controla los problemas con el envio de mensajes
     * @param ip IP del servidor al que se trato de conectar
     * @param puerto puerto del servidor al que se trato de conectar
     */
    @Override
    public synchronized void handleSendingError(String ip, int puerto) {
        this.mensajero.handleSendingError(ip, puerto);
    }
    
    
}
