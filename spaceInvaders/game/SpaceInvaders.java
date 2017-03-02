package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

import application.ArcadeView;
import application.MainMenu;
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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lib.ProgramUtil;
import models.Barrier;
import models.Bullet;
import models.Character;
import models.Enemy;
import models.Game;
import models.UFO;
import models.User;

public class SpaceInvaders extends Game {

	public static boolean paused = false;
	public static String scoreString = "0000000000";
	public static ArrayList<Bullet> bullets = new ArrayList<>();
	public static ArrayList<Enemy> enemies = new ArrayList<>();
	public static ArrayList<Barrier> barriers = new ArrayList<>();
	public static int score = 0;
	private static Scene titleScene;
	private Scene difficultyScene;
	private static Scene initScene;
	private static Scene winScene;
	private static Scene pauseScene;
	public static Scene gameScene;
	public static Group entities = new Group();
	public static Character player;
	public static UFO ufo;
	private Scene infoScene;
	//private Scene overScene;
	public static KeyPressHandler kp;
	public static MovementHandler mh;
	public static Label scorel;
	public static Label livesl;
	private Timeline timeline;
	private static User[] users = new User[] {
			new User("BOB", "11"),
			 new User("CUK", "10"),
			 new User("DOG", "9"),
			 new User("CAT", "8"),
			 new User("RAT", "7"),
			 new User("FAT", "6"),
			 new User("GOT", "5"),
			 new User("YOT", "4"),
			 new User("HOT", "3"),
			 new User("DOT", "1")
	};
	private static User user = new User();
	
	private void timerTick() {
		if (player.getLives() <= 0) {
			player = new Character(0,0);
			gameOver();
			timeline.stop();
		}
	}
	
	public static void continueGame(int playerLives) {
		int x = 50;
		int y = 50;
		for (int i = 1; i < 51; i++) {
			Image img = new Image("file:spaceInvaders/images/Enemy.png");
			if (i < 11) {
				img = new Image("file:spaceInvaders/images/EnemyRound.png");
			}
			else if (i < 21) {
				img = new Image("file:spaceInvaders/images/EnemySun.png");
			}
			else if (i < 31) {
				img = new Image("file:spaceInvaders/images/EnemyTriangle.png");
			}
			else if (i < 41) {
				img = new Image("file:spaceInvaders/images/EnemyX.png");
			}
			else{
				img = new Image("file:spaceInvaders/images/Enemy.png");
			}
			
			mh.timeLimit += 100;
			Enemy e = new Enemy(x, y, img);
			enemies.add(e);
			entities.getChildren().add(e.getImg());
			x += e.getWidth() + 5;
			if (i % 10 == 0 && i != 0) {
				y += e.getWidth() + 3;
				x = 50;
			}
		}
		if (playerLives <= 0) {
			player.setLives(1);
		}
		else {
			player.setLives(playerLives);
		}
	}
	
	private static void saveHighScores() {
		File f = new File("spaceInvaders/highScores.txt");
		
		String s = "";
		for (int i = 0; i < users.length; i++) {
			if (users[i] != null) {
				s += users[i].getInitials() + " -> " + users[i].getScore() + "\n";
			}
		}
		try {
			ProgramUtil.writeToFile(f.toString(), s);
		} catch (FileNotFoundException e) {
			
		}
	}
	
	private static String loadHighScores() {
		String s = null;
		File f = new File("spaceInvaders/highScores.txt");
		try {
			if (f.exists()) {
				s = ProgramUtil.readFile("spaceInvaders/highScores.txt");
			}
		} catch (NoSuchFileException e1) {
			
		}
		
		String[] temp = new String[0];
		
		temp = s.split("\n");
		
		int count = 0;
		for (int i = 0; i < temp.length; i += 2) {
			String[] userInfo = new String[2];
			userInfo = temp[i].split(" -> ");
			User u = new User(userInfo[0], userInfo[1]);
			users[count++] = u;
		}
		
		return s;
	}
	
	private static void sortHighScores() {
			for (int i = 0; i < users.length; i++) {
				for (int y = 0; y < users[i].getScore().length(); y++) {
					if (y < 10 && users[i].getScore().charAt(y) < user.getScore().charAt(y)) {
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
					else if (users[i].getScore().charAt(y) > user.getScore().charAt(y)) {
						break;
					}
				}
			}
	}
	
	private static Scene createInitScene(String message) {
		BorderPane bp = new BorderPane();
		HBox hbox = new HBox();
		VBox vbox2 = new VBox();
		
		String f = Integer.toString(SpaceInvaders.score);
		SpaceInvaders.scoreString = SpaceInvaders.scoreString.substring(0, SpaceInvaders.scoreString.length() - f.length());
		SpaceInvaders.scoreString += f;
		
		Text t = new Text(message);
		t.setStyle("-fx-font: 20 Arial;");
		Text t2 = new Text("Score: " + scoreString);
		t2.setStyle("-fx-font: 15 Arial;");
		
		vbox2.setSpacing(5);
		vbox2.getChildren().addAll(t, t2);
		vbox2.setAlignment(Pos.CENTER);
		
		Label l = new Label("Initials: ");
		TextField tf = new TextField();
		tf.setPrefWidth(50);
		tf.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
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
				}
				else {
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
	private static Scene createWinScene(String message) {
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		VBox vbox2 = new VBox();
		TableView<User> table = new TableView<User>();
		
		String f = Integer.toString(SpaceInvaders.score);
		SpaceInvaders.scoreString = SpaceInvaders.scoreString.substring(0, SpaceInvaders.scoreString.length() - f.length());
		SpaceInvaders.scoreString += f;
		
		Text t = new Text(message);
		t.setStyle("-fx-font: 20 Arial;");
		Text t2 = new Text("Score: " + scoreString);
		t2.setStyle("-fx-font: 15 Arial;");
		
		vbox2.setSpacing(5);
		vbox2.getChildren().addAll(t, t2);
		vbox2.setAlignment(Pos.CENTER);
 
        final Label label = new Label("Leaderboards");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);
 
        TableColumn<User, String> inits = new TableColumn<User, String>("Initials");
        inits.setCellValueFactory(
                new PropertyValueFactory<User, String>("Initials"));
        TableColumn<User, String> scores = new TableColumn<User, String>("Score");
        scores.setCellValueFactory(
                new PropertyValueFactory<User, String>("Score"));
        
        ObservableList<User> data = FXCollections.observableArrayList(users);
        
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
				kp.stopTimer();
				mh.stopTimer();
				ArcadeView.setScene(titleScene);
			}
		});
		Button menu = new Button("Return to Main Menu");
		menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				ArcadeView.setScene(MainMenu.getMenuScene());
			}
		});
		Button quit = new Button("Quit Application");
		quit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				System.exit(0);
			}
		});
		
		vbox.setSpacing(10);
		vbox.getChildren().addAll(vbox3, title, menu, quit);
		vbox.setAlignment(Pos.CENTER);
		
		bp.setTop(vbox2);
		bp.setCenter(vbox);
		//VBox.setMargin(table, new Insets(0,10,0,0));
		BorderPane.setMargin(vbox, new Insets(0,10,0,0));
		
		Scene s = new Scene(bp, 250, 450);
		return s;
	}
	
	private Scene createPauseScene() {
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		
		Text t = new Text("Game Paused");
		t.setStyle("-fx-font: 20 Arial;");
		
		Button resume = new Button("Resume Game");
		resume.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				kp.leftAdded = false;
				kp.rightAdded = false;
				kp.resumeTimer();
				mh.resumeTimer();
				showScene(gameScene);
				paused = false;
			}
		});
		Button title = new Button("Return to Title Screen");
		title.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				kp.stopTimer();
				mh.stopTimer();
				showScene(titleScene);
				paused = false;
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
		vbox.getChildren().addAll(resume, title, quit);
		vbox.setAlignment(Pos.CENTER);
		
		bp.setCenter(vbox);
		bp.setTop(t);
		
		Scene s = new Scene(bp , 200, 200);
		return s;
	}
	
	private Scene createInfoScene() {
		BorderPane bp = new BorderPane();
		HBox hbox = new HBox();
		VBox vbox1 = new VBox();
		VBox vbox2 = new VBox();
		
		Text t = new Text("Info / Controls");
		t.setStyle("-fx-font: 20 Arial;");
		
		Button left = new Button("A | <Left Arrow Key>");
		left.setStyle("-fx-background-color: #EEE;");
		Text leftInfo = new Text(": Move character left");
		leftInfo.setStyle("-fx-color: white;");
		Button right = new Button("D | <Right Arrow Key>");
		right.setStyle("-fx-background-color: #EEE;");
		Text rightInfo = new Text(": Move character right");
		Button shoot = new Button("Spacebar");
		shoot.setStyle("-fx-background-color: #EEE;");
		Text shootInfo = new Text(": Fire missile");
		Button pause = new Button("Escape | P");
		pause.setStyle("-fx-background-color: #EEE;");
		Text pauseInfo = new Text(": Pause game");
		Image you = new Image("file:spaceInvaders/images/Spaceship.png");
		ImageView youIV = new ImageView(you);
		youIV.setFitWidth(35);
		youIV.setFitHeight(35);
		Text youInfo = new Text(": Your character");
		Image them = new Image("file:spaceInvaders/images/Enemy.png");
		ImageView themIV = new ImageView(them);
		themIV.setFitWidth(35);
		themIV.setFitHeight(35);
		Text themInfo = new Text(": The enemy");
		
		Button title = new Button("Return to Title Screen");
		title.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				titleScreen();
			}
		});
		
		vbox1.setSpacing(10);
		vbox2.setSpacing(21.5);
		vbox1.setAlignment(Pos.TOP_RIGHT);
		vbox1.getChildren().addAll(left, right, shoot, pause, youIV, themIV);
		vbox2.getChildren().addAll(leftInfo, rightInfo, shootInfo, pauseInfo, youInfo, themInfo);
		vbox2.setStyle("-fx-color: #FFF;");
		
		hbox.getChildren().addAll(vbox1, vbox2);
		hbox.setAlignment(Pos.TOP_CENTER);
		HBox.setMargin(vbox2, new Insets(0,0,0,5));
		HBox.setMargin(vbox1, new Insets(0,5,0,0));
		
		bp.setTop(t);
		bp.setCenter(hbox);
		bp.setBottom(title);
		
		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setAlignment(t, Pos.CENTER);
		BorderPane.setAlignment(hbox, Pos.CENTER);
		BorderPane.setMargin(hbox, new Insets(15, 0, 0, 0));
		BorderPane.setMargin(title, new Insets(0, 0, 10, 0));
		
		bp.setStyle("-fx-background-color: #666;");
		
		Scene s = new Scene(bp, 300, 350);
		return s;
	}
	
	private Scene createDifficultyScene() {
		BorderPane bp = new BorderPane();
		
		VBox vbox = new VBox();
		
		Text t = new Text("Select a Difficulty");
		t.setStyle("-fx-font: 20 Arial;");
		
		Button easy = new Button("Easy");
		easy.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				startGame(1500, 551);
				mh.difficulty = "easy";
			}
		});
		Button medium = new Button("Medium");
		medium.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				startGame(1000, 451);
				mh.difficulty = "medium";
			}
		});
		Button hard = new Button("Hard");
		hard.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				startGame(500, 351);
				mh.difficulty = "hard";
			}
		});
		
		vbox.setSpacing(10);
		vbox.getChildren().addAll(easy, medium, hard);
		vbox.setAlignment(Pos.CENTER);
		
		bp.setTop(t);
		bp.setCenter(vbox);
		BorderPane.setAlignment(t, Pos.CENTER);
		BorderPane.setMargin(vbox, new Insets(0,0,0,0));
		
		Scene s = new Scene(bp, 200, 150);
		return s;
	}
	
	private static Scene createGameScene() {
		Pane bp = new Pane();
		entities = new Group();
		scorel = new Label("SCORE: " + scoreString);
		scorel.setTextFill(Color.web("#FFF"));
		scorel.setTextAlignment(TextAlignment.RIGHT);
		scorel.setPrefWidth(150);
		barriers.clear();
		bullets.clear();
		enemies.clear();
		
		loadHighScores();
		Label l = new Label("HIGH SCORE: " + users[0].getScore());
		l.setTextFill(Color.WHITE);
		
		int x = 50;
		int y = 50;
		for (int i = 1; i < 51; i++) {
			Image img = new Image("file:spaceInvaders/images/Enemy.png");
			if (i < 11) {
				img = new Image("file:spaceInvaders/images/EnemyRound.png");
			}
			else if (i < 21) {
				img = new Image("file:spaceInvaders/images/EnemySun.png");
			}
			else if (i < 31) {
				img = new Image("file:spaceInvaders/images/EnemyTriangle.png");
			}
			else if (i < 41) {
				img = new Image("file:spaceInvaders/images/EnemyX.png");
			}
			else{
				img = new Image("file:spaceInvaders/images/Enemy.png");
			}
			
			Enemy e = new Enemy(x, y, img);
			enemies.add(e);
			entities.getChildren().add(e.getImg());
			x += e.getWidth() + 5;
			if (i % 10 == 0 && i != 0) {
				y += e.getWidth() + 3;
				x = 50;
			}
		}
		
		x = 51;
		y = 350;
		for (int i = 0; i < 3; i++) {
			Barrier b = new Barrier(x, y);
			barriers.add(b);
			entities.getChildren().add(b.getImg());
			x += b.getWidth() + 51;
		}
		
		player = new Character(200, 450);
		player.setX(player.getX() - player.getWidth() / 2);
		player.setY(player.getY() - player.getHeight() - 2);
		entities.getChildren().add(player.getImg());
		
		livesl = new Label("LIVES: " + player.getLives());
		livesl.setTextFill(Color.web("#FFF"));
		
		bp.getChildren().addAll(entities, scorel, livesl, l);
		bp.setStyle("-fx-background-color: #000;");
		
		
		scorel.setLayoutX(420 - scorel.getPrefWidth());
		scorel.setLayoutY(0);
		livesl.setLayoutX(0);
		livesl.setLayoutY(0);
		l.setLayoutX(100);
		l.setLayoutY(0);
		
		Scene s = new Scene(bp, 400, 450);
		return s;
	}
	
//	private Scene createOverScene(String titleText) {
//		saveHighScores();
//		loadHighScores();
//		sortHighScores();
//		BorderPane bp = new BorderPane();
//		VBox vbox = new VBox();
//		VBox vbox2 = new VBox();
//		HBox hbox = new HBox();
//		
//		String f = Integer.toString(SpaceInvaders.score);
//		SpaceInvaders.scoreString = SpaceInvaders.scoreString.substring(0, SpaceInvaders.scoreString.length() - f.length());
//		SpaceInvaders.scoreString += f;
//		
//		Text t = new Text(titleText);
//		t.setStyle("-fx-font: 20 Arial;");
//		Text t2 = new Text("Score: " + scoreString);
//		t2.setStyle("-fx-font: 15 Arial;");
//		
//		Label l = new Label("Initials: ");
//		TextField tf = new TextField();
//		tf.setPrefWidth(50);
//		tf.lengthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable,
//                    Number oldValue, Number newValue) {
//                if (newValue.intValue() > oldValue.intValue()) {
//                    // Check if the new character is greater than LIMIT
//                    if (tf.getText().length() >= 3) {
//
//                        // if it's 11th character then just setText to previous
//                        // one
//                        tf.setText(tf.getText().substring(0, 3));
//                    }
//                }
//            }
//        });
//		
//		Button submit = new Button("Submit");
//		submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent arg0) {
//				vbox.getChildren().remove(hbox);
//				Label l = new Label("Score Submitted!");
//				l.setTextFill(Color.RED);
//				vbox.getChildren().add(0, l);
//				user.setInitials(tf.getText());
//				user.setScore(scoreString);
//			}
//		});
//		hbox.getChildren().addAll(l, tf, submit);
//		hbox.setSpacing(5);
//		hbox.setAlignment(Pos.CENTER);
//		
//		vbox2.setSpacing(5);
//		vbox2.getChildren().addAll(t, t2);
//		vbox2.setAlignment(Pos.CENTER);
//		
//		Button title = new Button("Return to Title Screen");
//		title.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent arg0) {
//				showScene(titleScene);
//			}
//		});
//		Button menu = new Button("Return to Main Menu");
//		menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent arg0) {
//				mainMenu();
//			}
//		});
//		Button quit = new Button("Quit Application");
//		quit.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent arg0) {
//				exit();
//			}
//		});
//		
//		vbox.setSpacing(10);
//		vbox.getChildren().addAll(hbox, title, menu, quit);
//		vbox.setAlignment(Pos.CENTER);
//		
//		bp.setTop(vbox2);
//		bp.setCenter(vbox);
//		
//		Scene s = new Scene(bp, 250, 200);
//		return s;
//	}
	
	@Override
	public void resume() {
		
	}

	@Override
	public void exit() {
		System.exit(0);
	}

	@Override
	public void mainMenu() {
		showScene(MainMenu.getMenuScene());
	}

	@Override
	public void titleScreen() {
		showScene(titleScene);
	}
	
	public static void pauseGame() {
		if (paused) {
			kp.leftAdded = false;
			kp.rightAdded = false;
			kp.resumeTimer();
			mh.resumeTimer();
			ArcadeView.setScene(gameScene);
		}
		else {
			kp.pauseTimer();
			mh.pauseTimer();
			ArcadeView.setScene(pauseScene);
		}
		paused = !paused;
	}
	
	private void startGame(int shotLimit, int timeLimit) {
		livesl.setText("LIVES: 3");
		scoreString = "0000000000";
		scorel.setText("SCORE: " + scoreString);
		score = 0;
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(250),
		        ae -> timerTick()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		gameScene = createGameScene();
		showScene(gameScene);
		
		kp = new KeyPressHandler();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			
		}
		mh = new MovementHandler();
		mh.shotLimit = shotLimit;
		mh.timeLimit = timeLimit;
	}
	
	public static void winGame(String message) {
		kp.stopTimer();
		mh.stopTimer();
		initScene = createInitScene(message);
		ArcadeView.setScene(initScene);
	}

	@Override
	public void play() {
		titleScene = createTitleScene();
		gameScene = createGameScene();
		infoScene = createInfoScene();
		difficultyScene = createDifficultyScene();
		//overScene = createOverScene("Game Over - You Lose");
		pauseScene = createPauseScene();
		pauseScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.P) || event.getCode().equals(KeyCode.ESCAPE)) {
					kp.leftAdded = false;
					kp.rightAdded = false;
					mh.resumeTimer();
					kp.resumeTimer();
					showScene(gameScene);
				}
			}
		});
		
//		String musicFile = "spaceInvaders/audio/01 Monody (feat. Laura Brehm).mp3";     // For example
//
//		Media sound = new Media(new File(musicFile).toURI().toString());
//		MediaPlayer mediaPlayer = new MediaPlayer(sound);
//		mediaPlayer.play();
		
		titleScreen();
	}

	@Override
	public Scene createTitleScene() {
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		
		Text t = new Text("Space Invaders");
		t.setStyle("-fx-font: 25 Arial");
		
		Button start = new Button("Start Game");
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				showScene(difficultyScene);
			}
		});
		Button info = new Button("Info/Controls");
		info.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				showScene(infoScene);
			}
		});
		Button menu = new Button("Return to Main Menu");
		menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mainMenu();
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
		vbox.getChildren().addAll(start, info, menu, quit);
		
		bp.setTop(t);
		bp.setCenter(vbox);
		vbox.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(vbox, Pos.CENTER);
		BorderPane.setAlignment(t, Pos.CENTER);
		BorderPane.setMargin(vbox, new Insets(-25, 0, 0, 0));
		
		Scene s = new Scene(bp, 250, 250);
		return s;
	}

	@Override
	public void gameOver() {
		kp.stopTimer();
		mh.stopTimer();
		initScene = createInitScene("Game Over - You Lose");
		showScene(initScene);
	}

	@Override
	public void pause() {
		
	}

}
