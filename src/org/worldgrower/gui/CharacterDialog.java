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
package org.worldgrower.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Conditions;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.CommonerOnTurn;
import org.worldgrower.goal.ArmorPropertyUtils;
import org.worldgrower.goal.BountyPropertyUtils;
import org.worldgrower.goal.MeleeDamagePropertyUtils;
import org.worldgrower.gui.cursor.Cursors;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JComboBoxFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JListFactory;
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.JProgressBarFactory;
import org.worldgrower.gui.util.JScrollPaneFactory;

public class CharacterDialog extends JDialog {

	private static final String HEAD_TOOL_TIP = "Head equipment slot";
	private static final String TORSO_TOOL_TIP = "Torso equipment slot";
	private static final String ARMS_TOOL_TIP = "Arms equipment slot";
	private static final String LEGS_TOOL_TIP = "Legs equipment slot";
	private static final String FEET_TOOL_TIP = "Feet equipment slot";
	private static final String LEFT_HAND_TOOL_TIP = "Left hand equipment slot";
	private static final String RIGHT_HAND_TOOL_TIP = "Right hand equipment slot";
	
	private static final String ARMOR_TOOL_TIP = "Armor reduces the damage taken from non-magical attacks";
	private static final String DAMAGE_RESIST_TOOL_TIP = "Damage from weapon attacks is reduced by this percentage";
	private static final String WEAPON_TOOL_TIP = "Damage indicates the damage done when performing melee or ranged attacks";
	private static final String ACTIVE_EFFECT_TOOL_TIP = "Shows active effects";
	
	private final JPanel contentPanel;
	private final WorldObject playerCharacter;
	
	private JComboBox<ComboBoxEquipmentItem> cmbHead;
	private JComboBox<ComboBoxEquipmentItem> cmbTorso;
	private JComboBox<ComboBoxEquipmentItem> cmbArms;
	private JComboBox<ComboBoxEquipmentItem> cmbLegs;
	private JComboBox<ComboBoxEquipmentItem> cmbFeet;
	private JLabel lblArmorValue;
	private JLabel lblDamageResist;
	private JComboBox<ComboBoxEquipmentItem> cmbLeftHand;
	private JComboBox<ComboBoxEquipmentItem> cmbRightHand;
	private JLabel lblDamageValue;

	public CharacterDialog(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, JFrame parentFrame) {
		super(parentFrame, true);
		contentPanel = new TiledImagePanel(imageInfoReader);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setUndecorated(true);
		IconUtils.setIcon(this);
		setResizable(false);
		setCursor(Cursors.CURSOR);
		
		this.playerCharacter = playerCharacter;
		
		int width = 1100;
		int height = 800;
		setSize(width, height);
		contentPanel.setPreferredSize(new Dimension(width, height));
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		Image characterImage = imageInfoReader.getImage(playerCharacter.getProperty(Constants.IMAGE_ID), null);
		String characterName = playerCharacter.getProperty(Constants.NAME);
		JLabel lblName = JLabelFactory.createJLabel(characterName, characterImage);
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		lblName.setBounds(12, 13, 120, 50);
		lblName.setToolTipText("displays character image and name");
		contentPanel.add(lblName);
		
		final JLabel lblDeity;
		Deity deity = playerCharacter.getProperty(Constants.DEITY);
		if (deity != null) {
			String deityDescription = deity.getName();
			Image deityImage = imageInfoReader.getImage(deity.getBoonImageId(), null);
			lblDeity = JLabelFactory.createJLabel(deityDescription, deityImage);
			lblDeity.setHorizontalAlignment(SwingConstants.LEFT);
		} else {
			String deityDescription = "<no deity>";
			lblDeity = JLabelFactory.createJLabel(deityDescription);
		}
		
		lblDeity.setBounds(12, 60, 120, 50);
		lblDeity.setToolTipText("displays deity");
		contentPanel.add(lblDeity);
		
		JLabel lblProfession = JLabelFactory.createJLabel(playerCharacter.getProperty(Constants.PROFESSION).getDescription());
		lblProfession.setBounds(12, 110, 120, 20);
		lblProfession.setToolTipText("displays character profession");
		contentPanel.add(lblProfession);
		
		JLabel lblLevel = JLabelFactory.createJLabel("Level " + playerCharacter.getProperty(Constants.LEVEL));
		lblLevel.setBounds(12, 140, 120, 20);
		lblLevel.setToolTipText("displays character level");
		contentPanel.add(lblLevel);

		String bountyDescription = getBountyDescription(world);
		JLabel lblBounty = JLabelFactory.createJLabel(bountyDescription);
		lblBounty.setBounds(12, 170, 120, 20);
		lblBounty.setToolTipText("displays bounty");
		contentPanel.add(lblBounty);
		
		JPanel attributePanel = JPanelFactory.createJPanel("Attributes");
		attributePanel.setToolTipText("shows player character attributes");
		attributePanel.setBounds(150, 5, 223, 200);
		attributePanel.setLayout(null);
		contentPanel.add(attributePanel);
		
		JLabel lblStrength = createAttributeLabel(Constants.STRENGTH, "Strength");
		lblStrength.setBounds(13, 23, 120, 20);
		attributePanel.add(lblStrength);
		
		JLabel lblConstitution = createAttributeLabel(Constants.CONSTITUTION, "Constitution");
		lblConstitution.setBounds(13, 52, 120, 20);
		attributePanel.add(lblConstitution);
		
		JLabel lblDexterity = createAttributeLabel(Constants.DEXTERITY, "Dexterity");
		lblDexterity.setBounds(13, 81, 120, 20);
		attributePanel.add(lblDexterity);
		
		JLabel lblIntelligence = createAttributeLabel(Constants.INTELLIGENCE, "Intelligence");
		lblIntelligence.setBounds(13, 110, 120, 20);
		attributePanel.add(lblIntelligence);
		
		JLabel lblWisdom = createAttributeLabel(Constants.WISDOM, "Wisdom");
		lblWisdom.setBounds(13, 139, 120, 20);
		attributePanel.add(lblWisdom);
		
		JLabel lblCharisma = createAttributeLabel(Constants.CHARISMA, "Charisma");
		lblCharisma.setBounds(13, 168, 120, 20);
		attributePanel.add(lblCharisma);
		
		JLabel lblStrengthValue = JLabelFactory.createJLabel(playerCharacter.getProperty(Constants.STRENGTH).toString());
		lblStrengthValue.setBounds(150, 23, 20, 20);
		lblStrengthValue.setToolTipText(lblStrength.getToolTipText());
		attributePanel.add(lblStrengthValue);
		
		JLabel lblConstitutionValue = JLabelFactory.createJLabel(playerCharacter.getProperty(Constants.CONSTITUTION).toString());
		lblConstitutionValue.setBounds(150, 52, 20, 20);
		lblConstitutionValue.setToolTipText(lblConstitution.getToolTipText());
		attributePanel.add(lblConstitutionValue);
		
		JLabel lblDexterityValue = JLabelFactory.createJLabel(playerCharacter.getProperty(Constants.DEXTERITY).toString());
		lblDexterityValue.setBounds(150, 81, 20, 20);
		lblDexterityValue.setToolTipText(lblDexterity.getToolTipText());
		attributePanel.add(lblDexterityValue);
		
		JLabel lblIntelligenceValue = JLabelFactory.createJLabel(playerCharacter.getProperty(Constants.INTELLIGENCE).toString());
		lblIntelligenceValue.setBounds(150, 110, 20, 20);
		lblIntelligenceValue.setToolTipText(lblIntelligence.getToolTipText());
		attributePanel.add(lblIntelligenceValue);
		
		JLabel lblWisdomValue = JLabelFactory.createJLabel(playerCharacter.getProperty(Constants.WISDOM).toString());
		lblWisdomValue.setBounds(150, 139, 20, 20);
		lblWisdomValue.setToolTipText(lblWisdom.getToolTipText());
		attributePanel.add(lblWisdomValue);
		
		JLabel lblCharismaValue = JLabelFactory.createJLabel(playerCharacter.getProperty(Constants.CHARISMA).toString());
		lblCharismaValue.setBounds(150, 168, 20, 20);
		lblCharismaValue.setToolTipText(lblCharisma.getToolTipText());
		attributePanel.add(lblCharismaValue);

		JPanel specialAttributePanel = JPanelFactory.createJPanel("Special Attributes");
		specialAttributePanel.setToolTipText("Special attributes indicate the state of special conditions like pregnancy, vampire blood level, etc");
		specialAttributePanel.setBounds(380, 5, 250, 200);
		specialAttributePanel.setLayout(null);
		contentPanel.add(specialAttributePanel);
		
		JScrollPane specialAttributesScrollPane = JScrollPaneFactory.createScrollPane();
		specialAttributesScrollPane.setBounds(10, 23, 257, 163);
		specialAttributesScrollPane.setBorder(null);
		specialAttributePanel.add(specialAttributesScrollPane);
		
		JList<SpecialAttribute> specialAttributesList = JListFactory.createJList(getSpecialAttributes());
		specialAttributesList.setCellRenderer(new SpecialAttributesListCellRenderer());
		specialAttributesList.setOpaque(false);
		specialAttributesList.setBorder(null);
		specialAttributesScrollPane.setViewportView(specialAttributesList);
		
		JPanel socialPanel = createPanel("Social Skills", 12, 208, 300, 250);
		socialPanel.setToolTipText("shows player character social skills");
		createSkillBlock(Constants.BLUFF_SKILL, socialPanel, 12, 28);
		createSkillBlock(Constants.DIPLOMACY_SKILL, socialPanel, 12, 58);
		createSkillBlock(Constants.INSIGHT_SKILL, socialPanel, 12, 88);
		createSkillBlock(Constants.INTIMIDATE_SKILL, socialPanel, 12, 118);
		createSkillBlock(Constants.PERCEPTION_SKILL, socialPanel, 12, 148);
		
		JPanel combatPanel = createPanel("Combat Skills", 12, 470, 300, 250);
		combatPanel.setToolTipText("shows player character combat skills");
		createSkillBlock(Constants.ARCHERY_SKILL, combatPanel, 12, 28);
		createSkillBlock(Constants.HEAVY_ARMOR_SKILL, combatPanel, 12, 58);
		createSkillBlock(Constants.HAND_TO_HAND_SKILL, combatPanel, 12, 88);
		createSkillBlock(Constants.LIGHT_ARMOR_SKILL, combatPanel, 12, 118);
		createSkillBlock(Constants.ONE_HANDED_SKILL, combatPanel, 12, 148);
		createSkillBlock(Constants.SMITHING_SKILL, combatPanel, 12, 178);
		createSkillBlock(Constants.TWO_HANDED_SKILL, combatPanel, 12, 208);
		
		JPanel professionPanel = createPanel("Profession Skills", 330, 208, 300, 250);
		professionPanel.setToolTipText("shows player character profession skills");
		createSkillBlock(Constants.CARPENTRY_SKILL, professionPanel, 12, 28);
		createSkillBlock(Constants.FARMING_SKILL, professionPanel, 12, 58);
		createSkillBlock(Constants.FISHING_SKILL, professionPanel, 12, 88);
		createSkillBlock(Constants.LUMBERING_SKILL, professionPanel, 12, 118);
		createSkillBlock(Constants.MINING_SKILL, professionPanel, 12, 148);		
		createSkillBlock(Constants.THIEVERY_SKILL, professionPanel, 12, 178);
		createSkillBlock(Constants.WEAVING_SKILL, professionPanel, 12, 208);
		
		JPanel magicPanel = createPanel("Magic Skills", 330, 470, 300, 250);
		magicPanel.setToolTipText("shows player character magic skills");
		createSkillBlock(Constants.ALCHEMY_SKILL, magicPanel, 12, 28);
		createSkillBlock(Constants.ENCHANTMENT_SKILL, magicPanel, 12, 58);
		createSkillBlock(Constants.EVOCATION_SKILL, magicPanel, 12, 88);		
		createSkillBlock(Constants.ILLUSION_SKILL, magicPanel, 12, 118);
		createSkillBlock(Constants.NECROMANCY_SKILL, magicPanel, 12, 148);
		createSkillBlock(Constants.RESTORATION_SKILL, magicPanel, 12, 178);
		createSkillBlock(Constants.TRANSMUTATION_SKILL, magicPanel, 12, 208);
		
		int labelLeft = 700;
		
		JLabel lblHead = JLabelFactory.createJLabel("Head");
		lblHead.setToolTipText(HEAD_TOOL_TIP);
		lblHead.setBounds(labelLeft, 35, 80, 20);
		contentPanel.add(lblHead);
		
		JLabel lblTorso = JLabelFactory.createJLabel("Torso");
		lblTorso.setToolTipText(TORSO_TOOL_TIP);
		lblTorso.setBounds(labelLeft, 95, 80, 20);
		contentPanel.add(lblTorso);
		
		JLabel lblArms = JLabelFactory.createJLabel("Arms");
		lblArms.setToolTipText(ARMS_TOOL_TIP);
		lblArms.setBounds(labelLeft, 155, 80, 20);
		contentPanel.add(lblArms);
		
		JLabel lblLegs = JLabelFactory.createJLabel("Legs");
		lblLegs.setToolTipText(LEGS_TOOL_TIP);
		lblLegs.setBounds(labelLeft, 215, 80, 20);
		contentPanel.add(lblLegs);
		
		JLabel lblFeet = JLabelFactory.createJLabel("Feet");
		lblFeet.setToolTipText(FEET_TOOL_TIP);
		lblFeet.setBounds(labelLeft, 275, 80, 20);
		contentPanel.add(lblFeet);
		
		JLabel lblLeftHand = JLabelFactory.createJLabel("Left Hand");
		lblLeftHand.setToolTipText(LEFT_HAND_TOOL_TIP);
		lblLeftHand.setBounds(labelLeft, 335, 80, 20);
		contentPanel.add(lblLeftHand);
		
		JLabel lblRightHand = JLabelFactory.createJLabel("Right Hand");
		lblRightHand.setToolTipText(RIGHT_HAND_TOOL_TIP);
		lblRightHand.setBounds(labelLeft, 395, 80, 20);
		contentPanel.add(lblRightHand);
		
		WorldObjectContainer inventory = playerCharacter.getProperty(Constants.INVENTORY);
		
		int equipmentLeft = 810;
		
		cmbHead = createEquipmentComboBox(inventory, Constants.HEAD_EQUIPMENT, imageInfoReader);
		cmbHead.setToolTipText(HEAD_TOOL_TIP);
		cmbHead.setBounds(equipmentLeft, 15, 243, 50);
		contentPanel.add(cmbHead);
		
		cmbTorso = createEquipmentComboBox(inventory, Constants.TORSO_EQUIPMENT, imageInfoReader);
		cmbTorso.setToolTipText(TORSO_TOOL_TIP);
		cmbTorso.setBounds(equipmentLeft, 75, 243, 50);
		contentPanel.add(cmbTorso);
		
		cmbArms = createEquipmentComboBox(inventory, Constants.ARMS_EQUIPMENT, imageInfoReader);
		cmbArms.setToolTipText(ARMS_TOOL_TIP);
		cmbArms.setBounds(equipmentLeft, 135, 243, 50);
		contentPanel.add(cmbArms);
		
		cmbLegs = createEquipmentComboBox(inventory, Constants.LEGS_EQUIPMENT, imageInfoReader);
		cmbLegs.setToolTipText(LEGS_TOOL_TIP);
		cmbLegs.setBounds(equipmentLeft, 195, 243, 50);
		contentPanel.add(cmbLegs);
		
		cmbFeet = createEquipmentComboBox(inventory, Constants.FEET_EQUIPMENT, imageInfoReader);
		cmbFeet.setToolTipText(FEET_TOOL_TIP);
		cmbFeet.setBounds(equipmentLeft, 255, 243, 50);
		contentPanel.add(cmbFeet);
		
		cmbLeftHand = createEquipmentComboBox(inventory, Constants.LEFT_HAND_EQUIPMENT, imageInfoReader);
		cmbLeftHand.setToolTipText(LEFT_HAND_TOOL_TIP);
		cmbLeftHand.setBounds(equipmentLeft, 315, 243, 50);
		contentPanel.add(cmbLeftHand);
		
		cmbRightHand = createEquipmentComboBox(inventory, Constants.RIGHT_HAND_EQUIPMENT, imageInfoReader);
		cmbRightHand.setToolTipText(RIGHT_HAND_TOOL_TIP);
		cmbRightHand.setBounds(equipmentLeft, 375, 243, 50);
		contentPanel.add(cmbRightHand);
		
		JPanel attackDefensePanel = JPanelFactory.createJPanel("Physical attack/defense");
		attackDefensePanel.setBounds(equipmentLeft, 455, 243, 100);
		attackDefensePanel.setLayout(null);
		contentPanel.add(attackDefensePanel);
		
		JLabel lblArmor = JLabelFactory.createJLabel("Armor");
		lblArmor.setToolTipText(ARMOR_TOOL_TIP);
		lblArmor.setBounds(10, 20, 150, 20);
		attackDefensePanel.add(lblArmor);
		
		lblArmorValue = JLabelFactory.createJLabel(playerCharacter.getProperty(Constants.ARMOR).toString());
		lblArmorValue.setToolTipText(ARMOR_TOOL_TIP);
		lblArmorValue.setBounds(160, 20, 30, 20);
		attackDefensePanel.add(lblArmorValue);
		
		JLabel lblDamageResistValue = JLabelFactory.createJLabel("Damage Resist");
		lblDamageResistValue.setToolTipText(DAMAGE_RESIST_TOOL_TIP);
		lblDamageResistValue.setBounds(10, 40, 150, 20);
		attackDefensePanel.add(lblDamageResistValue);
		
		lblDamageResist = JLabelFactory.createJLabel(playerCharacter.getProperty(Constants.DAMAGE_RESIST).toString() + "%");
		lblDamageResist.setToolTipText(DAMAGE_RESIST_TOOL_TIP);
		lblDamageResist.setBounds(160, 40, 30, 20);
		attackDefensePanel.add(lblDamageResist);
		
		JLabel lblWeaponDamage = JLabelFactory.createJLabel("Weapon Damage");
		lblWeaponDamage.setToolTipText(WEAPON_TOOL_TIP);
		lblWeaponDamage.setBounds(10, 60, 150, 20);
		attackDefensePanel.add(lblWeaponDamage);
		
		lblDamageValue = JLabelFactory.createJLabel(playerCharacter.getProperty(Constants.DAMAGE).toString());
		lblDamageValue.setToolTipText(WEAPON_TOOL_TIP);
		lblDamageValue.setBounds(160, 60, 30, 20);
		attackDefensePanel.add(lblDamageValue);
		
		JLabel lblCondition = JLabelFactory.createJLabel("Active effects:");
		lblCondition.setToolTipText(ACTIVE_EFFECT_TOOL_TIP);
		lblCondition.setBounds(labelLeft, 570, 150, 20);
		contentPanel.add(lblCondition);
		
		JScrollPane scrollPane = JScrollPaneFactory.createScrollPane();
		scrollPane.setBounds(equipmentLeft, 570, 243, 148);
		contentPanel.add(scrollPane);
		
		JList<String> conditionList = JListFactory.createJList(createConditionsDescriptions());
		conditionList.setCellRenderer(new ConditionListCellRenderer(imageInfoReader, createConditionImageIds(), createLongerConditionsDescriptions()));
		scrollPane.setViewportView(conditionList);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 730, 1095, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane);
		
		JButton okButton = JButtonFactory.createButton("OK", imageInfoReader, soundIdReader);
		okButton.setActionCommand("OK");
		okButton.addActionListener(new CloseDialogAction());
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		SwingUtils.installEscapeCloseOperation(this);
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}
	
	private String getBountyDescription(World world) {
		int bounty = BountyPropertyUtils.getBounty(playerCharacter, world);
		if (bounty == 0) {
			return "no bounty";
		} else {
			return bounty + " gold bounty";
		}
	}

	private SpecialAttribute[] getSpecialAttributes() {
		List<SpecialAttribute> specialAttributesList = new ArrayList<>();
		Integer pregnancyCounter = playerCharacter.getProperty(Constants.PREGNANCY);
		if (pregnancyCounter != null) {
			specialAttributesList.add(new SpecialAttribute("Pregnancy", pregnancyCounter, CommonerOnTurn.PREGNANCY_DURATION, "Indicates how far along a pregnancy is"));
		}
		
		Integer vampireBloodLevel = playerCharacter.getProperty(Constants.VAMPIRE_BLOOD_LEVEL);
		if (vampireBloodLevel != null) {
			specialAttributesList.add(new SpecialAttribute("Blood level", vampireBloodLevel, 1000, "Indicates the blood level for a vampire"));
		}
		
		Integer ghoulMeatLevel = playerCharacter.getProperty(Constants.GHOUL_MEAT_LEVEL);
		if (ghoulMeatLevel != null) {
			specialAttributesList.add(new SpecialAttribute("Meat level", ghoulMeatLevel, 1000, "Indicates the human meat level for a ghoul"));
		}
		
		return specialAttributesList.toArray(new SpecialAttribute[0]);
	}

	private JPanel createPanel(String title, int x, int y, int width, int height) {
		JPanel panel = JPanelFactory.createJPanel(title);
		panel.setLayout(null);
		panel.setBounds(x, y, width, height);
		contentPanel.add(panel);
		
		return panel;
	}
	
	private String[] createConditionsDescriptions() {
		Conditions conditions = playerCharacter.getProperty(Constants.CONDITIONS);
		return conditions.getDescriptions().toArray(new String[0]);
	}
	
	private List<ImageIds> createConditionImageIds() {
		Conditions conditions = playerCharacter.getProperty(Constants.CONDITIONS);
		return conditions.getImageIds();
	}
	
	private List<String> createLongerConditionsDescriptions() {
		Conditions conditions = playerCharacter.getProperty(Constants.CONDITIONS);
		return conditions.getLongerDescriptions();
	}

	private JLabel createAttributeLabel(IntProperty attributeProperty, String description) {
		String tooltip = GuiAttributeDescription.createToolTipDescription(attributeProperty, description);
		JLabel label = JLabelFactory.createJLabel(description);
		label.setToolTipText(tooltip);
		return label;
	}
	
	private void createSkillBlock(SkillProperty skillProperty, JPanel parentPanel, int x, int y) {
		
		String skillDescription = skillProperty.getName();
		skillDescription = Character.toUpperCase(skillDescription.charAt(0)) + skillDescription.substring(1);
		JLabel lblSkill = JLabelFactory.createJLabel(skillDescription);
		lblSkill.setBounds(x, y, 100, 20);
		lblSkill.setToolTipText(skillProperty.getLongDescription());
		parentPanel.add(lblSkill);
		
		JLabel lblSkillValue = JLabelFactory.createJLabel(playerCharacter.getProperty(skillProperty).toString());
		lblSkillValue.setBounds(x + 105, y, 15, 20);
		lblSkillValue.setToolTipText(skillProperty.getLongDescription());
		parentPanel.add(lblSkillValue);
		
		JProgressBar skillProgressBar = JProgressBarFactory.createJProgressBar(0, 100);
		skillProgressBar.setBounds(x + 120, y, 140, 20);
		skillProgressBar.setToolTipText(skillProperty.getLongDescription());
		skillProgressBar.setValue(playerCharacter.getProperty(skillProperty).getPercentageUntilNextLevelUp());
		parentPanel.add(skillProgressBar);
	}
	
	private JComboBox<ComboBoxEquipmentItem> createEquipmentComboBox(WorldObjectContainer inventory, UnCheckedProperty<WorldObject> propertyKey, ImageInfoReader imageInfoReader) {
		List<WorldObject> worldObjects = getEquipmentItems(inventory, propertyKey);
		List<ComboBoxEquipmentItem> equipmentWorldObjects = new ArrayList<>();
		ComboBoxEquipmentItem noSelectedComboBoxEquipmentItem = new ComboBoxEquipmentItem(null, "");
		equipmentWorldObjects.add(noSelectedComboBoxEquipmentItem);
		ComboBoxEquipmentItem selectedItem = noSelectedComboBoxEquipmentItem;
		for(WorldObject worldObject : worldObjects) {
			ComboBoxEquipmentItem comboBoxEquipmentItem = new ComboBoxEquipmentItem(worldObject, worldObject.getProperty(Constants.NAME));
			if (worldObject == playerCharacter.getProperty(propertyKey)) {
				selectedItem = comboBoxEquipmentItem;
			}
			equipmentWorldObjects.add(comboBoxEquipmentItem);
		}
		
		JComboBox<ComboBoxEquipmentItem> equipmentComboBox = JComboBoxFactory.createJComboBox(equipmentWorldObjects.toArray(new ComboBoxEquipmentItem[0]), imageInfoReader);
		equipmentComboBox.setRenderer(new ComboBoxRenderer(imageInfoReader));
		equipmentComboBox.setSelectedItem(selectedItem);
		equipmentComboBox.addActionListener(new EquipmentChangedAction());
		return equipmentComboBox;
	}

	private List<WorldObject> getEquipmentItems(WorldObjectContainer inventory, UnCheckedProperty<WorldObject> propertyKey) {
		List<WorldObject> worldObjects = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, propertyKey);
		
		if (propertyKey == Constants.RIGHT_HAND_EQUIPMENT) {
			worldObjects = inventory.getWorldObjectsByFunction(Constants.EQUIPMENT_SLOT, w -> MeleeDamagePropertyUtils.isTwoHandedWeapon(w));
		}
		
		return worldObjects;
	}
	
	private class EquipmentChangedAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			playerCharacter.setProperty(Constants.HEAD_EQUIPMENT, cmbHead.getItemAt(cmbHead.getSelectedIndex()).getEquipment());
			playerCharacter.setProperty(Constants.TORSO_EQUIPMENT, cmbTorso.getItemAt(cmbTorso.getSelectedIndex()).getEquipment());
			playerCharacter.setProperty(Constants.ARMS_EQUIPMENT, cmbArms.getItemAt(cmbArms.getSelectedIndex()).getEquipment());
			playerCharacter.setProperty(Constants.LEGS_EQUIPMENT, cmbLegs.getItemAt(cmbLegs.getSelectedIndex()).getEquipment());
			playerCharacter.setProperty(Constants.FEET_EQUIPMENT, cmbFeet.getItemAt(cmbFeet.getSelectedIndex()).getEquipment());
			
			playerCharacter.setProperty(Constants.LEFT_HAND_EQUIPMENT, cmbLeftHand.getItemAt(cmbLeftHand.getSelectedIndex()).getEquipment());
			playerCharacter.setProperty(Constants.RIGHT_HAND_EQUIPMENT, cmbRightHand.getItemAt(cmbRightHand.getSelectedIndex()).getEquipment());
			
			int armor = ArmorPropertyUtils.calculateArmor(playerCharacter);
			
			playerCharacter.setProperty(Constants.ARMOR, armor);
			lblArmorValue.setText(playerCharacter.getProperty(Constants.ARMOR).toString());

			int damageResist = ArmorPropertyUtils.calculateDamageResist(playerCharacter);
			playerCharacter.setProperty(Constants.DAMAGE_RESIST, damageResist);
			lblDamageResist.setText(playerCharacter.getProperty(Constants.DAMAGE_RESIST).toString() + "%");
			
			UnCheckedProperty<WorldObject> lastModifiedHandEquipmentProperty = getLastModifiedHandEquipmentProperty(e);
			
			MeleeDamagePropertyUtils.setTwoHandedWeapons(playerCharacter, lastModifiedHandEquipmentProperty);
			cmbLeftHand.setSelectedItem(findItemInHand(cmbLeftHand, playerCharacter.getProperty(Constants.LEFT_HAND_EQUIPMENT)));
			cmbRightHand.setSelectedItem(findItemInHand(cmbRightHand, playerCharacter.getProperty(Constants.RIGHT_HAND_EQUIPMENT)));
			
			int meleeDamage = MeleeDamagePropertyUtils.calculateMeleeDamage(playerCharacter);
			playerCharacter.setProperty(Constants.DAMAGE, meleeDamage);
			lblDamageValue.setText(playerCharacter.getProperty(Constants.DAMAGE).toString());
		}

		private UnCheckedProperty<WorldObject> getLastModifiedHandEquipmentProperty(ActionEvent e) {
			UnCheckedProperty<WorldObject> lastModifiedHandEquipmentProperty;
			JComboBox<ComboBoxEquipmentItem> source = (JComboBox<ComboBoxEquipmentItem>) e.getSource();
			if (source == cmbLeftHand) {
				lastModifiedHandEquipmentProperty = Constants.LEFT_HAND_EQUIPMENT;
			} else if (source == cmbRightHand) {
				lastModifiedHandEquipmentProperty = Constants.RIGHT_HAND_EQUIPMENT;
			} else {
				return null;
			}
			return lastModifiedHandEquipmentProperty;
		}

		private Object findItemInHand(JComboBox<ComboBoxEquipmentItem> comboBoxEquipment, WorldObject handEquipment) {
			for(int i=0; i<comboBoxEquipment.getModel().getSize(); i++) {
				ComboBoxEquipmentItem comboBoxEquipmentItem = comboBoxEquipment.getModel().getElementAt(i);
				if (comboBoxEquipmentItem.getEquipment() == handEquipment) {
					return comboBoxEquipmentItem;
				}
			}
			throw new IllegalStateException("Item " + handEquipment + " not found in cmbRightHand");
		}
	}
	
	private static class ComboBoxEquipmentItem {
		private final WorldObject equipment;
		private final String description;
		
		public ComboBoxEquipmentItem(WorldObject equipment, String description) {
			super();
			this.equipment = equipment;
			this.description = description;
		}

		public WorldObject getEquipment() {
			return equipment;
		}

		public String getDescription() {
			return description;
		}
		
		@Override
		public String toString() {
			return getDescription();
		}
	}
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			CharacterDialog.this.dispose();
		}
	}
	
	public void showMe() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	static class ComboBoxRenderer extends JLabel implements ListCellRenderer<ComboBoxEquipmentItem> {

		private final ImageInfoReader imageInfoReader;
		
		public ComboBoxRenderer(ImageInfoReader imageInfoReader) {
			setOpaque(false);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
			
			this.imageInfoReader = imageInfoReader;
		}

		public Component getListCellRendererComponent(JList list, ComboBoxEquipmentItem value, int index, boolean isSelected, boolean cellHasFocus) {
			WorldObject equipment = value.getEquipment();

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			final ImageIcon icon;
			final String description;
			if (equipment != null) {
				icon = new ImageIcon(imageInfoReader.getImage(equipment.getProperty(Constants.IMAGE_ID), null));
				description = equipment.getProperty(Constants.NAME);
			} else {
				icon = null;
				description = " ";
				setPreferredSize(new Dimension(getWidth(), 48));
			}
			setIcon(icon);
			setText(description);

			return this;
		}
	}
	
	public class ConditionListCellRenderer implements ListCellRenderer<String> {
		private final ImageInfoReader imageInfoReader;
		private final List<ImageIds> imageIds;
		private final List<String> longerDescriptions;
		private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

		public ConditionListCellRenderer(ImageInfoReader imageInfoReader, List<ImageIds> imageIds, List<String> longerDescriptions) {
			super();
			this.imageInfoReader = imageInfoReader;
			this.imageIds = imageIds;
			this.longerDescriptions = longerDescriptions;
			defaultRenderer.setOpaque(false);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String item, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer
					.getListCellRendererComponent(list, item, index, isSelected, cellHasFocus);

			renderer.setText(item);
			renderer.setToolTipText(longerDescriptions.get(index));
			renderer.setIcon(new ImageIcon(imageInfoReader.getImage(imageIds.get(index), null)));
			renderer.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
			renderer.setForeground(ColorPalette.FOREGROUND_COLOR);
			
			return renderer;
		}
	}
}
