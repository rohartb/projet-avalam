import javax.swing.*;
import java.awt.Point;

public class Avalam{
	Fenetre f;
	Thread thFenetre;
	Terrain t;
	Jeu j;

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
	static final int APPARENCE=17;


	static final int QUITTER=42;


	int etat,etatSuivant;

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

	synchronized void pause(int time){
		try{
			this.wait(time);
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
				t = new Terrain();
				j = new Jeu(this);
				f = new Fenetre(this);
				thFenetre = new Thread(f);
				thFenetre.start();
				pause();
				//charger preferences
				etat = NOUVEAU;
				break;

				//nouvelle partie
			case NOUVEAU:
				j.init();
				t.init();
				System.out.println("nouveau");
				etatSuivant=JEU;
				etat = ACTUALISER;
				break;

				//verifier l'etat du jeu + attente d'un coup
			case JEU:
				System.out.println("jeu");
				System.out.println("joueur:"+j.joueurCourant);
				if(j.finPartie)
					etat=FIN;
				/*
				  if(courant==bot)
				  etat=BOT;
				  if(courant=resau)
				  etat=RESEAU;
				*/
				// on attend une interraction du joueur humain
				else{
					//atente de la fin du chargement de la fenetre
					pause();
				}
				//etat changé par les ecouteurs puis unpause()
				// SAUVEGARDER, CHARGER, PREFERENCES, OPTIONS, ANNLER,REFAIRE....
				break;

				//met le jeu en pause
				//TODO bloquer les clik souris sur le graphique
			case PAUSE:
				System.out.println("pause");
				j.pause=true;
				f.m.actualiser();
				pause();
				j.pause=false;
				break;

				//fin de partie
			case FIN:
				//popop (revoir,quitter,nouveau)
				System.out.println("fin");
				break;

				//TODO calcul du coup du bot dans jeu.c
			case BOT:
				System.out.println("bot");
				etat=JOUERAUTO;
				break;

				//TODO calcul un coup reseau dans jeu.c
			case RESEAU:
				System.out.println("reseau");
				etat=JOUERAUTO;
				break;

				//TODO annime un coup
			case JOUERAUTO:
				System.out.println("jouer auto");
				//anim
				etat=ACTUALISER;
				break;

			case JOUERMANU:
				System.out.println("jouer manu");
				System.out.println("attente release");
				//TODO annimation dans le thread fenetre
				pause();
				j.calculerCoup();
				if(t.estPossible(j.c)){
					etat=JOUER;
				}else{
					if (f.g.release.x != f.g.click.x)
						f.g.animationRetourPion();
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
				System.out.println("charger");
				//TODO on charge le terrain + historique + temps + parametres
				//if(!j.finPartie){
				//
				//}
				// puis on joue directement
				etat=ACTUALISER;
				break;


			//TODO lors de la fermeture de la popup sauver soit
			case SAUVER:
				System.out.println("sauver");
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
				etat=etatSuivant;
				break;

			case APPARENCE:
				System.out.println("apparence");
				f.app.afficherApparence();
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
				//calcul score
				//calcul fin de partie
				f.m.actualiser();
				f.g.repaint();
				j.nbCoupsRestants = t.nbDeplRestant();
				f.s.actualiser();
				etat=etatSuivant;
				break;

			case QUITTER:
				System.out.println("quitter");
				System.out.println("popop sauvegarder ?");
				System.exit(0);
				break;
			}
		}
	}
}
