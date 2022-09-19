
package interfaces;



public interface Sender {
    
    public void sendData(String ip, int port, Object obj);
    
    public void handleSendingError(String ip, int port);
    
    //public void handleTimeOut();
    
    //public void handleReset();
    
    //public void handleRefused();
    
    
}
