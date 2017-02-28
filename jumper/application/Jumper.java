package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import models.Game;

public class Jumper extends Game {
	public static Scene titleScene;

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mainMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void titleScreen() {
		showScene(titleScene, "Jumper");
		
	}

	@Override
	public void play() {
		titleScene = createTitleScene();
		titleScreen();
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

}
