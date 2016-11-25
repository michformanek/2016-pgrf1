import annotations.NotNull;
import annotations.Nullable;
import gui.GuiColor;
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
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
	CircleRasterizer<Integer> circleRasterizer;
	private final
	@NotNull
	List<Point> selectedPoints;
	private
	@NotNull
	LineRasterizer<Integer> liner;
	private
	@NotNull
	RasterImage<Integer> img;
	private
	@NotNull
	Color color;
	private
	@NotNull
	Tool selectedTool;

	public Canvas(final int width, final int height) {
		frame = new JFrame();
		frame.setTitle("UHK FIM PGRF : Canvas");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		final @NotNull RasterImageBuf<Integer> tempImage =
				new RasterImageBuf<>(width, height, BufferedImage.TYPE_INT_RGB, pixel -> pixel, pixel -> pixel);
		img = tempImage;
		imagePresenter = tempImage;

		linerFactory = new LineRasterizerFactory<>();
		liner = linerFactory.getLineRasterizer(LineRasterizerType.DDA);
		polygonRasterizer = new PolygonRasterizerImpl<>();
		circleRasterizer = new CircleSegmentRasterizer<>();
		selectedTool = Tool.LINE;

		selectedPoints = new ArrayList<>();
		color = GuiColor.SUN_FLOWER;
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(width, height));

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				selectedPoints.add(PointConverter.convertToNDC(new Point(e.getX(), e.getY()), width, height));
				if (selectedTool == Tool.LINE) {
					if (selectedPoints.size() == 1) {
						selectedPoints.add(PointConverter.convertToNDC(new Point(e.getX(), e.getY()), width, height));
					} else {
						selectedPoints.set(1, PointConverter.convertToNDC(new Point(e.getX(), e.getY()), width, height));
					}
					draw();
					present();
				}
				if (selectedTool == Tool.CIRCLE && selectedPoints.size() == 1) {
					selectedPoints.add(PointConverter.convertToNDC(new Point(e.getX(), e.getY()), width, height));
				}
				if (selectedTool == Tool.POLYGON) {
					draw();
					present();
				}
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

			@Override
			public void mouseMoved(final MouseEvent e) {
				final Point point = PointConverter.convertToNDC(new Point(e.getX(), e.getY()), width, height);
				if (selectedTool == Tool.CIRCLE) {
					if (selectedPoints.size() < 4 && selectedPoints.size() > 1) {
						selectedPoints.set(selectedPoints.size() - 1, point);
						draw();
						present();
					}
					if (selectedPoints.size() == 4) {
						selectedPoints.clear();
					}
				}
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
		if (selectedTool == Tool.LINE && selectedPoints.size() == 2) {
			img = liner.drawLine(img, selectedPoints.get(0).getX(), selectedPoints.get(0).getY(), selectedPoints.get(1).getX(), selectedPoints.get(1).getY(), color.getRGB());
		} else if (selectedTool == Tool.POLYGON && selectedPoints.size() > 1) {
			img = polygonRasterizer.drawPolygon(img, selectedPoints, liner, color.getRGB());
		} else if (selectedTool == Tool.CIRCLE) {
			if (selectedPoints.size() == 2) {
				img = circleRasterizer.drawCircle(img, selectedPoints.get(0), selectedPoints.get(1), selectedPoints.get(1), liner, color.getRGB());
			}
			if (selectedPoints.size() == 3) {
				img = circleRasterizer.drawCircle(img, selectedPoints.get(0), selectedPoints.get(1), selectedPoints.get(2), liner, color.getRGB());
			}
		}
	}

	public void start() {
		draw();
		present();
	}

	private JPanel createSidebar() {
		final JPanel sidebar = new JPanel();
		final List<SidebarButton> buttons = new ArrayList<>();
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
			selectedPoints.clear();
			buttons.forEach(btn -> {
				if (btn.isInverse()) {
					btn.toggle();
				}
				if (btn.getTool() == selectedTool) {
					btn.toggle();
				}
			});
		});

		for (Tool tool : Tool.values()) {
			buttons.add(createButton(tool, toolListeners));
		}


		JComboBox<LineRasterizerType> comboBox = new JComboBox<>(LineRasterizerType.values());
		comboBox.setSelectedItem(LineRasterizerType.DDA);
		comboBox.addActionListener(e -> liner = linerFactory.getLineRasterizer((LineRasterizerType) comboBox.getSelectedItem()));
		comboBox.setBackground(GuiColor.MIDNIGHT_BLUE);
		comboBox.setForeground(GuiColor.NEPHRITIS);
		comboBox.setFocusable(false);
		((JLabel) comboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
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
		sidebarButton.setForeground(GuiColor.NEPHRITIS);
		sidebarButton.setBackground(GuiColor.MIDNIGHT_BLUE);
		sidebarButton.setTool(tool);
		return sidebarButton;
	}

}