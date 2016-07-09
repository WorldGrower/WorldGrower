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
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestContainerUtils {

	@Test
	public void testAccessContainer() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		int targetId = BuildingGenerator.generateHouse(0, 0, world, 1f, performer);
		WorldObject target = world.findWorldObjectById(targetId);
		
		performer.setProperty(Constants.HIT_POINTS, 10 * Item.COMBAT_MULTIPLIER);
		performer.setProperty(Constants.ENERGY, 1000);
		
		Actions.TRAP_CONTAINER_MAGIC_SPELL_ACTION.execute(performer, target, Args.EMPTY, world);
		ContainerUtils.accessContainer(performer, target, world);
		
		assertEquals(5 * Item.COMBAT_MULTIPLIER, performer.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testAvoidTrappedContainer() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		int targetId = BuildingGenerator.generateHouse(0, 0, world, 1f, performer);
		WorldObject target = world.findWorldObjectById(targetId);
		
		performer.setProperty(Constants.HIT_POINTS, 10 * Item.COMBAT_MULTIPLIER);
		performer.setProperty(Constants.ENERGY, 1000);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(target, Constants.TRAPPED_CONTAINER_DAMAGE, 5);
		Actions.TRAP_CONTAINER_MAGIC_SPELL_ACTION.execute(performer, target, Args.EMPTY, world);

		assertEquals(null, ContainerUtils.avoidTrappedContainer(performer, target, world));
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.DISPEL_MAGIC_ACTION));
		assertEquals(Actions.DISPEL_MAGIC_ACTION, ContainerUtils.avoidTrappedContainer(performer, target, world).getManagedOperation());
	}
}
