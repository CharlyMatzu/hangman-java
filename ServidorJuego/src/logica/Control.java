package logica;

import interfaces.Receiver;
import interfaces.Sender;
import mensajeria.Messenger;
import org.json.simple.JSONObject;
import transmission.Local;


public class Control implements Receiver, Sender{
    
    public static void main(String[] args) {
        new Control();
    }
    
    
    //---VISTA
    private final FrmConexion frmConexion;
    
    //---Transmision
    private Messenger mensajeria;
    private int puertoBroker;
    private String ipBroker;
    
    //--Logica
    private final Juego juego;

    
    
    /**
     * Constructor vacio que inicializa la lista de palabras erroneas y la
     * lista de jugadores
     */
    public Control() {
        this.juego = new Juego(2, 3, 10);
        //Ventana de conexion
        this.frmConexion = new FrmConexion(this);
        this.frmConexion.setVisible(true);
    }
    
    
    //----------------------------------- CONEXION E INICIO DEL SERVIDOR LOCAL
    
    
    /**
     * Inicializando servidor local
     * @param puertoLocal puerto del servidor local
     */
    public void iniciarServidor(int puertoLocal) {
        if( !Local.isServerON() )
            this.mensajeria = new Messenger(this, this, puertoLocal);
        else
            consoleLog("Server ya iniciado");
    }
    
    public void suscribirse(String ipBroker, int puertoBroker) {
        //Creando JSON
        JSONObject json = new JSONObject();
        json.put("tipo", "servidor");
        json.put("ip", Local.getLocalIP());
        json.put("puerto", ""+ Local.getPuertoLocal());
        
        //Asignando datos de conexion
        this.ipBroker = ipBroker;
        this.puertoBroker = puertoBroker;
        
        //Cerrando ventana
        this.frmConexion.dispose();
        //Enviando datos
        enviarMensaje(json);
    }
    
    
    
    
    //----------------------------------- LOGICA DEL JUEGO
    
    /**
     * Logica del juego
     * @param json mensaje en JSON
     */
    private void controlMensajes(JSONObject json) {
        String tipo = (String) json.get("tipo");
        
        switch( tipo ){
            //Suscripcion
            //case "aceptado": consoleLog("Aceptado..."); break;
            //case "rechazado": consoleLog("Rechazado...."); break;
            
            //Jugadores
            case "jugador": nuevoJugador(json); break;
            //case "remover": removerJugador(json); break;
            
            //Juego
            case "iniciar": iniciarJuego(); break;
            case "jugada":  verificarJugada(json); break;
            case "tiempo": tiempoFinalizado(); break; //Tiempo del cronometro
            //case "juego":   enviarJuego(); break; //Cuando empieza juego, cuando responde a jugada
            
            default: consoleLog("No se conoce Tipo: " + tipo); break;
        }
    }
    
    
    public void nuevoJugador(JSONObject json){
        String jugador = (String) json.get("nombre");
        boolean agregado = this.juego.agregarJugador(jugador);
        
        JSONObject obj = new JSONObject();
        
        if( agregado ){
            consoleLog("Jugador "+jugador+" aceptado");
            
            obj.put("tipo", "aceptado");
            obj.put("nombre", jugador);
        }
        else{
            consoleLog("Jugador "+jugador+" rechazado");
            
            obj.put("tipo", "rechazado");
            obj.put("nombre", jugador);
            obj.put("razon", "Nombre no disponible");
        }
        enviarMensaje(obj);
    }
    
    public void removerJugador(JSONObject json){
        String jugador = (String) json.get("nombre");
        this.juego.removerJugador(jugador);
    }
    
    private void iniciarJuego() {
        this.juego.sortear();
        enviarJuego();
    }
    
    
    private void enviarJuego() {
        JSONObject juegoJSON = new JSONObject();
        juegoJSON.put("tipo",      "juego");
        juegoJSON.put("tiempo",    ""+this.juego.getTiempo() );
        juegoJSON.put("palabra",   this.juego.getPalabraOculta() );
        juegoJSON.put("erroneas",  this.juego.getLetrasErroneas() );
        juegoJSON.put("turno",     this.juego.getJugadorTurno() );
        juegoJSON.put("estado",    ""+this.juego.getEstado() );
        
        enviarMensaje(juegoJSON);
    }
    
    

    private void verificarJugada(JSONObject json) {
        //Casteo de letra (y a mayuscula)
        char letra = ((String) json.get("letra")).toUpperCase().charAt(0);
        consoleLog("Checanco letra:" + letra);
        
        this.juego.verificarLetra( letra );
        enviarJuego();
    }


    private void tiempoFinalizado() {
        juego.timeout();
        enviarJuego();
    }
    
    //----------------------------------- MENSAJERIA
    private void enviarMensaje(JSONObject obj) {
        sendData(ipBroker, puertoBroker, obj);
    }
    
    
    @Override
    public void receiveData(Object o) {
        JSONObject json = (JSONObject) o;
        consoleLog( "Recibido: "+json.toJSONString() );
        controlMensajes(json);
    }

    @Override
    public void sendData(String ip, int puerto, Object obj) {
        this.mensajeria.sendData(ip, puerto, obj);
    }

    @Override
    public void handleSendingError(String ip, int puerto) {
        consoleLog("No se pudo enviar mensaje a: "+ ip+":"+puerto);
    }

    public static void consoleLog(String msg) {
        System.out.println("-GAME- "+msg);
    }

    


    

    
}
