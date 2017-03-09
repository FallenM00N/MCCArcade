package game;

import application.MainMenu;
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
import javafx.scene.layout.VBox;
import models.Game;

public class Neublasters extends Game {

	private static Scene titleScreen;
	private static Scene infoScene;
	private static Scene overScene;
	private static Scene pauseScene;
	@FXML
	private Button playButton;
	@FXML
	private Button infoButton;
	@FXML
	private Button menuButton;
	@FXML
	private Button quitButton;
	@FXML
	private Label title;
	public static Label l;
	@Override
	public void play() {
		titleScreen = createScene("NeublastersTitleScreen.fxml");
		infoScene = createScene("NeublastersHelp.fxml");
		overScene = createScene("NeublastersOver.fxml");
		pauseScene = createScene("NeublastersPause.fxml");
		l = (Label) findNode("finalScoreLabel", overScene.getRoot().getChildrenUnmodifiable());
		showScene(titleScreen, "Neublasters");
	}

	@FXML
	public void start(ActionEvent event) {
		NeublastersEngine.run();
	}

	@FXML
	@Override
	public void resume() {
		showScene(NeublastersEngine.getGameScene(), "Neublasters");
		NeublastersEngine.getTimer().start();
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

	@Override
	public void titleScreen() {
		showScene(titleScreen);
	}

	public void infoScreen() {
		showScene(infoScene);
	}

	public void overScreen() {
		showScene(overScene);
	}

	public void pauseScreen() {
		showScene(pauseScene);
	}

	public static Scene getOverScene() {
		return overScene;
	}

	public static void setOverScene(Scene overScene) {
		Neublasters.overScene = overScene;
	}

	public static Scene getPauseScene() {
		return pauseScene;
	}

	public static void setPauseScene(Scene pauseScene) {
		Neublasters.pauseScene = pauseScene;
	}

	public Scene createScene(String url) {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource(url));
			Scene scene = new Scene(root);
			return scene;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
			if (n.getId().equals("scoreLabel")) {
				return n;
			}
		}
		return n;
	}

	@Override
	public void gameOver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public Scene createTitleScene() {
		// TODO Auto-generated method stub
		return null;
	}

}
