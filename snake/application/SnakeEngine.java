package application;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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
	private static Snake sn = new Snake();
	private static String musicFile;
	private static AudioClip sound;
	private static boolean isMusicPlaying = false;

	private static Scene gameScene;

	public static void run() throws Exception {
		initializeGame();
		startGame();
		showScene();

	}

	private static Parent createSnakeContent() {
		createBackground();
		createSnake();
		createFood();
		KeyFrame frame = new KeyFrame(Duration.seconds(0.15), event -> {
			if (!running){
				return;				
			}
			snakeMovement();
			detectCollision();
			eatFood();
		});

		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		

		root.getChildren().addAll(food, snakeBody);
		root.getChildren().add(t);
		return root;
	}

	private static void createBackground() {
		root = new Pane();
		root.setPrefSize(WIDTH, HEIGHT);
		root.setBackground(new Background(new BackgroundFill(Color.MIDNIGHTBLUE.darker().darker().darker().darker(), null, null)));
		t = new Text(10,20, "Score: 0");

		t.setFill(Color.WHITE);
		t.setFont(Font.font(java.awt.Font.SANS_SERIF, 15));

		

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
				sn.gameOver();
				break;
			}
		}

		if (tail.getTranslateX() < 0 || tail.getTranslateX() >= WIDTH || tail.getTranslateY() < 0
				|| tail.getTranslateY() >= HEIGHT) {
			sn.gameOver();

		}

	}

	private static void eatFood() {
		if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
			food.setTranslateX((int) (Math.random() * (WIDTH - SQUARE_SIZE)) / SQUARE_SIZE * SQUARE_SIZE);
			food.setTranslateY((int) (Math.random() * (HEIGHT - SQUARE_SIZE)) / SQUARE_SIZE * SQUARE_SIZE);

			root.getChildren().remove(t);
			s.setScore(s.getScore() + 1);
			//t.setFill(Color.WHITE);
			//t.setFont(Font.font(java.awt.Font.SANS_SERIF, 15));
			String score = Integer.toString(s.getScore());
			t.setText("Score: " + score);
			root.getChildren().add(t);
			
			
			Rectangle rect = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
			rect.setFill(Color.MEDIUMSEAGREEN);
			rect.setArcWidth(30);
			rect.setArcHeight(30);
			rect.setTranslateX(tailX);
			rect.setTranslateY(tailY);

			snake.add(rect);
		}
	}

	public static void restartGame() throws Exception{
		run();
	}

	public static void stopGame() {
		if(isMusicPlaying){
			sound.stop();			
			snake.clear();
		}
		running = false;
		timeline.stop();
		timeline = new Timeline();
		s.setScore(0);

	}

	public static void startGame() {
		running = true;
		direction = Direction.RIGHT;
		Rectangle head = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
		head.setFill(Color.MEDIUMSEAGREEN);
		head.setArcWidth(30);
		head.setArcHeight(30);
		snake.add(head);
		timeline.play();
	}

	public static void initializeGame() throws Exception {
		gameScene = new Scene(createSnakeContent());
		
		gameScene.setOnKeyPressed(event -> {
			
			if (event.getCode().equals(KeyCode.SPACE) && running) {
				pauseSnake();
				sn.pause();
			}
			
			if (!moved)
				return;

			if (moved) {
				switch (event.getCode()) {
				case UP:
					if (direction != Direction.DOWN)
						direction = Direction.UP;
					break;
				case W:
					if (direction != Direction.DOWN)
						direction = Direction.UP;
					break;
				case DOWN:
					if (direction != Direction.UP)
						direction = Direction.DOWN;
					break;
				case S:
					if (direction != Direction.UP)
						direction = Direction.DOWN;
					break;
				case LEFT:
					if (direction != Direction.RIGHT)
						direction = Direction.LEFT;
					break;
				case A:
					if (direction != Direction.RIGHT)
						direction = Direction.LEFT;
					break;
				case RIGHT:
					if (direction != Direction.LEFT)
						direction = Direction.RIGHT;
					break;
				case D:
					if (direction != Direction.LEFT)
						direction = Direction.RIGHT;
					break;
				}
			}
			moved = false;
		});

	}
	
	public static void showScene(){
		if(running){
			ArcadeView.setScene(gameScene, "Snake");	
			musicFile = "snake\\application\\snake8bit.mp3";
			sound = new AudioClip(new File(musicFile).toURI().toString());
			sound.play();
			isMusicPlaying = true;
			//System.out.println(isMusicPlaying);
		}
		else{
			ArcadeView.setScene(overScene);
		}
		
	}
	
	
	public static void pauseSnake(){
		timeline.pause();
	}
	
	public static void resumeSnake(){
		ArcadeView.setScene(gameScene, "Snake");
		timeline.play();
	}

}
