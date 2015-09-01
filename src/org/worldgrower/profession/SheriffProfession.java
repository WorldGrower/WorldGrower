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
package org.worldgrower.profession;

import java.io.ObjectStreamException;
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

public class SheriffProfession implements Profession {

	public SheriffProfession(List<Profession> allProfessions) {
		allProfessions.add(this);
	}

	@Override
	public String getDescription() {
		return "sheriff";
	}

	@Override
	public List<Goal> getProfessionGoals() {
		return Arrays.asList(
				Goals.JAIL_GOAL,
				Goals.CAPTURE_CRIMINALS_GOAL,
				Goals.KILL_OUTSIDERS_GOAL,
				Goals.CATCH_THIEVES_GOAL,
				Goals.COLLECT_PAY_CHECK_GOAL,
				Goals.RELEASE_CAPTURED_CRIMINALS_GOAL,
				Goals.TRAIN_GOAL
				);
	}

	@Override
	public SkillProperty getSkillProperty() {
		return Constants.ARCHERY_SKILL;
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public boolean isPaidByVillagerLeader() {
		return true;
	}
	
	@Override
	public boolean avoidEnemies() {
		return false;
	}
}
