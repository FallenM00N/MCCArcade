package game;

import java.io.IOException;

import application.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import models.Game;

public class Pong extends Game{
	
	public static Scene titleScene;
	public static Scene playerSelection;
	public static Scene infoScene;
	public static Scene overScene;
	
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
		showScene(MainMenu.getMenuScene(), "MCC Arcade");
	}

	@FXML
	@Override
	public void titleScreen() {
		showScene(titleScene, "Pong");
	}

	@Override
	public void play() {
		titleScene = createTitleScene();
		infoScene = createInfoScene();
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
	
	public void startGame(ActionEvent e) {
		PongEngine.run(2);
	}
	

	@FXML
	public void playerSelection(ActionEvent e) {
		startGame(e);
//		Scene playerSelect = new Scene(null);
//		showScene(playerSelect);
	}
	
	@FXML
	public void infoScreen() {
		showScene(infoScene, "Help");
	}
	
	public Scene createInfoScene() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("Help.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
