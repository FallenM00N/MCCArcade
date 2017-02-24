package application;

import java.io.IOException;

import application.MainMenu;
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
		showScene(MainMenu.getMenuScene());
	}

	@FXML
	@Override
	public void titleScreen() {
		showScene(titleScene);
	}

	@FXML
	@Override
	public void play() {
		titleScene = createTitleScene();
		titleScreen();
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
	
	@FXML
	public void playSnake(){
		
	}
	
	@FXML
	public void infoScreen(){
		
	}
	
	@FXML
	@Override
	public void gameOver() {
		// TODO Auto-generated method stub
		
	}
	@FXML
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
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
