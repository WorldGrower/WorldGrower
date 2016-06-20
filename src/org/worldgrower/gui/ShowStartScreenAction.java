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

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.worldgrower.World;
import org.worldgrower.gui.music.MusicPlayer;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.KeyBindings;
import org.worldgrower.gui.start.StartScreen;

public class ShowStartScreenAction extends AbstractAction {

	private final WorldPanel container;
	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	private final MusicPlayer musicPlayer;
	private final KeyBindings keyBindings;
	private final World world;
	private final JFrame parentFrame;
	
	public ShowStartScreenAction(WorldPanel container, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, KeyBindings keyBindings, World world, JFrame parentFrame) {
		super();
		this.container = container;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.musicPlayer = musicPlayer;
		this.keyBindings = keyBindings;
		this.world = world;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		StartScreen startScreen = new StartScreen(imageInfoReader, soundIdReader, musicPlayer, keyBindings, parentFrame);
		startScreen.enableSave(true, world);
		startScreen.setVisible(true);
		
		container.initializeKeyBindings();
	}

}
