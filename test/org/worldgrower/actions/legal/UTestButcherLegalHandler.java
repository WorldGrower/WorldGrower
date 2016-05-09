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
package org.worldgrower.actions.legal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Skill;

public class UTestButcherLegalHandler {

	private ButcherLegalHandler legalHandler = new ButcherLegalHandler();
	
	@Test
	public void testIsApplicable() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INSIGHT_SKILL, new Skill(10));
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.INSIGHT_SKILL, new Skill(10));
		
		assertEquals(false, legalHandler.isApplicable(performer, target, Args.EMPTY));
		
		target.setProperty(Constants.CATTLE_OWNER_ID, 1);
		assertEquals(false, legalHandler.isApplicable(performer, target, Args.EMPTY));
		
		target.setProperty(Constants.CATTLE_OWNER_ID, null);
		assertEquals(false, legalHandler.isApplicable(performer, target, Args.EMPTY));
		
		target.setProperty(Constants.CATTLE_OWNER_ID, 7);
		assertEquals(true, legalHandler.isApplicable(performer, target, Args.EMPTY));
	}
}
