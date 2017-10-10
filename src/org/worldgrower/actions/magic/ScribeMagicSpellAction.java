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
package org.worldgrower.actions.magic;

import java.io.ObjectStreamException;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class ScribeMagicSpellAction implements ManagedOperation {

	private static final int PAPER_REQUIRED = 5;
	private final MagicSpell magicSpell;
	
	public ScribeMagicSpellAction(MagicSpell magicSpell) {
		this.magicSpell = magicSpell;
	}

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		inventory.addQuantity(Item.generateSpellBook(magicSpell));
		inventory.removeQuantity(Constants.PAPER, PAPER_REQUIRED);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.hasEnoughResources(performer, Constants.PAPER, PAPER_REQUIRED);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.PAPER, PAPER_REQUIRED);
	}
	
	@Override
	public String getDescription() {
		return "scribe magic spell so that other can learn it more easily";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.LIBRARY_QUALITY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "scribing spell '" + magicSpell.getSimpleDescription() + "'";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getSimpleDescription() {
		return "scribe spell '" + magicSpell.getSimpleDescription() + "'";
	}
	
	public MagicSpell getSpell() {
		return magicSpell;
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return magicSpell.getImageIds(performer);
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.PAPER;
	}
}