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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.worldgrower.generator.BuildingGenerator;

class JailCache implements WorldObjectsCache, Serializable {

	private final List<WorldObject> jailsCache = new ArrayList<>();
	
	@Override
	public void add(WorldObject jailLeftWorldObject) {
		if (BuildingGenerator.isJailLeft(jailLeftWorldObject)) {
			jailsCache.add(jailLeftWorldObject);
		}
	}

	@Override
	public void remove(WorldObject jailLeftWorldObject) {
		if (BuildingGenerator.isJailLeft(jailLeftWorldObject)) {
			jailsCache.remove(jailLeftWorldObject);
		}
	}

	@Override
	public List<WorldObject> getWorldObjectsFor(int x, int y) {
		return Collections.unmodifiableList(jailsCache);
	}
	
	@Override
	public void update(WorldObject worldObject, int newX, int newY) {
		throw new IllegalStateException("operation update not supported");
	}

	@Override
	public void update(WorldObject worldObject, int newX, int newY, int newWidth, int newHeight) {
		throw new IllegalStateException("operation update not supported");
	}
}
