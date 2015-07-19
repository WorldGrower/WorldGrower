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
import org.worldgrower.WorldObjectContainer;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.UnCheckedProperty;
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
		
		setBounds(100, 100, 605, 689);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblStrength = new JLabel("Strength");
		lblStrength.setBounds(22, 13, 77, 16);
		contentPanel.add(lblStrength);
		
		JLabel lblConstitution = new JLabel("Constitution");
		lblConstitution.setBounds(22, 42, 77, 16);
		contentPanel.add(lblConstitution);
		
		JLabel lblDexterity = new JLabel("Dexterity");
		lblDexterity.setBounds(22, 71, 77, 16);
		contentPanel.add(lblDexterity);
		
		JLabel lblIntelligence = new JLabel("Intelligence");
		lblIntelligence.setBounds(22, 100, 77, 16);
		contentPanel.add(lblIntelligence);
		
		JLabel lblWisdom = new JLabel("Wisdom");
		lblWisdom.setBounds(22, 129, 77, 16);
		contentPanel.add(lblWisdom);
		
		JLabel lblCharisma = new JLabel("Charisma");
		lblCharisma.setBounds(22, 158, 77, 16);
		contentPanel.add(lblCharisma);
		
		JLabel lblStrengthValue = new JLabel(playerCharacter.getProperty(Constants.STRENGTH).toString());
		lblStrengthValue.setBounds(111, 13, 14, 16);
		contentPanel.add(lblStrengthValue);
		
		JLabel lblConstitutionValue = new JLabel(playerCharacter.getProperty(Constants.CONSTITUTION).toString());
		lblConstitutionValue.setBounds(111, 42, 14, 16);
		contentPanel.add(lblConstitutionValue);
		
		JLabel lblDexterityValue = new JLabel(playerCharacter.getProperty(Constants.DEXTERITY).toString());
		lblDexterityValue.setBounds(111, 71, 14, 16);
		contentPanel.add(lblDexterityValue);
		
		JLabel lblIntelligenceValue = new JLabel(playerCharacter.getProperty(Constants.INTELLIGENCE).toString());
		lblIntelligenceValue.setBounds(111, 100, 14, 16);
		contentPanel.add(lblIntelligenceValue);
		
		JLabel lblWisdomValue = new JLabel(playerCharacter.getProperty(Constants.WISDOM).toString());
		lblWisdomValue.setBounds(111, 129, 14, 16);
		contentPanel.add(lblWisdomValue);
		
		JLabel lblCharismaValue = new JLabel(playerCharacter.getProperty(Constants.CHARISMA).toString());
		lblCharismaValue.setBounds(111, 158, 14, 16);
		contentPanel.add(lblCharismaValue);

		createSkillBlock(Constants.BLUFF_SKILL, 208);
		createSkillBlock(Constants.INSIGHT_SKILL, 238);
		createSkillBlock(Constants.HAND_TO_HAND_SKILL, 268);
		createSkillBlock(Constants.ONE_HANDED_SKILL, 298);
		createSkillBlock(Constants.TWO_HANDED_SKILL, 328);
		createSkillBlock(Constants.PERCEPTION_SKILL, 358);
		createSkillBlock(Constants.DIPLOMACY_SKILL, 388);
		createSkillBlock(Constants.INTIMIDATE_SKILL, 418);
		createSkillBlock(Constants.SMITHING_SKILL, 448);
		createSkillBlock(Constants.ALCHEMY_SKILL, 478);
		createSkillBlock(Constants.ARCHERY_SKILL, 508);
		createSkillBlock(Constants.THIEVERY_SKILL, 538);
		createSkillBlock(Constants.EVOCATION_SKILL, 568);
		
		JLabel lblHead = new JLabel("Head");
		lblHead.setBounds(264, 13, 56, 16);
		contentPanel.add(lblHead);
		
		JLabel lblTorso = new JLabel("Torso");
		lblTorso.setBounds(264, 42, 56, 16);
		contentPanel.add(lblTorso);
		
		JLabel lblArms = new JLabel("Arms");
		lblArms.setBounds(264, 71, 56, 16);
		contentPanel.add(lblArms);
		
		JLabel lblLegs = new JLabel("Legs");
		lblLegs.setBounds(264, 100, 56, 16);
		contentPanel.add(lblLegs);
		
		JLabel lblFeet = new JLabel("Feet");
		lblFeet.setBounds(264, 129, 56, 16);
		contentPanel.add(lblFeet);
		
		JLabel lblLeftHand = new JLabel("Left Hand");
		lblLeftHand.setBounds(264, 192, 69, 16);
		contentPanel.add(lblLeftHand);
		
		JLabel lblRightHand = new JLabel("Right Hand");
		lblRightHand.setBounds(264, 221, 69, 16);
		contentPanel.add(lblRightHand);
		
		WorldObjectContainer inventory = playerCharacter.getProperty(Constants.INVENTORY);
		
		cmbHead = createEquipmentComboBox(inventory, Constants.HEAD_EQUIPMENT);
		cmbHead.setBounds(332, 13, 243, 22);
		contentPanel.add(cmbHead);
		
		cmbTorso = createEquipmentComboBox(inventory, Constants.TORSO_EQUIPMENT);
		cmbTorso.setBounds(332, 42, 243, 22);
		contentPanel.add(cmbTorso);
		
		cmbArms = createEquipmentComboBox(inventory, Constants.ARMS_EQUIPMENT);
		cmbArms.setBounds(332, 71, 243, 22);
		contentPanel.add(cmbArms);
		
		cmbLegs = createEquipmentComboBox(inventory, Constants.LEGS_EQUIPMENT);
		cmbLegs.setBounds(332, 100, 243, 22);
		contentPanel.add(cmbLegs);
		
		cmbFeet = createEquipmentComboBox(inventory, Constants.FEET_EQUIPMENT);
		cmbFeet.setBounds(332, 129, 243, 22);
		contentPanel.add(cmbFeet);
		
		cmbLeftHand = createEquipmentComboBox(inventory, Constants.LEFT_HAND_EQUIPMENT);
		cmbLeftHand.setBounds(332, 192, 243, 22);
		contentPanel.add(cmbLeftHand);
		
		cmbRightHand = createEquipmentComboBox(inventory, Constants.RIGHT_HAND_EQUIPMENT);
		cmbRightHand.setBounds(332, 221, 243, 22);
		contentPanel.add(cmbRightHand);
		
		JLabel lblArmor = new JLabel("Armor");
		lblArmor.setBounds(264, 331, 77, 16);
		contentPanel.add(lblArmor);
		
		lblArmorValue = new JLabel(playerCharacter.getProperty(Constants.ARMOR).toString());
		lblArmorValue.setBounds(376, 331, 14, 16);
		contentPanel.add(lblArmorValue);
		
		JLabel lblWeaponDamage = new JLabel("Weapon Damage");
		lblWeaponDamage.setBounds(264, 293, 111, 16);
		contentPanel.add(lblWeaponDamage);
		
		lblDamageValue = new JLabel(playerCharacter.getProperty(Constants.DAMAGE).toString());
		lblDamageValue.setBounds(376, 293, 14, 16);
		contentPanel.add(lblDamageValue);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new CloseDialogAction());
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	}
	
	private void createSkillBlock(SkillProperty skillProperty, int y) {
		
		JLabel lblInsight = new JLabel(skillProperty.getName());
		lblInsight.setBounds(22, y, 77, 16);
		contentPanel.add(lblInsight);
		
		JLabel lblBluffValue = new JLabel(playerCharacter.getProperty(skillProperty).toString());
		lblBluffValue.setBounds(111, y, 14, 16);
		contentPanel.add(lblBluffValue);
		
		JProgressBar bluffProgressBar = new JProgressBar(0, 100);
		bluffProgressBar.setBounds(127, y, 100, 20);
		bluffProgressBar.setValue(playerCharacter.getProperty(skillProperty).getPercentageUntilNextLevelUp());
		contentPanel.add(bluffProgressBar);
	}
	
	private JComboBox<ComboBoxEquipmentItem> createEquipmentComboBox(WorldObjectContainer inventory, UnCheckedProperty<WorldObject> propertyKey) {
		List<WorldObject> worldObjects = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, propertyKey);
		List<ComboBoxEquipmentItem> equipmentWorldObjects = new ArrayList<>();
		equipmentWorldObjects.add(new ComboBoxEquipmentItem(null, ""));
		for(WorldObject worldObject : worldObjects) {
			equipmentWorldObjects.add(new ComboBoxEquipmentItem(worldObject, worldObject.getProperty(Constants.NAME)));
		}
		
		JComboBox<ComboBoxEquipmentItem> equipmentComboBox = new JComboBox<ComboBoxEquipmentItem>(equipmentWorldObjects.toArray(new ComboBoxEquipmentItem[0]));
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
