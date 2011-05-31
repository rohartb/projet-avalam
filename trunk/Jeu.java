public class Jeu{
	Avalam a;

	Joueur J1,J2;
	Coups c;
	Historique h;
	Options o;

	boolean finPartie;
	boolean pause;
	int joueurCourant;
	int nbCoupsRestants;
	int mode;

	static final int HUMAIN =0;
	static final int BOTLVL1=1;
	static final int BOTLVL2=2;
	static final int BOTLVL3=3;
	static final int RESEAU =4;

	static final int JOUEUR1=1;
	static final int JOUEUR2=2;
	
	static final int NORMAL=0;
	static final int MATCH=1;

	public Jeu(Avalam a){
		this.a = a;
		J1 = new Joueur(a);
		J2 = new Joueur(a);
		joueurCourant=JOUEUR1;
		nbCoupsRestants=292;
	}

	public void calculerCoup(){
		c = new Coups(a.f.g.coordToIndince(a.f.g.click),a.f.g.coordToIndince(a.f.g.release));
	}

	void init(){
		finPartie=false;
		pause=false;
		h = new Historique();
		J1.type=HUMAIN;
		J1.joueur=JOUEUR1;
		J2.type=BOTLVL1;
		J2.joueur=JOUEUR2;
		a.f.s.initTimer();
	}

	void changerJoueur(){
		if (joueurCourant == JOUEUR1) {
			joueurCourant = JOUEUR2;
		} else {
			joueurCourant = JOUEUR1;
		}
	}

	public boolean courantEstJ1() {
		return (joueurCourant == 1);
	}

	public boolean courantEstJ2() {
		return (joueurCourant == 2);
	}
	
	void actualiser(){
		nbCoupsRestants = a.t.nbDeplRestant();
		System.out.println(nbCoupsRestants);
		finPartie = nbCoupsRestants == 0;
			
	}
}

