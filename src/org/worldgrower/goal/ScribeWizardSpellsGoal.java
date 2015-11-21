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

public class ScribeWizardSpellsGoal extends AbstractScribeSpellsGoal {

	private static final List<MagicSpell> WIZARD_SPELLS = Arrays.asList(Actions.FIRE_BOLT_ATTACK_ACTION, Actions.RAY_OF_FROST_ATTACK_ACTION);
	
	public ScribeWizardSpellsGoal() {
		super(WIZARD_SPELLS, Actions.RESEARCH_EVOCATION_SKILL_ACTION);
	}

	public ScribeWizardSpellsGoal(List<Goal> allGoals) {
		this();
		allGoals.add(this);
	}
}
