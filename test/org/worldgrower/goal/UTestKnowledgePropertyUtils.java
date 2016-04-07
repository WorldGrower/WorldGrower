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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.attribute.Knowledge;
import org.worldgrower.attribute.PropertyKnowledge;

public class UTestKnowledgePropertyUtils {

	@Test
	public void testCreateArgs() {
		List<Knowledge> sortedKnowledge = new ArrayList<>();
		sortedKnowledge.add(new PropertyKnowledge(1, Constants.FOOD, 500));
		sortedKnowledge.add(new PropertyKnowledge(2, Constants.FOOD, 500));
		sortedKnowledge.add(new PropertyKnowledge(3, Constants.FOOD, 500));
	
		int[] args = KnowledgePropertyUtils.createArgs(sortedKnowledge, 2);
		assertEquals(2, args.length);
		assertEquals(sortedKnowledge.get(0).getId(), args[0]);
		assertEquals(sortedKnowledge.get(1).getId(), args[1]);
	}
	
	@Test
	public void testExists() {
		List<Knowledge> sortedKnowledge = new ArrayList<>();
		PropertyKnowledge id = new PropertyKnowledge(1, Constants.FOOD, 500);
		sortedKnowledge.add(id);
		
		assertEquals(true, KnowledgePropertyUtils.exists(sortedKnowledge, id.getId()));
		assertEquals(false, KnowledgePropertyUtils.exists(sortedKnowledge, id.getId() + 7));
	}
}
