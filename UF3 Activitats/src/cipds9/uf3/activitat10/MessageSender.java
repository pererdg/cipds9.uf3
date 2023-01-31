package cipds9.uf3.activitat10;

import java.util.Scanner;

/**
 * Demana per consola les dades d'un correu electrònic,
 * es connecta a servidor SMTP i envia missatge.
 * 
 * @author perer
 *
 */
public class MessageSender {
	
	private static final String SMTP_SERVER = "smtp.freesmtpservers.com"; //"alt1.gmail-smtp-in.l.google.com"; 
	private static final int SMTP_PORT = 25;
	// El correu es pot veure a https://www.wpoven.com/tools/free-smtp-server-for-testing posant l'adreça del from o del to.
	
	public static void main(String[] args) throws Exception {
		
		EMail mail = new EMail();
		
		// Demanar dades per missatge
		Scanner in = new Scanner(System.in);
		System.out.println("From: ");
		mail.setFrom(in.nextLine());
		System.out.println("To: ");
		mail.setTo(in.nextLine());
		System.out.println("Subject: ");
		mail.setSubject(in.nextLine());
		System.out.println("Cos del missatge: ");
		mail.setBody(in.nextLine());
		in.close();

		/*
		mail.setFrom("...@inspalamos.cat");
		mail.setTo("...@gmail.com");
		mail.setSubject("Missatge de proves");
		mail.setBody("Hola\nProves de missatge.");
		*/
		
		// Enviar missatge
		System.out.println("Enviar email");
		MailSender mailSender = new MailSender(SMTP_SERVER, SMTP_PORT);
		mailSender.connect("alumne.inspalamos.cat", System.out);
		mailSender.send(mail);
		mailSender.close();
	}

}
