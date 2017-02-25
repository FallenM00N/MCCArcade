package models;

import application.ArcadeView;
import interfaces.Pausable;
import javafx.scene.Scene;

public abstract class Game implements Pausable{

	public abstract void play();
	
	public abstract Scene createTitleScene(); 
		
	public void showScene(Scene scene, String title){
		ArcadeView.setScene(scene, title);
	}
	
	public void showScene(Scene scene){
		ArcadeView.setScene(scene);
	}
	
	public abstract void gameOver();
	
	public abstract void pause();
}
