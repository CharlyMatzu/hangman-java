
package interfaces;

import exception.TransmissionException;


public interface ITransmission {
    
    public void startServer( int localPort, Receiver re );
    
    public void sendData( String ip, int puerto, Sender sender, String data ) throws TransmissionException;
    
    //public void sendData( String ip, int puerto, Object obj );
    
}
