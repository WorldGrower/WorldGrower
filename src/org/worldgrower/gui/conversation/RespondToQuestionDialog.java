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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Response;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.util.IconUtils;

public class RespondToQuestionDialog extends JDialog {

	private final JComboBox<Response> comboBoxResponse;
	private final JLabel label;
	private int selectedResponse = -1;
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			RespondToQuestionDialog.this.dispose();
		}
	}

	public RespondToQuestionDialog(int id, int conversationId, int historyItemId, int additionalValue, Questioner questioner, Conversations conversations, ImageIds imageIdPerformer, ImageIds imageIdTarget, ImageInfoReader imageInfoReader) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(560, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		IconUtils.setIcon(this);
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new CloseDialogAction(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 218, 519, 35);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		JLabel targetLabel = new JLabel(new ImageIcon(imageInfoReader.getImage(imageIdTarget, null)));
		targetLabel.setBounds(6, 90, 32, 48);
		getContentPane().add(targetLabel);
		
		comboBoxResponse = createResponseComboBox(id, conversationId, historyItemId, conversations, questioner, additionalValue);
		comboBoxResponse.setBounds(44, 86, 475, 80);
		getContentPane().add(comboBoxResponse);
		
		JLabel performerLabel = new JLabel(new ImageIcon(imageInfoReader.getImage(imageIdPerformer, null)));
		performerLabel.setBounds(6, 17, 32, 48);
		getContentPane().add(performerLabel);
		
		label = new JLabel("<html>"+ questioner.getQuestionPhrase() +"</html>");
		label.setBounds(44, 27, 475, 46);
		getContentPane().add(label);
		
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				selectedResponse = comboBoxResponse.getSelectedIndex();
			}
		});
		
		okButton.addActionListener(new CloseDialogAction());
		cancelButton.addActionListener(new CloseDialogAction());		
	}
	
	private JComboBox<Response> createResponseComboBox(int id, int subjectId, int historyItemId, Conversations conversations, Questioner questioner, int additionalValue) {
		JComboBox<Response> responseComboBox = new JComboBox<Response>();
		List<Response> responses = questioner.getResponsePhrases(id, subjectId, historyItemId, additionalValue);
		for(Response response : responses) {
			responseComboBox.addItem(response);
		}
		return responseComboBox;
	}

	public int showMe() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		return selectedResponse;
	}
}
