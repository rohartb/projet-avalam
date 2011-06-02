import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Reseau implements Runnable {
	Socket sock;
	InputStream inputReseau; // on y lit
	PrintStream outputReseau;// on y écrit

	public Reseau(Socket sock) {
		try {
			this.sock = sock;
			inputReseau = sock.getInputStream();
			outputReseau = new PrintStream(sock.getOutputStream());
		} catch (Exception e) { System.out.println(e);  }
	}

	public void run() {
		System.out.println("Un nouveau client est connecté, son ip: " + sock.getInetAddress().toString());
		while (true) {
			byte [] buffer = new byte [1024];
			int number;
			try {
				while ((number = inputReseau.read(buffer)) != -1) {
					System.out.write(buffer, 0, number);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
