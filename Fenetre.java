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
		this.setMinimumSize(new Dimension(400, 400));

		//creation d'ecouteurs
		em = new EcouteurDeMenu(a);
		es = new EcouteurDeSouris(a);
		ef = new EcouteurDeFenetre(a);

		//ajout menu
		m = new MenuAvalam(a);
		this.setJMenuBar(m);

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
		if (nbJ1 == nbJ2) {
			JOptionPane.showMessageDialog(null,"Personne ne gagne ! \n Score : "+nbJ1+" - "+nbJ2,"Egalité",JOptionPane.INFORMATION_MESSAGE);
		} else {
			if((a.j.J1.estRobot() && a.j.J2.estHumain()) ||
			   (a.j.J1.estRobot() && a.j.J1.estHumain())) {
				if ((nbJ2 > nbJ1 && a.j.J2.estRobot()) ||
				    (nbJ2 < nbJ1 && a.j.J2.estRobot())){
					JOptionPane.showMessageDialog(null,"Vous avez perdu! \n Score :  "+nbJ1+" - "+nbJ2, "Défaite" ,JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					JOptionPane.showMessageDialog(null,"Vous avez gagné! \n Score : "+nbJ1+" - "+nbJ2, "Victoire" ,JOptionPane.INFORMATION_MESSAGE);
				}
			}else{
				if (nbJ2 > nbJ1)
					vainqueur = a.j.J2.nom;
				else // nbJ1 > nbJ2
					vainqueur = a.j.J1.nom;

				JOptionPane.showMessageDialog(null,vainqueur +
				                              " remporte la partie ! \n Score :  "+nbJ1+" - "+nbJ2, "Victoire" ,JOptionPane.INFORMATION_MESSAGE);
			}
		}
		System.out.println("Score :  "+nbJ1+" - "+nbJ2);

	}
}
