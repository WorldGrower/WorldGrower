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
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestMinorIllusionAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		WorldObject target = TestUtils.createWorldObject(2, 2, 1, 1);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.MINOR_ILLUSION_ACTION));
		Actions.MINOR_ILLUSION_ACTION.execute(performer, target, new int[] { 2 }, world);

		List<WorldObject> worldObjects = world.getWorldObjects();
		WorldObject illusion = worldObjects.get(worldObjects.size() - 1);
		assertEquals(2, illusion.getProperty(Constants.ILLUSION_CREATOR_ID).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(0, 0, 1, 1);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.MINOR_ILLUSION_ACTION));
		assertEquals(true, Actions.MINOR_ILLUSION_ACTION.isValidTarget(performer, target, world));
		
		performer.setProperty(Constants.KNOWN_SPELLS, new ArrayList<>());
		assertEquals(false, Actions.MINOR_ILLUSION_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(0, 0, 1, 1);
		
		assertEquals(true, Actions.MINOR_ILLUSION_ACTION.isActionPossible(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(0, Actions.MINOR_ILLUSION_ACTION.distance(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testGetIllusionSources() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		
		WorldObject verminOrganization = GroupPropertyUtils.create(null, "vermin", world);
		verminOrganization.setProperty(Constants.ID, 2);
		world.addWorldObject(verminOrganization);
		
		assertEquals(performer, Actions.MINOR_ILLUSION_ACTION.getIllusionSources(performer, world).get(0));
		
		
		PlantGenerator.generateTree(0, 0, world);
		
		assertEquals(true, Actions.MINOR_ILLUSION_ACTION.getIllusionSources(performer, world).size() > 0);
		assertEquals(performer, Actions.MINOR_ILLUSION_ACTION.getIllusionSources(performer, world).get(0));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.CONDITIONS, new Conditions());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		return performer;
	}
}