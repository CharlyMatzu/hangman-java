
package logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Juego {
    
    private Random rnd;
   
    //---JUEGO
    private int turno;
    private int estado;
    private final int GANADOR = 7;
    private final int PERDEDOR = 6;
    //Jugadores
    private String[] ordenJugadores;
    //Palabras
    private String palabra;
    private char[] palabraOculta;
    private final int maxJugadores;
    private final int minJugadores;
    private final int tiempo;
    
    //---Listas
    private final List<String> listaJugadores;
    private final List<String> letrasErroneas;
    private final String[] diccionario;

    
    
    public Juego(int minJugadores, int maxJugadores, int tiempo) {
        //Limitaciones
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.tiempo = tiempo;
        
        //Listas
        this.letrasErroneas = new ArrayList<>();
        this.listaJugadores = new ArrayList<>();
        
        //palabras del juego
        this.diccionario = new String[]{"ABEJA", "AEROPUERTO", "COMPUTADOR", "OSO", "JAVA", "NEVERA", 
            "PROGRAMA", "INFORMATICA", "COMPUTACION", "COMPUTADOR", "CORAZON", "BANANO", "PLATANO", 
            "AUTOMOVIL", "PERRO", "COLOMBIA", "LONDRES", "CEPILLO", "BRAZO", "CABEZA", "CUERPO", 
            "DEPORTE", "SALUD", "ANONYMOUS", "CUADERNO", "PANTALLA", "UBUNTU", "SEMAFORO", "LINUX", 
            "LOBO", "AMOR", "MOSCA", "ZANAHORIA", "PINGUINO", "HACKER", "SISTEMA", "ELEFANTE", "CASCADA", 
            "JUEGOS", "LARGO", "BONITO"};
    }
   
    
    //TODO: tomar en cuenta numero de jugadores maximos y minimos
    public boolean agregarJugador(String jugador){
        if( !this.listaJugadores.contains(jugador) ){
            this.listaJugadores.add(jugador);
            return true;
        }
        else
            return false;
    }
    
    public boolean removerJugador(String jugador){
        return this.listaJugadores.remove(jugador);
    }
    
    
    public void sortear(){
        reset();
        sortearJugadores();
        sortearPalabras();
    }
    
    public void reset(){
        estado = 0;
        turno = 0;
        palabra = null;
        palabraOculta = null;
        letrasErroneas.clear();
    }
    
    private void sortearJugadores() {
        turno = 0;
        int orden;
        String jugador;
        boolean sortear;
        
        ordenJugadores = new String[ listaJugadores.size() ];
        //Lenando arreglo de orden
        for (int i = 0; i < ordenJugadores.length; i++) {
            sortear = true;
            //Ciclo de sorteo
            while( sortear ){
                orden = getEnteroRango(0, listaJugadores.size());
                jugador = listaJugadores.get(i);
                //Verifica si ya se agrego
                if( !isJugadorAgregado(jugador) ){
                    //Agrega
                    ordenJugadores[i] = jugador;
                    //finaliza ciclo de sorteo
                    sortear = false;
                }
            }
        }
    }
    
    private boolean isJugadorAgregado( String jugador ){
        for (int i = 0; i < ordenJugadores.length; i++) {
            if( ordenJugadores[i] != null ){
                if( ordenJugadores[i].equals( jugador ) )
                    return true;
            }
        }
        return false;
    }

    private void sortearPalabras() {
        int indice = getEnteroRango(0, diccionario.length);
        palabra = diccionario[indice].toUpperCase();
        
        //----Ocultando
        palabraOculta = palabra.toCharArray();
        //Se cambian caracteres
        for (int i = 0; i < palabraOculta.length; i++) {
            palabraOculta[i] = '_';
        }
        
        Control.consoleLog("Palabra seleccionada: "+palabra);
    }
    
    
    public String getJugadorTurno(){
        return ordenJugadores[turno];
    }
    
    public String getPalabraOculta(){
        String pal = separarLetras(String.valueOf( palabraOculta ));
        return pal;
    }
    
    private String separarLetras( String palabra ){
        String newPalabra = "";
        for (char p : palabra.toCharArray()) {
            newPalabra += p+" ";
        }
        return newPalabra;
    }
    
    
    public String getLetrasErroneas(){
        return letrasErroneas.toString();
    }

    public int getTiempo() {
        return tiempo;
    }
    
    public int getEstado(){
        return estado;
    }
    
    
    private void next(){
        //Turno jugadores
        if( turno >= ordenJugadores.length-1 )
            turno = 0;
        else
            turno++;
        
        //Estado juego
        estado++;
//        if( estado == 5 )
//            finalizar();
//        else
//            estado++;
    }
    
    private int getEnteroRango(int desde, int hasta){
        rnd = new Random();
        return rnd.nextInt((hasta - desde) + 1) + desde;
    }

    //TODO: checar letras ya seleccionadas tanto "buenas" como "malas"
    public boolean verificarLetra(char letra) {
        
        //Obtengo las letras de la palabra
        char[] letrasPalabra = palabra.toCharArray();
        
        //if( !letrasErroneas.contains( ""+letra ) )
            
        
        boolean isCorrecto = false;
        for(int i=0; i < letrasPalabra.length; i++){
            if( letrasPalabra[i] == letra ){
                palabraOculta[i] = letra;
                isCorrecto = true;
            }
        }
        //Si no le atino
        if( !isCorrecto ){
            letrasErroneas.add(""+letra);
            next();
        }
        else{
            if( isPalabraCompleta() )
                estado = GANADOR;
        }
        
        return isCorrecto;
    }

    public void timeout() {
        next();
    }

    private boolean isPalabraCompleta() {
        for (char c : palabraOculta) {
            if( c == '_' ){
                return false;
            }
        }
        return true;
    }
    
    
}
