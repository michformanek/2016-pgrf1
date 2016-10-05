import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Minimal GUI for drawing pixels
 * @author PGRF FIM UHK
 * @version 2016
 */

public class Canvas {

	private final JFrame frame;
	private final JPanel panel;
	private final BufferedImage img;

	public Canvas(final int width, final int height) {
		frame = new JFrame();
		frame.setTitle("UHK FIM PGRF : Canvas");
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(width, height));

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	public void clear(final int color) {
		final Graphics gr = img.getGraphics();
		gr.setColor(new Color(color));
		gr.fillRect(0, 0, img.getWidth(), img.getHeight());
	}

	public void present() {
		if (panel.getGraphics() != null)
			panel.getGraphics().drawImage(img, 0, 0, null);
	}

	public void draw() {
		clear(0x2f2f2f);
		img.setRGB(10, 10, 0xffff00);
	}

	public void start() {
		draw();
		present();
	}

	public static void main(String[] args) {
		final Canvas canvas = new Canvas(800, 600);
		SwingUtilities.invokeLater(() -> {
			SwingUtilities.invokeLater(() -> {
				SwingUtilities.invokeLater(() -> {
					SwingUtilities.invokeLater(() -> {
						canvas.start();
					});
				});
			});
		});
	}

}