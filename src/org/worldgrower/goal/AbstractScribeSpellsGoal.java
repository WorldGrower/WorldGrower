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
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.WorldObjectContainer;

public abstract class AbstractScribeSpellsGoal implements Goal {

	private final List<MagicSpell> magicSpellsForScribing;
	private final ManagedOperation researchSkillAction;
	
	public AbstractScribeSpellsGoal(List<MagicSpell> magicSpells, ManagedOperation researchSkillAction) {
		this.magicSpellsForScribing = magicSpells;
		this.researchSkillAction = researchSkillAction;
	}

	@Override
	public final OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> libraries = getLibraries(world);
		if (libraries.size() > 0) {
			WorldObject library = libraries.get(0);
			
			List<MagicSpell> missingSpells = new ArrayList<>(magicSpellsForScribing);
			Set<ManagedOperation> magicSpellsFound = findMagicSpells(world);
			missingSpells.removeAll(magicSpellsFound);
			
			for(MagicSpell missingSpell : missingSpells) {
				SkillProperty skillPropery = missingSpell.getSkill();
				WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
				List<ManagedOperation> spells = Arrays.asList(magicSpellsForScribing.get(0));
				List<WorldObject> knownSpellsInInventory = performerInventory.getWorldObjects(Constants.KNOWN_SPELLS, spells);
				int performerPaperQuantity = performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.PAPER);
				
				if (performer.getProperty(skillPropery).getLevel() < missingSpell.getRequiredSkillLevel()) {
					return new OperationInfo(performer, library, new int[0], researchSkillAction);
				} else if (!performer.getProperty(Constants.KNOWN_SPELLS).contains(missingSpell)) {
					return new OperationInfo(performer, library, new int[0], Actions.getResearchSpellActionFor(missingSpell));
				} else if (knownSpellsInInventory.size() == 0 && performerPaperQuantity < 5) {
					return new PaperGoal().calculateGoal(performer, world);
				} else if (knownSpellsInInventory.size() == 0) {
					return new OperationInfo(performer, performer, new int[0], Actions.getScribeMagicSpellActionFor(missingSpell));
				} else if (knownSpellsInInventory.size() > 0) {
					int indexOfSpellBook = performerInventory.getIndexFor(Constants.KNOWN_SPELLS, spells);
					return new OperationInfo(performer, library, new int[] {indexOfSpellBook}, Actions.PUT_ITEM_INTO_INVENTORY_ACTION);
				}
			}
		} else {
			return new LibraryGoal().calculateGoal(performer, world);
		}
		
		return null;
	}
	
	@Override
	public final void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public final boolean isGoalMet(WorldObject performer, World world) {
		Set<ManagedOperation> magicSpellsFound = findMagicSpells(world);
		return magicSpellsFound.size() == magicSpellsForScribing.size();
	}
	
	private Set<ManagedOperation> findMagicSpells(World world) {
		List<WorldObject> libraries = getLibraries(world);
		Set<ManagedOperation> magicSpellsFound = new HashSet<>();
		
		for(WorldObject library : libraries) {
			List<WorldObject> spellBooks = library.getProperty(Constants.INVENTORY).getWorldObjects(Constants.KNOWN_SPELLS, Arrays.asList(magicSpellsForScribing.get(0)));
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
	public final boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public final String getDescription() {
		return "scribing spells";
	}

	@Override
	public final int evaluate(WorldObject performer, World world) {
		Set<ManagedOperation> magicSpellsFound = findMagicSpells(world);
		return magicSpellsFound.size();
	}
}
