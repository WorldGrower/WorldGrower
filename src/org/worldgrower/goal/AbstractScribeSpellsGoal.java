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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public abstract class AbstractScribeSpellsGoal implements Goal {

	private final List<MagicSpell> magicSpellsForScribing;
	private final ManagedOperation researchSkillAction;
	
	public AbstractScribeSpellsGoal(List<MagicSpell> magicSpells, ManagedOperation researchSkillAction) {
		this.magicSpellsForScribing = magicSpells;
		this.researchSkillAction = researchSkillAction;
	}

	@Override
	public final OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject library = LibraryUtils.getLibraryFor(performer, world);
		if (library != null) {
			List<MagicSpell> missingSpells = new ArrayList<>(magicSpellsForScribing);
			Set<ManagedOperation> magicSpellsFound = findMagicSpells(performer, world);
			missingSpells.removeAll(magicSpellsFound);
			
			for(MagicSpell missingSpell : missingSpells) {
				SkillProperty skillPropery = missingSpell.getSkill();
				WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
				List<WorldObject> knownSpellsInInventory = getKnownSpellsInInventory(performer, missingSpell);
				int performerPaperQuantity = performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.PAPER);
				
				if (skillPropery.getLevel(performer) < missingSpell.getRequiredSkillLevel()) {
					return new OperationInfo(performer, library, Args.EMPTY, researchSkillAction);
				} else if (!performer.getProperty(Constants.KNOWN_SPELLS).contains(missingSpell)) {
					return new OperationInfo(performer, library, Args.EMPTY, Actions.getResearchSpellActionFor(missingSpell));
				} else if (knownSpellsInInventory.size() == 0 && performerPaperQuantity < 5) {
					return Goals.PAPER_GOAL.calculateGoal(performer, world);
				} else if (knownSpellsInInventory.size() == 0) {
					return new OperationInfo(performer, library, Args.EMPTY, Actions.getScribeMagicSpellActionFor(missingSpell));
				} else if (knownSpellsInInventory.size() > 0) {
					int indexOfSpellBook = performerInventory.getIndexFor(Constants.MAGIC_SPELL, missingSpell);
					return new OperationInfo(performer, library, new int[] {indexOfSpellBook}, Actions.PUT_ITEM_INTO_INVENTORY_ACTION);
				}
			}
		} else {
			return Goals.LIBRARY_GOAL.calculateGoal(performer, world);
		}
		
		return null;
	}

	static List<WorldObject> getKnownSpellsInInventory(WorldObject performer, ManagedOperation spell) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		List<WorldObject> knownSpellsInInventory = performerInventory.getWorldObjects(Constants.MAGIC_SPELL, spell);
		return knownSpellsInInventory;
	}
	
	@Override
	public final void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public final boolean isGoalMet(WorldObject performer, World world) {
		Set<ManagedOperation> magicSpellsFound = findMagicSpells(performer, world);
		return magicSpellsFound.size() == magicSpellsForScribing.size();
	}
	
	private Set<ManagedOperation> findMagicSpells(WorldObject performer, World world) {
		List<WorldObject> libraries = LibraryUtils.getLibraries(performer, world);
		Set<ManagedOperation> magicSpellsFound = new HashSet<>();
		
		for(WorldObject library : libraries) {
			for(MagicSpell magicSpell : magicSpellsForScribing) {
				List<WorldObject> spellBooks = library.getProperty(Constants.INVENTORY).getWorldObjects(Constants.MAGIC_SPELL, magicSpell);
				for(WorldObject spellBook : spellBooks) {
					magicSpellsFound.add(spellBook.getProperty(Constants.MAGIC_SPELL));
				}
			}
		}
		
		return magicSpellsFound;
	}

	@Override
	public final boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public final FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_SCRIBE_SPELLS);
	}

	@Override
	public final int evaluate(WorldObject performer, World world) {
		Set<ManagedOperation> magicSpellsFound = findMagicSpells(performer, world);
		return magicSpellsFound.size();
	}
}
