import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JComponent;

public class Regle extends JDialog {
	Avalam a;
	EcouteurDeRegle er;

	public Regle(Avalam a){
		this.a = a;
		this.er = new EcouteurDeRegle(a,this); 
		
		this.setTitle("Règles");
		this.setModal(true);

		this.setResizable(false);
		this.addWindowListener(er);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		JLabel lesRegles = new JLabel(

		"<html><CENTER><H1>Règles de l'Avalam </H1></CENTER>"
		+ "<BLOCKQUOTE><P>"
		+ "<b>Règle de base</b><br>"
		+ "Chaque joueur a 28 pions d'une même couleur. Le but du jeu<br>"
		+ "est de constituer un maximum de tours de 1 à 5 pions,<br>"
		+ "jamais plus, surmontées par un pion de sa couleur. Un joueur<br>"
		+ "est le propriétaire d'une tour lorsqu'un pion de sa couleur<br>"
		+ "en occupe le sommet. Un pion isolé constitue également une tour.<br>"
		+ "<br>"
		+ "<b>Déplacements</b><br>"
		+ "Chaque joueur en effectue un seul,dans n'importe quel sens<br>"
		+ "(horizontal, vertical, diagonal) avec n'importe quel pion<br>"
		+ "(ou pile de pions), quelle qu'en soit la couleur.<br>"
		+ "Ce mouvement consiste à empiler le ou les pions déplacés sur un<br>"
		+ "trou directement voisin déjà occupé par un ou plusieurs pions.<br>"
		+ "<br>"
		+ "<b>Mouvements interdits</b><br>"
		+ "On déplace obligatoirement toute la pile se trouvant sur un trou<br>"
		+ "(elle peut évidemment n'être constituée que d'un seul pion).<br>"
		+ "(ou pile de pions), quelle qu'en soit la couleur.<br>"
		+ "Autrement dit, une pile de pions ne peut qu'augmenter,<br>"
		+ "jamais diminuer.<br>"
		+ "On ne peut jamais poser de pions sur un trou inoccupé: il le reste<br>" 
		+ "donc définitivement. Un pion (ou une tour) isolé de tous les cotés<br"
		+ "ne pourra donc plus changer de propriétaire. <br>"
		+ "<br>"
		+ "<b>Fin de partie</b><br>"
		+ "Tant qu'un joueur peut effectuer un mouvement il a l'obligation<br>"
		+ "de jouer,la partie ne s'achevant que lorque plus aucun déplacement<br>"
		+ "n'est possible.On compte alors combien de pions de chaque couleur<br>"
		+ "occupent le sommet des tours restantes, le vainqueur étant<br>"
		+ "évidemment celui qui totalise le plus de tours<br>"
		+ "Attention! Qu'une tour comporte 1,2,... ou 5 pions,<br>"
		+ "elle vaut toujours UN point.<br>"
		+ "<br>"
		+ "<i>Source :http://jeuxstrategie.free.fr/Avalam_complet.php</i><br>"
		+ "<br>"
		+ "</P>"
		+ "</html>");
		
		JScrollPane sc = new JScrollPane (lesRegles);
		this.add(sc);
		this.setSize(600,350);
	  
		// Position au centre de la fenetre principale
		Point locAvalam  = a.f.getLocation();
		Dimension tailleAvalam = a.f.getSize();
		Dimension taillePref   = this.getSize();

		int x, y;
		x = (tailleAvalam.width - taillePref.width)/2+locAvalam.x;
		y = (tailleAvalam.height - taillePref.height)/2+locAvalam.y;

		this.setLocation(x,y);
	}
	  
	public void afficherRegle(){
		this.setVisible(true);
	}
}