package org.worldgrower.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableCellRenderer;

public class SwingUtils {

	private static final KeyStroke ESCAPE_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	private static final String ESCAPE_KEY = "WorldGrower:WINDOW_CLOSING";

	public static void installEscapeCloseOperation(final JDialog dialog) {
		Action dispatchClosing = new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		};
		JRootPane root = dialog.getRootPane();
		installCloseAction(dispatchClosing, root);
	}

	public static void installCloseAction(Action dispatchClosing, JRootPane root) {
		root.getInputMap(JComponent.WHEN_FOCUSED).put(ESCAPE_KEY_STROKE, ESCAPE_KEY);
		root.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ESCAPE_KEY_STROKE, ESCAPE_KEY);
		root.getActionMap().put(ESCAPE_KEY, dispatchClosing);
	}
	
	public static void installEscapeCloseOperation(final JFrame dialog) {
		Action dispatchClosing = new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		};
		JRootPane root = dialog.getRootPane();
		installCloseAction(dispatchClosing, root);
	}
	
	public static void makeTransparant(JTable table, JScrollPane scrollPane) {
		table.setOpaque(false);
		((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setOpaque(false);
		((DefaultTableCellRenderer)table.getDefaultRenderer(String.class)).setOpaque(false);
		((JComponent)table.getDefaultRenderer(Boolean.class)).setOpaque(false);
		((DefaultTableCellRenderer)table.getDefaultRenderer(Integer.class)).setOpaque(false);
		
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
	}
	
	public static void setBoundsAndCenterHorizontally(JComponent component, int x, int y, int width, int height) {
		Container parent = component.getParent();
		int parentWidth = parent.getWidth();
		int paddingOnBothSides = parentWidth - width;
		x = paddingOnBothSides / 2;
		component.setBounds(x, y, width, height);
	}
	
	public static void centerFrame(JFrame frame) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	}
	
	public static void setComboBoxSelectionColor(JComboBox comboBox, Color color) {
		Object child = comboBox.getAccessibleContext().getAccessibleChild(0);
		BasicComboPopup popup = (BasicComboPopup)child;
		JList list = popup.getList();
		list.setSelectionForeground(color);
	}
	
	public static void setMenuIcon(JMenuItem menuItem, ImageIds imageIds, ImageInfoReader imageInfoReader) {
		Image image = imageInfoReader.getImage(imageIds, null);
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		if (imageWidth > 96 || imageHeight > 96) {
			image = cropImage((BufferedImage)image, Math.min(imageWidth, 96), Math.min(imageHeight, 96));
		}
		menuItem.setIcon(new ImageIcon(image));
	}

	private static BufferedImage cropImage(BufferedImage src, int width, int height) {
		BufferedImage dest = src.getSubimage(0, 0, width, height);
		return dest;
	}
}
