import javax.swing.*;
import java.awt.*;
import java.awt.image.*;;
import java.util.*;
import javax.imageio.*;
import java.io.*;
import java.awt.geom.*;

class TerrainGraphique extends JComponent{
	Avalam a;
	private static final long serialVersionUID = 1L;
	public Terrain t;
	public int N;
	public int theme;

	public int tailleCase,gapV,gapG, gapD;
	public Dimension p;

	public Point click, release;

	public boolean animation;
	int plusX, plusY, lAnimation,xAnimation, yAnimation, cAnimation;
	float xAnim,yAnim;

	public boolean adjacent;
	public int lAdjacent, cAdjacent;


	private int taillePolice;
	private BufferedImage BIFondAnimation;
	public Image BIPlateau, imageJ1, imageJ2;

	JButton annuler,pause, rejouer;
	ImageIcon ampoule;
	JLabel labelAmpoule;
	String etatJeul1, etatJeul2;
	EcouteurDAide ea;
	static final Color blancTransparent = new Color(255,255,255,127);

	TerrainGraphique(Avalam a){
	etatJeul1 = "";
	etatJeul2 = "";
		this.a=a;
		t = a.t;
		N=a.t.TAB_SIZE;
		click=null;
		release=null;
		File ap = new File(System.getProperty("user.home")+"/.Avalam/Config/apparence.cfg");

		annuler = new JButton("↩");
		pause = new JButton("❙❙");
		rejouer = new JButton("↪");
		this.add(annuler);
		this.add(pause);
		this.add(rejouer);
		annuler.setActionCommand("annuler");
		pause.setActionCommand("pause");
		rejouer.setActionCommand("rejouer");
		annuler.setToolTipText("Annuler le dernier coup joué");
		pause.setToolTipText("Met le jeu en pause");
		rejouer.setToolTipText("Rejouer le coup annulé");
		annuler.addActionListener(a.f.em);
		pause.addActionListener(a.f.em);
		rejouer.addActionListener(a.f.em);

		ampoule = new ImageIcon("images/ampoule.png");
		labelAmpoule = new JLabel(ampoule);
		labelAmpoule.setDisabledIcon(new ImageIcon("images/ampouleoff.png"));
		labelAmpoule.setEnabled(false);
		this.add(labelAmpoule);

		ea = new EcouteurDAide(a, this);
		labelAmpoule.addMouseListener(ea);

		if(!ap.exists()){
			theme =1;
		}else{
			try{
				FileInputStream in = new FileInputStream(ap);
				Scanner s = new Scanner(in);
				theme = s.nextInt();
			}catch(FileNotFoundException e){}
		}
	}

	//methode qui calcule le "coté" du plateau
	void calculTaille(){
		p = a.f.g.getSize();
		if (p.height >= 7*p.width/10) {
			tailleCase = (7*p.width/10)/N;
			gapV = (p.height-7*p.width/10)/2;
			gapG = 3*p.width/10;
			gapD = 0;
		} else {
			tailleCase = p.height/N;
			gapV = 0;
			gapD = (p.width-p.height)/2;
			gapG = 3*p.width/10;
		}
	}

	//methodes de conversion coord//indices
	Point coordToIndice(Point coor){
		Point indice;
		int i = (coor.y-gapV)/tailleCase;
		int	j = (coor.x-gapG)/tailleCase;
		indice = new Point(i,j);
		return indice;
	}

	Point indiceToCoord(Point indice){
		Point coord;
		int x = indice.y*tailleCase+gapG;
		int y = indice.x*tailleCase+gapV;
		coord = new Point(x,y);
		return coord;
	}

	//methodes de dessin
	public void dessineCase(int i, int j, Graphics2D drawable) {
		int x,y;
		Point coord = indiceToCoord(new Point(i,j));
		x=coord.x;
		y=coord.y;
		int diametre = (int) (tailleCase*0.7);
		int xCentre = x+tailleCase/2;
		int yCentre = y+tailleCase/2;
		int xCercle = xCentre-diametre/2;
		int yCercle = yCentre-diametre/2;

		if (t.plateau[i][j].estJ1())
			drawable.setPaint(Themes.getCouleurPionJ1(theme));
		else
			drawable.setPaint(Themes.getCouleurPionJ2(theme));
		drawable.fillOval(xCercle, yCercle, diametre, diametre);

		drawable.setPaint(Themes.getCouleurChiffre(theme));
		drawable.setFont(new Font("Garuda", 0, tailleCase/3));
		drawable.drawString("" + t.plateau[i][j].getTaille(),x+2*tailleCase/5, y+3*tailleCase/5);
	}

	public void dessineCaseAccessible(int i, int j, Graphics2D drawable) {
		int x,y;
		Point coord = indiceToCoord(new Point(i,j));
		x=coord.x;
		y=coord.y;
		int diametre = (int) (tailleCase*0.9);
		int xCentre = x+tailleCase/2;
		int yCentre = y+tailleCase/2;
		int xCercle = xCentre-diametre/2;
		int yCercle = yCentre-diametre/2;
		drawable.setPaint(blancTransparent);
		drawable.fillOval(xCercle, yCercle, diametre, diametre);
		drawable.setPaint(Themes.getCouleurChiffre(theme));
		drawable.setFont(new Font("Garuda", 0, tailleCase/3));
		drawable.drawString("" + t.plateau[i][j].getTaille(),x+2*tailleCase/5, y+3*tailleCase/5);
	}

	public void dessineCaseVide(int i, int j, Graphics2D drawable) {
		int x,y;
		Point coord = indiceToCoord(new Point(i,j));
		x=coord.x;
		y=coord.y;
		drawable.setPaint(Themes.getCouleurVide(theme));
		drawable.fillOval(x+tailleCase/4, y+tailleCase/4, tailleCase/2, tailleCase/2);
	}



	public void panneau(Graphics2D drawable) {
		int xRect = tailleCase/4;
		int yRect = gapV+tailleCase/2;
		int hauteur = 8*tailleCase;
		int largeur = 2*p.width/10;
		int xCentre = xRect+(largeur/2);
		int yCentre = yRect+(hauteur/2);

		RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(xRect, yRect, largeur, hauteur, 20, 20);
		drawable.setPaint(blancTransparent);
		drawable.fill(roundedRectangle);

		Font f = new Font("Liberation Sans", 1 , (int) (0.35*tailleCase));
		FontMetrics metrics = getFontMetrics(f);
		int decalage;
		int yTextHaut = yRect+tailleCase/2;
		int yTextBas  = yRect+hauteur-tailleCase/3;

		drawable.setFont(f);
		drawable.setPaint(Color.black);
		drawable.setPaint(Themes.getCouleurPionJ2(theme));
		int gap = (largeur-2*tailleCase)/4;
		decalage = metrics.stringWidth(a.j.J2.nom);
		drawable.drawString(a.j.J2.nom, xCentre-decalage/2, yTextHaut);
		decalage = metrics.stringWidth("Score : " + a.j.J2.score);
		drawable.drawString("Score : " + a.j.J2.score, xCentre-decalage/2,yTextHaut+metrics.getHeight());

		drawable.drawImage(imageJ2,xCentre-tailleCase/2,
		                   yTextHaut+3*metrics.getHeight()/2,
		                   tailleCase, tailleCase,
		                   null);

		int yBiduleCentre = (yRect+hauteur/2)-(tailleCase/2+ metrics.getHeight()*2 + ampoule.getIconHeight())/2;
		annuler.setFont(annuler.getFont().deriveFont((float) (0.30*tailleCase)));
		annuler.setBounds(xRect+gap/3, yBiduleCentre,tailleCase, tailleCase/2);
		pause.setFont(annuler.getFont().deriveFont((float) (0.30*tailleCase)));
		pause.setBounds(xRect+(largeur-tailleCase)/2, yBiduleCentre,tailleCase, tailleCase/2);
		rejouer.setFont(rejouer.getFont().deriveFont((float) (0.30*tailleCase)));
		rejouer.setBounds(xRect+largeur-gap/3-tailleCase, yBiduleCentre, tailleCase, tailleCase/2);

		if (a.j.courantEstJ1())
			drawable.setPaint(Themes.getCouleurPionJ1(theme));
		else
			drawable.setPaint(Themes.getCouleurPionJ2(theme));

		decalage = metrics.stringWidth(etatJeul1);
		drawable.drawString(etatJeul1, xCentre-decalage/2,yBiduleCentre+tailleCase);
		decalage = metrics.stringWidth(etatJeul2);
		drawable.drawString(etatJeul2, xCentre-decalage/2,yBiduleCentre+tailleCase+metrics.getHeight());
		labelAmpoule.setBounds(xCentre-ampoule.getIconWidth()/2, yBiduleCentre+2*tailleCase/3+metrics.getHeight()*2, ampoule.getIconWidth(), ampoule.getIconHeight());


		drawable.drawImage(imageJ1, xCentre-tailleCase/2,
		                   yTextBas-metrics.getHeight()*2-tailleCase,
		                   tailleCase, tailleCase, null);


		drawable.setPaint(Themes.getCouleurPionJ1(theme));
		decalage = metrics.stringWidth("Score : " + a.j.J1.score);
		drawable.drawString("Score : " + a.j.J1.score, xCentre-decalage/2, yTextBas-metrics.getHeight());
		decalage =  metrics.stringWidth(a.j.J1.nom);
		drawable.drawString(a.j.J1.nom, xCentre-decalage/2, yTextBas);
		drawable.setPaint(Color.black);


		// drawable.drawImage(ampoule, xCentre-ampoule.getWidth()/2, yRect+hauteur-ampoule.getHeight(), null);
	}

	public void resetBIFondAnimation(Point lc) {
		BIFondAnimation = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
		Graphics2D drawable = BIFondAnimation.createGraphics();
		drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawable.setPaint(Themes.getCouleurFond(theme));
		drawable.fillRect(0,0, getSize().width, getSize().height);
		drawable.drawImage(BIPlateau, gapG-tailleCase*1/5, gapV-tailleCase*1/5, (int) (tailleCase*9.3), (int) (tailleCase*9.3), null);

		for (int i=0; i<N; i++) {
			for (int j=0; j<N; j++) {
				if (!t.plateau[i][j].estVide() && (i!=lc.x || j!=lc.y)){
					if (t.plateau[i][j].estJ1()) {
						drawable.setPaint(Themes.getCouleurPionJ1(theme));
						dessineCase(i, j, drawable);
					} else if (t.plateau[i][j].estJ2()) {
						drawable.setPaint(Themes.getCouleurPionJ2(theme));
						dessineCase(i, j, drawable);
					}
				} else {
					dessineCaseVide(i, j, drawable);
				}
			}
		}
	}


	public void setPlusXY(Point xy, Point lc) {
		Point p = indiceToCoord(lc);
		plusX = p.x - xy.x;
		plusY = p.y - xy.y;
		lAnimation = lc.x;
		cAnimation = lc.y;
	}

	public void setXYAnimation(int x, int y) {
		xAnimation = x;
		yAnimation = y;
	}


	public void animationPion(Point src, Point dst) {
		int xDes = dst.x;
		int yDes = dst.y;
		int xSrc = src.x;
		int ySrc = src.y;
		float xDist = (xDes-xSrc);
		float yDist = (yDes-ySrc);
		float xyDist = (float) Math.sqrt(xDist*xDist+yDist*yDist);
		int dir = xDes - xSrc;
		animation = true;
		xAnim = xSrc;
		yAnim = ySrc;
		for(float i=0;i<xyDist/3;i++){
			xAnimation=(int)(xSrc+i*xDist/(xyDist/3));
			yAnimation=(int)(ySrc+i*yDist/(xyDist/3));
			repaint();
			try{
				Thread.sleep(5);
			}catch(InterruptedException e){
			}
		}
		animation = false;
	}

	public void animationPionAuto() {
		lAnimation=a.j.c.pDep.x;
		cAnimation=a.j.c.pDep.y;
		resetBIFondAnimation(a.j.c.pDep);
		repaint();
		Point src = indiceToCoord(a.j.c.pDep);
		Point dst = indiceToCoord(a.j.c.pArr);
		int xDes = dst.x+tailleCase/2;
		int yDes = dst.y+tailleCase/2;
		int xSrc = src.x+tailleCase/2;
		int ySrc = src.y+tailleCase/2;
		float xDist = (xDes-xSrc);
		float yDist = (yDes-ySrc);
		float xyDist = (float) Math.sqrt(xDist*xDist+yDist*yDist);
		int dir = xDes - xSrc;
		animation = true;
		xAnim = xSrc;
		yAnim = ySrc;
		plusX=-tailleCase/2;
		plusY=-tailleCase/2;
		for(float i=0;i<xyDist/3;i++){
			xAnimation=(int)(xSrc+i*xDist/(xyDist/3));
			yAnimation=(int)(ySrc+i*yDist/(xyDist/3));
			repaint();
			try {
				Thread.sleep(20);
			}catch(InterruptedException e){}
		}
		animation = false;
	}

	public void animationPionAnnuler(){
		lAnimation=a.j.c.pDep.x;
		cAnimation=a.j.c.pDep.y;
		resetBIFondAnimation(a.j.c.pDep);
		repaint();
		Point dst = indiceToCoord(a.j.c.pDep);
		Point src = indiceToCoord(a.j.c.pArr);
		int xDes = dst.x+tailleCase/2;
		int yDes = dst.y+tailleCase/2;
		int xSrc = src.x+tailleCase/2;
		int ySrc = src.y+tailleCase/2;
		float xDist = (xDes-xSrc);
		float yDist = (yDes-ySrc);
		float xyDist = (float) Math.sqrt(xDist*xDist+yDist*yDist);
		int dir = xDes - xSrc;
		animation = true;
		xAnim = xSrc;
		yAnim = ySrc;
		plusX=-tailleCase/2;
		plusY=-tailleCase/2;
		for(float i=0;i<xyDist/3;i++){
			xAnimation=(int)(xSrc+i*xDist/(xyDist/3));
			yAnimation=(int)(ySrc+i*yDist/(xyDist/3));
			repaint();
			try {
				Thread.sleep(20);
			}catch(InterruptedException e){}
		}
		animation = false;
	}


	public static void setAntiAlias(Graphics2D drawable) {
		drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawable.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		                          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		drawable.setRenderingHint(RenderingHints.KEY_RENDERING,
		                          RenderingHints.VALUE_RENDER_QUALITY);
	}

	public void reinitialisationDesBI() {
		File f=null;
		BufferedImage loaded;
		ImageFilter filter;
		FilteredImageSource filteredSrc;
		Image filteredRes;
		Graphics2D drawable;
		// reini du plateau
		calculTaille();
		try {

			loaded = ImageIO.read(new File("images/plateau.png"));

			filter = new SetColorFilter(Themes.getCouleurPlateau(theme));
			filteredSrc = new FilteredImageSource(loaded.getSource(), filter);
			BIPlateau = Toolkit.getDefaultToolkit().createImage(filteredSrc);
		} catch (Exception e) {}

		// reini du J1
		try {
			if (a.j.J1.estHumain()) {
				imageJ1 = ImageIO.read(new File(Themes.getImageJ1(theme)));
			} else if (a.j.J1.type == Jeu.BOTLVL1) {
				imageJ1 = ImageIO.read(new File(Themes.getImageO10(theme)));
			} else if (a.j.J1.type == Jeu.BOTLVL2) {
				imageJ1 = ImageIO.read(new File(Themes.getImageO11(theme)));
			} else if (a.j.J1.type == Jeu.BOTLVL3) {
				imageJ1 = ImageIO.read(new File(Themes.getImageO12(theme)));
			} else { // reseau
				imageJ1 = ImageIO.read(new File(Themes.getImageR1(theme)));
			}
			if (a.j.J2.estHumain()) {
				imageJ2 = ImageIO.read(new File(Themes.getImageJ2(theme)));
			} else if (a.j.J2.type == Jeu.BOTLVL1) {
				imageJ2 = ImageIO.read(new File(Themes.getImageO20(theme)));
			} else if (a.j.J2.type == Jeu.BOTLVL2) {
				imageJ2 = ImageIO.read(new File(Themes.getImageO21(theme)));
			} else if (a.j.J2.type == Jeu.BOTLVL3) {
				imageJ2 = ImageIO.read(new File(Themes.getImageO22(theme)));
			} else { // reseau
				imageJ2 = ImageIO.read(new File(Themes.getImageR2(theme)));
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void dessineAdjacent(Graphics2D drawable) {
		for (int i = lAdjacent-1; i <= lAdjacent+1; i++)
			for (int j = cAdjacent-1; j<= cAdjacent+1; j++) {
				if (i < 9 && i > -1 && j < 9 && j > -1
				    && t.plateau[i][j].estOccupee()) {
					if (i == lAdjacent && j == cAdjacent) {

					} else if (a.t.estInferieurASix(i,j,lAdjacent, cAdjacent)) {
						dessineCaseAccessible(i,j, drawable);
						dessineCase(i,j,drawable);
					}
				}
			}
	}


	public void paintComponent(Graphics g){
		calculTaille();
		Graphics2D drawable = (Graphics2D) g;


		//dimensions de la partie dessin
		Dimension p = getSize();
		int hauteur = p.height;
		int largeur = p.width;

		setAntiAlias(drawable);


		if (!animation) {
			drawable.setPaint(Themes.getCouleurFond(theme));
			drawable.fillRect(0,0, largeur, hauteur);
			if (BIPlateau == null) {
				reinitialisationDesBI();
			}

			panneau(drawable);
			drawable.drawImage(BIPlateau, gapG-tailleCase*1/5, gapV-tailleCase*1/5, (int) (tailleCase*9.3), (int) (tailleCase*9.3), null);
			for(int i=0; i<N; i++){
				for(int j=0; j<N; j++){
					if(!t.plateau[i][j].estVide()){
						if(t.plateau[i][j].estJ1()){
							drawable.setPaint(Themes.getCouleurPionJ1(theme));
							dessineCase(i,j,drawable);
						}else if(t.plateau[i][j].estJ2()){
							drawable.setPaint(Themes.getCouleurPionJ2(theme));
							dessineCase(i,j,drawable);
						}
					} else {
						dessineCaseVide(i,j,drawable);
					}
				}
			}
		} else {
			drawable.drawImage(BIFondAnimation, 0, 0, null);

			panneau(drawable);
			if (adjacent) {
				dessineAdjacent(drawable);
			}
			if (t.plateau[lAnimation][cAnimation].estJ1()) {
				drawable.setPaint(Themes.getCouleurPionJ1(theme));
			} else if (t.plateau[lAnimation][cAnimation].estJ2()) {
				drawable.setPaint(Themes.getCouleurPionJ2(theme));
			}
			// dessine le pion en déplacement
			drawable.fillOval(xAnimation+plusX+tailleCase/6,yAnimation+plusY+tailleCase/6, (int) (tailleCase*0.7), (int) (tailleCase* 0.7));
			drawable.setFont(new Font("Garuda", 0, 2*tailleCase/6));
			drawable.setPaint(Themes.getCouleurChiffre(theme));
			drawable.drawString("" + t.plateau[lAnimation][cAnimation].getTaille(), xAnimation+plusX+2*tailleCase/5, yAnimation+plusY+3*tailleCase/5);
		}
	}
}

