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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.MockCommonerGenerator;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Skill;
import org.worldgrower.generator.CommonerGenerator;

public class UTestIllusionPropertyUtils {

	private final CommonerGenerator commonerGenerator = new MockCommonerGenerator();
	
	@Test
	public void testEveryoneInVicinityKnowsOfIllusionSelf() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.ILLUSION_SKILL, new Skill(20));
		performer.setProperty(Constants.INSIGHT_SKILL, new Skill(0));
		
		WorldObject illusion = createCommoner(world, organization);
		illusion.setProperty(Constants.ILLUSION_CREATOR_ID, 0);
		
		IllusionPropertyUtils.everyoneInVicinityKnowsOfIllusion(performer, world, illusion);
		
		assertEquals(true, performer.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(illusion, Constants.ILLUSION_CREATOR_ID));
	}
	
	@Test
	public void testEveryoneInVicinityKnowsOfIllusionObserver() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.ILLUSION_SKILL, new Skill(20));
		
		WorldObject observer = createCommoner(world, organization);
		observer.setProperty(Constants.INSIGHT_SKILL, new Skill(0));

		WorldObject observer2 = createCommoner(world, organization);
		observer2.setProperty(Constants.INSIGHT_SKILL, new Skill(20));
		
		WorldObject illusion = createCommoner(world, organization);
		illusion.setProperty(Constants.ILLUSION_CREATOR_ID, performer.getProperty(Constants.ID));
		
		IllusionPropertyUtils.everyoneInVicinityKnowsOfIllusion(performer, world, illusion);
		
		assertEquals(false, observer.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(illusion, Constants.ILLUSION_CREATOR_ID));
		assertEquals(true, observer2.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(illusion, Constants.ILLUSION_CREATOR_ID));
	}
	
	@Test
	public void testCreateIllusion() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.ILLUSION_SKILL, new Skill(20));
		
		WorldObject verminOrganization = GroupPropertyUtils.create(null, "", world);
		verminOrganization.setProperty(Constants.ID, 2);
		world.addWorldObject(verminOrganization);
		
		int illusionId = IllusionPropertyUtils.createIllusion(performer, -1000, world, 0, 0, 1, 1);
		WorldObject illusion = world.findWorldObjectById(illusionId);
		assertEquals("berry bush", illusion.getProperty(Constants.NAME));
	}
	
	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject commoner = world.findWorldObjectById(commonerId);
		return commoner;
	}
}
