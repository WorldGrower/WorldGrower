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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.worldgrower.gui.util.ButtonFactory;
import org.worldgrower.gui.util.JComboBoxFactory;
import org.worldgrower.gui.util.JLabelFactory;

public class ListInputDialog extends AbstractDialog {

	private String value = null;
	private JComboBox<String> comboBox;
	
	public ListInputDialog(String question, String[] list) {
		this(question, null, list);
	}
	
	//TODO: use icon
	public ListInputDialog(String question, Icon icon, String[] list) {
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
		buttonPane.setBounds(16, 120, 415, 50);
		addComponent(buttonPane);

		JButton okButton = ButtonFactory.createButton(" OK ");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = ButtonFactory.createButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		addActions(okButton, cancelButton);
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
