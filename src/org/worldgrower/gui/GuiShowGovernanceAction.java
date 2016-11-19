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

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.HousePropertyUtils;
import org.worldgrower.goal.LegalActionsPropertyUtils;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JCheckBoxFactory;
import org.worldgrower.gui.util.JComboBoxFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.JTableFactory;
import org.worldgrower.profession.Professions;

public class GuiShowGovernanceAction extends AbstractAction {
	
	private static final Integer[] PRICES = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	private static final Integer[] WAGES = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

	private static final Integer[] CANDIDATE_TURNS = new Integer[] {100, 200, 300, 400, 500};
	private static final Integer[] VOTING_TURNS = new Integer[] {100, 200, 300, 400, 500};
	
	private static final String LEGAL_ACTIONS_TOOLTIP = "When someone performs an illegal action, they are thrown out of the villagers group";
	private static final String VILLAGER_GOLD_TOOLTIP = "Villager gold is the stored income for the villager government in order to pay expenses";
	private static final String SHACK_TAX_RATE_TOOLTIP = "Shack Tax Rate is the amount of gold that a shack owner has to pay each " + GroupPropertyUtils.getTaxesPeriodDescription() + " turns";
	private static final String HOUSE_TAX_RATE_TOOLTIP = "House Tax Rate is the amount of gold that a house owner has to pay each " + GroupPropertyUtils.getTaxesPeriodDescription() + " turns";
	private static final String SHERIFF_WAGE_TOOLTIP = "Sheriff Wage is the amount of gold that a sheriff receives each " + GroupPropertyUtils.getTaxesPeriodDescription() + " turns";
	private static final String TAX_COLLECTOR_WAGE_TOOLTIP = "Tax Collector Wage is the amount of gold that a tax collector receives each " + GroupPropertyUtils.getTaxesPeriodDescription() + " turns";
	private static final String VOTING_STAGES_TOOLTIP = "Shows different stages of the voting process";
	private static final String CANDIDATE_STAGE_TOOLTIP = "Shows how many turns people can become a candidate for the election";
	private static final String VOTING_STAGE_TOOLTIP = "Shows how many turns people can vote for the election";
	
	
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
		GovernanceActionsDialog dialog = new GovernanceActionsDialog(1000, 800, imageInfoReader);
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
		incomePanel.setBounds(500, 55, 480, 135);
		dialog.addComponent(incomePanel);
		
		JLabel shackTaxRate = JLabelFactory.createJLabel("Shack Tax Rate:");
		shackTaxRate.setBounds(15, 20, 185, 30);
		shackTaxRate.setToolTipText(SHACK_TAX_RATE_TOOLTIP);
		incomePanel.add(shackTaxRate);
		
		JComboBox<Integer> shackComboBox = JComboBoxFactory.createJComboBox(PRICES, imageInfoReader);
		shackComboBox.setEnabled(performerIsLeaderOfVillagers);
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		shackComboBox.setSelectedItem(villagersOrganization.getProperty(Constants.SHACK_TAX_RATE));
		shackComboBox.setBounds(200, 20, 50, 30);
		shackComboBox.setForeground(Color.BLACK);
		shackComboBox.setToolTipText(SHACK_TAX_RATE_TOOLTIP);
		incomePanel.add(shackComboBox);
		
		int numberOfOwnedShacks = HousePropertyUtils.getOwnedBuildingCount(BuildingType.SHACK, world);
		JLabel numberOfShacksLabel = JLabelFactory.createJLabel(" x " + numberOfOwnedShacks + " shacks =");
		numberOfShacksLabel.setBounds(270, 20, 200, 30);
		numberOfShacksLabel.setToolTipText(SHACK_TAX_RATE_TOOLTIP);
		incomePanel.add(numberOfShacksLabel);
		
		JLabel shackIncome = JLabelFactory.createJLabel("0");
		shackIncome.setBounds(450, 20, 100, 30);
		shackIncome.setToolTipText(SHACK_TAX_RATE_TOOLTIP);
		incomePanel.add(shackIncome);
		
		addComboBoxListener(shackComboBox, numberOfOwnedShacks, shackIncome);
		
		JLabel houseTaxRate = JLabelFactory.createJLabel("House Tax Rate:");
		houseTaxRate.setBounds(15, 65, 185, 30);
		houseTaxRate.setToolTipText(HOUSE_TAX_RATE_TOOLTIP);
		incomePanel.add(houseTaxRate);
		
		JComboBox<Integer> houseComboBox = JComboBoxFactory.createJComboBox(PRICES, imageInfoReader);
		houseComboBox.setEnabled(performerIsLeaderOfVillagers);
		houseComboBox.setSelectedItem(villagersOrganization.getProperty(Constants.HOUSE_TAX_RATE));
		houseComboBox.setBounds(200, 65, 50, 30);
		houseComboBox.setForeground(Color.BLACK);
		houseComboBox.setToolTipText(HOUSE_TAX_RATE_TOOLTIP);
		incomePanel.add(houseComboBox);
		
		int numberOfOwnedHouses = HousePropertyUtils.getOwnedBuildingCount(BuildingType.HOUSE, world);
		JLabel numberOfHousesLabel = JLabelFactory.createJLabel(" x " + numberOfOwnedHouses + " houses =");
		numberOfHousesLabel.setBounds(270, 65, 200, 30);
		numberOfHousesLabel.setToolTipText(HOUSE_TAX_RATE_TOOLTIP);
		incomePanel.add(numberOfHousesLabel);
		
		JLabel houseIncome = JLabelFactory.createJLabel("0");
		houseIncome.setBounds(450, 65, 100, 30);
		houseIncome.setToolTipText(HOUSE_TAX_RATE_TOOLTIP);
		incomePanel.add(houseIncome);
		
		addComboBoxListener(houseComboBox, numberOfOwnedHouses, houseIncome);
		
		JPanel expensePanel = JPanelFactory.createJPanel("Expense");
		expensePanel.setLayout(null);
		expensePanel.setBounds(500, 200, 480, 135);
		dialog.addComponent(expensePanel);
		
		JLabel sheriffWage = JLabelFactory.createJLabel("Sheriff Wage:");
		sheriffWage.setBounds(15, 20, 185, 30);
		sheriffWage.setToolTipText(SHERIFF_WAGE_TOOLTIP);
		expensePanel.add(sheriffWage);
		
		JComboBox<Integer> sheriffComboBox = JComboBoxFactory.createJComboBox(WAGES, imageInfoReader);
		sheriffComboBox.setEnabled(performerIsLeaderOfVillagers);
		sheriffComboBox.setSelectedItem(villagersOrganization.getProperty(Constants.SHERIFF_WAGE));
		sheriffComboBox.setBounds(200, 20, 50, 30);
		sheriffComboBox.setForeground(Color.BLACK);
		sheriffComboBox.setToolTipText(SHERIFF_WAGE_TOOLTIP);
		expensePanel.add(sheriffComboBox);
		
		int numberOfSheriffs = Professions.getProfessionCount(world, Constants.CAN_ATTACK_CRIMINALS);
		JLabel numberOfSheriffsLabel = JLabelFactory.createJLabel(" x " + numberOfSheriffs + " sheriffs =");
		numberOfSheriffsLabel.setBounds(270, 20, 200, 30);
		numberOfSheriffsLabel.setToolTipText(SHERIFF_WAGE_TOOLTIP);
		expensePanel.add(numberOfSheriffsLabel);
		
		JLabel sheriffWageValue = JLabelFactory.createJLabel("0");
		sheriffWageValue.setBounds(450, 20, 100, 30);
		sheriffWageValue.setToolTipText(SHERIFF_WAGE_TOOLTIP);
		expensePanel.add(sheriffWageValue);
		
		addComboBoxListener(sheriffComboBox, numberOfSheriffs, sheriffWageValue);
		
		JLabel taxCollectorWage = JLabelFactory.createJLabel("Tax Collector Wage:");
		taxCollectorWage.setBounds(15, 65, 185, 30);
		taxCollectorWage.setToolTipText(TAX_COLLECTOR_WAGE_TOOLTIP);
		expensePanel.add(taxCollectorWage);
		
		JComboBox<Integer> taxCollectorComboBox = JComboBoxFactory.createJComboBox(PRICES, imageInfoReader);
		taxCollectorComboBox.setEnabled(performerIsLeaderOfVillagers);
		taxCollectorComboBox.setSelectedItem(villagersOrganization.getProperty(Constants.TAX_COLLECTOR_WAGE));
		taxCollectorComboBox.setBounds(200, 65, 50, 30);
		taxCollectorComboBox.setForeground(Color.BLACK);
		taxCollectorComboBox.setToolTipText(TAX_COLLECTOR_WAGE_TOOLTIP);
		expensePanel.add(taxCollectorComboBox);
		
		int numberOfTaxCollectors = Professions.getProfessionCount(world, Constants.CAN_COLLECT_TAXES);
		JLabel numberOfTaxCollectorsLabel = JLabelFactory.createJLabel(" x " + numberOfTaxCollectors + " tax collectors =");
		numberOfTaxCollectorsLabel.setBounds(270, 65, 200, 30);
		numberOfTaxCollectorsLabel.setToolTipText(TAX_COLLECTOR_WAGE_TOOLTIP);
		expensePanel.add(numberOfTaxCollectorsLabel);
		
		JLabel taxCollectorsWageValue = JLabelFactory.createJLabel("0");
		taxCollectorsWageValue.setBounds(450, 65, 100, 30);
		taxCollectorsWageValue.setToolTipText(TAX_COLLECTOR_WAGE_TOOLTIP);
		expensePanel.add(taxCollectorsWageValue);
		
		addComboBoxListener(taxCollectorComboBox, numberOfTaxCollectors, taxCollectorsWageValue);
		
		JPanel votingPanel = JPanelFactory.createJPanel("Voting");
		votingPanel.setLayout(null);
		votingPanel.setBounds(500, 345, 480, 390);
		dialog.addComponent(votingPanel);
		
		JCheckBox ownerShackHouseCheckBox = JCheckBoxFactory.createJCheckBox("Only owners of shacks/houses can vote");
		ownerShackHouseCheckBox.setOpaque(false);
		ownerShackHouseCheckBox.setSelected(villagersOrganization.getProperty(Constants.ONLY_OWNERS_CAN_VOTE));
		ownerShackHouseCheckBox.setEnabled(performerIsLeaderOfVillagers);
		ownerShackHouseCheckBox.setBounds(15, 25, 350, 30);
		ownerShackHouseCheckBox.setToolTipText("Only owners of shacks/houses can vote or become candidates in villager elections");
		votingPanel.add(ownerShackHouseCheckBox);
		
		JCheckBox maleCheckBox = JCheckBoxFactory.createJCheckBox("Only males can vote");
		maleCheckBox.setOpaque(false);
		maleCheckBox.setSelected(villagersOrganization.getProperty(Constants.ONLY_MALES_CAN_VOTE));
		maleCheckBox.setEnabled(performerIsLeaderOfVillagers);
		maleCheckBox.setBounds(15, 65, 350, 30);
		maleCheckBox.setToolTipText("Only males can vote or become candidates in villager elections");
		votingPanel.add(maleCheckBox);
		
		JCheckBox femaleCheckBox = JCheckBoxFactory.createJCheckBox("Only females can vote");
		femaleCheckBox.setOpaque(false);
		femaleCheckBox.setSelected(villagersOrganization.getProperty(Constants.ONLY_FEMALES_CAN_VOTE));
		femaleCheckBox.setEnabled(performerIsLeaderOfVillagers);
		femaleCheckBox.setBounds(15, 105, 350, 30);
		femaleCheckBox.setToolTipText("Only females can vote or become candidates in villager elections");
		votingPanel.add(femaleCheckBox);
		
		addExclusiveSelectedState(maleCheckBox, femaleCheckBox);
		
		JCheckBox undeadCheckBox = JCheckBoxFactory.createJCheckBox("Only undead can vote");
		undeadCheckBox.setOpaque(false);
		undeadCheckBox.setSelected(villagersOrganization.getProperty(Constants.ONLY_UNDEAD_CAN_VOTE));
		undeadCheckBox.setEnabled(performerIsLeaderOfVillagers);
		undeadCheckBox.setBounds(15, 145, 350, 30);
		undeadCheckBox.setToolTipText("Only undead can vote or become candidates in villager elections");
		votingPanel.add(undeadCheckBox);
		
		JLabel votingStagesLabel = JLabelFactory.createJLabel("Voting Stages:");
		votingStagesLabel.setBounds(15, 210, 185, 30);
		votingStagesLabel.setToolTipText(VOTING_STAGES_TOOLTIP);
		votingPanel.add(votingStagesLabel);
		
		JLabel candidateStageLabel = JLabelFactory.createJLabel("Number of turns people can become candidates:");
		candidateStageLabel.setBounds(15, 245, 385, 30);
		candidateStageLabel.setToolTipText(CANDIDATE_STAGE_TOOLTIP);
		votingPanel.add(candidateStageLabel);
		
		JComboBox<Integer> candidateStageComboBox = JComboBoxFactory.createJComboBox(CANDIDATE_TURNS, imageInfoReader);
		candidateStageComboBox.setEnabled(performerIsLeaderOfVillagers);
		int votingCandidacyTurns = villagersOrganization.getProperty(Constants.VOTING_CANDIDATE_TURNS);
		candidateStageComboBox.setSelectedItem(votingCandidacyTurns);
		candidateStageComboBox.setBounds(380, 245, 60, 30);
		candidateStageComboBox.setForeground(Color.BLACK);
		candidateStageComboBox.setToolTipText(CANDIDATE_STAGE_TOOLTIP);
		votingPanel.add(candidateStageComboBox);
		
		JLabel votingStageLabel = JLabelFactory.createJLabel("Number of turns people can vote:");
		votingStageLabel.setBounds(15, 280, 385, 30);
		votingStageLabel.setToolTipText(VOTING_STAGE_TOOLTIP);
		votingPanel.add(votingStageLabel);
		
		JComboBox<Integer> votingStageComboBox = JComboBoxFactory.createJComboBox(VOTING_TURNS, imageInfoReader);
		votingStageComboBox.setEnabled(performerIsLeaderOfVillagers);
		int votingStageTurns = villagersOrganization.getProperty(Constants.VOTING_TOTAL_TURNS) - votingCandidacyTurns;
		votingStageComboBox.setSelectedItem(votingStageTurns);
		votingStageComboBox.setBounds(380, 280, 60, 30);
		votingStageComboBox.setForeground(Color.BLACK);
		votingStageComboBox.setToolTipText(VOTING_STAGE_TOOLTIP);
		votingPanel.add(votingStageComboBox);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 745, 988, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.addComponent(buttonPane);
		
		JButton okButton = JButtonFactory.createButton("OK", imageInfoReader, soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, worldModel, shackComboBox, houseComboBox, sheriffComboBox, taxCollectorComboBox, dialog, performerIsLeaderOfVillagers, ownerShackHouseCheckBox, maleCheckBox, femaleCheckBox, undeadCheckBox, candidateStageComboBox, votingStageComboBox);
		dialog.getRootPane().setDefaultButton(okButton);
		SwingUtils.installEscapeCloseOperation(dialog);
		
		SwingUtils.makeTransparant(legalActionsTable, scrollPane);
		
		dialog.setLocationRelativeTo(null);
		DialogUtils.createDialogBackPanel(dialog, parentFrame.getContentPane());
		dialog.setVisible(true);
	}

	private void addComboBoxListener(JComboBox<Integer> comboBox, int numberOfOwnedBuildings, JLabel buildingIncome) {
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				buildingIncome.setText(Integer.toString(numberOfOwnedBuildings * (Integer)comboBox.getSelectedItem()));				
			}
		});
		buildingIncome.setText(Integer.toString(numberOfOwnedBuildings * (Integer)comboBox.getSelectedItem()));
	}

	private void addExclusiveSelectedState(JCheckBox maleCheckBox, JCheckBox femaleCheckBox) {
		maleCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (maleCheckBox.isSelected()) {
					femaleCheckBox.setSelected(false);
				}
			}
		});
		
		femaleCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (femaleCheckBox.isSelected()) {
					maleCheckBox.setSelected(false);
				}
			}
		});
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
	
	private void addActionHandlers(JButton okButton, WorldModel worldModel, JComboBox<Integer> shackComboBox, JComboBox<Integer> houseComboBox, JComboBox<Integer> sheriffComboBox, JComboBox<Integer> taxCollectorComboBox, JDialog dialog, boolean performerIsLeaderOfVillagers, JCheckBox ownerShackHouseCheckBox, JCheckBox maleCheckBox, JCheckBox femaleCheckBox, JCheckBox undeadCheckBox, JComboBox<Integer> candidateStageComboBox, JComboBox<Integer> votingStageComboBox) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (performerIsLeaderOfVillagers) {
					int shackTaxRate = (int) shackComboBox.getSelectedItem();
					int houseTaxRate = (int) houseComboBox.getSelectedItem();
					int sheriffWage = (int) sheriffComboBox.getSelectedItem();
					int taxCollectorWage = (int) taxCollectorComboBox.getSelectedItem();
					boolean onlyOwnerShackHouseCanVote = ownerShackHouseCheckBox.isSelected();
					boolean onlyMalesCanVote = maleCheckBox.isSelected();
					boolean onlyFemalesCanVote = femaleCheckBox.isSelected();
					boolean onlyUndeadCanVote = undeadCheckBox.isSelected();
					
					int candidateStageValue = (int) candidateStageComboBox.getSelectedItem();
					int votingStageValue = (int) votingStageComboBox.getSelectedItem();
					int endVotingInTurns = candidateStageValue + votingStageValue;
					
					int[] args = worldModel.getArgs(shackTaxRate, houseTaxRate, sheriffWage, taxCollectorWage, onlyOwnerShackHouseCanVote, onlyMalesCanVote, onlyFemalesCanVote, onlyUndeadCanVote, candidateStageValue, endVotingInTurns);
					Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, Actions.SET_GOVERNANCE_ACTION, args, world, dungeonMaster, playerCharacter, parent, imageInfoReader, soundIdReader);
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

		public int[] getArgs(int shackTaxRate, int houseTaxRate, int sheriffWage, int taxCollectorWage, boolean onlyOwnerShackHouseCanVote, boolean onlyMalesCanVote, boolean onlyFemalesCanVote, boolean onlyUndeadCanVote, int candidateStageValue, int endVotingInTurns) {
			return LegalActions.createGovernanceArgs(legalFlags, shackTaxRate, houseTaxRate, sheriffWage, taxCollectorWage, onlyOwnerShackHouseCanVote, onlyMalesCanVote, onlyFemalesCanVote, onlyUndeadCanVote, candidateStageValue, endVotingInTurns);
		}
	}
	
	private static class GovernanceActionsDialog extends AbstractDialog {

		public GovernanceActionsDialog(int width, int height, ImageInfoReader imageInfoReader) {
			super(width, height, imageInfoReader);
		}
		
	}
}