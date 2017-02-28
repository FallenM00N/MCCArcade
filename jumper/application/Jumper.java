package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import models.Game;

public class Jumper extends Game {
	public static Scene titleScene;
	public static Scene helpScene;
	public static Scene gameOverScene;

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
	public void helpScreen(){
		showScene(helpScene, "Jumper - Help");
	}
	
	@FXML 
	public void playJumper(ActionEvent e){
		startGame(e);
	}
	
	public void startGame(ActionEvent e){
		JumperEngine.run();
	}

	@FXML
	@Override
	public void play() {
		titleScene = createTitleScene();
		helpScene = createHelpScene();
		titleScreen();
	}

	private Scene createHelpScene() {
		try{
			Pane root = FXMLLoader.load(getClass().getResource("JumperHelpScene.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Scene createTitleScene() {
		try{
			Pane root = FXMLLoader.load(getClass().getResource("JumperTitleScene.fxml"));
			Scene scene = new Scene(root);
			return scene;
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
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
	public void resume() {
		// TODO Auto-generated method stub
		
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
}
