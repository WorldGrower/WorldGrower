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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.worldgrower.gui.ExceptionHandler;
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
import org.worldgrower.gui.util.JRadioButtonFactory;
import org.worldgrower.gui.util.JTextFieldFactory;
import org.worldgrower.gui.util.ShowTextDialog;
import org.worldgrower.util.NumberUtils;

public class OptionsScreen {
	private static final String GENDER_TOOL_TIP = "choose gender of player character";
	private static final String SEED_TOOL_TIP = "The seed is used for random number generation. A different value will result in different villagers which make other decisions";
	private static final String CHARACTER_PROFESSION_TOOL_TIP = "describes profession of player character";
	private static final String MONSTER_DENSITY_TOOL_TIP = "indicates whether there are monsters when the game starts: 0 indicates no, more than 0 indicates yes";
	private static final String NUMBER_OF_VILLAGERS_TOOL_TIP = "Set starting number of villagers";
	private static final String PLAYER_NAME_TOOL_TIP = "Sets player character name";
	private static final String WORLD_WIDTH_TOOL_TIP = "Sets width of world in number of tiles";
	private static final String WORLD_HEIGHT_TOOL_TIP = "Sets height of world in number of tiles";
	private static final String START_TURN_TOOL_TIP = "Sets turn on which the player character enters the world";
	private static final String STONE_RESOURCES_TOOL_TIP = "Sets abundance of stone resources";
	private static final String ORE_RESOURCES_TOOL_TIP = "Sets abundance of iron ore resources";
	private static final String GOLD_RESOURCES_TOOL_TIP = "Sets abundance of gold resources";
	
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
	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	private final MusicPlayer musicPlayer;
	private JComboBox<ImageIds> cmbImage;
	private final KeyBindings keyBindings;
	private JTextField startTurnTextField;
	private JComboBox<ResourceMultiplier> stoneResourceMultipliersComboBox;
	private JComboBox<ResourceMultiplier> oreResourceMultipliersComboBox;
	private JComboBox<ResourceMultiplier> goldResourceMultipliersComboBox;
	
	public OptionsScreen(CharacterAttributes characterAttributes, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, KeyBindings keyBindings, JFrame parentFrame) {
		this.characterAttributes = characterAttributes;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.musicPlayer = musicPlayer;
		this.keyBindings = keyBindings;
		
		initialize(imageInfoReader, parentFrame);
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(ImageInfoReader imageInfoReader, JFrame parentFrame) {
		frame = new JFrame();
		frame.setResizable(false);
		((JComponent)frame.getRootPane()).setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		JPanel contentPanel = new TiledImagePanel(imageInfoReader);
		contentPanel.setLocation(0, 0);
		contentPanel.setLayout(null);
		frame.setUndecorated(true);
		int width = 400;
		int height = 680;
		contentPanel.setSize(new Dimension(width, height));
		contentPanel.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().add(contentPanel);
		frame.setSize(new Dimension(width, height));
		frame.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().setPreferredSize(new Dimension(width, height));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		IconUtils.setIcon(frame);
		frame.setCursor(Cursors.CURSOR);
		
		CustomGameParameters customGameParameters = new CustomGameParameters();
		
		JLabel lblPlayerName = JLabelFactory.createJLabel("Character Name:");
		lblPlayerName.setToolTipText(PLAYER_NAME_TOOL_TIP);
		lblPlayerName.setBounds(25, 30, 191, 26);
		contentPanel.add(lblPlayerName);
		
		playerNameTextField = JTextFieldFactory.createJTextField();
		playerNameTextField.setToolTipText(PLAYER_NAME_TOOL_TIP);
		playerNameTextField.setText(customGameParameters.getPlayerName());
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
		worldWidthTextField.setText(Integer.toString(customGameParameters.getWorldWidth()));
		worldWidthTextField.setBounds(228, 234, 137, 22);
		contentPanel.add(worldWidthTextField);
		worldWidthTextField.setColumns(10);
		
		JLabel lblWorldHeight = JLabelFactory.createJLabel("World Height:");
		lblWorldHeight.setToolTipText(WORLD_HEIGHT_TOOL_TIP);
		lblWorldHeight.setBounds(25, 273, 191, 26);
		contentPanel.add(lblWorldHeight);
		
		worldHeightTextField = JTextFieldFactory.createJTextField();
		worldHeightTextField.setToolTipText(WORLD_HEIGHT_TOOL_TIP);
		worldHeightTextField.setText(Integer.toString(customGameParameters.getWorldHeight()));
		worldHeightTextField.setColumns(10);
		worldHeightTextField.setBounds(228, 271, 137, 22);
		contentPanel.add(worldHeightTextField);
		
		final int defaultResourceSelection = 2;
		
		JLabel lblStoneResource = JLabelFactory.createJLabel("Stone resources:");
		lblStoneResource.setToolTipText(STONE_RESOURCES_TOOL_TIP);
		lblStoneResource.setBounds(25, 310, 191, 26);
		contentPanel.add(lblStoneResource);
		
		stoneResourceMultipliersComboBox = JComboBoxFactory.createJComboBox(getResourceMultipliers(), imageInfoReader);
		stoneResourceMultipliersComboBox.setSelectedIndex(defaultResourceSelection);
		stoneResourceMultipliersComboBox.setForeground(Color.BLACK);
		stoneResourceMultipliersComboBox.setToolTipText(STONE_RESOURCES_TOOL_TIP);
		stoneResourceMultipliersComboBox.setBounds(228, 310, 137, 26);
		contentPanel.add(stoneResourceMultipliersComboBox);
		
		JLabel lblOreResource = JLabelFactory.createJLabel("Iron ore resources:");
		lblOreResource.setToolTipText(ORE_RESOURCES_TOOL_TIP);
		lblOreResource.setBounds(25, 352, 191, 26);
		contentPanel.add(lblOreResource);
		
		oreResourceMultipliersComboBox = JComboBoxFactory.createJComboBox(getResourceMultipliers(), imageInfoReader);
		oreResourceMultipliersComboBox.setSelectedIndex(defaultResourceSelection);
		oreResourceMultipliersComboBox.setForeground(Color.BLACK);
		oreResourceMultipliersComboBox.setToolTipText(ORE_RESOURCES_TOOL_TIP);
		oreResourceMultipliersComboBox.setBounds(228, 352, 137, 26);
		contentPanel.add(oreResourceMultipliersComboBox);
		
		JLabel lblGoldResource = JLabelFactory.createJLabel("Gold resources:");
		lblGoldResource.setToolTipText(GOLD_RESOURCES_TOOL_TIP);
		lblGoldResource.setBounds(25, 394, 191, 26);
		contentPanel.add(lblGoldResource);
		
		goldResourceMultipliersComboBox = JComboBoxFactory.createJComboBox(getResourceMultipliers(), imageInfoReader);
		goldResourceMultipliersComboBox.setSelectedIndex(defaultResourceSelection);
		goldResourceMultipliersComboBox.setForeground(Color.BLACK);
		goldResourceMultipliersComboBox.setToolTipText(GOLD_RESOURCES_TOOL_TIP);
		goldResourceMultipliersComboBox.setBounds(228, 394, 137, 26);
		contentPanel.add(goldResourceMultipliersComboBox);
		
		JLabel lblNumberOfEnemies = JLabelFactory.createJLabel("Enemy density:");
		lblNumberOfEnemies.setToolTipText(MONSTER_DENSITY_TOOL_TIP);
		lblNumberOfEnemies.setBounds(25, 436, 191, 26);
		contentPanel.add(lblNumberOfEnemies);
		
		numberOfEnemiesTextField = JTextFieldFactory.createJTextField();
		numberOfEnemiesTextField.setToolTipText(MONSTER_DENSITY_TOOL_TIP);
		numberOfEnemiesTextField.setText(Integer.toString(customGameParameters.getEnemyDensity()));
		numberOfEnemiesTextField.setColumns(10);
		numberOfEnemiesTextField.setBounds(228, 436, 137, 22);
		contentPanel.add(numberOfEnemiesTextField);
		
		JLabel lblNumberOfVillagers = JLabelFactory.createJLabel("Number of Villagers:");
		lblNumberOfVillagers.setToolTipText(NUMBER_OF_VILLAGERS_TOOL_TIP);
		lblNumberOfVillagers.setBounds(25, 478, 191, 26);
		contentPanel.add(lblNumberOfVillagers);
		
		numberOfVillagersTextField = JTextFieldFactory.createJTextField();
		numberOfVillagersTextField.setToolTipText(NUMBER_OF_VILLAGERS_TOOL_TIP);
		numberOfVillagersTextField.setText(Integer.toString(customGameParameters.getVillagerCount()));
		numberOfVillagersTextField.setColumns(10);
		numberOfVillagersTextField.setBounds(228, 478, 137, 22);
		contentPanel.add(numberOfVillagersTextField);
		
		JButton btnOk = JButtonFactory.createButton("Ok", imageInfoReader, soundIdReader);
		btnOk.setBounds(280, 635, 97, 25);
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
						int startTurn = Integer.parseInt(startTurnTextField.getText());
						
						String gender = maleRadioButton.isSelected() ? "male" : "female";
						
						float stoneResourceMultiplier = ((ResourceMultiplier)stoneResourceMultipliersComboBox.getSelectedItem()).getMultiplier();
						float oreResourceMultiplier = ((ResourceMultiplier)oreResourceMultipliersComboBox.getSelectedItem()).getMultiplier();
						float goldResourceMultiplier = ((ResourceMultiplier)goldResourceMultipliersComboBox.getSelectedItem()).getMultiplier();
						
						CustomGameParameters customGameParameters = new CustomGameParameters(playerNameTextField.getText(), playerProfessionTextField.getText(), gender, worldWidth, worldHeight, enemyDensity, villagerCount, seed, startTurn, stoneResourceMultiplier, oreResourceMultiplier, goldResourceMultiplier);
						new Thread() {
							public void run() {
								try {
									Game.run(characterAttributes, imageInfoReader, soundIdReader, musicPlayer, (ImageIds)cmbImage.getSelectedItem(), customGameParameters, keyBindings);
								} catch (Exception e) {
									ExceptionHandler.handle(e);
								}
							}
						}.start();
					} catch (Exception e1) {
						ExceptionHandler.handle(e1);
					}
				} else {
					StringBuilder buffer = new StringBuilder();
					buffer.append("<html>Problem validating input fields:<br>");
					for(String error : errors) {
						buffer.append(error).append("<br>");
					}
					buffer.append("</html");
					new ShowTextDialog(buffer.toString(), imageInfoReader, soundIdReader, frame).showMe();
				}
			}
		});
		
		JButton btnCancel = JButtonFactory.createButton("Cancel", imageInfoReader, soundIdReader);
		btnCancel.setBounds(169, 635, 97, 25);
		contentPanel.add(btnCancel);
		
		JLabel lblPlayerProfession = JLabelFactory.createJLabel("Character Profession:");
		lblPlayerProfession.setToolTipText(CHARACTER_PROFESSION_TOOL_TIP);
		lblPlayerProfession.setBounds(25, 69, 191, 26);
		contentPanel.add(lblPlayerProfession);
		
		playerProfessionTextField = JTextFieldFactory.createJTextField();
		playerProfessionTextField.setToolTipText(CHARACTER_PROFESSION_TOOL_TIP);
		playerProfessionTextField.setText(customGameParameters.getPlayerProfession());
		playerProfessionTextField.setColumns(10);
		playerProfessionTextField.setBounds(228, 69, 137, 22);
		contentPanel.add(playerProfessionTextField);
		
		JLabel lblSeed = JLabelFactory.createJLabel("Seed:");
		lblSeed.setToolTipText(SEED_TOOL_TIP);
		lblSeed.setBounds(25, 520, 191, 26);
		contentPanel.add(lblSeed);
		
		seedTextField = JTextFieldFactory.createJTextField();
		seedTextField.setToolTipText(SEED_TOOL_TIP);
		seedTextField.setText(Integer.toString(customGameParameters.getSeed()));
		seedTextField.setColumns(10);
		seedTextField.setBounds(228, 520, 137, 22);
		contentPanel.add(seedTextField);
		
		JLabel lblCharacterImage = JLabelFactory.createJLabel("Character image:");
		lblCharacterImage.setToolTipText("choose gender of player character");
		lblCharacterImage.setBounds(25, 152, 191, 26);
		lblCharacterImage.setCursor(Cursors.CURSOR);
		contentPanel.add(lblCharacterImage);
		
		cmbImage = JComboBoxFactory.createJComboBox(imageInfoReader);
		cmbImage.setModel(new ImageComboBoxModel(imageInfoReader));
		cmbImage.setRenderer(new ImageComboBoxCellRenderer(imageInfoReader));
		cmbImage.setSelectedIndex(0);
		cmbImage.setBounds(228, 160, 137, 58);
		contentPanel.add(cmbImage);
		
		JLabel lblStartTurn = JLabelFactory.createJLabel("<html>Start " + imageInfoReader.smallImageTag(ImageIds.SMALL_TURN) + " turn:</html>");
		lblStartTurn.setToolTipText(START_TURN_TOOL_TIP);
		lblStartTurn.setBounds(25, 562, 191, 26);
		contentPanel.add(lblStartTurn);
		
		startTurnTextField = JTextFieldFactory.createJTextField();
		startTurnTextField.setToolTipText(START_TURN_TOOL_TIP);
		startTurnTextField.setText(Integer.toString(customGameParameters.getStartTurn()));
		startTurnTextField.setColumns(10);
		startTurnTextField.setBounds(228, 562, 137, 22);
		contentPanel.add(startTurnTextField);
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				StartScreen startScreen = new StartScreen(imageInfoReader, soundIdReader, musicPlayer);
				startScreen.setVisible(true);
			}
		});
		
		if (parentFrame != null) {
			SwingUtils.installEscapeCloseOperation(frame);
			DialogUtils.createDialogBackPanel(frame, parentFrame.getContentPane());
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
		
		if (!NumberUtils.isNumeric(startTurnTextField.getText())) {
			errors.add("Start turn must be numeric");
		}
		
		if (NumberUtils.isNumeric(startTurnTextField.getText()) && Integer.parseInt(startTurnTextField.getText()) < 0) {
			errors.add("Start turn must be 0 or greater");
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
	
	private static class ResourceMultiplier {
		private final float multiplier;
		private final String description;

		public ResourceMultiplier(float multiplier, String description) {
			super();
			this.multiplier = multiplier;
			this.description = description;
		}
		
		public float getMultiplier() {
			return multiplier;
		}
		
		public String getDescription() {
			return description;
		}

		@Override
		public String toString() {
			return getDescription();
		}		
	}
	
	private ResourceMultiplier[] getResourceMultipliers() {
		List<ResourceMultiplier> resourceMultipliers = new ArrayList<>();
		resourceMultipliers.add(new ResourceMultiplier(0.0f, "Nonexistent"));
		resourceMultipliers.add(new ResourceMultiplier(0.5f, "Scarce"));
		resourceMultipliers.add(new ResourceMultiplier(1.0f, "Normal"));
		resourceMultipliers.add(new ResourceMultiplier(1.5f, "Abundant"));
		return resourceMultipliers.toArray(new ResourceMultiplier[0]);
	}
}
