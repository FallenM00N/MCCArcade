package game;

import java.util.ArrayList;

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

public class NeublastersEngine {

	private static Scene gameScene;
	private static Canvas gameCanvas;
	private static AnimationTimer timer;

	public static void run() {
		gameScene = createBlankScene();
		Neublasters.showScene(gameScene, "Neublasters");
		createAnimationTimer();
	}

	private static void createAnimationTimer() {
		timer = new AnimationTimer() {
			ArrayList<String> pressedKeys = new ArrayList<>();

			@Override
			public void handle(long now) {
				keyListen();
				handleCollisions();
				animateScene();
			}

			private void animateScene() {
				// TODO Auto-generated method stub
				for (String s : pressedKeys) {
					System.out.println(s);
				}
			}

			private void handleCollisions() {
				// TODO Auto-generated method stub

			}

			private void keyListen() {
				gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						KeyCode kc = event.getCode();
						if (kc.equals(KeyCode.W) && !pressedKeys.contains("up")) {
							pressedKeys.add("up");
						}
						if (kc.equals(KeyCode.S) && !pressedKeys.contains("down")) {
							pressedKeys.add("down");
						}
						if (kc.equals(KeyCode.A) && !pressedKeys.contains("left")) {
							pressedKeys.add("left");
						}
						if (kc.equals(KeyCode.D) && !pressedKeys.contains("right")) {
							pressedKeys.add("right");
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
						if (kc.equals(KeyCode.W)) {
							pressedKeys.remove("up");
						}
						if (kc.equals(KeyCode.S)) {
							pressedKeys.remove("down");
						}
						if (kc.equals(KeyCode.A)) {
							pressedKeys.remove("left");
						}
						if (kc.equals(KeyCode.D)) {
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
