package game;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import models.Player;

public class PongKeyPressHandler implements Runnable {
	private int time = 0;
	private Thread thread;
	private Timeline timeline;
	private ArrayList<String> pressedKeys = new ArrayList<>();

	public PongKeyPressHandler() {
		timeline = new Timeline(new KeyFrame(
				Duration.millis(1),
				ae -> timerTick()));

		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		PongEngine.gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode kc = event.getCode();
				switch (kc) {
				case W:
					pressedKeys.add("p1up");
					break;
				case S:
					pressedKeys.add("p1down");
					break;
				case UP:
					pressedKeys.add("p2up");
					break;
				case DOWN:
					pressedKeys.add("p2down");
					break;
				}
			}
		});
		PongEngine.gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode kc = event.getCode();
				switch(kc) {
				case W:
					pressedKeys.remove("p1up");
					break;
				case S:
					pressedKeys.remove("p1down");
					break;
				case UP:
					pressedKeys.remove("p2up");
					break;
				case DOWN:
					pressedKeys.remove("p2down");
					break;
				case ESCAPE:
				case P:
					pauseGame();
				}
			}
		});
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	private void timerTick() {
		time++;
		if(pressedKeys.contains("p1up") && !pressedKeys.contains("p1down")) {
			movePaddle1("up");
		}
		if(pressedKeys.contains("p1down") && !pressedKeys.contains("p1up")) {
			movePaddle1("down");
		}
		if(pressedKeys.contains("p2up") && !pressedKeys.contains("p2down")) {
			movePaddle2("up");
		}
		if(pressedKeys.contains("p2down") && !pressedKeys.contains("p2up")) {
			movePaddle2("down");
		}
		
	}
	
	private void movePaddle1(String dir) {
		Player leftPlayer = PongEngine.leftPlayer;
		if(dir.equals("up")) {
			leftPlayer.setY(leftPlayer.getY() - 1);
		}
	}
	
	private void movePaddle2(String dir) {
		
	}

	private void pauseGame() {
		// TODO Auto-generated method stub

	}
}
