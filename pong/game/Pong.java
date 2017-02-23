package game;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import models.Game;

public class Pong extends Game{
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	@Override
	public void exit() {
		System.exit(0);
	}

	@FXML
	@Override
	public void mainMenu() {
		
	}

	@FXML
	@Override
	public void titleScreen() {
		// TODO Auto-generated method stub
		Scene title = createTitleScene();
		showScene(title);
	}

	@Override
	public void play() {
		titleScreen();
	}

	@Override
	public Scene createTitleScene() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("TitleScreen.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void gameOver() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	public void playerSelection() {
		Scene playerSelect = new Scene(null);
		showScene(playerSelect);
	}
	
	public void infoScreen() {
		Scene infoScreen = new Scene(null);
		showScene(infoScreen);
	}
	
	@FXML
	private Button playButton;
	@FXML
	private Button infoButton;
	@FXML
	private Button menuButton;
	@FXML
	private Button quitButton;
	

}
