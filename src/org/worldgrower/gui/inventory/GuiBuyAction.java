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
package org.worldgrower.gui.inventory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.Main;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.BuySellUtils;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;

public class GuiBuyAction extends AbstractAction {

	private WorldObject playerCharacter;
	private World world;
	private DungeonMaster dungeonMaster;
	private WorldPanel container;
	private WorldObject target;
	private InventoryDialog dialog;
	private ImageInfoReader imageInfoReader;
	
	public GuiBuyAction(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, WorldPanel container, WorldObject target, ImageInfoReader imageInfoReader) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.dungeonMaster = dungeonMaster;
		this.container = container;
		this.target = target;
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog = new InventoryDialog(playerCharacter.getProperty(Constants.GOLD), target.getProperty(Constants.INVENTORY), new InventoryDialogBuyAction(), imageInfoReader);
		dialog.showMe();
	}

	private class InventoryDialogBuyAction implements InventoryDialogAction {

		@Override
		public String getDescription() {
			return "Buy selected item";
		}

		@Override
		public ActionListener getGuiAction() {
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					InventoryItem inventoryItem = dialog.getSelectedValue();
					int price = BuySellUtils.getPrice(playerCharacter, inventoryItem.getId());
					int[] args = new int[] { inventoryItem.getId(), price };
					buy(args);
					
					dialog.refresh(target.getProperty(Constants.INVENTORY), playerCharacter.getProperty(Constants.GOLD));
				}
			};
		}

		@Override
		public boolean isPossible(InventoryItem inventoryItem) {
			int price = inventoryItem.getPrice();
			return inventoryItem.isSellable() && (price <= playerCharacter.getProperty(Constants.GOLD));
		}
		
	}
	
	public void buy(int[] args) {
		Main.executeAction(playerCharacter, playerCharacter.getOperation(Actions.BUY_ACTION), args, world, dungeonMaster, target, container);
	}
}