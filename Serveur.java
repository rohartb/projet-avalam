import javax.swing.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Serveur implements Runnable {
	Avalam a;
	boolean pasDeConnexion;

	public Serveur(Avalam a) {
		this.a = a;
		pasDeConnexion = true;
	}

	public void run() {
        try {
            ServerSocket listener = new ServerSocket(8100);
            boolean finished = false;

            System.out.println("Serveur en ecoute sur le port : " +
                               listener.getLocalPort());
            while (true) {
	            if (pasDeConnexion) {
		            Socket client = listener.accept();

		            a.interupt = true;
		            a.etatSuivant = a.PAUSE;

		            int choix = JOptionPane.showOptionDialog(null,client.getInetAddress().toString() + " souhaite jouer en réseau avec vous", "Requète de partie en réseau",
		                                                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/question.png"), null, null);
		            if (choix == JOptionPane.YES_OPTION) {
			            if (a.f.o.isVisible()){
				            a.f.o.fermerOptions();
			            }
			            if (a.f.app.isVisible()) {
				            a.f.app.fermerApparence();
			            }
			            pasDeConnexion = false;
			            a.r = new Reseau(a, client, Reseau.SERVEUR);
			            a.thReseau = new Thread(a.r);
			            a.thReseau.start();
			            a.j.J1.type = a.j.RESEAU; // c'est le client
			            a.j.J2.type = a.j.HUMAIN; // c'est nous

			            //on envoi au client la validation de la connexion
			            a.r.outputReseau.print("1234 1234");

			            a.interupt = true;
			            a.etatSuivant = a.RESEAU;
			            a.j.actualiser();
			            a.f.s.actualiser();
			            a.f.m.actualiser();
			            a.f.g.repaint();
		            } else if (choix == JOptionPane.NO_OPTION) {
			            System.out.println("Je ferme la connexion");
			            client.close();
		            }
	            }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
