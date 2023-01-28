package cipds.uf3.tcpsocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static final int PORT = 7894;
	
	public static void main(String[] args) throws Exception{
		System.out.println("SERVIDOR");
		
		//Crear socket servidor
		ServerSocket serverSck = new ServerSocket(PORT);

		int n=0;
		while (n++<5) {
			// Esperar connexió
			Socket sck = serverSck.accept();
			// Connexió establerta
			System.out.println("Connexió: " + sck.getLocalSocketAddress().toString());
			// Agafar streams d'entrada i sortida
			BufferedReader in = new BufferedReader(new InputStreamReader(sck.getInputStream()));
			PrintStream out = new PrintStream(sck.getOutputStream(), true); // Autoflush
			// Legir de client 
			String msg = in.readLine();
			System.out.println(msg);
			// Escriure a client
			out.println("Hola client " + Integer.toString(n) + ", soc el servidor " + sck.getLocalSocketAddress().toString());
			// Tancar recursos
			in.close();
			out.close();
			sck.close();
		}
		// Tancar servidor
		serverSck.close();
	}
}
