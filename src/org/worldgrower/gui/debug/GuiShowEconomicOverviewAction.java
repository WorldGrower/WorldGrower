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
package org.worldgrower.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.World;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.OperationStatistics;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.Item;
import org.worldgrower.profession.Professions;

public class GuiShowEconomicOverviewAction extends AbstractAction {
	private World world;
	
	public GuiShowEconomicOverviewAction(World world) {
		super();
		this.world = world;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		
		JTable table = new JTable(new WorldModel(world));
		table.setBounds(50, 50, 200, 700);
		frame.add(new JScrollPane(table));
		
		Timer timer = new Timer(500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				table.repaint();
			}
			
		});
		
		timer.start();
		
		frame.setBounds(100,  100, 300, 800);
		frame.setVisible(true);
	}
	
	private static class WorldModel extends AbstractTableModel {
		private static final int ROW_OFFSET = 20;
		private World world;
		
		public WorldModel(World world) {
			super();
			this.world = world;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return ROW_OFFSET + Item.values().length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Name";
			} else if (columnIndex == 1) {
				return "Count";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				if (rowIndex == 0) {
					return "CutWoodAction";
				} else if (rowIndex == 1) {
					return "CutWoodAction by non-professionals";
				} else if (rowIndex == 2) {
					return "HarvestFood";
				} else if (rowIndex == 3) {
					return "HarvestFood by non-professionals";
				} else if (rowIndex == 4) {
					return "ButcherMeat";
				} else if (rowIndex == 5) {
					return "ButcherMeat by non-professionals";
				} else if (rowIndex == 6) {
					return "EatFood";
				} else if (rowIndex == 7) {
					return "MineStone";
				} else if (rowIndex == 8) {
					return "MineStone by non-professionals";
				} else if (rowIndex == 9) {
					return "MineOre";
				} else if (rowIndex == 10) {
					return "MineOre by non-professionals";
				} else if (rowIndex == 11) {
					return "MineGold";
				} else if (rowIndex == 12) {
					return "MineGold by non-professionals";
				} else if (rowIndex == 13) {
					return "HarvestCotton";
				} else if (rowIndex == 14) {
					return "HarvestCotton by non-professionals";
				} else if (rowIndex == 15) {
					return "BuildHouseAction";
				} else if (rowIndex == 16) {
					return "BuildHouseAction by non-professionals";
				} else if (rowIndex == 17) {
					return "ConstructBedAction";
				} else if (rowIndex == 18) {
					return "ConstructBedAction by non-professionals";
				} else if (rowIndex == 19) {
					return "BuyBuildingConversation";
				} else if (rowIndex == 20) {
					return "SellBuildingConversation";
				} else {
					return Item.values()[rowIndex - ROW_OFFSET].name() + " (current price/default price)";
				}
			} else if (columnIndex == 1) {
				if (rowIndex == 0) {
					return OperationStatistics.getRecentOperationsCount(Actions.CUT_WOOD_ACTION, world);
				} else if (rowIndex == 1) {
					return OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.CUT_WOOD_ACTION, Professions.LUMBERJACK_PROFESSION, world);
				} else if (rowIndex == 2) {
					return OperationStatistics.getRecentOperationsCount(Actions.HARVEST_FOOD_ACTION, world);
				} else if (rowIndex == 3) {
					return OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.HARVEST_FOOD_ACTION, Professions.FARMER_PROFESSION, world);
				} else if (rowIndex == 4) {
					return OperationStatistics.getRecentOperationsCount(Actions.BUTCHER_ACTION, world);
				} else if (rowIndex == 5) {
					return OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.BUTCHER_ACTION, Professions.FARMER_PROFESSION, world);
				} else if (rowIndex == 6) {
					return OperationStatistics.getRecentOperationsCount(Actions.EAT_ACTION, world);
				} else if (rowIndex == 7) {
					return OperationStatistics.getRecentOperationsCount(Actions.MINE_STONE_ACTION, world);
				} else if (rowIndex == 8) {
					return OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.MINE_STONE_ACTION, Professions.MINER_PROFESSION, world);
				} else if (rowIndex == 9) {
					return OperationStatistics.getRecentOperationsCount(Actions.MINE_ORE_ACTION, world);
				} else if (rowIndex == 10) {
					return OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.MINE_ORE_ACTION, Professions.MINER_PROFESSION, world);
				} else if (rowIndex == 11) {
					return OperationStatistics.getRecentOperationsCount(Actions.MINE_GOLD_ACTION, world);
				} else if (rowIndex == 12) {
					return OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.MINE_GOLD_ACTION, Professions.MINER_PROFESSION, world);
				} else if (rowIndex == 13) {
					return OperationStatistics.getRecentOperationsCount(Actions.HARVEST_COTTON_ACTION, world);
				} else if (rowIndex == 14) {
					return OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.HARVEST_COTTON_ACTION, Professions.WEAVER_PROFESSION, world);
				} else if (rowIndex == 15) {
					return OperationStatistics.getRecentOperationsCount(Actions.BUILD_HOUSE_ACTION, world);
				} else if (rowIndex == 16) {
					return OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.BUILD_HOUSE_ACTION, Professions.CARPENTER_PROFESSION, world);
				} else if (rowIndex == 17) {
					return OperationStatistics.getRecentOperationsCount(Actions.CONSTRUCT_BED_ACTION, world);
				} else if (rowIndex == 18) {
					return OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.CONSTRUCT_BED_ACTION, Professions.CARPENTER_PROFESSION, world);
				} else if (rowIndex == 19) {
					return OperationStatistics.getRecentOperationsCount(Actions.TALK_ACTION, Conversations.createArgs(Conversations.BUY_HOUSE_CONVERSATION), world);
				} else if (rowIndex == 20) {
					return OperationStatistics.getRecentOperationsCount(Actions.TALK_ACTION, Conversations.createArgs(Conversations.SELL_HOUSE_CONVERSATION), world);
				} else {
					Item item = Item.values()[rowIndex - ROW_OFFSET];
					return OperationStatistics.getPrice(item, world) + "/" + item.getPrice();
				}
			} else {
				return null;
			}
		}
	}
}