import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class EcouteurDApparence implements ActionListener,
                                           ItemListener,
                                           WindowListener {

	Avalam a;
	Apparence app;

	private int themeTmp;

	public EcouteurDApparence(Avalam a, Apparence app) {
		this.a   = a;
		this.app = app;
		themeTmp = -1;
	}


	public void actionPerformed(ActionEvent e) {
		String cmd;
		if (e.getActionCommand().equals("theme")) {
			JComboBox cb = (JComboBox) e.getSource();
			cmd = (String) cb.getSelectedItem();
			if (themeTmp == -1) {
				themeTmp = a.f.g.theme;
			}
			if (cmd.equals("Theme 1")) {
				a.f.g.theme = Themes.THEME1;
			} else if (cmd.equals("Theme 2")) {
				a.f.g.theme = Themes.THEME2;
			} else if (cmd.equals("Theme 3")) {
				a.f.g.theme = Themes.THEME3;
			} else {
				System.out.println("Commande : " + cmd );
			}
			a.f.g.reinitialisationBIPlateau();
			a.f.g.repaint();
		}
		cmd = e.getActionCommand() ;
		if (cmd.equals("ok")) {
			//ap.getMoteur().sp.sauvegarder();
			app.fermerApparence();
			themeTmp = -1;
		} else if (cmd.equals("annuler")) {
			if (themeTmp != -1) {
				a.f.g.theme = themeTmp;
				a.f.g.repaint();
			}
			app.fermerApparence();
			themeTmp = -1;
		}
	}


	public void itemStateChanged(ItemEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String cmd = (String) cb.getSelectedItem();
		System.out.println("Commande : " + cmd);
	}


	/// WINDOW LISTENER

	public void windowClosed(WindowEvent e) {
		// gestion de la validation des param
	}


	public void windowClosing      (WindowEvent e) {
		// Quand on clic sur la croix (revient à faire annuler)
			if (themeTmp != -1) {
				a.f.g.theme = themeTmp;
				a.f.g.repaint();
			}
			app.fermerApparence();
			themeTmp = -1;
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

