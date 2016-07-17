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
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.MarkInventoryItemAsSellableAction;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.BuySellUtils;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.util.TextInputDialog;
import org.worldgrower.util.NumberUtils;

public class InventoryActionFactory {

	private WorldObject playerCharacter;
	private InventoryDialog dialog;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private World world;
	private DungeonMaster dungeonMaster;
	private WorldPanel container;
	private WorldObject target;
	private JFrame parentFrame;
	
	public InventoryActionFactory(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, DungeonMaster dungeonMaster, WorldPanel container, WorldObject target, JFrame parentFrame) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.world = world;
		this.dungeonMaster = dungeonMaster;
		this.container = container;
		this.target = target;
		this.parentFrame = parentFrame;
	}
	
	public InventoryActionFactory setDialog(InventoryDialog dialog) {
		this.dialog = dialog;
		return this;
	}

	public List<Action> getPlayerCharacterInventoryActions(int inventoryItemId) {
		List<Action> inventoryActions = new ArrayList<>();
		WorldObjectContainer inventory = playerCharacter.getProperty(Constants.INVENTORY);
		WorldObject inventoryItem = inventory.get(inventoryItemId);
		
		for(org.worldgrower.actions.InventoryAction action : Actions.getInventoryActions()) {
			if (action.isValidInventoryItem(inventoryItem, inventory, playerCharacter)) {
				//TODO: move instanceof code to classes themselves
				if (action instanceof MarkInventoryItemAsSellableAction) {
					int[] args = MarkInventoryItemAsSellableAction.createArgs(inventoryItemId, !inventoryItem.getProperty(Constants.SELLABLE));
					inventoryActions.add(new InventoryItemAction(action, args, inventoryItemId, playerCharacter));
				} else {
					inventoryActions.add(new InventoryItemAction(action, inventoryItemId));	
				}
			}
		}
		
		if (target != null && target.hasIntelligence()) {
			if (BuySellUtils.targetWillBuyGoods(playerCharacter, target, inventoryItemId, world)) {
				int[] args = new int[] { inventoryItemId, inventoryItem.getProperty(Constants.PRICE), 1 };
				inventoryActions.add(new InventoryItemAction(Actions.SELL_ACTION, args, inventoryItemId, target));
			}
		}
		
		if (target != null) {
			if (Game.canActionExecute(playerCharacter, playerCharacter.getOperation(Actions.PUT_ITEM_INTO_INVENTORY_ACTION), new int[] {inventoryItemId}, world, target)) {
				inventoryActions.add(new InventoryItemAction(Actions.PUT_ITEM_INTO_INVENTORY_ACTION, inventoryItemId, target));
			}
		}
		return inventoryActions;
	}
	
	public List<Action> getTargetInventoryActions(int inventoryItemId) {
		List<Action> inventoryActions = new ArrayList<>();
		WorldObjectContainer inventory = target.getProperty(Constants.INVENTORY);
		
		WorldObject inventoryItem = inventory.get(inventoryItemId);
		
		if (target != null && target.hasIntelligence()) {
			int price = inventoryItem.getProperty(Constants.PRICE);
			if (inventoryItem.getProperty(Constants.SELLABLE) && (price <= playerCharacter.getProperty(Constants.GOLD))) {
				int[] args = new int[] { inventoryItemId, price, 1 };
				inventoryActions.add(new InventoryItemAction(Actions.BUY_ACTION, args, inventoryItemId, target));
			}
			
			if (Game.canActionExecute(playerCharacter, playerCharacter.getOperation(Actions.STEAL_ACTION), Args.EMPTY, world, target)) {
				inventoryActions.add(new InventoryItemAction(Actions.STEAL_ACTION, inventoryItemId, target));
			}
		}
		
		if (target != null) {
			if (Game.canActionExecute(playerCharacter, playerCharacter.getOperation(Actions.GET_ITEM_FROM_INVENTORY_ACTION), new int[] {inventoryItemId}, world, target)) {
				inventoryActions.add(new InventoryItemAction(Actions.GET_ITEM_FROM_INVENTORY_ACTION, inventoryItemId, target));
			}
		}
		return inventoryActions;
	}
	
	public boolean hasTargetMoneyActions() {
		return target.getProperty(Constants.GOLD).intValue() > 0;
	}
	
	public List<Action> getTargetMoneyActions() {
		List<Action> inventoryActions = new ArrayList<>();
		Action stealMoneyAction = new StealAction(soundIdReader);
		inventoryActions.add(stealMoneyAction);
		
		return inventoryActions;
	}
	
	private class StealAction extends AbstractAction {
		
		private final SoundIdReader soundIdReader;
		
		public StealAction(SoundIdReader soundIdReader) {
			super(Actions.STEAL_ACTION.getSimpleDescription(), new ImageIcon(imageInfoReader.getImage(Actions.STEAL_ACTION.getImageIds(), null)));
			this.putValue(Action.LONG_DESCRIPTION, Actions.STEAL_ACTION.getRequirementsDescription());
			
			this.soundIdReader = soundIdReader;
		}
		
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			int targetGold = target.getProperty(Constants.GOLD).intValue();
			TextInputDialog textInputDialog = new TextInputDialog("Steal how much money (1-" + targetGold + ")?", true, soundIdReader, parentFrame);
			String input = textInputDialog.showMe();
			if (input != null && input.length() > 0 && NumberUtils.isNumeric(input)) {
				int amount = Integer.parseInt(input);
				if (amount > 0 && amount <= targetGold) {
					int[] args = new int[] { amount };
					Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, playerCharacter.getOperation(Actions.STEAL_GOLD_ACTION), args, world, dungeonMaster, target, container, soundIdReader);
					
					dialog.refresh(new InventoryDialogModel(playerCharacter, target, world, dungeonMaster, container, soundIdReader));
				}
			}
		}
	}

	private class InventoryItemAction extends AbstractAction {
		
		private final ManagedOperation action;
		private final int inventoryItemId;
		private final int[] args;
		private final WorldObject target;
		
		public InventoryItemAction(ManagedOperation action, int[] args, int inventoryItemId, WorldObject target) {
			super(action.getSimpleDescription(), new ImageIcon(imageInfoReader.getImage(action.getImageIds(), null)));
			this.action = action;
			this.args = args;
			this.inventoryItemId = inventoryItemId;
			this.target = target;
			this.putValue(Action.LONG_DESCRIPTION, action.getRequirementsDescription());
		}
		
		public InventoryItemAction(ManagedOperation action, int inventoryItemId) {
			this(action, new int[] { inventoryItemId }, inventoryItemId, playerCharacter);
		}
		
		public InventoryItemAction(ManagedOperation action, int inventoryItemId, WorldObject target) {
			this(action, new int[] { inventoryItemId }, inventoryItemId, target);
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, playerCharacter.getOperation(action), args, world, dungeonMaster, target, container, soundIdReader);
			
			final InventoryDialogModel inventoryDialogModel;
			if (target == playerCharacter) {
				inventoryDialogModel = new InventoryDialogModel(playerCharacter, world, dungeonMaster, container, soundIdReader);
			} else {
				inventoryDialogModel = new InventoryDialogModel(playerCharacter, target, world, dungeonMaster, container, soundIdReader);
			}
			dialog.refresh(inventoryDialogModel);
		}

		@Override
		public boolean isEnabled() {
			return Game.canActionExecute(playerCharacter, action, args, world, target);
		}
	}
}