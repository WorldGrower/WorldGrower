package org.worldgrower.gui.knowledge;

import org.worldgrower.gui.ImageIds;

public class KnowledgeModelItem {
	private final int id;
	private final String knowledgeDescription;
	private final ImageIds imageId;
	private Boolean selected;
	
	public KnowledgeModelItem(int id, String knowledgeDescription, ImageIds imageId, Boolean selected) {
		super();
		this.id = id;
		this.knowledgeDescription = knowledgeDescription;
		this.imageId = imageId;
		this.selected = selected;
	}
	public int getId() {
		return id;
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
