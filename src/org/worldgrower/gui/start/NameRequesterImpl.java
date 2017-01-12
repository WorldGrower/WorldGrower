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
package org.worldgrower.gui.start;

import javax.swing.JFrame;

import org.worldgrower.CommonerNameGenerator;
import org.worldgrower.generator.NameRequester;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.TextInputDialog;

public class NameRequesterImpl implements NameRequester {

	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	private JFrame parentFrame;
	
	public NameRequesterImpl(ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		super();
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
	}

	@Override
	public String requestName(boolean isFemale, String gender, CommonerNameGenerator commonerNameGenerator) {
		String defaultName = commonerNameGenerator.getNextCommonerName(isFemale);
		String textDialogMessage = "Please name newborn " + gender + " child:";
		String childName = new TextInputDialog(textDialogMessage, defaultName, TextInputDialog.APHA_NUMERIC_INPUT, imageInfoReader, soundIdReader, parentFrame).showMe();
		if ((childName != null) && (childName.length() > 0)) {
			return childName;
		} else {
			return defaultName;
		}
	}
	
	public void setParent(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}
}
