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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.ArmorPropertyUtils;
import org.worldgrower.goal.MeleeDamagePropertyUtils;
import org.worldgrower.gui.util.IconUtils;

public class CharacterDialog extends JDialog {

	private static final String HEAD_TOOL_TIP = "Head equipment slot";
	private static final String TORSO_TOOL_TIP = "Torso equipment slot";
	private static final String ARMS_TOOL_TIP = "Arms equipment slot";
	private static final String LEGS_TOOL_TIP = "Legs equipment slot";
	private static final String FEET_TOOL_TIP = "Feet equipment slot";
	private static final String LEFT_HAND_TOOL_TIP = "Left hand equipment slot";
	private static final String RIGHT_HAND_TOOL_TIP = "Right hand equipment slot";
	
	private static final String ARMOR_TOOL_TIP = "Armor reduces the damage taken from non-magical attacks";
	private static final String WEAPON_TOOL_TIP = "Damage indicates the damage done when performing melee or ranged attacks";
	
	private final JPanel contentPanel = new GradientPanel();
	private final WorldObject playerCharacter;
	
	private JComboBox<ComboBoxEquipmentItem> cmbHead;
	private JComboBox<ComboBoxEquipmentItem> cmbTorso;
	private JComboBox<ComboBoxEquipmentItem> cmbArms;
	private JComboBox<ComboBoxEquipmentItem> cmbLegs;
	private JComboBox<ComboBoxEquipmentItem> cmbFeet;
	private JLabel lblArmorValue;
	private JComboBox<ComboBoxEquipmentItem> cmbLeftHand;
	private JComboBox<ComboBoxEquipmentItem> cmbRightHand;
	private JLabel lblDamageValue;

	public CharacterDialog(WorldObject playerCharacter, ImageInfoReader imageInfoReader) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		IconUtils.setIcon(this);
		setResizable(false);
		
		this.playerCharacter = playerCharacter;
		
		setSize(900, 700);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblStrength = createAttributeLabel(Constants.STRENGTH, "Strength");
		lblStrength.setBounds(12, 13, 120, 16);
		contentPanel.add(lblStrength);
		
		JLabel lblConstitution = createAttributeLabel(Constants.CONSTITUTION, "Constitution");
		lblConstitution.setBounds(12, 42, 120, 16);
		contentPanel.add(lblConstitution);
		
		JLabel lblDexterity = createAttributeLabel(Constants.DEXTERITY, "Dexterity");
		lblDexterity.setBounds(12, 71, 120, 16);
		contentPanel.add(lblDexterity);
		
		JLabel lblIntelligence = createAttributeLabel(Constants.INTELLIGENCE, "Intelligence");
		lblIntelligence.setBounds(12, 100, 120, 16);
		contentPanel.add(lblIntelligence);
		
		JLabel lblWisdom = createAttributeLabel(Constants.WISDOM, "Wisdom");
		lblWisdom.setBounds(12, 129, 120, 16);
		contentPanel.add(lblWisdom);
		
		JLabel lblCharisma = createAttributeLabel(Constants.CHARISMA, "Charisma");
		lblCharisma.setBounds(12, 158, 120, 16);
		contentPanel.add(lblCharisma);
		
		JLabel lblStrengthValue = new JLabel(playerCharacter.getProperty(Constants.STRENGTH).toString());
		lblStrengthValue.setBounds(150, 13, 20, 16);
		lblStrengthValue.setToolTipText(lblStrength.getToolTipText());
		contentPanel.add(lblStrengthValue);
		
		JLabel lblConstitutionValue = new JLabel(playerCharacter.getProperty(Constants.CONSTITUTION).toString());
		lblConstitutionValue.setBounds(150, 42, 20, 16);
		lblConstitutionValue.setToolTipText(lblConstitution.getToolTipText());
		contentPanel.add(lblConstitutionValue);
		
		JLabel lblDexterityValue = new JLabel(playerCharacter.getProperty(Constants.DEXTERITY).toString());
		lblDexterityValue.setBounds(150, 71, 20, 16);
		lblDexterityValue.setToolTipText(lblDexterity.getToolTipText());
		contentPanel.add(lblDexterityValue);
		
		JLabel lblIntelligenceValue = new JLabel(playerCharacter.getProperty(Constants.INTELLIGENCE).toString());
		lblIntelligenceValue.setBounds(150, 100, 20, 16);
		lblIntelligenceValue.setToolTipText(lblIntelligence.getToolTipText());
		contentPanel.add(lblIntelligenceValue);
		
		JLabel lblWisdomValue = new JLabel(playerCharacter.getProperty(Constants.WISDOM).toString());
		lblWisdomValue.setBounds(150, 129, 20, 16);
		lblWisdomValue.setToolTipText(lblWisdom.getToolTipText());
		contentPanel.add(lblWisdomValue);
		
		JLabel lblCharismaValue = new JLabel(playerCharacter.getProperty(Constants.CHARISMA).toString());
		lblCharismaValue.setBounds(150, 158, 20, 16);
		lblCharismaValue.setToolTipText(lblCharisma.getToolTipText());
		contentPanel.add(lblCharismaValue);

		createSkillBlock(Constants.BLUFF_SKILL, 12, 208);
		createSkillBlock(Constants.INSIGHT_SKILL, 12, 238);
		createSkillBlock(Constants.HAND_TO_HAND_SKILL, 12, 268);
		createSkillBlock(Constants.ONE_HANDED_SKILL, 12, 298);
		createSkillBlock(Constants.TWO_HANDED_SKILL, 12, 328);
		createSkillBlock(Constants.PERCEPTION_SKILL, 12, 358);
		createSkillBlock(Constants.DIPLOMACY_SKILL, 12, 388);
		createSkillBlock(Constants.INTIMIDATE_SKILL, 12, 418);
		createSkillBlock(Constants.SMITHING_SKILL, 12, 448);
		createSkillBlock(Constants.ALCHEMY_SKILL, 12, 478);
		createSkillBlock(Constants.ARCHERY_SKILL, 12, 508);
		createSkillBlock(Constants.NECROMANCY_SKILL, 12, 538);
		createSkillBlock(Constants.FISHING_SKILL, 12, 568);
		
		createSkillBlock(Constants.THIEVERY_SKILL, 250, 208);
		createSkillBlock(Constants.EVOCATION_SKILL, 250, 238);
		createSkillBlock(Constants.ILLUSION_SKILL, 250, 268);
		createSkillBlock(Constants.FARMING_SKILL, 250, 298);
		createSkillBlock(Constants.MINING_SKILL, 250, 328);
		createSkillBlock(Constants.LUMBERING_SKILL, 250, 358);
		createSkillBlock(Constants.RESTORATION_SKILL, 250, 388);
		createSkillBlock(Constants.WEAVING_SKILL, 250, 418);
		createSkillBlock(Constants.LIGHT_ARMOR_SKILL, 250, 448);
		createSkillBlock(Constants.HEAVY_ARMOR_SKILL, 250, 478);
		createSkillBlock(Constants.CARPENTRY_SKILL,250,  508);
		createSkillBlock(Constants.TRANSMUTATION_SKILL, 250, 538);
		createSkillBlock(Constants.ENCHANTMENT_SKILL, 250, 568);
		
		int labelLeft = 540;
		
		JLabel lblHead = new JLabel("Head");
		lblHead.setToolTipText(HEAD_TOOL_TIP);
		lblHead.setBounds(labelLeft, 35, 80, 16);
		contentPanel.add(lblHead);
		
		JLabel lblTorso = new JLabel("Torso");
		lblTorso.setToolTipText(TORSO_TOOL_TIP);
		lblTorso.setBounds(labelLeft, 95, 80, 16);
		contentPanel.add(lblTorso);
		
		JLabel lblArms = new JLabel("Arms");
		lblArms.setToolTipText(ARMS_TOOL_TIP);
		lblArms.setBounds(labelLeft, 155, 80, 16);
		contentPanel.add(lblArms);
		
		JLabel lblLegs = new JLabel("Legs");
		lblLegs.setToolTipText(LEGS_TOOL_TIP);
		lblLegs.setBounds(labelLeft, 215, 80, 16);
		contentPanel.add(lblLegs);
		
		JLabel lblFeet = new JLabel("Feet");
		lblFeet.setToolTipText(FEET_TOOL_TIP);
		lblFeet.setBounds(labelLeft, 275, 80, 16);
		contentPanel.add(lblFeet);
		
		JLabel lblLeftHand = new JLabel("Left Hand");
		lblLeftHand.setToolTipText(LEFT_HAND_TOOL_TIP);
		lblLeftHand.setBounds(labelLeft, 335, 80, 16);
		contentPanel.add(lblLeftHand);
		
		JLabel lblRightHand = new JLabel("Right Hand");
		lblRightHand.setToolTipText(RIGHT_HAND_TOOL_TIP);
		lblRightHand.setBounds(labelLeft, 395, 80, 16);
		contentPanel.add(lblRightHand);
		
		WorldObjectContainer inventory = playerCharacter.getProperty(Constants.INVENTORY);
		
		int equipmentLeft = 630;
		
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
		
		JLabel lblArmor = new JLabel("Armor");
		lblArmor.setToolTipText(ARMOR_TOOL_TIP);
		lblArmor.setBounds(equipmentLeft, 475, 150, 16);
		contentPanel.add(lblArmor);
		
		lblArmorValue = new JLabel(playerCharacter.getProperty(Constants.ARMOR).toString());
		lblArmorValue.setToolTipText(ARMOR_TOOL_TIP);
		lblArmorValue.setBounds(760, 475, 30, 16);
		contentPanel.add(lblArmorValue);
		
		JLabel lblWeaponDamage = new JLabel("Weapon Damage");
		lblWeaponDamage.setToolTipText(WEAPON_TOOL_TIP);
		lblWeaponDamage.setBounds(equipmentLeft, 495, 150, 16);
		contentPanel.add(lblWeaponDamage);
		
		lblDamageValue = new JLabel(playerCharacter.getProperty(Constants.DAMAGE).toString());
		lblDamageValue.setToolTipText(WEAPON_TOOL_TIP);
		lblDamageValue.setBounds(760, 495, 30, 16);
		contentPanel.add(lblDamageValue);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 620, 880, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane);
		
		JButton okButton = ButtonFactory.createButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new CloseDialogAction());
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		SwingUtils.installEscapeCloseOperation(this);
	}
	
	private JLabel createAttributeLabel(IntProperty attributeProperty, String description) {
		String tooltip = GuiAttributeDescription.createToolTipDescription(attributeProperty, description);
		JLabel label = new JLabel(description);
		label.setToolTipText(tooltip);
		return label;
	}
	
	private void createSkillBlock(SkillProperty skillProperty, int x, int y) {
		
		JLabel lblSkill = new JLabel(skillProperty.getName());
		lblSkill.setBounds(x, y, 100, 16);
		contentPanel.add(lblSkill);
		
		JLabel lblSkillValue = new JLabel(playerCharacter.getProperty(skillProperty).toString());
		lblSkillValue.setBounds(x + 105, y, 15, 16);
		contentPanel.add(lblSkillValue);
		
		JProgressBar skillProgressBar = new JProgressBar(0, 100);
		skillProgressBar.setBounds(x + 120, y, 100, 20);
		skillProgressBar.setValue(playerCharacter.getProperty(skillProperty).getPercentageUntilNextLevelUp());
		contentPanel.add(skillProgressBar);
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
		
		JComboBox<ComboBoxEquipmentItem> equipmentComboBox = new JComboBox<ComboBoxEquipmentItem>(equipmentWorldObjects.toArray(new ComboBoxEquipmentItem[0]));
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
			setOpaque(true);
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
}
