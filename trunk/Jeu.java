import java.util.*;
import java.io.*;

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
	boolean modeNormal;

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
		c = new Coups(a.f.g.coordToIndice(a.f.g.click),a.f.g.coordToIndice(a.f.g.release));
	}

	public void jouerBot(){
		if(joueurCourant==1){
			c = J1.b.jouerBot();
		}
		else{
			c = J2.b.jouerBot();
		}
	}

	void init(){
		finPartie=false;
		pause=false;
		h = new Historique();
		File option = new File(System.getProperty("user.home")+"/.Avalam/Config/options.cfg");
		if(!option.exists()){
			J1.type=HUMAIN;
			J1.joueur=JOUEUR1;
			J1.nom="Joueur 1";
			J2.type=BOTLVL1;
			J2.joueur=JOUEUR2;
			J2.nom="Jôzay";
		}else{
			try{
				FileInputStream in = new FileInputStream(option);
				Scanner s = new Scanner(in);
				J1.type = s.nextInt();
				J2.type = s.nextInt();
				modeNormal = s.nextBoolean();
				s.nextLine();
				J1.nom = s.nextLine();
				J2.nom = s.nextLine();
			}catch(FileNotFoundException e){}
		}
		J1.actualiserBot();
		J2.actualiserBot();
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

	public boolean courantEstRobot() {
		if (courantEstJ1())
			return J1.estRobot();
		else
			return J2.estRobot();
	}

	public boolean courantEstHumain() {
		if (courantEstJ1())
			return J1.estHumain();
		else
			return J2.estHumain();
	}


	void actualiser(){
		nbCoupsRestants = a.t.nbDeplRestant();
		finPartie = nbCoupsRestants == 0;
	}


	public void calculerScore() {
		int scoreJ1 = 0;
		int scoreJ2 = 0;
		for (int i = 0; i < a.t.TAB_SIZE; i++) {
			for (int j = 0; j < a.t.TAB_SIZE; j++)
				if (a.t.plateau[i][j].estOccupee()) {

				}
		}
	}

	public void sauverOptions(){
		try {
			File config = new File(System.getProperty("user.home")+"/.Avalam/Config/");
			if(!config.exists())
				config.mkdir();
			File f = new File(System.getProperty("user.home")+"/.Avalam/Config/options.cfg");
			f.setWritable(true);
			String s = "";

			s += J1.type +" "+ J2.type +" "+a.j.modeNormal+"\n";
			s += J1.nom +"\n";
			s += J2.nom +"\n";

			FileOutputStream o = new FileOutputStream(f);
			PrintStream ps = new PrintStream(f);
			ps.print(s);
			f.setReadOnly();
		} catch (FileNotFoundException ex) {}
	}
}
