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
package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.curse.Curse;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.TerrainGenerator;

public class UTestBecomeLichGoal {

	private BecomeLichGoal goal = Goals.BECOME_LICH_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalMineSoulGems() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		TerrainGenerator.generateStoneResource(0, 0, world);
		
		assertEquals(Actions.MINE_SOUL_GEMS_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalRest() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.setProperty(Constants.ENERGY, 0);
		
		addFilledSoulGems(performer);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.LICH_TRANSFORMATION_ACTION));
		
		assertEquals(Actions.REST_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	private void addFilledSoulGems(WorldObject performer) {
		for(int i=0; i<10; i++) {
			WorldObject soulGem = Item.FILLED_SOUL_GEM.generate(1f);
			performer.getProperty(Constants.INVENTORY).addQuantity(soulGem);
		}
	}
	
	@Test
	public void testCalculateGoalBecomeLich() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.setProperty(Constants.ENERGY, 1000);
		
		addFilledSoulGems(performer);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.LICH_TRANSFORMATION_ACTION));
		
		assertEquals(Actions.LICH_TRANSFORMATION_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.CURSE, Curse.LICH_CURSE);
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}