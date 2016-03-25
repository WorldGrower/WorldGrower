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

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class JGradientButton extends JButton {
	
	private static final int BORDER_PADDING = 4;
	
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
			g2.setPaint(new GradientPaint(new Point(0, 0), ColorPalette.LIGHT_BACKGROUND_COLOR, new Point(0, getHeight()), ColorPalette.LIGHT_BACKGROUND_COLOR.brighter()));
		} else {
			g2.setPaint(new GradientPaint(new Point(0, 0), ColorPalette.DARK_BACKGROUND_COLOR, new Point(0, getHeight()), ColorPalette.DARK_BACKGROUND_COLOR.brighter()));
		}
		
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		if (isFocusOwner()) {
			drawDashedLine(g2, BORDER_PADDING, BORDER_PADDING, getWidth() - BORDER_PADDING, BORDER_PADDING);
			drawDashedLine(g2, getWidth() - BORDER_PADDING, BORDER_PADDING, getWidth() - BORDER_PADDING, getHeight() - BORDER_PADDING);
			drawDashedLine(g2, getWidth() - BORDER_PADDING, getHeight() - BORDER_PADDING, BORDER_PADDING, getHeight() - BORDER_PADDING);
			drawDashedLine(g2, BORDER_PADDING, getHeight() - BORDER_PADDING, BORDER_PADDING, BORDER_PADDING);
		}
		
		g2.dispose();

		super.paintComponent(g);
	}
	
	private void drawDashedLine(Graphics2D g2, int x1, int y1, int x2, int y2){
       
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
        g2.setStroke(dashed);
        g2.setColor(ColorPalette.FOREGROUND_COLOR);
        g2.drawLine(x1, y1, x2, y2);
	}
}