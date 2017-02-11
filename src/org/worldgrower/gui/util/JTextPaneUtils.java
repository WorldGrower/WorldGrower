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

import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.worldgrower.gui.status.StatusMessageImageConverter;

public class JTextPaneUtils {

	public static void appendText(JTextPane textPane, String message) {
		StyledDocument document = (StyledDocument)textPane.getDocument();
    	try {
			document.insertString(document.getLength(), message, null);
		} catch (BadLocationException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static void appendIconAndText(JTextPane textPane, Image image, String message) {
		StyledDocument document = (StyledDocument)textPane.getDocument();
		image = StatusMessageImageConverter.convertImage(image);
		
        try {
			JLabel jl  = JLabelFactory.createJLabel("<html>" + message + "</html>", image);
			jl.setHorizontalAlignment(SwingConstants.LEFT);

			String styleName = "style"+message;
			Style textStyle = document.addStyle(styleName, null);
		    StyleConstants.setComponent(textStyle, jl);

		    document.insertString(document.getLength(), " ", document.getStyle(styleName));
		} catch (BadLocationException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static void appendIcon(JTextPane textPane, Image image) {
		StyledDocument document = (StyledDocument)textPane.getDocument();
		image = StatusMessageImageConverter.convertImage(image);
		
        try {
			JLabel jl  = JLabelFactory.createJLabel(image);
			jl.setHorizontalAlignment(SwingConstants.LEFT);

			String styleName = "style"+image.toString();
			Style textStyle = document.addStyle(styleName, null);
		    StyleConstants.setComponent(textStyle, jl);

		    document.insertString(document.getLength(), " ", document.getStyle(styleName));
		} catch (BadLocationException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static void appendTextUsingLabel(JTextPane textPane, String message) {
		StyledDocument document = (StyledDocument)textPane.getDocument();
		
        try {
			JLabel jl  = JLabelFactory.createJLabel(message);
			jl.setHorizontalAlignment(SwingConstants.LEFT);

			String styleName = "style"+message;
			Style textStyle = document.addStyle(styleName, null);
		    StyleConstants.setComponent(textStyle, jl);

		    document.insertString(document.getLength(), " ", document.getStyle(styleName));
		} catch (BadLocationException e) {
			throw new IllegalStateException(e);
		}
	}
}
