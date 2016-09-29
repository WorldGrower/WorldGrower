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
package org.worldgrower.condition;

import java.util.List;

import org.worldgrower.ManagedOperation;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.GovernanceOption;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.creaturetype.CreatureType;

public interface WorldStateChangedListener {

	public void creatureTypeChange(WorldObject worldObject, CreatureType newCreatureType, String description);
	public void electionStarted(WorldObject organization);
	public void electionFinished(WorldObject winner, WorldObject organization, IdList candidates);
	public void governanceChanged(List<LegalAction> changedLegalActions, List<GovernanceOption> changedGovernanceOptions, WorldObject villagerLeader);
	public void thrownOutOfGroup(WorldObject worldObject, WorldObject target, int[] args, ManagedOperation action, IdList oldGroup, IdList newGroup);
	public void skillIncreased(WorldObject worldObject, SkillProperty skillProperty, int oldValue, int newValue);
	public void conditionGained(WorldObject worldObject, Condition condition);
	public void conditionLost(WorldObject worldObject, Condition condition);
	public void levelIncreased(WorldObject worldObject, int newValue);
	public void lostLeadership(WorldObject worldObject, WorldObject organization);
	public void skillsDeteriorated(WorldObject worldObject);
	public void fireAssetsSeized(WorldObject worldObject, List<Integer> buildingIds);
}