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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.worldgrower.Constants;
import org.worldgrower.attribute.GhostImageIds;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.gui.GuiAttributeDescription;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.TiledImagePanel;
import org.worldgrower.gui.cursor.Cursors;
import org.worldgrower.gui.music.MusicPlayer;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JComboBoxFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.JRadioButtonFactory;
import org.worldgrower.gui.util.JTextFieldFactory;

public class CharacterCustomizationScreen extends JFrame {

	private static final String PLAYER_NAME_TOOL_TIP = "Sets player character name";
	private static final String GENDER_TOOL_TIP = "choose gender of player character";
	private static final String CHARACTER_IMAGE_TOOLTIP = "choose player character image";
	private static final String CHARACTER_PROFESSION_TOOL_TIP = "describes profession of player character";
	
	private static final String ATTRIBUTE_EXPLANATION = "Shows number of attribute points that can be distributed among attributes. Attributes must be between 8 and 20";
	private JPanel contentPane;
	private int attributePoints = 6;
	
	private List<JButton> plusButtons = new ArrayList<>();
	private List<JButton> minButtons = new ArrayList<>();
	private JLabel lblStrength;
	private JLabel lblConstitution;
	private JLabel lblDexterity;
	private JLabel lblIntelligence;
	private JLabel lblWisdom;
	private JLabel lblCharisma;
	
	private JTextField playerNameTextField;
	private JRadioButton maleRadioButton;
	private JRadioButton femaleRadioButton;
	private JTextField playerProfessionTextField;
	private JComboBox<ImageIds> cmbImage;
	
	private JButton btnOk;
	
	private final ImageInfoReader imageInfoReader;
	
	public CharacterCustomizationScreen(ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, KeyBindings keyBindings, GhostImageIds ghostImageIds, JFrame parentFrame) {
		super("Character Customization");
		this.imageInfoReader = imageInfoReader;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 599, 780);
		setResizable(false);
		contentPane = new TiledImagePanel(imageInfoReader);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setPreferredSize(getSize());
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);
		IconUtils.setIcon(this);
		setCursor(Cursors.CURSOR);
		((JComponent)getRootPane()).setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		
		JPanel attributePanel = JPanelFactory.createJPanel("Attributes");
		attributePanel.setLayout(null);
		attributePanel.setBounds(20, 20, 539, 410);
		contentPane.add(attributePanel);
		
		JLabel attributeLabel = JLabelFactory.createJLabel(attributePoints);
		attributeLabel.setToolTipText(ATTRIBUTE_EXPLANATION);
		attributeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		attributeLabel.setBounds(453, 61, 56, 20);
		attributePanel.add(attributeLabel);
		
		int attributeLabelWidth = 120;
		int attributeLabelHeight = 35;
		int labelValueLeft = 145;
		JLabel lblStrengthDesc = createAttributeLabel(Constants.STRENGTH, "Strength", ImageIds.STRENGTH_ICON);
		lblStrengthDesc.setBounds(12, 41, attributeLabelWidth, attributeLabelHeight);
		attributePanel.add(lblStrengthDesc);
		
		lblStrength = JLabelFactory.createJLabel("10");
		lblStrength.setBounds(labelValueLeft, 41, 24, attributeLabelHeight);
		lblStrength.setToolTipText(lblStrengthDesc.getToolTipText());
		attributePanel.add(lblStrength);
		
		JLabel lblConstitutionDesc = createAttributeLabel(Constants.CONSTITUTION, "Constitution", ImageIds.CONSTITUTION_ICON);
		lblConstitutionDesc.setBounds(12, 101, attributeLabelWidth, attributeLabelHeight);
		attributePanel.add(lblConstitutionDesc);
		
		lblConstitution = JLabelFactory.createJLabel("10");
		lblConstitution.setBounds(labelValueLeft, 101, 24, attributeLabelHeight);
		lblConstitution.setToolTipText(lblConstitutionDesc.getToolTipText());
		attributePanel.add(lblConstitution);
		
		JLabel lblDexterityDesc = createAttributeLabel(Constants.DEXTERITY, "Dexterity", ImageIds.DEXTERITY_ICON);
		lblDexterityDesc.setBounds(12, 161, attributeLabelWidth, attributeLabelHeight);
		attributePanel.add(lblDexterityDesc);
		
		lblDexterity = JLabelFactory.createJLabel("10");
		lblDexterity.setBounds(labelValueLeft, 161, 24, attributeLabelHeight);
		lblDexterity.setToolTipText(lblDexterityDesc.getToolTipText());
		attributePanel.add(lblDexterity);
		
		JLabel lblIntelligenceDesc = createAttributeLabel(Constants.INTELLIGENCE, "Intelligence", ImageIds.INTELLIGENCE_ICON);
		lblIntelligenceDesc.setBounds(12, 221, attributeLabelWidth, attributeLabelHeight);
		attributePanel.add(lblIntelligenceDesc);
		
		lblIntelligence = JLabelFactory.createJLabel("10");
		lblIntelligence.setBounds(labelValueLeft, 221, 24, attributeLabelHeight);
		lblIntelligence.setToolTipText(lblIntelligenceDesc.getToolTipText());
		attributePanel.add(lblIntelligence);
		
		JLabel lblWisdomDesc = createAttributeLabel(Constants.WISDOM, "Wisdom", ImageIds.WISDOM_ICON);
		lblWisdomDesc.setBounds(12, 281, attributeLabelWidth, attributeLabelHeight);
		attributePanel.add(lblWisdomDesc);
		
		lblWisdom = JLabelFactory.createJLabel("10");
		lblWisdom.setBounds(labelValueLeft, 281, 24, attributeLabelHeight);
		lblWisdom.setToolTipText(lblWisdomDesc.getToolTipText());
		attributePanel.add(lblWisdom);
		
		JLabel lblCharismaDesc = createAttributeLabel(Constants.CHARISMA, "Charisma", ImageIds.CHARISMA_ICON);
		lblCharismaDesc.setBounds(12, 341, attributeLabelWidth, attributeLabelHeight);
		attributePanel.add(lblCharismaDesc);
		
		lblCharisma = JLabelFactory.createJLabel("10");
		lblCharisma.setBounds(labelValueLeft, 341, 24, attributeLabelHeight);
		lblCharisma.setToolTipText(lblCharismaDesc.getToolTipText());
		attributePanel.add(lblCharisma);
		
		int plusButtonLeft = 254;
		int minusButtonLeft = 186;
		int buttonWidth = 48;
		int buttonHeight = 48;
		
		ImageIcon plusImageIcon = new ImageIcon(imageInfoReader.getImage(ImageIds.PLUS, null));
		ImageIcon minusImageIcon = new ImageIcon(imageInfoReader.getImage(ImageIds.MINUS, null));
		
		JButton button1Plus = JButtonFactory.createButton("", plusImageIcon, imageInfoReader, soundIdReader);
		plusButtons.add(button1Plus);
		button1Plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				incrementAttributeValue(lblStrength, attributeLabel); 
			}
		});
		button1Plus.setBounds(plusButtonLeft, 35, buttonWidth, buttonHeight);
		attributePanel.add(button1Plus);
		
		JButton button1Min = JButtonFactory.createButton("", minusImageIcon, imageInfoReader, soundIdReader);
		minButtons.add(button1Min);
		button1Min.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				decrementAttributeValue(lblStrength, attributeLabel); 
			}
		});
		button1Min.setBounds(minusButtonLeft, 35, buttonWidth, buttonHeight);
		attributePanel.add(button1Min);
		
		JButton button = JButtonFactory.createButton("", plusImageIcon, imageInfoReader, soundIdReader);
		plusButtons.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				incrementAttributeValue(lblConstitution, attributeLabel); 
			}
		});
		button.setBounds(plusButtonLeft, 95, buttonWidth, buttonHeight);
		attributePanel.add(button);
		
		JButton button_1 = JButtonFactory.createButton("", minusImageIcon, imageInfoReader, soundIdReader);
		minButtons.add(button_1);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				decrementAttributeValue(lblConstitution, attributeLabel); 
			}
		});
		button_1.setBounds(minusButtonLeft, 95, buttonWidth, buttonHeight);
		attributePanel.add(button_1);
		
		JButton button_2 = JButtonFactory.createButton("", plusImageIcon, imageInfoReader, soundIdReader);
		plusButtons.add(button_2);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				incrementAttributeValue(lblDexterity, attributeLabel); 
			}
		});
		button_2.setBounds(plusButtonLeft, 155, buttonWidth, buttonHeight);
		attributePanel.add(button_2);
		
		JButton button_3 = JButtonFactory.createButton("", minusImageIcon, imageInfoReader, soundIdReader);
		minButtons.add(button_3);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				decrementAttributeValue(lblDexterity, attributeLabel); 
			}
		});
		button_3.setBounds(minusButtonLeft, 155, buttonWidth, buttonHeight);
		attributePanel.add(button_3);
		
		JButton button_4 = JButtonFactory.createButton("", plusImageIcon, imageInfoReader, soundIdReader);
		plusButtons.add(button_4);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				incrementAttributeValue(lblIntelligence, attributeLabel); 
			}
		});
		button_4.setBounds(plusButtonLeft, 215, buttonWidth, buttonHeight);
		attributePanel.add(button_4);
		
		JButton button_5 = JButtonFactory.createButton("", minusImageIcon, imageInfoReader, soundIdReader);
		minButtons.add(button_5);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				decrementAttributeValue(lblIntelligence, attributeLabel); 
			}
		});
		button_5.setBounds(minusButtonLeft, 215, buttonWidth, buttonHeight);
		attributePanel.add(button_5);
		
		JButton button_6 = JButtonFactory.createButton("", plusImageIcon, imageInfoReader, soundIdReader);
		plusButtons.add(button_6);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				incrementAttributeValue(lblWisdom, attributeLabel); 
			}
		});
		button_6.setBounds(plusButtonLeft, 275, buttonWidth, buttonHeight);
		attributePanel.add(button_6);
		
		JButton button_7 = JButtonFactory.createButton("", minusImageIcon, imageInfoReader, soundIdReader);
		minButtons.add(button_7);
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				decrementAttributeValue(lblWisdom, attributeLabel); 
			}
		});
		button_7.setBounds(minusButtonLeft, 275, buttonWidth, buttonHeight);
		attributePanel.add(button_7);
		
		JButton button_8 = JButtonFactory.createButton("", plusImageIcon, imageInfoReader, soundIdReader);
		plusButtons.add(button_8);
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				incrementAttributeValue(lblCharisma, attributeLabel); 
			}
		});
		button_8.setBounds(plusButtonLeft, 335, buttonWidth, buttonHeight);
		attributePanel.add(button_8);
		
		JButton button_9 = JButtonFactory.createButton("", minusImageIcon, imageInfoReader, soundIdReader);
		minButtons.add(button_9);
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				decrementAttributeValue(lblCharisma, attributeLabel); 
			}
		});
		button_9.setBounds(minusButtonLeft, 335, buttonWidth, buttonHeight);
		attributePanel.add(button_9);
		
		setButtonTooltipTexts();
		
		JLabel lblRemainingAttributePoints = JLabelFactory.createJLabel("Remaining attribute points:");
		lblRemainingAttributePoints.setToolTipText(ATTRIBUTE_EXPLANATION);
		lblRemainingAttributePoints.setBounds(334, 33, 215, 25);
		attributePanel.add(lblRemainingAttributePoints);
		
		JPanel generalInfoPanel = JPanelFactory.createJPanel("General Info");
		generalInfoPanel.setLayout(null);
		generalInfoPanel.setBounds(20, 450, 539, 270);
		contentPane.add(generalInfoPanel);
		
		CustomGameParameters customGameParameters = new CustomGameParameters();
		
		JLabel lblPlayerName = JLabelFactory.createJLabel("Character Name:");
		lblPlayerName.setToolTipText(PLAYER_NAME_TOOL_TIP);
		lblPlayerName.setBounds(25, 33, 191, 26);
		generalInfoPanel.add(lblPlayerName);
		
		playerNameTextField = JTextFieldFactory.createJTextField();
		playerNameTextField.setToolTipText(PLAYER_NAME_TOOL_TIP);
		playerNameTextField.setText(customGameParameters.getPlayerName());
		playerNameTextField.setBounds(228, 33, 137, 22);
		generalInfoPanel.add(playerNameTextField);
		playerNameTextField.setColumns(10);
		playerNameTextField.selectAll();
		
		JLabel lblGender = JLabelFactory.createJLabel("Gender:");
		lblGender.setToolTipText(GENDER_TOOL_TIP);
		lblGender.setBounds(25, 116, 191, 26);
		generalInfoPanel.add(lblGender);
		
		maleRadioButton = JRadioButtonFactory.createJRadioButton("male");
		maleRadioButton.setSelected(true);
		maleRadioButton.setToolTipText(GENDER_TOOL_TIP);
		maleRadioButton.setBounds(228, 116, 80, 22);
		maleRadioButton.setOpaque(false);
		generalInfoPanel.add(maleRadioButton);
		
		femaleRadioButton = JRadioButtonFactory.createJRadioButton("female");
		femaleRadioButton.setToolTipText(GENDER_TOOL_TIP);
		femaleRadioButton.setBounds(308, 116, 80, 22);
		femaleRadioButton.setOpaque(false);
		generalInfoPanel.add(femaleRadioButton);
		
		JLabel lblCharacterImage = JLabelFactory.createJLabel("Character image:");
		lblCharacterImage.setToolTipText(CHARACTER_IMAGE_TOOLTIP);
		lblCharacterImage.setBounds(25, 155, 191, 26);
		lblCharacterImage.setCursor(Cursors.CURSOR);
		generalInfoPanel.add(lblCharacterImage);
		
		cmbImage = JComboBoxFactory.createJComboBox(imageInfoReader);
		cmbImage.setModel(new ImageComboBoxModel(imageInfoReader));
		cmbImage.setRenderer(new ImageComboBoxCellRenderer(imageInfoReader));
		cmbImage.setToolTipText(CHARACTER_IMAGE_TOOLTIP);
		cmbImage.setSelectedIndex(0);
		cmbImage.setBounds(228, 163, 137, 58);
		generalInfoPanel.add(cmbImage);
		
		JLabel lblPlayerProfession = JLabelFactory.createJLabel("Character Profession:");
		lblPlayerProfession.setToolTipText(CHARACTER_PROFESSION_TOOL_TIP);
		lblPlayerProfession.setBounds(25, 72, 191, 26);
		generalInfoPanel.add(lblPlayerProfession);
		
		playerProfessionTextField = JTextFieldFactory.createJTextField();
		playerProfessionTextField.setToolTipText(CHARACTER_PROFESSION_TOOL_TIP);
		playerProfessionTextField.setText(customGameParameters.getPlayerProfession());
		playerProfessionTextField.setColumns(10);
		playerProfessionTextField.setBounds(228, 72, 137, 22);
		generalInfoPanel.add(playerProfessionTextField);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(maleRadioButton);
		buttonGroup.add(femaleRadioButton);
		
		btnOk = JButtonFactory.createButton("Ok", imageInfoReader, soundIdReader);
		btnOk.setBounds(460, 730, 97, 25);
		getRootPane().setDefaultButton(btnOk);
		contentPane.add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CharacterAttributes characterAttributes = new CharacterAttributes(
						Integer.parseInt(lblStrength.getText()),
						Integer.parseInt(lblConstitution.getText()),
						Integer.parseInt(lblDexterity.getText()),
						Integer.parseInt(lblIntelligence.getText()),
						Integer.parseInt(lblWisdom.getText()),
						Integer.parseInt(lblCharisma.getText())
						);
				
				List<String> errors = validateInput();
				if (errors.size() == 0) {
					CharacterCustomizationScreen.this.setVisible(false);
					
					String gender = maleRadioButton.isSelected() ? "male" : "female";
					PlayerCharacterInfo playerCharacterInfo = new PlayerCharacterInfo(playerNameTextField.getText(), playerProfessionTextField.getText(), gender, (ImageIds)cmbImage.getSelectedItem());
					OptionsScreen window = new OptionsScreen(characterAttributes, playerCharacterInfo, imageInfoReader, soundIdReader, musicPlayer, keyBindings, ghostImageIds, parentFrame);
					window.setVisible(true);
				} else {
					ErrorDialog.showErrors(errors, imageInfoReader, soundIdReader, CharacterCustomizationScreen.this);
				}
			}
		});
		
		JButton btnCancel = JButtonFactory.createButton("Cancel", imageInfoReader, soundIdReader);
		btnCancel.setBounds(350, 730, 97, 25);
		contentPane.add(btnCancel);
		Action cancelAction = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CharacterCustomizationScreen.this.setVisible(false);
				StartScreen startScreen = new StartScreen(imageInfoReader, soundIdReader, musicPlayer);
				startScreen.setVisible(true);
				
			}
		};
		btnCancel.addActionListener(cancelAction);
		
		if (parentFrame != null) {
			SwingUtils.installEscapeCloseOperation(this);
			DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
		} else {
			SwingUtils.installCloseAction(cancelAction, this.getRootPane());
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		btnOk.requestFocusInWindow();
	}

	private void setButtonTooltipTexts() {
		for(JButton minButton : minButtons) {
			minButton.setToolTipText("Decrease attribute value");
		}
		
		for(JButton plusButton : plusButtons) {
			plusButton.setToolTipText("Increase attribute value");
		}
	}
	
	private JLabel createAttributeLabel(IntProperty attributeProperty, String description, ImageIds imageId) {
		String tooltip = GuiAttributeDescription.createToolTipDescription(attributeProperty, description);
		Image image = imageInfoReader.getImage(imageId, null);
		JLabel label = JLabelFactory.createJLabel(description, image);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setToolTipText(tooltip);
		return label;
	}

	private void incrementAttributeValue(JLabel label, JLabel attributeLabel) {
		int currentValue = Integer.parseInt(label.getText());
		int newValue = currentValue + 1;
		label.setText(Integer.toString(newValue));
		
		attributePoints--;
		attributeLabel.setText(Integer.toString(attributePoints));
		
		handleMinMaxValues();
		
		if (attributePoints == 0) {
			for(JButton plusButton : plusButtons) {
				plusButton.setEnabled(false);
			}
		}
	}
	
	private void handleMinMaxValues() {
		int strength = Integer.parseInt(lblStrength.getText());
		handleMinMaxValue(strength, 0);
		
		int constitution = Integer.parseInt(lblConstitution.getText());
		handleMinMaxValue(constitution, 1);
		
		int dexterity = Integer.parseInt(lblDexterity.getText());
		handleMinMaxValue(dexterity, 2);
		
		int intelligence = Integer.parseInt(lblIntelligence.getText());
		handleMinMaxValue(intelligence, 3);
		
		int wisdom = Integer.parseInt(lblWisdom.getText());
		handleMinMaxValue(wisdom, 4);
		
		int charisma = Integer.parseInt(lblCharisma.getText());
		handleMinMaxValue(charisma, 5);
	}
	
	private void handleMinMaxValue(int attributeValue, int buttonIndex) {
		if (attributeValue == 20) {
			plusButtons.get(buttonIndex).setEnabled(false);
		} else if (attributeValue == 8) {
			minButtons.get(buttonIndex).setEnabled(false);
		} else {
			plusButtons.get(buttonIndex).setEnabled(true);
			minButtons.get(buttonIndex).setEnabled(true);
		}
	}

	private void decrementAttributeValue(JLabel label, JLabel attributeLabel) {
		int currentValue = Integer.parseInt(label.getText());
		int newValue = currentValue - 1;
		label.setText(Integer.toString(newValue));
		
		attributePoints++;
		attributeLabel.setText(Integer.toString(attributePoints));
		
		if (attributePoints > 0) {
			for(JButton plusButton : plusButtons) {
				plusButton.setEnabled(true);
			}
		}
		
		handleMinMaxValues();
	}
	
	private List<String> validateInput() {
		List<String> errors = new ArrayList<>();
		if (playerNameTextField.getText().length() == 0) {
			errors.add("Player Name cannot be empty");
		}
		
		if (playerProfessionTextField.getText().length() == 0) {
			errors.add("Player Profession cannot be empty");
		}
		
		return errors;
	}
}
