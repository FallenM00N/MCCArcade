package game;

import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import application.Main;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
	private static double baseSpeed = 4;
	private static ArrayList<NeuEnemy> enemies = new ArrayList<>();
	private static ArrayList<NeuBullet> playerBullets = new ArrayList<>();
	private static ArrayList<NeuBullet> enemyBullets = new ArrayList<>();
	private static double time = 0;
	private static double bulletCountdown = 0;
	private static double score = 0;
	private static Label scoreBoard = new Label("Score: " + getScoreString());
	private static double comboTimer = 0;
	private static double comboCount = 0;
	private static Rectangle[] walls;
	private static int spawnRate = 100;
	private static int spawnTime = 0;

	public static void run() {
		spawnRate = 100;
		spawnTime = 0;
		score = 0;
		comboCount = 0;
		comboTimer = 0;
		enemies = new ArrayList<>();
		playerBullets = new ArrayList<>();
		enemyBullets = new ArrayList<>();
		gameScene = createBlankScene();
		player = new SpaceShip(0, 30, baseSpeed + 1);
		Game.showScene(gameScene, "Neublasters");
		createAnimationTimer();
	}

	private static void createAnimationTimer() {
		timer = new AnimationTimer() {
			ArrayList<String> pressedKeys = new ArrayList<>();
			boolean oddFrame;

			@Override
			public void handle(long now) {
				keyListen();
				if (oddFrame)
					generateBullets();
				handleCollisions();
				generateEnemies();
				updatePositions();
				animateScene();
				updateScore();
				time++;
				if (comboTimer > 0) {
					comboTimer--;
				}
				oddFrame = !oddFrame;
			}

			private void updateScore() {
				score += 2;
					scoreBoard.setText("Score: " + getScoreString());
			}

			private void generateBullets() {
				if (bulletCountdown > 0) {
					bulletCountdown--;
				}
				if (bulletCountdown == 0 && pressedKeys.contains("fire")) {
					NeuBullet bullet = new NeuBullet(player.getX() + player.getImage().getWidth() - 10,
							player.getY() + player.getImage().getHeight() / 2 - 4, player.getSpeed() * 4);
					playerBullets.add(bullet);
					playSound("shot");
					bulletCountdown = 5;
				}
				Random rand = new Random();
				for (NeuEnemy enemy : enemies) {
					if (!enemy.isDestroyed()) {
						if (enemy.getBulletCountDown() < 0) {
							enemy.fire();
							enemy.setBulletCountDown(rand.nextInt(30) + 15);
						}
						enemy.tick();
					}
				}
			}

			private void generateEnemies() {
				Random rand = new Random();
				if (spawnTime >= spawnRate) {
					enemies.add(new NeuEnemy(gameCanvas.getWidth() + 40,
							rand.nextDouble() * (gameCanvas.getHeight() - 90) + 30, -baseSpeed));
					spawnTime = 0;
				}
				if (time % 240 == 0 && spawnTime > 10) {
					spawnRate -= 9;
				}
				spawnTime++;

			}

			private void updatePositions() {
				if (pressedKeys.contains("up") && !pressedKeys.contains("topWall")) {
					player.move(0, -2.5);
				}
				if (pressedKeys.contains("down") && !pressedKeys.contains("bottomWall")) {
					player.move(0, 2.5);
				}
				if (pressedKeys.contains("left") && !pressedKeys.contains("leftWall")) {
					player.move(-1, 0);
				}
				if (pressedKeys.contains("right") && !pressedKeys.contains("rightWall")) {
					player.move(1.5, 0);
					player.setImage("neuBlasterThrust.png");
				} else {
					player.setImage("neuBlasterNoThrust.png");
				}
				for (int i = 0; i < enemies.size(); i++) {
					if (enemies.get(i).getDestroyFrame() < 15) {
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
				for (int i = 0; i < enemyBullets.size(); i++) {
					enemyBullets.get(i).move();
					if (enemyBullets.get(i).getX() > gameCanvas.getWidth()) {
						enemyBullets.remove(i);
						i--;
					}
				}
			}

			private void animateScene() {
				//TODO sounds when player is hit
				gameCanvas.getGraphicsContext2D().clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
				gameCanvas.getGraphicsContext2D().drawImage(player.getImage(), player.getX(), player.getY());
				gameCanvas.getGraphicsContext2D().fillText("Lives: " + player.getLives(), 170, 20);
				for (NeuEnemy enemy : enemies) {
					if (enemy.isDestroyed() && enemy.getDestroyFrame() < 30) {
						enemy.setExplosionFrame();
						enemy.setDestroyFrame(enemy.getDestroyFrame() + 1);
					}
					gameCanvas.getGraphicsContext2D().drawImage(enemy.getImage(), enemy.getX(), enemy.getY());
					if (enemy.getPoints() > 0) {
						double yPos = enemy.getY() - 20;
						if (yPos < 60) {
							yPos = enemy.getY() + enemy.getImage().getHeight() + 20;
						}
						gameCanvas.getGraphicsContext2D().fillText(Integer.toString((int) enemy.getPoints()),
								enemy.getX(), yPos);
					}
				}
				for (NeuBullet bullet : playerBullets) {
					gameCanvas.getGraphicsContext2D().setFill(Paint.valueOf("#fff"));
					gameCanvas.getGraphicsContext2D().fillRect(bullet.getX(), bullet.getY(), bullet.getWidth(),
							bullet.getHeight());
				}
				for (NeuBullet bullet : enemyBullets) {
					gameCanvas.getGraphicsContext2D().setFill(Paint.valueOf("#fff"));
					gameCanvas.getGraphicsContext2D().fillRect(bullet.getX(), bullet.getY(), bullet.getWidth(),
							bullet.getHeight());
				}
				gameCanvas.getGraphicsContext2D().fillText("Combo Timer: " + Integer.toString((int) comboTimer / 30),
						gameCanvas.getWidth() - 160, 20);
				gameCanvas.getGraphicsContext2D().fillText("Combo: " + (int) comboCount, gameCanvas.getWidth() - 280,
						20);
			}

			private void handleCollisions() {
				boolean touchingWall = false;
				for (int i = 0; i < walls.length; i++) {
					if (player.getBounds().intersects(walls[i].getBoundsInParent())) {
						touchingWall = true;
						switch (i) {
						case 0:
							if (!pressedKeys.contains("topWall"))
								pressedKeys.add("topWall");
							break;
						case 1:
							if (!pressedKeys.contains("leftWall"))
								pressedKeys.add("leftWall");
							break;
						case 2:
							if (!pressedKeys.contains("bottomWall"))
								pressedKeys.add("bottomWall");
							break;
						case 3:
							if (!pressedKeys.contains("rightWall"))
								pressedKeys.add("rightWall");
							break;
						}
					} else {
						switch (i) {
						case 0:
							pressedKeys.remove("topWall");
							break;
						case 1:
							pressedKeys.remove("leftWall");
							break;
						case 2:
							pressedKeys.remove("bottomWall");
							break;
						case 3:
							pressedKeys.remove("rightWall");
							break;
						}
					}
				}
				if (!touchingWall) {
					pressedKeys.remove("topWall");
					pressedKeys.remove("leftWall");
					pressedKeys.remove("bottomWall");
					pressedKeys.remove("rightWall");
				}
				for (int i = 0; i < enemies.size(); i++) {
					if (!enemies.get(i).isDestroyed() && enemies.get(i).collides(player)) {
						enemies.get(i).destroy();
						player.loseLife();
						playSound("hit");
					}
					if (enemies.get(i).isDestroyed() && enemies.get(i).getDestroyFrame() >= 30) {
						enemies.remove(i);
						i--;
					}
				}
				for (int i = 0; i < enemyBullets.size(); i++) {
					if (enemyBullets.get(i).collides(player)) {
						enemyBullets.remove(i);
						i--;
						player.loseLife();
						playSound("hit");
					}
				}
				for (int i = 0; i < playerBullets.size(); i++) {
					for (NeuEnemy enemy : enemies) {
						if (enemy.collides(playerBullets.get(i))) {
							if (!enemy.isDestroyed()) {
								enemy.destroy();
								playSound("explosion");
							}
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
						if (kc.equals(KeyCode.P) || kc.equals(KeyCode.ESCAPE)) {
							pause();
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

	private static void pause() {
		timer.stop();
		Neublasters.showScene(Neublasters.getPauseScene(), "NeuBlasters - Paused");
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
		gameCanvas.getGraphicsContext2D().setFont(new Font(18));
		scoreBoard.setTextFill(Paint.valueOf("#fff"));
		scoreBoard.setFont(new Font(18));
		gameCanvas.getGraphicsContext2D().setFill(Paint.valueOf("#fff"));
		walls = new Rectangle[] { new Rectangle(0, 30, gameCanvas.getWidth(), 1),
				new Rectangle(0, 0, 1, gameCanvas.getHeight()),
				new Rectangle(0, gameCanvas.getHeight() - 15, gameCanvas.getWidth(), 1),
				new Rectangle(gameCanvas.getWidth() / 2, 0, 0, gameCanvas.getHeight()) };
		background.getChildren().add(scoreBoard);
		background.getChildren().add(gameCanvas);
		background.getChildren().addAll(walls);
		background.setBackground(
				new Background(new BackgroundFill(Paint.valueOf("#000"), CornerRadii.EMPTY, Insets.EMPTY)));
		Scene scene = new Scene(background, width, height);
		return scene;
	}

	public static ArrayList<NeuBullet> getEnemyBullets() {
		return enemyBullets;
	}

	public static AnimationTimer getTimer() {
		return timer;
	}

	public static Scene getGameScene() {
		return gameScene;
	}

	private static String getScoreString() {
		StringBuilder sb = new StringBuilder(Double.toString(score));
		sb.delete(sb.length() - 2, sb.length());
		while (sb.length() < 10) {
			sb.insert(0, 0);
		}
		return sb.toString();
	}

	public static double addScore() {
		double scoreAdd = 1000;
		if (comboTimer > 0) {
			scoreAdd += (100 * comboCount);
		} else {
			comboCount = 0;
		}
		comboCount++;
		comboTimer = 180;
		score += scoreAdd;
		return scoreAdd;
	}

	public static double getScore() {
		// TODO Auto-generated method stub
		return score;
	}
	
	public static synchronized void playSound(String file) {
		  new Thread(new Runnable() {
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
		        Main.class.getResource("/neuSounds/" + file + ".wav"));
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}

}
