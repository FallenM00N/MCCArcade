package game;

import java.util.ArrayList;
import java.util.Random;

import application.MainMenu;
import enums.CommandType;
import enums.Difficulty;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import models.Game;

public class Simon extends Game {

	private static boolean gameOver = false;
	private static boolean paused = false;
	private static boolean playerTurn = false;
	private static double time = 0;
	private static Difficulty difficulty = Difficulty.EASY;
	private static int round = 0;
	private static int score = 0;
	private static double timeLimit = 2;
	private static int currentIndex = 0;
	private static ArrayList<CommandType> commands;
	private static Scene titleScene;
	private static Scene difficultyScene;
	private static Scene gameScene;
	private static Scene infoScene;
	private static Rectangle top = new Rectangle(50, 50, Paint.valueOf("rgba(255,0,60,.5)"));
	private static Rectangle right = new Rectangle(50, 50, Paint.valueOf("rgba(0,100,255,.5)"));
	private static Rectangle bottom = new Rectangle(50, 50, Paint.valueOf("rgba(0,200,40,.5)"));
	private static Rectangle left = new Rectangle(50, 50, Paint.valueOf("rgba(220,220,0,.5)"));
	private static Text title;
	private static Text scoreVal;
	private static Text roundVal;
	private static Text pause;
	private Timeline timeline = new Timeline(new KeyFrame(
	        Duration.millis(100),
	        ae -> timerTick()));
	
	private void timerTick() {
		time += .1;
		
		if (time > timeLimit && playerTurn) {
			gameOver();
		}
		else if (time >= timeLimit && !playerTurn) {
			time = 0;
			selectNextCommand();
		}
		if (time >= timeLimit / 2 && time <= timeLimit / 2 + .1) {
			top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
			right.setFill( Paint.valueOf("rgba(0,100,255,.3)"));
			bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
			left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		}
	}
	
	private void createPauseListener() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode kc = event.getCode();
				
				if ((kc.equals(KeyCode.ESCAPE) || kc.equals(KeyCode.P)) && !paused && !gameOver) {
					pause();
				}
				else if ((kc.equals(KeyCode.ESCAPE) || kc.equals(KeyCode.P)) && paused && !gameOver) {
					resume();
				}
			}
		});
	}
	
	private void createGameScene() {
		BorderPane bp = new BorderPane();
		GridPane gp = new GridPane();
		VBox vbox = new VBox();
		
		title = new Text("Simon's Turn");
		title.setStyle("-fx-font: 20 Arial;");
		scoreVal = new Text("Points: " + score);
		scoreVal.setStyle("-fx-font: 15 Arial;");
		roundVal = new Text("Round: " + round);
		roundVal.setStyle("-fx-font: 15 Arial;");
		pause = new Text("");
		pause.setStyle("-fx-font: 30 Arial;");
		
		top = new Rectangle(50, 50, Paint.valueOf("rgba(255,0,60,.3)"));
		top.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (playerTurn) {
					checkCorrect(CommandType.RED);
				}
			}
		});
		right = new Rectangle(50, 50, Paint.valueOf("rgba(0,100,255,.3)"));
		right.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (playerTurn) {
					checkCorrect(CommandType.BLUE);
				}
			}
		});
		bottom = new Rectangle(50, 50, Paint.valueOf("rgba(0,200,40,.3)"));
		bottom.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (playerTurn) {
					checkCorrect(CommandType.GREEN);
				}
			}
		});
		left = new Rectangle(50, 50, Paint.valueOf("rgba(220,220,0,.3)"));
		left.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (playerTurn) {
					checkCorrect(CommandType.YELLOW);
				}
			}
		});
		
		GridPane.setConstraints(top, 2,1);
		GridPane.setConstraints(right, 3,2);
		GridPane.setConstraints(bottom, 2,3);
		GridPane.setConstraints(left, 1,2);
		
		gp.getChildren().addAll(top, right, bottom, left);
		
		vbox.setSpacing(10);
		vbox.getChildren().addAll(scoreVal, roundVal, pause);
		
		Button menu = new Button("Return to Title Screen");
		menu.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				titleScreen();
			}
			
		});
		
		bp.setCenter(gp);
		bp.setTop(title);
		bp.setRight(vbox);
		bp.setBottom(menu);
		
		BorderPane.setMargin(gp, new Insets(20, 0, 0, 10));
		BorderPane.setMargin(vbox, new Insets(20, 10, 0, 0));
		BorderPane.setMargin(menu, new Insets(0, 0, 10, 0));
		
		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setAlignment(gp, Pos.CENTER_LEFT);
		BorderPane.setAlignment(vbox, Pos.CENTER);
		BorderPane.setAlignment(menu, Pos.CENTER);
		
		gameScene = new Scene(bp, 300, 250);
	}
	
	private void createInfoScene() {
		BorderPane bp = new BorderPane();
		HBox hbox = new HBox();
		VBox vbox1 = new VBox();
		VBox vbox2 = new VBox();
		
		Text t = new Text("Description");
		t.setStyle("-fx-font: 20 Arial;");
		Text simonTurn = new Text("\"Simon's Turn\"");
		Text yourTurn = new Text("\"Your Turn\"");
		Text over = new Text("\"Game Over\"");
		Text info1 = new Text(": Pay attention to pattern");
		Text info2 = new Text(": Repeat the pattern");
		Text info3 = new Text(": You lost, return to title screen");
		
		Button tScreen = new Button("Return to Title Screen");
		tScreen.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				titleScreen();
			}
			
		});
		
		vbox1.setSpacing(10);
		vbox2.setSpacing(10);
		vbox1.getChildren().addAll(simonTurn, yourTurn, over);
		vbox2.getChildren().addAll(info1, info2, info3);
		
		hbox.getChildren().addAll(vbox1, vbox2);
		HBox.setMargin(vbox1, new Insets(10,0,0,30));
		HBox.setMargin(vbox2, new Insets(10,0,0,10));
		
		bp.setTop(t);
		bp.setCenter(hbox);
		bp.setBottom(tScreen);
		BorderPane.setAlignment(t, Pos.CENTER);
		BorderPane.setAlignment(hbox, Pos.CENTER);
		BorderPane.setAlignment(tScreen, Pos.CENTER);
		BorderPane.setMargin(tScreen, new Insets(0,0,10,0));
		
		infoScene = new Scene(bp, 300, 300);
	}
	
	private void createDifficultyScene() {
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		
		Text t = new Text("Select a Difficulty");
		t.setStyle("-fx-font: 20 Arial;");
		Button easy = new Button("Easy");
		easy.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				difficulty = Difficulty.EASY;
				startGame();
			}
			
		});
		Button medium = new Button("Normal");
		medium.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				difficulty = Difficulty.NORMAL;
				startGame();
			}
			
		});
		Button hard = new Button("Hard");
		hard.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				difficulty = Difficulty.HARD;
				startGame();
			}
			
		});
		
		vbox.setSpacing(10);
		vbox.getChildren().addAll(easy, medium, hard);
		vbox.setAlignment(Pos.CENTER);
		
		bp.setCenter(vbox);
		bp.setTop(t);
		BorderPane.setAlignment(vbox, Pos.CENTER);
		BorderPane.setAlignment(t, Pos.TOP_CENTER);
		
		BorderPane.setMargin(vbox, new Insets(-30, 0, 0, 0));
		
		difficultyScene = new Scene(bp, 200, 200);
	}
	
	private void selectNextCommand() {
		if (commands.get(currentIndex).equals(CommandType.RED)) {
			top.setFill(Paint.valueOf("rgba(255,0,60,1.0)"));
			right.setFill( Paint.valueOf("rgba(0,100,255,.3)"));
			bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
			left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		}
		else if (commands.get(currentIndex).equals(CommandType.BLUE)) {
			top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
			right.setFill( Paint.valueOf("rgba(0,100,255,1.0)"));
			bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
			left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		}
		else if (commands.get(currentIndex).equals(CommandType.GREEN)) {
			top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
			right.setFill( Paint.valueOf("rgba(0,100,255,.3)"));
			bottom.setFill(Paint.valueOf("rgba(0,200,40,1.0)"));
			left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		}
		else {
			top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
			right.setFill( Paint.valueOf("rgba(0,100,255,.3)"));
			bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
			left.setFill(Paint.valueOf("rgba(220,220,0,1.0)"));
		}
		
		if (currentIndex == commands.size() - 1) {
			playerTurn = !playerTurn;
			currentIndex = 0;
			if (!playerTurn) {
				createCommands();
			}
			else {
				if (playerTurn) {
					title.setText("Your Turn");
				}
				else {
					title.setText("Simon's Turn");
				}
			}
		}
		else {
			currentIndex++;
		}
	}
	
	private void startGame() {
		round = 0;
		time = 0;
		commands.clear();
		score = 0;
		currentIndex = 0;
		title.setText("Simon's Turn");
		roundVal.setText("Round: " + round);
		scoreVal.setText("Points: " + score);
		top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
		right.setFill( Paint.valueOf("rgba(0,100,255,.3)"));
		bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
		left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		createCommands();
		createPauseListener();
		showScene(gameScene);
	}
	
	private void createCommands() {
		Random rand = new Random();
		if (round == 0) {
			for (int i = 0; i < 2; i++) {
				CommandType d = CommandType.values()[rand.nextInt(CommandType.values().length)];
				
				commands.add(d);
			}
		}
		if (difficulty.equals(Difficulty.HARD)) {
			commands.clear();
			
			for (int i = 0; i < round + 3; i++) {
				CommandType d = CommandType.values()[rand.nextInt(CommandType.values().length)];
				
				commands.add(d);
			}
		}
		else if (difficulty.equals(Difficulty.NORMAL)) {
			CommandType d = CommandType.values()[rand.nextInt(CommandType.values().length)];
			
			commands.add(d);
			timeLimit -= .1;
		}
		else {
			CommandType d = CommandType.values()[rand.nextInt(CommandType.values().length)];
			
			commands.add(d);
		}
		
		round++;
		roundVal.setText("Round: " + round);
	}
	
	private void checkCorrect(CommandType ct) {
		if (ct.equals(commands.get(currentIndex))) {
			time = 0;
			score++;
			scoreVal.setText("Points: " + score);
			currentIndex++;
		}
		else {
			gameOver();
		}
		if (currentIndex == commands.size()) {
			playerTurn = false;
			createCommands();
			currentIndex = 0;
			title.setText("Simon's Turn");
		}
	}
	
	@Override
	public void pause() {
		paused = true;
		pause.setText("Paused");
		timeline.pause();
	}
	@Override
	public void resume() {
		paused = false;
		pause.setText("");
		timeline.play();
	}
	@Override
	public void exit() {
		timeline.stop();
		System.exit(0);
	}
	@Override
	public void mainMenu() {
		showScene(MainMenu.getMenuScene());
	}
	@Override
	public void titleScreen() {
		timeline.stop();
		showScene(titleScene);
	}
	@Override
	public void play() {
		commands = new ArrayList<>();
		titleScene = createTitleScene();
		createGameScene();
		createInfoScene();
		createDifficultyScene();
		showScene(titleScene);
	}
	@Override
	public Scene createTitleScene() {
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		
		Text t = new Text("Simon (says)");
		t.setStyle("-fx-font: 25 Arial");
		Button start = new Button("Start Game");
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				showScene(difficultyScene);
			}
			
		});
		Button info = new Button("Info/Controls");
		info.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				showScene(infoScene);
			}
			
		});
		Button menu = new Button("Return to Main Menu");
		menu.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				mainMenu();
			}
			
		});
		Button quit = new Button("Quit");
		quit.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				exit();
			}
			
		});
		
		vbox.setSpacing(10);
		vbox.getChildren().addAll(start, info, menu, quit);
		vbox.setAlignment(Pos.CENTER);
		
		bp.setCenter(vbox);
		bp.setTop(t);
		//bp.setStyle("-fx-background: #F70;");
		BorderPane.setAlignment(t, Pos.TOP_CENTER);
		BorderPane.setMargin(vbox, new Insets(0, 0, 0, 0));
		
		Scene title = new Scene(bp, 300, 200);
		return title;
	}
	@Override
	public void gameOver() {
		gameOver = true;
		title.setText("Game Over");
		timeline.stop();
		playerTurn = false;
	}
}
