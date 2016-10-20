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
	private static final Integer[] WAGES = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	
	private static final String LEGAL_ACTIONS_TOOLTIP = "When someone performs an illegal action, they are thrown out of the villagers group";
	private static final String VILLAGER_GOLD_TOOLTIP = "Villager gold is the stored income for the villager government in order to pay expenses";
	private static final String SHACK_TAX_RATE_TOOLTIP = "Shack Tax Rate is the amount of gold that a shack owner has to pay each " + GroupPropertyUtils.getTaxesPeriodDescription() + " turns";
	private static final String HOUSE_TAX_RATE_TOOLTIP = "House Tax Rate is the amount of gold that a house owner has to pay each " + GroupPropertyUtils.getTaxesPeriodDescription() + " turns";
	private static final String SHERIFF_WAGE_TOOLTIP = "Sheriff Wage is the amount of gold that a sheriff receives each " + GroupPropertyUtils.getTaxesPeriodDescription() + " turns";
	private static final String TAX_COLLECTOR_WAGE_TOOLTIP = "Tax Collector Wage is the amount of gold that a tax collector receives each " + GroupPropertyUtils.getTaxesPeriodDescription() + " turns";
	
	private final WorldObject playerCharacter;
	private final DungeonMaster dungeonMaster;
	private final World world;
	private final WorldPanel parent;
	private final SoundIdReader soundIdReader;
	private final JFrame parentFrame;
	private final ImageInfoReader imageInfoReader;
	
	public GuiShowGovernanceAction(WorldObject playerCharacter, DungeonMaster dungeonMaster, World world, WorldPanel parent, SoundIdReader soundIdReader, JFrame parentFrame, ImageInfoReader imageInfoReader) {
		super();
		this.playerCharacter = playerCharacter;
		this.dungeonMaster = dungeonMaster;
		this.world = world;
		this.parent = parent;
		this.soundIdReader = soundIdReader;
		this.parentFrame = parentFrame;
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GovernanceActionsDialog dialog = new GovernanceActionsDialog(900, 800);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		IconUtils.setIcon(dialog);
		
		boolean performerIsLeaderOfVillagers = GroupPropertyUtils.performerIsLeaderOfVillagers(playerCharacter, world);
		
		JPanel legalActionsPanel = JPanelFactory.createJPanel("Legal actions");
		legalActionsPanel.setLayout(null);
		legalActionsPanel.setBounds(15, 15, 468, 720);
		dialog.addComponent(legalActionsPanel);
		
		WorldModel worldModel = new WorldModel(playerCharacter, world, performerIsLeaderOfVillagers);
		JTable legalActionsTable = JTableFactory.createJTable(worldModel);
		legalActionsTable.setDefaultRenderer(ImageIds.class, new ImageTableRenderer(imageInfoReader));
		legalActionsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		legalActionsTable.getColumnModel().getColumn(1).setPreferredWidth(250);
		legalActionsTable.getColumnModel().getColumn(2).setPreferredWidth(108);
		legalActionsTable.setRowHeight(50);
		legalActionsTable.setToolTipText(LEGAL_ACTIONS_TOOLTIP);
		JScrollPane scrollPane = new JScrollPane(legalActionsTable);
		scrollPane.setBounds(15, 25, 438, 680);
		legalActionsPanel.add(scrollPane);
		
		JLabel villagerGoldLabel = JLabelFactory.createJLabel("Villager Gold:");
		villagerGoldLabel.setBounds(500, 15, 200, 30);
		villagerGoldLabel.setToolTipText(VILLAGER_GOLD_TOOLTIP);
		dialog.addComponent(villagerGoldLabel);
		
		String villagerGold = getVillagerGold();
		JLabel villagerGoldValue = JLabelFactory.createJLabel(villagerGold);
		villagerGoldValue.setBounds(700, 15, 200, 30);
		villagerGoldValue.setToolTipText(VILLAGER_GOLD_TOOLTIP);
		dialog.addComponent(villagerGoldValue);
		
		JPanel incomePanel = JPanelFactory.createJPanel("Income");
		incomePanel.setLayout(null);
		incomePanel.setBounds(500, 65, 380, 335);
		dialog.addComponent(incomePanel);
		
		JLabel shackTaxRate = JLabelFactory.createJLabel("Shack Tax Rate:");
		shackTaxRate.setBounds(15, 20, 200, 30);
		shackTaxRate.setToolTipText(SHACK_TAX_RATE_TOOLTIP);
		incomePanel.add(shackTaxRate);
		
		JComboBox<Integer> shackComboBox = JComboBoxFactory.createJComboBox(PRICES);
		shackComboBox.setEnabled(performerIsLeaderOfVillagers);
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		shackComboBox.setSelectedItem(villagersOrganization.getProperty(Constants.SHACK_TAX_RATE));
		shackComboBox.setBounds(215, 20, 50, 30);
		shackComboBox.setToolTipText(SHACK_TAX_RATE_TOOLTIP);
		incomePanel.add(shackComboBox);
		
		JLabel houseTaxRate = JLabelFactory.createJLabel("House Tax Rate:");
		houseTaxRate.setBounds(15, 65, 200, 30);
		houseTaxRate.setToolTipText(HOUSE_TAX_RATE_TOOLTIP);
		incomePanel.add(houseTaxRate);
		
		JComboBox<Integer> houseComboBox = JComboBoxFactory.createJComboBox(PRICES);
		houseComboBox.setEnabled(performerIsLeaderOfVillagers);
		houseComboBox.setSelectedItem(villagersOrganization.getProperty(Constants.HOUSE_TAX_RATE));
		houseComboBox.setBounds(215, 65, 50, 30);
		houseComboBox.setToolTipText(HOUSE_TAX_RATE_TOOLTIP);
		incomePanel.add(houseComboBox);
		
		JPanel expensePanel = JPanelFactory.createJPanel("Expense");
		expensePanel.setLayout(null);
		expensePanel.setBounds(500, 400, 380, 335);
		dialog.addComponent(expensePanel);
		
		JLabel sheriffWage = JLabelFactory.createJLabel("Sheriff Wage:");
		sheriffWage.setBounds(15, 20, 200, 30);
		sheriffWage.setToolTipText(SHERIFF_WAGE_TOOLTIP);
		expensePanel.add(sheriffWage);
		
		JComboBox<Integer> sheriffComboBox = JComboBoxFactory.createJComboBox(WAGES);
		sheriffComboBox.setEnabled(performerIsLeaderOfVillagers);
		sheriffComboBox.setSelectedItem(villagersOrganization.getProperty(Constants.SHERIFF_WAGE));
		sheriffComboBox.setBounds(215, 20, 50, 30);
		sheriffComboBox.setToolTipText(SHERIFF_WAGE_TOOLTIP);
		expensePanel.add(sheriffComboBox);
		
		JLabel taxCollectorWage = JLabelFactory.createJLabel("Tax Collector Wage:");
		taxCollectorWage.setBounds(15, 65, 200, 30);
		taxCollectorWage.setToolTipText(TAX_COLLECTOR_WAGE_TOOLTIP);
		expensePanel.add(taxCollectorWage);
		
		JComboBox<Integer> taxCollectorComboBox = JComboBoxFactory.createJComboBox(PRICES);
		taxCollectorComboBox.setEnabled(performerIsLeaderOfVillagers);
		taxCollectorComboBox.setSelectedItem(villagersOrganization.getProperty(Constants.TAX_COLLECTOR_WAGE));
		taxCollectorComboBox.setBounds(215, 65, 50, 30);
		taxCollectorComboBox.setToolTipText(TAX_COLLECTOR_WAGE_TOOLTIP);
		expensePanel.add(taxCollectorComboBox);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 745, 888, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.addComponent(buttonPane);
		
		JButton okButton = JButtonFactory.createButton("OK", soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, worldModel, shackComboBox, houseComboBox, sheriffComboBox, taxCollectorComboBox, dialog, performerIsLeaderOfVillagers);
		dialog.getRootPane().setDefaultButton(okButton);
		SwingUtils.installEscapeCloseOperation(dialog);
		
		SwingUtils.makeTransparant(legalActionsTable, scrollPane);
		
		dialog.setLocationRelativeTo(null);
		DialogUtils.createDialogBackPanel(dialog, parentFrame.getContentPane());
		dialog.setVisible(true);
	}

	String getVillagerGold() {
		WorldObject leader = GroupPropertyUtils.getLeaderOfVillagers(world);
		if (leader != null) {
			Integer organizationGold = leader.getProperty(Constants.ORGANIZATION_GOLD);
			if (organizationGold != null) {
				return Integer.toString(organizationGold);
			}
		}
		return "n/a";
	}
	
	private void addActionHandlers(JButton okButton, WorldModel worldModel, JComboBox<Integer> shackComboBox, JComboBox<Integer> houseComboBox, JComboBox<Integer> sheriffComboBox, JComboBox<Integer> taxCollectorComboBox, JDialog dialog, boolean performerIsLeaderOfVillagers) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (performerIsLeaderOfVillagers) {
					int shackTaxRate = (int) shackComboBox.getSelectedItem();
					int houseTaxRate = (int) houseComboBox.getSelectedItem();
					int sheriffWage = (int) sheriffComboBox.getSelectedItem();
					int taxCollectorWage = (int) taxCollectorComboBox.getSelectedItem();
					int[] args = worldModel.getArgs(shackTaxRate, houseTaxRate, sheriffWage, taxCollectorWage);
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
			return 3;
		}

		@Override
		public int getRowCount() {
			return legalActionsList.size();
		}
		
		@Override
		public Class<?> getColumnClass(int column) {
			if (column == 0) {
				return ImageIds.class;
			} else if (column == 1) {
				return String.class;
			} else {
				return Boolean.class;
			}
		}
		
		@Override		
		public boolean isCellEditable(int row, int column) {
			if (column == 0 || column == 1) {
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
				return "Image";
			} else if (columnIndex == 1) {
				return "Action Name";
			} else if (columnIndex == 2) {
				return "Is Legal";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return legalActionsList.get(rowIndex).getImageId();
			} else if (columnIndex == 1) {
				return legalActionsList.get(rowIndex).getDescription();
			} else if (columnIndex == 2) {
				return legalFlags.get(legalActionsList.get(rowIndex));
			} else {
				return null;
			}
		}

		public int[] getArgs(int shackTaxRate, int houseTaxRate, int sheriffWage, int taxCollectorWage) {
			return LegalActions.createGovernanceArgs(legalFlags, shackTaxRate, houseTaxRate, sheriffWage, taxCollectorWage);
		}
	}
	
	private static class GovernanceActionsDialog extends AbstractDialog {

		public GovernanceActionsDialog(int width, int height) {
			super(width, height);
		}
		
	}
}