class ElemHist{
	String Source, Dest;
	int lSource,lDest,cSource,cDest;
	Coups c;
	
	ElemHist(String s1, int x1, int y1, String s2, int x2, int y2){
		Source = s1;
		lSource = x1;
		cSource = y1;
		Dest = s2;
		lDest = x2;
		cDest = y2;
	}
	
	ElemHist(Coups c, Terrain t){
		Source = t.plateau[c.pDep.x][c.pDep.y].getContenu();
		lSource = c.pDep.x;
		cSource = c.pDep.y;
		Dest = t.plateau[c.pArr.x][c.pArr.y].getContenu();
		lDest = c.pArr.x;
		cDest = c.pArr.y;
	}
	
	ElemHist(String s){
		String[] tab = s.split(" ");
		Source = tab[0];
		lSource = Integer.parseInt(tab[1]);
		cSource = Integer.parseInt(tab[2]);
		Dest = tab[3];
		lDest = Integer.parseInt(tab[4]);
		cDest = Integer.parseInt(tab[5]);
	}
	
	public String toString(){
		return ""+Source+" "+lSource+" "+cSource+" "+Dest+" "+lDest+" "+cDest;
	}
}
