import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Fenetre extends JFrame implements Runnable{
	Avalam a;

	MenuAvalam m;

	Apparence app;
	EcouteurDeMenu em;
	EcouteurDeSouris es;
	EcouteurDeFenetre ef;
	Options o;
	StatusBar s;
	BarreLaterale b;
	TerrainGraphique g;
	Regle r;
	Astuces as;

	boolean pleinEcran;

	Fenetre(Avalam a) {
		this.a=a;
		pleinEcran = false;

	}

	void pleinEcran(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		if(!pleinEcran){
			dispose();
			setUndecorated(true);
			setResizable(false);
			setVisible(true);
			if(gs.isFullScreenSupported()){
				validate();
				gs.setFullScreenWindow(this);
				pleinEcran = true;
				m.itemPleinEcran.setText("Quitter plein écran");
			}else{
				JOptionPane.showMessageDialog(null, "Erreur", "Plein écran non disponible sur votre Système", JOptionPane.INFORMATION_MESSAGE);
			}
		}else{
			dispose();
			setUndecorated(false);
			setResizable(true);
			setVisible(true);
			gs.setFullScreenWindow(null);
			pleinEcran = false;
			m.itemPleinEcran.setText("Plein écran");
		}
	}


	public void run() {
		//recuperation theme system
		try {
			if (System.getProperty("os.name").equals("Mac OS X")) {
			System.setProperty ("apple.laf.useScreenMenuBar","true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Test");
			} else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		} catch(Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}

		//titre
		this.setTitle("Avalam");

		// position initiale de la fenetre sur l'ecran
		int resHauteur = Toolkit.getDefaultToolkit().getScreenSize().height;
		int resLargeur = Toolkit.getDefaultToolkit().getScreenSize().width;
		if (resHauteur > 630 )
			this.setSize(600, 600);
		else
			this.setSize(resHauteur-100, resHauteur-100);
		int hauteur  = (resHauteur - this.getSize().height)/2;
		int largeur  = (resLargeur - this.getSize().width)/2;
		this.setLocation(largeur, hauteur);
		this.setMinimumSize(new Dimension(600, 500));

		//creation d'ecouteurs
		em = new EcouteurDeMenu(a);
		es = new EcouteurDeSouris(a);
		ef = new EcouteurDeFenetre(a);

		//ajout menu
		m = new MenuAvalam(a);
		this.setJMenuBar(m);

		//b = new BarreLaterale(a);
		//this.add(b, BorderLayout.WEST);

		//ajout plateau
		g = new TerrainGraphique(a);
		this.add(g);

		//ajout barre status
		s = new StatusBar(a);
		this.add(s, BorderLayout.SOUTH);

		//ajout ecouteurs
		g.addMouseMotionListener(es);
		g.addMouseListener(es);
		g.addComponentListener(es);
		this.addWindowListener(ef);

		// Creation des popups
		o = new Options(a);
		app = new Apparence(a);
		r = new Regle(a);
		as = new Astuces(a);

		//action fermeture fenetre
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.validate();
		this.setVisible(true);

		a.unpause();
	}

	public void popupFinDePartie() {
		// Calcul du score

		String vainqueur;
		int nbJ1 = a.j.J1.score;
		int nbJ2 = a.j.J2.score;
		// Affichage du la popup
		String message;
		String titre;
		if(nbJ1 == nbJ2){
			message=new String("Personne ne gagne ! \n Score : "+nbJ1+" - "+nbJ2);
			titre=new String("Egalité");
		} else {
			if((a.j.J1.estRobot() && a.j.J2.estHumain()) || (a.j.J2.estRobot() && a.j.J1.estHumain())) {
				if ((nbJ2 > nbJ1 && a.j.J2.estRobot()) || (nbJ2 < nbJ1 && a.j.J1.estRobot())){
					message=new String("Vous avez perdu! \n Score :  "+nbJ1+" - "+nbJ2);
					titre=new String("Défaite");
				}
				else{
					message=new String("Vous avez gagné! \n Score : "+nbJ1+" - "+nbJ2);
					titre=new String("Victoire");
				}
			}else{
				if (nbJ2 > nbJ1)
					vainqueur = a.j.J2.nom;
				else // nbJ1 > nbJ2
					vainqueur = a.j.J1.nom;
				message = new String(vainqueur+" remporte la partie ! \n Score :  "+nbJ1+" - "+nbJ2);
				titre = new String("Victoire");
			}
		}
		String[] options = {"Rejouer" , "Revoir la partie" , "Quitter"};
		int choix  = JOptionPane.showOptionDialog(null, message, titre, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./images/question.png"), options, options[0] );
		a.etatSuivant=a.JEU;
		a.j.finPartie=false;
		if (choix == JOptionPane.YES_OPTION) {
			a.etatSuivant=a.NOUVEAU;
		}else if (choix == JOptionPane.CANCEL_OPTION) {
			System.exit(0);
		}else{
			a.j.revoirH=true;
			a.etatSuivant=a.HISTORIQUE;
		}
	}
}
