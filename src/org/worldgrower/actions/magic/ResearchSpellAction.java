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
package org.worldgrower.actions.magic;

import java.io.ObjectStreamException;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class ResearchSpellAction implements ManagedOperation {

	private static final int DISTANCE = 1;
	private final MagicSpell spell;
	
	public ResearchSpellAction(MagicSpell spell) {
		this.spell = spell;
	}

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int researchBonus = target.getProperty(Constants.LIBRARY_QUALITY);
		performer.getProperty(Constants.STUDYING_SPELLS).add(spell, researchBonus);

		if (performer.getProperty(Constants.STUDYING_SPELLS).count(spell) > spell.getResearchCost()) {
			performer.getProperty(Constants.KNOWN_SPELLS).add(spell);
			
			String message = performer.getProperty(Constants.NAME) + " has learned spell '" + spell.getSimpleDescription() + "'";
			world.logAction(this, performer, target, args, message);
		}
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean skillLevelMet = (spell.getSkill().getLevel(performer) >= spell.getRequiredSkillLevel());
		return skillLevelMet;
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(spell.getSkill(), spell.getRequiredSkillLevel(), Constants.DISTANCE, DISTANCE);
	}
	
	@Override
	public String getDescription() {
		return "research magic spell";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return isValidTarget(target) && !performer.getProperty(Constants.KNOWN_SPELLS).contains(spell);
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

	public MagicSpell getSpell() {
		return spell;
	}
	
	public static boolean isValidTarget(WorldObject target) {
		return target.hasProperty(Constants.LIBRARY_QUALITY);
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.SPELL_BOOK;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.BOOK_FLIP;
	}
}