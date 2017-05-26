package Control;


import Modelo.ModeloJuego;
import Vista.FrmConexion;
import Vista.FrmJuego;
import Vista.FrmSuscribirse;
import Vista.FrmMenu;
import javax.swing.JOptionPane;
import org.json.simple.JSONObject;

public class Controlador extends Thread{
    
    
    public static void main(String[] args) {
        new Controlador();
    }
    
    
    //---MODELO
    private final ModeloJuego modelo;
    
    //---VISTAS
    private FrmSuscribirse frmSuscribirse;
    private final FrmConexion frmConexion;
    private FrmJuego frmJuego;
    private FrmMenu frmMenu;
    
    //Juego
    private int tiempo;
    private boolean goCrono;

    /**
     * Construcor vacio que inicializa la pantalla de suscripcion
     */
    public Controlador() {
        this.frmMenu = new FrmMenu(this);
        this.modelo = new ModeloJuego(this);
        this.frmConexion = new FrmConexion(this);
        this.nuevaConexion();
    }
    



    
    

//-------------------------------------------------------------------------
    public void suscribirse(String jugador) {
        modelo.suscribirse( jugador );
    }

    
    public void iniciarJuego() {
        modelo.iniciar();
    }

    
    public synchronized void enviarJugada(String letra) {
        //Si es su turno
        if( isTurno() )
            modelo.jugada( letra );
        else
            frmJuego.mostrarMensaje("No es tu turno je je!!");
    }

    public void tiempoLimite() {
        modelo.timeOut();
    }

    
    public void recibirPalabra(String palabra) {
        frmJuego.agregarPalabrasCorrecta(palabra);
    }

    
    public void recibirLetraErronea(String letras) {
        frmJuego.agregarPalabrasIncorrecta(letras);
    }

    private boolean isTurno() {
        return modelo.getJugadorTurno().equalsIgnoreCase( modelo.getNombreJugador() );
    }
    
   
    
    public void cambiaEstado(int numEsta) {
        int numEstad = numEsta;

        switch (numEstad) {
            case 0: frmJuego.setEstado("/imagenes/0.png"); break;
            case 1: frmJuego.setEstado("/imagenes/1.png"); break;
            case 2: frmJuego.setEstado("/imagenes/2.png"); break;
            case 3: frmJuego.setEstado("/imagenes/3.png"); break;
            case 4: frmJuego.setEstado("/imagenes/4.png"); break;
            case 5: frmJuego.setEstado("/imagenes/5.png"); break;
            case 6: frmJuego.setEstado("/imagenes/6.png");
                    frmJuego.mostrarMensaje("GAME OVER");
                    break;
            case 7: frmJuego.setEstado("/imagenes/gif2.jpg");
                    frmJuego.mostrarMensaje("GANADOR " + modelo.getJugadorTurno() );
                    break;
        }

    }

    
    //---------------------------------------------------------------------------------------
    private void cronometro(){
        if( tiempo == 0 ){
            timeout();
        }
        else
            tiempo --;
        frmJuego.setCronometor(tiempo);
    }
    
    
    private void timeout() {
        frmJuego.mostrarMensaje("Se acabo el tiempo. Presione aceptar para continuar");
        goCrono = false;
        modelo.timeOut();
    }
    
    
    public void tiempoLimite(String nombre, int puerto, String ipServidor) {
        JSONObject jsono = new JSONObject();
        jsono.put("tipo", "tiempo");
        jsono.put("nombre", nombre);

        //modelo.enviarMensaje(ipServidor, puertoServer, jsono);
        System.out.println(jsono.toJSONString());
        System.out.println("Se acabo el tiempo");
    }

    public void mostrarAceptado(String nombre) {
        JOptionPane.showMessageDialog(null, "Agregado al servidor", "Aceptado", JOptionPane.INFORMATION_MESSAGE);
        //Cierra ventana de suscripcion
        frmSuscribirse.dispose();
        //Abre ventana para juego
        frmMenu.setNombre(nombre);
        frmMenu.setVisible(true);
        //Inicializa juego
        frmJuego = new FrmJuego(this, modelo.getNombreJugador());
    }

    public void mostrarRechazado() {
        JOptionPane.showMessageDialog(null, "Rechazado, el nombre ya existe", "Rechazado", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setCantidadJugadores(int cantidad) {
        frmMenu.setCantidadJugadores(cantidad);
    }

    public void setJuego(String jugadorTurno, String palabra, 
            String erroneas, int tiempo, int estado) {

        //Se cierra ventana de menu la primera vez
        frmMenu.dispose();
        
        //Enviar valores
        frmJuego.setJuego(jugadorTurno, palabra, erroneas);
        
        //Se establece tiempo
        this.tiempo = tiempo;

        //Establecer estado
        cambiaEstado( estado );
        frmJuego.setVisible(true);
        
        //Inicia cronometro si es el turno del jugado
        if( isTurno() )
            iniciarCronometro();
    }

    public void nuevaSuscripcion() {
        frmSuscribirse = new FrmSuscribirse(this);
        this.frmSuscribirse.setVisible(true);
    }


    private void nuevaConexion() {
        this.frmConexion.setVisible(true);
    }

    public void conectar(int puertoLocal, String ipServidor, int puertoServidor) {
        this.modelo.conectar(puertoLocal, ipServidor, puertoServidor);
        frmConexion.dispose();
        nuevaSuscripcion();
    }

    private void iniciarCronometro() {
        //Inicia hilo la primera vez
        if( this.isAlive() )
            this.start();
        
        goCrono = true;
    }
    
    

    @Override
    public void run() {
        
        while( true ){
            while( goCrono ){
                cronometro();
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
            }
        }
            
    }

    
    
    
    
}
