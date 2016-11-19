package gui;

import javax.swing.JButton;

import util.Tool;

import java.awt.*;

/**
 * Created by Michal Formanek on 19.11.16.
 */
public class SidebarButton extends JButton {

	private Tool tool;
	private boolean inverse;

	public Tool getTool() {
		return tool;
	}

	public void setTool(final Tool tool) {
		this.tool = tool;
		setText(tool.toString());
	}

	public boolean isInverse() {
		return inverse;
	}

	public void toggle() {
		Color c = getForeground();
		setForeground(getBackground());
		setBackground(c);
		inverse = !inverse;
	}

}
