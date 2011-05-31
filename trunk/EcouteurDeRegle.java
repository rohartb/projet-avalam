import java.awt.event.*;
public class EcouteurDeRegle implements WindowListener{
	Avalam a;
	Regle r;
	
	EcouteurDeRegle(Avalam a,Regle r){
		this.a=a;
		this.r=r;
	}
	
	public void windowClosing(WindowEvent e) {
		r.setVisible(false);
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
