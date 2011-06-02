import javax.swing.*;
import java.awt.*;
import java.awt.image.*;;
import java.util.*;
import javax.imageio.*;
import java.io.*;
import java.awt.geom.*;

class TerrainGraphique extends JComponent{
	Avalam a;

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
	private BufferedImage BIFondAnimation, BIJ1Small, BIJ1Big, BIJ2Small, BIJ2Big;
	private Image BIPlateau;

	JButton annuler, rejouer;
	ImageIcon ampoule;
	JLabel labelAmpoule;
	EcouteurDAide ea;
	static final Color blancTransparent = new Color(255,255,255,127);

	TerrainGraphique(Avalam a){
		this.a=a;
		t = a.t;
		N=a.t.TAB_SIZE;
		click=null;
		release=null;
		File ap = new File(System.getProperty("user.home")+"/.Avalam/Config/apparence.cfg");

		annuler = new JButton("↩");
		rejouer = new JButton("↪");
		this.add(annuler);
		this.add(rejouer);
		annuler.setActionCommand("annuler");
		rejouer.setActionCommand("rejouer");
		annuler.setToolTipText("Annuler le dernier coup joué");
		rejouer.setToolTipText("Rejouer le coup annulé");
		annuler.addActionListener(a.f.em);
		rejouer.addActionListener(a.f.em);
		ampoule = new ImageIcon("images/ampoule.png");
		labelAmpoule = new JLabel(ampoule);
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

		//drawable.fillOval(x+tailleCase/6, y+tailleCase/6, (int) (tailleCase*0.7), (int) (tailleCase*0.7));
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
		// if (t.plateau[i][j].estJ1())
// 			drawable.setPaint(Themes.getCouleurPionJ1(theme));
// 		else
// 			drawable.setPaint(Themes.getCouleurPionJ2(theme));

		//Shape cercle = new Ellipse2D.Float(xCercle, yCercle, diametre, diametre);
		//drawable.setStroke(new BasicStroke(2));
		//drawable.draw(cercle);
		drawable.setPaint(blancTransparent);
		//drawable.setPaint(Themes.getCouleurPlateau(theme).brighter());
		drawable.fillOval(xCercle, yCercle, diametre, diametre);
		//drawable.fillRect(x,y,tailleCase, tailleCase);
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


	public void joueur1(Graphics2D drawable) {
			taillePolice = (int) (0.45* tailleCase);
			int hauteur = p.height;
			int largeur = p.width;

			drawable.setPaint(Themes.getCouleurPionJ1(theme));
			drawable.setFont(new Font("Liberation Sans", 1 , taillePolice));
			drawable.drawString(a.j.J1.nom,tailleCase/4,hauteur-(gapV+(1*tailleCase/3)));

			if (! a.j.courantEstJ1()) {
				drawable.drawImage(BIJ1Small, tailleCase/4, hauteur-((int) (gapV+tailleCase*1.8)) ,null);
			} else {
				drawable.drawImage(BIJ1Small,tailleCase/4, hauteur-((int) (gapV+tailleCase*1.8)) ,null);
			}
	}


	public void joueur2(Graphics2D drawable) {
		int hauteur = p.height;
		int largeur = p.width;
		//precalcule du decalage
		FontMetrics metrics = getFontMetrics(drawable.getFont());
		int decalage = metrics.stringWidth(a.j.J2.nom);

		taillePolice = (int) (0.45* tailleCase);

		drawable.setPaint(Themes.getCouleurPionJ2(theme));
		drawable.setFont(new Font("Liberation Sans", 1 , taillePolice));
		drawable.drawString(a.j.J2.nom, tailleCase/4, gapV + 3*tailleCase/2);

		if (! a.j.courantEstJ2()) {
			drawable.drawImage(BIJ2Small,tailleCase/4, gapV  ,null);
		} else {
			drawable.drawImage(BIJ2Small,tailleCase/4, gapV ,null);
		}
	}

	public void panneau(Graphics2D drawable) {
		int xRect = tailleCase/4;
		int yRect = p.height/2-2*tailleCase;
		int hauteur = 4*tailleCase;
		int largeur = 2*p.width/10;
		int centre = xRect+(largeur/2);

		RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(xRect, yRect, largeur, hauteur, 10, 10);
		drawable.setPaint(blancTransparent);
		drawable.fill(roundedRectangle);

		Font f = new Font("Liberation Sans", 1 , (int) (0.30*tailleCase));
		FontMetrics metrics = getFontMetrics(f);
		int decalage = metrics.stringWidth("Joueur 1");

		drawable.setFont(f);
		drawable.setPaint(Color.black);
		int hauteurTexte = yRect+4*tailleCase/3;
		if (a.j.courantEstJ1())
			drawable.drawString("Joueur 1", centre-decalage/2, hauteurTexte);
		else
			drawable.drawString("Joueur 2", centre-decalage/2, hauteurTexte);
		decalage = metrics.stringWidth("c'est à toi");
		drawable.drawString("c'est à toi", centre-decalage/2, hauteurTexte+metrics.getHeight());

		int gap = (largeur-2*tailleCase)/4;
		annuler.setBounds(xRect+gap, yRect+tailleCase/4, tailleCase, tailleCase/2);
		rejouer.setBounds(xRect+largeur-gap-tailleCase, yRect+tailleCase/4, tailleCase, tailleCase/2);
		labelAmpoule.setBounds(centre-ampoule.getIconWidth()/2, yRect+hauteur-ampoule.getIconHeight(), ampoule.getIconWidth(), ampoule.getIconHeight());
		//drawable.drawImage(ampoule, centre-ampoule.getWidth()/2, yRect+hauteur-ampoule.getHeight(), null);
	}

	public void resetBIFondAnimation(Point lc) {
		BIFondAnimation = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
		Graphics2D drawable = BIFondAnimation.createGraphics();
		drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawable.setPaint(Themes.getCouleurFond(theme));
		drawable.fillRect(0,0, getSize().width, getSize().height);
		drawable.drawImage(BIPlateau, gapG-tailleCase*1/5, gapV-tailleCase*1/5, (int) (tailleCase*9.3), (int) (tailleCase*9.3), null);
		//drawable.setPaint(Color.black);

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
	
	public void animationPionAnnuler() {
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
		if ( a.j.J1.estHumain())
			f = new File("images/joueur.png");
		else if ( a.j.J1.estRobot()) {
			f = new File("images/bot.png");
		}
		try {
		loaded = ImageIO.read(f);
		filter = new SetColorFilter(Themes.getCouleurPionJ1(theme));
		filteredSrc = new FilteredImageSource(loaded.getSource(), filter);
		filteredRes = Toolkit.getDefaultToolkit().createImage(filteredSrc);
		BIJ1Small = new BufferedImage(tailleCase, tailleCase,BufferedImage.TYPE_INT_ARGB);
		drawable = BIJ1Small.createGraphics();
		setAntiAlias(drawable);
		drawable.drawImage(filteredRes, 0, 0, tailleCase, tailleCase,null);

		BIJ1Big = new BufferedImage(tailleCase*2, tailleCase*2, BufferedImage.TYPE_INT_ARGB);
		drawable = BIJ1Big.createGraphics();
		setAntiAlias(drawable);
		drawable.drawImage(filteredRes, 0, 0,tailleCase*2, tailleCase*2,null);
		} catch (Exception e) {
			System.out.println(e);
		}

		// reini du J2
		if ( a.j.J2.estHumain())
			f = new File("images/joueur.png");
		else if ( a.j.J2.estRobot()) {
			f = new File("images/bot.png");
		}
		try {
		loaded = ImageIO.read(f);
		filter = new SetColorFilter(Themes.getCouleurPionJ2(theme));
		filteredSrc = new FilteredImageSource(loaded.getSource(), filter);
		filteredRes = Toolkit.getDefaultToolkit().createImage(filteredSrc);

		BIJ2Small = new BufferedImage(tailleCase, tailleCase,BufferedImage.TYPE_INT_ARGB);
		drawable = BIJ2Small.createGraphics();
		setAntiAlias(drawable);
		drawable.drawImage(filteredRes, 0, 0, tailleCase, tailleCase,null);

		BIJ2Big = new BufferedImage(tailleCase*2, tailleCase*2, BufferedImage.TYPE_INT_ARGB);
		drawable = BIJ2Big.createGraphics();
		setAntiAlias(drawable);
		drawable.drawImage(filteredRes, 0, 0,tailleCase*2, tailleCase*2,null);
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

					} else {
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
			joueur1(drawable);
			joueur2(drawable);
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
			if (adjacent) {
				dessineAdjacent(drawable);
			}
		} else {
			drawable.drawImage(BIFondAnimation, 0, 0, null);
			joueur1(drawable);
			joueur2(drawable);
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

