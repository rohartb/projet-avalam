import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class Options extends JDialog{
	Avalam a;

	int typeJ1, typeJ2;
	String nomJ1, nomJ2;
	boolean modeNormal;
	boolean aide;

	int typeJ1Tmp, typeJ2Tmp;
	String nomJ1Tmp, nomJ2Tmp;
	boolean modeTmp;
	boolean aideTmp;

	///////////////////////////////////////
	// Objets pr les ecouteurs
	EcouteurDOptions eo;

	// Nom des joueurs
	JTextField TextFieldNomJ1, TextFieldNomJ2;

	// type de joueurs
	JRadioButton humain1, machine1, humain2, machine2;

	//// niveau des bots
	JRadioButton n10, n11, n12;
	JRadioButton n20, n21, n22;
	JRadioButton PartieNormale, PartieMatch;
	JCheckBox activerAide;

	JButton ok;
	JButton annuler;


	public Options (Avalam a) {
		this.a = a;

		typeJ1 = a.j.J1.type;
		typeJ2 = a.j.J2.type;
		nomJ1 = a.j.J1.nom;
		nomJ2 = a.j.J2.nom;
		modeNormal= a.j.modeNormal;
		aide = a.j.modeAide;

		nomJ1Tmp = nomJ1;
		nomJ2Tmp = nomJ2;
		typeJ1Tmp = typeJ1;
		typeJ2Tmp = typeJ2;
		aideTmp = aide;

		eo = new EcouteurDOptions(a, this);


		this.setTitle("Options");
		this.setModal(true);

		this.setResizable(false);
		this.addWindowListener(eo);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


		/// CHOIX
		/////// Gauche - J1


		///// Type J1
		///////// Choix humain pour j1
		humain1 = new JRadioButton("Humain");
		humain1.setActionCommand("humain1");
		humain1.addActionListener(eo);
		humain1.setSelected(typeJ1 == Jeu.HUMAIN);

		///////// Choix bot pour j1
		machine1 = new JRadioButton("Machine");
		machine1.setActionCommand("machine1");
		machine1.addActionListener(eo);
		machine1.setSelected(estRobot(typeJ1));

		ButtonGroup BGroupJ1 = new ButtonGroup();
		BGroupJ1.add(humain1);
		BGroupJ1.add(machine1);


		n10 = new JRadioButton("Facile");
		n10.setActionCommand("facile1");
		n10.addActionListener(eo);
		n10.setSelected(typeJ1 == Jeu.BOTLVL1);
		n10.setMargin(new Insets(0, 20, 0, 0));

		n11 = new JRadioButton("Moyen");
		n11.setActionCommand("moyen1");
		n11.addActionListener(eo);
		n11.setSelected(typeJ1 == Jeu.BOTLVL2);
		n11.setMargin(new Insets(0, 20, 0, 0));

		n12 = new JRadioButton("Difficile");
		n12.setActionCommand("difficile1");
		n12.addActionListener(eo);
		n12.setSelected(typeJ1 == Jeu.BOTLVL3);
		n12.setMargin(new Insets(0, 20, 0, 0));

		// Niveau du robot désactivé si machine non choisi
		if (typeJ1 == Jeu.HUMAIN) {
			n10.setEnabled(false);
			n11.setEnabled(false);
			n12.setEnabled(false);
		}

		// ajout au JButtonGroup
		ButtonGroup BotLevelGroupJ1 = new ButtonGroup();
		BotLevelGroupJ1.add(n10);
		BotLevelGroupJ1.add(n11);
		BotLevelGroupJ1.add(n12);


		//////////// Panel de niveau du Bot
		//// Sous Panel Textfield Nom J1
		TextFieldNomJ1 = new JTextField(6);
		TextFieldNomJ1.addKeyListener(eo);
		TextFieldNomJ1.setText(nomJ1);

		JLabel LabelTextFieldNomJ1 = new JLabel("Nom");

		// Sous-panel Type du joueur1
		JPanel PanelTextFieldNomJ1 = new JPanel();
		PanelTextFieldNomJ1.add(LabelTextFieldNomJ1);
		PanelTextFieldNomJ1.add(TextFieldNomJ1);

		JPanel PanelTypeJ1 = new JPanel();

		JPanel PanelJ1 = new JPanel();
		PanelJ1.setBorder(BorderFactory.createTitledBorder("Joueur 1"));
		PanelJ1.setLayout(new BoxLayout(PanelJ1, BoxLayout.PAGE_AXIS));

		PanelJ1.add(humain1, BorderLayout.WEST);
		PanelJ1.add(PanelTextFieldNomJ1, BorderLayout.EAST);
		PanelJ1.add(machine1, BorderLayout.WEST);
		PanelJ1.add(n10);
		PanelJ1.add(n11);
		PanelJ1.add(n12);

		//////// Partie Droite - J2

		//////////// Choix humain pour j2
		humain2 = new JRadioButton("Humain");
		humain2.setSelected(typeJ2 == Jeu.HUMAIN);
		humain2.setActionCommand("humain2");
		humain2.addActionListener(eo);

		//////////// Choix bot pour j2
		machine2 = new JRadioButton("Machine");
		machine2.setActionCommand("machine2");
		machine2.addActionListener(eo);
		machine2.setSelected(estRobot(typeJ2) );


		ButtonGroup BGroupJ2 = new ButtonGroup();
		BGroupJ2.add(humain2);
		BGroupJ2.add(machine2);


		n20 = new JRadioButton("Facile");
		n20.setActionCommand("facile2");
		n20.addActionListener(eo);
		n20.setSelected(typeJ2 == Jeu.BOTLVL1);
		n20.setMargin(new Insets(0, 20, 0, 0));

		n21 = new JRadioButton("Moyen");
		n21.setActionCommand("moyen2");
		n21.addActionListener(eo);
		n21.setSelected(typeJ2 == Jeu.BOTLVL2);
		n21.setMargin(new Insets(0, 20, 0, 0));

		n22 = new JRadioButton("Difficile");
		n22.setActionCommand("difficile2");
		n22.addActionListener(eo);
		n22.setSelected(typeJ2 == Jeu.BOTLVL3);
		n22.setMargin(new Insets(0, 20, 0, 0));


		// Desactivé si machine non choisi
		if (typeJ2 == Jeu.HUMAIN) {
			n20.setEnabled(false);
			n21.setEnabled(false);
			n22.setEnabled(false);
		}


		// ajout au JButtonGroup
		ButtonGroup BotLevelGroupJ2 = new ButtonGroup();
		BotLevelGroupJ2.add(n20);
		BotLevelGroupJ2.add(n21);
		BotLevelGroupJ2.add(n22);


		//////////// Panel de niveau du bot
		JPanel BotLevelPanelJ2 = new JPanel();

		//// Sous-panel Textfield Nom J2
		TextFieldNomJ2 = new JTextField(6);
		TextFieldNomJ2.addKeyListener(eo);
		TextFieldNomJ2.setText(nomJ2);
		JLabel LabelTextFieldNomJ2 = new JLabel("Nom");

		JPanel PanelTextFieldNomJ2 = new JPanel();
		PanelTextFieldNomJ2.add(LabelTextFieldNomJ2);
		PanelTextFieldNomJ2.add(TextFieldNomJ2);


		JPanel PanelJ2 = new JPanel();
		PanelJ2.setBorder(BorderFactory.createTitledBorder("Joueur 2"));
		PanelJ2.setLayout(new BoxLayout(PanelJ2, BoxLayout.Y_AXIS));

		PanelJ2.add(humain2);

		PanelJ2.add(PanelTextFieldNomJ2, BorderLayout.WEST);
		PanelJ2.add(machine2,  BorderLayout.WEST);
		PanelJ2.add(n20,  BorderLayout.WEST);
		PanelJ2.add(n21,  BorderLayout.WEST);
		PanelJ2.add(n22,  BorderLayout.WEST);


		JPanel PanelChoixJoueur = new JPanel();
		PanelChoixJoueur.setLayout(new GridLayout(1,2));
		PanelChoixJoueur.add(PanelJ1);
		PanelChoixJoueur.add(PanelJ2);

		//////// Panel Option de partie
		PartieNormale = new JRadioButton("Normale");
		PartieNormale.setActionCommand("normal");
		PartieNormale.addActionListener(eo);
		PartieNormale.setSelected(modeNormal);

		PartieMatch = new JRadioButton("Match");
		PartieMatch.setActionCommand("match");
		PartieMatch.addActionListener(eo);
		PartieMatch.setSelected(!modeNormal);

		ButtonGroup OptionDePartieGroup = new ButtonGroup();
		OptionDePartieGroup.add(PartieMatch);
		OptionDePartieGroup.add(PartieNormale);



		JPanel PanelOptionDeJeu = new JPanel();
		PanelOptionDeJeu.setBorder(BorderFactory.createTitledBorder("Option de partie"));
		PanelOptionDeJeu.add(PartieNormale);
		PanelOptionDeJeu.add(PartieMatch);


		//Afficher déplacement d'un pion

		activerAide = new JCheckBox("Activer");
		activerAide.setActionCommand("activeraide");
		activerAide.addActionListener(eo);
		activerAide.setSelected(aide);

		JPanel panelOptionDAide = new JPanel();
		panelOptionDAide.setBorder(BorderFactory.createTitledBorder("Aide"));
		panelOptionDAide.add(activerAide);

		/////// Panel Bouton Ok / Annuler

		JPanel PanelOkAnnuler = new JPanel(); //Panel du boutons ok/annuler
		JButton appliquerRelancer = new JButton("Appliquer et relancer");
		appliquerRelancer.setActionCommand("appliquerrelancer");
		appliquerRelancer.addActionListener(eo);

		JButton ok = new JButton("Appliquer");
		ok.setActionCommand("ok");
		ok.addActionListener(eo);

		JButton annuler = new JButton("Annuler");
		annuler.setActionCommand("annuler");
		annuler.addActionListener(eo);

		PanelOkAnnuler.add(annuler);
		PanelOkAnnuler.add(ok);
		PanelOkAnnuler.add(appliquerRelancer);

		JPanel panelModeAide = new JPanel();
		panelModeAide.add(PanelOptionDeJeu);
		panelModeAide.add(panelOptionDAide);

		// Ajout des panels à la fenetre
		this.add(PanelChoixJoueur, BorderLayout.NORTH);
		this.add(panelModeAide);
		this.add(PanelOkAnnuler  , BorderLayout.SOUTH);


		//Affichage des prefs
		this.pack();

		// Position au centre de la fenetre principale
		Point locAvalam  = a.f.getLocation();
		Dimension tailleAvalam = a.f.getSize();
		Dimension taillePref   = this.getSize();

		int x, y;
		x = (tailleAvalam.width - taillePref.width)/2+locAvalam.x;
		y = (tailleAvalam.height - taillePref.height)/2+locAvalam.y;

		this.setLocation(x,y);
	}

	public void afficherOptions() {
		TextFieldNomJ1.setText(a.j.J1.nom);
		TextFieldNomJ2.setText(a.j.J2.nom);
		nomJ1Tmp = a.j.J1.nom;
		nomJ2Tmp = a.j.J2.nom;
		typeJ1Tmp = a.j.J1.type;
		typeJ2Tmp = a.j.J2.type;
		modeTmp = a.j.modeNormal;

		if(a.j.J1.type == Jeu.HUMAIN) {
			humain1.setSelected(true);
			machine1.setSelected(false);
			n10.setEnabled(false);
			n11.setEnabled(false);
			n12.setEnabled(false);
		} else if (a.j.J1.type == Jeu.BOTLVL1) {
			n10.setSelected(true);
			n10.setEnabled(true);
			n11.setEnabled(true);
			n12.setEnabled(true);
			machine1.setSelected(true);
		} else if (a.j.J1.type == Jeu.BOTLVL2) {
			n11.setSelected(true);
			n10.setEnabled(true);
			n11.setEnabled(true);
			n12.setEnabled(true);
			machine1.setSelected(true);
		} else if (a.j.J1.type == Jeu.BOTLVL3) {
			n10.setEnabled(true);
			n10.setEnabled(true);
			n11.setEnabled(true);
			n12.setSelected(true);
			machine1.setSelected(true);
		}

		if(a.j.J2.type == Jeu.HUMAIN) {
			humain2.setSelected(true);
			machine2.setSelected(false);
			n20.setEnabled(false);
			n21.setEnabled(false);
			n22.setEnabled(false);
		} else if (a.j.J2.type == Jeu.BOTLVL1) {
			machine2.setSelected(true);
			n20.setSelected(true);
			n20.setEnabled(true);
			n21.setEnabled(true);
			n22.setEnabled(true);
		} else if (a.j.J2.type == Jeu.BOTLVL2){
			machine2.setSelected(true);
			n21.setSelected(true);
			n20.setEnabled(true);
			n21.setEnabled(true);
			n22.setEnabled(true);
		} else if (a.j.J2.type == Jeu.BOTLVL3) {
			machine2.setSelected(true);
			n22.setSelected(true);
			n20.setEnabled(true);
			n21.setEnabled(true);
			n22.setEnabled(true);
		}
		if (a.j.modeNormal)
			PartieNormale.setSelected(true);
		else
			PartieMatch.setSelected(false);

		if (a.j.modeAide)
			activerAide.setSelected(true);
		else
			activerAide.setSelected(false);

		this.setVisible(true);
	}

	public void fermerOptions() {
		this.setVisible(false);
	}


	public static boolean estRobot(int type) {
		return ( type == Jeu.BOTLVL1 || type == Jeu.BOTLVL2 ||
		         type == Jeu.BOTLVL3 );

	}

}
