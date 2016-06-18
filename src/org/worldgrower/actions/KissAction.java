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
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.RacePropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class KissAction implements ManagedOperation {

	public static final int REQUIRED_RELATIONSHIP_VALUE = 10;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		
		RelationshipPropertyUtils.changeRelationshipValueUsingFacades(performer, target, 50, this, args, world);
		
		world.logAction(this, performer, target, args, "");
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		if (target.hasProperty(Constants.RELATIONSHIPS)) {
			return (target.hasIntelligence() && (target.getProperty(Constants.RELATIONSHIPS).getValue(performer) > REQUIRED_RELATIONSHIP_VALUE)  && !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, target));
		} else {
			return RacePropertyUtils.hasSameRace(performer, target);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "kissing " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "kiss";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.HEART;
	}

	@Override
	public SoundIds getSoundId() {
		return SoundIds.KISS;
	}
}