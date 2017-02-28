package game;

import java.io.File;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import models.Bullet;
import models.Character;

public class KeyPressHandler implements Runnable {

	private int time = 0;
	private int lastFire = 0;
	private Thread thread;
	private Timeline timeline;
	private ArrayList<String> pressedKeys = new ArrayList<>();
	
	public KeyPressHandler() {
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(1),
		        ae -> timerTick()));
		
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		SpaceInvaders.gameScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				KeyCode kc = event.getCode();
				if (kc.equals(KeyCode.LEFT) || kc.equals(KeyCode.A) && !pressedKeys.contains("left")) {
					pressedKeys.add("left");
				}
				else if (kc.equals(KeyCode.RIGHT) || kc.equals(KeyCode.D) && !pressedKeys.contains("right")) {
					pressedKeys.add("right");
				}
				else if (kc.equals(KeyCode.SPACE) && !pressedKeys.contains("space")) {
					pressedKeys.add("space");
				}
				else if (kc.equals(KeyCode.P) || kc.equals(KeyCode.ESCAPE)) {
					SpaceInvaders.pauseGame();
				}
			}
		});
		
		SpaceInvaders.gameScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				KeyCode kc = event.getCode();
				if (kc.equals(KeyCode.LEFT) || kc.equals(KeyCode.A)) {
					pressedKeys.remove("left");
				}
				else if (kc.equals(KeyCode.RIGHT) || kc.equals(KeyCode.D)) {
					pressedKeys.remove("right");
				}
				else if (kc.equals(KeyCode.SPACE)) {
					pressedKeys.remove("space");
				}
			}
		});
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
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
	
	private void timerTick() {
		time++;
		if (pressedKeys.contains("left") && !pressedKeys.contains("right")) {
			moveLeft();
		}
		else if (pressedKeys.contains("right") && !pressedKeys.contains("left")) {
			moveRight();
		}
		else {
			if (pressedKeys.contains("left") && pressedKeys.contains("right")) {
				
			}
		}
		
		if (pressedKeys.contains("space")) {
			shoot();
		}
		if (pressedKeys.size() > 3) {
			if (pressedKeys.contains("space")) {
				pressedKeys.clear();
				pressedKeys.add("space");
			}
			else {
				pressedKeys.clear();
			}
		}
	}
	
	private void moveLeft() {
		if (SpaceInvaders.player.getX() > 2) {
			SpaceInvaders.player.setX(SpaceInvaders.player.getX() - .25);
		}
		else {
			pressedKeys.remove("left");
		}
	}
	
	private void moveRight() {
		if (SpaceInvaders.player.getX() < 400 - SpaceInvaders.player.getWidth() - 2) {
			SpaceInvaders.player.setX(SpaceInvaders.player.getX() + .25);
		}
		else {
			pressedKeys.remove("right");
		}
	}
	
	private void shoot() {
		if (time - lastFire >= 800) {
			Character p = SpaceInvaders.player;
			Bullet b = new Bullet(p.getX() + p.getWidth() / 2 - 1.5, p.getY() - 12, true);
			SpaceInvaders.bullets.add(b);
			SpaceInvaders.entities.getChildren().add(b.getBullet());
			lastFire = time;
			if (SpaceInvaders.player.getX() >= 400 - SpaceInvaders.player.getWidth() - 2) {
				SpaceInvaders.player.setX(SpaceInvaders.player.getX() - .25);
			}
			else if (SpaceInvaders.player.getX() <= 2) {
				SpaceInvaders.player.setX(SpaceInvaders.player.getX() + .25);
			}
			String musicFile = "spaceInvaders/audio/Shoot.mp3";

			Media sound = new Media(new File(musicFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
		}
	}
}
