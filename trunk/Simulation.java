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
	
	public int evaluerScoreCourantDiff(){
		int score=0;
		for (int i=0; i<9; i++){
			for (int j=0; j<9; j++){
 				if(etatJeu[i][j].estOccupee()){
					if(etatJeu[i][j].estJ1()){
						if(estGagnee(i,j)){
							score = score + (100000 + ((5-etatJeu[i][j].getTaille())*10000));
						}
					}
					else{
						if(estGagnee(i,j)){
							score = score - (100000 + ((5-etatJeu[i][j].getTaille())*10000));
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
}
