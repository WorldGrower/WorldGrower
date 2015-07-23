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

import java.io.ObjectStreamException;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class ResearchSpellAction implements ManagedOperation {

	private final MagicSpell spell;
	
	public ResearchSpellAction(MagicSpell spell) {
		this.spell = spell;
	}

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		performer.getProperty(Constants.STUDYING_SPELLS).add(spell.getSkill(), 1);

		if (performer.getProperty(Constants.STUDYING_SPELLS).count(spell.getSkill()) > spell.getResearchCost()) {
			performer.getProperty(Constants.KNOWN_SPELLS).add(spell);
		}
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.LIBRARY_QUALITY) && !performer.getProperty(Constants.KNOWN_SPELLS).contains(spell) && (performer.getProperty(spell.getSkill()).getLevel() >= spell.getRequiredSkillLevel());
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "studying spell '" + spell.getSimpleDescription() + "'";
	}

	@Override
	public String getSimpleDescription() {
		return "study spell '" + spell.getSimpleDescription() + "'";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}