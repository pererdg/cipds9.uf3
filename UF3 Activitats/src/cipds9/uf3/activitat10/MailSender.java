package cipds9.uf3.activitat10;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class MailSender {

	private String smtp_server;
	private int smtp_port;
	private Socket sck;
	private BufferedReader in;
	private PrintStream out;
	private PrintStream printer;

	public MailSender(String smtp_server, int smtp_port) {
		this.smtp_server = smtp_server;
		this.smtp_port = smtp_port;
	}
	
	public void connect(String helo, PrintStream printer) throws Exception {
		// Crear socket client i connectar
		sck = new Socket(smtp_server, smtp_port);
		// Agafar streams d'entrad, sortida i sortida d'informació
		in = new BufferedReader(new InputStreamReader(sck.getInputStream()));
		out = new PrintStream(sck.getOutputStream(), true); // Autoflush
		this.printer = printer;
		// OK connexió
		read();
		// HELO
		writeRead("HELO " + helo);
	}
	
	public void send(EMail mail) throws Exception {
		// MAIL FROM:
		writeRead("MAIL FROM: <" + mail.getFrom() + ">");
		// RCPT TO:
		writeRead("RCPT TO: <" + mail.getTo() + ">");
		// DATA
		writeRead("DATA");
		// Header
		write("From: " + mail.getFrom());
		write("To: " + mail.getTo());
		write("Subject: " + mail.getSubject());
		write("");
		// Body
		write(mail.getBody());
		write("");
		writeRead(".");
	}

	public void close() throws Exception {
		writeRead("QUIT");
		in.close();
		out.close();
		printer = null;
		sck.close();
	}
	
	private void read() throws Exception {
		String line;
		do {
			line = in.readLine();
			if (printer != null) printer.println(">> " + line);
		} while (line != null && line.substring(3, 4).equals("-"));
	}
	
	private void write(String msg) throws Exception {
		out.println(msg);
		if (printer != null) printer.println("<< " + msg);
	}
	
	private void writeRead(String msg) throws Exception {
		write(msg);
		read();
	}


	
}
