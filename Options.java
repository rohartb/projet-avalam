import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Options extends JDialog{
	Avalam a;

	int typeJ1, typeJ2;
	String nomJ1, nomJ2;
	boolean modeNormal;

	int typeJ1Tmp, typeJ2Tmp;
	String nomJ1Tmp, nomJ2Tmp;
	boolean modeTmp;

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

	JButton ok;
	JButton annuler;


	public Options (Avalam a) {
		this.a = a;

		typeJ1 = a.j.HUMAIN;
		typeJ2 = a.j.BOTLVL1;
		nomJ1 = "Joueur 1";
		nomJ2 = "Easy";
		modeNormal=true;

		nomJ1Tmp = nomJ1;
		nomJ2Tmp = nomJ2;
		typeJ1Tmp = typeJ1;
		typeJ2Tmp = typeJ2;

		eo = new EcouteurDOptions(a, this);


		this.setTitle("Options");
		this.setModal(true);

		this.setResizable(false);
		this.addWindowListener(eo);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


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
		//BotLevelPanelJ2.setLayout(new GridLayout(4,1));
		//BotLevelPanelJ2.add(machine2);
		//BotLevelPanelJ2.add(n20);
		//BotLevelPanelJ2.add(n21);
		//BotLevelPanelJ2.add(n22);


		//// Sous-panel Textfield Nom J2
		TextFieldNomJ2 = new JTextField(6);
		TextFieldNomJ2.addKeyListener(eo);
		TextFieldNomJ2.setText(nomJ2);
		JLabel LabelTextFieldNomJ2 = new JLabel("Nom");

		JPanel PanelTextFieldNomJ2 = new JPanel();
		PanelTextFieldNomJ2.add(LabelTextFieldNomJ2);
		PanelTextFieldNomJ2.add(TextFieldNomJ2);


		//// Sous-panel Type du joueur2
		// JPanel PanelTypeJ2 = new JPanel();
		// PanelTypeJ2.setLayout(new GridLayout(1,2));
		// PanelTypeJ2.add(humain2, BorderLayout.NORTH);
		// PanelTypeJ2.add(BotLevelPanelJ2);




		JPanel PanelJ2 = new JPanel();
		PanelJ2.setBorder(BorderFactory.createTitledBorder("Joueur 2"));
		PanelJ2.setLayout(new BoxLayout(PanelJ2, BoxLayout.Y_AXIS));

		PanelJ2.add(humain2);

		PanelJ2.add(PanelTextFieldNomJ2, BorderLayout.WEST);
		PanelJ2.add(machine2,  BorderLayout.WEST);
		PanelJ2.add(n20,  BorderLayout.WEST);
		PanelJ2.add(n21,  BorderLayout.WEST);
		PanelJ2.add(n22,  BorderLayout.WEST);

		//PanelJ2.add(PanelTypeJ2);


		JPanel PanelChoixJoueur = new JPanel();
		PanelChoixJoueur.setLayout(new GridLayout(1,2));
		PanelChoixJoueur.add(PanelJ1);
		PanelChoixJoueur.add(PanelJ2);


		//////// Panel Option de partie
		JRadioButton PartieNormale = new JRadioButton("Normale");
		PartieNormale.setActionCommand("normal");
		PartieNormale.addActionListener(eo);
		PartieNormale.setSelected(modeNormal);


		JRadioButton PartieMatch = new JRadioButton("Match");
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

		// Ajout des panels à la fenetre
		this.add(PanelChoixJoueur, BorderLayout.NORTH);
		this.add(PanelOptionDeJeu);
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
		this.setVisible(true);
	}


	public static boolean estRobot(int type) {
		return ( type == Jeu.BOTLVL1 || type == Jeu.BOTLVL2 ||
		         type == Jeu.BOTLVL3 );

	}


}
