import javax.swing.*;
import java.awt.*;
import java.awt.image.*;;
import java.util.*;
import javax.imageio.*;
import java.io.*;


class TerrainGraphique extends JComponent{
	Avalam a;

	public Terrain t;
	public int N;
	public int theme;

	public int tailleCase,gapV,gapH;
	public Dimension p;

	public Point click, release;

	public boolean animation;
	int plusX, plusY, lAnimation,xAnimation, yAnimation, cAnimation;
	float xAnim,yAnim;
	private int taillePolice;
	private BufferedImage BIFondAnimation, BIJ1Small, BIJ1Big, BIJ2Small, BIJ2Big;
	private Image BIPlateau;

	TerrainGraphique(Avalam a){
		this.a=a;
		t = a.t;
		N=a.t.TAB_SIZE;
		click=null;
		release=null;
		File ap = new File(System.getProperty("user.home")+"/.Avalam/Config/apparence.cfg");
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
		if ( p.height >= p.width) {
			tailleCase = p.width/N;
			gapV = (p.height-p.width)/2;
			gapH = 0;
		} else {
			tailleCase = p.height/N;
			gapV = 0;
			gapH = (p.width-p.height)/2;
		}
	}

	//methodes de conversion coord//indices
	Point coordToIndice(Point coor){
		Point indice;
		int i = (coor.y-gapV)/tailleCase;
		int	j = (coor.x-gapH)/tailleCase;
		indice = new Point(i,j);
		return indice;
	}

	Point indiceToCoord(Point indice){
		Point coord;
		int x = indice.y*tailleCase+gapH;
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
		drawable.fillOval(x+tailleCase/6, y+tailleCase/6, (int) (tailleCase*0.7), (int) (tailleCase*0.7));
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
			drawable.drawString(a.j.J1.nom,10,hauteur-(gapV+(1*tailleCase/3)));

			if (! a.j.courantEstJ1()) {
				drawable.drawImage(BIJ1Small,8, hauteur-((int) (gapV+tailleCase*1.7)) ,null);
			} else {
				drawable.drawImage(BIJ1Big,8, hauteur-((int) (gapV+tailleCase*2.7)) ,null);
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
		drawable.drawString(a.j.J2.nom, largeur-decalage-10, gapV +
		                    (int) (tailleCase*0.6));

		if (! a.j.courantEstJ2()) {
			drawable.drawImage(BIJ2Small,largeur-tailleCase-10, gapV+2*tailleCase/3 ,null);
		} else {
			drawable.drawImage(BIJ2Big,largeur-tailleCase*2-10, gapV+2*tailleCase/3 ,null);
		}
	}

	public void resetBIFondAnimation(Point lc) {
		BIFondAnimation = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
		Graphics2D drawable = BIFondAnimation.createGraphics();
		drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawable.setPaint(Themes.getCouleurFond(theme));
		drawable.fillRect(0,0, getSize().width, getSize().height);
		drawable.drawImage(BIPlateau, gapH-tailleCase*1/5, gapV-tailleCase*1/5, (int) (tailleCase*9.3), (int) (tailleCase*9.3), null);
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
		int xDes = dst.x;//+tailleCase/2;
		int yDes = dst.y;//+tailleCase/2;
		int xSrc = src.x;//+tailleCase/2;
		int ySrc = src.y;//+tailleCase/2;
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
		drawable.drawImage(BIPlateau, gapH-tailleCase*1/5, gapV-tailleCase*1/5, (int) (tailleCase*9.3), (int) (tailleCase*9.3), null);
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
			if (t.plateau[lAnimation][cAnimation].estJ1()) {
				drawable.setPaint(Themes.getCouleurPionJ1(theme));
			} else if (t.plateau[lAnimation][cAnimation].estJ2()) {
				drawable.setPaint(Themes.getCouleurPionJ2(theme));
			}

			// dessine le pion en déplacement
			drawable.fillOval(xAnimation+plusX+tailleCase/6,yAnimation+plusY+tailleCase/6,
			                  (int) (tailleCase*0.7), (int) (tailleCase* 0.7));
			drawable.setFont(new Font("Garuda", 0, 2*tailleCase/6));
			drawable.setPaint(Themes.getCouleurChiffre(theme));
			drawable.drawString("" + t.plateau[lAnimation][cAnimation].getTaille(),
			                    xAnimation+plusX+2*tailleCase/5, yAnimation+plusY+3*tailleCase/5);
		}
	joueur1(drawable);
	joueur2(drawable);
	}
}

