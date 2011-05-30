import javax.swing.*;
import java.awt.*;
import java.awt.image.*;;
import java.util.*;

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
	private BufferedImage BIFondAnimation;

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

	public void resetBIFondAnimation(Point lc) {
		BIFondAnimation = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
		Graphics2D drawable = BIFondAnimation.createGraphics();
		drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawable.setPaint(Themes.getCouleurFond(theme));
		drawable.fillRect(0,0, getSize().width, getSize().height);
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
		int xDestination = xyDestination.x-plusX;
		int yDestination = xyDestination.y-plusY;
		int xSrc = release.x;
		int ySrc = release.y;
		animation = true;
		float direction = (yDestination - ySrc) / (xDestination - xSrc);
		while (xDestination != xSrc || yDestination != ySrc) {
			direction = Math.abs(direction);
			// System.out.println("direction : " + direction + " " +
			//                    xDestination + " " + xSrc + "  " +
			//                    yDestination + " " + ySrc);

			if (direction < 1) {
				if (xDestination > xSrc)
					xSrc++;
				else if (xDestination < xSrc)
					xSrc--;
			} else if (direction > 1) {
				if (yDestination > ySrc)
					ySrc++;
				else if (yDestination < ySrc)
					ySrc--;
			} else {
				if (xDestination > xSrc)
					xSrc++;
				else if (xDestination < xSrc)
					xSrc--;
				if (yDestination > ySrc)
					ySrc++;
				else if (yDestination < ySrc)
					ySrc--;
			}
			xAnimation = xSrc;
			yAnimation = ySrc;
			repaint();
			try {
				a.thFenetre.sleep(5);
			} catch (InterruptedException e) {}

			if ( xDestination - xSrc == 0)
				break;
			else
				direction = (yDestination - ySrc) / (xDestination - xSrc);
		}
		animation = false;
	}


	public void paintComponent(Graphics g){
		calculTaille();

		Graphics2D drawable = (Graphics2D) g;
		drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//dimensions de la partie dessin
		Dimension p = getSize();
		int hauteur = p.height;
		int largeur = p.width;

		if (!animation) {
			drawable.setPaint(Themes.getCouleurFond(theme));
			drawable.fillRect(0,0, largeur, hauteur);
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
	}
}

