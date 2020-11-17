import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Worker implements Runnable {

	private Socket clientSocket = null;
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
			BufferedReader buffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			StringBuilder str = new StringBuilder();
			
			String method = buffer.readLine();
			
			while(buffer.ready()) {
				str.append((char) buffer.read());
				requestList = str.toString().split("\n");
			}
				
			if (method.contains("POST")) {
				System.out.println("Cliente enviou : ");

				System.out.println(str);

				String[] listInput = requestList[requestList.length - 1].split("[=&]");

				if (listInput.length == 4)
					userInput = listInput[1];
					passwordInput = listInput[3];

				response(userInput, passwordInput);
			}
			

		} catch (IOException io) {
			io.printStackTrace();

		}
	}

	public void response(String userInput, String passwordInput) {
		String httpResponse = null;

		try {
			DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

			if (userInput.equalsIgnoreCase(user) && passwordInput.equalsIgnoreCase(password)) {
				httpResponse = "HTTP/1.1 200 OK\r\nContent-Type:text/html\r\n\r\n "
						+ "<html><header>HTTP/1.1 200 OK</header> \n \n <title> Bem vindo </title> <body> Login "
						+ "realizado com sucesso :) </body> <html>";

			} else {
				httpResponse = "HTTP/1.1 401 Unauthorized\r\nContent-Type:text/html\r\n\r\n "
						+ "<html> <header>HTTP/1.1 401 Unauthorized</header><title> Erro </title> <body> Senha ou usuario incorreto </body> <html>";

			}
			output.writeBytes(httpResponse);
			output.writeBytes("\n");
			System.out.println("\nServidor respondeu o cliente");
			
			output.flush();
			output.close();
			
			clientSocket.close();
			System.out.println("Conexão do cliente encerrada");

		} catch (IOException i) {
			i.printStackTrace();
		}
	}
}
