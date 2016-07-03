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

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Conditions;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.generator.Item;

public class UTestLichTransformationAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		Actions.LICH_TRANSFORMATION_ACTION.execute(performer, performer, new int[] {0}, world);
		
		assertEquals(CreatureType.LICH_CREATURE_TYPE, performer.getProperty(Constants.CREATURE_TYPE));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.LICH_TRANSFORMATION_ACTION));
		assertEquals(true, Actions.LICH_TRANSFORMATION_ACTION.isValidTarget(performer, performer, world));
		
		performer.setProperty(Constants.KNOWN_SPELLS, new ArrayList<>());
		assertEquals(false, Actions.LICH_TRANSFORMATION_ACTION.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		for(int i=0; i<10; i++) {
			WorldObject soulGem = Item.SOUL_GEM.generate(1f);
			soulGem.setProperty(Constants.SOUL_GEM_FILLED, Boolean.TRUE);
			performer.getProperty(Constants.INVENTORY).addQuantity(soulGem);
		}
		
		assertEquals(true, Actions.LICH_TRANSFORMATION_ACTION.isActionPossible(performer, performer, Args.EMPTY, world));
	}
	
	@Test
	public void testHasRequiredEnergy() {
		WorldObject performer = createPerformer(2);
		
		performer.setProperty(Constants.ENERGY, 1000);
		assertEquals(true, Actions.LICH_TRANSFORMATION_ACTION.hasRequiredEnergy(performer));
		
		performer.setProperty(Constants.ENERGY, 0);
		assertEquals(false, Actions.LICH_TRANSFORMATION_ACTION.hasRequiredEnergy(performer));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.CONDITIONS, new Conditions());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		performer.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		return performer;
	}
}