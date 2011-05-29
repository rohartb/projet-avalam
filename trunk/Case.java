import java.lang.String;

public class Case{

	private String contenu;

	//constructeur
	public Case(String c){
		contenu = c;
	}

	public String getContenu(){
		return contenu;
	}
	public void setContenu(String s) {
		contenu = s;
	}


	public int getTaille(){
		return contenu.length();
	}

	public char getSommet(){
		return contenu.charAt(0);
	}


	public boolean estVide(){
		return contenu.length()==0;
	}

	public boolean estJ1(){
		return getSommet()=='N';
	}

	public boolean estJ2(){
		return getSommet()=='B';
	}

	public boolean estVraieCase(){
		return !contenu.equals("X");
	}

	public boolean estOccupee(){
		return (estVraieCase() && !estVide());
	}
}


