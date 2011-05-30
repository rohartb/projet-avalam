import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MenuAvalam extends JMenuBar{
	Avalam a;

	JMenu avalam;
	JMenuItem nouveau, pause, sauvegarder, charger, abandonner, options, quitter;
	JMenu edition;
	JMenuItem annuler, rejouer;
	JMenu affichage;
	JMenuItem apparence, itemPleinEcran;
	JMenu aide;
	JMenuItem regle;

	MenuAvalam(Avalam a){
		this.a=a;
		EcouteurDeMenu em = a.f.em;

		avalam = new JMenu("Avalam");
		avalam.setMnemonic(KeyEvent.VK_A);
		nouveau = new JMenuItem("Nouveau");
		nouveau.setMnemonic(KeyEvent.VK_N);
		nouveau.setActionCommand("nouveau");
		nouveau.addActionListener(em);
		nouveau.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
		avalam.add(nouveau);
		pause = new JMenuItem("Pause");
		pause.setMnemonic(KeyEvent.VK_P);
		pause.setActionCommand("pause");
		pause.addActionListener(em);
		pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK));
		avalam.add(pause);
		avalam.add(new JSeparator());
		sauvegarder = new JMenuItem("Sauvegarder");
		sauvegarder.setActionCommand("sauvegarder");
		sauvegarder.addActionListener(em);
		sauvegarder.setMnemonic(KeyEvent.VK_S);
		sauvegarder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		avalam.add(sauvegarder);
		charger = new JMenuItem("Charger");
		charger.setActionCommand("charger");
		charger.addActionListener(em);
		charger.setMnemonic(KeyEvent.VK_C);
		charger.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		avalam.add(charger);
		abandonner = new JMenuItem("Abandonner");
		abandonner.setActionCommand("abandonner");
		abandonner.addActionListener(em);
		abandonner.setMnemonic(KeyEvent.VK_B);
		abandonner.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,Event.CTRL_MASK));
		avalam.add(abandonner);
		avalam.add(new JSeparator());
		options = new JMenuItem("Options");
		options.setActionCommand("options");
		options.addActionListener(em);
		options.setMnemonic(KeyEvent.VK_O);
		options.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
		avalam.add(options);
		quitter = new JMenuItem("Quitter");
		quitter.setActionCommand("quitter");
		quitter.addActionListener(em);
		quitter.setMnemonic(KeyEvent.VK_Q);
		quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
		avalam.add(quitter);
		this.add(avalam);

		edition = new JMenu("Edition");
		edition.setMnemonic(KeyEvent.VK_E);
		annuler = new JMenuItem("Annuler");
		annuler.setActionCommand("annuler");
		annuler.addActionListener(em);
		annuler.setMnemonic(KeyEvent.VK_N);
		annuler.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK));
		edition.add(annuler);
		rejouer = new JMenuItem("Rejouer");
		rejouer.setActionCommand("rejouer");
		rejouer.addActionListener(em);
		rejouer.setMnemonic(KeyEvent.VK_R);
		rejouer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK | Event.SHIFT_MASK));
		edition.add(rejouer);
		this.add(edition);

		affichage = new JMenu("Affichage");
		affichage.setMnemonic(KeyEvent.VK_A);
		apparence = new JMenuItem("Apparence");
		apparence.setActionCommand("apparence");
		apparence.addActionListener(em);
		apparence.setMnemonic(KeyEvent.VK_N);
		apparence.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,Event.CTRL_MASK));
		affichage.add(apparence);
		itemPleinEcran = new JMenuItem("Plein écran");
		itemPleinEcran.setActionCommand("pleinEcran");
		itemPleinEcran.addActionListener(em);
		itemPleinEcran.setMnemonic(KeyEvent.VK_P);
		itemPleinEcran.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
		affichage.add(itemPleinEcran);
		this.add(affichage);

		aide = new JMenu("Aide");
		aide.setActionCommand("aide");
		aide.addActionListener(em);
		aide.setMnemonic(KeyEvent.VK_D);
		regle = new JMenuItem("Règle du jeu");
		regle.setActionCommand("regle");
		regle.addActionListener(em);
		regle.setMnemonic(KeyEvent.VK_R);
		aide.add(regle);
		this.add(aide);
	}

	void actualiser(){
		annuler.setEnabled(!a.j.h.annulerVide());
		rejouer.setEnabled(!a.j.h.rejouerVide());
		if(a.j.pause){
			pause.setText("Reprendre");
			pause.setActionCommand("reprendre");
		}else{
			pause.setText("Pause");
			pause.setActionCommand("pause");
		}
	}
}
