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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.ArmorPropertyUtils;
import org.worldgrower.goal.MeleeDamagePropertyUtils;

public class CharacterDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
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

	public CharacterDialog(WorldObject playerCharacter) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		this.playerCharacter = playerCharacter;
		
		setSize(900, 700);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblStrength = new JLabel("Strength");
		lblStrength.setBounds(12, 13, 120, 16);
		contentPanel.add(lblStrength);
		
		JLabel lblConstitution = new JLabel("Constitution");
		lblConstitution.setBounds(12, 42, 120, 16);
		contentPanel.add(lblConstitution);
		
		JLabel lblDexterity = new JLabel("Dexterity");
		lblDexterity.setBounds(12, 71, 120, 16);
		contentPanel.add(lblDexterity);
		
		JLabel lblIntelligence = new JLabel("Intelligence");
		lblIntelligence.setBounds(12, 100, 120, 16);
		contentPanel.add(lblIntelligence);
		
		JLabel lblWisdom = new JLabel("Wisdom");
		lblWisdom.setBounds(12, 129, 120, 16);
		contentPanel.add(lblWisdom);
		
		JLabel lblCharisma = new JLabel("Charisma");
		lblCharisma.setBounds(12, 158, 120, 16);
		contentPanel.add(lblCharisma);
		
		JLabel lblStrengthValue = new JLabel(playerCharacter.getProperty(Constants.STRENGTH).toString());
		lblStrengthValue.setBounds(150, 13, 20, 16);
		contentPanel.add(lblStrengthValue);
		
		JLabel lblConstitutionValue = new JLabel(playerCharacter.getProperty(Constants.CONSTITUTION).toString());
		lblConstitutionValue.setBounds(150, 42, 20, 16);
		contentPanel.add(lblConstitutionValue);
		
		JLabel lblDexterityValue = new JLabel(playerCharacter.getProperty(Constants.DEXTERITY).toString());
		lblDexterityValue.setBounds(150, 71, 20, 16);
		contentPanel.add(lblDexterityValue);
		
		JLabel lblIntelligenceValue = new JLabel(playerCharacter.getProperty(Constants.INTELLIGENCE).toString());
		lblIntelligenceValue.setBounds(150, 100, 20, 16);
		contentPanel.add(lblIntelligenceValue);
		
		JLabel lblWisdomValue = new JLabel(playerCharacter.getProperty(Constants.WISDOM).toString());
		lblWisdomValue.setBounds(150, 129, 20, 16);
		contentPanel.add(lblWisdomValue);
		
		JLabel lblCharismaValue = new JLabel(playerCharacter.getProperty(Constants.CHARISMA).toString());
		lblCharismaValue.setBounds(150, 158, 20, 16);
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
		
		JLabel lblHead = new JLabel("Head");
		lblHead.setBounds(500, 13, 80, 16);
		contentPanel.add(lblHead);
		
		JLabel lblTorso = new JLabel("Torso");
		lblTorso.setBounds(500, 42, 80, 16);
		contentPanel.add(lblTorso);
		
		JLabel lblArms = new JLabel("Arms");
		lblArms.setBounds(500, 71, 80, 16);
		contentPanel.add(lblArms);
		
		JLabel lblLegs = new JLabel("Legs");
		lblLegs.setBounds(500, 100, 80, 16);
		contentPanel.add(lblLegs);
		
		JLabel lblFeet = new JLabel("Feet");
		lblFeet.setBounds(500, 129, 80, 16);
		contentPanel.add(lblFeet);
		
		JLabel lblLeftHand = new JLabel("Left Hand");
		lblLeftHand.setBounds(500, 192, 80, 16);
		contentPanel.add(lblLeftHand);
		
		JLabel lblRightHand = new JLabel("Right Hand");
		lblRightHand.setBounds(500, 221, 80, 16);
		contentPanel.add(lblRightHand);
		
		WorldObjectContainer inventory = playerCharacter.getProperty(Constants.INVENTORY);
		
		cmbHead = createEquipmentComboBox(inventory, Constants.HEAD_EQUIPMENT);
		cmbHead.setBounds(590, 13, 243, 22);
		contentPanel.add(cmbHead);
		
		cmbTorso = createEquipmentComboBox(inventory, Constants.TORSO_EQUIPMENT);
		cmbTorso.setBounds(590, 42, 243, 22);
		contentPanel.add(cmbTorso);
		
		cmbArms = createEquipmentComboBox(inventory, Constants.ARMS_EQUIPMENT);
		cmbArms.setBounds(590, 71, 243, 22);
		contentPanel.add(cmbArms);
		
		cmbLegs = createEquipmentComboBox(inventory, Constants.LEGS_EQUIPMENT);
		cmbLegs.setBounds(590, 100, 243, 22);
		contentPanel.add(cmbLegs);
		
		cmbFeet = createEquipmentComboBox(inventory, Constants.FEET_EQUIPMENT);
		cmbFeet.setBounds(590, 129, 243, 22);
		contentPanel.add(cmbFeet);
		
		cmbLeftHand = createEquipmentComboBox(inventory, Constants.LEFT_HAND_EQUIPMENT);
		cmbLeftHand.setBounds(590, 192, 243, 22);
		contentPanel.add(cmbLeftHand);
		
		cmbRightHand = createEquipmentComboBox(inventory, Constants.RIGHT_HAND_EQUIPMENT);
		cmbRightHand.setBounds(590, 221, 243, 22);
		contentPanel.add(cmbRightHand);
		
		JLabel lblArmor = new JLabel("Armor");
		lblArmor.setBounds(590, 331, 150, 16);
		contentPanel.add(lblArmor);
		
		lblArmorValue = new JLabel(playerCharacter.getProperty(Constants.ARMOR).toString());
		lblArmorValue.setBounds(740, 331, 14, 16);
		contentPanel.add(lblArmorValue);
		
		JLabel lblWeaponDamage = new JLabel("Weapon Damage");
		lblWeaponDamage.setBounds(590, 293, 150, 16);
		contentPanel.add(lblWeaponDamage);
		
		lblDamageValue = new JLabel(playerCharacter.getProperty(Constants.DAMAGE).toString());
		lblDamageValue.setBounds(740, 293, 14, 16);
		contentPanel.add(lblDamageValue);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new CloseDialogAction());
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		SwingUtils.installEscapeCloseOperation(this);
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
	
	private JComboBox<ComboBoxEquipmentItem> createEquipmentComboBox(WorldObjectContainer inventory, UnCheckedProperty<WorldObject> propertyKey) {
		List<WorldObject> worldObjects = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, propertyKey);
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
		equipmentComboBox.setSelectedItem(selectedItem);
		equipmentComboBox.addActionListener(new EquipmentChangedAction());
		return equipmentComboBox;
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
			
			int meleeDamage = MeleeDamagePropertyUtils.calculateMeleeDamage(playerCharacter);
			playerCharacter.setProperty(Constants.DAMAGE, meleeDamage);
			lblDamageValue.setText(playerCharacter.getProperty(Constants.DAMAGE).toString());
		}
	}
	
	private ComboBoxEquipmentItem createEquipmentItem(UnCheckedProperty<WorldObject> equipmentProperty) {
		WorldObject equipment = playerCharacter.getProperty(equipmentProperty);
		if (equipment != null) {
			return new ComboBoxEquipmentItem(equipment, equipment.getProperty(Constants.NAME));
		} else {
			return new ComboBoxEquipmentItem(null, "");
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
}
