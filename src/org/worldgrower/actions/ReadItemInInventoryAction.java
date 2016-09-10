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
package org.worldgrower.actions;

import java.io.ObjectStreamException;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class ReadItemInInventoryAction extends InventoryAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int inventoryIndex = args[0];
		WorldObject textTarget = performer.getProperty(Constants.INVENTORY).get(inventoryIndex);

		performer.getProperty(Constants.KNOWLEDGE_MAP).add(textTarget.getProperty(Constants.KNOWLEDGE_MAP));
		performer.setProperty(Constants.NEWSPAPER_READ_TURN, world.getCurrentTurn().getValue());
		
		world.logAction(this, performer, target, args, textTarget.getProperty(Constants.TEXT));
	}
	
	@Override
	public boolean isValidInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		return inventoryItem.hasProperty(Constants.TEXT);
	}
	
	@Override
	public boolean isActionPossibleOnInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		return true;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "Readable text";
	}
	
	@Override
	public String getDescription() {
		return "reads what is written on something";
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "reading " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "read";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.NEWS_PAPER;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.BOOK_FLIP;
	}
}