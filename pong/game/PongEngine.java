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
import javafx.scene.shape.Rectangle;
import models.Ball;
import models.Player;
import models.ScoreBoard;

public class PongEngine {

	static Player leftPlayer;
	static Player rightPlayer;
	private static Ball ball;
	public static Scene gameScene;
	private static ScoreBoard scoreBoard = new ScoreBoard();
	public static Group components = new Group();
	private static Pane background;
	private static final double HEIGHT = 800;
	public static final double WIDTH = 1000;
	private static Rectangle bottomWall = new Rectangle(0, HEIGHT, WIDTH, 1);
	private static Rectangle leftWall = new Rectangle(0, 0, 1, HEIGHT);
	private static Rectangle rightWall = new Rectangle(WIDTH, 0, 1, HEIGHT);

	public static double getHeight() {
		return HEIGHT;
	}

	public static double getWidth() {
		return WIDTH;
	}

	public static void run(int players) {
		createPlayers();
		createBall();
		createGroup();
		createBackround();
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
		components.getChildren().addAll(leftPlayer.getPaddle(),
										rightPlayer.getPaddle(),
										ball.getBall(),
										scoreBoard.getScoreBoard(),
										bottomWall,
										leftWall,
										rightWall
										);
		
	}

	public static Rectangle getBottomWall() {
		return bottomWall;
	}

	public static void setBottomWall(Rectangle bottomWall) {
		PongEngine.bottomWall = bottomWall;
	}

	public static Rectangle getLeftWall() {
		return leftWall;
	}

	public static void setLeftWall(Rectangle leftWall) {
		PongEngine.leftWall = leftWall;
	}

	public static Rectangle getRightWall() {
		return rightWall;
	}

	public static void setRightWall(Rectangle rightWall) {
		PongEngine.rightWall = rightWall;
	}
	
	public static Ball getBall() {
		return ball;
	}
	
	public static Player getLeftPlayer() {
		return leftPlayer;
	}
	
	public static Player getRightPlayer() {
		return rightPlayer;
	}

	public static void updateScore() {
		scoreBoard.updateScore(leftPlayer.getScore(), rightPlayer.getScore());
	}
}