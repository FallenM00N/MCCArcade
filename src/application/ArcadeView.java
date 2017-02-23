package application;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ArcadeView {
	private static Stage primaryStage = new Stage();
	
	public static void setScene(Scene scene, String title){
		primaryStage.setScene(scene);
		primaryStage.setTitle(title);
	}
	
	public static void setScene(Scene scene){
		primaryStage.setScene(scene);
	}
	
	public static void showStage(){
		primaryStage.show();
	}
}
