import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.util.*;
import java.sql.Time;

class Sauvegarde{
	Avalam a;

	String path, nom, s;
	File f;

	public Sauvegarde(Avalam a){
		this.a = a;

		//creation du dossier .Avalam/Sauvegardes
		File sauver = new File(System.getProperty("user.home")+"/.Avalam/Sauvegardes");
		if(!sauver.exists())
			sauver.mkdir();

		path = System.getProperty("user.home")+"/.Avalam/Sauvegardes/";
	}

	void sauver(){

		a.f.s.timer.stop();

		nom = (String)JOptionPane.showInputDialog(a.f,"Entrez votre nom de sauvegarde","Sauvegarder",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Images/icone_sauvegarder.png"), null,a.j.J1.nom);
		f = new File(path+nom+".save");
		while(nom!=null && (f.exists() || nom.equals(""))){
			nom = (String) JOptionPane.showInputDialog(a.f,"Erreur : Fichier déjà existant ou nom vide! \n Entrez un autre nom de sauvegarde","Sauvegarder",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Images/icone_sauvegarder.png"), null,a.j.J1.nom);
			f = new File(path+nom+".save");
		}

		if(nom!=null){
			s=new String();
			s += a.j.joueurCourant+" ";
			s += a.j.J1.type+" ";
			s += a.j.J2.type+" ";
			s += a.f.s.start.getTime()+" ";
			s += a.j.modeNormal+"\n";
			s += a.j.J1.nom+"\n";
			s += a.j.J2.nom+"\n";
			for(int i=0; i<9; i++)
				for(int j=0; j<9; j++)
					s += a.t.plateau[i][j].getContenu()+"\n";
			s += a.j.finPartie+"\n";
			s += a.j.h.toString();

			try{
				FileOutputStream o = new FileOutputStream(f);
				PrintStream ps = new PrintStream(f);
				ps.print(s);
				f.setReadOnly();
				JOptionPane.showMessageDialog(a.f,"Partie sauvegardée","Avalam",JOptionPane.INFORMATION_MESSAGE);
			}catch(Exception e){}
		}
		else{
			a.etatSuivant = a.JEU;
			a.quit=false;
		}

		a.f.s.timer.start();
	}

	void charger(){
		a.f.s.timer.stop();

		//Pour afficher uniquement les .save
		FilenameFilter ff = new FilenameFilter() {
			public boolean accept(File f,String name) {
				return name.endsWith(".save");
			}
		};
		System.out.println("avaant");

		File dossier = new File(System.getProperty("user.home")+"/.Avalam/Sauvegardes/");
		String st = (String)JOptionPane.showInputDialog(a.f, "Choisissez votre partie à charger","Charger",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("./Images/icone_charger.png"),dossier.list(ff),null);
		st = System.getProperty("user.home")+"/.Avalam/Sauvegardes/" + st;
		File f = new File(st);
		System.out.println(f.getPath());

		if(f.exists()){
			try{
				FileInputStream in = new FileInputStream(f);
				Scanner s = new Scanner(in);
				a.j.joueurCourant = s.nextInt();
				a.j.J1.type = s.nextInt();
				a.j.J2.type = s.nextInt();
				long temps = s.nextLong();
				a.j.modeNormal = s.nextBoolean();
				s.nextLine(); //on revient a la ligne
				a.j.J1.nom = s.nextLine();
				a.j.J2.nom = s.nextLine();
				for(int i=0; i<9; i++)
					for(int j=0; j<9; j++)
						a.t.plateau[i][j].setContenu(s.nextLine());

				//historique
				a.j.h = new Historique();
				a.j.finPartie = s.nextBoolean();
				s.nextLine(); //on revient a la ligne

				while(s.hasNext()){
					a.j.h.ajouterAnnuler(new ElemHist(s.nextLine()));
				}

				a.f.s.start = new Time(temps);
				a.f.s.labelTemps.setText("  " + a.f.s.sdf.format(a.f.s.start));

				//grisage des options annuler et refaire et voir dernier coup
				//annuler visible si la pile n'est pas vide ET (mode normal OU fin de partie)
				if(!a.j.h.annulerVide() && (a.j.modeNormal || a.j.finPartie)){
					a.f.m.annuler.setEnabled(true);
				}else{
					a.f.m.annuler.setEnabled(false);
				}
				//rejouer visible si la pile n'est pas vide ET  (mode normal OU fin de partie)
				if(!a.j.h.rejouerVide() && (a.j.modeNormal) || a.j.finPartie){
					a.f.m.rejouer.setEnabled(true);
				}else{
					a.f.m.rejouer.setEnabled(false);
				}
				//dernier coup visible si la pile annuler n'est pas vide
				if(!a.j.h.annulerVide()){
					a.f.m.dernierCoup.setEnabled(true);
				}else{
					a.f.m.dernierCoup.setEnabled(false);
				}

			}catch(Exception e){
				System.out.println(e);
			}
		}
		a.f.s.timer.start();
	}
}
