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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JTableFactory;

public class MagicOverviewDialog extends JDialog {

	private final JPanel contentPanel = new GradientPanel();

	public MagicOverviewDialog(WorldObject playerCharacter, SoundIdReader soundIdReader) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		setSize(650, 650);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		IconUtils.setIcon(this);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 608, 517);
		contentPanel.add(scrollPane);
		
		JTable magicSpellsTable = new MagicSpellsTable(new MagicSpellTableModel(playerCharacter));
		magicSpellsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		magicSpellsTable.setAutoCreateRowSorter(true);
		
		magicSpellsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		magicSpellsTable.getColumnModel().getColumn(0).setPreferredWidth(257);
		magicSpellsTable.getColumnModel().getColumn(1).setPreferredWidth(110);
		magicSpellsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
		magicSpellsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		
		
		scrollPane.setViewportView(magicSpellsTable);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.setOpaque(false);
			buttonPane.setBounds(12, 535, 608, 75);
			contentPanel.add(buttonPane);
			{
				JButton okButton = JButtonFactory.createButton("OK", soundIdReader);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				okButton.addActionListener(new CloseDialogAction());
				SwingUtils.installEscapeCloseOperation(this);
			}
		}
		SwingUtils.makeTransparant(magicSpellsTable, scrollPane);
	}
	
	
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MagicOverviewDialog.this.dispose();
		}
	}
	
	private static class MagicSpellTableModel extends AbstractTableModel {

		private final WorldObject playerCharacter;
		
		public MagicSpellTableModel(WorldObject playerCharacter) {
			super();
			this.playerCharacter = playerCharacter;
		}

		@Override
		public String getColumnName(int column) {
			if (column == 0) {
				return "Spell Name";
			} else if (column == 1) {
				return "School";
			} else if (column == 2) {
				return "Required Skill Level";
			} else if (column == 3) {
				return "Progress";
			} else {
				return null;
			}
		}
		
		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public int getRowCount() {
			return Actions.getMagicSpells().size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			MagicSpell magicSpell = Actions.getMagicSpells().get(row);
			if (column == 0) {
				return magicSpell.getSimpleDescription();
			} else if (column == 1) {
				return magicSpell.getSkill().getName();
			} else if (column == 2) {
				return magicSpell.getRequiredSkillLevel();
			} else if (column == 3) {
				List<ManagedOperation> knownSpells = playerCharacter.getProperty(Constants.KNOWN_SPELLS);
				PropertyCountMap<MagicSpell> studyingSpells = playerCharacter.getProperty(Constants.STUDYING_SPELLS);
				if (knownSpells.contains(magicSpell)) {
					return "Known";
				} else if (studyingSpells.count(magicSpell) > 0) {
					int percentage = (studyingSpells.count(magicSpell) * 100) / magicSpell.getResearchCost();
					return percentage + "%";
				} else {
					return "Unknown";
				}
			} else {
				return null;
			}
		}
	}
	
	private static class MagicSpellsTable extends JTable {
		
		public MagicSpellsTable(TableModel tableModel) {
			super(tableModel);
			JTableFactory.setTableProperties(this);
		}

		@Override
		public String getToolTipText(MouseEvent mouseEvent) {
			int row = convertRowIndexToModel(rowAtPoint(mouseEvent.getPoint()));
			MagicSpell magicSpell = Actions.getMagicSpells().get(row);
			return magicSpell.getDescription();
		}
		
	}
}
