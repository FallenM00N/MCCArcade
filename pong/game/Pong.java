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
	public static Scene pauseScene;
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@FXML
	@Override
	public void resume() {
		PongEngine.getKeyHandler().resume();
		showScene(PongEngine.gameScene, "Pong");
		PongEngine.animateBall();
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
		pauseScene = createPauseScene();
		playerSelection = createPlayerSelection();
		titleScreen();
	}

	private Scene createPlayerSelection() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("PlayerSelection.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		if(e.getSource().toString().contains("one")) {
			PongEngine.run(1);
			System.out.println("1");
		}
		else {
			PongEngine.run(2);
		}
	}
	

	@FXML
	public void playerSelection(ActionEvent e){
		showScene(playerSelection, "Player Select - Pong");
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
	
	public Scene createPauseScene() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("PauseScreen.fxml"));
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
	@FXML
	private Button onePlayer;
	@FXML
	private Button twoPlayers;

	public static void showPauseScreen() {
		showScene(pauseScene, "Paused - Pong");
		PongEngine.closeAnimation();
	}
	

}
