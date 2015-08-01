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
package org.worldgrower.goal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.BuildLibraryAction;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.WorldObjectContainer;

public class ScribeClericSpellsGoal implements Goal {

	private final List<MagicSpell> CLERIC_SPELLS = Arrays.asList(Actions.MINOR_HEAL_ACTION);
	
	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> libraries = getLibraries(world);
		if (libraries.size() > 0) {
			WorldObject library = libraries.get(0);
			
			List<MagicSpell> missingClericSpells = new ArrayList<>(CLERIC_SPELLS);
			Set<ManagedOperation> magicSpellsFound = findMagicSpells(world);
			missingClericSpells.removeAll(magicSpellsFound);
			
			for(MagicSpell missingClericSpell : missingClericSpells) {
				SkillProperty skillPropery = missingClericSpell.getSkill();
				WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
				List<ManagedOperation> clericSpells = Arrays.asList(CLERIC_SPELLS.get(0));
				
				if (performer.getProperty(skillPropery).getLevel() < missingClericSpell.getRequiredSkillLevel()) {
					return new OperationInfo(performer, library, new int[0], Actions.RESEARCH_RELIGION_SKILL_ACTION);
				} else if (!performer.getProperty(Constants.KNOWN_SPELLS).contains(missingClericSpell)) {
					return new OperationInfo(performer, library, new int[0], Actions.getResearchSpellActionFor(Actions.MINOR_HEAL_ACTION));
				} else if (performerInventory.getWorldObjects(Constants.KNOWN_SPELLS, clericSpells).size() == 0 && performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.PAPER) < 5) {
					return new PaperGoal().calculateGoal(performer, world);
				} else if (performerInventory.getWorldObjects(Constants.KNOWN_SPELLS, clericSpells).size() == 0) {
					return new OperationInfo(performer, performer, new int[0], Actions.getScribeMagicSpellActionFor(Actions.MINOR_HEAL_ACTION));
				} else if (performerInventory.getWorldObjects(Constants.KNOWN_SPELLS, clericSpells).size() > 0) {
					int indexOfSpellBook = performerInventory.getIndexFor(Constants.KNOWN_SPELLS, clericSpells);
					return new OperationInfo(performer, library, new int[] {indexOfSpellBook}, Actions.PUT_ITEM_INTO_INVENTORY_ACTION);
				}
			}
		} else {
			return new LibraryGoal().calculateGoal(performer, world);
		}
		
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		Set<ManagedOperation> magicSpellsFound = findMagicSpells(world);
		return magicSpellsFound.size() == CLERIC_SPELLS.size();
	}
	
	private Set<ManagedOperation> findMagicSpells(World world) {
		List<WorldObject> libraries = getLibraries(world);
		Set<ManagedOperation> magicSpellsFound = new HashSet<>();
		
		for(WorldObject library : libraries) {
			List<WorldObject> spellBooks = library.getProperty(Constants.INVENTORY).getWorldObjects(Constants.KNOWN_SPELLS, Arrays.asList(CLERIC_SPELLS.get(0)));
			for(WorldObject spellBook : spellBooks) {
				magicSpellsFound.add(spellBook.getProperty(Constants.KNOWN_SPELLS).get(0));
			}
		}
		
		return magicSpellsFound;
	}

	private List<WorldObject> getLibraries(World world) {
		List<WorldObject> libraries = world.findWorldObjects(w -> w.hasProperty(Constants.LIBRARY_QUALITY));
		return libraries;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "scribing cleric spells";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		Set<ManagedOperation> magicSpellsFound = findMagicSpells(world);
		return magicSpellsFound.size();
	}
}
