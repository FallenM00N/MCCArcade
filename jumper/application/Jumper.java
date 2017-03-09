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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import models.Game;
import models.JumperScore;

public class Jumper extends Game {
	public static Scene titleScene;
	public static Scene helpScene;
	public static Scene gameOverScene;
	public static Scene pauseScene;

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

	@Override
	public void titleScreen() {
		showScene(titleScene, "Jumper");

	}

	@FXML
	public void helpScreen() {
		showScene(helpScene, "Jumper - Help");
	}

	@FXML
	public void playJumper(ActionEvent e) {
		startGame(e);
	}

	public void startGame(ActionEvent e) {
		JumperEngine.run();
	}

	@FXML
	@Override
	public void play() {
		titleScene = createTitleScene();
		helpScene = createHelpScene();
		pauseScene = createPauseScene();
		gameOverScene = createGameOverScene();
		titleScreen();
	}

	private Scene createHelpScene() {
		try {
			Pane root = FXMLLoader.load(getClass().getResource("JumperHelpScene.fxml"));
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
			Pane root = FXMLLoader.load(getClass().getResource("JumperTitleScene.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Scene createPauseScene() {
		try {
			Pane root = FXMLLoader.load(getClass().getResource("JumperPauseScene.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@FXML
	public Scene createGameOverScene() {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("JumperGameOverScene.fxml"));


			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@FXML
	@Override
	public void gameOver() {
		JumperEngine.stopGame();
		System.out.println(JumperScore.getScore());

		Label l = (Label) findNode("finalScoreLabel", gameOverScene.getRoot().getChildrenUnmodifiable());
		l.setText("SCORE: " + JumperScore.getScore());
		showScene(gameOverScene, "Llama Run - Game Over!");

	}

	public Node findNode(String id, ObservableList<Node> ol) {
		Node n = null;
		for (int i = 0; i < ol.size(); i++) {
			n = ol.get(i);
			if (n instanceof Pane) {
				n = findNode("scoreLabel", ((Pane) n).getChildren());
			} else if (n.getId() == null) {
				continue;
			}
			if (n.getId().equals("finalScoreLabel")) {
				return n;
			}
		}
		return n;
	}

	@FXML
	@Override
	public void pause() {
		showScene(pauseScene, "Llama Run - Paused");
	}

	@FXML
	@Override
	public void resume() {
		JumperEngine.resumeJumper();
	}

	public void restartGame(ActionEvent e) throws Exception {
		JumperEngine.restartGame();
	}

	@FXML
	public void restartJumper(ActionEvent e) throws Exception {
		restartGame(e);
	}

	@FXML
	private Button menuButton;
	@FXML
	private Button quitButton;
	@FXML
	private Button helpButton;
	@FXML
	private Button returnToTitleButton;
	@FXML
	private Button playButton;
	@FXML
	private Button pauseButton;
	@FXML
	private Button resumeButton;
	@FXML
	private Button restartButton;
	@FXML
	private Label finalScoreLabel;
}
