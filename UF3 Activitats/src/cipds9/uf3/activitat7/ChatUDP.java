package cipds9.uf3.activitat7;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ChatUDP {

	// Variables de configuració
	private static final int PORT = 8323;
	private static final int BUFFER_LENGTH = 1024;

	
	public static void main(String[] args) throws Exception {
		
		// Agafar adreça de desti
		if (args.length<1) {
			exit("Cal indicar adreça de destí al primer argument");
		}
		InetAddress ipSend = InetAddress.getByName(args[0]);
		//InetAddress ipSend = InetAddress.getLoopbackAddress();
		//InetAddress ipSend = InetAddress.getLocalHost();
		
		// Crear socket per rebre
		// Si obrim els dos extrems del chat al mateix PC caldrà obrir
		// un socket a la interfície de host i l'altre a la loopback
		// perque no es pot obrir dos sockets en una interfície.
		// Primer intem a interfície host (principal) i després a loopback.
		DatagramSocket sckRecieve = null; 
		try {
			sckRecieve = new DatagramSocket(PORT, InetAddress.getLocalHost());
		} catch (Exception e) {
			try {
				sckRecieve = new DatagramSocket(PORT, InetAddress.getLoopbackAddress());
			} catch (Exception e1) { }
		} 
		if (sckRecieve == null) exit("No s'ha pogut obrir port UDP");
		
		// Crear socket per enviar i presentar informació
		DatagramSocket sckSend = new DatagramSocket();
		System.out.println("Socket per rebre " + sckRecieve.getLocalSocketAddress().toString());
		System.out.println("Socket per enviar " + sckSend.getLocalSocketAddress().toString());
		
		// Sincronitzar
		boolean startChat = synchronize(ipSend, sckRecieve, sckSend);
		
		// Preparar recursos per chat
		Scanner in = new Scanner(System.in);
		DatagramPacket pckRecieve = new DatagramPacket(new byte[BUFFER_LENGTH], BUFFER_LENGTH);
		boolean end = false;

		// Chat
		if (startChat) {
			System.out.println("Extrems sincronitzats. Comença a parlar.");
			do {
				end = send(in, ipSend, sckSend);
				if (!end) end = recieve(sckRecieve, pckRecieve);
			} while (!end);
		} else {
			System.out.println("Extrems sincronitzats. Espera fins que parli l'altre extrem.");
			do {
				end = recieve(sckRecieve, pckRecieve);
				if (!end) end = send(in, ipSend, sckSend);
			} while (!end);
		}
		System.out.println("Chat finalitzat");
		
		// Tancar recursos
		in.close();
		sckRecieve.close();
		sckSend.close();
	}
	
	
	/**
	 * Chat, enviar missatge.
	 * 
	 * @param in
	 * @param ipSend
	 * @param sckSend
	 * @return True si s'ha de tancar el chat
	 * @throws Exception
	 */
	private static boolean send(Scanner in, InetAddress ipSend, DatagramSocket sckSend) throws Exception {
		boolean send = false;
		boolean end = false;
		
		do {
			System.out.print("<< ");
			
			// Legir de consola i validar longitud
			String msg = in.nextLine();
			byte[] buffer = msg.getBytes();
			if (buffer.length>BUFFER_LENGTH) {
				System.out.println("Missatge massa llarg");
			} else  {
				// Crear paquet UDP i enviar utilitzant el socket UDP
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ipSend, PORT);
				sckSend.send(packet);
				send = true;
				end = msg.equalsIgnoreCase("end");
			}
		} while (!send);
		return end;
	}
	
	/**
	 * Chat, rebre missatge.
	 * 
	 * @param sckRecieve
	 * @param pckRecieve
	 * @return True si s'ha de tancar el chat
	 * @throws Exception
	 */
	private static boolean recieve(DatagramSocket sckRecieve, DatagramPacket pckRecieve) throws Exception {
		// Escoltar i esperar fins que arribin dades
		sckRecieve.receive(pckRecieve);
		
		// Transformar a String i presentar
		String msg = new String(pckRecieve.getData(), 0, pckRecieve.getLength());
		System.out.println(">> " + msg);
		
		return msg.equalsIgnoreCase("end");
	}
	
	/**
	 * Sincronitza els dos extrems del chat:
	 * 1.- Determina qui començarà a parlar. El que tingui la IP més petita començarà.
	 * 2.- Inicia la comunicació i espera fins que els dos extrems estan engegats.
	 * 
	 * @param ipSend
	 * @param sckRecieve
	 * @param sckSend
	 * 
	 * @return true si comença chat
	 */
	private static boolean synchronize(InetAddress ipSend, DatagramSocket sckRecieve, DatagramSocket sckSend) throws Exception {
		System.out.println("Sincronitzant els extrems, espera ...");
		
		// Determinar qui comença
		BigInteger s = new BigInteger(ipSend.getAddress());
		BigInteger r = new BigInteger(sckRecieve.getLocalAddress().getAddress());
		boolean startChat = r.floatValue()<s.floatValue();
		
		// Sincronitzar inici
		if (startChat) synchronizeSend(ipSend, sckRecieve, sckSend);
		else synchronizeRecieve(ipSend, sckRecieve, sckSend);
		
		return startChat;
	}
	
	/**
	 * Envia codi d'inicialització i espera retorn de codi d'inicialització.
	 * L'espera té timeout, si no es rep en el timeout torna a enviar.
	 * 
	 * @param ipSend
	 * @param sckRecieve
	 * @param sckSend
	 * @throws Exception
	 */
	private static void synchronizeSend(InetAddress ipSend, DatagramSocket sckRecieve, DatagramSocket sckSend) throws Exception {
		// Paquet amb codi d'inici
		DatagramPacket packetSend = new DatagramPacket(new byte[] {1}, 1, ipSend, PORT);
		
		// Paquet per rebre
		byte[] buffer = {0, 0};
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		do {
			// Enviar codi d'inici
			System.out.println("SincroSend -> envia codi d'inici");
			sckSend.send(packetSend);
						
			try {
				// Esperar resposta durant 1''
				sckRecieve.setSoTimeout(1000);
				System.out.println("SincroSend -> espera retorn codi d'inici");
				sckRecieve.receive(packet);
				// Comprovar que s'ha rebut codi d'inici des de l'altre extrem
				if (!(packet.getLength() == 1 && buffer[0] == 1)) {
					buffer[0] = 0;
				}
			} catch (Exception e) {
				
			} finally {
				sckRecieve.setSoTimeout(0);
			}
		} while (buffer[0] != 1);
	}
	
	/**
	 * Espera codi d'inicialització i quan el rep el retorna.
	 * 
	 * @param ipSend
	 * @param sckRecieve
	 * @param sckSend
	 * @throws Exception
	 */
	private static void synchronizeRecieve(InetAddress ipSend, DatagramSocket sckRecieve, DatagramSocket sckSend) throws Exception {
		byte[] buffer = {0, 0};
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		do {
			// Escoltar i esperar fins que arribin dades
			System.out.println("SincroRecieve -> espera codi d'inici");
			sckRecieve.receive(packet);
			// Comprovar que s'ha rebut codi d'inici des de l'altre extrem
			if (packet.getLength() == 1 && buffer[0] == 1) {
					// Retornar codi d'inici i finalitzar
					packet = new DatagramPacket(buffer, 1, ipSend, PORT);
					System.out.println("SincroRecieve -> retorna codi d'inici");
					sckSend.send(packet);
			} else {
				buffer[0] = 0;
			}
		} while (buffer[0] != 1);
	}
	
	/**
	 * Presenta missatge d'error i tanca programa
	 * 
	 * @param msg
	 */
	private static void exit(String msg) {
		System.out.println(msg);
		System.exit(0);
	}

}
