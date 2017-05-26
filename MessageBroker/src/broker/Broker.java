
package broker;

import objetos.Cliente;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONObject;

public class Broker extends Thread{
    
    public static void main(String[] args) {
        new Broker();
    }

    private Mensajes mensajes;
    private final List<JSONObject> colaMensajes;
    private final List<Cliente> listaEsperaJugadores;
    private final List<Cliente> listaJugadores;
    private Cliente servidor;
    private final FrmConexion frmConexion;
    
    public Broker() {
        this.listaEsperaJugadores = new ArrayList<>();
        this.listaJugadores = new ArrayList<>();
        this.colaMensajes = new ArrayList<>();
        //Ventana de conexion
        this.frmConexion = new FrmConexion(this);
        this.frmConexion.setVisible(true);
    }

    
    /**
     * Recibe los mensajes del servidor
     * @param json 
     */
    public void recibirMensaje(JSONObject json) {
        consoleLog("Mensaje Recibido" + json.toJSONString());
        //colaMensajes.add(json);
        //consoleLog("Mensaje agrgado a cola");
        controlMensajes(json);
    }
    
    
    
    
    //---------------------------------
    

    private synchronized void controlMensajes(JSONObject json) {
        String tipo = (String) json.get("tipo");
        
        switch( tipo ){
            
            //----Suscripciones
            //Jugador
            case "jugador":  agregarListaEspera(json); break;
            //servidor
            case "servidor": suscribirServidor(json); break;
            case "aceptado": aceptarJugador(json); break;
            case "rechazado": rechazarJugador( (String) json.get("nombre"),(String) json.get("razon") );
            
            //---Juego
            case "iniciar": iniciarJuego(json); break;
            case "jugada":  enviarJugada(json); break;
            case "tiempo":  enviarJugada(json); break; //Tiempo del cronometro
            
            //----------CASOS DEl SERVIDOR
            case "juego": enviarJuego(json); break; //Cuando empieza juego, cuando responde a jugada
            
            default: consoleLog("Tipo '"+tipo+"' no se reconoce"); break;
        }
    }
    
    
    private void enviarAServidor(JSONObject json){
        mensajes.sendData(servidor.getIp(), servidor.getPuerto(), json);
    }
    
    private void enviarAJugadores(JSONObject json){
        for (Cliente cli : listaJugadores)
            mensajes.sendData(cli.getIp(), cli.getPuerto(), json);
    }
    

    //------------------------ MÉTODOS DE JUGADORES Y SERVIDOR
    
    private void suscribirServidor( JSONObject json ){
        Cliente cliente;
        
        String ip = (String) json.get("ip");
        String puerto = (String) json.get("puerto");
        int puertoInt = Integer.parseInt( puerto );
        
        cliente = new Cliente(ip, puertoInt, "servidor logico");
        //asigna servidor unico
        this.servidor = cliente;

        JSONObject respuesta = new JSONObject();
        respuesta.put("tipo", "aceptado");
        consoleLog( "Servidor agregado" );
        enviarAServidor(respuesta);
    }
    
    
    //TODO: Hacer que la logica del juego decida sobre la suscripcion
    private void agregarListaEspera(JSONObject json) {
        Cliente cliente;
        
        String nombre = (String) json.get("nombre");
        String ip = (String) json.get("ip");
        String puerto = (String) json.get("puerto");
        int puertoInt = Integer.parseInt( puerto );
        
        //Crea un nuevo objeto Cliente
        cliente = new Cliente(ip, puertoInt, nombre);
        
        //Agrega a lista de espera
        listaEsperaJugadores.add( cliente );
        //Si jugador no se encuentra registrado
        consoleLog("Jugador "+cliente.getNombre()+" agregado a lista de espera");
        
        //Enviando mensaje para servidor
        JSONObject respuesta = new JSONObject();
        respuesta.put("tipo", "jugador");
        respuesta.put("nombre", cliente.getNombre());
        
        enviarAServidor(respuesta);
        
    }
    
    
    
    private void aceptarJugador(JSONObject json){
        
        JSONObject respuesta = new JSONObject();
        String nombre = (String) json.get("nombre");
        for (Cliente jugador : listaEsperaJugadores) {
            if( jugador.getNombre().equalsIgnoreCase( nombre ) ){
                
                synchronized (this){
                    listaJugadores.add( jugador );
                }
                
                
                //Removiendo de lista de espera
                synchronized (this){
                    listaEsperaJugadores.remove( new Cliente(nombre)  );
                }
                
                //Enviando respuesta a jugador
                respuesta.put("tipo", "aceptado");
                enviarRespuestaJugador(jugador, respuesta);
                
                
                consoleLog("Jugador "+ nombre +" aceptado");
                //Notiica a todos los clientes
                enviarNumeroJugadores();
                break;
            }
        }
                
            
    }
    
    private void rechazarJugador(String nombre, String razon){
        //Crea un nuevo objeto JSON
        JSONObject respuesta = new JSONObject();
        
        respuesta.put("tipo", "rechazado");
        respuesta.put("razon", razon);
        
        //Busca al jugador que le corresponde
        Cliente jugador = null;
        for (Cliente ju : listaEsperaJugadores) {
            if( ju.getNombre().equalsIgnoreCase( nombre ) ){
                jugador = ju;
                //Remueve de la lista de espera
                synchronized (this){
                    listaEsperaJugadores.remove( jugador );
                }
                break;
            }
        }
        
        //Envia mensaje
        consoleLog( "Jugador "+ nombre +" rechazado" );
        enviarRespuestaJugador(jugador, respuesta);
    }
    
    
    

    private void enviarNumeroJugadores(){
        consoleLog( "Notificando a jugadores" );
        
        JSONObject numJugadores = new JSONObject();
        numJugadores.put("tipo", "jugadores");
        numJugadores.put("cantidad", ""+listaJugadores.size());
        
        enviarAJugadores( numJugadores );
    }
    
    
    
    public void enviarRespuestaJugador(Cliente cliente, JSONObject respuesta){
        mensajes.sendData(cliente.getIp(), cliente.getPuerto(), respuesta);
    }
    
    
    
    private void iniciarJuego(JSONObject json) {
        if( servidor != null ){
            
            String nombre = (String) json.get("nombre");
            //Si jugador esta suscrito puede comenzar
            if( listaJugadores.contains( new Cliente(nombre) ) ){
                JSONObject iniciar = new JSONObject();
                
                iniciar.put("tipo", "iniciar");
                enviarAServidor(iniciar);  
            }
        }
    }

    
    private void enviarJugada(JSONObject json) {
        enviarAServidor(json);
    }
    
    
    //------------------------ MÉTODOS DE SERVIDOR

    private void enviarJuego(JSONObject json) {
        enviarAJugadores(json);
    }
    
   
    
    
    
    
    private synchronized void verificarCola() {
        if( !this.colaMensajes.isEmpty() ){
            consoleLog("Lectura de mensaje");
            //Obtiene primer elemento
            JSONObject json = this.colaMensajes.get(0);
            //Lo envia
            controlMensajes( json );
            //Lo elimina de la cola de mensajes
            this.colaMensajes.remove( json );
        }

    }
    
    public void iniciarLectura( int puerto ) {
        this.mensajes = new Mensajes(this, puerto);
        frmConexion.dispose();
        //this.start();
    }

    
//    @Override
//    public void run() {
//        
//        while( true ){
//            //Checar cola de mensajes
//            verificarCola();
//            //verificarClientes();
//        }
//        
//    }

    private synchronized void consoleLog(String mensaje) {
        System.out.println("-BROKER- "+mensaje);
    }

    

    
    
    
    
    
}
