package game;

import java.io.IOException;

import application.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import models.Game;

public class Pong extends Game{
	
	private static Scene titleScene;
	private static Scene playerSelection;
	private static Scene infoScene;
	private static Scene pauseScene;
	private static Scene player1Scene;
	private static Scene player2Scene;
	private static int players;
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@FXML
	@Override
	public void resume() {
		PongEngine.getKeyHandler().resume();
		showScene(PongEngine.gameScene, "Pong");
		PongEngine.animateGame(players);
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
		System.gc();
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
		player1Scene = createPlayer1Scene();
		player2Scene = createPlayer2Scene();
		titleScreen();
	}

	public static void player1Wins() {
		showScene(player1Scene, "Player 1 Wins!");
	}
	public static void player2Wins() {
		showScene(player2Scene, "Player 2 Wins!");
	}
	
	private Scene createPlayerSelection() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("PongPlayerSelection.fxml"));
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
			BorderPane root = FXMLLoader.load(getClass().getResource("PongTitleScreen.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Scene createPlayer1Scene() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("player1win.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Scene createPlayer2Scene() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("player2win.fxml"));
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
			players = 1;
		}
		else {
			players = 2;
		}
		PongEngine.run(players);
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
			AnchorPane root = FXMLLoader.load(getClass().getResource("PongHelp.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Scene createPauseScene() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("PongPauseScreen.fxml"));
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
	@FXML
	private Button titleButton;
	
	public static void showPauseScreen() {
		showScene(pauseScene, "Paused - Pong");
		PongEngine.closeAnimation();
	}
	

}
