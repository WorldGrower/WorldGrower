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
package org.worldgrower;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Conditions;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.curse.CurseListener;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.CommonerOnTurn;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.ItemGenerator;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.generator.TerrainGenerator;
import org.worldgrower.generator.WorldGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.CommonerImageIds;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.start.CharacterAttributes;
import org.worldgrower.profession.PlayerCharacterProfession;
import org.worldgrower.terrain.TerrainType;

public class Main {

	public static void run(String playerName, String playerProfession, int worldWidth, int worldHeight, int enemyDensity, int villagerCount, int seed, CharacterAttributes characterAttributes) throws Exception {
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = new WorldImpl(worldWidth, worldHeight, dungeonMaster);
		int playerCharacterId = world.generateUniqueId();
		
		final CommonerImageIds commonerImageIds = new CommonerImageIds();
		final CommonerNameGenerator commonerNameGenerator = new CommonerNameGenerator();
		final WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		
		final WorldObject playerCharacter = createPlayerCharacter(playerCharacterId, playerName, playerProfession, world, commonerImageIds, commonerNameGenerator, organization, characterAttributes);
		world.addWorldObject(playerCharacter);
		
		addDefaultWorldObjects(world, commonerImageIds, commonerNameGenerator, organization, villagerCount, seed);
		
		world.addListener(new CurseListener(world));
		exploreWorld(playerCharacter, world);
		
		addEnemies(enemyDensity, world, seed);
		
		createAndShowGUI(dungeonMaster, world, playerCharacter);
	}

	private static void addEnemies(int enemyDensity, World world, int seed) {
		if (enemyDensity > 0) {
			WorldGenerator worldGenerator = new WorldGenerator(seed);
			WorldObject verminOrganization = GroupPropertyUtils.create(null, "vermin", null, world);
			CreatureGenerator creatureGenerator = new CreatureGenerator(verminOrganization);
			PlantGenerator plantGenerator = new PlantGenerator(verminOrganization);
			worldGenerator.addWorldObjects(world, 1, 1, 5, TerrainType.GRASLAND, creatureGenerator::generateRat);
			worldGenerator.addWorldObjects(world, 1, 1, 5, TerrainType.GRASLAND, creatureGenerator::generateSlime);
			worldGenerator.addWorldObjects(world, 2, 2, 4, TerrainType.PLAINS, plantGenerator::generateDemonTree);
		}
	}
	
	public static void load(File fileToLoad) {
		DungeonMaster dungeonMaster = new DungeonMaster();//TODO: turn state will be lost
		World world = WorldImpl.load(fileToLoad);
		final WorldObject playerCharacter = world.findWorldObject(Constants.ID, 0);
		
		world.addListener(new CurseListener(world));
		
		createAndShowGUI(dungeonMaster, world, playerCharacter);
	}

	private static void createAndShowGUI(DungeonMaster dungeonMaster, World world, final WorldObject playerCharacter) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
            public void run() {
                try {
					createAndShowGUI(playerCharacter, world, dungeonMaster);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
            }
        });
	}

	private static void addDefaultWorldObjects(World world,
			final CommonerImageIds commonerImageIds,
			final CommonerNameGenerator commonerNameGenerator, WorldObject organization, int villagerCount, int seed) {
	
		PlantGenerator.generateBerryBush(3, 3, world);
		
		for(int i=0; i<villagerCount; i++) {
			CommonerGenerator.generateCommoner(1, 1, world, commonerImageIds, commonerNameGenerator, organization);
		}
		
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, 3);
		properties.put(Constants.Y, 5);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.WOOD_SOURCE, 4);
		properties.put(Constants.IMAGE_ID, ImageIds.TRUNK);
		properties.put(Constants.NAME, "tree trunk");
		WorldObject treeStump = new WorldObjectImpl(properties);
		world.addWorldObject(treeStump);
		
		PlantGenerator.generateTree(3, 8, world);

		WorldGenerator worldGenerator = new WorldGenerator(seed);
		worldGenerator.addWorldObjects(world, 2, 2, PlantGenerator::generateTree);
		worldGenerator.addWorldObjects(world, 2, 2, world.getWidth() / 10, TerrainType.HILL, TerrainGenerator::generateStoneResource);
		worldGenerator.addWorldObjects(world, 2, 2, world.getWidth() / 10, TerrainType.MOUNTAIN, TerrainGenerator::generateOreResource);
		worldGenerator.addWorldObjects(world, 2, 2, world.getWidth() / 20, TerrainType.MOUNTAIN, TerrainGenerator::generateGoldResource);
		worldGenerator.addWorldObjects(world, 1, 1, world.getWidth() / 50, TerrainType.GRASLAND, PlantGenerator::generateNightShade);
		
		worldGenerator.addWorldObjects(world, 1, 1, 20, TerrainType.PLAINS, PlantGenerator::generateBerryBush);
	}

	private static WorldObject createPlayerCharacter(int id, String playerName, String playerProfession, World world, final CommonerImageIds commonerImageIds, final CommonerNameGenerator commonerNameGenerator, WorldObject organization, CharacterAttributes characterAttributes) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, 5);
		properties.put(Constants.Y, 5);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.HIT_POINTS, 17);
		properties.put(Constants.HIT_POINTS_MAX, 17);
		properties.put(Constants.NAME, playerName);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.KNIGHT);
		properties.put(Constants.LOOK_DIRECTION, LookDirection.SOUTH);
		properties.put(Constants.FOOD, 500);
		properties.put(Constants.WATER, 500);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.GROUP, new IdList().add(organization));
		
		WorldObjectContainer inventory = new WorldObjectContainer();
		inventory.add(ItemGenerator.getIronClaymore(1.0f));
		inventory.add(ItemGenerator.getIronCuirass(1.0f));
		inventory.add(ItemGenerator.getLongBow(1.0f));
		properties.put(Constants.INVENTORY, inventory);
		properties.put(Constants.GOLD, 100);
		properties.put(Constants.ORGANIZATION_GOLD, 0);
		properties.put(Constants.PROFIT_PERCENTAGE, 0);
		
		properties.put(Constants.PROFESSION, new PlayerCharacterProfession(playerProfession));
		properties.put(Constants.RELATIONSHIPS, new IdMap());
		properties.put(Constants.CHILDREN, new IdList());
		properties.put(Constants.SOCIAL, 0);
		properties.put(Constants.GENDER, "male");
		properties.put(Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		properties.put(Constants.CONDITIONS, new Conditions());
		
		properties.put(Constants.HEAD_EQUIPMENT, null);
		properties.put(Constants.TORSO_EQUIPMENT, null);
		properties.put(Constants.ARMS_EQUIPMENT, null);
		properties.put(Constants.LEGS_EQUIPMENT, null);
		properties.put(Constants.FEET_EQUIPMENT, null);
		properties.put(Constants.LEFT_HAND_EQUIPMENT, null);
		properties.put(Constants.RIGHT_HAND_EQUIPMENT, null);
		
		properties.put(Constants.EXPERIENCE, 0);
		properties.put(Constants.ARMOR, 18);
		
		properties.put(Constants.STRENGTH, characterAttributes.getStrength());
		properties.put(Constants.DEXTERITY, characterAttributes.getDexterity());
		properties.put(Constants.CONSTITUTION, characterAttributes.getConstitution());
		properties.put(Constants.INTELLIGENCE, characterAttributes.getIntelligence());
		properties.put(Constants.WISDOM, characterAttributes.getWisdom());
		properties.put(Constants.CHARISMA, characterAttributes.getCharisma());
		
		SkillUtils.addAllSkills(properties);
		properties.put(Constants.KNOWN_SPELLS, new ArrayList<>());
		properties.put(Constants.STUDYING_SPELLS, new PropertyCountMap());

		properties.put(Constants.DAMAGE, 8);
		properties.put(Constants.DAMAGE_RESIST, 100);
		
		if (Boolean.getBoolean("DEBUG")) {
			((List<Object>)properties.get(Constants.KNOWN_SPELLS)).addAll(Actions.getMagicSpells());
		}
		
		final WorldObject playerCharacter = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, new CommonerOnTurn(commonerImageIds, commonerNameGenerator, organization), null);
		return playerCharacter;
	}

    private static void createAndShowGUI(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster) throws IOException {
        JFrame frame = new JFrame("World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        WorldPanel worldPanel = new WorldPanel(playerCharacter, world, dungeonMaster);
        worldPanel.setOpaque(true);
        frame.setContentPane(worldPanel);
        
        frame.pack();
        frame.setVisible(true);
        
        ToolTipManager.sharedInstance().setDismissDelay(9999999);
        
        worldPanel.createGuiRespondToImage();
    }
    
    public static void executeAction(WorldObject playerCharacter, ManagedOperation action, int[] args, World world, DungeonMaster dungeonMaster, WorldObject target, WorldPanel worldPanel) {
    	if (canActionExecute(playerCharacter, action, args, world, target)) {
    		dungeonMaster.executeAction(action, playerCharacter, target, args, world);
    		dungeonMaster.runWorld(world);
    		exploreWorld(playerCharacter, world);
    		worldPanel.centerViewOnPlayerCharacter();
    		worldPanel.repaint();
    	}
	}
    
    public static boolean canActionExecute(WorldObject playerCharacter, ManagedOperation action, int[] args, World world, WorldObject target) {
    	return action.isActionPossible(playerCharacter, target, args, world) 
    			&& playerCharacter.canWorldObjectPerformAction(action)
    			&& action.isValidTarget(playerCharacter, target, world);
    }

	private static void exploreWorld(WorldObject playerCharacter, World world) {
		int x = playerCharacter.getProperty(Constants.X);
		int y = playerCharacter.getProperty(Constants.Y);
		world.getTerrain().explore(x, y, 10);
	}
}
