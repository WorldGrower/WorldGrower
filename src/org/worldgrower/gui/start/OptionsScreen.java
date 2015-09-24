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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.worldgrower.Main;
import org.worldgrower.gui.ExceptionHandler;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.util.NumberUtils;
import javax.swing.JCheckBox;

public class OptionsScreen {
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
	private JTextField worldWidthTextField;
	private JTextField worldHeightTextField;
	private JTextField numberOfEnemiesTextField;
	private JTextField numberOfVillagersTextField;
	private JTextField playerProfessionTextField;
	private JTextField seedTextField;

	private final CharacterAttributes characterAttributes;
	private JCheckBox chkBackgroundMusic;
	
	/**
	 * Create the application.
	 * @param characterAttributes 
	 */
	public OptionsScreen(CharacterAttributes characterAttributes) {
		initialize();
		this.characterAttributes = characterAttributes;
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 414, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		IconUtils.setIcon(frame);
		
		JLabel lblPlayerName = new JLabel("Character Name:");
		lblPlayerName.setToolTipText(PLAYER_NAME_TOOL_TIP);
		lblPlayerName.setBounds(25, 30, 191, 26);
		frame.getContentPane().add(lblPlayerName);
		
		playerNameTextField = new JTextField();
		playerNameTextField.setToolTipText(PLAYER_NAME_TOOL_TIP);
		playerNameTextField.setText("MyName");
		playerNameTextField.setBounds(228, 30, 137, 22);
		frame.getContentPane().add(playerNameTextField);
		playerNameTextField.setColumns(10);
		
		JLabel lblWorldWidth = new JLabel("World Width:");
		lblWorldWidth.setToolTipText(WORLD_WIDTH_TOOL_TIP);
		lblWorldWidth.setBounds(25, 123, 191, 26);
		frame.getContentPane().add(lblWorldWidth);
		
		worldWidthTextField = new JTextField();
		worldWidthTextField.setToolTipText(WORLD_WIDTH_TOOL_TIP);
		worldWidthTextField.setText("100");
		worldWidthTextField.setBounds(228, 123, 137, 22);
		frame.getContentPane().add(worldWidthTextField);
		worldWidthTextField.setColumns(10);
		
		JLabel lblWorldHeight = new JLabel("World Height:");
		lblWorldHeight.setToolTipText(WORLD_HEIGHT_TOOL_TIP);
		lblWorldHeight.setBounds(25, 162, 191, 26);
		frame.getContentPane().add(lblWorldHeight);
		
		worldHeightTextField = new JTextField();
		worldHeightTextField.setToolTipText(WORLD_HEIGHT_TOOL_TIP);
		worldHeightTextField.setText("100");
		worldHeightTextField.setColumns(10);
		worldHeightTextField.setBounds(228, 160, 137, 22);
		frame.getContentPane().add(worldHeightTextField);
		
		JLabel lblNumberOfEnemies = new JLabel("Enemy density:");
		lblNumberOfEnemies.setToolTipText(MONSTER_DENSITY_TOOL_TIP);
		lblNumberOfEnemies.setBounds(25, 201, 191, 26);
		frame.getContentPane().add(lblNumberOfEnemies);
		
		numberOfEnemiesTextField = new JTextField();
		numberOfEnemiesTextField.setToolTipText(MONSTER_DENSITY_TOOL_TIP);
		numberOfEnemiesTextField.setText("0");
		numberOfEnemiesTextField.setColumns(10);
		numberOfEnemiesTextField.setBounds(228, 199, 137, 22);
		frame.getContentPane().add(numberOfEnemiesTextField);
		
		JLabel lblNumberOfVillagers = new JLabel("Number of Villagers:");
		lblNumberOfVillagers.setToolTipText(NUMBER_OF_VILLAGERS_TOOL_TIP);
		lblNumberOfVillagers.setBounds(25, 241, 191, 26);
		frame.getContentPane().add(lblNumberOfVillagers);
		
		numberOfVillagersTextField = new JTextField();
		numberOfVillagersTextField.setToolTipText(NUMBER_OF_VILLAGERS_TOOL_TIP);
		numberOfVillagersTextField.setText("4");
		numberOfVillagersTextField.setColumns(10);
		numberOfVillagersTextField.setBounds(228, 241, 137, 22);
		frame.getContentPane().add(numberOfVillagersTextField);
		
		JButton btnOk = new JButton("Ok");
		btnOk.setBounds(230, 415, 97, 25);
		frame.getRootPane().setDefaultButton(btnOk);
		frame.getContentPane().add(btnOk);
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
						Main.run(playerNameTextField.getText(), playerProfessionTextField.getText(), worldWidth, worldHeight, enemyDensity, villagerCount, seed, playBackgroundMusic, characterAttributes);
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
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(121, 415, 97, 25);
		frame.getContentPane().add(btnCancel);
		
		JLabel lblPlayerProfession = new JLabel("Character Profession:");
		lblPlayerProfession.setToolTipText(CHARACTER_PROFESSION_TOOL_TIP);
		lblPlayerProfession.setBounds(25, 69, 191, 26);
		frame.getContentPane().add(lblPlayerProfession);
		
		playerProfessionTextField = new JTextField();
		playerProfessionTextField.setToolTipText(CHARACTER_PROFESSION_TOOL_TIP);
		playerProfessionTextField.setText("adventurer");
		playerProfessionTextField.setColumns(10);
		playerProfessionTextField.setBounds(228, 69, 137, 22);
		frame.getContentPane().add(playerProfessionTextField);
		
		JLabel lblSeed = new JLabel("Seed:");
		lblSeed.setToolTipText(SEED_TOOL_TIP);
		lblSeed.setBounds(25, 283, 191, 26);
		frame.getContentPane().add(lblSeed);
		
		seedTextField = new JTextField();
		seedTextField.setToolTipText(SEED_TOOL_TIP);
		seedTextField.setText("666");
		seedTextField.setColumns(10);
		seedTextField.setBounds(228, 283, 137, 22);
		frame.getContentPane().add(seedTextField);
		
		chkBackgroundMusic = new JCheckBox("Music");
		chkBackgroundMusic.setToolTipText(MUSIC_TOOL_TIP);
		chkBackgroundMusic.setSelected(true);
		chkBackgroundMusic.setBounds(228, 340, 137, 25);
		frame.getContentPane().add(chkBackgroundMusic);
		
		JLabel lblPlayBackgroundMusic = new JLabel("Play background music:");
		lblPlayBackgroundMusic.setToolTipText(MUSIC_TOOL_TIP);
		lblPlayBackgroundMusic.setToolTipText("The seed is used for random number generation. A different value will result in different villagers which make other decisions");
		lblPlayBackgroundMusic.setBounds(25, 339, 191, 26);
		frame.getContentPane().add(lblPlayBackgroundMusic);
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				StartScreen startScreen = new StartScreen();
				startScreen.setVisible(true);
			}
		});
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
