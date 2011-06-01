import javax.swing.*;
import java.awt.Point;
import java.io.File;
import java.net.*;

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


	//popups
	//pour les popup on reviens a l'etat sauvegardé dans etatSuivant
	static final int CHARGER=100;
	static final int SAUVER=13;
	static final int FIN=14;
	static final int OPTIONS=15;
	static final int CONNEXION=16;
	static final int APPARENCE=17;
	static final int REGLE = 24;

	static final int QUITTER=42;


	int etat,etatSuivant,etatPause;

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
				serv = new Serveur();
				thServeur = new Thread(serv);
				thServeur.start();
				thFenetre = new Thread(f);
				thFenetre.start();
				pause();
				//charger preferences
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

			case CONNEXION:
				String result = JOptionPane.showInputDialog(null, "Entrez l'ip de notre adversaire");
				try {
				InetAddress addr = InetAddress.getByName(result);
				int port = 8100;
				Socket sock = new Socket(addr, port);
				r = new Reseau(sock);
				thReseau = new Thread(r);
				thReseau.start();
				} catch (Exception e) {
					System.out.println(e);
				}
				// celui qui établie la connexion devient J1
				// a.j.J1 = Jeu.HUMAIN;
				// a.j2.J2 = Jeu.RESEAU;
				// on active le mode match ?

				etat = JEU;
			case JEU:
				System.out.println("jeu");
				if(j.finPartie){
					etat=FIN;
					break;
				}
				if(j.joueurCourant==1){
					if(j.J1.type>0){
				  		etat=BOT;
				  		break;
				  	}
				}
				if(j.joueurCourant==2){
					if(j.J2.type>0){
				  		etat=BOT;
				  		break;
				  	}
				}
				//  if(courant=resau)
				//  etat=RESEAU;

				// on attend une interraction du joueur humain
					//atente de la fin du chargement de la fenetre
				pause();
				//etat changé par les ecouteurs puis unpause()
				// SAUVEGARDER, CHARGER, PREFERENCES, OPTIONS, ANNLER,REFAIRE....
				break;

				//met le jeu en pause
				//TODO bloquer les clik souris sur le graphique
			case PAUSE:
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
				// Calcul du score
				int nbJ2 = 0;
				int nbJ1 = 0;
				for (int i = 0; i < t.TAB_SIZE ; i++) {
					for (int j = 0; j < t.TAB_SIZE ; j++) {
						if (t.plateau[i][j].estOccupee()) {
							if (t.plateau[i][j].estJ1())
								nbJ1++;
							if (t.plateau[i][j].estJ2())
								nbJ2++;
						}
					}
				}
				String vainqueur;

				// Affichage du la popup
				if (nbJ1 == nbJ2) {
					JOptionPane.showMessageDialog(null,"Personne ne gagne !","Egalité",JOptionPane.INFORMATION_MESSAGE);
				} else {
					if((j.J1.estRobot() && j.J2.estHumain()) ||
					   (j.J1.estRobot() && j.J1.estHumain())) {
						if ((nbJ2 > nbJ1 && j.J2.estRobot()) ||
						    (nbJ2 < nbJ1 && j.J2.estRobot())){
							JOptionPane.showMessageDialog(null,"Vous avez perdu! \n Score :  "+nbJ1+" - "+nbJ2, "Défaite" ,JOptionPane.INFORMATION_MESSAGE);
						}
						else{
							JOptionPane.showMessageDialog(null,"Vous avez gagné! \n Score : "+nbJ1+" - "+nbJ2, "Victoire" ,JOptionPane.INFORMATION_MESSAGE);
						}
					}else{
						if (nbJ2 > nbJ1)
							vainqueur = j.J2.nom;
						else // nbJ1 > nbJ2
							vainqueur = j.J1.nom;

						JOptionPane.showMessageDialog(null,vainqueur +
						                              " remporte la partie ! \n Score :  "+nbJ1+" - "+nbJ2, "Victoire" ,JOptionPane.INFORMATION_MESSAGE);
					}
				}
				System.out.println("Score :  "+nbJ1+" - "+nbJ2);
				pause();
				break;

				//TODO calcul du coup du bot dans jeu.c
			case BOT:
				System.out.println("bot");
				j.jouerBot();
				etat=JOUERAUTO;
				break;

				//TODO calcul un coup reseau dans jeu.c
			case RESEAU:
				System.out.println("reseau");
				etat=JOUERAUTO;
				break;

			case JOUERAUTO:
				System.out.println("jouer auto");
				f.g.animationPionAuto();
				etat=JOUER;
				break;

			case JOUERMANU:
				System.out.println("jouer manu");
				System.out.println("attente release");
				pause();
				j.calculerCoup();
				System.out.println("calculOK");
				if(t.estPossible(j.c)){
					etat=JOUER;
				}else{
					f.g.animationPion(f.g.release,f.g.click);
					etat=JEU;
				}
				break;

				//equivalent du jouercoup
				//joue le coup+ajoute a l'historique+ change le joueur courrant
			case JOUER:
				ElemHist e = new ElemHist(j.c,t);
				j.h.ajouterAnnuler(e);
				j.h.viderRejouer();
				t.deplacer(j.c);
				etat=ACTUALISER;
				j.changerJoueur();
				break;

				//TODO methode charger
				// demander si sauver si j.finPartie==false
			case CHARGER:
				//System.out.println("charger");
				s.charger();
				etat=ACTUALISER;
				break;


			//TODO lors de la fermeture de la popup sauver soit
			case SAUVER:
				//System.out.println("sauver");
				s.sauver();
				etat=etatSuivant;
				break;

			case ABANDONNER:
				System.out.println("abandonner");
				etat=FIN;
				break;

			case OPTIONS:
				System.out.println("options");
				f.o.afficherOptions();
				//pause();
				etat=ACTUALISER;
				break;

			case APPARENCE:
				System.out.println("apparence");
				f.app.afficherApparence();
				etat=etatSuivant;
				break;

			case REGLE:
				System.out.println("regle");
				f.r.afficherRegle();
				etat=etatSuivant;
				break;

				//annule 1 coup
				//TODO si 1 joueur ordi, annuler son coup aussi
			case ANNULER:
				System.out.println("annuler");
				ElemHist eha = j.h.annuler();
				j.h.ajouterRejouer(eha);
				t.annuler(eha);
				etat=ACTUALISER;
				break;

				//rejoue 1 coup
				//TODO si 1 joueur ordi
			case REJOUER:
				System.out.println("rejouer");
				ElemHist ehr = j.h.rejouer();
				j.h.ajouterAnnuler(ehr);
				t.deplacer(new Coups(new Point(ehr.lSource,ehr.cSource),new Point(ehr.lDest,ehr.cDest)));
				etat=ACTUALISER;
				break;

			case ACTUALISER:
				System.out.println("actualiser");
				j.actualiser();
				f.g.repaint();
				f.s.actualiser();
				f.m.actualiser();
				etat=etatSuivant;
				break;

			case QUITTER:
				//System.out.println("quitter");
				//System.out.println("popop sauvegarder ?");
				f.s.timer.stop();
				if(!j.finPartie && !quit){
					String[] options = {"Sauvegarder" , "Quitter sans sauvegarder" , "Annuler"};
					int choix  = JOptionPane.showOptionDialog(null, "Quitter Avalam :\n voulez-vous sauvegarder la partie en cours", "Sauvegarder ?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/question.png"), options, options[0] );
					if (choix == JOptionPane.YES_OPTION) {
						etat=SAUVER;
						etatSuivant=QUITTER;
						quit=true;
					}else if (choix == JOptionPane.NO_OPTION) {
						System.exit(0);
					}else{
						f.s.timer.start();
						etat=JEU;
					}
				}else{
					System.exit(0);
				}
				break;
			}
		}
	}
}