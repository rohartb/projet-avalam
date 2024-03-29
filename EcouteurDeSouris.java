import java.awt.event.*;
import java.awt.*;

class EcouteurDeSouris implements MouseListener, MouseMotionListener, ComponentListener {
	Avalam a;
	private Point prec;

	boolean actif;
	boolean click;
	EcouteurDeSouris(Avalam a){
		this.a=a;

	}

	public void mousePressed(MouseEvent e){
		if (actif) {
			int x = e.getX();
			int y = e.getY();
			a.f.g.click = new Point(x,y);

			Point xy = new Point(x,y);
			Point lc = a.f.g.coordToIndice(xy);

			if (lc.x > -1 &&  lc.y > -1  && lc.x < 9 && lc.y < 9 &&
			    a.t.plateau[lc.x][lc.y].estOccupee()) {
				click = true;
				a.f.g.animation = true;
				a.f.g.resetBIFondAnimation(lc);
				a.f.g.setPlusXY(xy,lc);
				a.f.g.setXYAnimation(x,y);
				if(a.j.modeAide){
					a.f.g.adjacent = true;
					Point indice = a.f.g.coordToIndice(a.f.g.click);
					a.f.g.lAdjacent = indice.x;
					a.f.g.cAdjacent = indice.y;
				}
				a.f.g.repaint();
				a.unpause();
			}
			
		}
	}


	public void mouseReleased(MouseEvent e){
		if (actif && click) {
			a.f.g.release = new Point(e.getX(),e.getY());
			a.f.g.animation = false;
			a.f.g.adjacent = false;
			a.f.g.repaint();
			a.unpause();
		}
			click = false;
	}

	public void mouseDragged(MouseEvent e){
		if (actif) {
			if (a.f.g.animation) {
				int x = e.getX();
				int y = e.getY();
				a.f.g.setXYAnimation(x,y);
				a.f.g.repaint();
			}
		}
	}

 	public void mouseMoved(MouseEvent e){}
 	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	public void componentResized(ComponentEvent e) {
		a.f.g.calculTaille();
		a.f.g.reinitialisationDesBI();
		a.f.g.repaint();
	}

	public void componentHidden(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}

}
