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
package org.worldgrower.gui.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.RoundedBorder;
import org.worldgrower.gui.TiledImageButton;
import org.worldgrower.gui.font.Fonts;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.music.SoundIds;

public class JButtonFactory {

	public static JButton createButton(String text, ImageIcon icon, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		JButton button = createButton(text, icon, imageInfoReader);
		addClickSoundEffect(soundIdReader, button);		
		return button;
	}

	private static void addClickSoundEffect(SoundIdReader soundIdReader, AbstractButton button) {
		if (soundIdReader == null) {
			throw new IllegalStateException("soundIdReader is null");
		}
		
		button.addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
            	soundIdReader.playSoundEffect(SoundIds.CLICK);
            }
        });
	}
	
	private static JButton createButton(String text, ImageIcon icon, ImageInfoReader imageInfoReader) {
		JButton button = new TiledImageButton(text, icon, imageInfoReader);
		setButtonProperties(button);
		return button;
	}

	public static JButton createButton(String text, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		JButton button = createButton(text, imageInfoReader);
		addClickSoundEffect(soundIdReader, button);
		return button;
	}
	
	public static JButton createButton(String text, ImageInfoReader imageInfoReader) {
		JButton button = new TiledImageButton(text, imageInfoReader);
		setButtonProperties(button);
		return button;
	}
	
	public static JButton createUntexturedButton(String text) {
		JButton button = new JButton(text);
		setButtonProperties(button);
		button.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		return button;
	}
	
	public static JToggleButton createToggleButton(ImageIcon icon, SoundIdReader soundIdReader) {
		JToggleButton button = new JToggleButton(icon);
		button.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		addClickSoundEffect(soundIdReader, button);
		setButtonProperties(button);
		return button;
	}
	
	public static JToggleButton createToggleButton(String text, ImageIcon icon, SoundIdReader soundIdReader) {
		JToggleButton button = new JToggleButton(text, icon);
		button.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		addClickSoundEffect(soundIdReader, button);
		setButtonProperties(button);
		return button;
	}
	
	private static void setButtonProperties(AbstractButton button) {
		button.setBorder(new RoundedBorder(5));
		button.setForeground(ColorPalette.FOREGROUND_COLOR);
		button.setFont(Fonts.FONT);
	}
}
