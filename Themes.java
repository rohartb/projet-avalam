import java.awt.Color;

public class Themes {

	static final int THEME1 = 1;
	static private final  Color T1pionJ1  = new Color(31,18,0);
	static private final  Color T1pionJ2  = new Color(190,150,50);
	static private final  Color T1chiffre = new Color(255,255,255);
	static private final  Color T1plateau = new Color(102,51,0);
	static private final  Color T1vide    = new Color(0,0,0);
	static private final  Color T1fond    = new Color(51,153,153);
	static private final  String T1J1     = "images/j1t1.png";
	static private final  String T1O10    = "images/o10t1.png";
	static private final  String T1O11    = "images/o11t1.png";
	static private final  String T1O12    = "images/o12t1.png";
	static private final  String T1R1     = "images/r1t1.png";
	static private final  String T1J2     = "images/j2t1.png";
	static private final  String T1O20    = "images/o20t1.png";
	static private final  String T1O21    = "images/o21t1.png";
	static private final  String T1O22    = "images/o22t1.png";
	static private final  String T1R2     = "images/r2t1.png";


	static final int THEME3 = 3;
	static private final  Color T3pionJ1  = new Color(17,119,170);
	static private final  Color T3pionJ2  = new Color(255,255,255);
	static private final  Color T3chiffre = new Color(0,0,0);
	static private final  Color T3plateau = new Color(143,169,204);
	static private final  Color T3vide    = new Color(100,100,100);
	static private final  Color T3fond    = new Color(17,68,119);

	static private final  String T3J1     = "images/j1t3.png";
	static private final  String T3O10    = "images/o10t3.png";
	static private final  String T3O11    = "images/o11t3.png";
	static private final  String T3O12    = "images/o12t3.png";
	static private final  String T3R1     = "images/r1t3.png";
	static private final  String T3J2     = "images/j2t3.png";
	static private final  String T3O20    = "images/o20t3.png";
	static private final  String T3O21    = "images/o21t3.png";
	static private final  String T3O22    = "images/o22t3.png";
	static private final  String T3R2     = "images/r2t3.png";


	public Themes() {}

	static public Color getCouleurPionJ1(int theme) {
		switch(theme) {
		case THEME1:
			return T1pionJ1;
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
		case THEME3 :
			return T3fond;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public String getImageJ1(int theme) {
		switch(theme) {
		case THEME1 :
			return T1J1;
		case THEME3 :
			return T3J1;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public String getImageO10(int theme) {
		switch(theme) {
		case THEME1 :
				return T1O10;
		case THEME3 :
				return T3O10;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public String getImageO11(int theme) {
		switch(theme) {
		case THEME1 :
				return T1O11;
		case THEME3 :
				return T3O11;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public String getImageO12(int theme) {
		switch(theme) {
		case THEME1 :
				return T1O12;
		case THEME3 :
				return T3O12;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}


	static public String getImageR1(int theme) {
		switch(theme) {
		case THEME1 :
			return T1R1;
		case THEME3 :
			return T3R1;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public String getImageJ2(int theme) {
		switch(theme) {
		case THEME1 :
			return T1J2;
		case THEME3 :
			return T3J2;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public String getImageO20(int theme) {
		switch(theme) {
		case THEME1 :
			return T1O20;
		case THEME3 :
			return T3O20;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public String getImageO21(int theme) {
		switch(theme) {
		case THEME1 :
			return T1O21;
		case THEME3 :
			return T3O21;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}


	static public String getImageO22(int theme) {
		switch(theme) {
		case THEME1 :
			return T1O22;
		case THEME3 :
			return T3O22;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}

	static public String getImageR2(int theme) {
		switch(theme) {
		case THEME1 :
			return T1R2;
		case THEME3 :
			return T3R2;
		default :
			System.err.println("Erreur theme");
			return null;
		}
	}
}
