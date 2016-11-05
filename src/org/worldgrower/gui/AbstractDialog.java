package org.worldgrower.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.worldgrower.gui.cursor.Cursors;
import org.worldgrower.gui.util.IconUtils;

public abstract class AbstractDialog extends JDialog {

	private final JPanel panel;
	
	public AbstractDialog(int width, int height, ImageInfoReader imageInfoReader) {
		super();
		panel = new TiledImagePanel(imageInfoReader);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setUndecorated(true);
		getContentPane().setLayout(null);
		IconUtils.setIcon(this);
		SwingUtils.installEscapeCloseOperation(this);
		
		setSize(width, height);
		setResizable(false);
		panel.setBounds(0, 0, width, height);
		panel.setPreferredSize(new Dimension(width, height));
		getContentPane().setPreferredSize(new Dimension(width, height));
		panel.setLayout(null);
		
		getContentPane().add(panel);
		
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setCursor(Cursors.CURSOR);
	}
	
	public final void addComponent(Component component) {
		panel.add(component);
	} 
	

}
