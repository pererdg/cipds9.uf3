package cipds9.uf3.activitat9;

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
public class Reciever implements Runnable {

	private static final int PORT = 6500;
	private Socket sck;
	
	public Reciever(Socket sck) {
		this.sck = sck;
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		System.out.println("RECIEVER");
		
		//Crear socket servidor
		ServerSocket serverSck = new ServerSocket(PORT);

		do {
			// Esperar connexió
			Socket sck = serverSck.accept();
			new Thread(new Reciever(sck)).start();;
		} while (true);
		
		// Tancar servidor
		//serverSck.close();
	}

	@Override
	public void run() {
		String msg;
		try {
			// Agafar streams d'entrada i sortida
			BufferedReader in = new BufferedReader(new InputStreamReader(sck.getInputStream()));
			PrintStream out = new PrintStream(sck.getOutputStream(), true); // Autoflush
			do {
				// Legir de client 
				msg = in.readLine();
				System.out.println(sck.getLocalSocketAddress() + " >> " + msg);
				// Escriure a client
				out.println(new StringBuilder(msg).reverse());
			} while (!msg.equalsIgnoreCase("end"));
			// Tancar recursos
			in.close();
			out.close();
			sck.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
