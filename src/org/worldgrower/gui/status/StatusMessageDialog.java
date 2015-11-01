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
package org.worldgrower.gui.status;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.util.ButtonFactory;

public class StatusMessageDialog extends AbstractDialog {

	public StatusMessageDialog(List<String> statusMessages) {
		super(450, 475);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 16, 412, 340);
		addComponent(scrollPane);
		
		JList<String> list = new JList<>(statusMessages.toArray(new String[0]));
		list.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		list.setForeground(ColorPalette.FOREGROUND_COLOR);
		list.setSelectedIndex(statusMessages.size() - 1);
		scrollPane.setViewportView(list);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BorderLayout());
		buttonPane.setOpaque(false);
		buttonPane.setBounds(16, 380, 415, 50);
		addComponent(buttonPane);

		JButton okButton = ButtonFactory.createButton(" OK ");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton, BorderLayout.EAST);
		getRootPane().setDefaultButton(okButton);

		addActions(list, okButton);
	}
	
	public void showMe() {
		setVisible(true);
	}

	private void addActions(JList<String> list, JButton okButton) {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				StatusMessageDialog.this.dispose();
			}
		});
	}
}
