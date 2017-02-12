package org.worldgrower.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
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
		StringBuilder changedTooltipBuilder = new StringBuilder();
		String remainingTooltip = tooltip;
		Matcher matcher = pattern.matcher(remainingTooltip);
		
		while (matcher.find()) {
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			if (!startIndexmatchesExisting(remainingTooltip, startIndex)
					&& !isMatchedByOverlappingTerm(description, remainingTooltip, startIndex)) {
				String prefix = remainingTooltip.substring(0, startIndex);
				String replacedValue = mapImageFunction.apply(imageId) + " " + description;
				String suffix = remainingTooltip.substring(endIndex, remainingTooltip.length());
				changedTooltipBuilder.append(prefix).append(replacedValue);
				remainingTooltip = suffix;
				matcher = pattern.matcher(remainingTooltip);
			}
		}
		if (remainingTooltip.length() > 0) {
			changedTooltipBuilder.append(remainingTooltip);
		}
		return changedTooltipBuilder.toString();
	}

	private boolean isMatchedByOverlappingTerm(String description, String remainingTooltip, int startIndex) {
		if (description.equals("soulgem")) {
			String overlappingTerm = "filled soulgem";
			int indexOfOverlappingTerm = remainingTooltip.indexOf(overlappingTerm);
			if (indexOfOverlappingTerm >= 0 && indexOfOverlappingTerm < startIndex && startIndex < indexOfOverlappingTerm + overlappingTerm.length()) {
				return true;
			}
		}
		return false;
	}

	private boolean startIndexmatchesExisting(String changedTooltip, int startIndex) {
		for (String existingName : existingNames) {
			if (startIndex == changedTooltip.indexOf(existingName)) {
				return true;
			}
		}
		if (changedTooltip.contains(Actions.MINT_GOLD_ACTION.getDescription())) {
			return true;
		}
		return false;
	}
}
