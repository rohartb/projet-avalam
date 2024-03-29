import javax.swing.*;
import java.awt.Point;
import java.io.File;
import java.net.*;
import java.util.*;
import java.sql.Time;

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
	boolean load;
	boolean jeFaisLaConnexion;
	boolean partieEnCours;
	boolean partieReseauEnCours;

	static final int INIT=-1;

	static final int NOUVEAU=1;
	static final int JEU=2;
	static final int PAUSE=3;
	static final int BOT=4;
	static final int RESEAU=5;
	static final int PARTIERESEAU=31;
	static final int AIDE=30;
	static final int JOUERAUTO=6;
	static final int JOUERMANU=7;
	static final int JOUER=8;
	static final int ANNULER=9;
	static final int REJOUER=10;
	static final int ABANDONNER=11;
    static final int QUITTERMATCH=20;
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
                partieEnCours = false;
				serv = new Serveur(this);
				thServeur = new Thread(serv);
				thServeur.start();
				thFenetre = new Thread(f);
				thFenetre.start();
				pause();//atente du unpause lorsque la fenetre a fini de charger

				f.o.afficherOptions();

				if (etat == CHARGER || etat == ASTUCE) {
					etatSuivant = OPTIONS;
				} else if (j.modeNormal) {
					etat = NOUVEAU;
				} else {
					etat = MATCH;
				}
				break;

			//nouvelle partie
			case NOUVEAU:

				if(!j.finPartie && !save && j.nbCoupsRestants!=292 && j.modeNormal && partieEnCours){
					f.s.timer.stop();
					String[] options = {"Sauvegarder" , "Nouveau jeu sans sauvegarder" , "Annuler"};
					int choix  = JOptionPane.showOptionDialog(null, "Nouveau jeu :\n voulez-vous sauvegarder la partie en cours", "Sauvegarder ?",
					 				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/question.png"), options, options[0] );
					if (choix == JOptionPane.YES_OPTION) {
						//nouveau+sauvegarder
						etat=SAUVER;
						etatSuivant=NOUVEAU;
						save=true;
					}else if (choix == JOptionPane.NO_OPTION) {
						//nouveau sans sauvegarder
						j.init();
						t.init();
						save=false;
						etatSuivant=JEU;
						etat = ACTUALISER;
					}else{
						//annuler
						f.s.timer.start();
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
					t.init();
					j.init();
					save=false;
					etatSuivant=JEU;
					etat = ACTUALISER;
				}
				break;
				//verifier l'etat du jeu + attente d'un coup


            case MATCH:
	            if(partieEnCours && !j.finPartie){
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
                ma = new Match(this);
                ma.debutMatch();
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
							"Entrez l'ip de notre adversaire\nVotre ip : " + ip,
							"Lancer une partie en réseau",
							JOptionPane.INFORMATION_MESSAGE,
							new ImageIcon("./images/reseau.png"),
							null,
							null
							);
						if (ip != null && ip.matches("[0-9]*.[0-9]*.[0-9]*.[0-9]*")) {
							InetAddress addr = InetAddress.getByName(ip);
							int port = 8100;
							Socket sock = new Socket(addr, port);
							r = new Reseau(this, sock, Reseau.CLIENT);
							thReseau = new Thread(r);
							thReseau.start();
							//attente de la réponse
							sock.setSoTimeout(10000);
							while(! r.connexionAcceptee) {
								Thread.sleep(200);
							}
							sock.setSoTimeout(0); // desactive le timeout
							etat = PARTIERESEAU;
							jeFaisLaConnexion = true;
						} else {
							etat = JEU;
						}
					}

				} catch (NoRouteToHostException nt) {
					JOptionPane.showMessageDialog(f, "Hôte inconnu, connexion impossible", "Connexion impossible", JOptionPane.ERROR_MESSAGE);
					etat = JEU;
				} catch (ConnectException ce) {
					JOptionPane.showMessageDialog(f, "Connexion refusée", "Connexion impossible. Aucun Joueur d'Avalam n'est pas résent à cette adresse", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					break;
				}


			case PARTIERESEAU:
				if (! jeFaisLaConnexion) {
					pause();
				}
				partieReseauEnCours = true;
				t.init(); // raz du terrain
				f.g.reinitialisationDesBI();
				f.g.repaint();
				f.s.start = new Time(0); // raz du timer
				j.joueurCourant = 1;
				j.actualiser();
				etat = JEU;
				break;

			//verif si fin de partie ou atends un coup a jouer
			case JEU:
				f.s.timer.start();
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
				if (j.modeNormal && !partieReseauEnCours)
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
			case PAUSE:;
				f.s.timer.stop();
				JOptionPane.showMessageDialog(f, "Jeu en pause, cliquez sur OK pour reprendre la partie", "Pause", JOptionPane.INFORMATION_MESSAGE);
				etat=JEU;
				break;

			//fin de partie
			case FIN:
				partieEnCours = false; // la partie est finie
				f.s.timer.stop();
				if (partieReseauEnCours) {
					r.finDePartieReseau();
				} else if(j.modeNormal){
                    f.popupFinDePartie();
                    etat=etatSuivant;
                } else {
                    ma.popupFinDePartie();
				}
				break;

			//calcul un coup a partir du bot
			case BOT:
				f.g.labelAmpoule.setEnabled(false);
				f.activerAnnulerRefaire(false);
				f.es.actif = false;
				f.g.ea.actif = false;
				j.jouerBot();
				etat=JOUERAUTO;
				break;

			//calcul un coup reseau dans jeu.c
			case RESEAU:
				pause();
				etat=JOUERAUTO;
				break;

			case AIDE:
				f.g.labelAmpoule.setEnabled(false);
				f.es.actif = false;
				f.g.ea.actif = false;
				f.g.ejl1=f.g.etatJeul1;
				f.g.ejl2=f.g.etatJeul2;
				f.g.etatJeul1=new String("Calcul d'aide");
				f.g.etatJeul2=new String("en cours...");
				f.g.repaint();
				j.jouerBot();
				f.g.animationPionAuto();
				f.g.repaint();
				f.g.animationPionAnnuler();
				f.g.repaint();
				f.g.repaint();
				f.g.etatJeul1=f.g.ejl1;
				f.g.etatJeul2=f.g.ejl2;
				f.g.labelAmpoule.setEnabled(true);
				f.es.actif = true;
				f.g.ea.actif = true;
				etat=JEU;
				break;

			//animation du coup du bot ou reseau
			case JOUERAUTO:
				f.g.animationPionAuto();
				pause(30);
				etat=JOUER;
				break;

			//animation du coup manuel
			case JOUERMANU:
				pause();
				f.g.labelAmpoule.setEnabled(false);
				j.calculerCoup();
				if(t.estPossible(j.c)){
					etat=JOUER;
				}else{
					if(t.okAnim(f.g.coordToIndice(f.g.click))){
						f.es.actif = false;
						f.g.ea.actif = false;
						f.g.animationPion(f.g.release,f.g.click);
						f.g.repaint();
						f.es.actif = true;
						f.g.ea.actif = true;
					}
					etat=JEU;
				}
				if (j.modeNormal)
					f.g.labelAmpoule.setEnabled(true);
				break;


			//joue le coup+ajoute a l'historique+ change le joueur courrant
			case JOUER:
				partieEnCours = true;
				ElemHist e = new ElemHist(j.c,t);
				j.h.ajouterAnnuler(e);
				j.h.viderRejouer();
				t.deplacer(j.c);
				etat=ACTUALISER;
				if (j.partieEnReseau && j.courantEstHumain()) { // on envoi que si le coup est joué par le joueur
					r.outputReseau.print(j.c.toString());
				}
				j.changerJoueur();
				if(j.courantEstHumain() && j.modeNormal){
					f.g.labelAmpoule.setEnabled(true);
					f.es.actif = true;
					f.g.ea.actif = true;
				}
				break;

			case CHARGER:
				f.s.timer.stop();

				if(!j.finPartie && !load && j.nbCoupsRestants!=292 && j.modeNormal && partieEnCours){
					String[] options = {"Sauvegarder" , "Charger sans sauvegarder" , "Annuler"};
					int choix  = JOptionPane.showOptionDialog(null, "Charger :\n voulez-vous sauvegarder la partie en cours ?", "Sauvegarder ?",
					 				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/question.png"), options, options[0] );
					if (choix == JOptionPane.YES_OPTION) {
						//charger+sauvegarder
						etat=SAUVER;
						etatSuivant=CHARGER;
						load=true;
					}else if (choix == JOptionPane.NO_OPTION) {
						//charger sans sauvegarder
						s.afficherCharger();
						if(!j.revoirH)
							f.s.timer.start();
						etat=etatSuivant;
						load=false;
					}else{
						//annuler
						f.s.timer.start();
						etat=JEU;
					}
				}else{
					s.afficherCharger();
					if(!j.revoirH)
						f.s.timer.start();
					etat=etatSuivant;
					load=false;
				}
				break;

			//popop de sauvearde
			case SAUVER:
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
				}else if(load){
					etat=JEU;
					load=false;
				}else{
					etat=ACTUALISER;
					etatSuivant=JEU;
				}
				break;

			case ABANDONNER:
				etat=FIN;
                if(j.courantEstJ1()) {
					ma.abandonne = ma.J1_ABANDONNE;
                } else if(j.courantEstJ2()) {
					ma.abandonne = ma.J2_ABANDONNE;
                }
				break;

            case QUITTERMATCH:
		int choixQuitter  = JOptionPane.showOptionDialog(f.g, "Voulez-vous vraiment quitter le match en cours ?", "Quitter ?",
					 		  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,null,new ImageIcon("./images/question.png"));
		if (choixQuitter == JOptionPane.YES_OPTION) {
			ma.finDeMatch();
			etat=OPTIONS;
		} else {
			etat=JEU;
		}
                break;

			case OPTIONS:
				f.s.timer.stop();
				f.o.afficherOptions();
				if(!j.revoirH)
					f.s.timer.start();
				System.out.println("" + etatSuivant);
				etat=etatSuivant;
				break;

			case APPARENCE:
				f.s.timer.stop();
				f.app.afficherApparence();
				if(!j.revoirH)
					f.s.timer.start();
				etat=JEU;
				break;


			case REGLE:
				f.s.timer.stop();
				f.r.afficherRegle();
				if(!j.revoirH)
					f.s.timer.start();
				etat=JEU;
				break;


			case ASTUCE:
				f.s.timer.stop();
				f.as.afficherAstuces();
				f.s.timer.start();
				if (j.J1 == null || j.J2 == null || j.h == null)
					etat = OPTIONS;
				else if (etatSuivant == ASTUCE)
					etat = JEU;
				else
					etat=etatSuivant;
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
			case REJOUER:
				//grisage des fonctions et boutons annuler, rejouer et dernierCoup pendant le deplacement
				f.activerAnnulerRefaire(false);
				if( (j.J1.estRobot() || j.J2.estRobot()) && !j.revoirH){
					ElemHist coupJoueur = j.h.rejouer();
					j.h.ajouterAnnuler(coupJoueur);
					j.c = new Coups(new Point(coupJoueur.lSource,coupJoueur.cSource),new Point(coupJoueur.lDest,coupJoueur.cDest));
					f.g.animationPionAuto();
					pause(50);
					t.deplacer(j.c);

					ElemHist coupOrdi = j.h.rejouer();
					j.h.ajouterAnnuler(coupOrdi);
					j.c = new Coups(new Point(coupOrdi.lSource,coupOrdi.cSource),new Point(coupOrdi.lDest,coupOrdi.cDest));
					f.g.animationPionAuto();
					pause(50);
					t.deplacer(j.c);
				}else{
					ElemHist coup = j.h.rejouer();
					j.h.ajouterAnnuler(coup);
					j.c = new Coups(new Point(coup.lSource,coup.cSource),new Point(coup.lDest,coup.cDest));
					f.g.animationPionAuto();
					pause(50);
					t.deplacer(j.c);
					j.changerJoueur();
				}
				etat=ACTUALISER;
				break;

			case ACTUALISER:
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
				f.m.actualiser();
				f.es.actif=false;
				pause();
				if(etatSuivant==ACTUALISER){
					j.revoirH=false;
					f.m.actualiser();
					etat=JEU;
				}else{
					etat=etatSuivant;
				}
				break;

			case DERNIERCOUP:
				ElemHist dernierCoup = j.h.annuler();
				j.h.ajouterRejouer(dernierCoup);
				t.annuler(dernierCoup);

				dernierCoup = j.h.rejouer();
				j.h.ajouterAnnuler(dernierCoup);
				j.c = new Coups(new Point(dernierCoup.lSource,dernierCoup.cSource),new Point(dernierCoup.lDest,dernierCoup.cDest));
				f.g.animationPionAnnuler();
				f.g.repaint();
				f.g.animationPionAuto();
				pause(50);
				t.deplacer(j.c);
				f.g.repaint();
				etat=ACTUALISER;

			}
		}
	}
}
