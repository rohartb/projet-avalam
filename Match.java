import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.Time;

class Match{
	Avalam a;

	String nombrePartie;
	int nbPartiesTotales;       // nb Parties
	int nbPartiesJouees;
	int scoreMJ1;  // match gagné par J1
	int scoreMJ2;  // match gagné par J2
	int nbToursJ1; // nb tour totale de J1
	int nbToursJ2; // nb tour totale de J2
    boolean finMatch;

	public Match(Avalam a){
		this.a = a;
    }

	void debutMatch() {
		boolean estPasBon = true;
        a.f.s.timer.stop();

        while(estPasBon) {
            nombrePartie = (String)JOptionPane.showInputDialog(a.f,"Bienvenue dans le mode Match\n"
                                                               + a.j.J1.nom + " contre " + a.j.J2.nom + "\n\n"
                                                               + "Veuillez entrer le nombre de partie a disputer","Match",JOptionPane.WARNING_MESSAGE,new ImageIcon("Images/match.png"), null,"3");
            if(nombrePartie== null) { // retour au mode normal
                a.etat=a.JEU;
                a.j.modeNormal = true;
                estPasBon = false;
                a.etat = a.ACTUALISER;
        		a.etatSuivant = a.JEU;
                return;
            } else {
                try {
                    nbPartiesTotales = Integer.valueOf(nombrePartie);
                    estPasBon = false;
                } catch (Exception e){
                    estPasBon = true;
                }
            }
        }
        init();
    }

    public void init(){
        finMatch = false;
        scoreMJ1 = 0;
        scoreMJ2 = 0;
        nbToursJ1 = 0;
        nbToursJ2 = 0;
        a.f.m.options.setEnabled(false);
        a.f.m.sauvegarder.setEnabled(false);
        a.f.m.charger.setEnabled(false);
        a.f.g.labelAmpoule.setEnabled(false);
    }

	public void popupFinDePartie() {
		String vainqueur;
		int nbJ1 = a.j.J1.score;
		int nbJ2 = a.j.J2.score;
		// Affichage du la popup
		String message= new String ("match " + (nbPartiesJouees) + "/" + nbPartiesTotales+ "\n");
		String titre;
		if(nbJ1 == nbJ2){
			message+=("Personne ne gagne ! \n Score : "+nbJ1+" - "+nbJ2);
			titre=new String("Egalité");
		} else {
			if((a.j.J1.estRobot() && a.j.J2.estHumain()) || (a.j.J2.estRobot() && a.j.J1.estHumain())) {
				if ((nbJ2 > nbJ1 && a.j.J2.estRobot()) || (nbJ2 < nbJ1 && a.j.J1.estRobot())){
					message += ("Vous avez perdu! \n Score :  "+nbJ1+" - "+nbJ2);
					titre=new String("Défaite");
				}
				else{
					message += ("Vous avez gagné! \n Score : "+nbJ1+" - "+nbJ2);
					titre=new String("Victoire");
				}
			}else{
				if (nbJ2 > nbJ1) {
					vainqueur = a.j.J2.nom;
					scoreMJ2++;
				} else { // nbJ1 > nbJ2
					vainqueur = a.j.J1.nom;
					scoreMJ1++;
				}
				message += (vainqueur+" remporte la partie ! \n Score :  "+nbJ1+" - "+nbJ2);
				titre = new String("Victoire");
			}
		}
		nbToursJ1 += nbJ1;
		nbToursJ2 += nbJ2;
		String[] options = {"Continuer le match"};
		JOptionPane.showOptionDialog(a.f, message, titre, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		// nouvelle partie
		a.j.init();
		a.t.init();
		a.j.finPartie = false;
		a.etat = a.JEU;
	}

	public void popupFinDeMatch() {
		String vainqueur;
		int nbJ1 = a.j.J1.score;
		int nbJ2 = a.j.J2.score;
		// Affichage du la popup
		nbToursJ1 += nbJ1;
		nbToursJ2 += nbJ2;
		if (nbJ2 > nbJ1) {
			scoreMJ2++;
		} else if(nbJ1 > nbJ2) {
			scoreMJ1++;
		}else {
			scoreMJ1++; scoreMJ2++;
		}
		String message= new String ("Résultat du match : ");
		String titre;
		if(scoreMJ1 == scoreMJ2){
			message+=("Personne ne gagne ! \n Score : "+scoreMJ1+" - "+scoreMJ2);
			titre=new String("Egalité");
		} else {
			if((a.j.J1.estRobot() && a.j.J2.estHumain()) || (a.j.J2.estRobot() && a.j.J1.estHumain())) {
				if ((nbJ2 > nbJ1 && a.j.J2.estRobot()) || (nbJ2 < nbJ1 && a.j.J1.estRobot())){
					message += ("Vous avez perdu! \n Score : " + scoreMJ1 + " - " + scoreMJ2);
					titre=new String("Défaite");
				}
				else{
					message += ("Vous avez gagné! \n Score : " + scoreMJ1 + " - " + scoreMJ2);
					titre=new String("Victoire");
				}
			}else{
				if (scoreMJ2 > scoreMJ1) {
					vainqueur = a.j.J2.nom;
				}else { //
					vainqueur = a.j.J1.nom;
				}
				message += (vainqueur+" remporte la partie ! \n Score :  "+scoreMJ1+" - "+scoreMJ2);
				titre = new String("Victoire");
			}
		}
		message += ("\nStatistiques : \n" +
		            "   nombre de tours de " + a.j.J1.nom + " : " + nbToursJ1 + "\n" +
		            "   nombre de tours de " + a.j.J2.nom + " : " + nbToursJ2 + "\n");
		String[] options = {"Lancer un nouveau match", "Quitter le monde match"};
		int rep = JOptionPane.showOptionDialog(a.f, message, titre, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		if (rep == JOptionPane.YES_OPTION) {
			System.out.println("Nouveau match");
			a.etat = a.NOUVEAUMATCH;
		}else if (rep == JOptionPane.NO_OPTION || rep == -1) {
			System.out.println("Fin du mode match");
			a.etat = a.OPTIONS;
			a.f.o.ok.setEnabled(false);
		} else {
			System.err.println("Erreur fin match " + rep);
		}
	}

    public void finDeMatch(){
        //TODO popup de fin avec resultat et tout
        a.f.m.options.setEnabled(true);
        a.f.m.sauvegarder.setEnabled(true);
        a.f.m.charger.setEnabled(true);
        a.f.g.labelAmpoule.setEnabled(true);
        a.j.modeNormal=true;
        a.partieEnCours = false;
    }
}
