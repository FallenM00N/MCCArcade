package game;


import application.ArcadeView;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

public class NeublastersEngine {
	
	private static Scene gameScene;
	
	
	

	public static void run() {
		gameScene = createBlankScene();
		Neublasters.showScene(gameScene, "Neublasters");
	}
	
	private static Scene createBlankScene() {
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		double width = bounds.getWidth();
		double height = bounds.getHeight() - 20;
		Pane background = new Pane();
		Scene scene = new Scene(background, width, height);
		return scene; 
	}

}
