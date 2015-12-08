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
package org.worldgrower.gui.knowledge;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.gui.ActionContainingArgs;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.util.ButtonFactory;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JTableFactory;

public class ChooseKnowledgeDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable knowledgeTable;
	private JButton okButton;
	
	private ActionContainingArgs guiAction;

	public ChooseKnowledgeDialog(List<String> knowledgeDescriptions, ImageInfoReader imageInfoReader, List<ImageIds> imageIds, Component parent, ActionContainingArgs guiAction) {
		initializeGui(parent, knowledgeDescriptions, imageInfoReader, imageIds);
		
		this.guiAction = guiAction;
		
		handleActions();
	}

	private void initializeGui(Component parent, List<String> knowledgeDescriptions, ImageInfoReader imageInfoReader, List<ImageIds> imageIds) {
		setBounds(100, 100, 400, 502);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		IconUtils.setIcon(this);
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		knowledgeTable = JTableFactory.createJTable(new KnowledgeModel(knowledgeDescriptions, imageIds));
		JScrollPane scrollPane = new JScrollPane(knowledgeTable);
		scrollPane.setBounds(5, 5, 350, 450);
		contentPanel.add(scrollPane);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		okButton = ButtonFactory.createButton("OK");
		okButton.setActionCommand("OK");
		okButton.setEnabled(false);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		this.setLocationRelativeTo(parent);
	}
	
	private static class KnowledgeModel extends AbstractTableModel {

		private final List<String> knowledgeDescriptions;
		private final List<ImageIds> imageIds;
		private final List<Boolean> selected;
		
		public KnowledgeModel(List<String> knowledgeDescriptions, List<ImageIds> imageIds) {
			super();
			this.knowledgeDescriptions = knowledgeDescriptions;
			this.imageIds = imageIds;
			this.selected = new ArrayList<>();
			for(int i=0; i<knowledgeDescriptions.size(); i++) {
				this.selected.add(Boolean.FALSE);
			}
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public int getRowCount() {
			return knowledgeDescriptions.size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column == 0) {
				return selected.get(row);
			} else if (column == 1) {
				return imageIds.get(row);
			} else if (column == 2) {
				return knowledgeDescriptions.get(row);
			}
			return null;
		}

		public List<Boolean> getSelected() {
			return selected;
		}
	}

	public void showMe() {
		this.setVisible(true);
	}
	

	private void handleActions() {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				KnowledgeModel knowledgeModel = (KnowledgeModel) knowledgeTable.getModel();
				List<Boolean> selected = knowledgeModel.getSelected();
				
				int[] args = new int[selected.size()];
				for(int i=0; i<selected.size(); i++) {
					args[i] = selected.get(i) ? 1 : 0;
				}
				
				guiAction.setArgs(args);
				
				guiAction.actionPerformed(null);		
				ChooseKnowledgeDialog.this.dispose();
			}
		});
	}
}
