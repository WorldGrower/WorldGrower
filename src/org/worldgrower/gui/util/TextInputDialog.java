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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.music.SoundIdReader;

public class TextInputDialog extends AbstractDialog {

	private String value = null;
	private JTextField textField;
	
	public TextInputDialog(String question, SoundIdReader soundIdReader, JFrame parentFrame) {
		this(question, false, soundIdReader, parentFrame);
	}
	
	public TextInputDialog(String question, boolean numericInputOnly, SoundIdReader soundIdReader, JFrame parentFrame) {
		super(450, 190);
		
		JLabel label = JLabelFactory.createJLabel(question);
		label.setBounds(16, 16, 415, 50);
		addComponent(label);
		
		textField = numericInputOnly ? JTextFieldFactory.createIntegerOnlyJTextField() : JTextFieldFactory.createJTextField();
		textField.setBounds(16, 70, 415, 30);
		addComponent(textField);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setOpaque(false);
		buttonPane.setBounds(16, 133, 425, 50);
		addComponent(buttonPane);

		JButton okButton = JButtonFactory.createButton(" OK ", soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = JButtonFactory.createButton("Cancel", soundIdReader);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		addActions(okButton, cancelButton);
		
		this.addWindowListener(new WindowAdapter() {
		    public void windowOpened(WindowEvent e) {
		    	textField.requestFocus();
		    }
		});
		
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
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
