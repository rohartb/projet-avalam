import java.awt.*;
import javax.swing.*;

public class StatusBar extends JPanel {
	Avalam a;

	public StatusBarPanel PanelTemps, PanelCoups, PanelMode;
	public JLabel labelCoupsRestants;
	public StatusBarPanel[] panels;
	private static final long serialVersionUID = 1L;

	public StatusBar(Avalam a) {
		super();
		this.a = a;
		PanelTemps = new StatusBarPanel() ;
		PanelTemps.setPanelWidth(10);
		PanelTemps.setWidthFixed(true);
		PanelTemps.setPanelBorder();
		PanelTemps.setLayout(new BorderLayout());
		//temps.setHorizontalAlignment(JLabel.CENTER);
		PanelTemps.add(new JLabel ("Blabla time"));

		PanelCoups = new StatusBarPanel() ;
		PanelCoups.setPanelWidth(100);
		PanelCoups.setWidthFixed(false);
		PanelCoups.setPanelBorder();
		PanelCoups.setLayout(new BorderLayout());
		//m.coupsRestants.setHorizontalAlignment(JLabel.CENTER);
		labelCoupsRestants = new JLabel("Coups Restants : " + a.j.nbCoupsRestants);
		PanelCoups.add(labelCoupsRestants);

		PanelMode = new StatusBarPanel() ;
		PanelMode.setPanelWidth(100);
		PanelMode.setWidthFixed(true);
		PanelMode.setPanelBorder();
		PanelMode.setLayout(new BorderLayout());
		//m.mode.setHorizontalAlignment(JLabel.CENTER);
		PanelMode.add(new JLabel("Blabla mode"));

		StatusBarPanel[] BarStatut = {PanelTemps, PanelCoups,PanelMode};
		this.panels =  BarStatut;
		initialiser();
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
		labelCoupsRestants.setText("Coups Restants : " + a.j.nbCoupsRestants);
	}
}

