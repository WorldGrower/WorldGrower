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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.worldgrower.attribute.LookDirection;
import org.worldgrower.gui.util.ImageUtils;

public class ImageInfoReader {

	private final Map<ImageIds, List<Image>> idToImages = new HashMap<>();
	private final List<ImageIds> characterImageIds = new ArrayList<>();
	
    public ImageInfoReader() throws IOException {
    	Sprites sprites = readSprites();
    	Sprites spritesb = readSpritesB();
    	Sprites sprites2a = readSprites2A();
    	Sprites sprites2b = readSprites2B();
    	Sprites sprites2c = readSprites2C();
    	Sprites sprites2d = readSprites2D();
    	Sprites sprites3a = readSprites3A();
    	Sprites sprites3b = readSprites3B();
    	Sprites sprites3c = readSprites3C();
    	Sprites sprites3d = readSprites3D();
    	Sprites sprites3e = readSprites3E();
    	Sprites sprites3f = readSprites3F();
    	Sprites sprites3g = readSprites3G();
    	Sprites objects = readObjects();
    	Sprites monsters = readMonsters();
    	Sprites orcSoldier = readOrcSoldier();
    	Sprites pirates = readPirates();
    	Sprites houses = readHouses();
    	Sprites tileA2 = readTileA2();
    	Sprites tileB = readTileB();
    	Sprites tileC = readTileC();
    	Sprites tileCprison = readTileCprison();
    	Sprites tileE = readTileE();
    	Sprites statues = readStatues();
    	Sprites rat = readRat();
    	Sprites slime = readSlime();
    	Sprites sprites420 = readSprites420();
    	Sprites spider = readSpritesSpider();
    	Sprites stone = readSpritesStone();
    	Sprites tora_vx_02 = readSpritesTora02();
    	Sprites fish = readSpritesFish();
    	Sprites cow = readSpritesCow();
    	Sprites forge = readSpritesForge();
    	Sprites terrainTransitions = readTerrainTransitions();
    	Sprites vampire = readSpritesVampire();
    	Sprites clothingShop = readSpritesClothingShop();
    	Sprites weavery = readSpritesWeavery();
    	Sprites brewery = readSpritesBrewery();
    	Sprites papermill = readSpritesPapermill();
    	Sprites apothecary = readSpritesApothecary();
    	Sprites workbench = readSpritesWorkbench();
    	Sprites magic1 = readSpritesMagic1();
    	Sprites fire1 = readSpritesFire1();
    	Sprites ice1 = readSpritesIce1();
    	Sprites thunder1 = readSpritesThunder1();
    	Sprites darkness1 = readSpritesDarkness1();
    	Sprites heal1 = readSpritesHeal1();
    	Sprites sprites01 = readSprites01();
    	Sprites aktor1 = readSpritesAktor1();
    	Sprites aktor3 = readSpritesAktor3();
    	
    	Sprites smallTree = readSpritesSmallTree();
    	Sprites smallBorealTree = readSpritesSmallBorealTree();
    	Sprites vineWithGrapes = readSpritesVineWithGrapes();
    	Sprites vine = readSpritesVine();
    	
    	addCharacter(ImageIds.KNIGHT, sprites, 0, 0, 1, 1);
    	addCharacter(ImageIds.GUARD, sprites, 0, 4, 1, 1);
    	add(ImageIds.BUCKET, objects.getSubImage(10, 10, 1, 1));
        add(ImageIds.YOUNG_BERRY_BUSH, objects.getSubImage(1, 4, 1, 1));
        add(ImageIds.GOBLIN, monsters.getSubImage(2, 4, 1, 1));
        add(ImageIds.TRUNK, objects.getSubImage(3, 5, 1, 1));
        add(ImageIds.TREE, objects.getSubImage(6, 4, 2, 2));
        add(ImageIds.SHACK, houses.getSubImage(12, 0, 2, 4));
        addCharacter(ImageIds.FEMALE_COMMONER, sprites, 3, 0, 1, 1);
        add(ImageIds.WELL, objects.getSubImage(0, 11, 2, 2));
        addCharacter(ImageIds.SKELETON, monsters, 0, 0, 1, 1);
        add(ImageIds.SKELETAL_REMAINS, tileE.getSubImage(4, 3, 1, 1));
        add(ImageIds.ORC_SOLDIER, orcSoldier.getSubImage(0, 0, 1, 1));
        add(ImageIds.STONE_RESOURCE, pirates.getSubImage(10, 5, 2, 2));
        add(ImageIds.ORE_RESOURCE, pirates.getSubImage(14, 5, 2, 2));
        add(ImageIds.HOUSE, houses.getSubImage(0, 0, 2, 4));
        add(ImageIds.HOUSE2, houses.getSubImage(2, 0, 2, 4));
        add(ImageIds.HOUSE3, houses.getSubImage(4, 0, 2, 4));
        add(ImageIds.HOUSE4, houses.getSubImage(6, 0, 2, 4));
        add(ImageIds.HOUSE5, houses.getSubImage(8, 0, 2, 4));
        add(ImageIds.HOUSE6, houses.getSubImage(10, 0, 2, 4));
        add(ImageIds.HOUSE7, houses.getSubImage(12, 0, 2, 4));
        add(ImageIds.HOUSE8, houses.getSubImage(14, 0, 2, 4));
        
        add(ImageIds.SMITH, forge.getSubImage(0, 0, 1, 1));
        addCharacter(ImageIds.BLUE_HAIRED_COMMONER, sprites, 6, 0, 1, 1);
        addCharacter(ImageIds.TULBAN_MALE_COMMONER, sprites, 9, 0, 1, 1);
        addCharacter(ImageIds.GREEN_HAIRED_COMMONER, sprites, 3, 4, 1, 1);
        addCharacter(ImageIds.MALE_SCIENTIST_COMMONER, sprites, 6, 4, 1, 1);
        addCharacter(ImageIds.BLACK_HAIRED_FEMALE_COMMONER, sprites, 9, 4, 1, 1);
        addCharacter(ImageIds.GREEN_HAIRED_MALE_BOY, spritesb, 0, 0, 1, 1);
        addCharacter(ImageIds.GREEN_HAIRED_MALE_COMMONER, spritesb, 3, 0, 1, 1);
        addCharacter(ImageIds.PURPLE_HAIRED_MALE_GIRL, spritesb, 6, 0, 1, 1);
        addCharacter(ImageIds.PURPLE_HAIRED_MALE_COMMONER, spritesb, 9, 0, 1, 1);
        addCharacter(ImageIds.PURPLE_HAT_COMMONER, spritesb, 0, 4, 1, 1);
        addCharacter(ImageIds.GREY_HAIRED_MALE_COMMONER, spritesb, 3, 4, 1, 1);
        addCharacter(ImageIds.PINK_HAT_FEMALE_COMMONER, spritesb, 6, 4, 1, 1);
        addCharacter(ImageIds.RED_HAIR_FEMALE_COMMONER, spritesb, 9, 4, 1, 1);
        addCharacter(ImageIds.BLUE_HAT_MALE_COMMONER, sprites2a, 0, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_NUN_COMMONER, sprites2a, 3, 0, 1, 1);
        addCharacter(ImageIds.BOLD_MALE_COMMONER, sprites2a, 6, 0, 1, 1);
        addCharacter(ImageIds.MALE_CHEF_COMMONER, sprites2a, 9, 0, 1, 1);
        addCharacter(ImageIds.SPIKY_HAIR_MALE_COMMONER, sprites2a, 0, 4, 1, 1);
        addCharacter(ImageIds.GREY_HAIR_MALE_COMMONER, sprites2a, 3, 4, 1, 1);
        addCharacter(ImageIds.BLUE_DRESS_FEMALE_COMMONER, sprites2a, 6, 4, 1, 1);
        addCharacter(ImageIds.MALE_BAKER_COMMONER, sprites2a, 9, 4, 1, 1);
        add(ImageIds.STATUE_OF_DEITY, statues.getSubImage(3, 1, 1, 2));
        addCharacter(ImageIds.GREEN_HAIR_MALE_COMMONER, sprites2b, 0, 0, 1, 1);
        addCharacter(ImageIds.MALE_SAGE_COMMONER, sprites2b, 3, 0, 1, 1);
        addCharacter(ImageIds.MALE_MERCHANT_COMMONER, sprites2b, 6, 0, 1, 1);
        addCharacter(ImageIds.MALE_CAPTAIN_COMMONER, sprites2b, 9, 0, 1, 1);
        addCharacter(ImageIds.ORANGE_HAIR_BOY_COMMONER, sprites2b, 0, 4, 1, 1);
        addCharacter(ImageIds.TULBAN_BOY_COMMONER, sprites2b, 3, 4, 1, 1);
        addCharacter(ImageIds.SKINHEAD_BOY_COMMONER, sprites2b, 6, 4, 1, 1);
        addCharacter(ImageIds.FEMALE_BRIDE_COMMONER, sprites2b, 9, 4, 1, 1);
        add(ImageIds.RAT, rat.getSubImage(0, 0, 1, 1));
        add(ImageIds.SLIME, slime.getSubImage(0, 0, 1, 1));
        
        add(ImageIds.IRON_CLAYMORE, sprites420.getSubImage(2, 5, 1, 1));
        add(ImageIds.IRON_CUIRASS, sprites420.getSubImage(3, 13, 1, 1));
        
        add(ImageIds.SPIDER, spider.getSubImage(0, 0, 1, 1));
        add(ImageIds.COCOON, stone.getSubImage(5, 0, 1, 1));
        add(ImageIds.GRAVE, objects.getSubImage(6, 9, 1, 1));
        add(ImageIds.GRAPE_VINE, vine.getSubImage(0, 0, 1, 1));
        
        add(ImageIds.IRON_HELMET, sprites420.getSubImage(11, 13, 1, 1));
        add(ImageIds.IRON_GAUNTLETS, sprites420.getSubImage(12, 14, 1, 1));
        add(ImageIds.IRON_GREAVES, sprites420.getSubImage(4, 13, 1, 1));
        add(ImageIds.IRON_BOOTS, sprites420.getSubImage(4, 14, 1, 1));
        add(ImageIds.IRON_SHIELD, sprites420.getSubImage(8, 12, 1, 1));
        add(ImageIds.LONGBOW, sprites420.getSubImage(3, 11, 1, 1));
        
        addCharacter(ImageIds.MALE_LEADER, sprites2c, 0, 0, 1, 1);
        addCharacter(ImageIds.MALE_SENATOR, sprites2c, 3, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_SENATOR, sprites2c, 6, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_RED_EYE, sprites2c, 9, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_BUNNY, sprites2c, 0, 4, 1, 1);
        addCharacter(ImageIds.MALE_BELLBOY, sprites2c, 3, 4, 1, 1);
        addCharacter(ImageIds.MALE_LAWYER, sprites2c, 6, 4, 1, 1);
        addCharacter(ImageIds.FEMALE_MAID, sprites2c, 9, 4, 1, 1);
        
        add(ImageIds.BERRY, sprites420.getSubImage(1, 0, 1, 1));
        
        addCharacter(ImageIds.MALE_SAILOR, sprites2d, 0, 0, 1, 1);
        addCharacter(ImageIds.MALE_WHITE_SHIRT_SAILOR, sprites2d, 3, 0, 1, 1);
        addCharacter(ImageIds.MALE_BLOND_SAILOR, sprites2d, 6, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_RED_HAIRED_SAILOR, sprites2d, 9, 0, 1, 1);
      
        add(ImageIds.GRAPES, sprites420.getSubImage(6, 0, 1, 1));
        add(ImageIds.WOOD, pirates.getSubImage(4, 3, 1, 1));
        add(ImageIds.IRON, pirates.getSubImage(5, 7, 1, 1));
        add(ImageIds.STONE, pirates.getSubImage(5, 8, 1, 1));
        add(ImageIds.GOLD_RESOURCE, pirates.getSubImage(12, 5, 2, 2));
        add(ImageIds.GOLD, pirates.getSubImage(4, 8, 1, 1));
        add(ImageIds.WINE, sprites420.getSubImage(4, 2, 1, 1));
        
        addCharacter(ImageIds.MALE_CAPTAIN, sprites2d, 0, 4, 1, 1);
        addCharacter(ImageIds.FEMALE_SAILOR, sprites2d, 3, 4, 1, 1);
        addCharacter(ImageIds.MALE_BALD_SAILOR, sprites2d, 6, 4, 1, 1);
        addCharacter(ImageIds.MALE_GREEN_BANDANA_SAILOR, sprites2d, 9, 4, 1, 1);
        
        add(ImageIds.PAPER, sprites420.getSubImage(13, 17, 1, 1));
        add(ImageIds.PAPER_MILL, papermill.getSubImage(0, 0, 1, 1));
        add(ImageIds.WATER, sprites420.getSubImage(3, 2, 1, 1));
        
		addCharacter(ImageIds.MALE_BALD_COMMONER, sprites3a, 0, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_OLD_COMMONER, sprites3a, 3, 0, 1, 1);
		addCharacter(ImageIds.MALE_BEARDED_COMMONER, sprites3a, 6, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_BLOND_COMMONER, sprites3a, 9, 0, 1, 1);
		addCharacter(ImageIds.YOUNG_BOY_COMMONER, sprites3a, 0, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_ORANGE_GIRL, sprites3a, 3, 4, 1, 1);
		addCharacter(ImageIds.MALE_BLUE_PANTS_BOY_COMMONER, sprites3a, 6, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_ORANGE_HAIR_GIRL_COMMONER, sprites3a, 9, 4, 1, 1);
		
		add(ImageIds.BLACK_CROSS, pirates.getSubImage(7, 6, 1, 1));
		
		addCharacter(ImageIds.MALE_OLD_COMMONER, sprites3b, 0, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_OLDER_COMMONER, sprites3b, 3, 0, 1, 1);
		addCharacter(ImageIds.MALE_SPIKY_HAIRED_COMMONER, sprites3b, 6, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_MILKER_COMMONER, sprites3b, 9, 0, 1, 1);
		addCharacter(ImageIds.MALE_GREEN_HAIRED_COMMONER, sprites3b, 0, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_BLOND_GIRL, sprites3b, 3, 4, 1, 1);
		addCharacter(ImageIds.MALE_BROWN_HAIR_BOY_COMMONER, sprites3b, 6, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_BLUE_HAIR_GIRL_COMMONER, sprites3b, 9, 4, 1, 1);
		
		add(ImageIds.LIBRARY, tora_vx_02.getSubImage(0, 10, 1, 2));
		add(ImageIds.BURNING, sprites420.getSubImage(2, 20, 1, 1));
		add(ImageIds.SPELL_BOOK, sprites420.getSubImage(6, 29, 1, 1));
		add(ImageIds.KEY, sprites420.getSubImage(7, 16, 1, 1));
		
		add(ImageIds.NIGHT_SHADE_PLANT, tileB.getSubImage(3, 6, 1, 1));
		add(ImageIds.NIGHT_SHADE, statues.getSubImage(12, 0, 1, 1));
		add(ImageIds.POISON, sprites420.getSubImage(5, 2, 1, 1));
		
		addCharacter(ImageIds.MALE_3C_1, sprites3c, 0, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_3C_1, sprites3c, 3, 0, 1, 1);
		addCharacter(ImageIds.MALE_3C_2, sprites3c, 6, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_3C_2, sprites3c, 9, 0, 1, 1);
		addCharacter(ImageIds.MALE_3C_3, sprites3c, 0, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_3C_3, sprites3c, 3, 4, 1, 1);
		addCharacter(ImageIds.MALE_3C_4, sprites3c, 6, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_3C_4, sprites3c, 9, 4, 1, 1);
		
		addCharacter(ImageIds.MALE_3D_1, sprites3d, 0, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_3D_1, sprites3d, 3, 0, 1, 1);
		addCharacter(ImageIds.MALE_3D_2, sprites3d, 6, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_3D_2, sprites3d, 9, 0, 1, 1);
		addCharacter(ImageIds.MALE_3D_3, sprites3d, 0, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_3D_3, sprites3d, 3, 4, 1, 1);
		
		addCharacter(ImageIds.MALE_3E_1, sprites3e, 0, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_3E_1, sprites3e, 3, 0, 1, 1);
		addCharacter(ImageIds.MALE_3E_2, sprites3e, 6, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_3E_2, sprites3e, 9, 0, 1, 1);
		addCharacter(ImageIds.MALE_3E_3, sprites3e, 0, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_3E_3, sprites3e, 3, 4, 1, 1);
		addCharacter(ImageIds.MALE_3E_4, sprites3e, 6, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_3E_4, sprites3e, 9, 4, 1, 1);
		
		add(ImageIds.COTTON_PLANT, tileB.getSubImage(4, 4, 1, 1));
		add(ImageIds.COTTON, statues.getSubImage(11, 0, 1, 1));
		add(ImageIds.VOTING_BOX, tileB.getSubImage(9, 2, 1, 1));
    
    
		add(ImageIds.COTTON_SHIRT, sprites420.getSubImage(0, 13, 1, 1));
		add(ImageIds.COTTON_HAT, sprites420.getSubImage(10, 13, 1, 1));
		add(ImageIds.COTTON_BOOTS, sprites420.getSubImage(0, 14, 1, 1));
		add(ImageIds.COTTON_ARMS, sprites420.getSubImage(7, 14, 1, 1));
		add(ImageIds.COTTON_PANTS, sprites420.getSubImage(2, 13, 1, 1));
		
		add(ImageIds.BED, tileE.getSubImage(10, 4, 1, 2));
		add(ImageIds.INVISIBILITY_INDICATOR, sprites420.getSubImage(2, 21, 1, 1));
		add(ImageIds.POISONED_INDICATOR, sprites420.getSubImage(4, 24, 1, 1));
		
		addCharacter(ImageIds.WEREWOLF, monsters, 0, 4, 1, 1);
		add(ImageIds.TRAINING_DUMMY, tileB.getSubImage(1, 13, 1, 2));
		add(ImageIds.SLEEPING_INDICATOR, sprites420.getSubImage(13, 21, 1, 1));
		add(ImageIds.PARALYZED_INDICATOR, sprites420.getSubImage(5, 23, 1, 1));
		
		addCharacter(ImageIds.MALE_3F_1, sprites3f, 0, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_3F_1, sprites3f, 3, 0, 1, 1);
		addCharacter(ImageIds.MALE_3F_2, sprites3f, 6, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_3F_2, sprites3f, 9, 0, 1, 1);
		addCharacter(ImageIds.MALE_3F_3, sprites3f, 0, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_3F_3, sprites3f, 3, 4, 1, 1);
		addCharacter(ImageIds.MALE_3F_4, sprites3f, 6, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_3F_4, sprites3f, 9, 4, 1, 1);
		
		add(ImageIds.WATER_WALK_INDICATOR, sprites420.getSubImage(9, 21, 1, 1));
		add(ImageIds.DOOR, tileC.getSubImage(12, 6, 1, 2));
		add(ImageIds.STONE_DOOR, tileC.getSubImage(12, 2, 1, 2));
		add(ImageIds.WINDOW, tileC.getSubImage(14, 6, 1, 1));
		add(ImageIds.STONE_WINDOW, tileC.getSubImage(13, 2, 1, 2));
		
		addDoorToHouse(ImageIds.SHACK);
		
		add(ImageIds.GRASS_BACKGROUND, tileA2.getSubImage(0, 0, 2, 2));
		
		createCompleteHouse();
		
		add(ImageIds.UNCONSCIOUS_INDICATOR, sprites420.getSubImage(12, 25, 1, 1));
		
		add(ImageIds.JAIL_DOOR, pirates.getSubImage(6, 7, 1, 2));
		add(ImageIds.JAIL_LEFT, tileCprison.getSubImage(0, 0, 1, 3));
		add(ImageIds.JAIL_UP, tileCprison.getSubImage(1, 0, 1, 2));
		add(ImageIds.JAIL_RIGHT, tileCprison.getSubImage(2, 0, 1, 3));
		
		add(ImageIds.OIL_RESOURCE, pirates.getSubImage(9, 0, 1, 1));
		add(ImageIds.OIL, sprites420.getSubImage(6, 2, 1, 1));
		add(ImageIds.SACRIFIAL_ALTAR, tora_vx_02.getSubImage(4, 8, 1, 2));
		
		createCompleteLibrary();
		add(ImageIds.FISH_CREATURE, fish.getSubImage(0, 0, 1, 1));
		add(ImageIds.FISHING_POLE, sprites420.getSubImage(6, 9, 1, 1));
		add(ImageIds.RAW_FISH, sprites420.getSubImage(10, 1, 1, 1));
		
		add(ImageIds.ARENA_WALL, tileC.getSubImage(7, 11, 1, 1));
		createArenaWall48x48();
		createArenaVertical();
		createArenaHorizontal();
		
		addCharacter(ImageIds.MALE_3G_1, sprites3g, 0, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_3G_1, sprites3g, 3, 0, 1, 1);
		addCharacter(ImageIds.MALE_3G_2, sprites3g, 6, 0, 1, 1);
		addCharacter(ImageIds.FEMALE_3G_2, sprites3g, 9, 0, 1, 1);
		addCharacter(ImageIds.MALE_3G_3, sprites3g, 0, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_3G_3, sprites3g, 3, 4, 1, 1);
		addCharacter(ImageIds.MALE_3G_4, sprites3g, 6, 4, 1, 1);
		addCharacter(ImageIds.FEMALE_3G_4, sprites3g, 9, 4, 1, 1);
		
		add(ImageIds.REPAIR_HAMMER, sprites420.getSubImage(3, 9, 1, 1));
		
		add(ImageIds.IRON_GREATSWORD, sprites420.getSubImage(3, 5, 1, 1));
		addCharacter(ImageIds.COW, cow, 0, 0, 1, 1);
		addCharacter(ImageIds.BULL, cow, 6, 0, 1, 1);
		add(ImageIds.IRON_AXE, sprites420.getSubImage(5, 10, 1, 1));
		add(ImageIds.IRON_GREATAXE, sprites420.getSubImage(2, 10, 1, 1));
		add(ImageIds.MEAT, sprites420.getSubImage(11, 1, 1, 1));
		
		addCharacter(ImageIds.GHOUL, monsters, 3, 4, 1, 1);
		add(ImageIds.LIGHTNING_BOLT, sprites420.getSubImage(0, 23, 1, 1));
		add(ImageIds.ENLARGE_MAGIC_SPELL, sprites420.getSubImage(13, 26, 1, 1));
		add(ImageIds.REDUCE_MAGIC_SPELL, sprites420.getSubImage(12, 26, 1, 1));
		add(ImageIds.RAY_OF_FROST, sprites420.getSubImage(10, 23, 1, 1));
		add(ImageIds.MINOR_HEAL, sprites420.getSubImage(7, 22, 1, 1));
		add(ImageIds.FIRE_BOLT, sprites420.getSubImage(0, 20, 1, 1));
		add(ImageIds.ANIMATE_DEAD, sprites420.getSubImage(13, 25, 1, 1));
		add(ImageIds.BURDEN_MAGIC_SPELL, sprites420.getSubImage(2, 26, 1, 1));
		add(ImageIds.FEATHER_MAGIC_SPELL, sprites420.getSubImage(0, 26, 1, 1));
		add(ImageIds.CURE_DISEASE, sprites420.getSubImage(9, 22, 1, 1));
		add(ImageIds.CURE_POISON, sprites420.getSubImage(10, 22, 1, 1));
		add(ImageIds.DETECT_POISON_AND_DISEASE, sprites420.getSubImage(12, 22, 1, 1));
		add(ImageIds.DISGUISE_MAGIC_SPELL, sprites420.getSubImage(6, 21, 1, 1));
		add(ImageIds.DISINTEGRATE_ARMOR, sprites420.getSubImage(5, 21, 1, 1));
		add(ImageIds.INFLICT_WOUNDS_MAGIC_SPELL, sprites420.getSubImage(3, 24, 1, 1));
		add(ImageIds.LOCK_MAGIC_SPELL, sprites420.getSubImage(11, 16, 1, 1));
		add(ImageIds.UNLOCK_MAGIC_SPELL, sprites420.getSubImage(11, 16, 1, 1));
		add(ImageIds.MEND_MAGIC_SPELL, sprites420.getSubImage(2, 17, 1, 1));
		add(ImageIds.MINOR_ILLUSION_MAGIC_SPELL, sprites420.getSubImage(10, 24, 1, 1));
		
		add(ImageIds.WOODEN_SHIELD, sprites420.getSubImage(2, 12, 1, 1));
		add(ImageIds.CHEST, sprites420.getSubImage(7, 29, 1, 1));
		add(ImageIds.MAGIC_ICON, sprites420.getSubImage(7, 24, 1, 1));
		add(ImageIds.DISINTEGRATE_WEAPON, sprites420.getSubImage(4, 25, 1, 1));
		add(ImageIds.SOUL_TRAPPED_INDICATOR, sprites420.getSubImage(3, 21, 1, 1));
		add(ImageIds.SOUL_GEM, sprites420.getSubImage(3, 17, 1, 1));
		add(ImageIds.FILLED_SOUL_GEM, sprites420.getSubImage(6, 17, 1, 1));
		add(ImageIds.GOLD_COIN, sprites420.getSubImage(10, 15, 1, 1));
		add(ImageIds.SILVER_COIN, sprites420.getSubImage(11, 15, 1, 1));
		add(ImageIds.CAPTURE, sprites420.getSubImage(1, 19, 1, 1));
		add(ImageIds.HEART, sprites420.getSubImage(8, 22, 1, 1));
		add(ImageIds.BLOOD, sprites420.getSubImage(6, 25, 1, 1));
		add(ImageIds.GOLD_RING, sprites420.getSubImage(2, 19, 1, 1));
		
		addCharacter(ImageIds.ANIMATED_SUIT_OF_ARMOR, monsters, 6, 4, 1, 1);
		add(ImageIds.DISPEL_MAGIC_SPELL, sprites420.getSubImage(8, 24, 1, 1));
		add(ImageIds.SILENCED_CONDITION, sprites420.getSubImage(11, 21, 1, 1));
		
		add(ImageIds.SMALL_FLOWERS, tileA2.getSubImage(1, 3, 1, 1));
		add(ImageIds.LARGE_FLOWERS, tileA2.getSubImage(0, 4, 2, 2));
		add(ImageIds.SMALL_GRASS, tileA2.getSubImage(2, 3, 1, 1));
		add(ImageIds.LARGE_GRASS, tileA2.getSubImage(2, 4, 2, 2));
		add(ImageIds.SMALL_DIRT, tileA2.getSubImage(4, 3, 1, 1));
		add(ImageIds.LARGE_DIRT, tileA2.getSubImage(4, 4, 2, 2));
		resizeSmallFlowers();
		
		add(ImageIds.STATUE_OF_DEMETER, statues.getSubImage(3, 1, 1, 2));
		add(ImageIds.STATUE_OF_HEPHAESTUS, statues.getSubImage(2, 1, 1, 2));
		add(ImageIds.STATUE_OF_HADES, statues.getSubImage(1, 1, 1, 2));
		add(ImageIds.STATUE_OF_APHRODITE, statues.getSubImage(4, 3, 1, 2));
		add(ImageIds.STATUE_OF_APOLLO, statues.getSubImage(2, 1, 1, 2));
		add(ImageIds.STATUE_OF_DIONYSUS, statues.getSubImage(14, 2, 1, 2));
		add(ImageIds.STATUE_OF_ARES, statues.getSubImage(7, 1, 1, 2));
		add(ImageIds.STATUE_OF_ARTEMIS, statues.getSubImage(10, 8, 1, 2));
		add(ImageIds.STATUE_OF_HERMES, statues.getSubImage(4, 1, 1, 2));
		add(ImageIds.STATUE_OF_ATHENA, statues.getSubImage(5, 1, 1, 2));

		add(ImageIds.BOREAL_TREE, objects.getSubImage(0, 9, 2, 2));
		add(ImageIds.INN, tileC.getSubImage(0, 0, 4, 6));
		add(ImageIds.STONE_WALL, houses.getSubImage(0, 4, 2, 4));
		createInnImage();

		add(ImageIds.TRANSITION_TOP_LEFT, terrainTransitions.getSubImage(0, 0, 1, 1));
		add(ImageIds.TRANSITION_TOP_RIGHT, terrainTransitions.getSubImage(1, 0, 1, 1));
		add(ImageIds.TRANSITION_DOWN_LEFT, terrainTransitions.getSubImage(0, 1, 1, 1));
		add(ImageIds.TRANSITION_DOWN_RIGHT, terrainTransitions.getSubImage(1, 1, 1, 1));
		add(ImageIds.TRANSITION_LEFT, terrainTransitions.getSubImage(2, 0, 1, 1));
		add(ImageIds.TRANSITION_RIGHT, terrainTransitions.getSubImage(2, 1, 1, 1));
		add(ImageIds.TRANSITION_TOP, terrainTransitions.getSubImage(3, 0, 1, 1));
		add(ImageIds.TRANSITION_DOWN, terrainTransitions.getSubImage(3, 1, 1, 1));
    
		add(ImageIds.SIGN_POST, tileE.getSubImage(2, 14, 1, 1));
		add(ImageIds.INTOXICATED_CONDITION, sprites420.getSubImage(12, 24, 1, 1));
		add(ImageIds.DETECT_MAGIC, sprites420.getSubImage(0, 15, 1, 1));
		add(ImageIds.NEWS_PAPER, sprites420.getSubImage(11, 17, 1, 1));
		add(ImageIds.FIRE_TRAP, sprites420.getSubImage(1, 20, 1, 1));
		add(ImageIds.INVESTIGATE, sprites420.getSubImage(12, 17, 1, 1));
		add(ImageIds.STATUE_OF_ZEUS, statues.getSubImage(3, 5, 1, 2));
		add(ImageIds.STATUE_OF_HERA, statues.getSubImage(14, 8, 1, 2));
		add(ImageIds.STATUE_OF_POSEIDON, statues.getSubImage(12, 8, 1, 2));
		
		addCharacter(ImageIds.LICH, vampire, 0, 0, 1, 1);
		
		add(ImageIds.IRON_MACE, sprites420.getSubImage(4, 9, 1, 1));
		add(ImageIds.IRON_KATAR, sprites420.getSubImage(8, 6, 1, 1));
		add(ImageIds.SLEEPING_POTION, sprites420.getSubImage(1, 2, 1, 1));
		add(ImageIds.FEAR_INDICATOR, sprites420.getSubImage(3, 26, 1, 1));
		add(ImageIds.WEAVERY, weavery.getSubImage(0, 0, 1, 1));
		add(ImageIds.ENTANGLED_INDICATOR, tora_vx_02.getSubImage(0, 1, 1, 1));
		add(ImageIds.WORKBENCH, workbench.getSubImage(0, 0, 1, 1));
		add(ImageIds.DARKNESS_MAGIC_SPELL, sprites420.getSubImage(2, 21, 1, 1));
		add(ImageIds.BREWERY, brewery.getSubImage(0, 0, 3, 2));
		add(ImageIds.TRAP_CONTAINER_MAGIC_SPELL, sprites420.getSubImage(5, 20, 1, 1));
		add(ImageIds.LEASH, sprites420.getSubImage(13, 9, 1, 1));
		add(ImageIds.CLAIM_CATTLE, sprites420.getSubImage(7, 9, 1, 1));
		add(ImageIds.APOTHECARY, apothecary.getSubImage(0, 0, 1, 1));
		add(ImageIds.HAMMER, sprites420.getSubImage(3, 9, 1, 1));
		
		addMagic(ImageIds.MAGIC1, magic1, 5, 6);
		addMagic(ImageIds.FIRE1, fire1, 5, 4);
		addMagic(ImageIds.ICE1, ice1, 5, 6);
		addMagic(ImageIds.THUNDER1, thunder1, 5, 6);
		addMagic(ImageIds.DARKNESS1, darkness1, 5, 6);
		
		add(ImageIds.DIMENSION_DOOR, tileE.getSubImage(0, 4, 1, 1));
		
		addMagic(ImageIds.HEAL1, heal1, 5, 5);
		add(ImageIds.ANIMAL_FRIENDSHIP_SPELL, sprites420.getSubImage(8, 22, 1, 1));
		add(ImageIds.EAT_REMAINS, sprites420.getSubImage(4, 12, 1, 1));
		add(ImageIds.GOLD_AMULET, sprites420.getSubImage(11, 19, 1, 1));
		add(ImageIds.ATAXIA, sprites420.getSubImage(2, 18, 1, 1));
		add(ImageIds.FIRE_BALL, sprites420.getSubImage(4, 20, 1, 1));
		add(ImageIds.MAJOR_ILLUSION_MAGIC_SPELL, sprites420.getSubImage(3, 19, 1, 1));
		add(ImageIds.CUDGEL, sprites420.getSubImage(0, 9, 1, 1));
		add(ImageIds.LARGE_CUDGEL, sprites420.getSubImage(1, 9, 1, 1));
		add(ImageIds.GOLDEN_AXE, sprites420.getSubImage(9, 10, 1, 1));
		
		addCharacter(ImageIds.MALE_01_1, sprites01, 0, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_01_1, sprites01, 3, 0, 1, 1);
        addCharacter(ImageIds.MALE_01_2, sprites01, 6, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_01_2, sprites01, 9, 0, 1, 1);
        addCharacter(ImageIds.MALE_01_3, sprites01, 0, 4, 1, 1);
        addCharacter(ImageIds.FEMALE_01_3, sprites01, 3, 4, 1, 1);
        addCharacter(ImageIds.MALE_01_4, sprites01, 6, 4, 1, 1);
        addCharacter(ImageIds.FEMALE_01_4, sprites01, 9, 4, 1, 1);
        
        addCharacter(ImageIds.MALE_AKTOR1_1, aktor1, 0, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_AKTOR1_1, aktor1, 3, 0, 1, 1);
        addCharacter(ImageIds.MALE_AKTOR1_2, aktor1, 6, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_AKTOR1_2, aktor1, 9, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_AKTOR1_4, aktor1, 0, 4, 1, 1);
        addCharacter(ImageIds.FEMALE_AKTOR1_3, aktor1, 3, 4, 1, 1);
        addCharacter(ImageIds.MALE_AKTOR1_4, aktor1, 6, 4, 1, 1);
        addCharacter(ImageIds.MALE_AKTOR1_3, aktor1, 9, 4, 1, 1);
        
        addCharacter(ImageIds.MALE_AKTOR3_1, aktor3, 0, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_AKTOR3_1, aktor3, 3, 0, 1, 1);
        addCharacter(ImageIds.MALE_AKTOR3_2, aktor3, 6, 0, 1, 1);
        addCharacter(ImageIds.FEMALE_AKTOR3_2, aktor3, 9, 0, 1, 1);
        addCharacter(ImageIds.MALE_AKTOR3_3, aktor3, 0, 4, 1, 1);
        addCharacter(ImageIds.FEMALE_AKTOR3_3, aktor3, 3, 4, 1, 1);
        addCharacter(ImageIds.MALE_AKTOR3_4, aktor3, 6, 4, 1, 1);
        addCharacter(ImageIds.FEMALE_AKTOR3_4, aktor3, 9, 4, 1, 1);
        
        add(ImageIds.SMALL_TREE, smallTree.getSubImage(0, 0, 1, 1));
        add(ImageIds.SMALL_BOREAL_TREE, smallBorealTree.getSubImage(0, 0, 1, 1));

        add(ImageIds.BUSH, objects.getSubImage(10, 13, 1, 1));
        add(ImageIds.VINE_WITH_GRAPES, vineWithGrapes.getSubImage(0, 0, 1, 1));
        add(ImageIds.YOUNG_NIGHT_SHADE_PLANT, tileB.getSubImage(4, 6, 1, 1));

    }
    
    private void resizeSmallFlowers() {
    	Image smallFlowers = idToImages.get(ImageIds.SMALL_FLOWERS).get(0);
    	Image grassBackground = idToImages.get(ImageIds.GRASS_BACKGROUND).get(0);
    	
    	BufferedImage off_Image = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) off_Image.getGraphics();

		g2.drawImage(grassBackground, 0, 0, null);
		g2.drawImage(smallFlowers, 0, 0, null);

		g2.dispose();
		idToImages.put(ImageIds.SMALL_FLOWERS, Arrays.asList(off_Image));
    }
    
    private void createInnImage() {
    	Image innImage = idToImages.get(ImageIds.INN).get(0);
    	Image stoneWallImage = idToImages.get(ImageIds.STONE_WALL).get(0);
		BufferedImage off_Image = new BufferedImage(48 * 6, 48 * 8, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) off_Image.getGraphics();
		
		g2.drawImage(stoneWallImage, 0, 0, null);
		g2.drawImage(stoneWallImage, stoneWallImage.getWidth(null), 0, null);
		g2.drawImage(stoneWallImage, 0, stoneWallImage.getHeight(null), null);
		g2.drawImage(stoneWallImage, stoneWallImage.getWidth(null), stoneWallImage.getHeight(null), null);
		
		g2.drawImage(innImage, 0, 0, null);
		
		addStoneDoorToInn(g2);
		
		g2.dispose();
		idToImages.put(ImageIds.INN, Arrays.asList(off_Image));
    }
    
    private void createArenaWall48x48() {
    	Image arenaWall = idToImages.get(ImageIds.ARENA_WALL).get(0);
		BufferedImage off_Image = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) off_Image.getGraphics();

		g2.drawImage(arenaWall, 0, 0, null);
		g2.drawImage(arenaWall, 0, 32, null);
		g2.drawImage(arenaWall, 32, 0, null);
		g2.drawImage(arenaWall, 32, 32, null);

		g2.dispose();
		idToImages.put(ImageIds.ARENA_WALL, Arrays.asList(off_Image));
    }
    
	private void createArenaVertical() {
		Image arenaWall = idToImages.get(ImageIds.ARENA_WALL).get(0);
		BufferedImage off_Image = new BufferedImage(48, 48 * 8, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) off_Image.getGraphics();
		for(int i=0; i<8; i++) {
			g2.drawImage(arenaWall, 0, 48 * i, null);
		}
		g2.dispose();
		idToImages.put(ImageIds.ARENA_VERTICAL, Arrays.asList(off_Image));
		
	}
	
	private void createArenaHorizontal() {
		Image arenaWall = idToImages.get(ImageIds.ARENA_WALL).get(0);
		BufferedImage off_Image = new BufferedImage(48 * 4, 48, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) off_Image.getGraphics();
		for(int i=0; i<4; i++) {
			g2.drawImage(arenaWall, 48 * i, 0, null);
		}
		g2.dispose();
		idToImages.put(ImageIds.ARENA_HORIZONTAL, Arrays.asList(off_Image));
		
	}

	private void createCompleteHouse() {
		Image houseImage = idToImages.get(ImageIds.HOUSE6).get(0);
		BufferedImage off_Image = new BufferedImage(houseImage.getWidth(null) * 2, houseImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) off_Image.getGraphics();
		g2.drawImage(houseImage, houseImage.getWidth(null), 0, null);
		g2.drawImage(houseImage, 0, 0, null);
		addStoneDoorToHouse(g2);
		addStoneWindowToHouse(g2);
		g2.dispose();
		idToImages.put(ImageIds.HOUSE6, Arrays.asList(off_Image));
	}
	
	private void createCompleteLibrary() {
		BufferedImage libraryImage = (BufferedImage) idToImages.get(ImageIds.LIBRARY).get(0);
		BufferedImage off_Image = new BufferedImage(libraryImage.getWidth(null) * 2, libraryImage.getHeight(null) + 16, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2 = (Graphics2D) off_Image.getGraphics();
		g2.drawImage(libraryImage, libraryImage.getWidth(null), 16, null);
		g2.drawImage(libraryImage, 0, 16, null);
		Image subImage = libraryImage.getSubimage(0, 0, libraryImage.getWidth(null), 16);
		g2.drawImage(subImage, 0, 0, null);
		g2.drawImage(subImage, subImage.getWidth(null), 0, null);
		g2.dispose();
		idToImages.put(ImageIds.LIBRARY, Arrays.asList(off_Image));
	}
    
    private void addDoorToHouse(ImageIds houseId) {
		addDoorToHouse(idToImages.get(houseId).get(0));
    }
    
    private void addDoorToHouse(Image houseImage) {
    	Graphics2D g2 = (Graphics2D) houseImage.getGraphics();
		Image doorImage =  idToImages.get(ImageIds.DOOR).get(0);
		g2.drawImage(doorImage, 16, 82, null);
		g2.dispose();
    }
    
    private void addStoneWindowToHouse(Graphics2D g2) {
		Image windowImage =  idToImages.get(ImageIds.STONE_WINDOW).get(0);
		g2.drawImage(windowImage, 82, 62, null);
    }
    
    private void addStoneDoorToHouse(Graphics2D g2) {
		Image doorImage =  idToImages.get(ImageIds.STONE_DOOR).get(0);
		g2.drawImage(doorImage, 16, 82, null);
    }
    
    private void addStoneDoorToInn(Graphics2D g2) {
		Image doorImage =  idToImages.get(ImageIds.STONE_DOOR).get(0);
		g2.drawImage(doorImage, 16, 128, null);
    }
    
    private void addCharacter(ImageIds imageId, Sprites sprites, int x, int y, int width, int height) {
    	if (idToImages.containsKey(imageId)) {
    		throw new IllegalStateException("Id " + imageId + " exists in map " + idToImages);
    	}
    	
    	List<Image> images = new ArrayList<>();
    	images.add(sprites.getSubImage(x, y, width, height));
    	images.add(sprites.getSubImage(x, y + 1, width, height));
    	images.add(sprites.getSubImage(x, y + 2, width, height));
    	images.add(sprites.getSubImage(x, y + 3, width, height));
    	
    	images.add(sprites.getSubImage(x + 1, y, width, height));
    	images.add(sprites.getSubImage(x + 1, y + 1, width, height));
    	images.add(sprites.getSubImage(x + 1, y + 2, width, height));
    	images.add(sprites.getSubImage(x + 1, y + 3, width, height));
    	
    	images.add(sprites.getSubImage(x + 2, y, width, height));
    	images.add(sprites.getSubImage(x + 2, y + 1, width, height));
    	images.add(sprites.getSubImage(x + 2, y + 2, width, height));
    	images.add(sprites.getSubImage(x + 2, y + 3, width, height));
    	
    	idToImages.put(imageId, images);
    	characterImageIds.add(imageId);
	}
    
    private void addMagic(ImageIds imageId, Sprites sprites, int width, int height) {
    	if (idToImages.containsKey(imageId)) {
    		throw new IllegalStateException("Id " + imageId + " exists in map " + idToImages);
    	}
    	List<Image> images = new ArrayList<>();
    	
    	for(int j=0; j<height; j++) {
    		for(int i=0; i<width; i++) {
    			images.add(sprites.getSubImage(i, j, 1, 1));
    		}
    	}
    	
    	idToImages.put(imageId, images);
    	characterImageIds.add(imageId);
	}

	private void add(ImageIds id, Image image) {
    	if (idToImages.containsKey(id)) {
    		throw new IllegalStateException("Id " + id + " exists in map " + idToImages);
    	}
    	
    	idToImages.put(id, Arrays.asList(image));
    }
	
    public List<ImageIds> getCharacterImageIds() {
		return Collections.unmodifiableList(characterImageIds);
	}

	private static Sprites readOrcSoldier() throws IOException {
		return readImages("orcsoldier_a.png", 32, 32, 4, 3);
	}
    
    private static Sprites readMonsters() throws IOException {
		return readImages("charchip01.png", 32, 32, 8, 12);
	}
    
    private static Sprites readSprites() throws IOException {
		return readImages("vx_chara01_a.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSpritesB() throws IOException {
		return readImages("vx_chara01_b.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites2A() throws IOException {
		return readImages("vx_chara02_a.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites2B() throws IOException {
		return readImages("vx_chara02_b.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites2C() throws IOException {
		return readImages("vx_chara02_c.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites2D() throws IOException {
		return readImages("vx_chara02_d.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites3A() throws IOException {
		return readImages("vx_chara03_a.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites3B() throws IOException {
		return readImages("vx_chara03_b.png", 32, 48, 8, 12);
	}

    private static Sprites readSprites3C() throws IOException {
		return readImages("vx_chara03_c.png", 32, 48, 8, 12);
	}

    private static Sprites readSprites3D() throws IOException {
		return readImages("vx_chara03_d.png", 32, 48, 8, 12);
	}

    private static Sprites readSprites3E() throws IOException {
		return readImages("vx_chara03_e.png", 32, 48, 8, 12);
	}

    private static Sprites readSprites3F() throws IOException {
		return readImages("vx_chara03_f.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites3G() throws IOException {
		return readImages("vx_chara03_g.png", 32, 48, 8, 12);
	}
    
	private static Sprites readObjects() throws IOException {
		return readImages("tileb.png", 32, 32, 16, 16);
	}

	private static Sprites readPirates() throws IOException {
		return readImages("pirate-themed-tilee.png", 32, 32, 16, 16);
	}
	
	private static Sprites readHouses() throws IOException {
		return readImages("tilea3.png", 32, 32, 8, 16);
	}

	private static Sprites readTileA2() throws IOException {
		return readImages("tilea2.png", 32, 32, 16, 16);
	}
	
	private static Sprites readTileB() throws IOException {
		return readImages("tileb.png", 32, 32, 16, 16);
	}
	
	private static Sprites readTileC() throws IOException {
		return readImages("tilec.png", 32, 32, 16, 16);
	}
	private static Sprites readTileCprison() throws IOException {
		return readImages("tilec_prison.png", 48, 48, 3, 3);
	}
	
	private static Sprites readTileE() throws IOException {
		return readImages("tilee.png", 32, 32, 16, 16);
	}
	
	private static Sprites readStatues() throws IOException {
		return readImages("statues.png", 32, 32, 16, 16);
	}
	
    private static Sprites readRat() throws IOException {
		return readImages("rat_c.png", 32, 32, 4, 3);
	}
    
    private static Sprites readSlime() throws IOException {
		return readImages("slimeking_f.png", 32, 32, 4, 3);
	}
    
    private static Sprites readSprites420() throws IOException {
		return readImages("420icons.png", 34, 34, 14, 24);
	}
	
	private static Sprites readSpritesSpider() throws IOException {
		return readImages("SpiderSpriteB2.png", 40, 35, 3, 4);
	}
	
	private static Sprites readSpritesStone() throws IOException {
		return readImages("stone-b1.png", 32, 32, 12, 8);
	}
	
	private static Sprites readSpritesTora02() throws IOException {
		return readImages("tora_vx_02.png", 32, 32, 16, 16);
	}
	
	private static Sprites readSpritesFish() throws IOException {
		return readImages("fishcrgarpie.png", 32, 32, 4, 3);
	}
	
	private static Sprites readSpritesCow() throws IOException {
		return readImages("mackcowrecolors.png", 64, 48, 9, 4);
	}
	
	private static Sprites readSpritesForge() throws IOException {
		return readImages("forge.png", 64, 91, 1, 1);
	}
	
	private static Sprites readTerrainTransitions() throws IOException {
		return readImages("terrain_transitions.png", 48, 48, 4, 2);
	}
	
	private static Sprites readSpritesVampire() throws IOException {
		return readImages("vampire_a.png", 48, 48, 4, 3);
	}
	
	private static Sprites readSpritesClothingShop() throws IOException {
		return readImages("clothing_shop.png", 64, 64, 3, 5);
	}
	
	private static Sprites readSpritesWeavery() throws IOException {
		return readImages("weavery.png", 192, 144, 1, 1);
	}
	
	private static Sprites readSpritesBrewery() throws IOException {
		return readImages("brewery.png", 64, 65, 3, 2);
	}
	
	private static Sprites readSpritesPapermill() throws IOException {
		return readImages("papermill.png", 192, 144, 1, 1);
	}
	
	private static Sprites readSpritesSmallTree() throws IOException {
		return readImages("small_tree.png", 96, 96, 1, 1);
	}
	
	private static Sprites readSpritesSmallBorealTree() throws IOException {
		return readImages("small_boreal_tree.png", 96, 96, 1, 1);
	}
	
	private static Sprites readSpritesVineWithGrapes() throws IOException {
		return readImages("vine_with_grapes.png", 48, 96, 1, 1);
	}
	
	private static Sprites readSpritesVine() throws IOException {
		return readImages("vine.png", 48, 96, 1, 1);
	}
	
	private static Sprites readSpritesApothecary() throws IOException {
		return readImages("apothecary.png", 192, 144, 1, 1);
	}
	
	private static Sprites readSpritesWorkbench() throws IOException {
		return readImages("workbench.png", 192, 144, 1, 1);
	}
	
	private static Sprites readSpritesMagic1() throws IOException {
		return readImages("magic_001.png", 144, 144, 1, 1);
	}
	
	private static Sprites readSpritesFire1() throws IOException {
		return readImages("fire_001.png", 48, 48, 1, 1);
	}
	
	private static Sprites readSpritesIce1() throws IOException {
		return readImages("ice_001.png", 48, 48, 1, 1);
	}
	
	private static Sprites readSpritesThunder1() throws IOException {
		return readImages("thunder_001.png", 48, 48, 1, 1);
	}
	
	private static Sprites readSpritesDarkness1() throws IOException {
		return readImages("darkness_001.png", 48, 48, 1, 1);
	}
	
	private static Sprites readSpritesHeal1() throws IOException {
		return readImages("heal_001.png", 48, 48, 1, 1);
	}
	
    private static Sprites readSprites01() throws IOException {
		return readImages("remakertp01.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSpritesAktor1() throws IOException {
		return readImages("Actor1_zps2890a1cd.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSpritesAktor3() throws IOException {
		return readImages("Actor3_zpsb3524fc2.png", 32, 48, 8, 12);
	}
	
	private static Sprites readImages(String imageFilename, int width, int height, int rows, int cols) throws IOException {
		BufferedImage bigImg = ImageIO.read(ImageInfoReader.class.getResource("/" + imageFilename));
		return new Sprites(bigImg, width, height);
	}
	
   public Image getImage(ImageIds id, LookDirection lookDirection) {
	   return getImage(id, lookDirection, 0);
   }
   
   public Image getImage(ImageIds id, LookDirection lookDirection, int moveIndex) {
	   List<Image> images = idToImages.get(id);
	   
	   if (images == null) {
		   throw new IllegalStateException("No image found for imageId " + id);
	   }
	   
	   int index;
	   if ((lookDirection != null) && (images.size() > 1)) {
		   index = lookDirection.ordinal();
		   index += (moveIndex * 4);
	   } else {
		   index = 0;
	   }
	   return images.get(index);
   }
   
   public Image getImage(ImageIds id, int index) {
	   List<Image> images = idToImages.get(id);
	   
	   if (images == null) {
		   throw new IllegalStateException("No image found for imageId " + id);
	   }
	   
	   return images.get(index);
   }
   
   private static class Sprites {
	   private BufferedImage bufferedImage;
	   private int imageWidth;
	   private int imageHeight;
	   
	   public Sprites(BufferedImage bufferedImage, int imageWidth, int imageHeight) {
		super();
		this.bufferedImage = bufferedImage;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	   }

	public Image getSubImage(int x, int y, int width, int height) {
		return bufferedImage.getSubimage(
			        	x * imageWidth,
			            y * imageHeight,
			            width*imageWidth,
			            height*imageHeight
			        );
		}
   }
	   
}
