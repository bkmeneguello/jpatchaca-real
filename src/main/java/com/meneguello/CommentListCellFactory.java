package com.meneguello;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import com.meneguello.jira.Comment;

class CommentListCellFactory implements Callback<ListView<Comment>, ListCell<Comment>> {
	
	public static CommentListCellFactory create() {
		return new CommentListCellFactory();
	}
	
	private CommentListCellFactory() {
		
	}
	
	@Override
	public ListCell<Comment> call(ListView<Comment> listView) {
		return new CommentListCell();
	}
	
}