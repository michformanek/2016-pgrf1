import annotations.NotNull;
import annotations.Nullable;
import gui.GuiColor;
import gui.SidebarButton;
import rasterdata.Presentable;
import rasterdata.RasterImage;
import rasterdata.RasterImageBuf;
import rasterfillops.FillerType;
import rasterfillops.scanline.ScanLine;
import rasterfillops.scanline.ScanLiner;
import rasterfillops.seedfill.SeedFill;
import rasterfillops.seedfill.SeedFill4;
import rasterfillops.patterns.*;
import rasterization.*;
import geometry.Point;
import util.*;

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
	JPanel panel;
	private final
	@NotNull
	Presentable<Graphics> imagePresenter;
	private final
	@NotNull
	LineRasterizerFactory<Integer> linerFactory;
	private final
	@NotNull
	PatternFactory patternFactory;
	private final
	@NotNull
	FillerFactory<Integer> fillerFactory = new FillerFactory();
	private final
	@NotNull
	PolygonRasterizer<Integer> polygonRasterizer;
	private final
	@NotNull
	CircleRasterizer<Integer> circleRasterizer;
	private final
	@NotNull
	List<Point> selectedPoints;
	private final
	@NotNull
	ScanLine<Integer> scanLine;
	private
	@NotNull
	LineRasterizer<Integer> liner;
	private
	@NotNull
	SeedFill<Integer> filler;
	private
	@NotNull
	RasterImage<Integer> img;
	private
	@NotNull
	Color color;
	private
	@NotNull
	Tool selectedTool;
	private
	@NotNull
	Pattern pattern;
	private
	@NotNull
	FillerType selectedFiller;

	public Canvas(final int width, final int height) {
		JFrame frame = new JFrame();
		frame.setTitle("UHK FIM PGRF : Canvas");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		final @NotNull RasterImageBuf<Integer> tempImage =
				new RasterImageBuf<>(width, height, BufferedImage.TYPE_INT_RGB, pixel -> pixel, pixel -> pixel);
		img = tempImage;
		imagePresenter = tempImage;

		linerFactory = new LineRasterizerFactory<>();
		patternFactory = new PatternFactory();
		liner = linerFactory.getLineRasterizer(LineRasterizerType.DDA);
		scanLine = new ScanLiner<>();
		filler = new SeedFill4<>();
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
				if (e.getButton() == MouseEvent.BUTTON3) {
					img.getPixel(e.getX(), e.getY()).ifPresent(
							(final @NotNull Integer areaValue) -> {
								if (selectedFiller == FillerType.SCANLINE) {
									img = scanLine.fill(selectedPoints, img, color.getRGB());
								} else {
									img = filler.fillPattern(img, e.getX(), e.getY(),
											color.getRGB(), color.brighter().brighter().getRGB(),
											pixel -> pixel.intValue() == areaValue.intValue(), pattern);
								}
								present();
							});
				} else {
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
		pattern = new SolidPattern();
		selectedFiller = FillerType.SCANLINE;
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
			img = liner.drawLine(img, selectedPoints.get(0).getX(), selectedPoints.get(0).getY(), selectedPoints.get(selectedPoints.size() - 1).getX(), selectedPoints.get(selectedPoints.size() - 1).getY(), color.getRGB());
		} else if (selectedTool == Tool.POLYGON) {
			img = polygonRasterizer.drawPolygon(img, selectedPoints, liner, color.getRGB());
		} else if (selectedTool == Tool.CIRCLE) {
			img = circleRasterizer.drawCircle(img, selectedPoints.get(0), selectedPoints.get(1), selectedPoints.get(selectedPoints.size() - 1), liner, color.getRGB());
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


		JComboBox<LineRasterizerType> comboBoxLiner = new JComboBox<>(LineRasterizerType.values());
		comboBoxLiner.setSelectedItem(LineRasterizerType.DDA);
		comboBoxLiner.addActionListener(e -> liner = linerFactory.getLineRasterizer((LineRasterizerType) comboBoxLiner.getSelectedItem()));
		comboBoxLiner.setBackground(GuiColor.MIDNIGHT_BLUE);
		comboBoxLiner.setForeground(GuiColor.NEPHRITIS);
		comboBoxLiner.setFocusable(false);
		((JLabel) comboBoxLiner.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		comboBoxLiner.setToolTipText("Pick your line algorithm");

		JComboBox<PatternType> comboBoxPattern = new JComboBox<>(PatternType.values());
		comboBoxPattern.setSelectedItem(PatternType.SOLID);
		comboBoxPattern.addActionListener(e -> pattern = patternFactory.getPattern((PatternType) comboBoxPattern.getSelectedItem()));
		comboBoxPattern.setBackground(GuiColor.MIDNIGHT_BLUE);
		comboBoxPattern.setForeground(GuiColor.NEPHRITIS);
		comboBoxPattern.setFocusable(false);
		((JLabel) comboBoxPattern.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		comboBoxPattern.setToolTipText("Pick your pattern");

		JComboBox<FillerType> comboBoxFiller = new JComboBox<>(FillerType.values());
		comboBoxFiller.setSelectedItem(FillerType.SCANLINE);
		comboBoxFiller.addActionListener(e -> {
			filler = fillerFactory.getFiller((FillerType) comboBoxFiller.getSelectedItem());
			selectedFiller = (FillerType) comboBoxFiller.getSelectedItem();
		});
		comboBoxFiller.setBackground(GuiColor.MIDNIGHT_BLUE);
		comboBoxFiller.setForeground(GuiColor.NEPHRITIS);
		comboBoxFiller.setFocusable(false);
		((JLabel) comboBoxFiller.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		comboBoxFiller.setToolTipText("Pick your fill algorithm");

		buttons.forEach(btn -> {
			sidebar.add(btn);
			if (btn.getTool().equals(selectedTool)) {
				btn.toggle();
			}
		});
		sidebar.add(comboBoxLiner);
		sidebar.add(comboBoxPattern);
		sidebar.add(comboBoxFiller);

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