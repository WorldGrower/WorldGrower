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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.BuildingGenerator;

public class UTestResearchSpellAction {

	private ResearchSpellAction researchSpellAction = new ResearchSpellAction(Actions.BURDEN_ACTION);
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createLibrary(performer, world);
		
		assertEquals(0, performer.getProperty(Constants.STUDYING_SPELLS).count(Actions.BURDEN_ACTION));
		
		researchSpellAction.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.STUDYING_SPELLS).count(Actions.BURDEN_ACTION));
	}
	
	private WorldObject createLibrary(WorldObject performer, World world) {
		int libraryId = BuildingGenerator.generateLibrary(0, 0, world, performer);
		return world.findWorldObjectById(libraryId);
	}
	
	@Test
	public void testExecuteLearnSpell() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createLibrary(performer, world);
		
		assertEquals(0, performer.getProperty(Constants.STUDYING_SPELLS).count(Actions.BURDEN_ACTION));
		
		for(int i=0; i<100; i++) {
			researchSpellAction.execute(performer, target, Args.EMPTY, world);
		}
		
		assertEquals(true, performer.getProperty(Constants.KNOWN_SPELLS).contains(Actions.BURDEN_ACTION));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(false, researchSpellAction.isValidTarget(performer, performer, world));
		
		int targetId = BuildingGenerator.generateLibrary(0, 0, world, performer);
		WorldObject target = world.findWorldObjectById(targetId);
		assertEquals(true, researchSpellAction.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(false, researchSpellAction.isActionPossible(performer, target, Args.EMPTY, world));
		
		performer.getProperty(Constants.TRANSMUTATION_SKILL).use(100, performer, Constants.TRANSMUTATION_SKILL, world.getWorldStateChangedListeners());		
		
		assertEquals(true, researchSpellAction.isActionPossible(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(0, researchSpellAction.distance(performer, target, Args.EMPTY, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.CONDITIONS, new Conditions());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.STUDYING_SPELLS, new PropertyCountMap<>());
		performer.setProperty(Constants.KNOWN_SPELLS, new ArrayList<>());
		return performer;
	}
}