import java.awt.event.*;
public class EcouteurDeFenetre implements WindowListener{
	Avalam a;
	
	EcouteurDeFenetre(Avalam a){
		this.a=a;
	}
	
	public void windowClosing(WindowEvent e) {
		a.interupt=true;
		a.etatSuivant=a.QUITTER;
		a.unpause();
	}
	
	public void windowClosed(WindowEvent e){}
	public void windowOpened(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowGainedFocus(WindowEvent e){}
	public void windowLostFocus(WindowEvent e){}
	public void windowStateChanged(WindowEvent e){}
}
