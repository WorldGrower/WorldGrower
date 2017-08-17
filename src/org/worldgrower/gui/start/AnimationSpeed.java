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
package org.worldgrower.gui.start;

public enum AnimationSpeed {
	FASTEST(0, "Fastest"),
	VERY_FAST(4, "Very Fast"),
	FAST(6, "Fast"),
	NORMAL(8, "Normal"),
	SLOW(10, "Slow"),
	VERY_SLOW(12, "Very Slow");
	
	private final int sleepTime;
	private final String description;
	
	
	private AnimationSpeed(int sleepTime, String description) {
		this.sleepTime = sleepTime;
		this.description = description;
	}

	public int getSleepTime() {
		return sleepTime;
	}
	
	@Override
	public String toString() {
		return description;
	}

	public static AnimationSpeed valueOf(int animationSleepTime) {
		for(AnimationSpeed animationSpeed : values()) {
			if (animationSleepTime == animationSpeed.getSleepTime()) {
				return animationSpeed;
			}
		}
		throw new IllegalStateException("animationSleepTime " + animationSleepTime + " cannot be mapped to an existing AnimationSpeed instance");
	}
}
