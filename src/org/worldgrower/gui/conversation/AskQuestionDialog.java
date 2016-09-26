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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;

import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.WorldObject;
import org.worldgrower.conversation.ConversationCategory;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Question;
import org.worldgrower.conversation.Response;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ActionContainingArgs;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.chooseworldobject.ChooseWorldObjectDialog;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JProgressBarFactory;
import org.worldgrower.gui.util.MenuFactory;

public class AskQuestionDialog extends AbstractDialog implements ManagedOperationListener {

	private final Answerer answerer;
	private final JButton askQuestion;
	private final JLabel label;
	private final JProgressBar relationshipProgresBar;
	private final SoundIdReader soundIdReader;
	private final ImageInfoReader imageInfoReader;
	
	private class ExecuteQuestionAction extends AbstractAction implements ActionContainingArgs {
		
		private final Question question;
		private int subjectId = -1;
		
		public ExecuteQuestionAction(Question question) {
			this.question = question;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			int actualSubjectId;
			if (subjectId != -1) {
				actualSubjectId = subjectId;
			} else {
				actualSubjectId = question.getSubjectId();
			}
			
			int[] args = new int[] { question.getId(), actualSubjectId, question.getHistoryItemId(), question.getAdditionalValue(), question.getAdditionalValue2() };
			answerer.askQuestion(args);
			askQuestion.setEnabled(answerer.canAskQuestion());
		}

		@Override
		public void setArgs(int[] args) {
			subjectId = args[0];
		}
	}
	
	private class ChooseSubjectAction implements ActionListener {
		private final ExecuteQuestionAction executeQuestionAction;
		private final Question question;
		
		public ChooseSubjectAction(ExecuteQuestionAction executeQuestionAction, Question question) {
			super();
			this.executeQuestionAction = executeQuestionAction;
			this.question = question;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			ChooseWorldObjectDialog dialog = answerer.createChooseWorldObjectsDialog(executeQuestionAction, question, AskQuestionDialog.this);
			dialog.showMe();
		}
	}

	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			AskQuestionDialog.this.dispose();
		}
	}
	
	public AskQuestionDialog(Answerer answerer, Conversations conversations, ImageIds imageIdPerformer, ImageIds imageIdTarget, String performerName, String targetName, Map<Integer, ImageIds> subjectImageIds, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, JFrame parentFrame) {
		super(650, 300);
		this.answerer = answerer;
		this.soundIdReader = soundIdReader;
		this.imageInfoReader = imageInfoReader;
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new CloseDialogAction(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 218, 632, 40);
		buttonPane.setOpaque(false);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addComponent(buttonPane);

		JButton okButton = JButtonFactory.createButton("OK", soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = JButtonFactory.createButton("Cancel", soundIdReader);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		JLabel performerLabel = JLabelFactory.createJLabel(imageInfoReader.getImage(imageIdPerformer, null));
		performerLabel.setToolTipText(performerName);
		performerLabel.setBounds(6, 17, 32, 48);
		addComponent(performerLabel);
		
		askQuestion = JButtonFactory.createButton("Ask Question", soundIdReader);
		askQuestion.setBounds(44, 27, 580, 22);
		askQuestion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JPopupMenu popupMenu = createQuestions(imageInfoReader, subjectImageIds, conversations, answerer);
				popupMenu.show(askQuestion, askQuestion.getX(), askQuestion.getY());
				
			}
			
		});
		addComponent(askQuestion);
		
		JLabel targetLabel = JLabelFactory.createJLabel(imageInfoReader.getImage(imageIdTarget, null));
		targetLabel.setToolTipText(targetName);
		targetLabel.setBounds(6, 90, 32, 48);
		addComponent(targetLabel);
		
		label = JLabelFactory.createJLabel(" ");
		label.setBounds(44, 50, 495, 200);
		addComponent(label);
		
		JLabel relationshipLabel = JLabelFactory.createJLabel("Relationship:");
		relationshipLabel.setToolTipText("Relationship with " + targetName);
		relationshipLabel.setBounds(6, 220, 100, 30);
		addComponent(relationshipLabel);
		
		relationshipProgresBar = JProgressBarFactory.createJProgressBar(-1000, 1000);
		relationshipProgresBar.setBounds(110, 220, 300, 30);
		relationshipProgresBar.setValue(answerer.getRelationshipValue());
		relationshipProgresBar.setToolTipText("Relationship with " + targetName);
		addComponent(relationshipProgresBar);
		
		okButton.addActionListener(new CloseDialogAction());
		cancelButton.addActionListener(new CloseDialogAction());
		
		this.addWindowListener(new WindowAdapter()
	    {
	      public void windowClosed(WindowEvent e)
	      {
	        answerer.doneTalking();
	      }
	    });
		
		pressAskQuestionButtonOnVisible();
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}

	private void pressAskQuestionButtonOnVisible() {
		ComponentListener listener = new ComponentAdapter() {
			public void componentShown(ComponentEvent evt) {
				askQuestion.doClick();
			}
		};
		this.addComponentListener(listener);
	}
	
	private JPopupMenu createQuestions(ImageInfoReader imageInfoReader, Map<Integer, ImageIds> subjectImageIds, Conversations conversations, Answerer answerer) {
		Map<ConversationCategory, List<Question>> questionsMap = answerer.getQuestionPhrases();
		JPopupMenu popupMenu = MenuFactory.createJPopupMenu();
		for(Entry<ConversationCategory, List<Question>> entry : getQuestions(questionsMap)) {
			JMenu menu = MenuFactory.createJMenu(entry.getKey().getDescription(), soundIdReader);
			setMenuIcon(menu, entry.getKey().getImageId());
			popupMenu.add(menu);
			
			List<Question> questions = entry.getValue();
			if (questions.size() > 20 && questions.get(0).getSubjectId() != -1) {
				Map<Integer, JMenu> subMenus = new HashMap<>();
				
				fillSubMenu(answerer, entry, menu, subMenus);
				addQuestionsToSubMenu(imageInfoReader, subjectImageIds, entry, menu, subMenus);
				
 			} else {
 				if (questions.size() < 20) {
 					addQuestionsToMenu(imageInfoReader, subjectImageIds, answerer, menu, questions);
 				} else {
 					List<List<Question>> questionParts = splitList(questions, 20);
 					for(List<Question> questionPart : questionParts) {
 						JMenu menuPart = MenuFactory.createJMenu(getDescriptionForQuestions(questionPart), soundIdReader);
 						addQuestionsToMenu(imageInfoReader, subjectImageIds, answerer, menuPart, questionPart);
 						menu.add(menuPart);
 					}
 				}
			}
		}
		
		return popupMenu;
	}
	
	private void setMenuIcon(JMenuItem menuItem, ImageIds imageIds) {
		Image image = imageInfoReader.getImage(imageIds, null);
		menuItem.setIcon(new ImageIcon(image));
	}
	
	private String getDescriptionForQuestions(List<Question> questions) {
		String firstQuestion = questions.get(0).getQuestionPhrase();
		String lastQuestion = questions.get(questions.size()-1).getQuestionPhrase();
		int indexOfFirstDifferingCharacter = indexOfFirstDifferingCharacter(firstQuestion, lastQuestion);
		if (indexOfFirstDifferingCharacter != -1) {
			int firstQuestionEndIndex = Math.min(indexOfFirstDifferingCharacter+15, firstQuestion.length());
			int lastQuestionEndIndex = Math.min(indexOfFirstDifferingCharacter+15, lastQuestion.length());
			return "..."
					+ firstQuestion.substring(indexOfFirstDifferingCharacter, firstQuestionEndIndex) 
					+ " ... " 
					+ lastQuestion.substring(indexOfFirstDifferingCharacter, lastQuestionEndIndex)
					+ "...";
		} else {
			return firstQuestion + " ... " + lastQuestion;
		}
	}
	
	private int indexOfFirstDifferingCharacter(String s1, String s2) {
		int length = Math.min(s1.length(), s2.length());
		for(int i=0; i<length; i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				return i;
			}
		}
		return -1;
	}

	static <T> List<List<T>> splitList(List<T> list, final int listSize) {
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int totalListSize = list.size();
	    for (int i = 0; i < totalListSize; i += listSize) {
	        parts.add(new ArrayList<T>(list.subList(i, Math.min(totalListSize, i + listSize))));
	    }
	    return parts;
	}

	private void addQuestionsToMenu(ImageInfoReader imageInfoReader, Map<Integer, ImageIds> subjectImageIds, Answerer answerer, JMenu menu, List<Question> questions) {
		for(Question question : questions) {
			List<WorldObject> possibleSubjects = answerer.getPossibleSubjects(question);
			if (possibleSubjects != null && possibleSubjects.size() > 0) {
				JMenuItem questionMenuItem = createSubjectListMenuItem(imageInfoReader, subjectImageIds, question);
				menu.add(questionMenuItem);
			} else if (possibleSubjects == null) {
				JMenuItem questionMenuItem = createAskQuestionMenuItem(imageInfoReader, subjectImageIds, question);
				menu.add(questionMenuItem);
			}
		}
	}

	private JMenuItem createAskQuestionMenuItem(ImageInfoReader imageInfoReader, Map<Integer, ImageIds> subjectImageIds, Question question) {
		JMenuItem questionMenuItem = createQuestionMenuItem(imageInfoReader, subjectImageIds, question);
		questionMenuItem.addActionListener(new ExecuteQuestionAction(question));
		return questionMenuItem;
	}

	private JMenuItem createSubjectListMenuItem(ImageInfoReader imageInfoReader, Map<Integer, ImageIds> subjectImageIds, Question question) {
		JMenuItem questionMenuItem = createQuestionMenuItem(imageInfoReader, subjectImageIds, question);
		questionMenuItem.addActionListener(new ChooseSubjectAction(new ExecuteQuestionAction(question), question));
		questionMenuItem.setToolTipText("click to see list of possible subjects");
		return questionMenuItem;
	}

	private void addQuestionsToSubMenu(ImageInfoReader imageInfoReader, Map<Integer, ImageIds> subjectImageIds, Entry<ConversationCategory, List<Question>> entry, JMenu menu, Map<Integer, JMenu> subMenus) {
		for(Question question : entry.getValue()) {
			JMenuItem questionMenuItem = createAskQuestionMenuItem(imageInfoReader, subjectImageIds, question);
			int subjectId = question.getSubjectId();
			if (subjectId != -1) {
				subMenus.get(subjectId).add(questionMenuItem);
			} else {
				menu.add(questionMenuItem);
			}
		}
	}

	private void fillSubMenu(Answerer answerer, Entry<ConversationCategory, List<Question>> entry, JMenu menu, Map<Integer, JMenu> subMenus) {
		for(Question question : entry.getValue()) {
			int subjectId = question.getSubjectId();
			if (subjectId != -1) {
				if (!subMenus.containsKey(subjectId)) {
					String name = answerer.getDescription(subjectId);
					JMenu subMenu = MenuFactory.createJMenu(name, soundIdReader);
					subMenus.put(subjectId, subMenu);
					menu.add(subMenu);
				}
			}
		}
	}

	private JMenuItem createQuestionMenuItem(ImageInfoReader imageInfoReader, Map<Integer, ImageIds> subjectImageIds, Question question) {
		JMenuItem questionMenuItem = MenuFactory.createJMenuItem(question.getQuestionPhrase(), soundIdReader);
		int subjectId = question.getSubjectId();
		ImageIds subjectImageId = subjectImageIds.get(subjectId);
		if (subjectImageId != null) {
			questionMenuItem.setIcon(new ImageIcon(imageInfoReader.getImage(subjectImageId, null)));
		}
		return questionMenuItem;
	}

	private List<Entry<ConversationCategory, List<Question>>> getQuestions(Map<ConversationCategory, List<Question>> questionsMap) {
		List<Entry<ConversationCategory, List<Question>>> questions = new ArrayList<>();
		questions.addAll(questionsMap.entrySet());
		
		Collections.sort(questions, new Comparator<Entry<ConversationCategory, List<Question>>>() {
			@Override
			public int compare(Entry<ConversationCategory, List<Question>> o1, Entry<ConversationCategory, List<Question>> o2) {
				return o1.getKey().getDescription().compareTo(o2.getKey().getDescription());
			}
		});
		
		return questions;
	}

	public void showMe() {
		setVisible(true);
	}

	@Override
	public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
		if (answerer.filterMessage(performer)) {
			Response response = (Response) value;
			label.setText("<html>" + response.getResponsePhrase() + "</html>");
			relationshipProgresBar.setValue(answerer.getRelationshipValue());
		}
	}
}
