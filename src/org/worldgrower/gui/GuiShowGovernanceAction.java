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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.LegalActionsPropertyUtils;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JComboBoxFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.JTableFactory;

public class GuiShowGovernanceAction extends AbstractAction {
	
	private static final Integer[] PRICES = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	private static final Integer[] WAGES = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	
	private final WorldObject playerCharacter;
	private final DungeonMaster dungeonMaster;
	private final World world;
	private final WorldPanel parent;
	private final SoundIdReader soundIdReader;
	private final JFrame parentFrame;
	
	public GuiShowGovernanceAction(WorldObject playerCharacter, DungeonMaster dungeonMaster, World world, WorldPanel parent, SoundIdReader soundIdReader, JFrame parentFrame) {
		super();
		this.playerCharacter = playerCharacter;
		this.dungeonMaster = dungeonMaster;
		this.world = world;
		this.parent = parent;
		this.soundIdReader = soundIdReader;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GovernanceActionsDialog dialog = new GovernanceActionsDialog(800, 800);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		IconUtils.setIcon(dialog);
		
		boolean performerIsLeaderOfVillagers = GroupPropertyUtils.performerIsLeaderOfVillagers(playerCharacter, world);
		
		JPanel legalActionsPanel = JPanelFactory.createJPanel("Legal actions");
		legalActionsPanel.setLayout(null);
		legalActionsPanel.setBounds(15, 15, 368, 720);
		dialog.addComponent(legalActionsPanel);
		
		WorldModel worldModel = new WorldModel(playerCharacter, world, performerIsLeaderOfVillagers);
		JTable legalActionsTable = JTableFactory.createJTable(worldModel);
		legalActionsTable.getColumnModel().getColumn(0).setPreferredWidth(230);
		legalActionsTable.getColumnModel().getColumn(1).setPreferredWidth(108);
		JScrollPane scrollPane = new JScrollPane(legalActionsTable);
		scrollPane.setBounds(15, 25, 338, 680);
		legalActionsPanel.add(scrollPane);
		
		JPanel incomePanel = JPanelFactory.createJPanel("Income");
		incomePanel.setLayout(null);
		incomePanel.setBounds(400, 15, 380, 360);
		dialog.addComponent(incomePanel);
		
		JLabel shackTaxRate = JLabelFactory.createJLabel("Shack Tax Rate:");
		shackTaxRate.setBounds(15, 20, 200, 30);
		incomePanel.add(shackTaxRate);
		
		JComboBox<Integer> shackComboBox = JComboBoxFactory.createJComboBox(PRICES);
		shackComboBox.setEnabled(performerIsLeaderOfVillagers);
		shackComboBox.setSelectedItem(GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.SHACK_TAX_RATE));
		shackComboBox.setBounds(215, 20, 50, 30);
		incomePanel.add(shackComboBox);
		
		JLabel houseTaxRate = JLabelFactory.createJLabel("House Tax Rate:");
		houseTaxRate.setBounds(15, 65, 200, 30);
		incomePanel.add(houseTaxRate);
		
		JComboBox<Integer> houseComboBox = JComboBoxFactory.createJComboBox(PRICES);
		houseComboBox.setEnabled(performerIsLeaderOfVillagers);
		houseComboBox.setSelectedItem(GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.HOUSE_TAX_RATE));
		houseComboBox.setBounds(215, 65, 50, 30);
		incomePanel.add(houseComboBox);
		
		JPanel expensePanel = JPanelFactory.createJPanel("Expense");
		expensePanel.setLayout(null);
		expensePanel.setBounds(400, 375, 380, 360);
		dialog.addComponent(expensePanel);
		
		JLabel sheriffWage = JLabelFactory.createJLabel("Sheriff Wage:");
		sheriffWage.setBounds(15, 20, 200, 30);
		expensePanel.add(sheriffWage);
		
		JComboBox<Integer> sheriffComboBox = JComboBoxFactory.createJComboBox(WAGES);
		sheriffComboBox.setEnabled(performerIsLeaderOfVillagers);
		sheriffComboBox.setBounds(215, 20, 50, 30);
		expensePanel.add(sheriffComboBox);
		
		JLabel taxCollectorWage = JLabelFactory.createJLabel("Tax Collector Wage:");
		taxCollectorWage.setBounds(15, 65, 200, 30);
		expensePanel.add(taxCollectorWage);
		
		JComboBox<Integer> taxCollectorComboBox = JComboBoxFactory.createJComboBox(PRICES);
		taxCollectorComboBox.setEnabled(performerIsLeaderOfVillagers);
		taxCollectorComboBox.setBounds(215, 65, 50, 30);
		expensePanel.add(taxCollectorComboBox);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 745, 788, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.addComponent(buttonPane);
		
		JButton okButton = JButtonFactory.createButton("OK", soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, worldModel, shackComboBox, houseComboBox, dialog, performerIsLeaderOfVillagers);
		dialog.getRootPane().setDefaultButton(okButton);
		SwingUtils.installEscapeCloseOperation(dialog);
		
		SwingUtils.makeTransparant(legalActionsTable, scrollPane);
		
		dialog.setLocationRelativeTo(null);
		DialogUtils.createDialogBackPanel(dialog, parentFrame.getContentPane());
		dialog.setVisible(true);
	}
	
	private void addActionHandlers(JButton okButton, WorldModel worldModel, JComboBox<Integer> shackComboBox, JComboBox<Integer> houseComboBox, JDialog dialog, boolean performerIsLeaderOfVillagers) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (performerIsLeaderOfVillagers) {
					int shackTaxRate = (int) shackComboBox.getSelectedItem();
					int houseTaxRate = (int) houseComboBox.getSelectedItem();
					int[] args = worldModel.getArgs(shackTaxRate, houseTaxRate);
					Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, Actions.SET_GOVERNANCE_ACTION, args, world, dungeonMaster, playerCharacter, parent, soundIdReader);
				}
				dialog.dispose();
			}
		});
	}

	private static class WorldModel extends AbstractTableModel {
		private List<LegalAction> legalActionsList;
		private Map<LegalAction, Boolean> legalFlags = new HashMap<>();
		private boolean performerIsLeaderOfVillagers;
		
		public WorldModel(WorldObject playerCharacter, World world, boolean performerIsLeaderOfVillagers) {
			super();
			LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
			this.legalActionsList = legalActions.toList();
			this.legalFlags = legalActions.getLegalActions();
			this.performerIsLeaderOfVillagers = performerIsLeaderOfVillagers;
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

		public int[] getArgs(int shackTaxRate, int houseTaxRate) {
			return LegalActions.createGovernanceArgs(legalFlags, shackTaxRate, houseTaxRate);
		}
	}
	
	private static class GovernanceActionsDialog extends AbstractDialog {

		public GovernanceActionsDialog(int width, int height) {
			super(width, height);
		}
		
	}
}