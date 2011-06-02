import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Bot {
	Avalam a;
	Random r;
	int niveau;
	public Bot (Avalam a) {
		this.a = a;
		r = new Random();
	}
	
	
	public Coups jouer(){
	//On choisit pour chaque niveau de l'ordinateur une partie a jouer
		if(a.j.joueurCourant==1)
			niveau=a.j.J1.type;
		else
			niveau=a.j.J2.type;
			
		switch(niveau){
			case 1 : //bot lvl1
				return partieFacile();			
			case 2 : // botlvl2
				return partieMoyen();		
			case 3 : 
				return partieDifficile();
			default :
				return null;
		}
		
	}
	
	public Coups partieFacile(){
		return jouerMinMax(0);
	}
	
	public Coups partieMoyen(){
		return jouerMinMax(0);
	}
	
	public Coups partieDifficile(){
		if(a.j.nbCoupsRestants>100){
			return jouerMinMax(1);
		}
		else{
			return jouerMinMax(2);
		}
	}
	
	
	public Coups jouerMinMax(int profondeur){
		System.out.println("MinMax "+profondeur);
		Simulation s = new Simulation(a.t.plateau);
		LinkedList<Point> l;
		Point pSrc, pDst;
		Coups c;
		int tailleCoup;
		int val=0;
		LinkedList<Coups> randomcoup = new LinkedList<Coups>();
		if(a.j.joueurCourant==1){
			int max = -999999999;
			for(int i=0; i<9; i++){
				for(int j=0; j<9; j++){
					pSrc = new Point(i,j);
					tailleCoup = s.etatJeu[i][j].getTaille();
					l = s.casesAdjacentes(i,j);
					while(!l.isEmpty()){
						pDst = l.removeFirst();
						c = new Coups(pSrc, pDst);
						s.simulerCoup(c);
						val = min(s,profondeur,2);
						if(val>max){
							randomcoup = new LinkedList<Coups>();
							randomcoup.add(c);
							max = val;
						}
						if(val==max){
							randomcoup.add(c);
						}
						s.annulerCoup(c,tailleCoup);
					}
				}
			}
		}
		else{
			int min = 999999999;
			for(int i=0; i<9; i++){
				for(int j=0; j<9; j++){
					pSrc = new Point(i,j);
					tailleCoup = s.etatJeu[i][j].getTaille();
					l = s.casesAdjacentes(i,j);
					while(!l.isEmpty()){
						pDst = l.removeFirst();
						c = new Coups(pSrc, pDst);
						s.simulerCoup(c);
						val = max(s,profondeur,1);
						if(val<min){
							randomcoup = new LinkedList<Coups>();
							randomcoup.add(c);
							min = val;
						}
						if(val==min){
							randomcoup.add(c);
						}
						s.annulerCoup(c,tailleCoup);
					}
				}
			}
		}
		if(randomcoup.size()==1)
			return randomcoup.get(0);
		else
			return randomcoup.get(r.nextInt(randomcoup.size()));
	}
	


	public int min(Simulation s, int prof, int tour){
		if(prof==0 || s.partieFinie()){
			return eval(s,tour);
		}
		else{
			if(tour==1){
				tour=2;
			}
			else{
				tour=1;
			}
			int min = 999999999;
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
						val = max(s,prof-1,tour);
						if(val<min){
							min = val;
						}
						s.annulerCoup(c,tailleCoup);
					}
				}
			}
			return min;
		}
	}


	public int max(Simulation s, int prof, int tour){
		if(prof==0 || s.partieFinie()){
			return eval(s,tour);			
		}
		else{
			if(tour==1){
				tour=2;
			}
			else{
				tour=1;
			}
			int max = -999999999;
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
						val = min(s,prof-1,tour);;
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
	
	static int TOURFIN=1000;
	static int TOURDEF=100;
	
	public int eval(Simulation s, int tour){
		int score=0;
		if(s.partieFinie()){
			score = s.evaluerScoreFinal()*TOURFIN;
		}else{
			score = s.evaluerToursGagnees()*TOURDEF;
			
			
		/*	if(s.tour5Possible()){
				if(tour==1){
					score = score + 3;
				}
				else{
					score = score-2;
				}
			}
			else{
				if(s.deuxToursIsolees()){
					if(tour==1){
						score = score + 2;
					}
					else{
						score = score-2;
					}
				}
			}
			
			/*score = score + s.tourDefinitive3AuCentre(1);	
			score = score + s.tourDefinitive2AuCentre(1);*/			
		}
		return score;
	}
	
}
