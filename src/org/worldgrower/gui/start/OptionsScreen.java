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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.worldgrower.Main;
import org.worldgrower.gui.ExceptionHandler;
import org.worldgrower.gui.GradientPanel;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.util.ButtonFactory;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JCheckBoxFactory;
import org.worldgrower.gui.util.JComboBoxFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JRadioButtonFactory;
import org.worldgrower.gui.util.JTextFieldFactory;
import org.worldgrower.util.NumberUtils;

public class OptionsScreen {
	private static final String GENDER_TOOL_TIP = "choose gender of player character";
	private static final String MUSIC_TOOL_TIP = "Play background music";
	private static final String SEED_TOOL_TIP = "The seed is used for random number generation. A different value will result in different villagers which make other decisions";
	private static final String CHARACTER_PROFESSION_TOOL_TIP = "describes profession of player character";
	private static final String MONSTER_DENSITY_TOOL_TIP = "indicates whether there are monsters when the game starts: 0 indicates no, more than 0 indicates yes";
	private static final String NUMBER_OF_VILLAGERS_TOOL_TIP = "Set starting number of villagers";
	private static final String PLAYER_NAME_TOOL_TIP = "Sets player character name";
	private static final String WORLD_WIDTH_TOOL_TIP = "Sets width of world in number of tiles";
	private static final String WORLD_HEIGHT_TOOL_TIP = "Sets height of world in number of tiles";
	
	private JFrame frame;
	private JTextField playerNameTextField;
	private JRadioButton maleRadioButton;
	private JRadioButton femaleRadioButton;
	private JTextField worldWidthTextField;
	private JTextField worldHeightTextField;
	private JTextField numberOfEnemiesTextField;
	private JTextField numberOfVillagersTextField;
	private JTextField playerProfessionTextField;
	private JTextField seedTextField;

	private final CharacterAttributes characterAttributes;
	private JCheckBox chkBackgroundMusic;
	private final ImageInfoReader imageInfoReader;
	private JComboBox<ImageIds> cmbImage;
	
	public OptionsScreen(CharacterAttributes characterAttributes, ImageInfoReader imageInfoReader) {
		initialize(imageInfoReader);
		this.characterAttributes = characterAttributes;
		this.imageInfoReader = imageInfoReader;
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(ImageInfoReader imageInfoReader) {
		frame = new JFrame();
		frame.setResizable(false);
		JPanel contentPanel = new GradientPanel();
		contentPanel.setLocation(0, 0);
		contentPanel.setLayout(null);
		contentPanel.setSize(new Dimension(414, 563));
		frame.getContentPane().add(contentPanel);
		frame.setSize(new Dimension(414, 578));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		IconUtils.setIcon(frame);
		
		JLabel lblPlayerName = JLabelFactory.createJLabel("Character Name:");
		lblPlayerName.setToolTipText(PLAYER_NAME_TOOL_TIP);
		lblPlayerName.setBounds(25, 30, 191, 26);
		contentPanel.add(lblPlayerName);
		
		playerNameTextField = JTextFieldFactory.createJTextField();
		playerNameTextField.setToolTipText(PLAYER_NAME_TOOL_TIP);
		playerNameTextField.setText(getDefaultUsername());
		playerNameTextField.setBounds(228, 30, 137, 22);
		contentPanel.add(playerNameTextField);
		playerNameTextField.setColumns(10);
		playerNameTextField.selectAll();
		
		JLabel lblGender = JLabelFactory.createJLabel("Gender:");
		lblGender.setToolTipText(GENDER_TOOL_TIP);
		lblGender.setBounds(25, 113, 191, 26);
		contentPanel.add(lblGender);
		
		maleRadioButton = JRadioButtonFactory.createJRadioButton("male");
		maleRadioButton.setSelected(true);
		maleRadioButton.setToolTipText(GENDER_TOOL_TIP);
		maleRadioButton.setBounds(228, 113, 80, 22);
		maleRadioButton.setOpaque(false);
		contentPanel.add(maleRadioButton);
		
		femaleRadioButton = JRadioButtonFactory.createJRadioButton("female");
		femaleRadioButton.setToolTipText(GENDER_TOOL_TIP);
		femaleRadioButton.setBounds(308, 113, 80, 22);
		femaleRadioButton.setOpaque(false);
		contentPanel.add(femaleRadioButton);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(maleRadioButton);
		buttonGroup.add(femaleRadioButton);
		
		JLabel lblWorldWidth = JLabelFactory.createJLabel("World Width:");
		lblWorldWidth.setToolTipText(WORLD_WIDTH_TOOL_TIP);
		lblWorldWidth.setBounds(25, 234, 191, 26);
		contentPanel.add(lblWorldWidth);
		
		worldWidthTextField = JTextFieldFactory.createJTextField();
		worldWidthTextField.setToolTipText(WORLD_WIDTH_TOOL_TIP);
		worldWidthTextField.setText("100");
		worldWidthTextField.setBounds(228, 234, 137, 22);
		contentPanel.add(worldWidthTextField);
		worldWidthTextField.setColumns(10);
		
		JLabel lblWorldHeight = JLabelFactory.createJLabel("World Height:");
		lblWorldHeight.setToolTipText(WORLD_HEIGHT_TOOL_TIP);
		lblWorldHeight.setBounds(25, 273, 191, 26);
		contentPanel.add(lblWorldHeight);
		
		worldHeightTextField = JTextFieldFactory.createJTextField();
		worldHeightTextField.setToolTipText(WORLD_HEIGHT_TOOL_TIP);
		worldHeightTextField.setText("100");
		worldHeightTextField.setColumns(10);
		worldHeightTextField.setBounds(228, 271, 137, 22);
		contentPanel.add(worldHeightTextField);
		
		JLabel lblNumberOfEnemies = JLabelFactory.createJLabel("Enemy density:");
		lblNumberOfEnemies.setToolTipText(MONSTER_DENSITY_TOOL_TIP);
		lblNumberOfEnemies.setBounds(25, 310, 191, 26);
		contentPanel.add(lblNumberOfEnemies);
		
		numberOfEnemiesTextField = JTextFieldFactory.createJTextField();
		numberOfEnemiesTextField.setToolTipText(MONSTER_DENSITY_TOOL_TIP);
		numberOfEnemiesTextField.setText("0");
		numberOfEnemiesTextField.setColumns(10);
		numberOfEnemiesTextField.setBounds(228, 310, 137, 22);
		contentPanel.add(numberOfEnemiesTextField);
		
		JLabel lblNumberOfVillagers = JLabelFactory.createJLabel("Number of Villagers:");
		lblNumberOfVillagers.setToolTipText(NUMBER_OF_VILLAGERS_TOOL_TIP);
		lblNumberOfVillagers.setBounds(25, 352, 191, 26);
		contentPanel.add(lblNumberOfVillagers);
		
		numberOfVillagersTextField = JTextFieldFactory.createJTextField();
		numberOfVillagersTextField.setToolTipText(NUMBER_OF_VILLAGERS_TOOL_TIP);
		numberOfVillagersTextField.setText("4");
		numberOfVillagersTextField.setColumns(10);
		numberOfVillagersTextField.setBounds(228, 352, 137, 22);
		contentPanel.add(numberOfVillagersTextField);
		
		JButton btnOk = ButtonFactory.createButton("Ok");
		btnOk.setBounds(230, 486, 97, 25);
		frame.getRootPane().setDefaultButton(btnOk);
		contentPanel.add(btnOk);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> errors = validateInput();
				if (errors.size() == 0) {
					frame.setVisible(false);
					try {
						int worldWidth = Integer.parseInt(worldWidthTextField.getText());
						int worldHeight = Integer.parseInt(worldHeightTextField.getText());
						int enemyDensity = Integer.parseInt(numberOfEnemiesTextField.getText());
						int villagerCount = Integer.parseInt(numberOfVillagersTextField.getText());
						int seed = Integer.parseInt(seedTextField.getText());
						boolean playBackgroundMusic = chkBackgroundMusic.isSelected();
						
						String gender = maleRadioButton.isSelected() ? "male" : "female";
						
						
						Main.run(playerNameTextField.getText(), playerProfessionTextField.getText(), gender, worldWidth, worldHeight, enemyDensity, villagerCount, seed, playBackgroundMusic, characterAttributes, imageInfoReader, (ImageIds)cmbImage.getSelectedItem());
					} catch (Exception e1) {
						ExceptionHandler.handle(e1);
					}
				} else {
					StringBuilder buffer = new StringBuilder();
					buffer.append("Problem validating input fields:\n");
					for(String error : errors) {
						buffer.append(error).append("\n");
					}
					JOptionPane.showMessageDialog(OptionsScreen.this.frame, buffer.toString());
				}
			}
		});
		
		JButton btnCancel = ButtonFactory.createButton("Cancel");
		btnCancel.setBounds(121, 486, 97, 25);
		contentPanel.add(btnCancel);
		
		JLabel lblPlayerProfession = JLabelFactory.createJLabel("Character Profession:");
		lblPlayerProfession.setToolTipText(CHARACTER_PROFESSION_TOOL_TIP);
		lblPlayerProfession.setBounds(25, 69, 191, 26);
		contentPanel.add(lblPlayerProfession);
		
		playerProfessionTextField = JTextFieldFactory.createJTextField();
		playerProfessionTextField.setToolTipText(CHARACTER_PROFESSION_TOOL_TIP);
		playerProfessionTextField.setText("adventurer");
		playerProfessionTextField.setColumns(10);
		playerProfessionTextField.setBounds(228, 69, 137, 22);
		contentPanel.add(playerProfessionTextField);
		
		JLabel lblSeed = JLabelFactory.createJLabel("Seed:");
		lblSeed.setToolTipText(SEED_TOOL_TIP);
		lblSeed.setBounds(25, 394, 191, 26);
		contentPanel.add(lblSeed);
		
		seedTextField = JTextFieldFactory.createJTextField();
		seedTextField.setToolTipText(SEED_TOOL_TIP);
		seedTextField.setText("666");
		seedTextField.setColumns(10);
		seedTextField.setBounds(228, 394, 137, 22);
		contentPanel.add(seedTextField);
		
		chkBackgroundMusic = JCheckBoxFactory.createJCheckBox("Music");
		chkBackgroundMusic.setToolTipText(MUSIC_TOOL_TIP);
		chkBackgroundMusic.setSelected(true);
		chkBackgroundMusic.setOpaque(false);
		chkBackgroundMusic.setBounds(228, 431, 137, 25);
		contentPanel.add(chkBackgroundMusic);
		
		JLabel lblPlayBackgroundMusic = JLabelFactory.createJLabel("Play background music:");
		lblPlayBackgroundMusic.setToolTipText(MUSIC_TOOL_TIP);
		lblPlayBackgroundMusic.setBounds(25, 431, 191, 26);
		contentPanel.add(lblPlayBackgroundMusic);
		
		JLabel lblCharacterImage = JLabelFactory.createJLabel("Character image:");
		lblCharacterImage.setToolTipText("choose gender of player character");
		lblCharacterImage.setBounds(25, 152, 191, 26);
		contentPanel.add(lblCharacterImage);
		
		cmbImage = JComboBoxFactory.createJComboBox();
		cmbImage.setModel(new ImageComboBoxModel(imageInfoReader));
		cmbImage.setRenderer(new ImageComboBoxCellRenderer(imageInfoReader));
		cmbImage.setSelectedIndex(0);
		cmbImage.setBounds(228, 160, 137, 58);
		contentPanel.add(cmbImage);
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				StartScreen startScreen = new StartScreen(imageInfoReader);
				startScreen.setVisible(true);
			}
		});
	}
	
	private String getDefaultUsername() {
		String userName = System.getProperty("user.name");
		if (userName != null && userName.length() > 0) {
			return userName;
		} else {
			return "MyName";
		}
	}
	
	private List<String> validateInput() {
		List<String> errors = new ArrayList<>();
		if (playerNameTextField.getText().length() == 0) {
			errors.add("Player Name cannot be empty");
		}
		
		if (playerProfessionTextField.getText().length() == 0) {
			errors.add("Player Profession cannot be empty");
		}
		
		validateDimensionField(errors, worldWidthTextField);
		validateDimensionField(errors, worldHeightTextField);
		
		if (!NumberUtils.isNumeric(numberOfEnemiesTextField.getText())) {
			errors.add("Number of enemies must be numeric");
		}
		
		if (!NumberUtils.isNumeric(numberOfVillagersTextField.getText())) {
			errors.add("Number of villagers must be numeric");
		}
		
		if (!NumberUtils.isNumeric(seedTextField.getText())) {
			errors.add("Seed must be numeric");
		}
		
		return errors;
	}

	private static void validateDimensionField(List<String> errors, JTextField textField) {
		if (textField.getText().length() == 0) {
			errors.add("World Height cannot be empty");
			return;
		}
		
		if (!NumberUtils.isNumeric(textField.getText())) {
			errors.add("World Height must be numeric");
			return;
		}
		
		if (Integer.parseInt(textField.getText()) < 100) {
			errors.add("World Height must be at least 100");
		}
	}
}
