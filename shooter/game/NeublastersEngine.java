package game;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import models.Game;
import models.NeuBullet;
import models.NeuEnemy;
import models.SpaceShip;

public class NeublastersEngine {

	private static Scene gameScene;
	private static Canvas gameCanvas;
	private static AnimationTimer timer;
	private static SpaceShip player;
	private static double baseSpeed = 3;
	private static ArrayList<NeuEnemy> enemies = new ArrayList<>();
	private static ArrayList<NeuBullet> playerBullets = new ArrayList<>();
	private static double time = 0;
	private static double bulletCountdown = 0;

	public static void run() {
		gameScene = createBlankScene();
		player = new SpaceShip(0, 0, baseSpeed + 1);
		Game.showScene(gameScene, "Neublasters");
		createAnimationTimer();
	}

	private static void createAnimationTimer() {
		timer = new AnimationTimer() {
			ArrayList<String> pressedKeys = new ArrayList<>();
			int collisionTimer = 0;

			@Override
			public void handle(long now) {
				keyListen();
				generateBullets();
				handleCollisions();
				generateEnemies();
				updatePositions();
				animateScene();
				time++;
			}

			private void generateBullets() {
				if (bulletCountdown > 0) {
					bulletCountdown--;
				}
				if (bulletCountdown == 0 && pressedKeys.contains("fire")) {
					NeuBullet bullet = new NeuBullet(player.getX() + player.getImage().getWidth() - 10,
							player.getY() + player.getImage().getHeight() / 2, player.getSpeed() * 4);
					playerBullets.add(bullet);
					bulletCountdown = 10;
				}
			}

			private void generateEnemies() {
				// TODO Auto-generated method stub
				Random rand = new Random();
				if (time % 100 == 0) {
					enemies.add(new NeuEnemy(gameCanvas.getWidth() + 40,
							rand.nextDouble() * gameCanvas.getHeight() - 60, -baseSpeed));
				}
			}

			private void updatePositions() {
				if (pressedKeys.contains("up")) {
					player.move(0, -2.5);
				}
				if (pressedKeys.contains("down")) {
					player.move(0, 2.5);
				}
				if (pressedKeys.contains("left")) {
					player.move(-1, 0);
				}
				if (pressedKeys.contains("right")) {
					player.move(1.5, 0);
					player.setImage("neuBlasterThrust.png");
				} else {
					player.setImage("neuBlasterNoThrust.png");
				}
				for (int i = 0; i < enemies.size(); i++) {
					if (enemies.get(i).getDestroyFrame() > 15) {
						enemies.get(i).moveStraight();
						if (enemies.get(i).getX() < 0 - enemies.get(i).getImage().getWidth() - 5) {
							enemies.remove(i);
							i--;
						}
					}
				}
				for (int i = 0; i < playerBullets.size(); i++) {
					playerBullets.get(i).move();
					if (playerBullets.get(i).getX() > gameCanvas.getWidth()) {
						playerBullets.remove(i);
						i--;
					}
				}
			}

			private void animateScene() {
				gameCanvas.getGraphicsContext2D().clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
				gameCanvas.getGraphicsContext2D().drawImage(player.getImage(), player.getX(), player.getY());
				for (NeuEnemy enemy : enemies) {
					gameCanvas.getGraphicsContext2D().drawImage(enemy.getImage(), enemy.getX(), enemy.getY());
					if (enemy.isDestroyed() && enemy.getDestroyFrame() > 0) {
						System.out.println(enemy.getDestroyFrame());
						enemy.setDestroyFrame(enemy.getDestroyFrame() - 1);
					}
				}
				for (NeuBullet bullet : playerBullets) {
					gameCanvas.getGraphicsContext2D().setFill(Paint.valueOf("#fff"));
					gameCanvas.getGraphicsContext2D().fillRect(bullet.getX(), bullet.getY(), bullet.getWidth(),
							bullet.getHeight());
				}
			}

			private void handleCollisions() {
				for (int i = 0; i < enemies.size(); i++) {
					if (!enemies.get(i).isDestroyed() && enemies.get(i).collides(player)) {
						enemies.get(i).destroy();
					}
						if (enemies.get(i).isDestroyed() && enemies.get(i).getDestroyFrame() <= 0) {
						enemies.remove(i);
						i--;
					}
				}
				for (int i = 0; i < playerBullets.size(); i++) {
					for(NeuEnemy enemy : enemies) {
						if(enemy.collides(playerBullets.get(i))) {
							enemy.destroy();
							playerBullets.remove(i);
							i--;
							break;
						}
					}
				}
			}

			private void keyListen() {
				gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						KeyCode kc = event.getCode();
						if ((kc.equals(KeyCode.W) || kc.equals(KeyCode.UP)) && !pressedKeys.contains("up")) {
							pressedKeys.add("up");
							pressedKeys.remove("down");
						}
						if ((kc.equals(KeyCode.S) || kc.equals(KeyCode.DOWN)) && !pressedKeys.contains("down")) {
							pressedKeys.add("down");
							pressedKeys.remove("up");
						}
						if ((kc.equals(KeyCode.A) || kc.equals(KeyCode.LEFT)) && !pressedKeys.contains("left")) {
							pressedKeys.add("left");
							pressedKeys.remove("right");
						}
						if ((kc.equals(KeyCode.D) || kc.equals(KeyCode.RIGHT)) && !pressedKeys.contains("right")) {
							pressedKeys.add("right");
							pressedKeys.remove("left");
						}
						if (kc.equals(KeyCode.SPACE) && !pressedKeys.contains("fire")) {
							pressedKeys.add("fire");
						}
						if (kc.equals(KeyCode.SHIFT) && !pressedKeys.contains("missile")) {
							pressedKeys.add("missile");
						}
					}
				});

				gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						KeyCode kc = event.getCode();
						if (kc.equals(KeyCode.W) || kc.equals(KeyCode.UP)) {
							pressedKeys.remove("up");
						}
						if (kc.equals(KeyCode.S) || kc.equals(KeyCode.DOWN)) {
							pressedKeys.remove("down");
						}
						if (kc.equals(KeyCode.A) || kc.equals(KeyCode.LEFT)) {
							pressedKeys.remove("left");
						}
						if (kc.equals(KeyCode.D) || kc.equals(KeyCode.RIGHT)) {
							pressedKeys.remove("right");
						}
						if (kc.equals(KeyCode.SPACE)) {
							pressedKeys.remove("fire");
						}
						if (kc.equals(KeyCode.SHIFT)) {
							pressedKeys.remove("missile");
						}
					}
				});

			}
		};
		timer.start();
	}

	private static Canvas createCanvas(double width, double height) {
		Canvas canvas = new Canvas(width, height);
		return canvas;
	}

	private static Scene createBlankScene() {
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		double width = bounds.getWidth();
		double height = bounds.getHeight() - 20;
		Pane background = new Pane();
		gameCanvas = createCanvas(width, height);
		background.getChildren().add(gameCanvas);
		background.setBackground(
				new Background(new BackgroundFill(Paint.valueOf("#000"), CornerRadii.EMPTY, Insets.EMPTY)));
		Scene scene = new Scene(background, width, height);
		return scene;
	}

}
