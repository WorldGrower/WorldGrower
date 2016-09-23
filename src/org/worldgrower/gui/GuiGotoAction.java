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
package org.worldgrower.gui;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.Reach;
import org.worldgrower.TaskCalculator;
import org.worldgrower.TaskCalculatorImpl;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;

public class GuiGotoAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	private JFrame parentFrame;
	private final int destinationX;
	private final int destinationY;
	
	public GuiGotoAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, WorldPanel parent, DungeonMaster dungeonMaster, JFrame parentFrame, int destinationX, int destinationY) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
		this.parentFrame = parentFrame;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<OperationInfo> tasks = calculatePath(playerCharacter, destinationX, destinationY, world);
		if (tasks.size() > 0) {
			tasks = tasks.subList(0, tasks.size() - 1);
			Game.executeMultipleActionsAndMoveIntelligentWorldObjects(playerCharacter, tasks, world, dungeonMaster, playerCharacter, parent, soundIdReader);
		}
	}
	
	public static List<OperationInfo> calculatePath(WorldObject playerCharacter, int destinationX, int destinationY, World world) {
		TaskCalculator taskCalculator = new TaskCalculatorImpl();
		OperationInfo goal = new OperationInfo(playerCharacter, playerCharacter, Args.EMPTY, new GotoAction(destinationX, destinationY));
		List<OperationInfo> tasks = taskCalculator.calculateTask(playerCharacter, world, goal);
		return tasks;
	}
	
	private static class GotoAction implements ManagedOperation {
		private final WorldObject destinationTarget;

		public GotoAction(int destinationX, int destinationY) {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.X, destinationX);
			properties.put(Constants.Y, destinationY);
			properties.put(Constants.WIDTH, 1);
			properties.put(Constants.HEIGHT, 1);
			destinationTarget = new WorldObjectImpl(properties);
		}

		@Override
		public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		}

		@Override
		public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
			return true;
		}

		@Override
		public boolean requiresArguments() {
			return false;
		}

		@Override
		public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
			return true;
		}

		@Override
		public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
			int performerX = performer.getProperty(Constants.X).intValue();
			int performerY = performer.getProperty(Constants.Y).intValue();
			int destinationTargetX = destinationTarget.getProperty(Constants.X).intValue();
			int destinationTargetY = destinationTarget.getProperty(Constants.Y).intValue();
			if (performerX == destinationTargetX && performerY == destinationTargetY) {
				return 0;
			} else {
				return Reach.evaluateTarget(performer, null, destinationTarget, 1) + 1;
			}
		}

		@Override
		public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
			return null;
		}

		@Override
		public String getSimpleDescription() {
			return null;
		}

		@Override
		public String getRequirementsDescription() {
			return null;
		}
		
		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public ImageIds getImageIds() {
			return null;
		}		
	}
}