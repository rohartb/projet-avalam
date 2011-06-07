import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JComponent;

public class Astuces extends JDialog {
	Avalam a;
	private static final long serialVersionUID = 1L;
	EcouteurDAstuces eas;
	int nbAstuce;
	JButton precedent;
	JButton suivant;
	JLabel lesAstuces;
	ImageIcon i[] = new ImageIcon[8];

	public Astuces(Avalam a){
		i[0] = new ImageIcon("images/aide1.png");
		i[1] = new ImageIcon("images/aide2.png");
		i[2] = new ImageIcon("images/aide3.png");
		i[3] = new ImageIcon("images/aide4.png");
		i[4] = new ImageIcon("images/aide5.png");
		i[5] = new ImageIcon("images/aide6.png");
		i[6] = new ImageIcon("images/aide7.png");
		i[7] = new ImageIcon("images/aide8.png");

		this.a = a;
		this.nbAstuce = 1;
		this.eas = new EcouteurDAstuces(a,this,this.nbAstuce);

		this.setTitle("Astuces");
		this.setModal(true);

		this.setResizable(false);
		this.addWindowListener(eas);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		lesAstuces = new JLabel();


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

		this.add(lesAstuces, BorderLayout.NORTH);
		this.add(panelBouton,BorderLayout.SOUTH);
		this.setSize(500,410);


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
		this.lesAstuces.setIcon(i[0]) ;
		this.precedent.setEnabled(false);
		this.suivant.setEnabled(true);
		this.setVisible(true);

	}
}
