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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;

public class UTestResearchEvocationSkillAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.TEXT, "text");
		
		for(int i=0; i<200; i++) {
			Actions.RESEARCH_EVOCATION_SKILL_ACTION.execute(performer, target, Args.EMPTY, world);
		}
		
		assertEquals(5, performer.getProperty(Constants.EVOCATION_SKILL).getLevel(performer));
	}
	
    @Test
	public void testIsValidTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		
		int targetId = BuildingGenerator.generateLibrary(0, 0, world);
		WorldObject target = world.findWorldObject(Constants.ID, targetId);
		
		assertEquals(true, Actions.RESEARCH_EVOCATION_SKILL_ACTION.isValidTarget(performer, target, world));
		assertEquals(false, Actions.RESEARCH_EVOCATION_SKILL_ACTION.isValidTarget(performer, performer, world));
	}
    
    @Test
	public void testDistance() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		
		int targetId = BuildingGenerator.generateLibrary(0, 0, world);
		WorldObject target = world.findWorldObject(Constants.ID, targetId);
		
		assertEquals(0, Actions.RESEARCH_EVOCATION_SKILL_ACTION.distance(performer, target, Args.EMPTY, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}