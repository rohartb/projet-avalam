import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.Time;

class Sauvegarde{
	Avalam a;

	String path, nom, s;
	File f;

    JList listCharger;
    JButton fermer, charger, supprimer;
    JDialog fenetreCharger;
	Object[] listeFichiers;

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
			s += a.j.modeAide+"\n";
			s += a.j.J1.nom+"\n";
			s += a.j.J2.nom+"\n";
			for(int i=0; i<9; i++)
				for(int j=0; j<9; j++)
					s += a.t.plateau[i][j].getContenu()+"\n";
			s += a.j.revoirH+" "+a.j.finPartie+"\n";
			s += a.j.h.toString();

			try{
				FileOutputStream o = new FileOutputStream(f);
				PrintStream ps = new PrintStream(f);
				ps.print(s);
				f.setReadOnly();
				JOptionPane.showMessageDialog(a.f,"Partie sauvegardée","Avalam",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("./images/checked.png"));
			}catch(Exception e){}
		}
		else{
			a.quit=false;
		}
	}

	void charger(String st){
		File f = new File(path+st);
        try{
            FileInputStream in = new FileInputStream(f);
            Scanner s = new Scanner(in);
            a.j.joueurCourant = s.nextInt();
            a.j.J1.type = s.nextInt();
            a.j.J2.type = s.nextInt();
            long temps = s.nextLong();
            a.j.modeAide = s.nextBoolean();
            s.nextLine(); //on revient a la ligne
            a.j.J1.nom = s.nextLine();
            a.j.J2.nom = s.nextLine();
            for(int i=0; i<9; i++)
                for(int j=0; j<9; j++)
                    a.t.plateau[i][j].setContenu(s.nextLine());

            //historique
            a.j.h = new Historique();
            a.j.revoirH = s.nextBoolean();
            a.j.finPartie = s.nextBoolean();
            s.nextLine(); //on revient a la ligne

            while(s.hasNext()){
                a.j.h.ajouterAnnuler(new ElemHist(s.nextLine()));
            }
            a.f.s.start = new Time(temps);
            a.f.s.labelTemps.setText("  " + a.f.s.sdf.format(a.f.s.start));

            //grisage de l'aide si la partie est finie ou si on est dans historique
            if(a.j.revoirH || a.j.finPartie){
		        a.f.g.labelAmpoule.setEnabled(false);
				a.f.g.ea.actif=false;
			}else{
				a.f.g.labelAmpoule.setEnabled(true);
				a.f.g.ea.actif=true;
			}

            //grisage des options annuler et refaire et voir dernier coup
            //annuler visible si la pile n'est pas vide ET (mode normal OU fin de partie)
            if(!a.j.h.annulerVide() && (a.j.modeNormal || a.j.revoirH)){
                a.f.m.annuler.setEnabled(true);
                a.f.g.annuler.setEnabled(true);
            }else{
                a.f.m.annuler.setEnabled(false);
                a.f.g.annuler.setEnabled(false);
            }

            //rejouer visible si la pile n'est pas vide ET  (mode normal OU fin de partie)
            if(!a.j.h.rejouerVide() && (a.j.modeNormal) || a.j.revoirH){
                a.f.m.rejouer.setEnabled(true);
                a.f.g.rejouer.setEnabled(true);
            }else{
                a.f.m.rejouer.setEnabled(false);
                a.f.g.rejouer.setEnabled(false);
            }

            //dernier coup visible si la pile annuler n'est pas vide
            if(!a.j.h.annulerVide()){
                a.f.m.dernierCoup.setEnabled(true);
            } else {
                a.f.m.dernierCoup.setEnabled(false);
            }
            a.etatSuivant = a.ACTUALISER;
        }catch(Exception e){
				System.out.println(e);
        }
	}

    public void afficherCharger() {
        a.etatSuivant = a.JEU;

        fenetreCharger = new JDialog();
        fenetreCharger.setTitle("Charger");
        fenetreCharger.setModal(true);
        fenetreCharger.setResizable(false);
        fenetreCharger.setLayout(new BorderLayout(5,5));

        //Panel du centre, avec l'icone et la liste
        JPanel panelCentre = new JPanel(new FlowLayout());

            //List
            listCharger = new JList();
            listCharger.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            //Scroll List
            JScrollPane scrollList = new JScrollPane(listCharger);
            scrollList.setPreferredSize(new Dimension(250,100));

            //Icon
            JLabel icone = new JLabel(new ImageIcon("images/icone_charger.png"),JLabel.CENTER);

            panelCentre.add(icone);
            panelCentre.add(scrollList);

        fenetreCharger.add(panelCentre);
        ////////////////////////////////////////////////

        //Panel du bas, avec les trois boutons
        JPanel panelBas = new JPanel(new BorderLayout());

            //Panel Boutons :
            JPanel panelBoutons = new JPanel(new GridLayout(1,3,10,10));

            fermer = new JButton("Fermer");
            fermer.setActionCommand("fermer");
            panelBoutons.add(fermer);

            supprimer = new JButton("Supprimer");
            supprimer.setActionCommand("supprimer");
            panelBoutons.add(supprimer);

            charger = new JButton("Charger");
            charger.setActionCommand("charger");
            panelBoutons.add(charger);

            EcouteurDeSauvegarde es = new EcouteurDeSauvegarde(this);
            charger.addActionListener(es);
            supprimer.addActionListener(es);
            fermer.addActionListener(es);
            panelBas.add(panelBoutons);

            //Espaces pour aerer les boutons
            JPanel p1 = new JPanel();
            p1.setSize(40,300);
            JPanel p2 = new JPanel();
            p2.setSize(40,300);
            JPanel p3 = new JPanel();
            p3.setSize(250,40);
            JPanel p4 = new JPanel();
            p4.setSize(250,40);
            panelBas.add(p1,"North");
            panelBas.add(p2,"South");
            panelBas.add(p3,"East");
            panelBas.add(p4,"West");

        fenetreCharger.add(panelBas,"South");

        //Panel nord, label
        JPanel panelNord = new JPanel(new GridLayout(2,1));

            //Panel vide pour aerer
            panelNord.add(new JPanel());

            //Label
            JLabel labelNord = new JLabel("Selectionner une partie :",JLabel.CENTER);
            panelNord.add(labelNord);

        fenetreCharger.add(panelNord,BorderLayout.NORTH);

        //Panel Est pour decaler la list du bord droit
        JLabel pEst = new JLabel("        ");
        fenetreCharger.add(pEst,"East");

        //Mise a jour de la liste une fois toute l'interface "creer"
        miseAJourListCharger();

        // position initiale de la fenetre sur l'ecran
        fenetreCharger.pack();
		int resHauteur = Toolkit.getDefaultToolkit().getScreenSize().height;
		int resLargeur = Toolkit.getDefaultToolkit().getScreenSize().width;
		int hauteur  = (resHauteur - fenetreCharger.getSize().height)/2;
		int largeur  = (resLargeur - fenetreCharger.getSize().width)/2;
		fenetreCharger.setLocation(largeur, hauteur);

        fenetreCharger.setVisible(true);
    }

    public void miseAJourListCharger() {
        FilenameFilter ff = new FilenameFilter() {
			public boolean accept(File f,String name) {
				return name.endsWith(".save");
			}
		};

		File dossier = new File(System.getProperty("user.home")+"/.Avalam/Sauvegardes/");
        listeFichiers = dossier.list(ff);

        if(listeFichiers.length != 0) {
            listCharger.setListData(listeFichiers);
            listCharger.setSelectedIndex(0);
        } else {
            String[] tab = {"Aucune partie sauvegardee"};
            listCharger.setListData(tab);
            charger.setEnabled(false);
            supprimer.setEnabled(false);
        }
    }

	public boolean fichierAChargerSontPresents() {
        FilenameFilter ff = new FilenameFilter() {
			public boolean accept(File f,String name) {
				return name.endsWith(".save");
			}
		};

		File dossier = new File(System.getProperty("user.home")+"/.Avalam/Sauvegardes/");
        listeFichiers = dossier.list(ff);
        System.out.println(listeFichiers.length + " ata");
        if(listeFichiers.length > 0) {
	        return true;
        } else {
	        return false;
        }
	}
}
