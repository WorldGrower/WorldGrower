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
package org.worldgrower;

import java.util.List;

public interface WorldObjectsCache {

	public void add(WorldObject worldObject);
	public void remove(WorldObject worldObject);
	public void update(WorldObject worldObject, int value1, int value2);
	public void update(WorldObject worldObject, int value1, int value2, int value3, int value4);
	public List<WorldObject> getWorldObjectsFor(int value1, int value2);
	public boolean hasWorldObjects(int value1, int value2);
}
