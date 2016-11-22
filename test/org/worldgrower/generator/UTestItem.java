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
package org.worldgrower.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.deity.Deity;
import org.worldgrower.profession.Professions;

public class UTestItem {

	@Test
	public void testGenerateNewsPaper() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		world.addWorldObject(performer);
		
		KnowledgeMap knowledgeMap = performer.getProperty(Constants.KNOWLEDGE_MAP);
		int id1 = knowledgeMap.addKnowledge(performer, Constants.DEITY, Deity.ARES);
		int id2 = knowledgeMap.addKnowledge(performer, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		WorldObject newsPaper = Item.generateNewsPaper(knowledgeMap.getSortedKnowledge(performer, world), new int[] { id1, id2 }, world);
		assertEquals("performer worships Ares. performer is a farmer. ", newsPaper.getProperty(Constants.TEXT));
	}
}