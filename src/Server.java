import java.io.*;
import java.net.*;

public class Server implements Runnable {
	private final int port;
	ServerSocket server = null;

	public Server(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
			server = new ServerSocket(port);
			System.out.println("Servidor está rodando na porta " + port);

			while (true) {
				Socket client = server.accept();
				System.out.println("Cliente conectado: " + client.getInetAddress() + " " + client.getPort() + "\n");
				new Thread(new Worker(client)).start();
			}

		} catch (IOException ex) {
			System.out.println("Servidor já está em uso.");

			if (server != null && !server.isClosed()) {
				try {
					server.close();
				} catch (IOException ex1) {
					ex1.getMessage();
				}
			}
		}
	}
}