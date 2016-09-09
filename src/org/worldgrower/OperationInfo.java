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
package org.worldgrower;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.worldgrower.actions.Actions;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.goal.KnowledgeMapPropertyUtils;
import org.worldgrower.history.HistoryItem;

/**
 * An OperationInfo instance holds all the state necessary to execute a ManagedOperation.
 */
public class OperationInfo implements Serializable {
	private final WorldObject performer;
	private final WorldObject target;
	private final int[] args;
	private final ManagedOperation managedOperation;
	
	public OperationInfo(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation) {
		if (performer == null) {
			throw new IllegalArgumentException("performer is null");
		}
		if (target == null) {
			throw new IllegalArgumentException("target is null");
		}
		if (args == null) {
			throw new IllegalArgumentException("args is null");
		}
		if (managedOperation == null) {
			throw new IllegalArgumentException("managedOperation is null");
		}
		
		this.performer = performer;
		this.target = target;
		this.args = args;
		this.managedOperation = managedOperation;
	}

	public int distance(WorldObject performer, World world) {
		return managedOperation.distance(performer, target, args, world);
	}

	public boolean isValidTarget(World world) {
		return managedOperation.isValidTarget(performer, target, world);
	}
	
	public void perform(World world) {
		boolean actionCanAngerOthers = actionCanAngerOthers();
		
		GoalChangedCalculator goalChangedCalculator = new GoalChangedCalculator(new DefaultGoalObstructedHandler());
		if (actionCanAngerOthers) {
			goalChangedCalculator.recordStartState(performer, target, world);
		}
		
		performImpl(world);
		
		if (actionCanAngerOthers) {
			goalChangedCalculator.recordEndState(performer, target, managedOperation, args, world);
			goalChangedCalculator.checkLegality(performer, target, managedOperation, args, world);
		}
	}

	private void performImpl(World world) {
		boolean targetIsIllusion = target.hasProperty(Constants.ILLUSION_CREATOR_ID);
		final WorldObject actualPerformer;
		if (targetIsIllusion) {
			actualPerformer = new ImmutableWorldObject(performer, Arrays.asList(Constants.ENERGY), new DoNothingOnTurn());
		} else {
			actualPerformer = performer;
		}
		
		managedOperation.execute(actualPerformer, target, args, world);
		if (targetIsIllusion) {
			KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfProperty(performer, target, Constants.ILLUSION_CREATOR_ID, target.getProperty(Constants.ILLUSION_CREATOR_ID), world);
			performer.setProperty(Constants.ENERGY, actualPerformer.getProperty(Constants.ENERGY));
		}
		HistoryItem historyItem = world.getHistory().actionPerformed(this, world.getCurrentTurn());
		
		if (actionCanKillOthers()) {
			world.removeDeadWorldObjects();
		}
	}
	
	private boolean actionCanAngerOthers() {
		return managedOperation != Actions.DO_NOTHING_ACTION
				&& managedOperation != Actions.REST_ACTION
				&& managedOperation != Actions.SLEEP_ACTION
				&& managedOperation != Actions.MOVE_ACTION
				&& managedOperation != Actions.TALK_ACTION;
	}
	
	private boolean actionCanKillOthers() {
		return managedOperation != Actions.DO_NOTHING_ACTION
				&& managedOperation != Actions.REST_ACTION
				&& managedOperation != Actions.SLEEP_ACTION
				&& managedOperation != Actions.MOVE_ACTION
				&& managedOperation != Actions.TALK_ACTION
				&& managedOperation != Actions.SEX_ACTION;
	}
	
	@Override
	public String toString() {
		return "OperationInfo [performer=" + performer + ", target=" + target
				+ ", args=" + Arrays.toString(args) + ", managedOperation="
				+ managedOperation + "]";
	}
	
	public String toShortString() {
		return "[" + managedOperation.getClass().getSimpleName() + "(" + Arrays.toString(args) + ")]";
	}
	
	public String getDescription(World world) {
		return managedOperation.getDescription(performer, target, args, world);
	}
	
	public boolean evaluate(OperationInfoEvaluator operationInfoEvaluator) {
		return operationInfoEvaluator.evaluate(performer, target, args, managedOperation);
	}
	
	public boolean isPerformer(WorldObject performer) {
		return (this.performer.equals(performer));
	}

	public WorldObject getPerformer() {
		return performer;
	}
	
	public WorldObject getTarget() {
		return target;
	}

	public ManagedOperation getManagedOperation() {
		return managedOperation;
	}

	public String getSecondPersonDescription(World world) {
		return "You were " + managedOperation.getDescription(performer, target, args, world);
	}
	
	public boolean targetMoved(World world) {
		if (target.hasIntelligence() && !performer.equals(target)) {
			OperationInfo lastPerformedOperation = world.getHistory().getLastPerformedOperation(target);
			if (lastPerformedOperation != null) {
				return (lastPerformedOperation.managedOperation == Actions.MOVE_ACTION);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean canExecute(WorldObject performer, World world) {
		return (canExecuteIgnoringDistance(performer, world)) 
				&& (distance(performer, world) == 0);
	}
	
	public boolean canExecuteIgnoringDistance(WorldObject performer, World world) {
		return (isValidTarget(world)) 
				&& isActionPossible(performer, world)
				&& performer.canWorldObjectPerformAction(managedOperation);
	}

	public boolean isActionPossible(WorldObject performer, World world) {
		return managedOperation.isActionPossible(performer, target, args, world);
	}

	public OperationInfo copy() {
		return new OperationInfo(performer.deepCopy(), target.deepCopy(), args, managedOperation);
	}

	public boolean firstArgsIs(int i) {
		return args[0] == i;
	}

	public int[] getArgs() {
		return args;
	}
}
