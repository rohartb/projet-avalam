import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class StatusBarPanel extends JPanel {

	private int panelWidth = -1;
	private boolean widthFixed = false;
	private boolean panelBorder = true;
	private static final long serialVersionUID = 1L;

	public StatusBarPanel() {
		super();
	}

	public void setPanelBorder() {
		this.setBorder(BorderFactory.createMatteBorder(1,1,0,0, Color.lightGray));
	}

	public void setTopBorder() {
	this.setBorder(BorderFactory.createMatteBorder(1,0,0,0, Color.lightGray));
	}

	public int getPanelWidth() {
		return panelWidth;
	}

	public void setPanelWidth(int panelWidth) {
		this.panelWidth = panelWidth;
	}
	public boolean isWidthFixed() {
		return widthFixed;
	}
	public void setWidthFixed(boolean widthFixed) {
		this.widthFixed = widthFixed;
	}
}
