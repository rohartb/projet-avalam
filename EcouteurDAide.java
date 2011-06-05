import java.awt.event.*;

public class EcouteurDAide implements MouseListener  {
	Avalam a;
	TerrainGraphique g;
	boolean actif;


	public EcouteurDAide (Avalam a, TerrainGraphique g) {
		this.a = a;
		this.g = g;
		actif=true;
	}

	public void mousePressed(MouseEvent e){
		if(actif){
			a.aide=true;
			a.unpause();
		}
	}


	public void mouseReleased(MouseEvent e){}
 	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

}
