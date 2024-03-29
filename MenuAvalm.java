import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MenuAvalam extends JMenuBar{
	Avalam a;
	private static final long serialVersionUID = 1L;

	JMenu avalam;
	JMenuItem nouveau, reseau, pause, sauvegarder, charger, options, quitter;
	JMenu edition;
	JMenuItem annuler, rejouer, dernierCoup;
	JMenu affichage;
	JMenuItem apparence, itemPleinEcran;
    JMenu match;
    JMenuItem nouveauMatch, abandonner, quitterMatch;
	JMenu aide;
	JMenuItem regle;
	JMenuItem astuces;

	MenuAvalam(Avalam a){
		this.a=a;
		EcouteurDeMenu em = a.f.em;

		avalam = new JMenu("Jeu");
		avalam.setMnemonic(KeyEvent.VK_A);
		nouveau = new JMenuItem("Nouveau");
		nouveau.setMnemonic(KeyEvent.VK_N);
		nouveau.setActionCommand("nouveau");
		nouveau.addActionListener(em);
		nouveau.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
		avalam.add(nouveau);
		reseau = new JMenuItem("Nouvelle partie en réseau");
		reseau.setMnemonic(KeyEvent.VK_R);
		reseau.setActionCommand("reseau");
		reseau.addActionListener(em);
		reseau.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
		avalam.add(reseau);
		pause = new JMenuItem("Pause");
		pause.setMnemonic(KeyEvent.VK_P);
		pause.setActionCommand("pause");
		pause.addActionListener(em);
		pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK));
		avalam.add(pause);
		avalam.add(new JSeparator());
		options = new JMenuItem("Options");
		options.setActionCommand("options");
		options.addActionListener(em);
		options.setMnemonic(KeyEvent.VK_O);
		options.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
		avalam.add(options);
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
        avalam.add(new JSeparator());
		quitter = new JMenuItem("Quitter");
		quitter.setActionCommand("quitter");
		quitter.addActionListener(em);
		quitter.setMnemonic(KeyEvent.VK_Q);
		quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
		avalam.add(quitter);
		this.add(avalam);
        
        match= new JMenu("Match");
        nouveauMatch = new JMenuItem("Demarrer un nouveau match");
		nouveauMatch.setActionCommand("nouveauMatch");
		nouveauMatch.addActionListener(em);
		match.add(nouveauMatch);
        abandonner = new JMenuItem("Abandonner la partie");
		abandonner.setActionCommand("abandonner");
		abandonner.addActionListener(em);
		abandonner.setMnemonic(KeyEvent.VK_B);
		abandonner.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,Event.CTRL_MASK));
		match.add(abandonner);
        quitterMatch = new JMenuItem("Quitter le match");
		quitterMatch.setActionCommand("quitterMatch");
		quitterMatch.addActionListener(em);
		match.add(quitterMatch);
        this.add(match);
        


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
		dernierCoup = new JMenuItem("Voir dernier coup");
		dernierCoup.setActionCommand("dernierCoup");
		dernierCoup.addActionListener(em);
		//dernierCoup.setMnemonic(KeyEvent.VK_R);
		//dernierCoup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK | Event.SHIFT_MASK));
		edition.add(dernierCoup);
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
		astuces= new JMenuItem("Astuces");
		astuces.setActionCommand("astuces");
		astuces.addActionListener(em);
		astuces.setMnemonic(KeyEvent.VK_A);
		aide.add(astuces);
		this.add(aide);
	}

	void actualiser(){
		if (a.s.fichierAChargerSontPresents())
			charger.setEnabled(true);
		else
			charger.setEnabled(false);

		//si la taille de la pile est de 1 (un seul coup joue, coup d'un robot) et que c'est au joueur de jouer, on ne peut pas annuler! (sauf si on est en revoirHistorique)
		if(a.j.h.tailleAnnuler()==1 && (a.j.J1.estRobot() || a.j.J2.estRobot()) && a.j.courantEstHumain() && !a.j.revoirH){
			annuler.setEnabled(false);
			a.f.g.annuler.setEnabled(false);
		}else{
			annuler.setEnabled(!a.j.h.annulerVide() && (a.j.revoirH || a.j.modeNormal)
								&& !(a.j.J1.estRobot() && a.j.J2.estRobot() && !a.j.revoirH));
			a.f.g.annuler.setEnabled(!a.j.h.annulerVide() && (a.j.revoirH || a.j.modeNormal)
								&& !(a.j.J1.estRobot() && a.j.J2.estRobot() && !a.j.revoirH)); // bouton du terrain
		}

		rejouer.setEnabled(!a.j.h.rejouerVide() && (a.j.revoirH || a.j.modeNormal));
		a.f.g.rejouer.setEnabled(!a.j.h.rejouerVide() && (a.j.revoirH || a.j.modeNormal));
        nouveauMatch.setEnabled(a.j.modeNormal);
        abandonner.setEnabled(!a.j.modeNormal);
        quitterMatch.setEnabled(!a.j.modeNormal);

        
		if(!a.j.revoirH){
			dernierCoup.setEnabled(!a.j.h.annulerVide());
		}else{
			dernierCoup.setEnabled(false);
		}
		if(a.j.revoirH){
			pause.setText("Reprendre");
			a.f.g.pause.setText("➤");
			pause.setActionCommand("reprendre");
			a.f.g.pause.setActionCommand("reprendre");
			//a.f.s.timer.stop();
		}else{
			pause.setText("Pause");
			a.f.g.pause.setText("❙❙");
			pause.setActionCommand("pause");
			a.f.g.pause.setActionCommand("pause");
			//if(!a.j.revoirH)
			//	a.f.s.timer.start();
	 	}
	}
}
