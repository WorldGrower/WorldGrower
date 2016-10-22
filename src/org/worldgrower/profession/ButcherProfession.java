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
import org.worldgrower.generator.Item;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.gui.ImageIds;

public class ButcherProfession implements Profession {

	public ButcherProfession(List<Profession> allProfessions) {
		allProfessions.add(this);
	}

	@Override
	public String getDescription() {
		return "butcher";
	}

	@Override
	public List<Goal> getProfessionGoals() {
		return Arrays.asList(
				Goals.CLAIM_CATTLE_GOAL,
				Goals.BUTCHER_OWNED_CATTLE_GOAL,
				Goals.SELL_FOOD_GOAL,
				Goals.EQUIP_BUTCHER_KNIFE_GOAL,
				Goals.MARK_WORKBENCH_AS_SELLABLE_AFTER_EQUIPING_BUTCHER_KNIFE
				);
	}

	@Override
	public SkillProperty getSkillProperty() {
		return Constants.FARMING_SKILL;
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public boolean isPaidByVillagerLeader() {
		return false;
	}
	
	@Override
	public boolean avoidEnemies() {
		return true;
	}

	@Override
	public List<Item> getSellItems() {
		return Arrays.asList(Item.MEAT);
	}

	@Override
	public ImageIds getImageId() {
		return ImageIds.BUTCHER_KNIFE;
	}
}
