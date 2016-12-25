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
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Knowledge;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class CreateNewsPaperAction implements ManagedOperation {

	private static final int PAPER_REQUIRED = 2;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		List<Knowledge> knowledgeList = performer.getProperty(Constants.KNOWLEDGE_MAP).getSortedKnowledge(performer, world);
		int[] knowledgeIds = args;
		
		WorldObject newsPaper = Item.generateNewsPaper(knowledgeList, knowledgeIds, world);
		
		inventory.addUniqueQuantity(newsPaper);
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
		return "a newspaper contains knowledge that is spread by reading it";
	}

	@Override
	public boolean requiresArguments() {
		return true;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "creating a newspaper";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getSimpleDescription() {
		return "create newspaper";
	}
	
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.NEWS_PAPER;
	}
	
	public static boolean hasEnoughPaper(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.PAPER) >= PAPER_REQUIRED;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.PAPER;
	}
}