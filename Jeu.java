class Jeu{
	Avalam a;
	
	Joueur J1,J2;
	Coups c;
	Historique h;
	
	boolean finPartie;
	boolean pause;
	
	
	public Jeu(Avalam a){
		this.a = a;
		J1 = new Joueur(a);
		J1 = new Joueur(a);
	}
	
	void calculerCoup(){
		c = new Coups(a.f.g.coordToIndince(a.f.g.click),a.f.g.coordToIndince(a.f.g.release));
	}
	
	void init(){
		finPartie=false;
		pause=false;
		
		h = new Historique();
	}
	
}
