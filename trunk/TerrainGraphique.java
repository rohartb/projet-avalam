import javax.swing.*;
import java.awt.*;
import java.awt.image.*;;
import java.util.*;

class TerrainGraphique extends JComponent{
	Avalam a;
	
	//Theme th
	Terrain t;
	int N;
	
	int tailleCase,gapV,gapH;
	Dimension p;
	
	Point click, release;

	TerrainGraphique(Avalam a){
		this.a=a;
		t = a.t;
		N=a.t.TAB_SIZE;
		click=null;
		release=null;
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
		drawable.setPaint(Color.white);
		drawable.setFont(new Font("Garuda", 0, tailleCase/3));
		drawable.drawString("" + t.plateau[i][j].getTaille(),x+2*tailleCase/5, y+3*tailleCase/5);
	}
	
	public void dessineCaseVide(int i, int j, Graphics2D drawable) {
		int x,y;
		Point coord = indiceToCoord(new Point(i,j));
		x=coord.x;
		y=coord.y;
		drawable.setPaint(Color.gray);
		drawable.fillOval(x+tailleCase/4, y+tailleCase/4, tailleCase/2, tailleCase/2);
	}
	
	public void paintComponent(Graphics g){
		calculTaille();
		
		Graphics2D drawable = (Graphics2D) g;
		drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//dimensions de la partie dessin
		Dimension p = getSize();
		int hauteur = p.height;
		int largeur = p.width;
		drawable.setPaint(Color.black);
		drawable.fillRect(0,0, largeur, hauteur);
		
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++){
				if(!t.plateau[i][j].estVide()){
					if(t.plateau[i][j].estJ1()){
						drawable.setPaint(Color.blue);
						dessineCase(i,j,drawable);
					}else if(t.plateau[i][j].estJ2()){
						drawable.setPaint(Color.red);
						dessineCase(i,j,drawable);
					}
				} else {
					dessineCaseVide(i,j,drawable);
				}
			}
		}
	}
}

