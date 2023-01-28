package cipds9.uf3.activitat8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ServerSocket pel port 6500.
 * Espera un string i el torna a la inversa.
 * 
 * @author perer
 *
 */
public class Reciever {

	private static final int PORT = 6500;
	
	public static void main(String[] args) throws Exception {
		System.out.println("RECIEVER");
		
		//Crear socket servidor
		ServerSocket serverSck = new ServerSocket(PORT);

		String msg;
		do {
			// Esperar connexió
			Socket sck = serverSck.accept();
			// Agafar streams d'entrada i sortida
			BufferedReader in = new BufferedReader(new InputStreamReader(sck.getInputStream()));
			PrintStream out = new PrintStream(sck.getOutputStream(), true); // Autoflush
			// Legir de client 
			msg = in.readLine();
			System.out.println(msg);
			// Escriure a client
			out.println(new StringBuilder(msg).reverse());
			// Tancar recursos
			in.close();
			out.close();
			sck.close();
		} while (!msg.equalsIgnoreCase("end"));
		System.out.println("END");
		
		// Tancar servidor
		serverSck.close();
	}

}
