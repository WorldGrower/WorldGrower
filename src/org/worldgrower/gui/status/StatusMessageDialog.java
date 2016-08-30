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

	public StatusMessageDialog(List<StatusMessage> statusMessages, SoundIdReader soundIdReader, JFrame parentFrame) {
		super(700, 475);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 16, 665, 380);
		addComponent(scrollPane);
		
		JList<StatusMessage> list = JListFactory.createJList(statusMessages.toArray(new StatusMessage[0]));
		list.setSelectedIndex(statusMessages.size() - 1);
		list.setCellRenderer(new StatusMessageListRenderer());
		scrollPane.setViewportView(list);
		list.ensureIndexIsVisible(list.getSelectedIndex());
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BorderLayout());
		buttonPane.setOpaque(false);
		buttonPane.setBounds(16, 417, 665, 40);
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

	private void addActions(JList<StatusMessage> list, JButton okButton) {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				StatusMessageDialog.this.dispose();
			}
		});
	}
}
