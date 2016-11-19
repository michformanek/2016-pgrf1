import annotations.NotNull;
import annotations.Nullable;
import gui.SidebarButton;
import rasterdata.Presentable;
import rasterdata.RasterImage;
import rasterdata.RasterImageBuf;
import rasterization.*;
import rasterization.Point;
import util.PointConverter;
import util.Tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal GUI for drawing pixels
 *
 * @author PGRF FIM UHK
 * @version 2016
 */

public class Canvas {

	private final
	@NotNull
	JFrame frame;
	private final
	@NotNull
	JPanel panel;
	private final
	@NotNull
	Presentable<Graphics> imagePresenter;
	private final
	@NotNull
	LineRasterizerFactory<Integer> linerFactory;
	private final
	@NotNull
	PolygonRasterizer<Integer> polygonRasterizer;
	private final
	@NotNull
	List<Point> selectedPoints;
	private final Color btnColor;
	private
	@NotNull
	LineRasterizer<Integer> liner;
	private
	@NotNull
	RasterImage<Integer> img;
	private Color color;
	private Tool selectedTool;

	public Canvas(final int width, final int height) {
		frame = new JFrame();
		frame.setTitle("UHK FIM PGRF : Canvas");
		frame.setResizable(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		final @NotNull RasterImageBuf<Integer> tempImage =
				new RasterImageBuf<>(width, height, BufferedImage.TYPE_INT_RGB, pixel -> pixel, pixel -> pixel);
		img = tempImage;
		imagePresenter = tempImage;

		linerFactory = new LineRasterizerFactory<>();
		liner = linerFactory.getLineRasterizer(LineRasterizerType.TRIVIAL);
		polygonRasterizer = new PolygonRasterizerImpl<>();
		selectedTool = Tool.LINE;

		selectedPoints = new ArrayList<>();
		color = new Color(0x08A0AB);
		btnColor = new Color(0x2ecc71);

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(width, height));

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				if (selectedTool == Tool.LINE) {
					if (selectedPoints.size() == 0) {
						selectedPoints.add(PointConverter.convertToNDC(new Point(e.getX(), e.getY()), width, height));
						selectedPoints.add(PointConverter.convertToNDC(new Point(e.getX(), e.getY()), width, height));
					} else {
						selectedPoints.set(1, PointConverter.convertToNDC(new Point(e.getX(), e.getY()), width, height));
					}
				} else if (selectedTool == Tool.POLYGON) {
					selectedPoints.add(PointConverter.convertToNDC(new Point(e.getX(), e.getY()), width, height));
				}
				draw();
				present();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if ((selectedTool == Tool.LINE)) {
					selectedPoints.clear();
				}
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(final MouseEvent e) {
				if (selectedTool == Tool.LINE) {
					selectedPoints.set(1, PointConverter.convertToNDC(new Point(e.getX(), e.getY()), width, height));
				}
				draw();
				present();
			}
		});


		frame.add(createSidebar(), BorderLayout.EAST);
		frame.add(panel, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		final Canvas canvas = new Canvas(800, 600);
		SwingUtilities.invokeLater(() -> {
			SwingUtilities.invokeLater(() -> {
				SwingUtilities.invokeLater(() -> {
					SwingUtilities.invokeLater(canvas::start);
				});
			});
		});
	}

	public void clear(final int color) {
		img = img.fill(color);
	}

	public void present() {
		final @Nullable Graphics graphics = panel.getGraphics();
		if (graphics != null)
			imagePresenter.present(graphics);
	}

	public void draw() {
		clear(0x2f2f2f);
		if (selectedTool == Tool.LINE && selectedPoints.size() > 1) {
			img = liner.drawLine(img, selectedPoints.get(0).getX(), selectedPoints.get(0).getY(), selectedPoints.get(1).getX(), selectedPoints.get(1).getY(), color.getRGB());
		} else if (selectedTool == Tool.CIRCLE) {
			// TODO img = kruzitko.kruh
		} else if (selectedTool == Tool.POLYGON && selectedPoints.size() > 1) {
			img = polygonRasterizer.drawPolygon(img, selectedPoints, liner, color.getRGB());
		}
	}

	public void start() {
		draw();
		present();
	}

	private JPanel createSidebar() {
		JPanel sidebar = new JPanel();
		List<SidebarButton> buttons = new ArrayList<>();
		sidebar.setLayout(new GridLayout(0, 1));

		final List<ActionListener> toolListeners = new ArrayList<>();
		toolListeners.add(e -> {
			final SidebarButton clickedBtn = (SidebarButton) e.getSource();
			if (clickedBtn.getTool().equals(Tool.CLEAR)) {
				clickedBtn.toggle();
				buttons.get(0).toggle();
				selectedTool = Tool.LINE;
				selectedPoints.clear();
				clear(0x2f2f2f);
				present();
			}
		});
		toolListeners.add(e -> {
			final SidebarButton clickedBtn = (SidebarButton) e.getSource();
			if (clickedBtn.getTool().equals(Tool.COLOR)) {
				clickedBtn.toggle();
				buttons.get(0).toggle();
				selectedTool = Tool.LINE;
				color = JColorChooser.showDialog(null, "Choose a Color", color);
			}
		});
		toolListeners.add(e -> {
			final SidebarButton clickedBtn = (SidebarButton) e.getSource();
			selectedTool = clickedBtn.getTool();
			buttons.forEach(btn -> {
				if (btn.getTool() == selectedTool && !btn.isInverse()) {
					btn.toggle();
				} else if (btn.getTool() != selectedTool && btn.isInverse()) {
					btn.toggle();
				}
			});
		});

		for (Tool tool : Tool.values()) {
			buttons.add(createButton(tool, toolListeners));
		}


		JComboBox<LineRasterizerType> comboBox = new JComboBox<>(LineRasterizerType.values());
		comboBox.setSelectedItem(LineRasterizerType.TRIVIAL);
		comboBox.addActionListener(e -> liner = linerFactory.getLineRasterizer((LineRasterizerType) comboBox.getSelectedItem()));
		comboBox.setBackground(new Color(0x95a5a6));
		comboBox.setForeground(new Color(0x2c3e50));
		comboBox.setToolTipText("Pick your line algorithm");

		buttons.forEach(btn -> {
			sidebar.add(btn);
			if (btn.getTool().equals(selectedTool)) {
				btn.toggle();
			}
		});
		sidebar.add(comboBox);

		return sidebar;
	}

	private SidebarButton createButton(final Tool tool, final List<ActionListener> listeners) {
		final SidebarButton sidebarButton = new SidebarButton();
		listeners.forEach(sidebarButton::addActionListener);
		sidebarButton.setFocusPainted(false);
		sidebarButton.setBorderPainted(false);
		sidebarButton.setForeground(new Color(0x2ecc71));
		sidebarButton.setBackground(new Color(0x34495e));
		sidebarButton.setTool(tool);
		return sidebarButton;
	}

}