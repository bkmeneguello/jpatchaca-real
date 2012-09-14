package com.meneguello;

import java.util.BitSet;

import javafx.scene.Scene;
import javafx.util.Builder;

public class DialogStageBuilder implements Builder<DialogStage> {
	
	private static final int TITLE_ATTR = 0;

	private static final int SCENE_ATTR = 0;

	private String title;
	
	private BitSet bitSet;

	private Scene scene;

	public static DialogStageBuilder create() {
		return new DialogStageBuilder();
	}
	
	private DialogStageBuilder() {
		
	}

	public DialogStageBuilder title(String title) {
		this.title = title;
		bitSet.set(TITLE_ATTR);
		return this;
	}

	public DialogStageBuilder scene(Scene scene) {
		this.scene = scene;
		bitSet.set(SCENE_ATTR);
		return this;
	}

	@Override
	public DialogStage build() {
		final DialogStage dialogStage = new DialogStage();
		if (bitSet.get(TITLE_ATTR)) {
			dialogStage.setTitle(title);
		}
		if (bitSet.get(SCENE_ATTR)) {
			dialogStage.setScene(scene);
		}
		return dialogStage;
	}

}
