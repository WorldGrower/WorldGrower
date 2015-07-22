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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.worldgrower.attribute.LookDirection;

public class ImageInfoReader {

	private final Map<ImageIds, List<Image>> idToImages = new HashMap<>();
	
    public ImageInfoReader() throws IOException {
    	Sprites sprites = readSprites();
    	Sprites spritesb = readSpritesB();
    	Sprites sprites2a = readSprites2A();
    	Sprites sprites2b = readSprites2B();
    	Sprites sprites2c = readSprites2C();
    	Sprites sprites2d = readSprites2D();
    	Sprites sprites3a = readSprites3A();
    	Sprites sprites3b = readSprites3B();
    	Sprites objects = readObjects();
    	Sprites monsters = readMonsters();
    	Sprites orcSoldier = readOrcSoldier();
    	Sprites pirates = readPirates();
    	Sprites houses = readHouses();
    	Sprites tileB = readTileB();
    	Sprites tileE = readTileE();
    	Sprites statues = readStatues();
    	Sprites rat = readRat();
    	Sprites slime = readSlime();
    	Sprites sprites420 = readSprites420();
    	Sprites spider = readSpritesSpider();
    	Sprites stone = readSpritesStone();
    	
    	addCharacter(ImageIds.KNIGHT, sprites, 0, 0, 1, 1);
    	addCharacter(ImageIds.GUARD, sprites, 0, 4, 1, 1);
    	add(ImageIds.BUCKET, objects.getSubImage(10, 10, 1, 1));
        add(ImageIds.BUSH, objects.getSubImage(1, 4, 1, 1));
        add(ImageIds.GOBLIN, monsters.getSubImage(2, 4, 1, 1));
        add(ImageIds.TRUNK, objects.getSubImage(3, 5, 1, 1));
        add(ImageIds.TREE, objects.getSubImage(6, 4, 2, 2));
        add(ImageIds.SHACK, objects.getSubImage(5, 0, 2, 2));
        addCharacter(ImageIds.FEMALE_COMMONER, sprites, 3, 0, 1, 1);
        add(ImageIds.WELL, objects.getSubImage(0, 11, 2, 2));
        add(ImageIds.SKELETON, monsters.getSubImage(0, 0, 1, 1));
        add(ImageIds.ORC_SOLDIER, orcSoldier.getSubImage(0, 0, 1, 1));
        add(ImageIds.STONE_RESOURCE, pirates.getSubImage(10, 5, 2, 2));
        add(ImageIds.ORE_RESOURCE, pirates.getSubImage(14, 5, 2, 2));
        add(ImageIds.HOUSE, houses.getSubImage(0, 0, 2, 4));
        add(ImageIds.SMITH, tileE.getSubImage(2, 8, 1, 2));
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
        add(ImageIds.GRAPE_VINE, objects.getSubImage(2, 6, 1, 2));
        
        add(ImageIds.IRON_HELMET, sprites420.getSubImage(11, 13, 1, 1));
        add(ImageIds.IRON_GAUNTLETS, sprites420.getSubImage(12, 14, 1, 1));
        add(ImageIds.IRON_BOOTS, sprites420.getSubImage(4, 14, 1, 1));
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
        add(ImageIds.PAPER_MILL, tileB.getSubImage(13, 12, 1, 1));
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
    	
    	idToImages.put(imageId, images);
		
	}

	private void add(ImageIds id, Image image) {
    	if (idToImages.containsKey(id)) {
    		throw new IllegalStateException("Id " + id + " exists in map " + idToImages);
    	}
    	
    	idToImages.put(id, Arrays.asList(image));
    }
	
    private static Sprites readOrcSoldier() throws IOException {
		return readImages("resources/orcsoldier_a.png", 32, 32, 4, 3);
	}
    
    private static Sprites readMonsters() throws IOException {
		return readImages("resources/charchip01.png", 32, 32, 8, 12);
	}
    
    private static Sprites readSprites() throws IOException {
		return readImages("resources/vx_chara01_a.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSpritesB() throws IOException {
		return readImages("resources/vx_chara01_b.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites2A() throws IOException {
		return readImages("resources/vx_chara02_a.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites2B() throws IOException {
		return readImages("resources/vx_chara02_b.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites2C() throws IOException {
		return readImages("resources/vx_chara02_c.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites2D() throws IOException {
		return readImages("resources/vx_chara02_d.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites3A() throws IOException {
		return readImages("resources/vx_chara03_a.png", 32, 48, 8, 12);
	}
    
    private static Sprites readSprites3B() throws IOException {
		return readImages("resources/vx_chara03_b.png", 32, 48, 8, 12);
	}
	
	private static Sprites readObjects() throws IOException {
		return readImages("resources/tileb.png", 32, 32, 16, 16);
	}

	private static Sprites readPirates() throws IOException {
		return readImages("resources/pirate-themed-tilee.png", 32, 32, 16, 16);
	}
	
	private static Sprites readHouses() throws IOException {
		return readImages("resources/tilea3.png", 32, 32, 8, 16);
	}

	private static Sprites readTileB() throws IOException {
		return readImages("resources/tileb.png", 32, 32, 16, 16);
	}
	
	private static Sprites readTileE() throws IOException {
		return readImages("resources/tilee.png", 32, 32, 16, 16);
	}
	
	private static Sprites readStatues() throws IOException {
		return readImages("resources/statues.png", 32, 32, 16, 16);
	}
	
    private static Sprites readRat() throws IOException {
		return readImages("resources/rat_c.png", 32, 32, 4, 3);
	}
    
    private static Sprites readSlime() throws IOException {
		return readImages("resources/slimeking_f.png", 32, 32, 4, 3);
	}
    
    private static Sprites readSprites420() throws IOException {
		return readImages("resources/420icons.png", 34, 34, 14, 24);
	}
	
	private static Sprites readSpritesSpider() throws IOException {
		return readImages("resources/SpiderSpriteB2.png", 40, 35, 3, 4);
	}
	
	private static Sprites readSpritesStone() throws IOException {
		return readImages("resources/stone-b1.png", 32, 32, 12, 8);
	}
	
	private static Sprites readImages(String imageFilename, int width, int height, int rows, int cols) throws IOException {
		BufferedImage bigImg = ImageIO.read(new File(imageFilename));
		return new Sprites(bigImg, width, height);
	}
	
   public Image getImage(ImageIds id, LookDirection lookDirection) {
	   List<Image> images = idToImages.get(id);
	   
	   final int index;
	   if ((lookDirection != null) && (images.size() > 1)) {
		   index = lookDirection.ordinal();
	   } else {
		   index = 0;
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
