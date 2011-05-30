import java.awt.event.*;
import java.awt.*;

class EcouteurDeSouris implements MouseListener, MouseMotionListener{
	Avalam a;

	boolean actif;
	//TerrainGraphique tg;
	EcouteurDeSouris(Avalam a){
		this.a=a;
	}

	public void mousePressed(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		a.f.g.click=new Point(x,y);
		a.f.g.animation = true;

		Point xy = new Point(x,y);
		Point lc = a.f.g.coordToIndince(xy);
		a.f.g.resetBIFondAnimation(lc);
		a.f.g.setPlusXY(xy,lc);
		a.f.g.setXYAnimation(x,y);
		a.f.g.repaint();
		a.etat=a.JOUERMANU;
		a.unpause();
	}


	public void mouseReleased(MouseEvent e){
		a.f.g.release=new Point(e.getX(),e.getY());
		a.f.g.animation = false;
		a.f.g.repaint();
		a.unpause();

	}

	public void mouseDragged(MouseEvent e){
		int x =(int) e.getX();
		int y =(int) e.getY();
		a.f.g.setXYAnimation(x,y);
		a.f.g.repaint();
	}


 	public void mouseMoved(MouseEvent e){}
 	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

}
