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

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.music.MusicPlayer;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.start.KeyBindings;
import org.worldgrower.gui.util.ShowTextDialog;

public class GuiGameOverAction implements ManagedOperationListener {

	private WorldObject playerCharacter;
	private World world;
	private WorldPanel container;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private MusicPlayer musicPlayer;
	private KeyBindings keyBindings;
	private JFrame parentFrame;
	
	private boolean gameOver = false;
	
	public GuiGameOverAction(WorldObject playerCharacter, World world, WorldPanel container, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, KeyBindings keyBindings, JFrame parentFrame) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.container = container;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.musicPlayer = musicPlayer;
		this.keyBindings = keyBindings;
		this.parentFrame = parentFrame;
		
		world.addListener(this);
	}

	@Override
	public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
		if (playerCharacter.getProperty(Constants.HIT_POINTS) <= 0 && !gameOver) {
			gameOver = true;
			String text = "Your hit points are reduced to zero, the game is over";			
			new ShowTextDialog(text, soundIdReader, parentFrame).showMe();
			Game.closeMainPanel();
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					new ShowStartScreenAction(container, imageInfoReader, soundIdReader, musicPlayer, keyBindings, world, null).actionPerformed(null);
					
				}
				
			});
			
		}
	}
}