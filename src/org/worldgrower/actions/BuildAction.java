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

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.WorldObject;

public interface BuildAction extends ManagedOperation {

	public int getWidth();
	public int getHeight();
	
	public default boolean isInAreaOfEffect(int x, int y, WorldObject w) {
		int targetX = w.getProperty(Constants.X);
		int targetY = w.getProperty(Constants.Y);
		return x <= targetX && targetX < x + getWidth()
			&& y <= targetY && targetY < y + getHeight();
	}
}
