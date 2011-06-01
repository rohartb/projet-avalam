import java.awt.*;
import javax.swing.*;
import java.text.*;
import java.sql.*;

public class StatusBar extends JPanel {
	Avalam a;

	public StatusBarPanel PanelTemps, PanelCoups, PanelMode;
	public JLabel labelCoupsRestants,labelMode;
	public StatusBarPanel[] panels;
	private static final long serialVersionUID = 1L;

	SimpleDateFormat sdf;
	Time start;
	JLabel labelTemps;
	Timer timer;

	public StatusBar(Avalam a) {
		super();

		sdf = new SimpleDateFormat("mm:ss");
		start = new Time(0);
		EcouteurDeTimer et = new EcouteurDeTimer(this);
		timer = new Timer(1000, et);
		timer.setActionCommand("ticseconde");
		labelTemps = new JLabel("  " + sdf.format(start));
		timer.start();

		this.a = a;
		PanelTemps = new StatusBarPanel() ;
		PanelTemps.setPanelWidth(10);
		PanelTemps.setWidthFixed(true);
		PanelTemps.setPanelBorder();
		PanelTemps.setLayout(new BorderLayout());
		//temps.setHorizontalAlignment(JLabel.CENTER);
		PanelTemps.add(labelTemps);

		PanelCoups = new StatusBarPanel() ;
		PanelCoups.setPanelWidth(100);
		PanelCoups.setWidthFixed(false);
		PanelCoups.setPanelBorder();
		PanelCoups.setLayout(new BorderLayout());
		labelCoupsRestants = new JLabel("Déplacement possible : " + a.j.nbCoupsRestants);
		labelCoupsRestants.setHorizontalAlignment(JLabel.CENTER);
		PanelCoups.add(labelCoupsRestants);

		PanelMode = new StatusBarPanel() ;
		PanelMode.setPanelWidth(100);
		PanelMode.setWidthFixed(true);
		PanelMode.setPanelBorder();
		PanelMode.setLayout(new BorderLayout());
		labelMode = new JLabel("Mode : " + a.j.getModeString());
		labelMode.setHorizontalAlignment(JLabel.CENTER);
		PanelMode.add(labelMode);

		StatusBarPanel[] BarStatut = {PanelTemps, PanelCoups,PanelMode};
		this.panels =  BarStatut;
		initialiser();
	}

	void initTimer(){
		start = new Time(0);
	}

	public StatusBar(StatusBarPanel[] panels) {
		super();
		this.panels = panels;
		initialiser();
	}

	public void initialiser() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		constraints.anchor=GridBagConstraints.WEST;
		constraints.insets = new Insets(0, 0, 0, 0);
		this.setLayout(new GridBagLayout());
		for (int i=0; i<panels.length; i++) {
			if (i==panels.length-1) constraints.insets=new Insets(0, 0, 0, 0);
			if (i==1) constraints.anchor=GridBagConstraints.CENTER;
			constraints.ipadx = (panels[i].getPanelWidth()==-1) ? 0 : panels[i].getPanelWidth();
			if (panels[i].isWidthFixed()) {
				constraints.fill=GridBagConstraints.NONE;
				constraints.weightx = 0;
			} else {
				constraints.fill=GridBagConstraints.HORIZONTAL;
				constraints.weightx = 100;
			}
			constraints.gridx=i;
			this.add(panels[i], constraints);
		}
	}

	public void actualiser() {
		labelCoupsRestants.setText("Déplacement possible : " + a.j.nbCoupsRestants);
		labelMode.setText("Mode : " + a.j.getModeString());
	}
}

