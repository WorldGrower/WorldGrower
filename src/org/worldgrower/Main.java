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
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import org.worldgrower.actions.ArenaFightOnTurn;
import org.worldgrower.actions.BrawlListener;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.condition.ConditionListener;
import org.worldgrower.curse.CurseListener;
import org.worldgrower.deity.DeityWorldOnTurn;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.generator.TerrainGenerator;
import org.worldgrower.generator.WorldGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.CommonerImageIds;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.music.BackgroundMusicUtils;
import org.worldgrower.gui.music.MusicPlayer;
import org.worldgrower.gui.start.CharacterAttributes;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.terrain.TerrainType;

/**
 * This class is responsible for building the world and creating the gui.
 */
public class Main {

	private static JFrame frame = null;
	private static MusicPlayer musicPlayer = null;
	
	public static void run(String playerName, String playerProfession, String gender, int worldWidth, int worldHeight, int enemyDensity, int villagerCount, int seed, boolean playBackgroundMusic, CharacterAttributes characterAttributes, ImageInfoReader imageInfoReader, ImageIds playerCharacterImageId) throws Exception {
		DungeonMaster dungeonMaster = new DungeonMaster();
		WorldOnTurnImpl worldOnTurn = new WorldOnTurnImpl(new DeityWorldOnTurn(), new ArenaFightOnTurn());
		World world = new WorldImpl(worldWidth, worldHeight, dungeonMaster, worldOnTurn);
		int playerCharacterId = world.generateUniqueId();
		
		final CommonerImageIds commonerImageIds = new CommonerImageIds();
		final CommonerNameGenerator commonerNameGenerator = new CommonerNameGeneratorImpl();
		final WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		final CommonerGenerator commonerGenerator = new CommonerGenerator(seed, commonerImageIds, commonerNameGenerator);
		
		final WorldObject playerCharacter = CommonerGenerator.createPlayerCharacter(playerCharacterId, playerName, playerProfession, gender, world, commonerGenerator, organization, characterAttributes, playerCharacterImageId);
		world.addWorldObject(playerCharacter);
		
		addDefaultWorldObjects(world, commonerGenerator, organization, villagerCount, seed);
		
		addWorldListeners(world);
		exploreWorld(playerCharacter, world);
		
		addEnemiesAndFriendlyAnimals(enemyDensity, world, seed);
		
		createAndShowGUIInvokeLater(playerCharacter, world, dungeonMaster, playBackgroundMusic, imageInfoReader);
	}

	private static void addWorldListeners(World world) {
		world.addListener(new CurseListener(world));
		world.addListener(new ConditionListener(world));
		world.addListener(new BrawlListener());
	}

	private static void addEnemiesAndFriendlyAnimals(int enemyDensity, World world, int seed) {
		WorldGenerator worldGenerator = new WorldGenerator(seed);
		WorldObject verminOrganization = GroupPropertyUtils.create(null, "vermin", world);
		CreatureGenerator creatureGenerator = new CreatureGenerator(verminOrganization);
		
		if (enemyDensity > 0) {
			PlantGenerator plantGenerator = new PlantGenerator(verminOrganization);
			worldGenerator.addWorldObjects(world, 1, 1, 5, TerrainType.GRASLAND, creatureGenerator::generateRat);
			worldGenerator.addWorldObjects(world, 1, 1, 5, TerrainType.GRASLAND, creatureGenerator::generateSlime);
			worldGenerator.addWorldObjects(world, 2, 2, 4, TerrainType.PLAINS, plantGenerator::generateDemonTree);
		}
		
		worldGenerator.addWorldObjects(world, 1, 1, world.getWidth() / 20, TerrainType.WATER, creatureGenerator::generateFish);
	}
	
	public static void load(File fileToLoad, ImageInfoReader imageInfoReader) {
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = WorldImpl.load(fileToLoad);
		final WorldObject playerCharacter = world.findWorldObject(Constants.ID, 0);
		
		addWorldListeners(world);
		
		//TODO: load playBackgroundMusic flag from file
		createAndShowGUIInvokeLater(playerCharacter, world, dungeonMaster, true, imageInfoReader);
	}

	private static void createAndShowGUIInvokeLater(final WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, boolean playBackgroundMusic, ImageInfoReader imageInfoReader) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
            public void run() {
                try {
					createAndShowGUI(playerCharacter, world, dungeonMaster, playBackgroundMusic, imageInfoReader);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
            }
        });
	}

	private static void addDefaultWorldObjects(World world, CommonerGenerator commonerGenerator, WorldObject organization, int villagerCount, int seed) {
	
		PlantGenerator.generateBerryBush(3, 3, world);
		
		for(int i=0; i<villagerCount; i++) {
			commonerGenerator.generateCommoner(1, 1, world, organization);
		}
		
		new CreatureGenerator(organization).generateCow(7, 2, world);
		
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
		worldGenerator.addWorldObjects(world, 1, 1, world.getWidth() / 50, TerrainType.PLAINS, TerrainGenerator::generateOilResource);
		
		worldGenerator.addWorldObjects(world, 1, 1, 20, TerrainType.PLAINS, PlantGenerator::generateBerryBush);
	}



    private static void createAndShowGUI(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, boolean playBackgroundMusic, ImageInfoReader imageInfoReader) throws IOException {
    	if (frame != null) {
    		frame.dispose();
    	}
        frame = new JFrame("WorldGrower");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        IconUtils.setIcon(frame);
        
        WorldPanel worldPanel = new WorldPanel(playerCharacter, world, dungeonMaster, imageInfoReader);
        worldPanel.setOpaque(true);
        frame.setContentPane(worldPanel);
        
        frame.pack();
        SwingUtils.centerFrame(frame);
        frame.setVisible(true);
        
        ToolTipManager.sharedInstance().setDismissDelay(9999999);
        
        worldPanel.createGuiRespondToImage();
        
        if (musicPlayer == null && playBackgroundMusic) {
        	musicPlayer = BackgroundMusicUtils.startBackgroundMusic();
        } else if (musicPlayer != null && !playBackgroundMusic) {
        	musicPlayer.stop();
        	musicPlayer = null;
        }
    }

    public static void executeAction(WorldObject playerCharacter, ManagedOperation action, int[] args, World world, DungeonMaster dungeonMaster, WorldObject target, WorldPanel worldPanel) {
    	if (canActionExecute(playerCharacter, action, args, world, target)) {
    		dungeonMaster.executeAction(action, playerCharacter, target, args, world);
    		dungeonMaster.runWorld(world, worldPanel.getWorldStateChangedListeners());
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
