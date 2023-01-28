package cipds.uf3.tcpsocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

	private static final int PORT = 7894;
	
	public static void main(String[] args) throws Exception {
		System.out.println("CLIENT");
		// Crear socket client i connectar
		Socket sckClient = new Socket(InetAddress.getLoopbackAddress(), PORT);
		// Connexió establerta
		System.out.println("Connexió: " + sckClient.getLocalSocketAddress().toString());
		// Agafar streams d'entrada i sortida
		BufferedReader in = new BufferedReader(new InputStreamReader(sckClient.getInputStream()));
		PrintStream out = new PrintStream(sckClient.getOutputStream(), true); // Autoflush
		// Escriure a servidor
		out.println("Hola servidor, soc el client " + sckClient.getLocalSocketAddress().toString());
		// Legir de servidor 
		String msg = in.readLine();
		System.out.println(msg);
		// Tancar recursos
		in.close();
		out.close();
		sckClient.close();
	}
}
