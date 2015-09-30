package org.worldgrower.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
  
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
  
    public static void main( String[] arg ) {
        JFrame f = new JFrame( "GradientPanel Test" );
        f.setDefaultCloseOperation( f.EXIT_ON_CLOSE );
        GradientPanel p = new GradientPanel();
        p.setBackground( Color.green );
        p.add( new JButton( "Button" ) );
        p.add( new JTextField( 12 ) );
        p.setBorder( BorderFactory.createTitledBorder( "Gradient Panel" ) );
        f.getContentPane().add( p );
        f.setSize( 300, 300 );
        f.setVisible( true );
    }
}