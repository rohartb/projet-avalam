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
		a.interupt=true;
		String cmd = e.getActionCommand();
		if (cmd.equals("nouveau")){
			a.etatSuivant=a.NOUVEAU;
			a.unpause();
		}else if (cmd.equals("reseau")){
			a.etatSuivant=a.CONNEXION;
			a.unpause();
		}else if (cmd.equals("pause")){
			a.etatSuivant=a.PAUSE;
			a.unpause();
		}else if(cmd.equals("charger")){
			a.etatSuivant=a.CHARGER;
			a.unpause();
 		}else if(cmd.equals("sauvegarder")){
			a.etatSuivant=a.SAUVER;
			a.unpause();
		}else if(cmd.equals("abandonner")){
			a.etatSuivant=a.ABANDONNER;
			a.unpause();
		}else if(cmd.equals("options")){
			a.etatSuivant=a.OPTIONS;
			a.unpause();
		}else if(cmd.equals("quitter")){
			a.etatSuivant=a.QUITTER;
			a.unpause();
		}else if(cmd.equals("annuler")){
			a.etatSuivant=a.ANNULER;
			a.unpause();
		}else if(cmd.equals("rejouer")){
			a.etatSuivant=a.REJOUER;
			a.unpause();
		}else if(cmd.equals("apparence")){
			a.etatSuivant=a.APPARENCE;
			a.unpause();
		}else if(cmd.equals("aide")){

		}else if(cmd.equals("regle")){
			a.etatSuivant=a.REGLE;
			a.unpause();
		}else if(cmd.equals("astuces")){
			a.etatSuivant=a.ASTUCE;
			a.unpause();
		}else if(cmd.equals("pleinEcran")){
			a.f.pleinEcran();
		}else if(cmd.equals("dernierCoup")){
			a.etatSuivant=a.DERNIERCOUP;
			a.unpause();
		}else{
			System.out.println("La commande '" + e.getActionCommand() + "' est non implémenté");
		}
	}
}
