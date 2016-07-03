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

/**
 * Whenever the goal of a non-player character changes,
 * the reason is recorded as one of these states.
 */
public enum GoalChangedReason {
	EMPTY_META_INFORMATION, 
	FINAL_GOAL_WAS_MET, 
	TARGET_NO_LONGER_VALID,
	MORE_IMPORTANT_GOAL_NOT_MET,
	FINAL_OPERATION_NOT_POSSIBLE,
	OPERATION_NOT_POSSIBLE,
	TARGET_MOVED, 
	DECEIVED,
	NO_ACTION_POSSIBLE
}
