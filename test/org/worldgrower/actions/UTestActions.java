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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.actions.magic.ResearchSpellAction;
import org.worldgrower.actions.magic.ScribeMagicSpellAction;

public class UTestActions {

	@Test
	public void testGetResearchSpellActionFor() {
		ResearchSpellAction researchSpellAction = Actions.getResearchSpellActionFor(Actions.MINOR_HEAL_ACTION);
		assertEquals(Actions.MINOR_HEAL_ACTION, researchSpellAction.getSpell());
		assertEquals(ResearchSpellAction.class, researchSpellAction.getClass());
	}
	
	@Test
	public void testgetScribeMagicSpellActionFor() {
		ScribeMagicSpellAction researchSpellAction = Actions.getScribeMagicSpellActionFor(Actions.MINOR_HEAL_ACTION);
		assertEquals(Actions.MINOR_HEAL_ACTION, researchSpellAction.getSpell());
		assertEquals(ScribeMagicSpellAction.class, researchSpellAction.getClass());
	}
	
	@Test
	public void testGetMagicSpellsToResearch() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.KNOWN_SPELLS, new ArrayList<>());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		List<MagicSpell> magicSpells = Actions.getMagicSpellsToResearch(performer);
		assertEquals(true, magicSpells.size() > 0);
	}
	
	@Test
	public void testgetResearchKnowledgeSkillActionFor() {
		ResearchKnowledgeSkillAction researchKnowledgeSkillAction = Actions.getResearchKnowledgeSkillActionFor(Constants.RESTORATION_SKILL);
		assertEquals(Constants.RESTORATION_SKILL, researchKnowledgeSkillAction.getSkillProperty());
	}
}