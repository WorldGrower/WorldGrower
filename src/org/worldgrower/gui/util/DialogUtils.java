/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower.gui.util;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.plaf.LayerUI;

import org.worldgrower.gui.font.Fonts;

/**
 * Based on example at http://www.aurelienribon.com/blog/2012/07/tutorial-beautifying-the-dialogs-in-swing-applications/
 */
public class DialogUtils {
	/**
	 * Centers the dialog over the given parent component. Also, creates a
	 * semi-transparent panel behind the dialog to mask the parent content.
	 * The title of the dialog is displayed in a custom fashion over the dialog
	 * panel, and a rectangular shadow is placed behind the dialog.
	 */
	public static void createDialogBackPanel(JDialog dialog, Component parent) {
		dialog.setBackground(new Color(255, 255, 255, 64));
		
		DialogBackPanel newContentPane = new DialogBackPanel(dialog.getContentPane(), dialog.getTitle());
		dialog.setContentPane(newContentPane);
		dialog.setSize(parent.getSize());
		dialog.setLocation(parent.getLocationOnScreen());
	}
	
	public static void createDialogBackPanel(JFrame dialog, Component parent) {
		dialog.setBackground(new Color(255, 255, 255, 64));
		
		DialogBackPanel newContentPane = new DialogBackPanel(dialog.getContentPane(), dialog.getTitle());
		dialog.setContentPane(newContentPane);
		dialog.setSize(parent.getSize());
		dialog.setLocation(parent.getLocationOnScreen());
	}

	/**
	 * Adds a glass layer to the dialog to intercept all key events. If the
	 * espace key is pressed, the dialog is disposed (either with a fadeout
	 * animation, or directly).
	 */
	public static void addEscapeToCloseSupport(final JDialog dialog, final boolean fadeOnClose) {
		LayerUI<Container> layerUI = new LayerUI<Container>() {
			private boolean closing = false;

			@Override
			public void installUI(JComponent c) {
				super.installUI(c);
				((JLayer) c).setLayerEventMask(AWTEvent.KEY_EVENT_MASK);
			}

			@Override
			public void uninstallUI(JComponent c) {
				super.uninstallUI(c);
				((JLayer) c).setLayerEventMask(0);
			}

			@Override
			public void eventDispatched(AWTEvent e, JLayer<? extends Container> l) {
				if (e instanceof KeyEvent && ((KeyEvent) e).getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (closing) return;
					closing = true;
					if (fadeOnClose) fadeOut(dialog);
					else dialog.dispose();
				}
			}
		};

		JLayer<Container> layer = new JLayer<>(dialog.getContentPane(), layerUI);
		dialog.setContentPane(layer);
	}

	/**
	 * Creates an animation to fade the dialog opacity from 0 to 1.
	 */
	public static void fadeIn(final JDialog dialog) {
		final Timer timer = new Timer(10, null);
		timer.setRepeats(true);
		timer.addActionListener(new ActionListener() {
			private float opacity = 0;
			@Override public void actionPerformed(ActionEvent e) {
				opacity += 0.15f;
				dialog.setOpacity(Math.min(opacity, 1));
				if (opacity >= 1) timer.stop();
			}
		});

		dialog.setOpacity(0);
		timer.start();
		dialog.setVisible(true);
	}

	/**
	 * Creates an animation to fade the dialog opacity from 1 to 0.
	 */
	public static void fadeOut(final JDialog dialog) {
		final Timer timer = new Timer(10, null);
		timer.setRepeats(true);
		timer.addActionListener(new ActionListener() {
			private float opacity = 1;
			@Override public void actionPerformed(ActionEvent e) {
				opacity -= 0.15f;
				dialog.setOpacity(Math.max(opacity, 0));
				if (opacity <= 0) {
					timer.stop();
					dialog.dispose();
				}
			}
		});

		dialog.setOpacity(1);
		timer.start();
	}

	// -------------------------------------------------------------------------
	// Helpers
	// -------------------------------------------------------------------------

	private static class DialogBackPanel extends JPanel {
		private final Paint fill = new Color(0xAAFFFFFF, true);
		private final ImageIcon shadowImage = new ImageIcon(DialogUtils.class.getResource("/dialogShadow.png"));
		private final JComponent cmp;
		private final JLabel title = new JLabel();
		private final JLabel info = new JLabel("Hit 'ESC' to close the dialog");

		public DialogBackPanel(Container contentPane, String titleText) {
			this.cmp = (JComponent) contentPane;

			setOpaque(false);
			setLayout(null);
			add(cmp);
			add(title);
			add(info);

			cmp.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
			title.setFont(Fonts.FONT);
			title.setForeground(Color.WHITE);
			info.setForeground(Color.WHITE);

			title.setText(titleText);
			title.setSize(title.getPreferredSize());
			info.setSize(info.getPreferredSize());
			cmp.setSize(cmp.getPreferredSize());
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			int w = getWidth();
			int h = getHeight();

			int shadowX = w/2 - (cmp.getWidth()+100)/2;
			int shadowY = h/2 - (cmp.getHeight()+100)/2;
			cmp.setLocation(w/2-cmp.getWidth()/2, h/2-cmp.getHeight()/2);
			title.setLocation(w/2-cmp.getWidth()/2, h/2-cmp.getHeight()/2-title.getHeight());
			info.setLocation(w/2+cmp.getWidth()/2-info.getWidth(), h/2-cmp.getHeight()/2-info.getHeight());

			Graphics2D gg = (Graphics2D) g.create();
			gg.setPaint(fill);
			gg.fillRect(0, 0, w, h);
			gg.drawImage(shadowImage.getImage(), shadowX, shadowY, cmp.getWidth()+100, cmp.getHeight()+100, null);
			gg.dispose();
		}
	}
}
