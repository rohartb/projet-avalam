import javax.swing.*;
import java.awt.*;
import java.util.*;


public class Simulation{
	Case[][] etatJeu;

	public Simulation(Case[][] tab){
		etatJeu = new Case[9][9];
		Case c;
		for(int i=0; i<9;i++){
			for(int j=0;j<9; j++){
				c = new Case(tab[i][j].getContenu());
				etatJeu[i][j] = c;
			}
		}
	}
	
	public boolean estPossible(Coups c){
		return estInferieurASix(c.pDep.x, c.pDep.y,c.pArr.x, c.pArr.y);
	}
	
	public void simulerCoup(Coups c){
		etatJeu[c.pArr.x][c.pArr.y].setContenu(etatJeu[c.pDep.x][c.pDep.y].getContenu() +
		                                       etatJeu[c.pArr.x][c.pArr.y].getContenu());
		etatJeu[c.pDep.x][c.pDep.y].setContenu("");
	}

	public void annulerCoup(Coups c, int taille){
		String nvContenuSrc, nvContenuDst;
		nvContenuSrc = etatJeu[c.pArr.x][c.pArr.y].getContenu().substring(0,taille);
		nvContenuDst = etatJeu[c.pArr.x][c.pArr.y].getContenu().substring(taille);
		etatJeu[c.pDep.x][c.pDep.y].setContenu(nvContenuSrc);
		etatJeu[c.pArr.x][c.pArr.y].setContenu(nvContenuDst);
	}

	public boolean estAdjacent(int lSource, int cSource, int lDest, int cDest) {
		return ((lDest != lSource || cDest != cSource) &&
		        (lSource == lDest || lSource == (lDest+1) ||
		         lSource == (lDest-1)) &&
		        (cSource == cDest || cSource == (cDest+1) ||
		         cSource == (cDest-1)) &&
		        etatJeu[lDest][cDest].estOccupee() &&
		        etatJeu[lSource][cSource].estOccupee());
	}


	public boolean estInferieurASix(int lSource, int cSource, int lDest, int cDest) {
		return (etatJeu[lSource][cSource].getTaille() +
		        etatJeu[lDest][cDest].getTaille() <= 5);
	}

	LinkedList<Point> casesAdjacentes(int i, int j){
		LinkedList<Point> list = new LinkedList<Point>();
		Point p;
		if (etatJeu[i][j].estOccupee()) {
			for (int k = i-1; k <= i+1; k++) {
				for (int l = j-1; l <= j+1 ; l++) {
					if (k >= 0 && k < 9 &&
					    l >= 0 && l < 9 && etatJeu[k][l].estOccupee()) {
						if (estAdjacent(i,j,k,l) &&  estInferieurASix(i,j,k,l)) {
							p = new Point(k,l);
							list.add(p);
						}
					}
				}
			}
		}
	return list;
	}
	
	public boolean partieFinie(){
		for (int i=0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (etatJeu[i][j].estOccupee()) {
					for (int k = i-1; k <= i+1; k++) {
						for (int l = j-1; l <= j+1 ; l++) {
							if (k >= 0 && k < 9 &&
							    l >= 0 && l < 9 && etatJeu[k][l].estOccupee()) {
								if (estAdjacent(i,j,k,l) &&  estInferieurASix(i,j,k,l)) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	
	
	//strategies
	
	//differences de nombre de pions
	public int evaluerNbPions(){
		int score=0;
		for (int i=0; i<9; i++){
			for (int j=0; j<9; j++){
				if(etatJeu[i][j].estOccupee()){
					if(etatJeu[i][j].estJ1())
						score++;
					else
						score--;
				}
			}
		}
		return score;
	}
	
	//difference des tours definitives
	public int evaluerScoreCourant(){
		int score=0;
		for (int i=0; i<9; i++){
			for (int j=0; j<9; j++){
 				if(etatJeu[i][j].estOccupee()){
					if(etatJeu[i][j].estJ1()){
						if(estGagnee(i,j)){
							score++;
						}
					}
					else{
						if(estGagnee(i,j)){
							score--;
						}
					}
				}
			}
		}
		return score;
	}
	
	public boolean estGagnee(int i, int j){
		if(etatJeu[i][j].getTaille()==5){
			return true;
		}
		LinkedList<Point> l = new LinkedList<Point>();
		l = casesAdjacentes(i,j);
		if(l.isEmpty()){
			return true;
		}
		return false;
	}
	
	public int configMickey(){
		int score=0;
		for (int i=0; i<9; i++){
			for (int j=0; j<9; j++){
 				if(etatJeu[i][j].estOccupee()){
					score += estConfigMickey(i,j);
 				}
 			}
 		}
		return score/3; 			
	}
	
	public int estConfigMickey(int i, int j){
		LinkedList<Point> adj = casesAdjacentes(i,j);
		Point p1,p2,p3;
		if(adj.size()!=2)
			return 0;
		p1=new Point(i,j);
		p2=adj.get(0);
		p3=adj.get(1);
		int nb = 0;
		int taille = etatJeu[i][j].getTaille()+etatJeu[p2.x][p2.y].getTaille()+etatJeu[p3.x][p3.y].getTaille();
		if(taille > 5)
			return 0;
		if(etatJeu[p1.x][p1.y].estJ1())
			nb++;
		else
			nb--;
		if(etatJeu[p2.x][p2.y].estJ1())
			nb++;
		else
			nb--;
		if(etatJeu[p3.x][p3.y].estJ1())
			nb++;
		else
			nb--;
		if(Math.abs(nb) != 1)
			return 0;
			
		adj = casesAdjacentes(p2.x,p2.y);
		if(adj.size()!=2)
			return 0;
		if(!((adj.get(0).equals(p1)||adj.get(0).equals(p3))&&(adj.get(1).equals(p1)||adj.get(1).equals(p3))))
			return 0;
		adj = casesAdjacentes(p3.x,p3.y);
		if(adj.size()!=2)
			return 0;
		if(!((adj.get(0).equals(p1)||adj.get(0).equals(p2))&&(adj.get(1).equals(p1)||adj.get(1).equals(p2))))
			return 0;
		return nb;
	}
	
	public int tourDefinitive3AuCentre(int joueur){
		int res = 0;
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				if(etatJeu[i][j].estOccupee() && etatJeu[i][j].getTaille()==3){
					if( (joueur==1 && etatJeu[i][j].estJ2()) || (joueur==2 && etatJeu[i][j].estJ1()) ){
						if(estBonneConfig3(i,j,joueur)){
							res++;
						}
					}
				}
			}
		}
		return res;
	}
	
	public boolean deuxToursIsolees(){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				if(etatJeu[i][j].estOccupee() && etatJeu[i][j].getTaille()<5 && etatJeu[i][j].estJ2()){
					if(estConfigIsolee(i,j)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean estConfigIsolee(int i, int j){
		int nbSucc=0;
		int nbJ1=0;
		int u=0;
		int v=0;
		for (int k = i-1; k <= i+1; k++) {
			for (int l = j-1; l <= j+1 ; l++) {
				if(!(i==k && j==l)){
					if (k >= 0 && k < 9 && l >= 0 && l < 9 && etatJeu[k][l].estOccupee()) {
						nbSucc++;
						if(etatJeu[k][l].estJ1()){
							nbJ1++;
							u = k;
							v = l;
						}
			 		}
			 	}
		 	}
		}
		if(nbSucc!=1 || nbJ1!=1){
			return false;
		}
		else{
			for (int k = u-1; k <= u+1; k++) {
				for (int l = v-1; l <= v+1 ; l++) {
					if(!((k==u && l==v) || (k==i && l==j)) ){
						if (k >= 0 && k < 9 && l >= 0 && l < 9 && etatJeu[k][l].estOccupee()) {
							return false;
						}
					}
				}
			}
			return true;
		}			
	}
	
	public int tourDefinitive2AuCentre(int joueur){
		int res = 0;
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				if(etatJeu[i][j].estOccupee() && etatJeu[i][j].getTaille()==2){
					if( (joueur==1 && etatJeu[i][j].estJ2()) || (joueur==2 && etatJeu[i][j].estJ1()) ){
						if(estBonneConfig2(i,j,joueur)){
							res++;
						}
					}
				}
			}
		}
		return res;
	}
	
	
	public boolean estBonneConfig2(int i, int j, int c){
		boolean pasDeCouleurAdv = true;
		boolean auMoins1PionJoueur = false;
		for (int k = i-1; k <= i+1; k++) {
			for (int l = j-1; l <= j+1 ; l++) {
				if (k >= 0 && k < 9 && l >= 0 && l < 9 && etatJeu[k][l].estOccupee()) {
					if(!(i==k && j==l)){
						if( (c==1 && etatJeu[k][l].estJ2()) || (c==2 && etatJeu[k][l].estJ1()) ){
							if(etatJeu[k][l].getTaille()==1){
								pasDeCouleurAdv = false;
							}
						}
						if( (c==1 && etatJeu[k][l].estJ1()) || (c==2 && etatJeu[k][l].estJ2()) ){
							if(etatJeu[k][l].getTaille()==2){
			 					auMoins1PionJoueur = true;
			 				}
			 			}
			 		}
		 		}
		 	}
		 }
		return pasDeCouleurAdv && auMoins1PionJoueur;
	}
								    
								    
	public boolean estBonneConfig3(int i, int j, int joueur){
		boolean pasDeCouleurAdv = true;
		boolean auMoins1PionJoueur = false;
		for (int k = i-1; k <= i+1; k++) {
			for (int l = j-1; l <= j+1 ; l++) {
				if (k >= 0 && k < 9 && l >= 0 && l < 9 && etatJeu[k][l].estOccupee()) {
					if(!(i==k && j==l)){
						if( (joueur==1 && etatJeu[k][l].estJ2()) || (joueur==2 && etatJeu[k][l].estJ1()) ){
							if(etatJeu[k][l].getTaille()==1){
								pasDeCouleurAdv = false;
							}
						}
						if( (joueur==1 && etatJeu[k][l].estJ1()) || (joueur==2 && etatJeu[k][l].estJ2()) ){
							if(etatJeu[k][l].getTaille()==1){
			 					auMoins1PionJoueur = true;
			 				}
			 			}
			 		}
		 		}
		 	}
		 }
		return pasDeCouleurAdv && auMoins1PionJoueur;
	}
	
	
	public boolean tour5Possible(){
		LinkedList<Point> listPoint;
		int i,j,k;
		for (i=0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				if(etatJeu[i][j].estOccupee()){
					if(etatJeu[i][j].estJ1()){
						listPoint = casesAdjacentes(i,j);
						Point pDep = new Point(i,j);
						Point pArr;
						if (listPoint.size()>0){
							for( k = 0; k < listPoint.size(); k++ ){
								pArr = listPoint.get(k);
								if(etatJeu[pArr.x][pArr.y].estJ2()){
									if(etatJeu[i][j].getTaille() + etatJeu[pArr.x][pArr.y].getTaille() == 5){
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
}
