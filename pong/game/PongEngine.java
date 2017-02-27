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

	static Player leftPlayer;
	static Player rightPlayer;
	private static Ball ball;
	public static Scene gameScene;
	private static ScoreBoard scoreBoard = new ScoreBoard();
	private static Group components = new Group();
	private static Pane background;
	private static final double HEIGHT = 800;
	public static final double WIDTH = 1000;

	public static double getHeight() {
		return HEIGHT;
	}

	public static double getWidth() {
		return WIDTH;
	}

	public static void run(int players) {
		createPlayers();
		createBall();
		createBackround();
//		createGroup();
		buildScene();
		showScene();
		PongKeyPressHandler keyHandler = new PongKeyPressHandler();
	}

	private static void createBall() {
		ball = new Ball(WIDTH / 2, HEIGHT / 2, 20);
	}

	private static void createBackround() {
		background = new Pane();
		background.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		components.getChildren().add(leftPlayer.getPaddle());
		components.getChildren().add(rightPlayer.getPaddle());
		components.getChildren().add(ball.getBall());
		components.getChildren().add(scoreBoard.getScoreBoard());
		background.getChildren().add(components);
	}

	private static void buildScene() {
		gameScene = new Scene(background, WIDTH, HEIGHT);
	}

	private static void showScene() {
		ArcadeView.setScene(gameScene, "Pong");
	}

	private static void createPlayers() {
		leftPlayer = new Player(20, HEIGHT / 2, 20, 100);
		rightPlayer = new Player(WIDTH - 40, HEIGHT / 2, 20, 100);
	}

	private static void createGroup() {
		components = new Group();
		components.getChildren().add(leftPlayer.getPaddle());
	}
}