package org.worldgrower.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
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

	private static void installCloseAction(Action dispatchClosing, JRootPane root) {
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ESCAPE_KEY_STROKE, ESCAPE_KEY);
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
}
