import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.Time;

class Match {
	Avalam a;

	String nombrePartie;
	int nbPartiesTotales;       // nb Parties
	int nbPartiesJouees;
	int scoreMJ1;  // match gagné par J1
	int scoreMJ2;  // match gagné par J2
	int nbToursJ1; // nb tour totale de J1
	int nbToursJ2; // nb tour totale de J2
    boolean finMatch;
    
    //popupDebutPartie
    JDialog fenetre;
    JSlider slider;

	public Match(Avalam a){
		this.a = a;
    }

	void debutMatch() {
        a.f.s.timer.stop();
        popupDebutPartie();
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
        a.f.g.repaint();
    }
    
    public void popupDebutPartie() {
        fenetre = new JDialog();
        fenetre.setModal(true);
        fenetre.setResizable(false);
        
        //Panel du centre, avec l'icone et la liste
        JPanel panelCentre = new JPanel(new FlowLayout());
        
            //Slider
            slider = new JSlider(2,10,2);
            slider.setMajorTickSpacing(2);
            slider.setMinorTickSpacing(1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setSnapToTicks(true);
            
            //Icon
            JLabel icone = new JLabel(new ImageIcon("images/match.png"),JLabel.CENTER);
            
            panelCentre.add(icone);
            panelCentre.add(slider);
        
        fenetre.add(panelCentre);
        
        //Panel du bas, avec les trois boutons
        JPanel panelBas = new JPanel(new BorderLayout());
        
            //Panel Boutons :
            JPanel panelBoutons = new JPanel(new GridLayout(1,2,10,10));
            
            JButton annuler = new JButton("Annuler");
            annuler.setActionCommand("annuler");
            panelBoutons.add(annuler);
            
            JButton ok = new JButton("Ok");
            ok.setActionCommand("ok");
            panelBoutons.add(ok);
            
            EcouteurDeMatch em = new EcouteurDeMatch(this);
            ok.addActionListener(em);
            annuler.addActionListener(em);
            panelBas.add(panelBoutons);
            
            //Espaces pour aerer les boutons
            JPanel p1 = new JPanel();
            p1.setSize(40,300);
            JPanel p2 = new JPanel();
            p2.setSize(40,300);
            JPanel p3 = new JPanel();
            p3.setSize(250,40);
            JPanel p4 = new JPanel();
            p4.setSize(250,40);
            panelBas.add(p1,"North");
            panelBas.add(p2,"South");
            panelBas.add(p3,"East");
            panelBas.add(p4,"West");
        
        fenetre.add(panelBas,"South");
        
        //Panel nord, label
        JPanel panelNord = new JPanel(new GridLayout(2,1));
        
            //Panel vide pour aerer
            panelNord.add(new JPanel());
            
            //Label
            JLabel labelNord = new JLabel("Nombre de match gagnant :",JLabel.CENTER);
            panelNord.add(labelNord);
        
        fenetre.add(panelNord,BorderLayout.NORTH);
        
        //Panel Est et ouest pour decaler la list du bord droit
        JLabel pEst = new JLabel("      ");
        fenetre.add(pEst,"East");
        JLabel pOuest = new JLabel("  ");
        fenetre.add(pOuest,"West");

        
        fenetre.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        fenetre.addWindowListener(em);
        
        fenetre.pack();
        int resHauteur = Toolkit.getDefaultToolkit().getScreenSize().height;
		int resLargeur = Toolkit.getDefaultToolkit().getScreenSize().width;
		int hauteur  = (resHauteur - fenetre.getSize().height)/2;
		int largeur  = (resLargeur - fenetre.getSize().width)/2;
		fenetre.setLocation(largeur, hauteur);
        
        fenetre.setVisible(true);
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
		String[] options = {"Lancer un nouveau match", "Quitter le mode match"};
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
        System.out.println("feagae");
        a.f.m.options.setEnabled(true);
        a.f.m.sauvegarder.setEnabled(true);
        a.f.m.charger.setEnabled(true);
        a.f.g.labelAmpoule.setEnabled(true);
        a.j.modeNormal=true;
        a.partieEnCours = false;
        a.f.m.pause.setEnabled(true);
        a.f.g.pause.setEnabled(true);
    }
}
