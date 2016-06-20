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

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.music.SoundIdReader;

public class ListInputDialog extends AbstractDialog {

	private String value = null;
	private JComboBox<String> comboBox;
	
	public ListInputDialog(String question, String[] list, SoundIdReader soundIdReader, JFrame parentFrame) {
		this(question, null, list, soundIdReader, parentFrame);
	}
	
	//TODO: use icon
	public ListInputDialog(String question, Icon icon, String[] list, SoundIdReader soundIdReader, JFrame parentFrame) {
		super(450, 210);
		
		JLabel label = JLabelFactory.createJLabel(question);
		label.setBounds(16, 16, 415, 50);
		addComponent(label);
		
		comboBox = JComboBoxFactory.createJComboBox(list);
		comboBox.setBounds(16, 70, 415, 30);
		addComponent(comboBox);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setOpaque(false);
		buttonPane.setBounds(16, 153, 425, 50);
		addComponent(buttonPane);

		JButton okButton = JButtonFactory.createButton(" OK ", soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = JButtonFactory.createButton("Cancel", soundIdReader);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		addActions(okButton, cancelButton);
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}
	
	public String showMe() {
		setVisible(true);
		return value;
	}

	private void addActions(JButton okButton, JButton cancelButton) {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				value = (String) comboBox.getSelectedItem();
				ListInputDialog.this.dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				value = null;
				ListInputDialog.this.dispose();
			}
		});
	}
}
