import javax.swing.*;
import java.awt.Point;
import java.io.File;
import java.net.*;
import java.util.*;

public class Avalam{
	Fenetre f;
	Thread thFenetre;
	Thread thServeur;
	Thread thReseau;
	Terrain t;
	Jeu j;
	Reseau r;
	Sauvegarde s;
	Serveur serv;
	boolean quit;

	static final int INIT=-1;

	static final int NOUVEAU=1;
	static final int JEU=2;
	static final int PAUSE=3;
	static final int BOT=4;
	static final int RESEAU=5;
	static final int JOUERAUTO=6;
	static final int JOUERMANU=7;
	static final int JOUER=8;
	static final int ANNULER=9;
	static final int REJOUER=10;
	static final int ABANDONNER=11;
	static final int ACTUALISER=12;
	static final int HISTORIQUE=18;
	static final int DERNIERCOUP=19;


	//popups
	//pour les popup on reviens a l'etat sauvegardé dans etatSuivant
	static final int CHARGER=100;
	static final int SAUVER=13;
	static final int FIN=14;
	static final int OPTIONS=15;
	static final int CONNEXION=16;
	static final int APPARENCE=17;
	static final int REGLE = 24;
	static final int ASTUCE = 25;

	static final int QUITTER=42;


	int etat,etatSuivant,etatPause;
	boolean interupt;

	public static void main(String[] args) {
		new Avalam();
	}

	synchronized void pause(){
		try{
			//System.out.println("pause");
			this.wait();
		}catch(InterruptedException e){
			System.out.println(e);
		}
	}
	synchronized void pause(int n){
		try{
			//System.out.println("pause");
			this.wait(n);
		}catch(InterruptedException e){
			System.out.println(e);
		}
	}
	synchronized void unpause(){
		try{
			//System.out.println("unpause");
			this.notify();
		}catch(IllegalMonitorStateException e){
			System.out.println(e);
		}
	}

	public Avalam(){
		etat=Avalam.INIT;
		interupt=false;

		//automate d'etats
		while(true){
			switch(etat){

			//initialisation
			case INIT:
				System.out.println("init");
				//creation du dossier .Avalam s'il n'existe pas
				File dossier = new File(System.getProperty("user.home")+"/.Avalam");
				if(!dossier.exists())
					dossier.mkdir();
				t = new Terrain();
				j = new Jeu(this);
				f = new Fenetre(this);
				s = new Sauvegarde(this);
				quit=false;
				serv = new Serveur(this);
				thServeur = new Thread(serv);
				thServeur.start();
				thFenetre = new Thread(f);
				thFenetre.start();
				pause();
				etat = NOUVEAU;
				break;

			//nouvelle partie
			case NOUVEAU:
				//TODO popup voulez vous sauvegarder votre partie en cours?
				System.out.println("nouveau");
				j.init();
				t.init();
				etatSuivant=JEU;
				etat = ACTUALISER;
				break;
				//verifier l'etat du jeu + attente d'un coup

				//init reseau
			case CONNEXION:
				String ip=null;
				try {
					Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
					if (interfaces.hasMoreElements()) {
						NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
						Enumeration<InetAddress> addresses = ni.getInetAddresses();
						while (addresses.hasMoreElements()) {
							InetAddress address = (InetAddress) addresses.nextElement();

							ip = "" + address.getHostAddress();
							if (ip.matches("[0-9]*.[0-9]*.[0-9]*.[0-9]*"))
								break;
						}
					}
					if (ip.equals("127.0.0.1")) {
						JOptionPane.showMessageDialog(null,"Vérifiez votre connexion réseau.\nPartie en réseau impossible.", "Attention connexion" ,JOptionPane.WARNING_MESSAGE);
					} else {
						String result = JOptionPane.showInputDialog(null, "Entrez l'ip de notre adversaire\nVotre ip à contacter : " + ip);
						if (result != null) {
							System.out.println("tutu");
							InetAddress addr = InetAddress.getByName(result);
							int port = 8100;
							Socket sock = new Socket(addr, port);
							r = new Reseau(sock);
							thReseau = new Thread(r);
							thReseau.start();
						}
					}
					// celui qui établie la connexion devient J1
					// a.j.J1 = Jeu.HUMAIN;
					// a.j2.J2 = Jeu.RESEAU;
					// on active le mode match ?
					etat = JEU;
				} catch (Exception e) {
					System.out.println(e);

				}

			//verif si fin de partie ou atends un coup a jouer
			case JEU:
				System.out.println("jeu");
				f.es.actif = true;
				if(interupt){
					etat=etatSuivant;
					interupt=false;
					break;
				}
				if(j.finPartie){
					etat=FIN;
					break;
				}
				if(j.courantEstRobot()){
					f.es.actif = false;
				  	etat=BOT;
				  	break;
				}
				pause();
				if(interupt){
					etat=etatSuivant;
					interupt=false;
				}else{
					etat=JOUERMANU;
				}
				break;
				// on attend une interraction du joueur humain
				//atente de la fin du chargement de la fenetre

				//etat changé par les ecouteurs puis unpause()
				// SAUVEGARDER, CHARGER, PREFERENCES, OPTIONS, ANNLER,REFAIRE....

			//met le jeu en pause
			case PAUSE:
				//TODO bloquer les clik souris sur le graphique
				System.out.println("pause");
				j.pause=true;
				etatPause=etatSuivant;
				f.m.actualiser();
				pause();
				j.pause=false;
				etatSuivant=etatPause;
				etat=ACTUALISER;
				break;

			//fin de partie
			case FIN:
				//popop (revoir,quitter,nouveau)
				System.out.println("fin");
				f.s.timer.stop();
				f.popupFinDePartie();
				etat=etatSuivant;
				break;

			//calcul un coup a partir du bot
			case BOT:
				f.activerAnnulerRefaire(false);
				System.out.println("bot");
				j.jouerBot();
				etat=JOUERAUTO;
				break;

			//calcul un coup reseau dans jeu.c
			case RESEAU:
				//TODO
				System.out.println("reseau");
				etat=JOUERAUTO;
				break;

			//animation du coup du bot ou reseau
			case JOUERAUTO:
				System.out.println("jouer auto");
				f.g.animationPionAuto();
				etat=JOUER;
				break;

			//animation du coup manuel
			case JOUERMANU:
				System.out.println("jouer manu");
				pause();
				j.calculerCoup();
				if(t.estPossible(j.c)){
					etat=JOUER;
				}else{
					if(t.okAnim(f.g.coordToIndice(f.g.click))){
						f.g.animationPion(f.g.release,f.g.click);
					}
					etat=JEU;
				}
				break;


			//joue le coup+ajoute a l'historique+ change le joueur courrant
			case JOUER:
				System.out.println("jouer");
				ElemHist e = new ElemHist(j.c,t);
				j.h.ajouterAnnuler(e);
				j.h.viderRejouer();
				t.deplacer(j.c);
				etat=ACTUALISER;
				j.changerJoueur();
				//r.outputReseau.print("coucou");
				break;

			//TODO popop sauvegarder avant charger
			case CHARGER:
				System.out.println("charger");
				f.s.timer.stop();
				s.afficherCharger();
				if(!j.revoirH)
					f.s.timer.start();
				etat=etatSuivant;
				break;

			//popop de sauvearde
			case SAUVER:
				System.out.println("sauver");
				f.s.timer.stop();
				s.sauver();
				if(!j.revoirH)
					f.s.timer.start();
				etat=JEU;
				break;

			//TODO
			case ABANDONNER:
				System.out.println("abandonner");
				etat=FIN;
				break;

			case OPTIONS:
				System.out.println("options");
				f.s.timer.stop();
				f.o.afficherOptions();
				//pause();
				if(!j.revoirH)
					f.s.timer.start();
				etat=etatSuivant;
				break;

			case APPARENCE:
				System.out.println("apparence");
				f.s.timer.stop();
				f.app.afficherApparence();
				f.s.timer.start();
				etat=JEU;
				break;


			case REGLE:
				System.out.println("regle");
				f.s.timer.stop();
				f.r.afficherRegle();
				if(!j.revoirH)
					f.s.timer.start();
				etat=JEU;
				break;


			case ASTUCE:
				System.out.println("astuces");
				f.s.timer.stop();
				f.as.afficherAstuces();
				f.s.timer.start();
				etat=JEU;
				break;

			//annule 1 coup
			case ANNULER:
				//grisage des fonctions et boutons annuler, rejouer et dernierCoup pendant le deplacement
				f.activerAnnulerRefaire(false);

				//si 1 joueur ordi ET !finPartie, annuler 2 coups
				if( (j.J1.estRobot() || j.J2.estRobot()) && !j.revoirH){
					ElemHist coupOrdi = j.h.annuler();
					j.h.ajouterRejouer(coupOrdi);
					t.annuler(coupOrdi);
					j.c = new Coups(new Point(coupOrdi.lSource,coupOrdi.cSource),new Point(coupOrdi.lDest,coupOrdi.cDest));
					f.g.animationPionAnnuler();

					ElemHist coupJoueur = j.h.annuler();
					j.h.ajouterRejouer(coupJoueur);
					t.annuler(coupJoueur);
					j.c = new Coups(new Point(coupJoueur.lSource,coupJoueur.cSource),new Point(coupJoueur.lDest,coupJoueur.cDest));
					f.g.animationPionAnnuler();
				}else{
					ElemHist coup = j.h.annuler();
					j.h.ajouterRejouer(coup);
					t.annuler(coup);
					j.c = new Coups(new Point(coup.lSource,coup.cSource),new Point(coup.lDest,coup.cDest));
					f.g.animationPionAnnuler();
					j.changerJoueur();
				}
				etat=ACTUALISER;
				break;

				//rejoue 1 coup
				//TODO si 1 joueur ordi
			case REJOUER:

				//grisage des fonctions et boutons annuler, rejouer et dernierCoup pendant le deplacement
				f.activerAnnulerRefaire(false);
				if( (j.J1.estRobot() || j.J2.estRobot()) && !j.revoirH){
					ElemHist coupJoueur = j.h.rejouer();
					j.h.ajouterAnnuler(coupJoueur);
					j.c = new Coups(new Point(coupJoueur.lSource,coupJoueur.cSource),new Point(coupJoueur.lDest,coupJoueur.cDest));
					f.g.animationPionAuto();
					t.deplacer(j.c);

					ElemHist coupOrdi = j.h.rejouer();
					j.h.ajouterAnnuler(coupOrdi);
					j.c = new Coups(new Point(coupOrdi.lSource,coupOrdi.cSource),new Point(coupOrdi.lDest,coupOrdi.cDest));
					f.g.animationPionAuto();
					t.deplacer(j.c);
				}else{
					ElemHist coup = j.h.rejouer();
					j.h.ajouterAnnuler(coup);
					j.c = new Coups(new Point(coup.lSource,coup.cSource),new Point(coup.lDest,coup.cDest));
					f.g.animationPionAuto();
					t.deplacer(j.c);
					j.changerJoueur();
				}
				etat=ACTUALISER;
				break;

			case ACTUALISER:
				System.out.println("actualiser");
				j.actualiser();
				f.g.repaint();
				f.s.actualiser();
				f.m.actualiser();
				if(!j.revoirH){
					etat=JEU;
				}else{
					etat=HISTORIQUE;
				}
				break;

			case QUITTER:
				System.out.println("quitter");
				System.out.println("popop sauvegarder ?");
				f.s.timer.stop();
				if(!j.finPartie && !quit){
					String[] options = {"Sauvegarder" , "Quitter sans sauvegarder" , "Annuler"};
					int choix  = JOptionPane.showOptionDialog(null, "Quitter Avalam :\n voulez-vous sauvegarder la partie en cours", "Sauvegarder ?",
					 				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/question.png"), options, options[0] );
					if (choix == JOptionPane.YES_OPTION) {
						etat=SAUVER;
						etatSuivant=QUITTER;
						quit=true;
					}else if (choix == JOptionPane.NO_OPTION) {
						System.exit(0);
					}else{
						if(!j.revoirH){
							f.s.timer.start();
							etat=JEU;
						}else{
							etat=HISTORIQUE;
						}
					}
				}else{
					System.exit(0);
				}
				break;

			case HISTORIQUE:
				System.out.println("historique");
				pause();
				etat=etatSuivant;
				break;

			case DERNIERCOUP:
				System.out.println("dernierCoup");
				ElemHist dernierCoup = j.h.annuler();
				j.h.ajouterRejouer(dernierCoup);
				t.annuler(dernierCoup);

				dernierCoup = j.h.rejouer();
				j.h.ajouterAnnuler(dernierCoup);
				j.c = new Coups(new Point(dernierCoup.lSource,dernierCoup.cSource),new Point(dernierCoup.lDest,dernierCoup.cDest));
				f.g.animationPionAuto();
				t.deplacer(j.c);
				etat=ACTUALISER;				

			}
		}
	}
}
