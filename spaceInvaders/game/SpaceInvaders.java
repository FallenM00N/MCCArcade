package game;

import java.util.ArrayList;

import application.MainMenu;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import models.Barrier;
import models.Bullet;
import models.Enemy;
import models.Game;
import models.Character;

public class SpaceInvaders extends Game {

	public static String scoreString = "0000000000";
	public static ArrayList<Bullet> bullets = new ArrayList<>();
	public static ArrayList<Enemy> enemies = new ArrayList<>();
	public static ArrayList<Barrier> barriers = new ArrayList<>();
	public static int score = 0;
	private Scene titleScene;
	private Scene difficultyScene;
	public static Scene gameScene;
	public static Group entities = new Group();
	public static Character player;
	private Scene infoScene;
	private Scene overScene;
	public KeyPressHandler kp;
	public MovementHandler mh;
	public static Label scorel;
	public static Label livesl;
	
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
		return null;
	}
	
	private Scene createGameScene() {
		Pane bp = new Pane();
		entities = new Group();
		scorel = new Label("SCORE: " + scoreString);
		scorel.setTextFill(Color.web("#FFF"));
		scorel.setTextAlignment(TextAlignment.RIGHT);
		scorel.setPrefWidth(150);
		
		int x = 50;
		int y = 50;
		for (int i = 1; i < 51; i++) {
			Enemy e = new Enemy(x, y);
			enemies.add(e);
			entities.getChildren().add(e.getImg());
			x += e.getWidth() + 5;
			if (i % 10 == 0 && i != 0) {
				y += e.getWidth() + 2;
				x = 50;
			}
		}
		
		x = 40;
		y = 350;
		for (int i = 0; i < 3; i++) {
			Barrier b = new Barrier(x, y);
			barriers.add(b);
			entities.getChildren().add(b.getImg());
			x += b.getWidth() + 40;
		}
		
		player = new Character(200, 450);
		player.setX(player.getX() - player.getWidth() / 2);
		player.setY(player.getY() - player.getHeight() - 2);
		entities.getChildren().add(player.getImg());
		
		livesl = new Label("LIVES: " + player.getLives());
		livesl.setTextFill(Color.web("#FFF"));
		
		bp.getChildren().addAll(entities, scorel, livesl);
		bp.setStyle("-fx-background-color: #000;");
		
		scorel.setLayoutX(400 - scorel.getPrefWidth());
		scorel.setLayoutY(0);
		livesl.setLayoutX(0);
		livesl.setLayoutY(0);
		
		Scene s = new Scene(bp, 400, 450);
		return s;
	}
	
	private Scene createOverScene() {
		return null;
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
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
	
	private void startGame() {
		kp = new KeyPressHandler();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mh = new MovementHandler();
	}

	@Override
	public void play() {
		titleScene = createTitleScene();
		gameScene = createGameScene();
		infoScene = createInfoScene();
		difficultyScene = createDifficultyScene();
		overScene = createOverScene();
		
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
				showScene(gameScene);
				startGame();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

}
