public class Joueur{
	Avalam a;

	Bot b;

	String nom;
	int type;
	int joueur;

	public Joueur(Avalam a){
		this.a=a;
		nom = "tutu";
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

