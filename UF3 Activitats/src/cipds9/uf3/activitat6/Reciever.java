package cipds9.uf3.activitat6;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Reciever {

	private static int PORT = 6500;
	private static String IP = "127.0.0.1";
	
	public static void main(String[] args) throws Exception {
		String msg;
		byte[] buffer = new byte[2048];
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		DatagramSocket socket = new DatagramSocket(PORT, InetAddress.getByName(IP));
		
		do {
			socket.receive(packet);
			msg = new String(buffer, 0, packet.getLength());
			System.out.println(msg);
		} while (!msg.equalsIgnoreCase("end"));
		
		socket.close();
	}
}
