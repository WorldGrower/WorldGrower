package org.worldgrower.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class SwingUtils {

	private static final KeyStroke ESCAPE_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	public static final String ESCAPE_KEY = "WorldGrower:WINDOW_CLOSING";

	public static void installEscapeCloseOperation(final JDialog dialog) {
		Action dispatchClosing = new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		};
		JRootPane root = dialog.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ESCAPE_KEY_STROKE, ESCAPE_KEY);
		root.getActionMap().put(ESCAPE_KEY, dispatchClosing);
	}
}
