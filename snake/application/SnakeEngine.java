package application;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import models.Direction;
import models.Score;

public class SnakeEngine extends Snake{

	public static final int SQUARE_SIZE = 40;
	public static final int WIDTH = 20 * SQUARE_SIZE;
	public static final int HEIGHT = 15 * SQUARE_SIZE;

	private static Direction direction = Direction.RIGHT;
	private static boolean moved = false;
	private static boolean running = false;

	private static Timeline timeline = new Timeline();
	private static ObservableList<Node> snake;
	private static Pane root = new Pane();
	private static Rectangle food = new Rectangle();
	private static Group snakeBody = new Group();
	private static boolean toRemove;
	private static Node tail;
	private static double tailX;
	private static double tailY;
	private static Text t = new Text();
	private static Score s = new Score();
	


	public static void run() throws Exception {
		initializeGame();
		startGame();

	}

	private static Parent createSnakeContent() {
		createBackground();
		createSnake();
		createFood();
		KeyFrame frame = new KeyFrame(Duration.seconds(0.15), event -> {
			if (!running)
				return;
			snakeMovement();
			detectCollision();
			eatFood();
		});

		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		root.getChildren().addAll(food, snakeBody);

		return root;
	}

	private static void createBackground() {
		root = new Pane();
		root.setPrefSize(WIDTH, HEIGHT);
		root.setBackground(new Background(new BackgroundFill(Color.MIDNIGHTBLUE.darker().darker().darker().darker(), null, null)));
		
		t = new Text(10,20, "Score: ");
		t.setFill(Color.BLACK);
		t.setFont(Font.font(java.awt.Font.SANS_SERIF, 15));
		root.getChildren().add(t);
		
		String musicFile = "snake\\application\\snake8bit.mp3";
		AudioClip sound = new AudioClip(new File(musicFile).toURI().toString());
		sound.play();
	}

	private static void createFood() {
		food = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
		food.setFill(Color.GOLDENROD);
		food.setArcWidth(40);
		food.setArcHeight(40);
		food.setTranslateX((int) (Math.random() * (WIDTH - SQUARE_SIZE)) / SQUARE_SIZE * SQUARE_SIZE);
		food.setTranslateY((int) (Math.random() * (HEIGHT - SQUARE_SIZE)) / SQUARE_SIZE * SQUARE_SIZE);
	}

	private static void createSnake() {
		snake = snakeBody.getChildren();
	}

	private static void snakeMovement() {
		toRemove = snake.size() > 1;

		tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

		tailX = tail.getTranslateX();
		tailY = tail.getTranslateY();

		switch (direction) {
		case UP:
			tail.setTranslateX(snake.get(0).getTranslateX());
			tail.setTranslateY(snake.get(0).getTranslateY() - SQUARE_SIZE);
			break;
		case DOWN:
			tail.setTranslateX(snake.get(0).getTranslateX());
			tail.setTranslateY(snake.get(0).getTranslateY() + SQUARE_SIZE);
			break;
		case LEFT:
			tail.setTranslateX(snake.get(0).getTranslateX() - SQUARE_SIZE);
			tail.setTranslateY(snake.get(0).getTranslateY());
			break;
		case RIGHT:
			tail.setTranslateX(snake.get(0).getTranslateX() + SQUARE_SIZE);
			tail.setTranslateY(snake.get(0).getTranslateY());
			break;
		}

		moved = true;

		if (toRemove)
			snake.add(0, tail);
	}

	private static void detectCollision() {
		for (Node rect : snake) {
			if (rect != tail && tail.getTranslateX() == rect.getTranslateX()
					&& tail.getTranslateY() == rect.getTranslateY()) {
				restartGame();
				break;
			}
		}

		if (tail.getTranslateX() < 0 || tail.getTranslateX() >= WIDTH || tail.getTranslateY() < 0
				|| tail.getTranslateY() >= HEIGHT) {
			restartGame();
		}

	}

	private static void eatFood() {
		if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
			food.setTranslateX((int) (Math.random() * (WIDTH - SQUARE_SIZE)) / SQUARE_SIZE * SQUARE_SIZE);
			food.setTranslateY((int) (Math.random() * (HEIGHT - SQUARE_SIZE)) / SQUARE_SIZE * SQUARE_SIZE);

			s.setScore(s.getScore() + 1);
			t = new Text(10,20, "Score: " + s.getScore());
			t.setFill(Color.WHITE);
			t.setFont(Font.font(java.awt.Font.SANS_SERIF, 15));
			
			
			root.getChildren().add(t);
			
			
			Rectangle rect = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
			rect.setFill(Color.DARKSEAGREEN);
			rect.setArcWidth(30);
			rect.setArcHeight(30);
			rect.setTranslateX(tailX);
			rect.setTranslateY(tailY);

			snake.add(rect);
		}
	}

	private static void restartGame() {
		stopGame();
		startGame();
	}

	private static void stopGame() {
		running = false;
		timeline.stop();
		snake.clear();
	}

	private static void startGame() {
		direction = Direction.RIGHT;
		Rectangle head = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
		head.setFill(Color.DARKSEAGREEN);
		head.setArcWidth(30);
		head.setArcHeight(30);
		snake.add(head);
		timeline.play();
		running = true;
	}

	public static void initializeGame() throws Exception {
		Scene scene = new Scene(createSnakeContent());

		scene.setOnKeyPressed(event -> {
			if (!moved)
				return;

			if (moved) {
				switch (event.getCode()) {
				case UP:
					if (direction != Direction.DOWN)
						direction = Direction.UP;
					break;
				case DOWN:
					if (direction != Direction.UP)
						direction = Direction.DOWN;
					break;
				case LEFT:
					if (direction != Direction.RIGHT)
						direction = Direction.LEFT;
					break;
				case RIGHT:
					if (direction != Direction.LEFT)
						direction = Direction.RIGHT;
					break;
				}
			}
			moved = false;
		});

		ArcadeView.setScene(scene, "Snake");
	}

}
