import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Fenetre extends JFrame implements Runnable{
	Avalam a;

	MenuAvalam m;

	EcouteurDeMenu em;
	EcouteurDeSouris es;
	EcouteurDeFenetre ef;
	Options o;
	StatusBar s;
	TerrainGraphique g;

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
      		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
		StatusBarPanel PanelTemps = new StatusBarPanel() ;
		PanelTemps.setPanelWidth(10);
		PanelTemps.setWidthFixed(true);
		PanelTemps.setPanelBorder();
		PanelTemps.setLayout(new BorderLayout());
		//temps.setHorizontalAlignment(JLabel.CENTER);
		PanelTemps.add(new JLabel ("Blabla time"));

		StatusBarPanel PanelCoups = new StatusBarPanel() ;
		PanelCoups.setPanelWidth(100);
		PanelCoups.setWidthFixed(false);
		PanelCoups.setPanelBorder();
		PanelCoups.setLayout(new BorderLayout());
		//m.coupsRestants.setHorizontalAlignment(JLabel.CENTER);
		PanelCoups.add(new JLabel("Score restant"));

		StatusBarPanel PanelMode = new StatusBarPanel() ;
		PanelMode.setPanelWidth(100);
		PanelMode.setWidthFixed(true);
		PanelMode.setPanelBorder();
		PanelMode.setLayout(new BorderLayout());
		//m.mode.setHorizontalAlignment(JLabel.CENTER);
		PanelMode.add(new JLabel("Blabla mode"));
		StatusBarPanel[] BarStatut = {PanelTemps, PanelCoups,PanelMode};

		s = new StatusBar(BarStatut);
		this.add(s, BorderLayout.SOUTH);



		//ajout ecouteurs
		g.addMouseMotionListener(es);
		g.addMouseListener(es);
		this.addWindowListener(ef);

		// Creation des popups
		o = new Options(a);

		//action fermeture fenetre
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.validate();
		this.setVisible(true);

		a.unpause();
	}
}
