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
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.Item;

public class UTestDisintegrateArmorAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		WorldObject ironCuirass = Item.IRON_CUIRASS.generate(1f);
		target.getProperty(Constants.INVENTORY).addQuantity(ironCuirass);
		target.setProperty(Constants.TORSO_EQUIPMENT, ironCuirass);
		assertEquals(1000, target.getProperty(Constants.INVENTORY).get(0).getProperty(Constants.EQUIPMENT_HEALTH).intValue());
		
		Actions.DISINTEGRATE_ARMOR_ACTION.execute(performer, target, new int[0], world);
		
		assertEquals(900, target.getProperty(Constants.INVENTORY).get(0).getProperty(Constants.EQUIPMENT_HEALTH).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		target.setProperty(Constants.ARMOR, 10);
		performer.setProperty(Constants.KNOWN_SPELLS, new ArrayList<>());
		assertEquals(false, Actions.DISINTEGRATE_ARMOR_ACTION.isValidTarget(performer, target, world));
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.DISINTEGRATE_ARMOR_ACTION));
		assertEquals(true, Actions.DISINTEGRATE_ARMOR_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(0, Actions.DISINTEGRATE_ARMOR_ACTION.distance(performer, target, new int[0], world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		return performer;
	}
}