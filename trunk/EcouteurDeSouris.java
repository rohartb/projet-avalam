import java.awt.event.*;
import java.awt.*;

class EcouteurDeSouris implements MouseListener, MouseMotionListener, ComponentListener {
	Avalam a;

	boolean actif;
	EcouteurDeSouris(Avalam a){
		this.a=a;
	}

	public void mousePressed(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		a.f.g.click = new Point(x,y);

		Point xy = new Point(x,y);
		Point lc = a.f.g.coordToIndice(xy);

		if (lc.x > -1 &&  lc.y > -1  && lc.x < 9 && lc.y < 9 &&
		    a.t.plateau[lc.x][lc.y].estOccupee()) {
			System.out.println("coucou");
			a.f.g.animation = true;
			a.f.g.resetBIFondAnimation(lc);
			a.f.g.setPlusXY(xy,lc);
			a.f.g.setXYAnimation(x,y);
			a.f.g.repaint();
			a.unpause();
		}
	}


	public void mouseReleased(MouseEvent e){
		a.f.g.release = new Point(e.getX(),e.getY());
		a.f.g.animation = false;
		a.f.g.repaint();
		a.unpause();
	}

	public void mouseDragged(MouseEvent e){
		if (a.f.g.animation) {
			int x =(int) e.getX();
			int y =(int) e.getY();
			a.f.g.setXYAnimation(x,y);
			a.f.g.repaint();
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
