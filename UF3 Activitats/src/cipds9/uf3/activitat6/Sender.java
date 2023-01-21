package cipds9.uf3.activitat6;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Sender {

	private static int PORT = 6500;
	private static String IP = "127.0.0.1";
	
	public static void main(String[] args) throws Exception {
		String msg;
		byte[] buffer;
		
		DatagramSocket socket = new DatagramSocket();
		
		Scanner in = new Scanner(System.in);
		
		do {
			msg = in.nextLine();
			buffer = msg.getBytes();
			if (buffer.length>2048) {
				System.out.println("Missatge massa llarg");
			} else  {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(IP), PORT);
				socket.send(packet);
			}
		} while (!msg.equalsIgnoreCase("end"));

		in.close();
		socket.close();
	}
}
