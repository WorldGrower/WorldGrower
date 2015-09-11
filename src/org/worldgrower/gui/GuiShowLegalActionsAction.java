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
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.DungeonMaster;
import org.worldgrower.Main;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.LegalActionsPropertyUtils;
import org.worldgrower.gui.util.IconUtils;

public class GuiShowLegalActionsAction extends AbstractAction {
	private final WorldObject playerCharacter;
	private final DungeonMaster dungeonMaster;
	private final World world;
	private final WorldPanel parent;
	
	public GuiShowLegalActionsAction(WorldObject playerCharacter, DungeonMaster dungeonMaster, World world, WorldPanel parent) {
		super();
		this.playerCharacter = playerCharacter;
		this.dungeonMaster = dungeonMaster;
		this.world = world;
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JDialog dialog = new JDialog();
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		IconUtils.setIcon(dialog);
		
		WorldModel worldModel = new WorldModel(world);
		JTable table = new JTable(worldModel);
		table.setBounds(50, 50, 300, 700);
		dialog.add(new JScrollPane(table));
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, worldModel, dialog);
		dialog.getRootPane().setDefaultButton(okButton);
		
		dialog.setSize(400, 800);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	private void addActionHandlers(JButton okButton, WorldModel worldModel, JDialog dialog) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] args = LegalActionsPropertyUtils.legalActionsToArgs(worldModel.getLegalActions());
				Main.executeAction(playerCharacter, Actions.SET_LEGAL_ACTIONS_ACTION, args, world, dungeonMaster, playerCharacter, parent);
				dialog.dispose();
			}
		});
		
	}

	private static class WorldModel extends AbstractTableModel {

		private Map<ManagedOperation, Boolean> legalActions;
		List<ManagedOperation> actions;
		
		public WorldModel(World world) {
			super();
			this.legalActions = LegalActionsPropertyUtils.getLegalActions(world);
			this.actions = LegalActionsPropertyUtils.getLegalActionsList(world);
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return legalActions.size();
		}
		
		@Override
		public Class<?> getColumnClass(int column) {
			if (column == 0) {
				return String.class;
			} else {
				return Boolean.class;
			}
		}
		
		@Override		
		public boolean isCellEditable(int row, int column) {
			if (column == 0) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public void setValueAt(Object value, int row, int column) {
			legalActions.put(actions.get(row), (Boolean)value);
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Action Name";
			} else if (columnIndex == 1) {
				return "Is Legal";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return actions.get(rowIndex).getSimpleDescription();
			} else if (columnIndex == 1) {
				return legalActions.get(actions.get(rowIndex));
			} else {
				return null;
			}
		}

		public Map<ManagedOperation, Boolean> getLegalActions() {
			return legalActions;
		}
	}
}