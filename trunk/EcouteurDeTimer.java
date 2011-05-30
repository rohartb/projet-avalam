import java.awt.event.*;

public class EcouteurDeTimer implements ActionListener {
	StatusBar s;

	public EcouteurDeTimer(StatusBar s) {
		this.s =s;
	}


	public void actionPerformed(ActionEvent e) {
		s.start.setTime(s.start.getTime()+1000);
		s.labelTemps.setText("  " + s.sdf.format(s.start));

	}
}
