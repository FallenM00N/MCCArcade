package game;

import java.util.Random;

import application.ArcadeView;
import javafx.animation.AnimationTimer;
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
	private static final double HEIGHT = 750;
	public static final double WIDTH = 1000;
	private static Rectangle bottomWall = new Rectangle(0, HEIGHT, WIDTH, 1);
	private static Rectangle leftWall = new Rectangle(0, 0, 1, HEIGHT);
	private static Rectangle rightWall = new Rectangle(WIDTH, 0, 1, HEIGHT);
	private static PongKeyPressHandler keyHandler;
	private static AnimationTimer timer;
	private static double speed = 8;

	public static double getHeight() {
		return HEIGHT;
	}

	public static double getWidth() {
		return WIDTH;
	}

	public static PongKeyPressHandler getKeyHandler() {
		return keyHandler;
	}

	public static void run(int players) {
		createPlayers();
		updateScore();
		createBall();
		createGroup();
		createBackround();
		buildScene();
		showScene();
		animateGame(players);
		keyHandler = new PongKeyPressHandler();
	}

	public static void animateGame(int players) {
		double speed = 12;
		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				ball.getDirection();
				ball.move(speed);
				animatePaddles(players);
				if(checkPlayer1Score()) {
					Ball.reset();
					PongEngine.getLeftPlayer().incrementScore();
					PongEngine.updateScore();
				}
				if(checkPlayer2Score()) {
					Ball.reset();
					PongEngine.getRightPlayer().incrementScore();
					PongEngine.updateScore();
				}
				if(ball.getdX() == 0) {
					ball.countDown();
				}
			}

			private void animatePaddles(int players) {
				if(PongKeyPressHandler.pressedKeys.contains("p1up") && !PongKeyPressHandler.pressedKeys.contains("p1down")) {
					movePaddle1("up");
				}
				if(PongKeyPressHandler.pressedKeys.contains("p1down") && !PongKeyPressHandler.pressedKeys.contains("p1up")) {
					movePaddle1("down");
				}
				if(PongKeyPressHandler.pressedKeys.contains("p2up") && !PongKeyPressHandler.pressedKeys.contains("p2down") && players == 2) {
					movePaddle2("up");
				}
				if(PongKeyPressHandler.pressedKeys.contains("p2down") && !PongKeyPressHandler.pressedKeys.contains("p2up") && players == 2) {
					movePaddle2("down");
				}
				if(players == 1) {
					movePaddleAI();
				}
			}
		};
		timer.start();
	}
	
	private static void movePaddleAI() {
		double dY= 0;
		double target = ball.getBall().getLayoutY();
		if(target > rightPlayer.getPaddle().getLayoutY() + 10) {
			dY = 1;
		}
		else if(target < rightPlayer.getPaddle().getLayoutY() - 10) {
			dY = -1;
		}
		if(rightPlayer.getPaddle().getBoundsInParent().intersects(bottomWall.getBoundsInParent())) {
			if(dY > 0) {
				dY = 0;
			}
		}
		if(rightPlayer.getPaddle().getBoundsInParent().intersects(ScoreBoard.getWall().getBoundsInParent())) {
			if(dY < 0) {
				dY = 0;
			}
		}
		rightPlayer.getPaddle().setLayoutY(rightPlayer.getPaddle().getLayoutY() + (dY * speed * 0.8));
	}
	
	private static void movePaddle1(String dir) {
		Player leftPlayer = PongEngine.leftPlayer;
		if(dir.equals("up") && !leftPlayer.getPaddle().getBoundsInParent().intersects(ScoreBoard.getWall().getBoundsInParent())) {
			leftPlayer.move(-speed);
		}else if(dir.equals("down") && !leftPlayer.getPaddle().getBoundsInParent().intersects(PongEngine.getBottomWall().getBoundsInParent())) {
			leftPlayer.move(speed);
		}
	}
	
	private static void movePaddle2(String dir) {
		Player rightPlayer = PongEngine.rightPlayer;
		if(dir.equals("up") && !rightPlayer.getPaddle().getBoundsInParent().intersects(ScoreBoard.getWall().getBoundsInParent())) {
			rightPlayer.move(-speed);
		}else if(dir.equals("down") && !rightPlayer.getPaddle().getBoundsInParent().intersects(PongEngine.getBottomWall().getBoundsInParent())) {
			rightPlayer.move(speed);
		}
	}
	
	private static boolean checkPlayer2Score() {
		boolean hasScored = false;
		if(getBall().getBall().getBoundsInParent().intersects(PongEngine.getLeftWall().getBoundsInParent())) {
			hasScored = true;
		}
		return hasScored;
	}

	private static boolean checkPlayer1Score() {
		boolean hasScored = false;
		if(getBall().getBall().getBoundsInParent().intersects(PongEngine.getRightWall().getBoundsInParent())) {
			hasScored = true;
		}
		return hasScored;
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
		components.getChildren().addAll(leftPlayer.getPaddle(), rightPlayer.getPaddle(), ball.getBall(),
				scoreBoard.getScoreBoard(), bottomWall, leftWall, rightWall);

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
		if(leftPlayer.getScore() == 10) {
			player1Win();
		}
		if(rightPlayer.getScore() == 10) {
			player2Win();
		}
	}

	private static void player2Win() {
		keyHandler.close();
		timer.stop();
		Pong.player2Wins();
	}

	private static void player1Win() {
		keyHandler.close();
		timer.stop();
		Pong.player1Wins();
	}

	public static void closeAnimation() {
		timer.stop();
	}
}