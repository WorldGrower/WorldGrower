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

public class LearnMagicSpellFromBookAction extends InventoryAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int inventoryIndex = args[0];
		WorldObject textTarget = performer.getProperty(Constants.INVENTORY).get(inventoryIndex);

		performer.getProperty(Constants.KNOWN_SPELLS).add(textTarget.getProperty(Constants.MAGIC_SPELL));
		
		world.logAction(this, performer, target, args, textTarget.getProperty(Constants.TEXT));
	}
	
	@Override
	public boolean isValidInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		return inventoryItem.hasProperty(Constants.MAGIC_SPELL) && !performer.getProperty(Constants.KNOWN_SPELLS).contains(inventoryItem.getProperty(Constants.MAGIC_SPELL));
	}
	
	@Override
	public boolean isActionPossibleOnInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		return true;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "Magic spell book";
	}
	
	@Override
	public String getDescription() {
		return "learn magic spell from spell book";
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "learning magic spell " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "learn magic spell";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.SPELL_BOOK;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.PAPER;
	}
}