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
package org.worldgrower.conversation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.attribute.EventKnowledge;
import org.worldgrower.attribute.Knowledge;
import org.worldgrower.attribute.PropertyKnowledge;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.CommonerImageIds;

public class UTestKnowledgeSorter {

	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testSortPropertyAndEvent() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		List<Knowledge> knowledgeList = new ArrayList<>();
		knowledgeList.add(new PropertyKnowledge(0, Constants.FOOD, 500));
		knowledgeList.add(new EventKnowledge(0, world));
		knowledgeList.add(new PropertyKnowledge(0, Constants.WATER, 500));
		knowledgeList.add(new EventKnowledge(0, world));
		
		new KnowledgeSorter().sort(performer, knowledgeList, world);
		assertEquals(4, knowledgeList.size());
		assertEquals(EventKnowledge.class, knowledgeList.get(0).getClass());
		assertEquals(EventKnowledge.class, knowledgeList.get(1).getClass());
		assertEquals(PropertyKnowledge.class, knowledgeList.get(2).getClass());
		assertEquals(PropertyKnowledge.class, knowledgeList.get(3).getClass());
	}
	
	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject performer = world.findWorldObject(Constants.ID, commonerId);
		return performer;
	}
}
