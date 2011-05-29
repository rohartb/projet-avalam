import java.awt.Color;

public class Themes {

	static final int THEME1 = 1;
	static private final  Color T1pionJ1  = new Color(31,18,0);
	static private final  Color T1pionJ2  = new Color(170,110,0);
	static private final  Color T1chiffre = new Color(255,255,255);
	static private final  Color T1plateau = new Color(85,134,0);
	static private final  Color T1vide    = new Color(0,0,0);
	static private final  Color T1fond    = new Color(85,34,0);

	static final int THEME2 = 2;
	static private final  Color T2pionJ1  = new Color(226,245,22);
	static private final  Color T2pionJ2  = new Color(245,141,22);
	static private final  Color T2chiffre = new Color(255,255,255);
	static private final  Color T2plateau = new Color(122,135,130);
	static private final  Color T2vide    = new Color(0,0,0);
	static private final  Color T2fond    = new Color(122,35,130);

	static final int THEME3 = 3;
	static private final  Color T3pionJ1  = new Color(204,255,102);
	static private final  Color T3pionJ2  = new Color(239,215,245);
	static private final  Color T3chiffre = new Color(0,0,0);
	static private final  Color T3plateau = new Color(143,69,204);
	static private final  Color T3vide    = new Color(0,0,0);
	static private final  Color T3fond    = new Color(143,169,204);


	public Themes() {
	}

	static public Color getCouleurPionJ1(int theme) {
		switch(theme) {
		case THEME1:
			return T1pionJ1;
		case THEME2:
			return T2pionJ1;
		case THEME3:
			return T3pionJ1;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public Color getCouleurPionJ2(int theme) {
		switch(theme) {
		case THEME1:
			return T1pionJ2;
		case THEME2:
			return T2pionJ2;
		case THEME3:
			return T3pionJ2;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public Color getCouleurPlateau(int theme) {
		switch(theme) {
		case THEME1:
			return T1plateau;
		case THEME2:
			return T2plateau;
		case THEME3:
			return T3plateau;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}


	static public Color getCouleurChiffre(int theme) {
		switch(theme) {
		case THEME1:
			return T1chiffre;
		case THEME2:
			return T2chiffre;
		case THEME3:
			return T3chiffre;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public Color getCouleurVide(int theme) {
		switch(theme) {
		case THEME1 :
			return T1vide;
		case THEME2 :
			return T2vide;
		case THEME3 :
			return T3vide;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public Color getCouleurFond(int theme) {
		switch(theme) {
		case THEME1 :
			return T1fond;
		case THEME2 :
			return T2fond;
		case THEME3 :
			return T3fond;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}
}
