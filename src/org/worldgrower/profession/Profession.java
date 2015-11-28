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
import java.io.Serializable;
import java.util.List;

import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.Goal;

public interface Profession extends Serializable {
	public String getDescription();
	public List<Goal> getProfessionGoals();
	public SkillProperty getSkillProperty();
	public List<Item> getSellItems();
	
	public default Object readResolveImpl() throws ObjectStreamException {
		Class<?> clazz = getClass();
		List<Profession> allProfessions = Professions.getAllProfessions();
		
		for(Profession profession : allProfessions) {
			if (profession.getClass() == clazz) {
				return profession;
			}
		}
		
		throw new IllegalStateException("Profession with class " + clazz + " not found");
	}
	public boolean isPaidByVillagerLeader();
	public boolean avoidEnemies();
}
