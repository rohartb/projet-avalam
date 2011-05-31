import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

class Sauvegarde{
	Avalam a;
	
	String path, nom, s;
	File f;
	
	public Sauvegarde(Avalam a){
		this.a = a;
		path = System.getProperty("user.home")+"/.Avalam/Sauvegardes/";
		
	}
	
	void sauver(){
		nom = (String)JOptionPane.showInputDialog(a.f,"Entrez votre nom de sauvegarde","Sauvegarder",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Images/icone_sauvegarder.png"), null,a.j.J1.nom);
		f = new File(path+nom+".save");
		while(f.exists() || nom.equals("")){
			nom = (String) JOptionPane.showInputDialog(a.f,"Erreur : Fichier déjà existant ou nom vide! \n Entrez un autre nom de sauvegarde","Sauvegarder",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Images/icone_sauvegarder.png"), null,a.j.J1.nom);
		}
		
		s=new String();
		s += a.j.joueurCourant+" ";
		s += a.j.J1.type+" ";
		s += a.j.J1.type+" ";
		s += a.f.s.timer+" ";
		s += a.j.mode+"\n";
	}
}
