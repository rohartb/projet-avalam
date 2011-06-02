public class Joueur{
	Avalam a;

	String nom;
	int type; //0,1,2,3,4 
	int joueur; // 1 ou 2
	int score;
	

	public Joueur(Avalam a){
		this.a=a;
	}

	public boolean estHumain() {
		return (type == a.j.HUMAIN );
	}

	public boolean estRobot() {
		return (type == a.j.BOTLVL1 || type == a.j.BOTLVL2 ||
		        type == a.j.BOTLVL3);
	}

	public boolean estReseau() {
		return (type == a.j.RESEAU);
	}
}

