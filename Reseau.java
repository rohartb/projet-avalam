import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.*;
import java.sql.*;

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
		} catch (Exception e) { e.printStackTrace();  }
	}

	public void run() {
		byte [] buffer = new byte [1024];
		int number;
		String s;

		try {
			if(type == CLIENT) {
				//si on est le client on attend d'abord la valid
				number = inputReseau.read(buffer);
				if (number > 0) {
				s = new String(buffer, 0, number);

					String[] tokens = s.split("[ ]+");
					int t = Integer.valueOf(tokens[0]);
					if (t == 1234) {
						System.out.println("Connexion acceptee par le serveur");
						outputReseau.print(a.j.J1.nom);
						number = inputReseau.read(buffer);
						s = new String(buffer,0,number);
						a.j.J2.nom = s;

						a.j.J1.type=Jeu.HUMAIN;
						a.j.J2.type=Jeu.RESEAU;
						a.f.es.actif = true;
						connexionAcceptee = true;

						a.f.g.pause.setEnabled(false);
						a.f.m.pause.setEnabled(false);
					}
				} else {
					//connexion refusée par le serveur
					JOptionPane.showMessageDialog(a.f,"Connexion refusée par l'hôte", "Connexion refusée", JOptionPane.ERROR_MESSAGE);
				}
			} else if (type == SERVEUR) {
				outputReseau.print(a.j.J2.nom);

				number = inputReseau.read(buffer);
				s = new String(buffer, 0, number);
				a.j.J1.nom = s;

				a.j.J1.type=Jeu.RESEAU ;
				a.j.J2.type=Jeu.HUMAIN ;

				a.f.g.pause.setEnabled(false);
				a.f.m.pause.setEnabled(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			sock.setSoTimeout(30000);
		} catch (Exception e) {
			e.printStackTrace();
		};

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
					a.unpause();
				}
			} catch (java.net.SocketTimeoutException t) {
				InterruptionReseau();
				try {
				sock.close();
				} catch (Exception e) {
					e.printStackTrace();
					InterruptionReseau();
				}
			} catch (Exception e) {
				e.printStackTrace();
				InterruptionReseau();
			}
		}
		System.out.println("Connection interrompue");
	}

	public void InterruptionReseau() {
		try {
		sock.close();
		} catch (Exception e) {e.printStackTrace();}

		String nom;
		if (type == SERVEUR)
			nom = a.j.J1.nom;
		else //if (type == CLIENT)
			nom = a.j.J2.nom;

		String[] options = {"Quitter le jeu", "relancer une partie", "Relancer une partie en réseau"};
		int rep = JOptionPane.showOptionDialog(null,
		                              "La connexion avec " + nom + "a été interrompue",
		                              "Fin de partie en réseau",
		                              JOptionPane.YES_NO_CANCEL_OPTION,
		                              JOptionPane.ERROR_MESSAGE,
		                              null,
		                              options,
		                              options[0]);
		if (rep == JOptionPane.YES_OPTION) {
			System.out.println("quitter le jeu");
		} else if (rep == JOptionPane.NO_OPTION) {
			System.out.println("nouvelle partie");
		} else if (rep == JOptionPane.CANCEL_OPTION) {
			System.out.println("relancer la connexion");
		}
		finReseau();
	}

	public void finDePartieReseau() {
		String nom;
		if (type == SERVEUR)
			nom = a.j.J1.nom;
		else //if (type == CLIENT)
			nom = a.j.J2.nom;

		String[] options = {"Quitter le jeu", "relancer une partie", "Relancer une partie en réseau"};
		int rep = JOptionPane.showOptionDialog(null,
		                              "Fin de partie en réseau",
		                              "Fin de partie en réseau",
		                              JOptionPane.YES_NO_CANCEL_OPTION,
		                              JOptionPane.ERROR_MESSAGE,
		                              null,
		                              options,
		                              options[0]);
		if (rep == JOptionPane.YES_OPTION) {
			System.out.println("quitter le jeu");
		} else if (rep == JOptionPane.NO_OPTION) {
			System.out.println("nouvelle partie");
		} else if (rep == JOptionPane.CANCEL_OPTION) {
			System.out.println("relancer la connexion");
		}
		finReseau();
	}

	public void finReseau() {
		a.partieReseauEnCours = false;
		a.f.g.pause.setEnabled(true);
		a.f.m.pause.setEnabled(true);
	}
}


