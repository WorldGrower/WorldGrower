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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JTextPaneFactory;
import org.worldgrower.util.FileUtils;

public class CreditsDialog extends AbstractDialog {

	private final SoundIdReader soundIdReader;
	
	public CreditsDialog(SoundIdReader soundIdReader) throws IOException {
		super(400, 800);
		
		this.soundIdReader = soundIdReader;
		
		addCreditsPane();
		addButtonPane();
	}

	private void addCreditsPane() throws IOException {
		String creditBuilder = getCreditsText();
		
		JTextPane textPane = JTextPaneFactory.createJTextPane(creditBuilder); 
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(15, 15, 368, 400);
		addComponent(scrollPane);
	}

	private String getCreditsText() throws IOException {
		List<String> creditLines = FileUtils.readFile("/credits.txt");
		StringBuilder creditBuilder = new StringBuilder();
		for(String creditLine : creditLines) {
			creditBuilder.append(creditLine).append("\n");
		}
		return creditBuilder.toString();
	}

	public void showMe() {
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addButtonPane() {
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 740, 383, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addComponent(buttonPane);
		
		JButton okButton = JButtonFactory.createButton("OK", soundIdReader);
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
