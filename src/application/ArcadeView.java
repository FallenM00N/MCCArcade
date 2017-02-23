package application;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ArcadeView {
	private static Stage primaryStage;
	
	public static void setScene(Scene scene){
		primaryStage.setScene(scene);
	
	}
	
	public static void showStage(){
		primaryStage.show();
	}
}
