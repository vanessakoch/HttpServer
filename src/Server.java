import java.io.*;
import java.net.*;

public class Server implements Runnable {
    private final int porta;
    ServerSocket server = null;
    
    
    public Server(int porta) {
        this.porta = porta;
    }

	@Override
	public void run() {
        try {
            server = new ServerSocket(porta);
            System.out.println("Servidor está rodando na porta " + porta);

        	while(true) {

	            Socket client = server.accept();
	            
	            System.out.println("Cliente conectado: " + client.getInetAddress() + " " + client.getPort() + "\n");
	            
	            new Thread(new Worker(client)).start();           
        	}
            
        } catch(IOException ex) {
            System.out.println("Servidor já está em uso.");

            if(server != null && !server.isClosed()) {

            	try {

                	server.close();
                } catch(IOException ex1) {
                    ex1.getMessage();
                }
            }
        }
	}
}