import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Apparence extends JDialog {
	Avalam a;
	EcouteurDApparence ea;
	int dimension;
	JComboBox ComboTheme;

	public Apparence(Avalam a) {
		this.a = a;

		// charge par loader
		dimension = 2; // 2D intialement
		ea = new EcouteurDApparence(a,this);

		this.setTitle("Apparence");
		this.setModal(true);
		// class de gestion de l'apparence du jeu
		String[] themeStrings = {"Theme 1", "Theme 2", "Theme 3"};

		ComboTheme = new JComboBox(themeStrings);
		if ( a.f.g.theme == Themes.THEME1 )
			ComboTheme.setSelectedIndex(0);
		else if ( a.f.g.theme == Themes.THEME2 )
			ComboTheme.setSelectedIndex(1);
		else if ( a.f.g.theme == Themes.THEME3 )
			ComboTheme.setSelectedIndex(2);

		ComboTheme.setActionCommand("theme");
		ComboTheme.setPreferredSize(new Dimension(100,30));
		ComboTheme.addActionListener(ea);
		//ComboFond.addItemListener(ea);

		JLabel LabelTheme = new JLabel("Theme");
		LabelTheme.setHorizontalAlignment(JLabel.RIGHT);

		// 2D ou 3D
		JRadioButton Plateau2D = new JRadioButton("Plateau2D");
		Plateau2D.addActionListener(ea);
		Plateau2D.setActionCommand("plateau2D");

		JRadioButton Plateau3D = new JRadioButton("Plateau3D");
		Plateau3D.addActionListener(ea);
		Plateau3D.setActionCommand("plateau3D");

		ButtonGroup DimensionGroup = new ButtonGroup();
		DimensionGroup.add(Plateau2D);
		DimensionGroup.add(Plateau3D);

		Plateau3D.setSelected(dimension == 3);
		Plateau2D.setSelected(dimension == 2);

		JPanel PanelChoixTheme =  new JPanel();
		PanelChoixTheme.setBorder(BorderFactory.createTitledBorder("Choix du style"));
		PanelChoixTheme.setLayout(new GridLayout(1,2, 10, 10));
		PanelChoixTheme.add(LabelTheme);
		PanelChoixTheme.add(ComboTheme);

		ComboTheme.setPreferredSize(new Dimension(100, 30));

		JPanel PanelChoixDimension = new JPanel();
		PanelChoixDimension.setBorder(BorderFactory.createTitledBorder("Affichage"));
		PanelChoixDimension.add(Plateau2D);
		PanelChoixDimension.add(Plateau3D);


		JPanel ApparencePanel = new JPanel();
		ApparencePanel.setLayout(new BoxLayout(ApparencePanel, BoxLayout.PAGE_AXIS));
		ApparencePanel.add(PanelChoixTheme);
		ApparencePanel.add(PanelChoixDimension);


		JButton Ok = new JButton("Appliquer");
		Ok.setActionCommand("ok");
		Ok.addActionListener(ea);

		JButton Annuler = new JButton("Annuler");
		Annuler.setActionCommand("annuler");
		Annuler.addActionListener(ea);

		JPanel OkAnnuler = new JPanel();
		OkAnnuler.add(Annuler);
		OkAnnuler.add(Ok);

		ApparencePanel.add(OkAnnuler, BorderLayout.SOUTH);

		this.addWindowListener(ea);
		this.add(ApparencePanel);
		this.validate();
		this.setSize(250,200);
        this.setResizable(false);

		// Position au centre de la fenetre principale
		Point     locAvalam    = a.f.getLocation();
		Dimension tailleAvalam = a.f.getSize();
		Dimension tailleAppa   = this.getSize();

		int x, y;
		x = (tailleAvalam.width - tailleAppa.width)/2+locAvalam.x;
		y = (tailleAvalam.height - tailleAppa.height)/2+locAvalam.y;

		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setLocation(x,y);
	}

	public void afficherApparence() {
		this.setVisible(true);
	}

	public void fermerApparence() {
		this.setVisible(false);
		ComboTheme.setSelectedIndex(a.f.g.theme-1);
	}
	
	public void sauver(){
		try {
			File config = new File(System.getProperty("user.home")+"/.Avalam/Config/");
			if(!config.exists())
				config.mkdir();
			File f = new File(System.getProperty("user.home")+"/.Avalam/Config/apparence.cfg");
			f.setWritable(true);
			
			String s = "";

			s += a.f.g.theme;

			FileOutputStream o = new FileOutputStream(f);
			PrintStream ps = new PrintStream(f);
			ps.print(s);
			f.setReadOnly();
		} catch (FileNotFoundException ex) {}
	}
}
