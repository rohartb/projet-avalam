import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class EcouteurDeSauvegarde implements ActionListener {
    Sauvegarde sauvegarde;

    public EcouteurDeSauvegarde(Sauvegarde s) {
        sauvegarde = s;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("supprimer")) {
            int value = JOptionPane.showConfirmDialog(sauvegarde.fenetreCharger,
                                                      "Voulez vous vraiment supprimer \""
                                                        +sauvegarde.listCharger.getSelectedValue()
                                                        +"\" ?","Confirmation",
                                                      JOptionPane.YES_NO_OPTION,
                                                      JOptionPane.QUESTION_MESSAGE,
                                                      new ImageIcon("images/question.png"));
            if(value == JOptionPane.YES_OPTION) {
                String tmp = (String)sauvegarde.listCharger.getSelectedValue();
                File fichierTmp = new File(sauvegarde.path+tmp);
                fichierTmp.delete();
                sauvegarde.miseAJourListCharger();
            }
        } else if (e.getActionCommand().equals("charger")) {
            sauvegarde.charger((String)sauvegarde.listCharger.getSelectedValue());
            sauvegarde.fenetreCharger.dispose();
        } else if (e.getActionCommand().equals("fermer")) {
	        sauvegarde.fenetreCharger.dispose();
	        if (sauvegarde.a.j.J1 == null || sauvegarde.a.j.J2 == null || sauvegarde.a.j.h == null){
		        sauvegarde.a.etatSuivant = sauvegarde.a.OPTIONS;
	        }
        }
    }
}
