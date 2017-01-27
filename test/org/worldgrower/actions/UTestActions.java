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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.actions.magic.ResearchSpellAction;
import org.worldgrower.actions.magic.ScribeMagicSpellAction;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.GroupPropertyUtils;

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
	
	@Test
	public void testGetDeadlyActionDescriptions() {
		assertEquals(true, Actions.getDeadlyActionDescriptions().size() > 0);
	}
	
	@Test
	public void testIsMutuallyAgreedAction() {
		assertEquals(true, Actions.isMutuallyAgreedAction(Actions.BUY_ACTION));
		assertEquals(true, Actions.isMutuallyAgreedAction(Actions.SELL_ACTION));
		assertEquals(false, Actions.isMutuallyAgreedAction(Actions.MELEE_ATTACK_ACTION));
	}
	
	@Test
	public void testSortActionsByDescription() {
		List<ManagedOperation> actions = Arrays.asList(Actions.WORSHIP_DEITY_ACTION, Actions.ANIMATE_DEAD_ACTION);
		Actions.sortActionsByDescription(actions);
		
		assertEquals(Actions.ANIMATE_DEAD_ACTION, actions.get(0));
		assertEquals(Actions.WORSHIP_DEITY_ACTION, actions.get(1));
	}
	
	@Test
	public void testGetScribeMagicSpellActions() {
		Map<SkillProperty, List<ManagedOperation>> scribeMagicSpellActions = Actions.getScribeMagicSpellActions();
		
		assertEquals(true, scribeMagicSpellActions.containsKey(Constants.EVOCATION_SKILL));
		
		List<ManagedOperation> scribeEvocationSpells = scribeMagicSpellActions.get(Constants.EVOCATION_SKILL);
		assertEquals("scribe spell 'cast lock'", scribeEvocationSpells.get(0).getSimpleDescription());
		assertEquals("scribe spell 'cast silence'", scribeEvocationSpells.get(1).getSimpleDescription());
		assertEquals("scribe spell 'cast unlock'", scribeEvocationSpells.get(2).getSimpleDescription());
		assertEquals("scribe spell 'create secret chest'", scribeEvocationSpells.get(3).getSimpleDescription());
	}
	
	@Test
	public void testSortSkillProperties() {
		List<SkillProperty> skillProperties = Arrays.asList(Constants.ILLUSION_SKILL, Constants.EVOCATION_SKILL);
		skillProperties = Actions.sortSkillProperties(skillProperties);
		
		assertEquals(2, skillProperties.size());
		assertEquals(Constants.EVOCATION_SKILL, skillProperties.get(0));
		assertEquals(Constants.ILLUSION_SKILL, skillProperties.get(1));
	}
	
	@Test
	public void testGetSortedSkillProperties() {
		Map<SkillProperty, List<ManagedOperation>> scribeMagicSpellActions = Actions.getScribeMagicSpellActions();
		List<SkillProperty> skillProperties = Actions.getSortedSkillProperties(scribeMagicSpellActions);
		
		assertEquals(Constants.ENCHANTMENT_SKILL, skillProperties.get(0));
		assertEquals(Constants.EVOCATION_SKILL, skillProperties.get(1));
	}
	
	@Test
	public void testGetActionsWithTargetPropertyApothecary() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		createVillagersOrganization(world);
		
		List<ManagedOperation> actions = Actions.getActionsWithTargetProperty(performer, Constants.APOTHECARY_QUALITY, world);
		assertEquals(Arrays.asList(Actions.BREW_POISON_ACTION, Actions.BREW_SLEEPING_POTION_ACTION, Actions.BREW_HEALING_POTION_ACTION, Actions.BREW_CURE_POISON_POTION_ACTION, Actions.BREW_CURE_DISEASE_POTION_ACTION, Actions.BREW_CHANGE_GENDER_POTION_ACTION), actions);
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		return performer;
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		return villagersOrganization;
	}
}