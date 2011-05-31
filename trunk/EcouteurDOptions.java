import java. awt.event.*;
import javax.swing.*;

public class EcouteurDOptions implements ActionListener,
                                         KeyListener,
                                         WindowListener {
	Avalam a;
	Options o;

	EcouteurDOptions (Avalam a, Options o) {
		this.a = a;
		this.o = o;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		System.out.println("Commande " + cmd);
		if (cmd.equals("ok")) {
			traiterOk();
		} else if (cmd.equals("annuler" )) {
			traiterAnnuler();
		} else if (cmd.equals("appliquerrelancer")) {
			traiterAppliquerRelancer();
		} else if (cmd.equals("machine1")) {
			o.n10.setSelected(true);
			o.n10.setEnabled(true);
			o.n11.setEnabled(true);
			o.n12.setEnabled(true);
			if (o.n10.isSelected())
				o.typeJ1Tmp = Jeu.BOTLVL1;
			else if (o.n11.isSelected())
				o.typeJ1Tmp = Jeu.BOTLVL2;
			else if (o.n12.isSelected())
				o.typeJ1Tmp = Jeu.BOTLVL3;
		} else if (cmd.equals("humain1")) {
			o.typeJ1Tmp = Jeu.HUMAIN;
			o.n10.setEnabled(false);
			o.n11.setEnabled(false);
			o.n12.setEnabled(false);
		} else if (cmd.equals("machine2")) {
			o.n20.setSelected(true);
			o.n20.setEnabled(true);
			o.n21.setEnabled(true);
			o.n22.setEnabled(true);
			if (o.n20.isSelected())
				o.typeJ2Tmp = Jeu.BOTLVL1;
			else if (o.n21.isSelected())
				o.typeJ2Tmp = Jeu.BOTLVL2;
			else if (o.n22.isSelected())
				o.typeJ2Tmp = Jeu.BOTLVL3;
		} else if (cmd.equals("humain2")) {
			o.typeJ2Tmp = Jeu.HUMAIN;
			o.n20.setEnabled(false);
			o.n21.setEnabled(false);
			o.n22.setEnabled(false);
		} else if (cmd.equals("facile1")) {
			o.typeJ1Tmp = Jeu.BOTLVL1;
		} else if (cmd.equals("moyen1")) {
			o.typeJ1Tmp = Jeu.BOTLVL2;
		} else if (cmd.equals("difficile1")) {
			o.typeJ1Tmp = Jeu.BOTLVL3;
		} else if (cmd.equals("facile2")) {
			o.typeJ2Tmp = Jeu.BOTLVL1;
			//System.out.println("lvl1 : " + p.typeJ2Tmp);
		} else if (cmd.equals("moyen2")) {
			o.typeJ2Tmp = Jeu.BOTLVL2;
		} else if (cmd.equals("difficile2")) {
			o.typeJ2Tmp = Jeu.BOTLVL3;
		} else if (cmd.equals("nomj1")) {
			// rien
		} else if (cmd.equals("nomj2")) {
			// rien
		} else if (cmd.equals("normal")) {
			o.modeTmp=true;
		} else if (cmd.equals("match")) {
			o.modeTmp=false;
		} else {
			System.out.println("Commande '" + cmd + "' n'est pas implémentée");
		}
	}

	//// Ecouteur du texte tapé pr les noms
	public void keyReleased(KeyEvent e) {
		JTextField tf = (JTextField) e.getSource();

		if (tf == o.TextFieldNomJ1) {
			o.nomJ1Tmp = tf.getText();
		} else if (tf == o.TextFieldNomJ2) {
			o.nomJ2Tmp = tf.getText();
		} else {
			System.err.println("TextField inconnu : " + tf);
		}
	}

	public void keyPressed(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}


	//////////////////////////
	public void traiterOk() {

		//Validation des params temporaires
		o.nomJ1  = o.nomJ1Tmp;
		o.nomJ2  = o.nomJ2Tmp;
		o.typeJ1 = o.typeJ1Tmp;
		o.typeJ2 = o.typeJ2Tmp;
		o.modeNormal = o.modeTmp;


		// si le bot n'existe pas on le cree
		// si il existe, on remplace son niveau
		if(o.estRobot(o.typeJ1) && a.j.J1.b == null ) {
			a.j.J1.b = new Bot(a, o.typeJ1, 1);
		} else if(o.estRobot(o.typeJ1) && a.j.J1.b != null ) {
			a.j.J1.b.niveau = o.typeJ1;
		}


		if(o.estRobot(o.typeJ2) && a.j.J2.b == null ) {
			a.j.J1.b = new Bot(a, o.typeJ2, 2);
		} else if(o.estRobot(o.typeJ2) && a.j.J2.b != null ) {
			a.j.J1.b.niveau = o.typeJ2;
		}

		a.j.J1.nom = o.nomJ1;
		a.j.J2.nom = o.nomJ2;

		//Changement des params sur le joueur actuel
		a.j.J1.type = o.typeJ1;
		a.j.J2.type = o.typeJ2;
		o.fermerOptions();
		//a.etat = a.ACTUALISER;
	}

	public void traiterAppliquerRelancer() {
		traiterOk();
		a.etatSuivant = a.NOUVEAU;
	}

	public void traiterAnnuler() {
		o.nomJ1Tmp  = o.nomJ1;
		o.nomJ2Tmp  = o.nomJ2;
		o.typeJ1Tmp = o.typeJ1;
		o.typeJ2Tmp = o.typeJ2;

		a.unpause();
		o.fermerOptions();
	}

	public void windowClosed(WindowEvent e) {}

	public void windowClosing(WindowEvent e) {
		o.fermerOptions();
	}


	public void windowOpened       (WindowEvent e) {}
	public void windowIconified    (WindowEvent e) {}
	public void windowDeiconified  (WindowEvent e) {}
	public void windowActivated    (WindowEvent e) {}
	public void windowDeactivated  (WindowEvent e) {}
	public void windowGainedFocus  (WindowEvent e) {}
	public void windowLostFocus    (WindowEvent e) {}
	public void windowStateChanged (WindowEvent e) {}

}