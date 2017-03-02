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

import java.util.List;

import javax.swing.JFrame;

import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.ShowTextDialog;

public class ErrorDialog {

	public static void showErrors(List<String> errors, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, JFrame frame) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("<html>Problem validating input fields:<br>");
		for(String error : errors) {
			buffer.append(error).append("<br>");
		}
		buffer.append("</html");
		new ShowTextDialog(buffer.toString(), imageInfoReader, soundIdReader, frame).showMe();
	}
}
