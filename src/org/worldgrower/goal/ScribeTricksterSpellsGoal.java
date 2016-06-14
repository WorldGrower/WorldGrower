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

import java.util.Arrays;
import java.util.List;

import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;

public class ScribeTricksterSpellsGoal extends AbstractScribeSpellsGoal {

	private static final List<MagicSpell> TRICKSTER_SPELLS = Arrays.asList(Actions.DISGUISE_MAGIC_SPELL_ACTION, Actions.MINOR_ILLUSION_ACTION);
	
	public ScribeTricksterSpellsGoal() {
		super(TRICKSTER_SPELLS, Actions.RESEARCH_ILLUSION_SKILL_ACTION);
	}

	public ScribeTricksterSpellsGoal(List<Goal> allGoals) {
		this();
		allGoals.add(this);
	}
}
