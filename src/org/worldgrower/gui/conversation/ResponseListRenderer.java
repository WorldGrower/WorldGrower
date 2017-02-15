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
package org.worldgrower.gui.conversation;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;

import org.worldgrower.conversation.Response;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.ImageSubstituter;
import org.worldgrower.gui.ImageSubstitutionMode;
import org.worldgrower.gui.util.JTextPaneFactory;

public class ResponseListRenderer implements ListCellRenderer<Response> {
	private final JTextPane rendererTextPane;
	private final ImageSubstituter imageSubstituter;
	
	public ResponseListRenderer(ImageInfoReader imageInfoReader) {
		this.rendererTextPane = JTextPaneFactory.createHmtlJTextPane(imageInfoReader);
		this.imageSubstituter = new ImageSubstituter(imageInfoReader, ImageSubstitutionMode.ALL);
	}
	
	@Override
    public Component getListCellRendererComponent(JList<? extends Response> list,
    											Response response,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus) {
		rendererTextPane.setText("");
		imageSubstituter.subtituteImagesInTextPane(rendererTextPane, response.getResponsePhrase());
		return rendererTextPane;
    }
}