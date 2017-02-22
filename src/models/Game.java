package models;

import application.Pausable;
import javafx.scene.Scene;

public abstract class Game implements Pausable{

	public abstract void play();
	
	public abstract Scene createTitleScene(); 
		
	public void showScene(Scene scene){
		
	}
	public abstract void gameOver();
	
}
