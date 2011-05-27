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
		a.f.g.click=new Point(e.getX(),e.getY());
		a.etat=a.JOUERMANU;
		a.unpause();
	}


	public void mouseReleased(MouseEvent e){
		a.f.g.release=new Point(e.getX(),e.getY());
		a.unpause();
	}

	public void mouseDragged(MouseEvent e){
		int x =(int) e.getX();
		int y =(int) e.getY();
	}


 	public void mouseMoved(MouseEvent e){}
 	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

}
