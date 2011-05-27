public class Jeu{
	Avalam a;

	Joueur J1,J2;
	Coups c;
	Historique h;
	Options o;

	boolean finPartie;
	boolean pause;

	static final int HUMAIN =0;
	static final int BOTLVL1=1;
	static final int BOTLVL2=2;
	static final int BOTLVL3=3;
	static final int RESEAU =4;

	public Jeu(Avalam a){
		this.a = a;
		J1 = new Joueur(a);
		J2 = new Joueur(a);
	}

	public void calculerCoup(){
		c = new Coups(a.f.g.coordToIndince(a.f.g.click),a.f.g.coordToIndince(a.f.g.release));
	}

	void init(){
		finPartie=false;
		pause=false;

		h = new Historique();
	}

}
