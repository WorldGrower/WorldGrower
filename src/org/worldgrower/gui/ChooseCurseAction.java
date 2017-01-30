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
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.curse.Curse;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.util.ListData;
import org.worldgrower.gui.util.ListInputDialog;

public class ChooseCurseAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	private JFrame parentFrame;
	
	public ChooseCurseAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, WorldPanel parent, DungeonMaster dungeonMaster, JFrame parentFrame) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] curseNames = map(c -> c.getName());
		String[] tooltips = map(c -> c.getDescription());
		ListData listData = new ListData(curseNames, tooltips, Curse.getBestowableCurseImageIds(), imageInfoReader);
		String curseName = new ListInputDialog("Choose Curse", listData, imageInfoReader, soundIdReader, parentFrame).showMe();
		if (curseName != null) {
			int indexOfCurse = Arrays.asList(curseNames).indexOf(curseName);
			
			Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, Actions.BESTOW_CURSE_ACTION, new int[] { indexOfCurse }, world, dungeonMaster, playerCharacter, parent, imageInfoReader, soundIdReader);
		}
	}
	
	private String[] map(Function<Curse, String> mapFunction) {
		return Curse.BESTOWABLE_CURSES.stream().map(mapFunction).collect(Collectors.toList()).toArray(new String[0]);
	}
}