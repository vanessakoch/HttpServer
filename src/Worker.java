import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Worker implements Runnable{

	private Socket clientSocket = null;
    private InputStream input = null;
    private OutputStream output = null;
	private String request = null;
	private String user = "admin";
	private String password = "ifsc";
	private String requestList[] = null;
	private String userInput = null;
	private String passwordInput = null;
    
    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
    	
        try {
			input = clientSocket.getInputStream();
			 
			byte[] dataByte = new byte[1024];
			int data = input.read(dataByte);
			
			if (data > 0){
				request = new String(dataByte, 0 , data);
	        	 
				requestList = request.split("\n");
			} else {  
				return;
			}
			
			if(request.contains("POST")) {
                System.out.println("Cliente enviou : ");

				for(String s : requestList) {
					System.out.println(s);
				}
				
				String[] listInput = requestList[requestList.length-1].split("[=&]");
				
				if(listInput.length == 4)
					userInput = listInput[1];
		            passwordInput = listInput[3];
		            
		            response(request, userInput, passwordInput);
			}
            
        } catch (IOException io) {
        	io.printStackTrace();
        	
        }
    }

    
    public void response(String request, String userInput, String passwordInput) {
    	String httpResponse = null;
    	try {
            output = clientSocket.getOutputStream();

            if(userInput.equalsIgnoreCase(user) && passwordInput.equalsIgnoreCase(password)) {
            	httpResponse = 
						"HTTP/1.1 200 OK\r\nContent-Type:text/html\r\n\r\n "
						+ "<html><header>HTTP/1.1 200 OK</header> \n \n <title> Bem vindo </title> <body> Login "
						+ "realizado com sucesso :) </body> <html>";
				
            } else {
            	httpResponse = 
						"HTTP/1.1 401 Unauthorized\r\nContent-Type:text/html\r\n\r\n "
						+ "<html> <header>HTTP/1.1 401 Unauthorized</header><title> Erro </title> <body> Senha ou usuario incorreto </body> <html>";
				
            }
            output.write(httpResponse.getBytes("UTF-8"));
			output.flush();
			output.close();
			clientSocket.close();
			
            System.out.println("\nServidor respondeu o cliente");
            System.out.println("Conexão do cliente encerrada");

			
			
		} catch (IOException i) {
			i.printStackTrace();
		}
    }
}
