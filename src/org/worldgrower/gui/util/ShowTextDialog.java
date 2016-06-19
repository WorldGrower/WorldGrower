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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.music.SoundIdReader;

public class ShowTextDialog extends AbstractDialog {

	public ShowTextDialog(String value, SoundIdReader soundIdReader, JFrame parentFrame) {
		super(450, 160);
		
		JLabel label = JLabelFactory.createJLabel(value);
		label.setBounds(16, 16, 415, 50);
		addComponent(label);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setOpaque(false);
		buttonPane.setBounds(16, 70, 415, 50);
		addComponent(buttonPane);

		JButton okButton = JButtonFactory.createButton(" OK ", soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		addActions(okButton);
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}
	
	public void showMe() {
		setVisible(true);
	}

	private void addActions(JButton okButton) {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowTextDialog.this.dispose();
			}
		});
	}
}
