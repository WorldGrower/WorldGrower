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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.font.Fonts;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JTextPaneFactory;
import org.worldgrower.util.FileUtils;

public class CreditsDialog extends AbstractDialog {

	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	
	public CreditsDialog(ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) throws IOException {
		super(700, 800, imageInfoReader);
		
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		
		addCreditsPane();
		addButtonPane();
		
		((JComponent)getRootPane()).setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
	}

	private void addCreditsPane() throws IOException {
		String creditsText = getCreditsTextAsHtml();
		
		JTextPane textPane = JTextPaneFactory.createHmtlJTextPane(imageInfoReader); 
		textPane.setEditable(false);
		textPane.setText(creditsText);
		
		textPane.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						openWebPage(e.getURL().toURI());
					} catch (URISyntaxException e1) {
						 throw new IllegalStateException("Problem opening " + e.getURL().toString(), e1);
					}
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(15, 15, 668, 720);
		addComponent(scrollPane);
	}
	
	private static void openWebPage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (IOException e) {
	            throw new IllegalStateException("Problem opening " + uri.toString(), e);
	        }
	    }
	}

	private String getCreditsTextAsHtml() throws IOException {
		List<String> creditLines = FileUtils.readFile("/credits.txt");
		StringBuilder creditBuilder = new StringBuilder("<html><body style=\"font-family: " + Fonts.FONT.getFamily() + "\"><font color=\"white\">");
		for(String creditLine : creditLines) {
			if (creditLine.contains("http://")) {
				int indexOfHtpp = creditLine.indexOf("http://");
				int endOfUrl = creditLine.indexOf(" ", indexOfHtpp);
				if (endOfUrl == -1) {
					endOfUrl = creditLine.length();
				}
				String url = creditLine.substring(indexOfHtpp, endOfUrl);
				creditLine = "<a href=\"" + url + "\">" + creditLine + "</a>";
			}
			creditBuilder.append(creditLine).append("<br/>");
		}
		creditBuilder.append("</font></body></html>");
		return creditBuilder.toString();
	}

	public void showMe() {
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addButtonPane() {
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 740, 688, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addComponent(buttonPane);
		
		JButton okButton = JButtonFactory.createButton("OK", imageInfoReader, soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, this);
		getRootPane().setDefaultButton(okButton);
	}
	
	private void addActionHandlers(JButton okButton, JDialog dialog) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
	}
	
	
}
