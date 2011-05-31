import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Bot {
	Avalam a;
	int niveau;
	int bot, adv;
	int nbcoupsjoues;

	public Bot (Avalam a, int niveau, int joueur) {
		this.a = a;
		this.niveau = niveau;
		bot = joueur;
		if(joueur==1){
			adv=2;
		}
		else{
			adv=1;
		}
		nbcoupsjoues=0;
	}
	
	public Coups choisirBot(){
	//On choisit pour chaque niveau de l'ordinateur une partie a jouer
		switch(niveau){
			case 1 : //bot lvl1
				return partieFacile();
				break;
			case 2 : // botlvl2
				return partieMoyen();			
				break;
			case 3 : 
				return partieDifficile();
				break;
		}
	}
	
	public Coups partieFacile(){
	
	}
	
	public Coups partieMoyen(){
	
	}
	
	public Coups partieDifficile(){
		if(nbcoupsjoues<40){
			nbcoupsjoues++;
			return jouerMinMax(1);
		}
		else{
			return jouerMinMax(3);
		}
	}
	
	
	public Coups jouerMinMax(int profondeur){
		Simulation s = new Simulation(a.t.plateau);
		/*System.out.println("toursGagnees"+s.evaluerToursGagnees(bot));
		System.out.println("tour 5: "+s.tour5Possible());
		System.out.println("tourDef3Bot: "+s.tourDefinitive3AuCentre(bot));
		System.out.println("tourDef3Adv: "+s.tourDefinitive3AuCentre(adv));
		System.out.println("tourDef2Bot: "+s.tourDefinitive2AuCentre(bot));
		System.out.println("tourDef2Adv: "+s.tourDefinitive2AuCentre(adv));
		System.out.println("toursIsolees: "+s.deuxToursIsolees());*/
		int max = -4242;
		LinkedList<Point> l;
		Point pSrc, pDst;
		Coups c,meilleurCoup;
		meilleurCoup = null;
		int tailleCoup;
		int val=0;

		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				pSrc = new Point(i,j);
				tailleCoup = s.etatJeu[i][j].getTaille();
				l = s.casesAdjacentes(i,j);
				while(!l.isEmpty()){
					pDst = l.removeFirst();
					c = new Coups(pSrc, pDst);
					s.simulerCoup(c);
					val = min(s,profondeur);
					if(val>=max){
						max = val;
						meilleurCoup = c;
					}
					s.annulerCoup(c,tailleCoup);
				}
			}
		}
		return meilleurCoup;
	}

	public int min(Simulation s, int prof){
		if(prof==0 || s.partieFinie()){
			return evalMin(s);
		}
		else{
			int min = 4242;
			int val,tailleCoup;
			Point pSrc, pDst;
			LinkedList<Point> l;
			Coups c;
			for(int i=0; i<9; i++){
				for(int j=0; j<9; j++){
					pSrc = new Point(i,j);
					tailleCoup = s.etatJeu[i][j].getTaille();
					l = s.casesAdjacentes(i,j);
					while(!l.isEmpty()){
						pDst = l.removeFirst();
						c = new Coups(pSrc, pDst);
						s.simulerCoup(c);
						val = max(s,prof-1);
						if(val<min){
							min = val;
						}
						s.annulerCoup(c,tailleCoup);
					}
				}
			}
			//System.out.println(min);
			return min;
		}
	}


	public int max(Simulation s, int prof){
		if(prof==0 || s.partieFinie()){
			return evalMax(s);			
		}
		else{
			int max = -42;
			int val,tailleCoup;
			Point pSrc, pDst;
			LinkedList<Point> l;
			Coups c;
			for(int i=0; i<9; i++){
				for(int j=0; j<9; j++){
					pSrc = new Point(i,j);
					tailleCoup = s.etatJeu[i][j].getTaille();
					l = s.casesAdjacentes(i,j);
					while(!l.isEmpty()){
						pDst = l.removeFirst();
						c = new Coups(pSrc, pDst);
						s.simulerCoup(c);
						val = min(s,prof-1);;
						if(val>max){
							max = val;
						}
						s.annulerCoup(c,tailleCoup);
					}
				}
			}
			return max;
		}
	}
	
	public int evalMax(Simulation s){
		//System.out.println("evalMax");
		int score=0;
		if(s.partieFinie()){
			score = s.evaluerScoreFinal(bot);
		}
		else{
			score = s.evaluerToursGagnees(bot);
			score = score + s.tour5Possible();
			score = score + s.tourDefinitive3AuCentre(bot);	
			score = score - s.tourDefinitive3AuCentre(adv);
			score = score + s.tourDefinitive2AuCentre(bot);
			score = score - s.tourDefinitive2AuCentre(adv);
			score = score + s.deuxToursIsolees();
			//...
			
		}
		return score;
	}
	
	public int evalMin(Simulation s){
		int score=0;
		if(s.partieFinie()){
			score = s.evaluerScoreFinal(bot);
		}
		else{
			score = s.evaluerToursGagnees(bot);
			score = score - s.tour5Possible();
			score = score + s.tourDefinitive3AuCentre(bot);	
			score = score - s.tourDefinitive3AuCentre(adv);
			score = score + s.tourDefinitive2AuCentre(bot);
			score = score - s.tourDefinitive2AuCentre(adv);			
			score = score - s.deuxToursIsolees();
			//...
		}
		return score;
	}
}
