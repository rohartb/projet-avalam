import java.util.*;
import java.io.*;

public class Jeu{
	Avalam a;

	Thread thBot;
	Bot b;

	Joueur J1,J2;
	Coups c;
	Historique h;
	Options o;

	boolean partieEnReseau;
	boolean finPartie;
	boolean pause;
	int joueurCourant;
	int nbCoupsRestants;
	boolean modeNormal;
	boolean modeAide;
	boolean revoirH;

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
		b = new Bot(a);
		thBot=new Thread(b);
		joueurCourant=JOUEUR1;
		nbCoupsRestants=292;
	}

	public void calculerCoup(){
		c = new Coups(a.f.g.coordToIndice(a.f.g.click),a.f.g.coordToIndice(a.f.g.release));
	}

	public void jouerBot(){
		thBot=new Thread(b);
		thBot.start();
		a.pause(2000);
		thBot.stop();
	}

	void init(){
		finPartie=false;
		revoirH =false;
		pause=false;
		h = new Historique();
		File option = new File(System.getProperty("user.home")+"/.Avalam/Config/options.cfg");
		if(!option.exists()){
			J1.type=HUMAIN;
			J1.joueur=JOUEUR1;
			J1.nom="";
			J2.type=BOTLVL1;
			J2.joueur=JOUEUR2;
			J2.nom="";
			modeNormal = true;
			modeAide = true;
		}else{
			try{
				FileInputStream in = new FileInputStream(option);
				Scanner s = new Scanner(in);
				J1.type = s.nextInt();
				J2.type = s.nextInt();
				modeAide = s.nextBoolean();
				s.nextLine();
				J1.nom = s.nextLine();
				J2.nom = s.nextLine();
			}catch(FileNotFoundException e){}
		}
		joueurCourant=JOUEUR1;
		a.f.s.initTimer();
		actualiser();
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

	public boolean courantEstReseau() {
		if (courantEstJ1())
			return J1.estReseau();
		else
			return J2.estReseau();
	}

	public String getModeString() {
		if (modeNormal)
			return "Normal";
		else
			return "Match";
	}


	void actualiser(){
		nbCoupsRestants = a.t.nbDeplRestant();
		if(!finPartie && !revoirH) {
			finPartie = (nbCoupsRestants == 0);
		}
		calculerScore();
		actualiserlabelTexte();
	}


	public void actualiserlabelTexte() {
		if(!revoirH){
			if (courantEstHumain()) {
				if (courantEstJ1())
					a.f.g.etatJeul1 = J1.nom;
				else
					a.f.g.etatJeul1 = J2.nom;
				a.f.g.etatJeul2 = "c'est à toi !";
			} else if (courantEstRobot()) {
				if (courantEstJ1())
					a.f.g.etatJeul1 = J1.nom;
				else
					a.f.g.etatJeul1 =  J2.nom;
				a.f.g.etatJeul2 = "réfléchit ...";
			} else if (courantEstReseau()) {
				a.f.g.etatJeul1 = "attente du ";
				a.f.g.etatJeul2 = "coups réseau";
			}
		}else{
			a.f.g.etatJeul1 =  "Lecture";
			a.f.g.etatJeul2 = "d'historique";
		}
	}

	public void calculerScore() {
		int scoreJ1 = 0;
		int scoreJ2 = 0;
		for (int i = 0; i < a.t.TAB_SIZE; i++) {
			for (int j = 0; j < a.t.TAB_SIZE; j++)
				if (a.t.plateau[i][j].estOccupee() && a.t.adjacents(i,j).isEmpty()) {
					if ( a.t.plateau[i][j].estJ1())
						scoreJ1++;
					else if (a.t.plateau[i][j].estJ2())
						scoreJ2++;
				}
		}
		J1.score = scoreJ1;
		J2.score = scoreJ2;
	}

	public void sauverOptions(){
		try {
			File config = new File(System.getProperty("user.home")+"/.Avalam/Config/");
			if(!config.exists())
				config.mkdir();
			File f = new File(System.getProperty("user.home")+"/.Avalam/Config/options.cfg");
			f.setWritable(true);
			String s = "";

			s += J1.type +" "+ J2.type +" "+modeAide+"\n";
			s += J1.nom +"\n";
			s += J2.nom +"\n";

			FileOutputStream o = new FileOutputStream(f);
			PrintStream ps = new PrintStream(f);
			ps.print(s);
			f.setReadOnly();
		} catch (FileNotFoundException ex) {}
	}
}

