package org.worldgrower.gui;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.worldgrower.gui.util.IconUtils;

public abstract class AbstractDialog extends JDialog {

	private final GradientPanel gradientPanel = new GradientPanel();
	
	public AbstractDialog(int width, int height) {
		super();
		setModalityType(ModalityType.APPLICATION_MODAL);		
		getContentPane().setLayout(null);
		IconUtils.setIcon(this);
		SwingUtils.installEscapeCloseOperation(this);
		
		setSize(width, height);
		setResizable(false);
		gradientPanel.setBounds(0, 0, width, height);
		gradientPanel.setLayout(null);
		
		getContentPane().add(gradientPanel);
		
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public final void addComponent(Component component) {
		gradientPanel.add(component);
	}
}
