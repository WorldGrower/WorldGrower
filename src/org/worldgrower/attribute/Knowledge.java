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
package org.worldgrower.attribute;

import java.util.function.Function;

import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public interface Knowledge {

	public boolean refersToSameKnowledge(Knowledge knowledge);
	public boolean knowledgeContainsId(int idToRemove);
	public Knowledge copy();
	public int getId();
	public int getSubjectId();
	
	public boolean hasProperty(ManagedProperty<?> managedProperty);
	public boolean hasPropertyValue(ManagedProperty<?> managedProperty, Object value);

	public boolean hasEvent(Function<Integer, Boolean> turnFunction, Function<WorldObject, Boolean> targetFunction, World world, ManagedOperation... actions);
	
	public int evaluate(WorldObject performer, World world);
}