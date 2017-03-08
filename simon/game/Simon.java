package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Random;

import application.ArcadeView;
import application.MainMenu;
import enums.CommandType;
import enums.Difficulty;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lib.ProgramUtil;
import models.Game;
import models.SimonUser;

public class Simon extends Game {

	private String scoreString = "00000";
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
	private Scene winScene;
	private Scene initScene;
	private static Rectangle top = new Rectangle(50, 50, Paint.valueOf("rgba(255,0,60,.5)"));
	private static Rectangle right = new Rectangle(50, 50, Paint.valueOf("rgba(0,100,255,.5)"));
	private static Rectangle bottom = new Rectangle(50, 50, Paint.valueOf("rgba(0,200,40,.5)"));
	private static Rectangle left = new Rectangle(50, 50, Paint.valueOf("rgba(220,220,0,.5)"));
	private static Text title;
	private static Text scoreVal;
	private static Text roundVal;
	private static Text pause;
	private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), ae -> timerTick()));
	private static SimonUser[] users = new SimonUser[] { new SimonUser("BOB", "1"), new SimonUser("BOB", "1"),
			new SimonUser("BOB", "1"), new SimonUser("BOB", "1"), new SimonUser("BOB", "1"), new SimonUser("BOB", "1"),
			new SimonUser("BOB", "1"), new SimonUser("BOB", "1"), new SimonUser("BOB", "1"),
			new SimonUser("BOB", "1") };
	private static SimonUser user = new SimonUser();

	private void timerTick() {
		time += .1;

		if (time > timeLimit && playerTurn) {
			gameOver();
		} else if (time >= timeLimit && !playerTurn) {
			time = 0;
			selectNextCommand();
		}
		if (time >= timeLimit / 2 && time <= timeLimit / 2 + .1) {
			top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
			right.setFill(Paint.valueOf("rgba(0,100,255,.3)"));
			bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
			left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		}
	}

	private void saveHighScores() {
		File f = new File("simon/highScores.txt");

		String s = "";
		for (int i = 0; i < users.length; i++) {
			if (users[i] != null) {
				s += users[i].getInitials() + " -> " + users[i].getScore().replaceAll("\\r", "") + "\n";
			}
		}
		try {
			ProgramUtil.writeToFile(f.toString(), s);
		} catch (FileNotFoundException e) {

		}
	}

	private String loadHighScores() {
		String s = null;
		File f = new File("simon/highScores.txt");
		try {
			if (f.exists()) {
				s = ProgramUtil.readFile("simon/highScores.txt");
			}
		} catch (NoSuchFileException e1) {

		}

		String[] temp = new String[0];

		temp = s.split("\n");

		int count = 0;
		for (int i = 0; i < temp.length; i += 2) {
			String[] userInfo = new String[2];
			userInfo = temp[i].split(" -> ");
			SimonUser u = new SimonUser(userInfo[0], userInfo[1].replaceAll("\\r", ""));
			users[count++] = u; 
		}

		return s;
	}

	private void sortHighScores() {
		for (int i = 0; i < users.length; i++) {
			for (int y = 0; y < users[i].getScore().length(); y++) {
				if (y < 5 && users[i].getScore().charAt(y) < user.getScore().charAt(y)) {
					if (i != users.length - 1) {
						for (int x = users.length - 1; x > i; x--) {
							users[x] = users[x - 1];
						}
						users[i] = user;
						i = users.length - 1;
						break;
					}
					else {
						users[i] = user;
						i = users.length - 1;
						break;
					}
				}
				else if (y < 5 && users[i].getScore().charAt(y) > user.getScore().charAt(y)) {
					break;
				}
			}
		}
	}

	private Scene createInitScene(String message) {
		BorderPane bp = new BorderPane();
		HBox hbox = new HBox();
		VBox vbox2 = new VBox();

		String f = Integer.toString(score);
		scoreString = scoreString.substring(0, scoreString.length() - f.length());
		scoreString += f;

		Text t = new Text(message);
		t.setStyle("-fx-font: 20 Arial;");
		Text t2 = new Text("Score: " + score);
		t2.setStyle("-fx-font: 15 Arial;");

		vbox2.setSpacing(5);
		vbox2.getChildren().addAll(t, t2);
		vbox2.setAlignment(Pos.CENTER);

		Label l = new Label("Initials: ");
		TextField tf = new TextField();
		tf.setPrefWidth(50);
		tf.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					// Check if the new character is greater than LIMIT
					if (tf.getText().length() >= 3) {

						// if it's 11th character then just setText to previous
						// one
						tf.setText(tf.getText().substring(0, 3));
					}
				}
			}
		});

		Button submit = new Button("Submit");
		submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (tf.getText().isEmpty()) {
					user.setInitials("AAA");
				} else {
					user.setInitials(tf.getText());
				}
				user.setScore(scoreString);
				loadHighScores();
				sortHighScores();
				saveHighScores();
				winScene = createWinScene(message);
				ArcadeView.setScene(winScene);
			}
		});
		hbox.getChildren().addAll(l, tf, submit);
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.CENTER);

		bp.setCenter(hbox);
		bp.setTop(vbox2);

		Scene s = new Scene(bp, 250, 200);
		return s;
	}

	@SuppressWarnings("unchecked")
	private Scene createWinScene(String message) {
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		VBox vbox2 = new VBox();
		TableView<SimonUser> table = new TableView<SimonUser>();

		String f = Integer.toString(score);
		scoreString = scoreString.substring(0, scoreString.length() - f.length());
		scoreString += f;

		Text t = new Text(message);
		t.setStyle("-fx-font: 20 Arial;");
		Text t2 = new Text("Score: " + score);
		t2.setStyle("-fx-font: 15 Arial;");

		vbox2.setSpacing(5);
		vbox2.getChildren().addAll(t, t2);
		vbox2.setAlignment(Pos.CENTER);

		final Label label = new Label("Leaderboards");
		label.setFont(new Font("Arial", 20));

		table.setEditable(true);

		TableColumn<SimonUser, String> inits = new TableColumn<SimonUser, String>("Initials");
		inits.setCellValueFactory(new PropertyValueFactory<SimonUser, String>("Initials"));
		TableColumn<SimonUser, String> scores = new TableColumn<SimonUser, String>("Score");
		scores.setCellValueFactory(new PropertyValueFactory<SimonUser, String>("Score"));

		ObservableList<SimonUser> data = FXCollections.observableArrayList(users);

		table.setPrefHeight(226);
		table.setFixedCellSize(20.0);
		table.setItems(data);
		table.getColumns().addAll(inits, scores);

		final VBox vbox3 = new VBox();
		vbox3.setSpacing(5);
		vbox3.setPadding(new Insets(10, 0, 0, 10));
		vbox3.getChildren().addAll(label, table);

		Button title = new Button("Return to Title Screen");
		title.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				showScene(titleScene);
			}
		});
		Button menu = new Button("Return to Main Menu");
		menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				showScene(MainMenu.getMenuScene());
			}
		});
		Button quit = new Button("Quit Application");
		quit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				exit();
			}
		});

		vbox.setSpacing(10);
		vbox.getChildren().addAll(vbox3, title, menu, quit);
		vbox.setAlignment(Pos.CENTER);

		bp.setTop(vbox2);
		bp.setCenter(vbox);
		// VBox.setMargin(table, new Insets(0,10,0,0));
		BorderPane.setMargin(vbox, new Insets(0, 10, 0, 0));

		Scene s = new Scene(bp, 250, 450);
		return s;
	}

	private void createPauseListener() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode kc = event.getCode();

				if ((kc.equals(KeyCode.ESCAPE) || kc.equals(KeyCode.P)) && !paused && !gameOver) {
					pause();
				} else if ((kc.equals(KeyCode.ESCAPE) || kc.equals(KeyCode.P)) && paused && !gameOver) {
					resume();
				}
			}
		});
	}

	private void resetColor(Rectangle r) {
		if (r.equals(top)) {
			top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));

		} else if (r.equals(right)) {
			right.setFill(Paint.valueOf("rgba(0,100,255,.3)"));

		} else if (r.equals(bottom)) {
			bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));

		} else {
			left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		}
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
		
		loadHighScores();
		Label l = new Label("High Score: " + users[0].getInitials() + " - " + users[0].getScore());
		l.setFont(new Font("Arial", 12));

		top = new Rectangle(50, 50, Paint.valueOf("rgba(255,0,60,.3)"));

		top.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (playerTurn && !paused) {
					top.setFill(Paint.valueOf("rgba(255,0,60,1.0)"));
					checkCorrect(CommandType.RED);
					timeline.play();
					Timeline timeline = new Timeline(new KeyFrame(Duration.millis(70), ae -> resetColor(top)));
					timeline.play();
					
					String musicFile = "simon/audio/SimonRed.mp3";
					Media sound = new Media(new File(musicFile).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.play();
				}
			}
		});
		right = new Rectangle(50, 50, Paint.valueOf("rgba(0,100,255,.3)"));
		right.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (playerTurn && !paused) {
					timeline.play();
					right.setFill(Paint.valueOf("rgba(0,100,255,1.0)"));
					checkCorrect(CommandType.BLUE);
					Timeline timeline = new Timeline(new KeyFrame(Duration.millis(70), ae -> resetColor(right)));
					timeline.play();
					
					String musicFile = "simon/audio/SimonBlue.mp3";
					Media sound = new Media(new File(musicFile).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.play();
				}
			}
		});
		bottom = new Rectangle(50, 50, Paint.valueOf("rgba(0,200,40,.3)"));
		bottom.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (playerTurn && !paused) {
					timeline.play();
					bottom.setFill(Paint.valueOf("rgba(0,200,40,1.0)"));
					checkCorrect(CommandType.GREEN);
					Timeline timeline = new Timeline(new KeyFrame(Duration.millis(70), ae -> resetColor(bottom)));
					timeline.play();
					
					String musicFile = "simon/audio/SimonGreen.mp3";
					Media sound = new Media(new File(musicFile).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.play();
				}
			}
		});
		left = new Rectangle(50, 50, Paint.valueOf("rgba(220,220,0,.3)"));
		left.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (playerTurn && !paused) {
					timeline.play();
					left.setFill(Paint.valueOf("rgba(220,220,0,1.0)"));
					checkCorrect(CommandType.YELLOW);
					Timeline timeline = new Timeline(new KeyFrame(Duration.millis(70), ae -> resetColor(left)));
					timeline.play();
					
					String musicFile = "simon/audio/SimonYellow.mp3";
					Media sound = new Media(new File(musicFile).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.play();
				}
			}
		});

		GridPane.setConstraints(top, 2, 1);
		GridPane.setConstraints(right, 3, 2);
		GridPane.setConstraints(bottom, 2, 3);
		GridPane.setConstraints(left, 1, 2);

		gp.getChildren().addAll(top, right, bottom, left);

		vbox.setSpacing(10);
		vbox.getChildren().addAll(scoreVal, roundVal, pause, l);

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
		BorderPane.setMargin(menu, new Insets(-50, 0, 10, 0));

		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setAlignment(gp, Pos.CENTER_LEFT);
		BorderPane.setAlignment(vbox, Pos.CENTER);
		BorderPane.setAlignment(menu, Pos.TOP_CENTER);

		gameScene = new Scene(bp, 300, 300);
	}

	private void createInfoScene() {
		BorderPane bp = new BorderPane();
		HBox hbox = new HBox();
		VBox vbox1 = new VBox();
		VBox vbox2 = new VBox();

		Text t = new Text("Info / Controls");
		t.setStyle("-fx-font: 22 Arial;");
		Text simonTurn = new Text("\"Simon's Turn\"");
		Text yourTurn = new Text("\"Your Turn\"");
		Text over = new Text("\"Game Over\"");
		Text easy = new Text("Easy Difficulty");
		Text medium = new Text("Medium Difficulty");
		Text hard = new Text("Hard Difficulty");
		Text info1 = new Text(": Pay attention to pattern");
		Text info2 = new Text(": Repeat the pattern");
		Text info3 = new Text(": You lost, return to title screen");
		Text info4 = new Text(": Adds one more command each round");
		Text info5 = new Text(": Same as easy, but speeds up");
		Text info6 = new Text(": Speeds up, and random each round");
		vbox1.setStyle("-fx-font: 16 Arial;");
		vbox2.setStyle("-fx-font: 16 Arial;");

		Button tScreen = new Button("Return to Title Screen");
		tScreen.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				titleScreen();
			}

		});

		vbox1.setSpacing(10);
		vbox2.setSpacing(10);
		vbox1.getChildren().addAll(simonTurn, yourTurn, over, easy, medium, hard);
		vbox1.setAlignment(Pos.CENTER_RIGHT);
		vbox2.getChildren().addAll(info1, info2, info3, info4, info5, info6);
		vbox2.setAlignment(Pos.CENTER_LEFT);

		hbox.getChildren().addAll(vbox1, vbox2);
		HBox.setMargin(vbox1, new Insets(10, 0, 0, 30));
		HBox.setMargin(vbox2, new Insets(10, 0, 0, 10));
		hbox.setAlignment(Pos.CENTER);

		bp.setTop(t);
		bp.setCenter(hbox);
		bp.setBottom(tScreen);
		BorderPane.setAlignment(t, Pos.CENTER);
		BorderPane.setAlignment(hbox, Pos.CENTER);
		BorderPane.setAlignment(tScreen, Pos.CENTER);
		BorderPane.setMargin(tScreen, new Insets(0, 0, 10, 0));
		BorderPane.setMargin(hbox, new Insets(-30, 0, 0, 0));

		infoScene = new Scene(bp, 450, 300);
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

	private void resetColors() {
		top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
		right.setFill(Paint.valueOf("rgba(0,100,255,.3)"));
		bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
		left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		timeline.play();
	}

	private void selectNextCommand() {
		String musicFile = "";
		if (commands.get(currentIndex).equals(CommandType.RED)) {
			musicFile = "simon/audio/SimonRed.mp3";

			Media sound = new Media(new File(musicFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
			top.setFill(Paint.valueOf("rgba(255,0,60,1.0)"));
			right.setFill(Paint.valueOf("rgba(0,100,255,.3)"));
			bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
			left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		} else if (commands.get(currentIndex).equals(CommandType.BLUE)) {
			musicFile = "simon/audio/SimonBlue.mp3";

			
			top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
			right.setFill(Paint.valueOf("rgba(0,100,255,1.0)"));
			bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
			left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		} else if (commands.get(currentIndex).equals(CommandType.GREEN)) {
			musicFile = "simon/audio/SimonGreen.mp3";

			Media sound = new Media(new File(musicFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
			top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
			right.setFill(Paint.valueOf("rgba(0,100,255,.3)"));
			bottom.setFill(Paint.valueOf("rgba(0,200,40,1.0)"));
			left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		} else {
			musicFile = "simon/audio/SimonYellow.mp3";

			Media sound = new Media(new File(musicFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
			top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
			right.setFill(Paint.valueOf("rgba(0,100,255,.3)"));
			bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
			left.setFill(Paint.valueOf("rgba(220,220,0,1.0)"));
		}
		
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();

		if (currentIndex == commands.size() - 1) {
			currentIndex = 0;
			if (playerTurn) {
				createCommands();
			} else {
				if (!playerTurn) {
					title.setText("Your Turn");
					playerTurn = true;
					timeline.pause();
					Timeline timeline = new Timeline(
							new KeyFrame(Duration.millis((timeLimit * 1000) / 2), ae -> resetColors()));
					timeline.play();
				} else {
					playerTurn = false;
					title.setText("Simon's Turn");
				}
			}
		} else {
			currentIndex++;
		}
	}

	private void startGame() {
		timeLimit = 2;
		round = 0;
		time = 0;
		commands.clear();
		score = 0;
		currentIndex = 0;
		title.setText("Simon's Turn");
		roundVal.setText("Round: " + round);
		scoreVal.setText("Points: " + score);
		top.setFill(Paint.valueOf("rgba(255,0,60,.3)"));
		right.setFill(Paint.valueOf("rgba(0,100,255,.3)"));
		bottom.setFill(Paint.valueOf("rgba(0,200,40,.3)"));
		left.setFill(Paint.valueOf("rgba(220,220,0,.3)"));
		timeline = new Timeline(new KeyFrame(Duration.millis(100), ae -> timerTick()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		createCommands();
		createPauseListener();
		createGameScene();
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
		} else if (difficulty.equals(Difficulty.NORMAL)) {
			CommandType d = CommandType.values()[rand.nextInt(CommandType.values().length)];

			commands.add(d);
			if (timeLimit > .5) {
				timeLimit -= .1;
			}
		} else {
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
		} else {
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
		// bp.setStyle("-fx-background: #F70;");
		BorderPane.setAlignment(t, Pos.TOP_CENTER);
		BorderPane.setMargin(vbox, new Insets(0, 0, 0, 0));

		Scene title = new Scene(bp, 300, 200);
		return title;
	}

	@Override
	public void gameOver() {
		gameOver = true;
		timeline.stop();
		playerTurn = false;
		initScene = createInitScene("Game Over");
		showScene(initScene);
	}
}
