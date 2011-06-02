import java.awt.*;
import java.util.*;

public class Terrain{
	static final int TAB_SIZE = 9;
	Case[][] plateau;

	//constructeur
	public  Terrain(){
		init();
	}

	void init(){
		plateau = new Case[][] {
			{new Case("X"), new Case("X"), new Case("X"), new Case("X"), new Case("B"), new Case("N"), new Case("X"), new Case("X"), new Case("X")},
			{new Case("X"), new Case("B"), new Case("N"), new Case("B"), new Case("N"), new Case("B"), new Case("X"), new Case("X"), new Case("X")},
			{new Case("B"), new Case("N"), new Case("B"), new Case("N"), new Case("B"), new Case("N"), new Case("B"), new Case("X"), new Case("X")},
			{new Case("N"), new Case("B"), new Case("N"), new Case("B"), new Case("N"), new Case("B"), new Case("N"), new Case("X"), new Case("X")},
			{new Case("X"), new Case("N"), new Case("B"), new Case("N"), new Case("X"), new Case("N"), new Case("B"), new Case("N"), new Case("X")},
			{new Case("X"), new Case("X"), new Case("N"), new Case("B"), new Case("N"), new Case("B"), new Case("N"), new Case("B"), new Case("N")},
			{new Case("X"), new Case("X"), new Case("B"), new Case("N"), new Case("B"), new Case("N"), new Case("B"), new Case("N"), new Case("B")},
			{new Case("X"), new Case("X"), new Case("X"), new Case("B"), new Case("N"), new Case("B"), new Case("N"), new Case("B"), new Case("X")},
			{new Case("X"), new Case("X"), new Case("X"), new Case("N"), new Case("B"), new Case("X"), new Case("X"), new Case("X"), new Case("X")}
		};
	}

	//methodes de tests
	boolean estPossible(Coups c){
		if( okCoup(c) && estAdjacent(c.pDep.x,c.pDep.y,c.pArr.x,c.pArr.y)
			&& estInferieurASix(c.pDep.x,c.pDep.y,c.pArr.x,c.pArr.y))
			return 	true;
		else
			return false;
	}

	boolean okCoup(Coups c){
		return (c.pDep.x>-1 &&
		 		c.pDep.x< 9 &&
		 		c.pDep.y>-1 &&
		 		c.pDep.y< 9 &&
				c.pArr.x>-1 &&
				c.pArr.x< 9 &&
				c.pArr.y>-1 &&
				c.pArr.y< 9);
	}
	//true si le clik en Point correspond a une case possiblep our un deplacement
	boolean okAnim(Point p){
		return (p.x>-1 && p.x< 9 && p.y>-1 && p.y< 9) && plateau[p.x][p.y].estOccupee();
	}

	//Verifie que la case de destination est adjacente à la case source
	// (donc qu'elles sont différentes
	// Et qu'elle n'est pas vide
	public boolean estAdjacent(int lSource, int cSource, int lDest, int cDest) {
		return ((lDest != lSource || cDest != cSource) &&
		        (lSource == lDest || lSource == (lDest+1) ||
		         lSource == (lDest-1)) &&
		        (cSource == cDest || cSource == (cDest+1) ||
		         cSource == (cDest-1)) &&
		        plateau[lDest][cDest].estOccupee() &&
		        plateau[lSource][cSource].estOccupee());
	}

	public boolean estInferieurASix(int lSource, int cSource, int lDest, int cDest) {
		return (plateau[lSource][cSource].getTaille()+plateau[lDest][cDest].getTaille() <= 5);
	}

	public boolean resteDeplacement(){
		return nbDeplRestant()==0;
	}


	//deplacements
	public void deplacer(int lSource, int cSource, int lDest, int cDest){
		plateau[lDest][cDest].setContenu(plateau[lSource][cSource].getContenu()+plateau[lDest][cDest].getContenu());
		plateau[lSource][cSource].setContenu("");
	}
	public void deplacer(Coups c){
		int lSource = c.pDep.x;
		int cSource = c.pDep.y;
		int lDest = c.pArr.x;
		int cDest = c.pArr.y;
		plateau[lDest][cDest].setContenu(plateau[lSource][cSource].getContenu()+plateau[lDest][cDest].getContenu());
		plateau[lSource][cSource].setContenu("");
	}

	public void annuler(ElemHist e){
		plateau[e.lSource][e.cSource].setContenu(e.Source);
		plateau[e.lDest][e.cDest].setContenu(e.Dest);
	}


	//renvoi une liste de point
	public LinkedList<Point> adjacents(int i, int j){
		LinkedList<Point> list = new LinkedList<Point>();
		Point p;
		if (plateau[i][j].estOccupee()) {
			for (int k = i-1; k <= i+1; k++) {
				for (int l = j-1; l <= j+1 ; l++) {
					if (k >= 0 && k < 9 &&
					    l >= 0 && l < 9 && plateau[k][l].estOccupee()) {
						if (estAdjacent(i,j,k,l) &&
						    estInferieurASix(i,j,k,l)) {
							p = new Point(k,l);
							list.add(p);
						}
					}
				}
			}
		}
	return list;
	}

	public int nbDeplRestant(){
		int nbCoups = 0;
		int i,j;
		LinkedList<Point> liste;

		for(i=0;i<TAB_SIZE;i++){
			for(j=0;j<TAB_SIZE;j++){
				liste = adjacents(i,j);
				nbCoups = nbCoups + liste.size();
			}
		}
		return nbCoups;
	}

	public LinkedList<Coups> coupsRestants(){
		LinkedList<Coups> l1 = new LinkedList<Coups>();
		LinkedList<Point> l2;
		int i,j,k;
		Point a,b;
		for(i=0;i<TAB_SIZE;i++){
			for(j=0;j<TAB_SIZE;j++){
				l2 = adjacents(i,j);
				if(l2.size()!=0){
					for(k=0;k<l2.size();k++){
						a = new Point(i,j);
						b = l2.get(k);
						Coups c = new Coups(a,b);
						l1.add(c);
					}
				}
			}
		}
	 return l1;
	}

	LinkedList<Coups> enumTourPossible(){
		LinkedList<Coups> listCoups = new LinkedList<Coups>();
		LinkedList<Point> listPoint;
		int i,j,k;
		for (i=0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				listPoint = adjacents(i,j);
				Point pDep = new Point(i,j);
				Point pArr;
				if ( listPoint.size()>0){
					for( k = 0; k < listPoint.size(); k++ ){
						pArr = listPoint.get(k);
						if(plateau[i][j].getTaille() + plateau[pArr.x][pArr.y].getTaille() == 5){
							Coups c = new Coups(pDep,pArr);
							listCoups.add(c);
						}
					}
				}
			}
		}
		return listCoups;
	}
}

