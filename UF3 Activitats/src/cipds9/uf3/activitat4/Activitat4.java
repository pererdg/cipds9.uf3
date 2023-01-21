package cipds9.uf3.activitat4;

import java.net.InetAddress;

public class Activitat4 {

	public static void main(String[] args) throws Exception {
		InetAddress ip1 = InetAddress.getByName("inspalamos.cat");
		
		InetAddress ip2 = InetAddress.getLoopbackAddress();
		
		InetAddress ip3 = InetAddress.getLocalHost();
		
		byte[] ip = {8, 8, 8, 8};
		InetAddress ip4 = InetAddress.getByAddress(ip);
		
		printIP(ip1);
		printIP(ip2);
		printIP(ip3);
		printIP(ip4);
		printIP(ip4);
	}
	
	private static void printIP(InetAddress ip) {
		System.out.println(ip.toString());
		System.out.println(ip.getHostName());
		System.out.println(ip.getHostAddress() + "\n");
	}

}
