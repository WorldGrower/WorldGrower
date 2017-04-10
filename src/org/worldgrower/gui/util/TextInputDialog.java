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

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.music.SoundIdReader;

public class TextInputDialog extends AbstractDialog {

	public static final boolean NUMERIC_INPUT = true;
	public static final boolean APHA_NUMERIC_INPUT = false;
	
	private String value = null;
	private final JTextPane textPane;
	private JTextField textField;
	private final ImageInfoReader imageInfoReader;
	
	public TextInputDialog(String question, boolean numericInputOnly, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, JFrame parentFrame) {
		this(question, null, numericInputOnly, imageInfoReader, soundIdReader, parentFrame);
	}
	
	public TextInputDialog(String question, String defaultValue, boolean numericInputOnly, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, JFrame parentFrame) {
		this(numericInputOnly, imageInfoReader, soundIdReader, parentFrame);
		append(question);
		setDefaultValue(defaultValue);
	}
	
	public TextInputDialog(boolean numericInputOnly, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, JFrame parentFrame) {
		super(500, 190, imageInfoReader);
		this.imageInfoReader = imageInfoReader;
		
		textPane = JTextPaneFactory.createJTextPane(imageInfoReader);
		textPane.setEditable(false);
		textPane.setBounds(16, 16, 465, 50);
		addComponent(textPane);
		
		textField = numericInputOnly ? JTextFieldFactory.createIntegerOnlyJTextField() : JTextFieldFactory.createJTextField();
		textField.setBounds(16, 70, 465, 30);
		addComponent(textField);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setOpaque(false);
		buttonPane.setBounds(16, 133, 475, 50);
		addComponent(buttonPane);

		JButton cancelButton = JButtonFactory.createButton("Cancel", imageInfoReader, soundIdReader);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		JButton okButton = JButtonFactory.createButton(" OK ", imageInfoReader, soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		addActions(okButton, cancelButton);
		
		this.addWindowListener(new WindowAdapter() {
		    public void windowOpened(WindowEvent e) {
		    	textField.requestFocus();
		    }
		});
		
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}
	
	public TextInputDialog append(ImageIds imageId, String text) {
		Image image = imageInfoReader.getImage(imageId, null);
		JTextPaneUtils.appendIconAndText(textPane, image, text);
		return this;
	}
	
	public TextInputDialog append(String text) {
		JTextPaneUtils.appendTextUsingLabel(textPane, text);
		return this;
	}
	
	public TextInputDialog setDefaultValue(String defaultValue) {
		textField.setText(defaultValue);
		return this;
	}
	
	public String showMe() {
		setVisible(true);
		return value;
	}

	private void addActions(JButton okButton, JButton cancelButton) {
		ActionListener okButtonAction = new OkButtonAction();
		okButton.addActionListener(okButtonAction);
		textField.addActionListener(okButtonAction);
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				value = null;
				TextInputDialog.this.dispose();
			}
		});
	}
	
	private class OkButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			value = textField.getText();
			TextInputDialog.this.dispose();
		}
	}
}
