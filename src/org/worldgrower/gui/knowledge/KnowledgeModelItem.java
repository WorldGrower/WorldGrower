package org.worldgrower.gui.knowledge;

import org.worldgrower.gui.ImageIds;

public class KnowledgeModelItem {
	private final String knowledgeDescription;
	private final ImageIds imageId;
	private Boolean selected;
	
	public KnowledgeModelItem(String knowledgeDescription, ImageIds imageId, Boolean selected) {
		super();
		this.knowledgeDescription = knowledgeDescription;
		this.imageId = imageId;
		this.selected = selected;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public String getKnowledgeDescription() {
		return knowledgeDescription;
	}
	public ImageIds getImageId() {
		return imageId;
	}
	
	
}
