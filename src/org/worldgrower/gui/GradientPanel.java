package org.worldgrower.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
  
public class GradientPanel extends JPanel {

	private final Color color1 = Color.WHITE;
	private final Color color2 = Color.LIGHT_GRAY;
  
    public GradientPanel() {
        super();
        setOpaque( true );
    }
  
    @Override
    public void paintComponent( Graphics g ) {
    	 Graphics2D g2d = (Graphics2D) g;
         int w = getWidth();
         int h = getHeight();
         GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
         g2d.setPaint(gp);
         g2d.fillRect(0, 0, w, h);
    }
}