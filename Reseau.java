import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Reseau implements Runnable {
	Avalam a;
	boolean connexionAcceptee;
	static final int SERVEUR= 0;
	static final int CLIENT = 1;
	int type;
	Socket sock;
	InputStream inputReseau; // on y lit
	PrintStream outputReseau;// on y écrit

	public Reseau(Avalam a, Socket sock, int type) {
		connexionAcceptee =false;
		try {
			this.a = a;
			this.sock = sock;
			this.type = type;
			a.j.partieEnReseau = true;
			inputReseau = sock.getInputStream();
			outputReseau = new PrintStream(sock.getOutputStream());
		} catch (Exception e) { System.out.println(e);  }
	}

	public void run() {
		byte [] buffer = new byte [1024];
		int number;
		String s;

		try {
		if(type == CLIENT) {
			//si on est le client on attend d'abord la valid
			number = inputReseau.read(buffer);
			s = new String(buffer);
			System.out.println("'" + s + "'" );
			System.out.println(s.equals("1234"));
			if (s.equals("connexionAcceptee")) {
				System.out.println("je reçois connexionAcceptee");
				connexionAcceptee = true;
				outputReseau.print(a.j.J1.nom);
				inputReseau.read(buffer);
				s = new String(buffer);
				a.j.J2.nom = s;
				System.out.println("Je change les noms");
			}
		} else if (type == SERVEUR) {
			inputReseau.read(buffer);
			s = new String(buffer);
			a.j.J1.nom = s;
			System.out.println("Je change les noms");
		}
		} catch (Exception e) {}

		while (!sock.isClosed()) {
			try {
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
