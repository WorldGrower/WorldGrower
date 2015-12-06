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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Knowledge;

public class KnowledgeSorter {

	public void sort(WorldObject performer, List<Knowledge> knowledgeList, World world) {
		Collections.sort(knowledgeList, new KnowledgeComparator(performer, world));
	}
	
	private static class KnowledgeComparator implements Comparator<Knowledge> {

		private final WorldObject performer;
		private final World world;
		
		public KnowledgeComparator(WorldObject performer, World world) {
			super();
			this.performer = performer;
			this.world = world;
		}

		@Override
		public int compare(Knowledge o1, Knowledge o2) {
			return Integer.compare(o1.evaluate(performer, world), o2.evaluate(performer, world));
		}
	}
}
