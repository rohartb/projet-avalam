import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class EcouteurDeMatch implements ActionListener, WindowListener {
    Match match;
    
    public EcouteurDeMatch(Match m) {
        match = m;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ok")) {
            match.nbPartiesTotales = match.slider.getValue();
            match.init();
            match.fenetre.dispose();
        } else if (e.getActionCommand().equals("annuler")) {
            match.finDeMatch();
            match.a.etat = match.a.ACTUALISER;
            match.a.etatSuivant = match.a.JEU;
            match.fenetre.dispose();
        }
    }
    
    public void windowActivated(WindowEvent e){}
    public void windowClosed(WindowEvent e){}
    
    public void windowClosing(WindowEvent e) {
        match.finDeMatch();
		match.a.etat = match.a.ACTUALISER;
        match.a.etatSuivant = match.a.JEU;
        match.fenetre.dispose();
	}
	
    public void windowDeactivated(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    public void windowIconified(WindowEvent e){}
    public void windowOpened(WindowEvent e){}
}