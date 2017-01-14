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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.TiledHorizontalImageProgressBar;
import org.worldgrower.gui.TiledImagePanel;
import org.worldgrower.gui.cursor.Cursors;

public class ProgressDialog extends JFrame {

	private JProgressBar progressBar;
	
	public ProgressDialog(String description, int max, ImageInfoReader imageInfoReader) {
		getContentPane().setLayout(null);
		IconUtils.setIcon(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setCursor(Cursors.CURSOR);
		
		int width = 450;
		int height = 210;
		
		setSize(width, height);
		setResizable(false);
		setUndecorated(true);
		((JComponent)getRootPane()).setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		JPanel gradientPanel = new TiledImagePanel(imageInfoReader);
		gradientPanel.setBounds(0, 0, width, height);
		gradientPanel.setLayout(null);
		
		getContentPane().add(gradientPanel);
		
		this.setLocationRelativeTo(null);
		
		JLabel label = JLabelFactory.createJLabel(description);
		label.setBounds(16, 16, 415, 50);
		gradientPanel.add(label);
		
		progressBar = new TiledHorizontalImageProgressBar(0, max, ImageIds.PROGRESSBAR_BACKGROUND, imageInfoReader);
		progressBar.setBounds(16, 70, 415, 30);
		gradientPanel.add(progressBar);
	}
	
	public void showMe() {
		setVisible(true);
	}

	public void setValue(int value) {
		progressBar.setValue(value);
	}
	
	public void close() {
		this.dispose();
	}
}
