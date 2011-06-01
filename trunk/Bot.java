import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Bot {
	Avalam a;
	int niveau;
	Random r;

	public Bot (Avalam a) {
		this.a = a;
		r = new Random();
	}
	
	
	public Coups jouerBot(){
	//On choisit pour chaque niveau de l'ordinateur une partie a jouer
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
			return jouerMinMax(0);
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
		Coups c,meilleurCoup;
		meilleurCoup = null;
		int tailleCoup;
		int val=0;

		if(a.j.joueurCourant==1){
			int max = -4242;
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
							max = val;
							meilleurCoup = c;
						}
						if(val==max){
							int e = r.nextInt(2);
							if(e==0){
								max = val;
								meilleurCoup = c;
							}
						}
						s.annulerCoup(c,tailleCoup);
					}
				}
			}
		}
		else{
			int min = 4242;
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
							min = val;
							meilleurCoup = c;
						}
						if(val==min){
							int e = r.nextInt(2);
							if(e==0){
								min = val;
								meilleurCoup = c;
							}
						}
						s.annulerCoup(c,tailleCoup);
					}
				}
			}
		}
		return meilleurCoup;
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
						val = max(s,prof-1,tour);
						if(val<min){
							min = val;
						}
						if(val==min){
							int e = r.nextInt(2);
							if(e==0){
								min = val;
							}
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
						val = min(s,prof-1,tour);;
						if(val>max){
							max = val;
						}
						if(val==max){
							int e = r.nextInt(2);
							if(e==0){
								max = val;
							}
						}
						s.annulerCoup(c,tailleCoup);
					}
				}
			}
			return max;
		}
	}
	
	public int eval(Simulation s, int tour){
		int score=0;
		if(s.partieFinie()){
			score = s.evaluerScoreFinal(1);
		}
		else{
			score = s.evaluerToursGagnees(1);
			if(s.tour5Possible()){
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
