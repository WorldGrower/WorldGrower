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
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JListFactory;

public class StatusMessageDialog extends AbstractDialog {

	public StatusMessageDialog(List<String> statusMessages, SoundIdReader soundIdReader, JFrame parentFrame) {
		super(500, 475);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 16, 462, 340);
		addComponent(scrollPane);
		
		JList<String> list = JListFactory.createJList(statusMessages.toArray(new String[0]));
		list.setSelectedIndex(statusMessages.size() - 1);
		scrollPane.setViewportView(list);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BorderLayout());
		buttonPane.setOpaque(false);
		buttonPane.setBounds(16, 380, 465, 50);
		addComponent(buttonPane);

		JButton okButton = JButtonFactory.createButton(" OK ", soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton, BorderLayout.EAST);
		getRootPane().setDefaultButton(okButton);

		addActions(list, okButton);
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
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
