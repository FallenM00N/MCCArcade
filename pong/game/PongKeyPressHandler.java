package game;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class PongKeyPressHandler implements Runnable {
	private Thread thread;
	private Timeline timeline;
	static ArrayList<String> pressedKeys = new ArrayList<>();

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
				if(kc.equals(KeyCode.W) && !pressedKeys.contains("p1up")) {
					pressedKeys.add("p1up");
				}
				if(kc.equals(KeyCode.S) && !pressedKeys.contains("p1down")) {
					pressedKeys.add("p1down");
				}
				if(kc.equals(KeyCode.UP) && !pressedKeys.contains("p2up")) {
					pressedKeys.add("p2up");
				}
				if(kc.equals(KeyCode.DOWN) && !pressedKeys.contains("p2down")) {
					pressedKeys.add("p2down");
				}
				if(kc.equals(KeyCode.LEFT) && !pressedKeys.contains("left")) {
					pressedKeys.add("left");
				}
				if(kc.equals(KeyCode.RIGHT) && !pressedKeys.contains("right")) {
					pressedKeys.add("right");
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
				case LEFT:
					pressedKeys.remove("left");
					break;
				case RIGHT:
					pressedKeys.remove("right");
					break;
				case ESCAPE:
				case P:
					pauseGame();
				default:
					break;
				}
			}
		});
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	private void timerTick() {
//		if(pressedKeys.contains("p1up") && !pressedKeys.contains("p1down")) {
//			movePaddle1("up");
//		}
//		if(pressedKeys.contains("p1down") && !pressedKeys.contains("p1up")) {
//			movePaddle1("down");
//		}
//		if(pressedKeys.contains("p2up") && !pressedKeys.contains("p2down")) {
//			movePaddle2("up");
//		}
//		if(pressedKeys.contains("p2down") && !pressedKeys.contains("p2up")) {
//			movePaddle2("down");
//		}
	}
	
	

//	private void movePaddle1(String dir) {
//		Player leftPlayer = PongEngine.leftPlayer;
//		if(dir.equals("up") && !leftPlayer.getPaddle().getBoundsInParent().intersects(ScoreBoard.getWall().getBoundsInParent())) {
//			leftPlayer.move(-speed);
//		}else if(dir.equals("down") && !leftPlayer.getPaddle().getBoundsInParent().intersects(PongEngine.getBottomWall().getBoundsInParent())) {
//			leftPlayer.move(speed);
//		}
//	}
//	
//	private void movePaddle2(String dir) {
//		Player rightPlayer = PongEngine.rightPlayer;
//		if(dir.equals("up") && !rightPlayer.getPaddle().getBoundsInParent().intersects(ScoreBoard.getWall().getBoundsInParent())) {
//			rightPlayer.move(-speed);
//		}else if(dir.equals("down") && !rightPlayer.getPaddle().getBoundsInParent().intersects(PongEngine.getBottomWall().getBoundsInParent())) {
//			rightPlayer.move(speed);
//		}
//	}

	private void pauseGame() {
		timeline.pause();
		Pong.showPauseScreen();
	} 

	public void resume() {
		pressedKeys.clear();
		timeline.play();
	}

	public void close() {
		thread.interrupt();
		timeline.stop();
	}
}
