
package Modelo;

import Control.Controlador;
import interfaces.Receiver;
import interfaces.Sender;
import mensajeria.Messenger;
import org.json.simple.JSONObject;
import transmission.Local;


public class ModeloJuego implements Receiver, Sender{

    private final Controlador control;
    private Messenger mensajeria;
    private String ipLocal;
    //Servidor remoto
    private int puertoServidor;
    private String ipServidor;
    //Juego
    private String nombreJugador;
    private String jugadorTurno;
    private boolean canPlay;
    
    
    public ModeloJuego(Controlador control) {
        this.control = control;
    }
    
    //------SOCKET
    public void conectar(int puertoLocal, String ipServidor, int puertoServidor, String tipo) {
        mensajeria = new Messenger(this, this, puertoLocal);
        
        if( tipo.equalsIgnoreCase("local") )
            this.ipLocal = Local.getLocalIP();
        else
            this.ipLocal = Local.getPublicIP();
            
        //Guardando datos de conexion
        this.puertoServidor = puertoServidor;
        this.ipServidor = ipServidor;
    }
    
    
    //------SUSCRIPCION
    public void suscribirse(String nombre) {
        this.nombreJugador = nombre;
        
        JSONObject suscripcion = new JSONObject();
        suscripcion.put("tipo",    "jugador");
        suscripcion.put("nombre",  nombre);
        suscripcion.put("ip",      ipLocal);
        suscripcion.put("puerto",  ""+Local.getPuertoLocal());
        
        enviarMensaje( suscripcion );
    }
    
    
    //------LOGICA DEL JUEGO

    private void controlMensajes(JSONObject json) {
        String tipo = (String) json.get("tipo");
        switch (tipo) {

            //---------SUSCRIPCION
            case "aceptado":  aceptado(); break;
            case "rechazado": rechazado(); break;
            case "jugadores": jugadores(json); break;

            //-------JUEGO
            case "juego": establecerJuego(json); break;
            
            //-------Chat
            case "chat": recibirMensajeChat(json); break;
            
            default: consoleLog("Tipo "+tipo+" desconocido"); break;
        }
    }
    
    
    private void aceptado(){
        control.mostrarAceptado( nombreJugador );
    }
    
    
    private void rechazado(){
        control.mostrarRechazado();
    }
    
    private void jugadores(JSONObject json){
        int cantidad = Integer.parseInt((String) json.get("cantidad"));
        control.setCantidadJugadores(cantidad);
    }
    
    
    private void establecerJuego(JSONObject json){
        //Obtiene datos
        String palabra = (String) json.get("palabra");
        String erroneas = (String) json.get("erroneas");
        String jugador = (String) json.get("turno");
        int estado = Integer.parseInt( (String) json.get("estado") );
        //Cronometro
        int tiempo = Integer.parseInt((String) json.get("tiempo"));
        
        this.jugadorTurno = jugador;
        control.setJuego(jugador, palabra, erroneas, tiempo, estado);
    }
    
    
    public void iniciar() {
        JSONObject json = new JSONObject();
        json.put("tipo", "iniciar");
        json.put("nombre", nombreJugador);
        enviarMensaje(json);
    }

    public void jugada(String letra) {
        JSONObject json = new JSONObject();
        json.put("tipo", "jugada");
        json.put("nombre", nombreJugador);
        json.put("letra", letra);
        
        enviarJuego(json);
    }
    
    public void timeOut() {
        JSONObject json = new JSONObject();
        json.put("tipo", "tiempo");
        json.put("nombre", nombreJugador);
        
        enviarJuego(json);
    }
    
    private void enviarJuego(JSONObject json){
        //Para evitar que mande mas
        this.jugadorTurno = "";
        enviarMensaje(json);
    }
    
    
    public String getNombreJugador(){
        return nombreJugador;
    }

    public String getJugadorTurno() {
        return jugadorTurno;
    }
    
    
    public void enviarMensajeChat(String msj) {
        JSONObject json = new JSONObject();
        json.put("tipo", "chat");
        json.put("nombre", nombreJugador);
        json.put("mensaje", msj);
        enviarMensaje(json);
    }
    
    private void recibirMensajeChat( JSONObject json ){
        String mensaje = (String) json.get("mensaje");
        control.setMensajeChat(mensaje);
    }
    
    
    //------ENVIO Y RECEPCION DE MENSAJES
    private void enviarMensaje( JSONObject json ){
        sendData(ipServidor, puertoServidor, json);
    }

    @Override
    public void receiveData(Object o) {
        consoleLog("Recibido: "+ o.toString());
        controlMensajes((JSONObject) o);
    }

    @Override
    public void sendData(String string, int i, Object obj) {
        mensajeria.sendData(ipServidor, puertoServidor, obj);
    }

    @Override
    public void handleSendingError(String string, int i) {
        consoleLog("No se pudo enviar mensaje");
        //control.
    }
    
    private static void consoleLog(String mensaje) {
        System.out.println("-JUGADOR- "+mensaje);
    }

    

    

    

    

}
