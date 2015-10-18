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
package org.worldgrower.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class JGradientButton extends JButton {
	public JGradientButton(String text) {
		super(text);
		setPaintOptions();
	}

	public JGradientButton(String text, ImageIcon icon) {
		super(text, icon);
		setPaintOptions();
	}

	private void setPaintOptions() {
		setContentAreaFilled(false);
		setFocusPainted(false);
	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g.create();
		if (getModel().isPressed()) {
			g2.setPaint(new GradientPaint(new Point(0, 0), Color.PINK.brighter(), new Point(0, getHeight()), Color.PINK));
		} else {
			g2.setPaint(new GradientPaint(new Point(0, 0), Color.WHITE, new Point(0, getHeight()), Color.PINK.darker()));
		}
		
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.dispose();

		super.paintComponent(g);
	}
}