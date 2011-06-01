import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.*;

class EcouteurDeMenu implements ActionListener{
	Avalam a;

	EcouteurDeMenu(Avalam a){
		this.a=a;
	}

	public void actionPerformed(ActionEvent e){
		a.etatSuivant=a.etat;
		String cmd = e.getActionCommand();
		if (cmd.equals("nouveau")){
			a.etat=a.NOUVEAU;
			a.unpause();
		}else if (cmd.equals("reseau")){
			a.etat=a.CONNEXION;
			a.unpause();
		}else if (cmd.equals("pause")){
			a.etat=a.PAUSE;
			a.unpause();
		}else if (cmd.equals("reprendre")){
			a.etat=a.ACTUALISER;
			a.unpause();
		}else if(cmd.equals("charger")){
			a.etat=a.CHARGER;
			a.unpause();
 		}else if(cmd.equals("sauvegarder")){
			a.etat=a.SAUVER;
			a.unpause();
		}else if(cmd.equals("abandonner")){
			a.etat=a.ABANDONNER;
			a.unpause();
		}else if(cmd.equals("options")){
			a.etat=a.OPTIONS;
			a.unpause();
		}else if(cmd.equals("quitter")){
			a.etat=a.QUITTER;
			a.unpause();
		}else if(cmd.equals("annuler")){
			a.etat=a.ANNULER;
			a.unpause();
		}else if(cmd.equals("rejouer")){
			a.etat=a.REJOUER;
			a.unpause();
		}else if(cmd.equals("apparence")){
			a.etat=a.APPARENCE;
			a.unpause();
		}else if(cmd.equals("aide")){

		}else if(cmd.equals("regle")){
			a.etat=a.REGLE;
			a.unpause();
		}else if(cmd.equals("astuces")){
			a.etat=a.ASTUCE;
			a.unpause();
		}else if(cmd.equals("pleinEcran")){
			a.f.pleinEcran();
		}else{
			System.out.println("La commande '" + e.getActionCommand() + "' est non implémenté");
		}
	}
}
