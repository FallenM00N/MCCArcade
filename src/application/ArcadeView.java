package application;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ArcadeView {
	private static Stage primaryStage = new Stage();
	
	public static void setScene(Scene scene, String title){
		primaryStage.setScene(scene);
		primaryStage.setTitle(title);
		primaryStage.centerOnScreen();
	}
	
	public static void setScene(Scene scene){
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
	}
	
	public static void showStage(){
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}
