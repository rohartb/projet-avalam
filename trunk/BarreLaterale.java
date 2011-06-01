import javax.swing.*;
import java.awt.*;

public class BarreLaterale extends JComponent {
	Avalam a;

	public BarreLaterale(Avalam a) {
		this.a = a;
	}

	public void joueur1(Graphics2D drawable) {

	}


	public void joueur2(Graphics2D drawable) {

	}

	public void paintComponent(Graphics g) {
		Graphics2D drawable = (Graphics2D) g;

		Dimension p = getSize();
		int hauteur = p.height;
		int largeur = p.width;

		drawable.setPaint(Themes.getCouleurFond(a.f.g.theme));
		drawable.fillRect(0,0, largeur, hauteur);
		drawable.setPaint(Color.red);
		drawable.fillRect(50, 50, 20, 20);
		joueur1(drawable);
		joueur2(drawable);
	}
}
