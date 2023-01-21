package cipds9.uf3.activitat6;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Classe per establir una connexió UDP com a servidor.
 * Les dades que es reben pel socket UDP es presenten en consola.
 * 
 * @author perer
 *
 */
public class Reciever {

	// Constants de configuració
	private static final int PORT = 6500;

	/**
	 * Punt d'entrada al programa
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String msg;
		byte[] buffer = new byte[2048];
		
		// Crear paquet UDP i socket per rebre les dades.
		// Al socket no se li especifica la IP, per tant, podrà rebre per totes les
		// interfícies de la màquina.
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		DatagramSocket socket = new DatagramSocket(PORT);
		
		System.out.println("RECIEVER");
		
		do {
			// Escoltar i esperar fins que arribin dades
			socket.receive(packet);
			// Transformar a String i presentar
			msg = new String(buffer, 0, packet.getLength());
			System.out.println(msg);
		} while (!msg.equalsIgnoreCase("end"));
		
		// Tancar recursos
		socket.close();
	}
}
