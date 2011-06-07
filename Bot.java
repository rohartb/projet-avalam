import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Bot implements Runnable{
	Avalam a;
	Random r;
	int niveau;
	Coups coupTemp;
	int n,nMax;
	boolean finEval;
	Simulation s;
	
	public Bot (Avalam a) {
		this.a = a;
		r = new Random();
		finEval=false;
		coupTemp=null;
		n=0;
	}
	
	synchronized void pause(int n){
		try{
			//System.out.println("pause");
			this.wait(n);
		}catch(InterruptedException e){
			System.out.println(e);
		}
	}
	
	public void run() {
		if(a.j.joueurCourant==1)
			niveau=a.j.J1.type;
		else
			niveau=a.j.J2.type;
		if(niveau==0)//humain demande aide
			niveau=3;
		finEval=false;
		if(niveau==1)
			nMax=0;
		if(niveau==2)
			nMax=1;		
		if(niveau==3)
			nMax=1000;//on limite pas le jouer difficile
		n=0;
		while(!finEval && n<=nMax){
			coupTemp=jouerMinMax(n);
			n++;
			a.j.c = new Coups(coupTemp.pDep,coupTemp.pArr);
		}
	}
	
	public Coups jouerMinMax(int profondeur){
		System.out.println("MinMax "+profondeur);
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
					if(s.etatJeu[i][j].estOccupee()){
						pSrc = new Point(i,j);
						tailleCoup = s.etatJeu[i][j].getTaille();
						l = s.casesAdjacentes(i,j);
						while(!l.isEmpty()){
							pDst = l.removeFirst();
							c = new Coups(pSrc, pDst);
							s.simulerCoup(c);
							val = min(profondeur);
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
			System.out.println("max:"+max);
		}		
		else{
			int min = 999999999;
			for(int i=0; i<9; i++){
				for(int j=0; j<9; j++){
					if(s.etatJeu[i][j].estOccupee()){
						pSrc = new Point(i,j);
						tailleCoup = s.etatJeu[i][j].getTaille();
						l = s.casesAdjacentes(i,j);
						while(!l.isEmpty()){
							pDst = l.removeFirst();
							c = new Coups(pSrc, pDst);
							s.simulerCoup(c);
							val = max(profondeur);
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
			System.out.println("min:"+min);
		}
		if(randomcoup.size()==1){
			return randomcoup.get(0);
		}
		else{
			return randomcoup.get(r.nextInt(randomcoup.size()));
		}
	}
	


	public int min( int prof){
		if(prof==0 || s.partieFinie()){
			if(s.partieFinie())
				finEval=true;
			return eval();
		}
		else{
			int min = 999999999;
			int val,tailleCoup;
			Point pSrc, pDst;
			LinkedList<Point> l;
			Coups c;
			for(int i=0; i<9; i++){
				for(int j=0; j<9; j++){
					if(s.etatJeu[i][j].estOccupee()){
						pSrc = new Point(i,j);
						tailleCoup = s.etatJeu[i][j].getTaille();
						l = s.casesAdjacentes(i,j);
						while(!l.isEmpty()){
							pDst = l.removeFirst();
							c = new Coups(pSrc, pDst);
							s.simulerCoup(c);
							val = max(prof-1);
							if(val<min){
								min = val;
							}
							s.annulerCoup(c,tailleCoup);
						}
					}
				}
			}
			return min;
		}
	}


	public int max( int prof){
		if(prof==0 || s.partieFinie()){
			if(s.partieFinie())
				finEval=true;
			return eval();			
		}
		else{
			int max = -999999999;
			int val,tailleCoup;
			Point pSrc, pDst;
			LinkedList<Point> l;
			Coups c;
			for(int i=0; i<9; i++){
				for(int j=0; j<9; j++){
					if(s.etatJeu[i][j].estOccupee()){
						pSrc = new Point(i,j);
						tailleCoup = s.etatJeu[i][j].getTaille();
						l = s.casesAdjacentes(i,j);
						while(!l.isEmpty()){
							pDst = l.removeFirst();
							c = new Coups(pSrc, pDst);
							s.simulerCoup(c);
							val = min(prof-1);;
							if(val>max){
								max = val;
							}
							s.annulerCoup(c,tailleCoup);
						}
					}
				}
			}
			return max;
		}
	}
	
	static int TOURFIN=10000000;
	static int TOURDEF=100000;
	static int TRUC=1000;
	static int PIONS=10;
	
	public int eval(){
		int score=0;
		if(s.partieFinie()){
			score = s.evaluerNbPions()*TOURFIN;
		}else{
			if(niveau<=3)
				score += s.evaluerScoreCourant()*TOURDEF;
			if(niveau>1)
				score += s.evaluerNbPions()*PIONS;
			/*if(niveau>2){
				score += s.evaluerScoreCourantDiff();
			}*/
		}
		return score;
	}
	
}
