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

import java.io.Serializable;

public class Skill implements Serializable {

	private int level;
	private int currentUsageCount;
	private int maxUsageCount;
	
	public Skill() {
		this.level = 0;
		this.currentUsageCount = 0;
		this.maxUsageCount = calculateMaxUsageCount();
	}

	private Skill(int level, int currentUsageCount, int maxUsageCount) {
		super();
		this.level = level;
		this.currentUsageCount = currentUsageCount;
		this.maxUsageCount = maxUsageCount;
	}

	private int calculateMaxUsageCount() {
		return 3 * level * level + 10;
	}
	
	public void use() {
		currentUsageCount++;
		
		if (currentUsageCount >= maxUsageCount) {
			level++;
			currentUsageCount = 0;
			maxUsageCount = calculateMaxUsageCount();
		}
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getPercentageUntilNextLevelUp() {
		return (currentUsageCount * 100) / maxUsageCount;
	}

	public Skill copy() {
		return new Skill(level, currentUsageCount, maxUsageCount);
	}
	
	@Override
	public String toString() {
		return Integer.toString(level);
	}
}
