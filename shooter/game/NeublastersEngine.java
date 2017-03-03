package game;

import java.awt.GraphicsEnvironment;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class NeublastersEngine {
	
	private static Scene gameScene;
	
	
	

	public static void run() {
		Neublasters.showScene(createBlankScene(), "Neublasters");
	}
	
	private static Scene createBlankScene() {
		java.awt.Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		double width = bounds.getWidth();
		double height = bounds.getHeight() - 500;
		Pane background = new Pane();
		Scene scene = new Scene(background, width, height);
		return scene;
	}

}
