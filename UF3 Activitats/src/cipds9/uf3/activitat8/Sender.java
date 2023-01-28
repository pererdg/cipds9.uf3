package cipds9.uf3.activitat8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Llegeix un missatge per consola i l'envia
 * via TCP pel port 6500.
 * El programa acaba quan llegeix end.
 * 
 * @author perer
 *
 */
public class Sender {
	
	private static final int PORT = 6500;
	private static final String IP = "127.0.0.1";

	public static void main(String[] args) throws Exception {
		String msg;
		
		System.out.println("SENDER");
		Scanner con = new Scanner(System.in);
		
		do {
			// Legir de consola i validar longitud
			System.out.print("<< ");
			msg = con.nextLine();
			// Crear socket client i connectar
			Socket sckClient = new Socket(InetAddress.getByName(IP), PORT);
			// Agafar streams d'entrada i sortida
			BufferedReader in = new BufferedReader(new InputStreamReader(sckClient.getInputStream()));
			PrintStream out = new PrintStream(sckClient.getOutputStream(), true); // Autoflush
			// Escriure a servidor
			out.println(msg);
			// Legir de servidor 
			String res = in.readLine();
			System.out.println(">> " + res);
			// Tancar recursos
			in.close();
			out.close();
			sckClient.close();
		} while (!msg.equalsIgnoreCase("end"));
		System.out.println("END");
		
		// Tancar recursos
		con.close();
	}
}
