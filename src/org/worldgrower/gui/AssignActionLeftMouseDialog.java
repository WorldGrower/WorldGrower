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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JListFactory;
import org.worldgrower.gui.util.JScrollPaneFactory;

public class AssignActionLeftMouseDialog extends AbstractDialog {

	private String selectedAction = null;

	public AssignActionLeftMouseDialog(String[] actionDescriptions, ImageIds[] imageIds, SoundIdReader soundIdReader, JFrame parentFrame, ImageInfoReader imageInfoReader) {
		super(450, 475, imageInfoReader);
		
		JScrollPane scrollPane = JScrollPaneFactory.createScrollPane();
		scrollPane.setBounds(32, 32, 390, 292);
		addComponent(scrollPane);
		
		JList<String> list = JListFactory.createJList(actionDescriptions);
		list.setSelectedIndex(0);
		list.setCellRenderer(new ImageListRenderer<>(imageIds, imageInfoReader));
		
		this.addWindowFocusListener(new WindowAdapter() {
			@Override
		    public void windowGainedFocus(WindowEvent e) {
		        list.requestFocusInWindow();
		    }
		});

		scrollPane.setViewportView(list);
		
		JLabel lblNewLabel = JLabelFactory.createJLabel("Ctrl-left mouse click to talk with a person");
		lblNewLabel.setBounds(34, 343, 360, 24);
		addComponent(lblNewLabel);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setOpaque(false);
		buttonPane.setBounds(34, 423, 410, 50);
		addComponent(buttonPane);

		JButton okButton = JButtonFactory.createButton("OK", imageInfoReader, soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = JButtonFactory.createButton("Cancel", imageInfoReader, soundIdReader);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		addActions(list, okButton, cancelButton);
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}
	
	public String showMe() {
		setVisible(true);
		return selectedAction;
	}

	private void addActions(JList<String> list, JButton okButton, JButton cancelButton) {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedAction = list.getSelectedValue();
				AssignActionLeftMouseDialog.this.dispose();
				
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AssignActionLeftMouseDialog.this.dispose();
			}
		});
	}
}
