import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Reseau implements Runnable {
	Avalam a;
	boolean connexionAcceptee;
	Socket sock;
	InputStream inputReseau; // on y lit
	PrintStream outputReseau;// on y écrit

	public Reseau(Avalam a, Socket sock) {
		connexionAcceptee =false;
		try {
			this.a = a;
			this.sock = sock;
			//sock.setSoTimeout(3000);
			a.j.partieEnReseau = true;
			inputReseau = sock.getInputStream();
			outputReseau = new PrintStream(sock.getOutputStream());
		} catch (Exception e) { System.out.println(e);  }
	}

	public void run() {
		while (!sock.isClosed()) {
			byte [] buffer = new byte [1024];
			int number;
			String s;
			try {
				number = inputReseau.read(buffer);
				s = new String(buffer);
				System.out.println(s);
				if (s.equals("connexionAcceptee"))
					     connexionAcceptee = true;

				while ((number = inputReseau.read(buffer)) != -1) {
					s = new String(buffer);
					String[] tokens = s.split("[ ]+");
					int lSrc, cSrc, lDest, cDest;
					lSrc = Integer.valueOf(tokens[0]);
					cSrc  = Integer.valueOf(tokens[1]);
					lDest  = Integer.valueOf(tokens[2]);
					cDest  = Integer.valueOf(tokens[3]);
					Coups c = new Coups(lSrc, cSrc, lDest, cDest);
					a.j.c = c;
					System.out.println("Coup " + c + " reçu");
					a.unpause();
				}
			} catch (java.net.SocketTimeoutException t) {
				System.out.println(t);
				try {
				sock.close();
				} catch (Exception e) {}
			} catch (Exception e) {

			}
		}
		System.out.println("Connection interropue");
	}
}
