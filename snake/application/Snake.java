package application;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import models.Game;
import models.Score;

public class Snake extends Game{
	public static Scene titleScene;
	public static Scene playerSelection;
	public static Scene infoScene;
	public static Scene overScene;
	public static Scene pauseScene;
	public static Score s = new Score();
	


	@FXML
	@Override
	public void exit() {

		System.exit(0);
	}

	@FXML
	@Override
	public void mainMenu() {
		SnakeEngine.stopGame();
		showScene(MainMenu.getMenuScene(), "MCC Arcade");
	}

	@FXML
	@Override
	public void titleScreen() {
		showScene(titleScene, "Snake");
	}
	
	@FXML
	public void returnToTitle(){
		SnakeEngine.stopGame();
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
			AnchorPane root = FXMLLoader.load(getClass().getResource("SnakeHelp.fxml"));
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
	
	public Node findNode(String id, ObservableList<Node> ol) {
		Node n = null;
		for (int i = 0; i < ol.size(); i++) {
			n = ol.get(i);
			if (n instanceof Pane) {
				n = findNode("finalScoreLabel", ((Pane) n).getChildren());
			} else if (n.getId() == null) {
				continue;
			}
			if (n.getId().equals("finalScoreLabel")) {
				return n;
			}
		}
		return n;
	}
	
	public void startGame(ActionEvent e) throws Exception {
		SnakeEngine.run();
	}
	
	@FXML
	public void playSnake(ActionEvent e) throws Exception{
		startGame(e);
	}
	
	public void restartGame(ActionEvent e) throws Exception{
		SnakeEngine.restartGame();
	}
	@FXML
	public void restartSnake(ActionEvent e) throws Exception{
		restartGame(e);
	}
	
	@FXML
	public void infoScreen(){
		showScene(infoScene, "Snake - Help");
	}
	
	@FXML
	@Override
	public void gameOver() {
		SnakeEngine.stopGame();
		Label l = (Label) findNode("finalScoreLabel",overScene.getRoot().getChildrenUnmodifiable());
		l.setText("SCORE: " + s.getScore());
		showScene(overScene, "Snake - Game Over!");
		
	}
	@FXML
	@Override
	public void pause() {
		showScene(pauseScene, "Snake - Paused");
	}
	
	@FXML
	@Override
	public void resume() {
		SnakeEngine.resumeSnake();
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
	@FXML
	private Button restartButton;
	@FXML
	private Label finalScorelabel;
}
