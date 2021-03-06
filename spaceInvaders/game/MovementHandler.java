package game;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import models.Barrier;
import models.Bullet;
import models.Enemy;
import models.UFO;

public class MovementHandler implements Runnable {

	public String difficulty;
	public int shotLimit = 1000;
	private int lastShot = 0;
	private int direction = 5;
	public int timeLimit = 501;
	private int time = 0;
	private int lastSpawn = 0;
	private Thread thread;
	private Timeline timeline;
	private Random rand = new Random();
	public int chance = 6;
	private String musicFile = "spaceInvaders/audio/UFOMove.mp3";

	private Media sound = new Media(new File(musicFile).toURI().toString());
	private MediaPlayer mediaPlayer = new MediaPlayer(sound);
	
	public MovementHandler() {
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(1),
		        ae -> timerTick()));
		
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	private void timerTick() {
		time++;
		if (!SpaceInvaders.enemies.isEmpty()) {
			if (time - lastSpawn >= timeLimit) {
				moveEnemies();
				lastSpawn = time;
			}
			
			moveBullets();
			moveUfo();
			
			if (time - lastShot >= shotLimit) {
				enemyShoot();
				lastShot = time;
			}
		}
	}
	
	private void moveUfo() {
		UFO ufo = SpaceInvaders.ufo;
		if (SpaceInvaders.ufo != null) {
			ufo.setX(ufo.getX() - .075);
			if (ufo.getX() + ufo.getWidth() <= 0) {
				SpaceInvaders.entities.getChildren().remove(ufo);
				ufo = null;
				mediaPlayer.stop();
			}
			else if (mediaPlayer.getStatus().equals(Status.READY)) {
				mediaPlayer.setCycleCount(Animation.INDEFINITE);
				mediaPlayer.play();
			}
		}
	}
	
	private void enemyShoot() {
		ArrayList<Enemy> e = SpaceInvaders.enemies;
		int x = 0;
		boolean valid = true;
		
		do {
			if (e.size() > 10) {
				x = rand.nextInt(10);
			}
			else {
				if (!e.isEmpty()) {
					x = rand.nextInt(e.size());
				}
			}
			
			if (x > e.size()) {
				valid = false;
			}
			
			double posX = e.get(x).getX();
			double posY = e.get(x).getY();
			
			for (int i = 0; i < e.size(); i++) {
				if ((e.get(i).getX() == posX && i > 10 && e.size() > 10) ||
						(e.get(i).getX() == posX && i < e.size())) {
					if (e.get(i).getY() > posY) {
						x = i;
					}
				}
			}
		}while(!valid);
		
		Enemy enemy = e.get(x);
		Bullet b = new Bullet(
				enemy.getX() + enemy.getWidth() / 2,
				enemy.getY() + enemy.getHeight(), false);
		SpaceInvaders.entities.getChildren().add(b.getBullet());
		b.setX(0);
		b.setY(0);
		SpaceInvaders.bullets.add(b);
	}
	
	private void moveEnemies() {
		ArrayList<Enemy> e = SpaceInvaders.enemies;
		for (int i = 0; i < e.size(); i++) {
			if (e.get(i).getX() + e.get(i).getWidth() >= SpaceInvaders.gameScene.getWidth() - 5 &&
					direction > 0 || direction < 0 &&
					e.get(i).getX() <= 5) {
				direction = -direction;
				if (timeLimit > 200) {
					timeLimit -= 40;
				}
				else if (timeLimit > 50) {
					timeLimit -= 10;
				}
				for (int j = 0; j < e.size(); j++) {
					e.get(j).setY(e.get(j).getY() + 20);
				}
			}
			if (e.get(i).getY() >= SpaceInvaders.player.getY() - 20) {
				SpaceInvaders.winGame("Game Over - You Lose");
			}
		}
		
		for (int i = 0 ; i < e.size(); i++) {
			e.get(i).setX(e.get(i).getX() + direction);
		}
	}
	
	private void moveBullets() {
		ArrayList<Enemy> e = SpaceInvaders.enemies;
		ArrayList<Barrier> a = SpaceInvaders.barriers;
		ArrayList<Bullet> b = SpaceInvaders.bullets;
		models.Character p = SpaceInvaders.player;
		for (int i = 0; i < b.size(); i++) {
			if (b.get(i).getY() > 400 && b.get(i).isMovingUp()) {
				b.get(i).setY(5);
			}
			if (b.get(i).isMovingUp()) {
				b.get(i).setY(b.get(i).getY() - .2);
			}
			else {
				b.get(i).setY(b.get(i).getY() + .2);
			}
			
			for (int x = 0; x < b.size(); x++) {
				if (i < b.size() && x < b.size() &&
						b.get(i).getBullet().getBoundsInParent().intersects(b.get(x).getBullet().getBoundsInParent())
						&& !b.get(i).equals(b.get(x))) {
					collideBullets(b.get(i), b.get(x));
				}
			}
			
			for (int x = 0; x < e.size(); x++) {
				if (i < b.size() && x < e.size() &&
						b.get(i).getBullet().getBoundsInParent().intersects(e.get(x).getImg().getBoundsInParent())
						&& b.get(i).isMovingUp()) {
					killEnemy(b.get(i), e.get(x));
				}
			}
			
			for (int x = 0; x < a.size(); x++) {
				if (i < b.size() && x < a.size() &&
						b.get(i).getBullet().getBoundsInParent().intersects(a.get(x).getImg().getBoundsInParent())) {
					damageBarrier(b.get(i), a.get(x));
				}
			}
			
			if (i < b.size() && SpaceInvaders.ufo != null &&
					b.get(i).getBullet().getBoundsInParent().intersects(SpaceInvaders.ufo.getImg().getBoundsInParent())) {
				killUFO(b.get(i));
			}
			
			if (i < b.size() && !b.get(i).isMovingUp() &&
					b.get(i).getBullet().getBoundsInParent().intersects(p.getImg().getBoundsInParent())) {
				loseLife(1);
				SpaceInvaders.entities.getChildren().remove(b.get(i).getBullet());
				b.remove(i);
			}
			
			if (i < b.size() && b.get(i).getBullet().getBoundsInParent().getMaxY() < -410) {
				SpaceInvaders.entities.getChildren().remove(b.get(i).getBullet());
				b.remove(i);
			}
			
			if (i < b.size() && b.get(i).getY() > SpaceInvaders.gameScene.getHeight() - 160 &&
					!b.get(i).isMovingUp()) {
				SpaceInvaders.entities.getChildren().remove(b.get(i).getBullet());
				b.remove(i);
			}
		}
	}
	
	private void killUFO(Bullet b) {
		int score = 0;
		
		switch (difficulty) {
		case "easy":
			score = 5000;
			break;
		case "medium":
			score = 10000;
			break;
		case "hard":
			score = 15000;
			break;
		default:
			break;
		}
		
		SpaceInvaders.score += score;
		String f = Integer.toString(SpaceInvaders.score);
		SpaceInvaders.scoreString = SpaceInvaders.scoreString.substring(0, SpaceInvaders.scoreString.length() - f.length());
		SpaceInvaders.scoreString += f;
		SpaceInvaders.scorel.setText("SCORE: " + SpaceInvaders.scoreString);
		
		SpaceInvaders.entities.getChildren().remove(b.getBullet());
		SpaceInvaders.bullets.remove(b);
		SpaceInvaders.ufo.playDeathAnimation();
		//SpaceInvaders.entities.getChildren().remove(SpaceInvaders.ufo.getImg());
		//SpaceInvaders.ufo = null;
		mediaPlayer.stop();
		
		ImageView iv = new ImageView(new Image("file:spaceInvaders/images/SpaceshipRed.png"));
		iv.setFitWidth(SpaceInvaders.player.getWidth());
		iv.setFitHeight(SpaceInvaders.player.getHeight());
		SpaceInvaders.player.getImg().getChildren().remove(SpaceInvaders.player.getIv());
		SpaceInvaders.player.getImg().getChildren().add(iv);
		SpaceInvaders.player.setFireSpeed(300);
		Timeline tl = new Timeline(new KeyFrame(
		        Duration.millis(5000),
		        ae -> resetFireSpeed(iv)));
		tl.play();
	}
	
	private void resetFireSpeed(ImageView iv1) {
		ImageView iv = new ImageView(new Image("file:spaceInvaders/images/Spaceship.png"));
		iv.setFitWidth(SpaceInvaders.player.getWidth());
		iv.setFitHeight(SpaceInvaders.player.getHeight());
		SpaceInvaders.player.getImg().getChildren().remove(iv1);
		SpaceInvaders.player.getImg().getChildren().add(iv);
		SpaceInvaders.player.setFireSpeed(800);
	}
	
	private void collideBullets(Bullet b1, Bullet b2) {
		if (b1.isMovingUp() != b2.isMovingUp()) {
			SpaceInvaders.entities.getChildren().remove(b1.getBullet());
			SpaceInvaders.entities.getChildren().remove(b2.getBullet());
			SpaceInvaders.bullets.remove(b1);
			SpaceInvaders.bullets.remove(b2);
		}
	}
	
	private void damageBarrier(Bullet b, Barrier a) {
		a.takeDamage(13);
		SpaceInvaders.entities.getChildren().remove(b.getBullet());
		SpaceInvaders.bullets.remove(b);
	}
	
	private void addPoints() {
		int points = 0;
		switch (difficulty) {
		case "easy":
			points = 100 + (551 - (timeLimit));
			break;
		case "medium":
			points = 200 + (551 - (timeLimit));
			break;
		case "hard":
			points = 300 + (551 - (timeLimit));
			break;
		default:
			points = 100 + (551 - (timeLimit));
			break;
		}
		SpaceInvaders.score += points;
		String f = Integer.toString(SpaceInvaders.score);
		SpaceInvaders.scoreString = SpaceInvaders.scoreString.substring(0, SpaceInvaders.scoreString.length() - f.length());
		SpaceInvaders.scoreString += f;
		SpaceInvaders.scorel.setText("SCORE: " + SpaceInvaders.scoreString);
	}
	
	private void summonUFO() {
		if (SpaceInvaders.enemies.size() == 15 || SpaceInvaders.enemies.size() == 25 ||
				SpaceInvaders.enemies.size() == 35) {
			int x = 0;
			if (chance > 0) {
				x = rand.nextInt(chance);
			}
			if (x == 0 && chance > 0) {
				SpaceInvaders.ufo = new UFO(400, 20);
				SpaceInvaders.entities.getChildren().add(SpaceInvaders.ufo.getImg());
				chance = 0;
			}
			else {
				chance--;
			}
		}
	}
	
	private void killEnemy(Bullet b, Enemy e) {
		addPoints();
		
		e.playDeathAnimation();
		SpaceInvaders.entities.getChildren().remove(b.getBullet());
		SpaceInvaders.bullets.remove(b);
		if (SpaceInvaders.enemies.size() <= 1) {
			SpaceInvaders.continueGame(SpaceInvaders.player.getLives());
		}
		else if (SpaceInvaders.enemies.size()% 26 == 0) {
			SpaceInvaders.player.setLives(SpaceInvaders.player.getLives() + 1);
			SpaceInvaders.livesl.setText("LIVES: " + SpaceInvaders.player.getLives());
			String musicFile = "spaceInvaders/audio/ExtraLife.mp3";

			Media sound = new Media(new File(musicFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
			
			SpaceInvaders.player.playExtraLifeAnimation();
		}
		
		summonUFO();
		
		String musicFile = "spaceInvaders/audio/Boom.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
	
	private void loseLife(int livesLost) {
		SpaceInvaders.liveLostAnimation(SpaceInvaders.player.getLives());
		SpaceInvaders.player.loseLife(1);
		SpaceInvaders.livesl.setText("LIVES: " + SpaceInvaders.player.getLives());
		
		String musicFile = "spaceInvaders/audio/TakeDamge.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
	
	public void stopTimer() {
		timeline.stop();
	}
	
	public void pauseTimer() {
		timeline.pause();
	}
	
	public void resumeTimer() {
		timeline.play();
	}
	
	public void resetSound() {
		mediaPlayer = new MediaPlayer(sound);
	}
}
