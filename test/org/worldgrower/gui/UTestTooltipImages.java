package org.worldgrower.gui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestTooltipImages {

	@Test
	public void testSubstituteImages() {
		TooltipImages tooltipImages = new TooltipImages();
		assertEquals("mine [GOLD] gold here", tooltipImages.substituteImages("mine gold here", "gold", ImageIds.GOLD, i -> "[GOLD]"));
		assertEquals("a cotton plant provides [COTTON] cotton", tooltipImages.substituteImages("a cotton plant provides cotton", "cotton", ImageIds.COTTON, i -> "[COTTON]"));
		assertEquals("heal hit points", tooltipImages.substituteImages("heal hit points", "cotton", ImageIds.COTTON, i -> "[COTTON]"));
		assertEquals("filled soulgem", tooltipImages.substituteImages("filled soulgem", "soulgem", ImageIds.SOUL_GEM, i -> "[SOULGEM]"));
	}
}
