import java.awt.event.*;

public class EcouteurDAide implements MouseListener  {
	Avalam a;
	TerrainGraphique g;


	public EcouteurDAide (Avalam a, TerrainGraphique g) {
		this.a = a;
		this.g = g;
	}

	public void mousePressed(MouseEvent e){
		System.out.println("Je veux de l'aide !!!!! ");
	}


	public void mouseReleased(MouseEvent e){}
 	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

}
