package org.worldgrower.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.ItemType;
import org.worldgrower.generator.PlantGenerator;

public class TooltipImages {

	private final List<String> existingNames = new ArrayList<>();
	
	public TooltipImages() {
		super();
		for (WorldObject plant : PlantGenerator.getAllPlants()) {
			existingNames.add(plant.getProperty(Constants.NAME));
		}
		
		for (Item item : Item.getItems(ItemType.ARMOR, ItemType.WEAPON)) {
			existingNames.add(item.getDescription());
		}
	}

	public String substituteImages(String tooltip, String description, ImageIds imageId, Function<ImageIds, String> mapImageFunction) {
		String patternString = "\\b(" + description + ")\\b";
		Pattern pattern = Pattern.compile(patternString);
		String changedTooltip = tooltip;
		Matcher matcher = pattern.matcher(changedTooltip);
		
		while (matcher.find()) {
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			if (!startIndexmatchesExisting(changedTooltip, startIndex)) {
				String prefix = changedTooltip.substring(0, startIndex);
				String replacedValue = mapImageFunction.apply(imageId);
				String suffix = changedTooltip.substring(endIndex, changedTooltip.length());
				changedTooltip = prefix + replacedValue + suffix;
				matcher = pattern.matcher(changedTooltip);
			}
		}
		return changedTooltip;
	}

	private boolean startIndexmatchesExisting(String changedTooltip, int startIndex) {
		for (String existingName : existingNames) {
			if (startIndex == changedTooltip.indexOf(existingName)) {
				return true;
			}
		}
		return false;
	}
}
