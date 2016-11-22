package org.worldgrower.gui.knowledge;

import org.worldgrower.gui.ImageIds;

public class KnowledgeDescription {
	private final int id;
	private final String description;
	private final ImageIds imageId;
	
	public KnowledgeDescription(int id, String description, ImageIds imageId) {
		super();
		this.id = id;
		this.description = description;
		this.imageId = imageId;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public ImageIds getImageId() {
		return imageId;
	}
}
