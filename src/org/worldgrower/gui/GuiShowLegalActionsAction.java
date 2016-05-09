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

import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.LegalActionsPropertyUtils;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.util.ButtonFactory;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JTableFactory;

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
		LegalActionsDialog dialog = new LegalActionsDialog(400, 800);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		IconUtils.setIcon(dialog);
		
		WorldModel worldModel = new WorldModel(playerCharacter, world);
		JTable table = JTableFactory.createJTable(worldModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(15, 15, 368, 700);
		dialog.addComponent(scrollPane);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 720, 378, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.addComponent(buttonPane);
		
		JButton okButton = ButtonFactory.createButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, worldModel, dialog);
		dialog.getRootPane().setDefaultButton(okButton);
		SwingUtils.installEscapeCloseOperation(dialog);
		
		SwingUtils.makeTransparant(table, scrollPane);
		
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	private void addActionHandlers(JButton okButton, WorldModel worldModel, JDialog dialog) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] args = worldModel.getArgs();
				Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, Actions.SET_LEGAL_ACTIONS_ACTION, args, world, dungeonMaster, playerCharacter, parent);
				dialog.dispose();
			}
		});
	}

	private static class WorldModel extends AbstractTableModel {
		private List<LegalAction> legalActionsList;
		private Map<LegalAction, Boolean> legalFlags = new HashMap<>();
		private boolean performerIsLeaderOfVillagers;
		
		public WorldModel(WorldObject playerCharacter, World world) {
			super();
			LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
			this.legalActionsList = legalActions.toList();
			this.legalFlags = legalActions.getLegalActions();
			this.performerIsLeaderOfVillagers = GroupPropertyUtils.performerIsLeaderOfVillagers(playerCharacter, world);
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return legalActionsList.size();
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
				return performerIsLeaderOfVillagers;
			}
		}
		
		@Override
		public void setValueAt(Object value, int row, int column) {
			legalFlags.put(legalActionsList.get(row), (Boolean)value);
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
				return legalActionsList.get(rowIndex).getDescription();
			} else if (columnIndex == 1) {
				return legalFlags.get(legalActionsList.get(rowIndex));
			} else {
				return null;
			}
		}

		public int[] getArgs() {
			return LegalActions.legalFlagsToArgs(legalFlags);
		}
	}
	
	private static class LegalActionsDialog extends AbstractDialog {

		public LegalActionsDialog(int width, int height) {
			super(width, height);
		}
		
	}
}