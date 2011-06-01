import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JComponent;

public class Astuces extends JDialog {
	Avalam a;
	EcouteurDAstuces eas;
	int nbAstuce;
	JButton precedent;
	JButton suivant;
	JLabel lesAstuces;

	public Astuces(Avalam a){
		this.a = a;
		this.nbAstuce = 1;
		this.eas = new EcouteurDAstuces(a,this,this.nbAstuce); 

		this.setTitle("Astuces");
		this.setModal(true);

		this.setResizable(false);
		this.addWindowListener(eas);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		JPanel panelAst = new JPanel();
		lesAstuces = new JLabel("Bienvenue lala");
		
		panelAst.add(lesAstuces);

		// Quitter / Suivant / Précédent

		JPanel panelBouton = new JPanel(); //Panel du boutons ok/annuler
		JButton quitter = new JButton("Quitter");
		quitter.setActionCommand("quitter");
		quitter.addActionListener(eas);

		precedent = new JButton("Précédent");
		precedent.setActionCommand("precedent");
		precedent.addActionListener(eas);

		suivant = new JButton("Suivant");
		suivant.setActionCommand("suivant");
		suivant.addActionListener(eas);

		panelBouton.add(quitter);
		panelBouton.add(precedent);
		panelBouton.add(suivant);
  
		this.precedent.setEnabled(false);

		this.add(panelAst);
		this.add(panelBouton,BorderLayout.SOUTH);
		this.setSize(500,400);

	  
		// Position au centre de la fenetre principale
		Point locAvalam  = a.f.getLocation();
		Dimension tailleAvalam = a.f.getSize();
		Dimension taillePref   = this.getSize();

		int x, y;
		x = (tailleAvalam.width - taillePref.width)/2+locAvalam.x;
		y = (tailleAvalam.height - taillePref.height)/2+locAvalam.y;

		this.setLocation(x,y);
	}
	  
	public void afficherAstuces(){
		nbAstuce = 1;
		this.lesAstuces.setText("Bienvenue blabla");
		this.precedent.setEnabled(false);
		this.suivant.setEnabled(true);
		this.setVisible(true);
	}
}