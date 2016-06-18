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
package org.worldgrower.gui.loadsave;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JListFactory;

public class LoadSaveDialog extends AbstractDialog {

	public LoadSaveDialog(SaveGameHandler saveGameHandler, LoadSaveMode loadSaveMode, SoundIdReader soundIdReader) {
		super(500, 475);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 16, 462, 340);
		addComponent(scrollPane);
		
		JList<SaveGame> list = JListFactory.createJList(loadSaveMode.getSaveFiles());
		list.setOpaque(true);
		list.setSelectedIndex(0);
		list.setCellRenderer(new SaveGameRenderer());
		scrollPane.setViewportView(list);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BorderLayout());
		buttonPane.setOpaque(false);
		buttonPane.setBounds(16, 380, 465, 50);
		addComponent(buttonPane);

		JButton okButton = JButtonFactory.createButton(loadSaveMode.getDescription(), soundIdReader);
		okButton.setActionCommand("OK");
		okButton.setEnabled(false);
		buttonPane.add(okButton, BorderLayout.EAST);
		getRootPane().setDefaultButton(okButton);
		
		JButton cancelButton = JButtonFactory.createButton("Cancel", soundIdReader);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton, BorderLayout.WEST);
		getRootPane().setDefaultButton(cancelButton);

		addActions(list, okButton, cancelButton, loadSaveMode, saveGameHandler);
	}
	
	public void showMe() {
		setVisible(true);
	}

	private void addActions(JList<SaveGame> list, JButton okButton, JButton cancelButton, LoadSaveMode loadSaveMode, SaveGameHandler saveGameHandler) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadSaveMode.handleFile(saveGameHandler, list.getSelectedValue());
				LoadSaveDialog.this.dispose();
			}
		}
		);
		cancelButton.addActionListener(e -> LoadSaveDialog.this.dispose());
		
		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					okButton.setEnabled(true);
					okButton.doClick();
				}
			}
		});
		
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				okButton.setEnabled(true);
			}
		});
	}
}
