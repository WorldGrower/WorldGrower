package org.worldgrower.gui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestTooltipImages {

	@Test
	public void testSubstituteImages() {
		TooltipImages tooltipImages = new TooltipImages();
		assertEquals("mine [GOLD] here", tooltipImages.substituteImages("mine gold here", "gold", ImageIds.GOLD, i -> "[GOLD]"));
		assertEquals("a cotton plant provides [COTTON]", tooltipImages.substituteImages("a cotton plant provides cotton", "cotton", ImageIds.COTTON, i -> "[COTTON]"));
	}
}
