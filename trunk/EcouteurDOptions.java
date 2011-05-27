import java. awt.event.*;
import javax.swing.*;

public class EcouteurDOptions implements ActionListener,
                                         KeyListener,
                                         WindowListener {
	Avalam a;
	Options o;

	EcouteurDOptions (Avalam a) {
		this.a = a;
		this.o = a.f.o;
	}

	public void actionPerformed(ActionEvent e) {}

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
		if(o.estRobot(o.typeJ1) && a.j.J1.s == null ) {
			a.j.J1.s = new Skynet(a);
		} else if(o.estRobot(o.typeJ1) && a.j.J1.s != null ) {
			a.j.J1.s.niveau = o.typeJ1;
		}


		if(o.estRobot(o.typeJ2) && a.j.J2.s == null ) {
			a.j.J1.s = new Skynet(a);
		} else if(o.estRobot(o.typeJ2) && a.j.J2.s != null ) {
			a.j.J1.s.niveau = o.typeJ2;
		}

		a.j.J1.nom = o.nomJ1;
		a.j.J2.nom = o.nomJ2;

		//Changement des params sur le joueur actuel
		a.j.J1.type = o.typeJ1;
		a.j.J2.type = o.typeJ2;

		//sauvegarder à faire des param

		//fermeture de la fenetre

		int rep = JOptionPane.showConfirmDialog(null,"Voulez-vous démarrer une nouvelle partie?","Avalam",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		if(rep==JOptionPane.YES_OPTION){
			a.etatSuivant = a.NOUVEAU;
		} else {
			a.etatSuivant = a.ACTUALISER;
		}
		o.setVisible(false);
	}

	public void traiterAnnuler() {
		o.nomJ1Tmp  = o.nomJ1;
		o.nomJ2Tmp  = o.nomJ2;
		o.typeJ1Tmp = o.typeJ1;
		o.typeJ2Tmp = o.typeJ2;
		o.setVisible(false);
	}

	public void windowClosed(WindowEvent e) {}
	public void windowClosing      (WindowEvent e) {}
	public void windowOpened       (WindowEvent e) {}
	public void windowIconified    (WindowEvent e) {}
	public void windowDeiconified  (WindowEvent e) {}
	public void windowActivated    (WindowEvent e) {}
	public void windowDeactivated  (WindowEvent e) {}
	public void windowGainedFocus  (WindowEvent e) {}
	public void windowLostFocus    (WindowEvent e) {}
	public void windowStateChanged (WindowEvent e) {}
}
