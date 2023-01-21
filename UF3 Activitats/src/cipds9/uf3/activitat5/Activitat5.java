package cipds9.uf3.activitat5;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Activitat5 {

	public static void main(String[] args) throws Exception {
		URL url = new URL("https://www.google.es");
		
		printURL(url);
		
		URL url2 = new URL(url, "/images/branding/googleg/1x/googleg_standard_color_128dp.png");
		
		writeURL(url2);
	}

	private static void printURL(URL url) throws Exception {
		URLConnection con = url.openConnection();
		System.out.println(con.getContentType());
		
		InputStreamReader in = new InputStreamReader(con.getInputStream());
		
		char[] c = new char[1024];
		int n;
		while ((n=in.read(c)) != -1) {
			System.out.print(String.copyValueOf(c, 0, n));
		}
	}

	private static void writeURL(URL url) throws Exception {
		URLConnection con = url.openConnection();
		System.out.println(con.getContentType());
		
		InputStream in = con.getInputStream();
		OutputStream out = new FileOutputStream("./imatge.png");
		
		byte[] b = new byte[1024];
		int n;
		while ((n=in.read(b)) != -1) {
			out.write(b, 0, n);
		}
		out.close();
	}
}
