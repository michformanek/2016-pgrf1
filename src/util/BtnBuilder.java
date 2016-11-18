package util;

import annotations.NotNull;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Michal Formanek on 18.11.16.
 */
public class BtnBuilder {

	private String text = "Button";
	private Color background = new Color(0x2ecc71);
	private Color foreground = new Color(0x34495e);
	private List<ActionListener> actionListeners = new ArrayList<>();

	public BtnBuilder text(final @NotNull String text) {
		this.text = text;
		return this;
	}

	public BtnBuilder background(final @NotNull Color color) {
		this.background = color;
		return this;
	}

	public BtnBuilder foreground(final @NotNull Color color) {
		this.foreground = color;
		return this;
	}

	public BtnBuilder addActionListener(ActionListener listener) {
		this.actionListeners.add(listener);
		return this;
	}

	public JButton buildBtn() {
		final JButton btn = new JButton();
		btn.setBackground(background);
		btn.setForeground(foreground);
		btn.setText(text);
		actionListeners.forEach(btn::addActionListener);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		return btn;
	}
}
