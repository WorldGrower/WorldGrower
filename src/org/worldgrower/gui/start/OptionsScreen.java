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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.worldgrower.attribute.GhostImageIds;
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
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.JTextFieldFactory;
import org.worldgrower.terrain.TerrainMapper;
import org.worldgrower.util.NumberUtils;

public class OptionsScreen {	
	private static final String MONSTER_DENSITY_TOOL_TIP = "indicates whether there are monsters when the game starts: 0 indicates no, more than 0 indicates yes";
	private static final String NUMBER_OF_VILLAGERS_TOOL_TIP = "Set starting number of villagers";
	private static final String WORLD_WIDTH_TOOL_TIP = "Sets width of world in number of tiles";
	private static final String WORLD_HEIGHT_TOOL_TIP = "Sets height of world in number of tiles";
	private static final String START_TURN_TOOL_TIP = "Sets turn on which the player character enters the world";
	private static final String STONE_RESOURCES_TOOL_TIP = "Sets abundance of stone resources";
	private static final String ORE_RESOURCES_TOOL_TIP = "Sets abundance of iron ore resources";
	private static final String GOLD_RESOURCES_TOOL_TIP = "Sets abundance of gold resources";
	private static final String OIL_RESOURCES_TOOL_TIP = "Sets abundance of oil resources";
	private static final String WATER_CUTOFF_TOOL_TIP = "Sets abundance of water";
	private static final String SEED_TOOL_TIP = "The seed is used for random number generation. A different value will result in different villagers which make other decisions";

	private JFrame frame;
	private JTextField worldWidthTextField;
	private JTextField worldHeightTextField;
	private JTextField numberOfEnemiesTextField;
	private JTextField numberOfVillagersTextField;
	private JTextField seedTextField;

	private final CharacterAttributes characterAttributes;
	private final PlayerCharacterInfo playerCharacterInfo;
	private final SoundIdReader soundIdReader;
	private final MusicPlayer musicPlayer;
	
	private final KeyBindings keyBindings;
	private final GhostImageIds ghostImageIds;
	private JTextField startTurnTextField;
	private JComboBox<ResourceMultiplier> stoneResourceMultipliersComboBox;
	private JComboBox<ResourceMultiplier> oreResourceMultipliersComboBox;
	private JComboBox<ResourceMultiplier> goldResourceMultipliersComboBox;
	private JComboBox<ResourceMultiplier> oilResourceMultipliersComboBox;
	private JComboBox<WaterCutoff> waterCutoffComboBox;
	
	public OptionsScreen(CharacterAttributes characterAttributes, PlayerCharacterInfo playerCharacterInfo, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, KeyBindings keyBindings, GhostImageIds ghostImageIds, JFrame parentFrame) {
		this.characterAttributes = characterAttributes;
		this.playerCharacterInfo = playerCharacterInfo;
		this.soundIdReader = soundIdReader;
		this.musicPlayer = musicPlayer;
		this.keyBindings = keyBindings;
		this.ghostImageIds = ghostImageIds;
		
		initialize(imageInfoReader, parentFrame);
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(ImageInfoReader imageInfoReader, JFrame parentFrame) {
		frame = new JFrame("World Customization");
		frame.setResizable(false);
		((JComponent)frame.getRootPane()).setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		JPanel contentPanel = new TiledImagePanel(imageInfoReader);
		contentPanel.setLocation(0, 0);
		contentPanel.setLayout(null);
		frame.setUndecorated(true);
		int width = 440;
		int height = 582;
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
		
		JPanel worldInfoPanel = JPanelFactory.createJPanel("World Info");
		worldInfoPanel.setLayout(null);
		worldInfoPanel.setBounds(20, 20, 400, 502);
		contentPanel.add(worldInfoPanel);
		
		CustomGameParameters customGameParameters = new CustomGameParameters();
		
		JLabel lblWorldWidth = JLabelFactory.createJLabel("World Width:");
		lblWorldWidth.setToolTipText(WORLD_WIDTH_TOOL_TIP);
		lblWorldWidth.setBounds(25, 34, 191, 26);
		worldInfoPanel.add(lblWorldWidth);
		
		worldWidthTextField = JTextFieldFactory.createJTextField();
		worldWidthTextField.setToolTipText(WORLD_WIDTH_TOOL_TIP);
		worldWidthTextField.setText(Integer.toString(customGameParameters.getWorldWidth()));
		worldWidthTextField.setBounds(228, 34, 137, 22);
		worldInfoPanel.add(worldWidthTextField);
		worldWidthTextField.setColumns(10);
		
		JLabel lblWorldHeight = JLabelFactory.createJLabel("World Height:");
		lblWorldHeight.setToolTipText(WORLD_HEIGHT_TOOL_TIP);
		lblWorldHeight.setBounds(25, 73, 191, 26);
		worldInfoPanel.add(lblWorldHeight);
		
		worldHeightTextField = JTextFieldFactory.createJTextField();
		worldHeightTextField.setToolTipText(WORLD_HEIGHT_TOOL_TIP);
		worldHeightTextField.setText(Integer.toString(customGameParameters.getWorldHeight()));
		worldHeightTextField.setColumns(10);
		worldHeightTextField.setBounds(228, 73, 137, 22);
		worldInfoPanel.add(worldHeightTextField);
		
		final int defaultResourceSelection = 2;
		
		JLabel lblStoneResource = JLabelFactory.createJLabel("Stone resources:");
		lblStoneResource.setToolTipText(STONE_RESOURCES_TOOL_TIP);
		lblStoneResource.setBounds(25, 110, 191, 26);
		worldInfoPanel.add(lblStoneResource);
		
		stoneResourceMultipliersComboBox = JComboBoxFactory.createJComboBox(getResourceMultipliers(), imageInfoReader);
		stoneResourceMultipliersComboBox.setSelectedIndex(defaultResourceSelection);
		stoneResourceMultipliersComboBox.setForeground(Color.BLACK);
		stoneResourceMultipliersComboBox.setToolTipText(STONE_RESOURCES_TOOL_TIP);
		stoneResourceMultipliersComboBox.setBounds(228, 110, 137, 26);
		worldInfoPanel.add(stoneResourceMultipliersComboBox);
		
		JLabel lblOreResource = JLabelFactory.createJLabel("Iron ore resources:");
		lblOreResource.setToolTipText(ORE_RESOURCES_TOOL_TIP);
		lblOreResource.setBounds(25, 152, 191, 26);
		worldInfoPanel.add(lblOreResource);
		
		oreResourceMultipliersComboBox = JComboBoxFactory.createJComboBox(getResourceMultipliers(), imageInfoReader);
		oreResourceMultipliersComboBox.setSelectedIndex(defaultResourceSelection);
		oreResourceMultipliersComboBox.setForeground(Color.BLACK);
		oreResourceMultipliersComboBox.setToolTipText(ORE_RESOURCES_TOOL_TIP);
		oreResourceMultipliersComboBox.setBounds(228, 152, 137, 26);
		worldInfoPanel.add(oreResourceMultipliersComboBox);
		
		JLabel lblGoldResource = JLabelFactory.createJLabel("Gold resources:");
		lblGoldResource.setToolTipText(GOLD_RESOURCES_TOOL_TIP);
		lblGoldResource.setBounds(25, 194, 191, 26);
		worldInfoPanel.add(lblGoldResource);
		
		goldResourceMultipliersComboBox = JComboBoxFactory.createJComboBox(getResourceMultipliers(), imageInfoReader);
		goldResourceMultipliersComboBox.setSelectedIndex(defaultResourceSelection);
		goldResourceMultipliersComboBox.setForeground(Color.BLACK);
		goldResourceMultipliersComboBox.setToolTipText(GOLD_RESOURCES_TOOL_TIP);
		goldResourceMultipliersComboBox.setBounds(228, 194, 137, 26);
		worldInfoPanel.add(goldResourceMultipliersComboBox);
		
		JLabel lblOilResource = JLabelFactory.createJLabel("Oil resources:");
		lblOilResource.setToolTipText(OIL_RESOURCES_TOOL_TIP);
		lblOilResource.setBounds(25, 236, 191, 26);
		worldInfoPanel.add(lblOilResource);
		
		oilResourceMultipliersComboBox = JComboBoxFactory.createJComboBox(getResourceMultipliers(), imageInfoReader);
		oilResourceMultipliersComboBox.setSelectedIndex(defaultResourceSelection);
		oilResourceMultipliersComboBox.setForeground(Color.BLACK);
		oilResourceMultipliersComboBox.setToolTipText(OIL_RESOURCES_TOOL_TIP);
		oilResourceMultipliersComboBox.setBounds(228, 236, 137, 26);
		worldInfoPanel.add(oilResourceMultipliersComboBox);
		
		JLabel lblWaterCutoff = JLabelFactory.createJLabel("Water availability:");
		lblWaterCutoff.setToolTipText(WATER_CUTOFF_TOOL_TIP);
		lblWaterCutoff.setBounds(25, 278, 191, 26);
		worldInfoPanel.add(lblWaterCutoff);
		
		waterCutoffComboBox = JComboBoxFactory.createJComboBox(getWaterCutoffs(), imageInfoReader);
		waterCutoffComboBox.setSelectedIndex(2);
		waterCutoffComboBox.setForeground(Color.BLACK);
		waterCutoffComboBox.setToolTipText(WATER_CUTOFF_TOOL_TIP);
		waterCutoffComboBox.setBounds(228, 278, 137, 26);
		worldInfoPanel.add(waterCutoffComboBox);
		
		JLabel lblNumberOfEnemies = JLabelFactory.createJLabel("Enemy density:");
		lblNumberOfEnemies.setToolTipText(MONSTER_DENSITY_TOOL_TIP);
		lblNumberOfEnemies.setBounds(25, 320, 191, 26);
		worldInfoPanel.add(lblNumberOfEnemies);
		
		numberOfEnemiesTextField = JTextFieldFactory.createJTextField();
		numberOfEnemiesTextField.setToolTipText(MONSTER_DENSITY_TOOL_TIP);
		numberOfEnemiesTextField.setText(Integer.toString(customGameParameters.getEnemyDensity()));
		numberOfEnemiesTextField.setColumns(10);
		numberOfEnemiesTextField.setBounds(228, 320, 137, 22);
		worldInfoPanel.add(numberOfEnemiesTextField);
		
		JLabel lblNumberOfVillagers = JLabelFactory.createJLabel("Number of Villagers:");
		lblNumberOfVillagers.setToolTipText(NUMBER_OF_VILLAGERS_TOOL_TIP);
		lblNumberOfVillagers.setBounds(25, 362, 191, 26);
		worldInfoPanel.add(lblNumberOfVillagers);
		
		numberOfVillagersTextField = JTextFieldFactory.createJTextField();
		numberOfVillagersTextField.setToolTipText(NUMBER_OF_VILLAGERS_TOOL_TIP);
		numberOfVillagersTextField.setText(Integer.toString(customGameParameters.getVillagerCount()));
		numberOfVillagersTextField.setColumns(10);
		numberOfVillagersTextField.setBounds(228, 362, 137, 22);
		worldInfoPanel.add(numberOfVillagersTextField);
		
		JButton btnOk = JButtonFactory.createButton("Ok", imageInfoReader, soundIdReader);
		btnOk.setBounds(319, 532, 97, 25);
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
						
						String gender = playerCharacterInfo.getGender();
						
						float stoneResourceMultiplier = ((ResourceMultiplier)stoneResourceMultipliersComboBox.getSelectedItem()).getMultiplier();
						float oreResourceMultiplier = ((ResourceMultiplier)oreResourceMultipliersComboBox.getSelectedItem()).getMultiplier();
						float goldResourceMultiplier = ((ResourceMultiplier)goldResourceMultipliersComboBox.getSelectedItem()).getMultiplier();
						float oilResourceMultiplier = ((ResourceMultiplier)oilResourceMultipliersComboBox.getSelectedItem()).getMultiplier();
						
						double waterCutoff = ((WaterCutoff)waterCutoffComboBox.getSelectedItem()).getCutoff();
						CustomGameParameters customGameParameters = new CustomGameParameters(playerCharacterInfo.getPlayerName(), playerCharacterInfo.getPlayerProfession(), gender, worldWidth, worldHeight, enemyDensity, villagerCount, seed, startTurn, stoneResourceMultiplier, oreResourceMultiplier, goldResourceMultiplier, oilResourceMultiplier, waterCutoff);
						new Thread() {
							public void run() {
								try {
									Game.run(characterAttributes, imageInfoReader, soundIdReader, musicPlayer, playerCharacterInfo.getImageId(), customGameParameters, keyBindings, ghostImageIds);
								} catch (Exception e) {
									ExceptionHandler.handle(e);
								}
							}
						}.start();
					} catch (Exception e1) {
						ExceptionHandler.handle(e1);
					}
				} else {
					ErrorDialog.showErrors(errors, imageInfoReader, soundIdReader, frame);
				}
			}
		});
		
		JButton btnCancel = JButtonFactory.createButton("Cancel", imageInfoReader, soundIdReader);
		btnCancel.setBounds(208, 532, 97, 25);
		contentPanel.add(btnCancel);
		
		JLabel lblSeed = JLabelFactory.createJLabel("Seed:");
		lblSeed.setToolTipText(SEED_TOOL_TIP);
		lblSeed.setBounds(25, 404, 191, 26);
		worldInfoPanel.add(lblSeed);
		
		seedTextField = JTextFieldFactory.createJTextField();
		seedTextField.setToolTipText(SEED_TOOL_TIP);
		seedTextField.setText(Integer.toString(customGameParameters.getSeed()));
		seedTextField.setColumns(10);
		seedTextField.setBounds(228, 404, 137, 22);
		worldInfoPanel.add(seedTextField);
		
		JLabel lblStartTurn = JLabelFactory.createJLabel("<html>Start turn" + imageInfoReader.smallImageTag(ImageIds.SMALL_TURN) + ":</html>");
		lblStartTurn.setToolTipText(START_TURN_TOOL_TIP);
		lblStartTurn.setBounds(25, 446, 191, 26);
		worldInfoPanel.add(lblStartTurn);
		
		startTurnTextField = JTextFieldFactory.createJTextField();
		startTurnTextField.setToolTipText(START_TURN_TOOL_TIP);
		startTurnTextField.setText(Integer.toString(customGameParameters.getStartTurn()));
		startTurnTextField.setColumns(10);
		startTurnTextField.setBounds(228, 446, 137, 22);
		worldInfoPanel.add(startTurnTextField);
		
		Action cancelAction = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				StartScreen startScreen = new StartScreen(imageInfoReader, soundIdReader, musicPlayer);
				startScreen.setVisible(true);
			}
		};
		btnCancel.addActionListener(cancelAction);
		
		if (parentFrame != null) {
			SwingUtils.installEscapeCloseOperation(frame);
			DialogUtils.createDialogBackPanel(frame, parentFrame.getContentPane());
		} else {
			SwingUtils.installCloseAction(cancelAction, frame.getRootPane());
		}
	}
	
	private List<String> validateInput() {
		List<String> errors = new ArrayList<>();
		
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
	
	private static class WaterCutoff {
		private final double cutoff;
		private final String description;
		
		public WaterCutoff(double cutoff, String description) {
			super();
			this.cutoff = cutoff;
			this.description = description;
		}

		public double getCutoff() {
			return cutoff;
		}

		public String getDescription() {
			return description;
		}

		@Override
		public String toString() {
			return getDescription();
		}
	}
	
	private WaterCutoff[] getWaterCutoffs() {
		List<WaterCutoff> waterCutoffs = new ArrayList<>();
		waterCutoffs.add(new WaterCutoff(TerrainMapper.NO_WATER_CUTOFF, "Nonexistent"));
		waterCutoffs.add(new WaterCutoff(TerrainMapper.SCARCE_WATER_CUTOFF, "Scarce"));
		waterCutoffs.add(new WaterCutoff(TerrainMapper.NORMAL_WATER_CUTOFF, "Normal"));
		waterCutoffs.add(new WaterCutoff(TerrainMapper.ABUNDANT_WATER_CUTOFF, "Abundant"));
		waterCutoffs.add(new WaterCutoff(TerrainMapper.HUGE_WATER_CUTOFF, "Huge"));
		return waterCutoffs.toArray(new WaterCutoff[0]);
	}
}
