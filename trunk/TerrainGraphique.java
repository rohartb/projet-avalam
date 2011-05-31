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
	private int plusX, plusY, xAnimation, yAnimation, lAnimation, cAnimation;
	private int taillePolice;
	private BufferedImage BIFondAnimation;
	private Image BIPlateau;

	TerrainGraphique(Avalam a){
		this.a=a;
		t = a.t;
		N=a.t.TAB_SIZE;
		click=null;
		release=null;
		theme =1;
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
	Point coordToIndince(Point coor){
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

			if (a.j.J1.estHumain()) {
				if (a.j.courantEstJ1()) {
					drawable.setPaint(Color.red);
					drawable.fillOval(40, hauteur-(gapV+tailleCase-3), 10 , 10);
				} else {

				}
			} else if (a.j.J1.estRobot()) {

			} else {
				drawable.fillOval(15, hauteur-67 ,25, 25);
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

		if (a.j.courantEstJ2()) {
			drawable.setPaint(Color.red);
			drawable.fillOval(largeur-60, gapV+tailleCase*4/5+9, 10 , 10);
		}

		if (a.j.J2.estHumain()) {

		} else if (a.j.J2.estRobot()) {

		} else {
			//drawable.fillOval(largeur-40, 31, 25, 25);
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


	public void animationRetourPion() {
		Point lc = new Point(lAnimation, cAnimation);
		Point xyDestination = indiceToCoord(lc);
		int xDes = xyDestination.x-plusX;
		int yDes = xyDestination.y-plusY;
		int xSrc = release.x;
		int ySrc = release.y;
		float coef = (float)(xDes-xSrc)/(yDes-ySrc);
		System.out.println(coef);
		animation = true;
		float direction = (yDes - ySrc) / (xDes - xSrc);
		while (xDes != xSrc || yDes != ySrc) {
			direction = Math.abs(direction);
			if (direction < 1) {
				if (xDes > xSrc)
					xSrc++;
				else if (xDes < xSrc)
					xSrc--;
			} else if (direction > 1) {
				if (yDes > ySrc)
					ySrc+=coef;
				else if (yDes < ySrc)
					ySrc+=coef;
			} else {
				if (xDes > xSrc)
					xSrc++;
				else if (xDes < xSrc)
					xSrc--;
				if (yDes > ySrc)
					ySrc++;
				else if (yDes < ySrc)
					ySrc--;
			}
			xAnimation = xSrc;
			yAnimation = ySrc;
			repaint();
			try {
				a.thFenetre.sleep(5);
			} catch (InterruptedException e) {}

			if ( xDes - xSrc == 0)
				break;
			else
				direction = (yDes - ySrc) / (xDes - xSrc);
		}
		animation = false;
	}

	public void reinitialisationBIPlateau() {
		try {
			BufferedImage resized;
			resized = ImageIO.read(new File("images/plateau.png"));

			ImageFilter filter = new SetColorFilter(Themes.getCouleurPlateau(theme));
			FilteredImageSource filteredSrc = new FilteredImageSource(resized.getSource(), filter);
			BIPlateau = Toolkit.getDefaultToolkit().createImage(filteredSrc);
		} catch (Exception e) {}
	}


	public void paintComponent(Graphics g){
		calculTaille();
		Graphics2D drawable = (Graphics2D) g;
		drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawable.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		                          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		drawable.setRenderingHint(RenderingHints.KEY_RENDERING,
		                          RenderingHints.VALUE_RENDER_QUALITY);

		//dimensions de la partie dessin
		Dimension p = getSize();
		int hauteur = p.height;
		int largeur = p.width;


		if (!animation) {
			drawable.setPaint(Themes.getCouleurFond(theme));
			drawable.fillRect(0,0, largeur, hauteur);

		if (BIPlateau == null) {
			reinitialisationBIPlateau();
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
			drawable.fillOval(xAnimation+plusX+tailleCase/6, yAnimation+plusY+tailleCase/6,
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

