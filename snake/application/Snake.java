package application;

import java.io.IOException;

import application.MainMenu;
import game.PongEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import models.Game;

public class Snake extends Game{
	public static Scene titleScene;
	public static Scene playerSelection;
	public static Scene infoScene;
	public static Scene overScene;
	public static Scene pauseScene;
	
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
		showScene(titleScene, "Snake");
	}

	@FXML
	@Override
	public void play() {
		titleScene = createTitleScene();
		infoScene = createInfoScene();
		overScene = createOverScene();
		pauseScene = createPauseScene();
		titleScreen();
	}


	private Scene createInfoScene() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("SnakeHelp.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Scene createTitleScene() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("SnakeTitleScreen.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Scene createOverScene(){
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("SnakeOverScreen.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Scene createPauseScene(){
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("SnakePauseScreen.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void startGame(ActionEvent e) throws Exception {
		SnakeEngine.run();
	}
	
	@FXML
	public void playSnake(ActionEvent e) throws Exception{
		startGame(e);
	}
	
	@FXML
	public void infoScreen(){
		showScene(infoScene, "Snake - Help");
	}
	
	@FXML
	@Override
	public void gameOver() {
		showScene(overScene, "Snake - Game Over!");
		
	}
	@FXML
	@Override
	public void pause() {
		showScene(pauseScene, "Snake - Paused");
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
	private Button returnToTitleButton;
	@FXML
	private Button pauseButton;
	@FXML
	private Button resumeButton;

}
