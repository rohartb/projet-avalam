import java.awt.event.*;
import javax.swing.*;

public class EcouteurDAstuces implements ActionListener,
                                         WindowListener {
	Avalam a;
	Astuces as;
	static int num;

	EcouteurDAstuces(Avalam a,Astuces as,int nb){
		this.a=a;
		this.as=as;
		this.num = nb;
		System.out.println("lala c'est le num" +num);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		System.out.println("Commande " + cmd);
		if (cmd.equals("quitter")) {
			traiterQuitter();
		} else if (cmd.equals("precedent" )) {
			traiterPrecedent();
		} else if (cmd.equals("suivant")) {
			traiterSuivant();
		}
	}

	public void traiterQuitter(){
		as.setVisible(false);
		num = 1;
	}

	public void traiterPrecedent(){
		switch(num){
			case 2 :
				num--;
				//as.lesAstuces.setText("Bienvenue");
				as.lesAstuces.setIcon(as.i[0]);
				as.precedent.setEnabled(false);
				break;
			case 3 :
				num--;
				as.lesAstuces.setIcon(as.i[1]);
				//as.lesAstuces.setText("Jeu Avalam");
				break;
			case 4 :
				num--;
				as.lesAstuces.setIcon(as.i[2]);
				//as.lesAstuces.setText("Comment jouer");
				break;
			case 5 :
				num--;
				as.lesAstuces.setIcon(as.i[3]);
				//as.lesAstuces.setText("Faire des tours");
				break;
			case 6 :
				num--;
				as.lesAstuces.setIcon(as.i[4]);
				//as.lesAstuces.setText("Gagner une partie");
				break;
			case 7 :
				num--;
				as.lesAstuces.setIcon(as.i[5]);
				//as.lesAstuces.setText("Modifier jeu");
				as.suivant.setEnabled(true);
				break;
		}
	}

	public void traiterSuivant(){
		as.precedent.setEnabled(true);
		switch(num){
			case 1 :
				num++;
				as.lesAstuces.setIcon(as.i[1]);
				//as.lesAstuces.setText("Jeu Avalam");
				break;
			case 2 :
				as.lesAstuces.setIcon(as.i[2]);
				num++;
				//as.lesAstuces.setText("Comment jouer");
				break;
			case 3 :
				num++;
				as.lesAstuces.setIcon(as.i[3]);
				//as.lesAstuces.setText("Faire des tours");
				break;
			case 4 :
				num++;
				as.lesAstuces.setIcon(as.i[4]);
				//as.lesAstuces.setText("Gagner une partie");
				break;
			case 5 :
				num++;
				as.lesAstuces.setIcon(as.i[5]);
				//as.lesAstuces.setText("Modifier jeu");
				break;
			case 6 :
				as.suivant.setEnabled(false);
				num++;
				as.lesAstuces.setIcon(as.i[6]);
				//as.lesAstuces.setText("Fin");
				break;
		}

	}

	public void windowClosing(WindowEvent e) {
		as.setVisible(false);
	}

	public void windowClosed(WindowEvent e){}
	public void windowOpened(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowGainedFocus(WindowEvent e){}
	public void windowLostFocus(WindowEvent e){}
	public void windowStateChanged(WindowEvent e){}
}
