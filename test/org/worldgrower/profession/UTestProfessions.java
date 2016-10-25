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
package org.worldgrower.profession;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageIds;

public class UTestProfessions {

	@Test
	public void testGetProfessionByDescription() {
		assertEquals(Professions.FARMER_PROFESSION, Professions.getProfessionByDescription("farmer"));
	}
	
	@Test
	public void testGetDescriptions() {
		assertEquals("alchemist", Professions.getDescriptions().get(0));
	}
	
	@Test
	public void testGetImageIds() {
		List<Profession> professions = Arrays.asList(Professions.FARMER_PROFESSION);
		assertEquals(Arrays.asList(ImageIds.SCYTHE), Professions.getImageIds(professions));
	}
	
	@Test
	public void testGetProfessionCount() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject target1 = TestUtils.createIntelligentWorldObject(2, Constants.CAN_ATTACK_CRIMINALS, true);
		world.addWorldObject(target1);
		WorldObject target2 = TestUtils.createIntelligentWorldObject(3, Constants.CAN_COLLECT_TAXES, true);
		world.addWorldObject(target2);
		
		assertEquals(1, Professions.getProfessionCount(world, Constants.CAN_ATTACK_CRIMINALS));
	}
}