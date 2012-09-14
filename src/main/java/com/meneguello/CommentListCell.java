package com.meneguello;

import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;

import com.meneguello.jira.Comment;

class CommentListCell extends ListCell<Comment> {
	
	@Override
	public void updateItem(Comment comment, boolean empty) {
		super.updateItem(comment, empty);
		if (comment != null) {
			VBox graphic = VBoxBuilder.create()
					.children(LabelBuilder.create()
							.text(comment.getAuthor().getFullname())
							.build(),
							LabelBuilder.create()
							.text(comment.getBody())
							.build())
					.build();
			setGraphic(graphic);
		}
	}
	
}