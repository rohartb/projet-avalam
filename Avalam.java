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
    Match ma;
	Sauvegarde s;
	Serveur serv;
	boolean quit;
	boolean save;
    boolean match;
	boolean partieEnCours;

	static final int INIT=-1;

	static final int NOUVEAU=1;
	static final int JEU=2;
	static final int PAUSE=3;
	static final int BOT=4;
	static final int RESEAU=5;
	static final int AIDE=30;
	static final int JOUERAUTO=6;
	static final int JOUERMANU=7;
	static final int JOUER=8;
	static final int ANNULER=9;
	static final int REJOUER=10;
	static final int ABANDONNER=11;
	static final int ACTUALISER=12;
	static final int HISTORIQUE=18;
	static final int DERNIERCOUP=19;
    static final int MATCH=74;
    static final int NOUVEAUMATCH=76;

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
	boolean interupt,aide;

	public static void main(String[] args) {
		new Avalam();
	}

	synchronized void pause(){
		try{
			this.wait();
		}catch(InterruptedException e){
			System.out.println(e);
		}
	}
	synchronized void pause(int n){
		try{
			this.wait(n);
		}catch(InterruptedException e){
			System.out.println(e);
		}
	}
	synchronized void unpause(){
		try{
			this.notify();
		}catch(IllegalMonitorStateException e){
			System.out.println(e);
		}
	}

	public Avalam(){
		etat=Avalam.INIT;
		interupt=false;
		aide=false;

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
                j.modeNormal = true;
				quit=false;
                match=false;
                partieEnCours = false;
				serv = new Serveur(this);
				thServeur = new Thread(serv);
				thServeur.start();
				thFenetre = new Thread(f);
				thFenetre.start();
				pause();//atente du unpause lorsque la fenetre a fini de charger
				f.o.afficherOptions();
				if (etat == RESEAU) {
					// Si il y une demande de connexion
					etat = JEU;
					j.init();
					t.init();
					j.J1.type = Jeu.HUMAIN;
				} else if (j.modeNormal)
					etat = NOUVEAU;
				else
					etat = MATCH;
				break;

			//nouvelle partie
			case NOUVEAU:
				System.out.println("nouveau");
				if(!j.finPartie && !save && j.nbCoupsRestants!=292 && j.modeNormal && partieEnCours){
					String[] options = {"Sauvegarder" , "Nouveau jeu sans sauvegarder" , "Annuler"};
					int choix  = JOptionPane.showOptionDialog(null, "Nouveau jeu :\n voulez-vous sauvegarder la partie en cours", "Sauvegarder ?",
					 				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/question.png"), options, options[0] );
					if (choix == JOptionPane.YES_OPTION) {
						System.out.println("save");
						etat=SAUVER;
						etatSuivant=NOUVEAU;
						save=true;
					}else if (choix == JOptionPane.NO_OPTION) {
						System.out.println("new without save");
						j.init();
						t.init();
						save=false;
						etatSuivant=JEU;
						etat = ACTUALISER;
					}else{

						//annuler
						System.out.println("annuler");
						etat=JEU;
					}
				} else if (!j.modeNormal) { // on fait nouveau en mode match
 int choix  = JOptionPane.showOptionDialog(f, "Quitter Avalam :\n Etes-vous sur de vouloir quitter le match ?", "Quitter",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/question.png"),null,null);
                    if (choix == JOptionPane.YES_OPTION) {
	                    ma.finDeMatch();
	                    etat = NOUVEAU;
                    } else {
                        if(!j.revoirH){
                            f.s.timer.start();
                            etat=JEU;
                        }else{
                            etat=HISTORIQUE;
                        }
                    }
				}else{
					j.init();
					t.init();
					save=false;
					etatSuivant=JEU;
					etat = ACTUALISER;
				}
				break;
				//verifier l'etat du jeu + attente d'un coup


            case MATCH:
	            System.out.println("match");
	            if(partieEnCours && !j.finPartie && !match){
		            String[] options = {"Sauvegarder", "Continuer sans sauvegarder", "Annuler"};
		            int choix  = JOptionPane.showOptionDialog(f, "Nouveau match :\n voulez-vous sauvegarder la partie en cours", "Sauvegarder ?",
		                                                      0, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/question.png"), options, options[0] );
		            if (choix == JOptionPane.YES_OPTION) {
			            etat=SAUVER;
			            etat = NOUVEAUMATCH;
		            } else if (choix == JOptionPane.NO_OPTION) {
			            // continuer sans sauvegarder
			            etat=NOUVEAUMATCH;
		            } else {//annuler
			            j.modeNormal = true;
			            if(!j.revoirH){
				            f.s.timer.start();
				            etat=JEU;
			            }else{
				            etat=HISTORIQUE;
			            }
		            }
	            } else {
		            etat = NOUVEAUMATCH;
	            }
	            break;

            case NOUVEAUMATCH:
                    //TODO popup de match
                    System.out.println("nouveaumatch");
                    match=true;
                    ma = new Match(this);
                    ma.debutMatch();
                    f.activerAnnulerRefaire(false);
					j.init();
					t.init();
					f.s.actualiser();
					save=false;
					etat=JEU;
                    break;


			case CONNEXION:
				String ip=null;
				try {
					Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
					if (interfaces.hasMoreElements()) {
						NetworkInterface ni = interfaces.nextElement();
						Enumeration<InetAddress> addresses = ni.getInetAddresses();
						while (addresses.hasMoreElements()) {
							InetAddress address =  addresses.nextElement();

							ip = "" + address.getHostAddress();
							if (ip.matches("[0-9]*.[0-9]*.[0-9]*.[0-9]*"))
								break;
						}
					}
					if (ip != null && ip.equals("127.0.0.1")) {
						JOptionPane.showMessageDialog(null,"Vérifiez votre connexion réseau.\nPartie en réseau impossible.", "Attention connexion" ,JOptionPane.WARNING_MESSAGE);
					} else {
						ip = (String) JOptionPane.showInputDialog(null,
							"Entrez l'ip de notre adversaire\nVotre ip à contacter : " + ip,
							"lala",
							JOptionPane.INFORMATION_MESSAGE,
							new ImageIcon("./images/reseau.png"),
							null,
							null
							);
						if (ip != null && ip.matches("[0-9]*.[0-9]*.[0-9]*.[0-9]*")) {
							System.out.println("Connexion réseau !");
							InetAddress addr = InetAddress.getByName(ip);
							int port = 8100;
							Socket sock = new Socket(addr, port);
							r = new Reseau(this, sock, Reseau.CLIENT);
							thReseau = new Thread(r);
							thReseau.start();
							// celui qui établie la connexion devient J1
							j.J1.type = Jeu.HUMAIN;
							j.J2.type = Jeu.RESEAU;

							//attente de la réponse
							sock.setSoTimeout(10000);
							while(! r.connexionAcceptee) {};
							System.out.println("La demande de connexion a été accpetée");
							sock.setSoTimeout(0); // desactive le timeout

						} else {
							System.out.println("Pas de connexion");
						}
					}
					// on active le mode match ?
					etat = JEU;
				} catch (NoRouteToHostException nt) {
					JOptionPane.showMessageDialog(f, "Hôte inconnu, connexion impossible", "Connexion impossible", JOptionPane.ERROR_MESSAGE);
					etat = JEU;
				} catch (Exception e) {
					System.out.print("\t Avalam : ");
					e.printStackTrace();
				} finally {
					break;
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
				} else if (j.courantEstReseau()){
					f.es.actif = false;
					etat=RESEAU;
					break;
				}
				f.g.labelAmpoule.setEnabled(true);
				pause();
				if(interupt){
					etat=etatSuivant;
					interupt=false;
				}else if(aide){
					aide=false;
					etat=AIDE;
				}else{
					etat=JOUERMANU;
				}
				break;
				// on attend une interraction du joueur humain
				//atente de la fin du chargement de la fenetre

				//etat changé par les ecouteurs puis unpause()
				//SAUVEGARDER, CHARGER, PREFERENCES, OPTIONS, ANNLER,REFAIRE....

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
				partieEnCours = false; // la partie est finie
				f.s.timer.stop();
                if(j.modeNormal){
                    f.popupFinDePartie();
                    etat=etatSuivant;
                } else {
	                ma.nbPartiesJouees++;
	                if(ma.nbPartiesJouees != ma.nbPartiesTotales) {
		                ma.popupFinDePartie();
	                } else {
		                ma.popupFinDeMatch();
		                ma.finDeMatch();
		                ma.finMatch = true;

	                }
                }
				break;

			//calcul un coup a partir du bot
			case BOT:
				f.activerAnnulerRefaire(false);
				System.out.println("bot");
				//f.g.labelAmpoule.setEnabled(false);
				j.jouerBot();
				etat=JOUERAUTO;
				break;

			//calcul un coup reseau dans jeu.c
			case RESEAU:
				//TODO
				System.out.println("reseau");
				pause();
				etat=JOUERAUTO;
				break;

			case AIDE:
				System.out.println("aide");
				f.g.labelAmpoule.setEnabled(false);
				f.es.actif = false;
				j.jouerBot();
				f.g.animationPionAuto();
				f.g.animationPionAnnuler();
				f.g.repaint();
				f.g.labelAmpoule.setEnabled(true);
				f.es.actif = true;
				etat=JEU;
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
				f.g.labelAmpoule.setEnabled(false);
				j.calculerCoup();
				if(t.estPossible(j.c)){
					etat=JOUER;
				}else{
					if(t.okAnim(f.g.coordToIndice(f.g.click))){
						f.g.animationPion(f.g.release,f.g.click);
						f.g.repaint();
					}
					etat=JEU;
				}
				break;


			//joue le coup+ajoute a l'historique+ change le joueur courrant
			case JOUER:
				System.out.println("jouer");
				partieEnCours = true;
				ElemHist e = new ElemHist(j.c,t);
				j.h.ajouterAnnuler(e);
				j.h.viderRejouer();
				t.deplacer(j.c);
				etat=ACTUALISER;
				if (j.partieEnReseau && j.courantEstHumain()) { // on envoi que si le coup est joué par le joueur
					System.out.println("j'envoie mon coup sur le réseau");
					r.outputReseau.print(j.c.toString());
				}
				j.changerJoueur();
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
				if(save){
					etat=NOUVEAU;
				}else if(j.revoirH){
					etat=HISTORIQUE;
				}else if(quit){
					etat=QUITTER;
				}else{
					etat=JEU;
				}
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
				if(!j.revoirH)
					f.s.timer.start();
				System.out.println("" + etatSuivant);
				etat=etatSuivant;
				break;

			case APPARENCE:
				System.out.println("apparence");
				f.s.timer.stop();
				f.app.afficherApparence();
				if(!j.revoirH)
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

				System.out.println("annuler");
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
				System.out.println("rejouer");
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
				f.s.actualiser();
				f.m.actualiser();
				f.g.repaint();
				if(!j.revoirH){
					etat=JEU;
				}else{
					etat=HISTORIQUE;
				}
				break;

			case QUITTER:
				System.out.println("quitter");
				f.s.timer.stop();
                if(j.modeNormal){
                    if(!j.finPartie && !quit){
                        String[] options = {"Sauvegarder" , "Quitter sans sauvegarder" , "Annuler"};
                        int choix  = JOptionPane.showOptionDialog(f, "Quitter Avalam :\n voulez-vous sauvegarder la partie en cours", "Sauvegarder ?",
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
                } else {
                    int choix  = JOptionPane.showOptionDialog(f, "Quitter Avalam :\n Etes-vous sur de vouloir quitter le match ?", "Quitter",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/question.png"),null,null);
                    if (choix == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    } else {
                        if(!j.revoirH){
                            f.s.timer.start();
                            etat=JEU;
                        }else{
                            etat=HISTORIQUE;
                        }
                    }
                }
				break;

			case HISTORIQUE:
				System.out.println("historique");
				f.es.actif=false;
				pause();
				if(etatSuivant==ACTUALISER){
					j.revoirH=false;
					etat=JEU;
				}else{
					etat=etatSuivant;
				}
				break;

			case DERNIERCOUP:
				System.out.println("dernierCoup");
				ElemHist dernierCoup = j.h.annuler();
				j.h.ajouterRejouer(dernierCoup);
				t.annuler(dernierCoup);

				dernierCoup = j.h.rejouer();
				j.h.ajouterAnnuler(dernierCoup);
				j.c = new Coups(new Point(dernierCoup.lSource,dernierCoup.cSource),new Point(dernierCoup.lDest,dernierCoup.cDest));
				f.g.animationPionAnnuler();
				f.g.animationPionAuto();
				t.deplacer(j.c);
				etat=ACTUALISER;

			}
		}
	}
}
