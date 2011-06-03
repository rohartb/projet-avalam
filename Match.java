import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.Time;

class Match{
	Avalam a;
    
	String nombrePartie;
    int nbP;
    int scoreMJ1;
    int scoreMJ2;
    int nbToursJ1;
    int nbToursJ2;
    boolean finMatch;
    
	public Match(Avalam a){
		this.a = a;
    }
    
	void debutMatch() {
		boolean estPasBon = true;
        a.f.s.timer.stop();
		
        while(estPasBon) {
            nombrePartie = (String)JOptionPane.showInputDialog(a.f,"Bienvenue dans le mode Match\n"
                                                               + a.j.J1.nom + " contre " + a.j.J2.nom + "\n\n"
                                                               + "Veuillez entrer le nombre de partie a disputer","Match",JOptionPane.WARNING_MESSAGE,new ImageIcon("Images/match.png"), null,"3");
            if(nombrePartie== null) {
                a.etat=a.JEU;
                a.j.modeNormal = true;
                estPasBon = false;
                return;
            } else {
                try {
                    nbP = Integer.valueOf(nombrePartie);
                    estPasBon = false;
                } catch (Exception e){
                    estPasBon = true;
                }
            }
        }
        a.etat = a.ACTUALISER;
        a.etatSuivant = a.JEU;
        init();
        deroulementMatch();
    }
    
    public void init(){
        finMatch = false;
        scoreMJ1 = 0;
        scoreMJ2 = 0;
        nbToursJ1 = 0;
        nbToursJ2 = 0;
        a.f.m.options.setEnabled(false);
        a.f.m.sauvegarder.setEnabled(false);
        a.f.m.charger.setEnabled(false);
        //TODO desactiver la loupiote
    }
    
    public void deroulementMatch(){
        
    }
    
    public void finMatch(){
        //TODO popup de fin avec resultat et tout
        a.f.m.options.setEnabled(true);
        a.f.m.sauvegarder.setEnabled(true);
        a.f.m.charger.setEnabled(true);
        //TODO activer la loupiote
        a.j.modeNormal=true;
        
    }
    public void bb(){
        /*j.init();
        t.init();
        etatSuivant=JEU;
        etat = ACTUALISER;*/
    }
}
