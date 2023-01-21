package cipds9.uf3.activitat6;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * Classe per establir connexió UDP com a client.
 * Llegeix dades de consola i les envia pel socket UDP.
 * 
 * @author perer
 *
 */
public class Sender {

	// Constants de configuració
	private static final int PORT = 6500;
	private static final String IP = "127.0.0.1";
	
	/**
	 * Punt d'entrada al programa
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String msg;
		byte[] buffer;
		
		// Crear socket UDP per enviar dades
		DatagramSocket socket = new DatagramSocket();
		
		System.out.println("SENDER");
		Scanner in = new Scanner(System.in);
		
		do {
			// Legir de consola i validar longitud
			msg = in.nextLine();
			buffer = msg.getBytes();
			if (buffer.length>2048) {
				System.out.println("Missatge massa llarg");
			} else  {
				// Crear paquet UDP i enviar utilitzant el socket UDP
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(IP), PORT);
				socket.send(packet);
			}
		} while (!msg.equalsIgnoreCase("end"));

		// Tancar recursos
		in.close();
		socket.close();
	}
}
