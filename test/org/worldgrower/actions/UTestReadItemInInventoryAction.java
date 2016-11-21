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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Knowledge;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.PropertyKnowledge;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.profession.Professions;

public class UTestReadItemInInventoryAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(1);
		world.addWorldObject(performer);
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		List<Knowledge> knowledgeList = Arrays.asList(new PropertyKnowledge(1, Constants.PROFESSION, Professions.FARMER_PROFESSION));
		int[] knowledgeIds = { knowledgeList.get(0).getId() };
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.generateNewsPaper(knowledgeList, knowledgeIds, world));
		
		Actions.READ_ITEM_IN_INVENTORY_ACTION.execute(performer, performer, new int[] {0}, world);
		
		assertEquals(true, performer.getProperty(Constants.KNOWLEDGE_MAP).hasKnowledge(1));
		assertEquals("name is a farmer\n", performer.getProperty(Constants.INVENTORY).get(0).getProperty(Constants.TEXT));
	}
	
	@Test
	public void testExecuteNewspaper() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(1);
		world.addWorldObject(performer);
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		// increase knowledge Id
		new PropertyKnowledge(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		new PropertyKnowledge(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		new PropertyKnowledge(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		List<Knowledge> knowledgeList = Arrays.asList(new PropertyKnowledge(1, Constants.PROFESSION, Professions.FARMER_PROFESSION));
		int[] knowledgeIds = { knowledgeList.get(0).getId() };
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.generateNewsPaper(knowledgeList, knowledgeIds, world));
		
		Actions.READ_ITEM_IN_INVENTORY_ACTION.execute(performer, performer, new int[] {0}, world);
		
		assertEquals(true, performer.getProperty(Constants.KNOWLEDGE_MAP).hasKnowledge(1));
		assertEquals("name is a farmer\n", performer.getProperty(Constants.INVENTORY).get(0).getProperty(Constants.TEXT));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.NAME, "name");
		return performer;
	}
}