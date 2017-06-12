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
package org.worldgrower.deity;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.worldgrower.deity.DeityAttributes.WorshipActionStatistics;

public class UTestDeityAttributes {

	@Test
	public void testCalculateHapinessDeltaNoWorshippers() {
		DeityAttributes deityAttributes = new DeityAttributes();
		
		Map<Deity, Integer> worshippersByDeity = new HashMap<>();
		worshippersByDeity.put(Deity.ARES, 0);
		int totalNumberOfWorshippers = 0;
		WorshipActionStatistics worshipActionStatistics = new WorshipActionStatistics();
		
		assertEquals(0, deityAttributes.calculateHapinessDelta(worshippersByDeity, totalNumberOfWorshippers, worshipActionStatistics, Deity.ARES));
	}
	
	@Test
	public void testCalculateHapinessDeltaOneWorshipper() {
		DeityAttributes deityAttributes = new DeityAttributes();
		
		Map<Deity, Integer> worshippersByDeity = new HashMap<>();
		worshippersByDeity.put(Deity.ARES, 1);
		int totalNumberOfWorshippers = 1;
		WorshipActionStatistics worshipActionStatistics = new WorshipActionStatistics();
		
		assertEquals(1, deityAttributes.calculateHapinessDelta(worshippersByDeity, totalNumberOfWorshippers, worshipActionStatistics, Deity.ARES));
	}
	
	@Test
	public void testCalculateHapinessDeltaOneWorshipperTwelveTotal() {
		DeityAttributes deityAttributes = new DeityAttributes();
		
		Map<Deity, Integer> worshippersByDeity = new HashMap<>();
		worshippersByDeity.put(Deity.ARES, 1);
		int totalNumberOfWorshippers = 12;
		WorshipActionStatistics worshipActionStatistics = new WorshipActionStatistics();
		
		assertEquals(0, deityAttributes.calculateHapinessDelta(worshippersByDeity, totalNumberOfWorshippers, worshipActionStatistics, Deity.ARES));
	}
	
	@Test
	public void testCalculateHapinessDeltaOneWorshipperThirteenTotal() {
		DeityAttributes deityAttributes = new DeityAttributes();
		
		Map<Deity, Integer> worshippersByDeity = new HashMap<>();
		worshippersByDeity.put(Deity.ARES, 1);
		int totalNumberOfWorshippers = 13;
		WorshipActionStatistics worshipActionStatistics = new WorshipActionStatistics();
		
		assertEquals(-1, deityAttributes.calculateHapinessDelta(worshippersByDeity, totalNumberOfWorshippers, worshipActionStatistics, Deity.ARES));
	}
	
	@Test
	public void testCalculateHapinessDeltaNoWorshipActions() {
		DeityAttributes deityAttributes = new DeityAttributes();
		
		Map<Deity, Integer> worshippersByDeity = new HashMap<>();
		worshippersByDeity.put(Deity.ARES, 1);
		int totalNumberOfWorshippers = 12;
		WorshipActionStatistics worshipActionStatistics = new WorshipActionStatistics();
		
		assertEquals(0, deityAttributes.calculateHapinessDelta(worshippersByDeity, totalNumberOfWorshippers, worshipActionStatistics, Deity.ARES));
	}
	
	@Test
	public void testCalculateHapinessDeltaOneWorshipActions() {
		DeityAttributes deityAttributes = new DeityAttributes();
		
		Map<Deity, Integer> worshippersByDeity = new HashMap<>();
		worshippersByDeity.put(Deity.ARES, 1);
		int totalNumberOfWorshippers = 12;
		WorshipActionStatistics worshipActionStatistics = new WorshipActionStatistics();
		worshipActionStatistics.incrementWorshipCount(Deity.ARES);
		worshipActionStatistics.setTotalWorshipActions(1);
		
		assertEquals(30, deityAttributes.calculateHapinessDelta(worshippersByDeity, totalNumberOfWorshippers, worshipActionStatistics, Deity.ARES));
	}
	
	@Test
	public void testCalculateHapinessDeltaTwoWorshipActions() {
		DeityAttributes deityAttributes = new DeityAttributes();
		
		Map<Deity, Integer> worshippersByDeity = new HashMap<>();
		worshippersByDeity.put(Deity.ARES, 1);
		int totalNumberOfWorshippers = 12;
		WorshipActionStatistics worshipActionStatistics = new WorshipActionStatistics();
		worshipActionStatistics.incrementWorshipCount(Deity.ARES);
		worshipActionStatistics.incrementWorshipCount(Deity.ARES);
		worshipActionStatistics.setTotalWorshipActions(2);
		
		assertEquals(30, deityAttributes.calculateHapinessDelta(worshippersByDeity, totalNumberOfWorshippers, worshipActionStatistics, Deity.ARES));
	}
	
	@Test
	public void testCalculateHapinessDeltaOneWorshipOneOtherActions() {
		DeityAttributes deityAttributes = new DeityAttributes();
		
		Map<Deity, Integer> worshippersByDeity = new HashMap<>();
		worshippersByDeity.put(Deity.ARES, 1);
		int totalNumberOfWorshippers = 12;
		WorshipActionStatistics worshipActionStatistics = new WorshipActionStatistics();
		worshipActionStatistics.incrementWorshipCount(Deity.ARES);
		worshipActionStatistics.incrementWorshipCount(Deity.HADES);
		worshipActionStatistics.setTotalWorshipActions(2);
		
		assertEquals(15, deityAttributes.calculateHapinessDelta(worshippersByDeity, totalNumberOfWorshippers, worshipActionStatistics, Deity.ARES));
	}
}
