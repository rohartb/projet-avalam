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
	static final int NON_ABANDONNE = 0;
	static final int J1_ABANDONNE = 1;
	static final int J2_ABANDONNE = 2;
	int abandonne;
    
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
        int abandonne = NON_ABANDONNE;
        scoreMJ1 = 0;
        scoreMJ2 = 0;
        nbToursJ1 = 0;
        nbToursJ2 = 0;
        a.f.m.options.setEnabled(false);
        a.f.m.sauvegarder.setEnabled(false);
        a.f.m.charger.setEnabled(false);
        a.f.g.labelAmpoule.setEnabled(false);
        a.f.g.ea.actif = false;
        //a.f.g.repaint();
        a.f.activerAnnulerRefaire(false);
        a.f.activerPause(false);
        a.j.init();
        a.t.init();
        a.j.modeNormal=false;
        a.j.modeAide = false;
        a.f.s.actualiser();
        a.save=false;
        a.etat=a.ACTUALISER;
    }
    
    public void popupDebutPartie() {
        fenetre = new JDialog();
        fenetre.setTitle("Match");
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
        JPanel panelNord = new JPanel(new GridLayout(3,1));
        
            //Panel vide pour aerer
            panelNord.add(new JPanel());
            
            //Label
            JLabel labelNord = new JLabel("Choisir le nombre de parties a remporter",JLabel.CENTER);
            JLabel labelBis = new JLabel("pour gagner le match :",JLabel.CENTER);
            panelNord.add(labelNord);
            panelNord.add(labelBis);
        
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
        nbPartiesJouees++;
		int nbJ1 = a.j.J1.score;
		int nbJ2 = a.j.J2.score;
		// Affichage du la popup
		String message= new String ("Partie : " + nbPartiesJouees + " (match en "+nbPartiesTotales+" parties gagnantes).\n");
		String titre;
		if(abandonne == NON_ABANDONNE) {
			if(nbJ1 == nbJ2){
				message+=("Personne ne gagne ! \n Score : "+scoreMJ1+" - "+scoreMJ2);
				titre=new String("Egalité");
			} else {
				if((a.j.J1.estRobot() && a.j.J2.estHumain()) || (a.j.J2.estRobot() && a.j.J1.estHumain())) {
					if (nbJ2 > nbJ1 && a.j.J2.estRobot()) {
                        scoreMJ2++;
						message += ("Vous avez perdu! \n Score :  "+scoreMJ1+" - "+scoreMJ2);
						titre=new String("Défaite");
					} else if (nbJ2 < nbJ1 && a.j.J1.estRobot()) {
                        scoreMJ1++;
						message += ("Vous avez perdu! \n Score :  "+scoreMJ1+" - "+scoreMJ2);
						titre=new String("Défaite");
					} else if (nbJ1 > nbJ2) {
                        scoreMJ1++;
						message += ("Vous avez gagné! \n Score : "+scoreMJ1+" - "+scoreMJ2);
						titre=new String("Victoire");
					} else {
						scoreMJ2++;
						message += ("Vous avez gagné! \n Score : "+scoreMJ1+" - "+scoreMJ2);
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
					message += (vainqueur+" remporte la partie ! \n Score :  "+scoreMJ1+" - "+scoreMJ2);
					titre = new String("Victoire");
				}
			}
			nbToursJ1 += nbJ1;
			nbToursJ2 += nbJ2;
		} else if (abandonne == J1_ABANDONNE) {
            scoreMJ2++;
			message += (a.j.J1.nom+ " abandonne ! "+scoreMJ1+"-"+scoreMJ2);
			titre=new String("Abandon");
		} else {
            scoreMJ1++;
			message += (a.j.J2.nom+ " abandonne ! "+scoreMJ1+"-"+scoreMJ2);
			titre=new String("Abandon");
		}
        abandonne = NON_ABANDONNE;
        
        if(scoreMJ1 >= nbPartiesTotales || scoreMJ2 >= nbPartiesTotales) {
            popupFinDeMatch();
        } else {
            String[] options = {"Continuer le match"};
            JOptionPane.showOptionDialog(a.f, message, titre, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            // nouvelle partie
            a.j.init();
            a.t.init();
            a.j.finPartie = false;
            a.etat = a.ACTUALISER;
            a.etatSuivant = a.JEU;
        }
	}

	public void popupFinDeMatch() {
		String vainqueur;
		int nbJ1 = a.j.J1.score;
		int nbJ2 = a.j.J2.score;
                
		// Affichage du la popup
		nbToursJ1 += nbJ1;
		nbToursJ2 += nbJ2;
        String message= new String ("Résultat du match : ");
		String titre;
        if((a.j.J1.estRobot() && a.j.J2.estHumain()) || (a.j.J2.estRobot() && a.j.J1.estHumain())) {
            if ((scoreMJ2 > scoreMJ1 && a.j.J2.estRobot()) || (scoreMJ2 < scoreMJ1 && a.j.J1.estRobot())){
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
		message += ("\nStatistiques : \n" +
		            "   nombre de tours de " + a.j.J1.nom + " : " + nbToursJ1 + "\n" +
		            "   nombre de tours de " + a.j.J2.nom + " : " + nbToursJ2 + "\n");
		String[] options = {"Nouveau match", "Quitter le mode match"};
		int rep = JOptionPane.showOptionDialog(a.f, message, titre, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		if (rep == JOptionPane.YES_OPTION) {
            a.etat=a.NOUVEAUMATCH;
		}else if (rep == JOptionPane.NO_OPTION || rep == -1) {
            finDeMatch();
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
        a.f.g.ea.actif = true;
        a.j.modeNormal=true;
        a.partieEnCours = false;
        a.f.m.pause.setEnabled(true);
        a.f.g.pause.setEnabled(true);
        a.j.modeAide = true;
        a.etat = a.ACTUALISER;
    }
}
