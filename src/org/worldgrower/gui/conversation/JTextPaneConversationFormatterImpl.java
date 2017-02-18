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

import java.awt.Image;

import javax.swing.JTextPane;

import org.worldgrower.conversation.ConversationFormatter;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.util.JTextPaneUtils;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextParser;

public class JTextPaneConversationFormatterImpl implements ConversationFormatter {

	private final JTextPane textPane;
	private final ConversationArgumentFormatter conversationArgumentFormatter;
	private final ImageInfoReader imageInfoReader;

	public JTextPaneConversationFormatterImpl(JTextPane textPane, ConversationArgumentFormatter conversationArgumentFormatter, ImageInfoReader imageInfoReader) {
		super();
		this.conversationArgumentFormatter = conversationArgumentFormatter;
		this.textPane = textPane;
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public String format(FormattableText formattableText) {
		formattableText.getTextId().parse(new TextParser() {
			
			@Override
			public void variableFound(int index) {
				Object object = formattableText.getObjects()[index];
				if (object instanceof Item) {
					Item item = (Item) object;
					Image image = imageInfoReader.getImage(item.getImageId(), null);
					JTextPaneUtils.appendIconAndText(textPane, image, item.getDescription());	
				} else {
					JTextPaneUtils.appendTextUsingLabel(textPane, conversationArgumentFormatter.formatObject(formattableText.getObjects()));
				}
			}
			
			@Override
			public void constantStringFound(String string) {
				JTextPaneUtils.appendTextUsingLabel(textPane, string);
			}
		});
		return null;
	}	
}
