package game;

import application.ArcadeView;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.Ball;
import models.Player;
import models.ScoreBoard;

public class PongEngine {

	private static Player leftPlayer;
	private static Player rightPlayer;
	private static Ball ball;
	public static Scene gameScene;
	public static ScoreBoard scoreBoard;
	public static Group components;
	public static Pane background;

	public static void run(int players) {
		createPlayers();
		createBall();
		createBackround();
		createGroup();
		buildScene();
		showScene();
	}

	private static void createBall() {
		
	}

	private static void createBackround() {
		background = new Pane();
		background.setBackground(new Background(
				  new BackgroundFill(
				    Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY
						  )
				  )
			);
	}

	private static void showScene() {
		ArcadeView.setScene(gameScene, "Pong");
	}

	private static void createPlayers() {
		leftPlayer = new Player(10, 250, 20, 100);
		rightPlayer = new Player(770, 250, 20, 100);
	}

	private static void createGroup() {
		components = new Group();
		components.getChildren().add(leftPlayer.getPaddle());
		components.getChildren().add(rightPlayer.getPaddle());
		background.getChildren().add(components);
	}

	private static void buildScene() {
		gameScene = new Scene(background, 800, 600);
	}
}
