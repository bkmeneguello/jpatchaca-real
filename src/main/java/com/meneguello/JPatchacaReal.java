package com.meneguello;

import java.awt.SplashScreen;
import java.io.IOException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JPatchacaReal extends Application {
	
	private static final Logger logger = LoggerFactory.getLogger(JPatchacaReal.class);
	
	private static JPatchacaReal instance;

	private final ResourceBundle bundle = ResourceBundle.getBundle("messages");

	public static void main(String[] args) {
		logger.info("Welcome");
		launch(args);
	}
	
	public static JPatchacaReal getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Application not started!");
		}
		
		return instance;
	}
	
	@Override
	public void init() throws Exception {
		logger.info("Application initialization");
		instance = this;
	}
	
	@Override
	public void start(Stage stage) {
		logger.info("Application starting");
		try {
			stage.setTitle(bundle.getString("app.title"));
			
			final Scene scene = loadScene("/main.fxml");
			stage.setScene(scene);
			stage.show();
			
			SplashScreen.getSplashScreen().close();
			logger.info("Application started");
		} catch(IOException e) {
			logger.error("Application starting failure", e);
		}
	}

	private Scene loadScene(final String resource) throws IOException {
		return FXMLLoader.load(getClass().getResource(resource), bundle, new JavaFXBuilderFactory(), new ControllerFactory());
	}

}
