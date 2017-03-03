package application;

import java.io.File;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.JumperScore;



public class JumperEngine extends Jumper{
	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;
	private static boolean isJumping = false;

	private static Scene gameScene;
	private static boolean isRunning = false;
	private static Image image;
	private static ImageView llama;
	private static Group root;
	private static AnimationTimer timer;
	private static Timeline timeline = new Timeline();
	private static Jumper j = new Jumper();
	private static JumperScore js = new JumperScore();
	private static Text t = new Text();
	private static Text m = new Text();
	
	private static int yJumpMotion;
	private static AudioClip song;
	private static AudioClip sound;
	private static ImageView pickle;
	private static boolean didCollide = false;
	public static Random rand = new Random();
	public static int speed = 12;
	public static Cloud[] clouds;
	public static int pickleSpawn;
	
	public static void run(){
		startGame();
		showScene();

	}
	
	public static void showScene(){
		
			if(isRunning){
				ArcadeView.setScene(gameScene, "Llama Run");			
			}
			else{
				ArcadeView.setScene(gameOverScene);
			}
			
		
	}
	
	public static void createBackground(){
		
		root = new Group();
		gameScene = new Scene(root, WIDTH, HEIGHT, Color.TRANSPARENT);
		
		
		Rectangle sky = new Rectangle(WIDTH, HEIGHT);
		sky.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0, Color.DODGERBLUE), new Stop(0.7, Color.LIGHTSKYBLUE), new Stop(1.0, Color.POWDERBLUE)));
		root.getChildren().add(sky);
		
        Rectangle ground = new Rectangle(WIDTH, 100);

        ground.setTranslateY(400);
        ground.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.2, Color.DARKGREEN), new Stop(0.1, Color.SEAGREEN)));
        root.getChildren().add(ground);
        
		String musicFile = "jumper\\models\\jumperSong.mp3";
		song = new AudioClip(new File(musicFile).toURI().toString());		
			song.play();
			
			t = new Text(310,90,"0");

			t.setFill(Color.WHITE);
			t.setFont(Font.font(java.awt.Font.SANS_SERIF, 90));
			root.getChildren().add(t);
			
			clouds = new Cloud[]{new Cloud(100, 200), new Cloud(170, 60), new Cloud(220, 150), new Cloud(290, 210), new Cloud(370, 75), new Cloud(450, 200), new Cloud(600, 30), new Cloud(550, 120)};
	        root.getChildren().addAll(clouds);
			
		image = new Image("file:jumper/models/pickle.png");
		pickle = new ImageView();
		pickle.setImage(image);
		pickle.setTranslateX(600);
		pickle.setTranslateY(300);
		pickle.setFitHeight(110);
		pickle.setFitWidth(110);
		root.getChildren().add(pickle);
	}
	
	  static class Cloud extends Ellipse {

	        public Cloud(double centerX, double centerY) {
	            super(0, 0, 50, 25);
	            this.setTranslateX(centerX);
	            this.setTranslateY(centerY);
	            this.setFill(Color.WHITESMOKE);
	            this.setOpacity(0.5);
	        }
	    }
	
	public static void createJumper(){
		image = new Image("file:jumper/models/llama.png");
		llama = new ImageView();
		llama.setImage(image);
		llama.setFitWidth(140);
		llama.setFitHeight(160);
		llama.setLayoutX(50);
		llama.setLayoutY(255);
		root.getChildren().add(llama);
	}
	
	public static void jump(){ 
		yJumpMotion = 315;//adjust jumpheight
		if(isRunning){
			llama.setTranslateY((llama.getTranslateY() - yJumpMotion) - 10);
			
		}
	}
	
	public static void createKeyListener(){
		gameScene.setOnKeyPressed(event -> {
			
			if (event.getCode().equals(KeyCode.SPACE) && isRunning) {
				
				if(!isJumping){
					jump();
					System.out.println("Jump");
					//donothing
					String jumpSound = "jumper\\models\\jumpSound.mp3";
					sound = new AudioClip(new File(jumpSound).toURI().toString());		
						sound.play();
				}
			}
			else if (event.getCode().equals(KeyCode.ESCAPE) && isRunning) {

				pauseJumper();
				j.pause();
				
			}
			
		});
	}
	
	
	public static void createJumperContent(){
		createBackground();
		createJumper();

	}
	
	public static void detectCollision(){
		if(pickle.getBoundsInParent().intersects(llama.getBoundsInParent())){
			System.out.println("collision");
//			String loseSound = "jumper\\models\\loseSound.mp3";
//			sound = new AudioClip(new File(loseSound).toURI().toString());		
//			//sound.play();

			llama.setImage(image);
			j.gameOver();
				
		}
	}
	
	
	
	public static void startGame(){
		speed = 12;
		isRunning = true;
		createJumperContent();
		createKeyListener();
		timer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
			
				createKeyListener();
				
				for(int i = 0; i < clouds.length; i++){
					clouds[i].setTranslateX(clouds[i].getTranslateX() - 5);
					if(clouds[i].getTranslateX() == -50){
						clouds[i].setTranslateX(750);
					}
				}
				
				detectCollision();
				if(llama.getTranslateY()<0){ //if llama is in the air
					isJumping = true;
					llama.setTranslateY(llama.getTranslateY() + 11);//bring back llama to ground
				} 
				else{
					isJumping = false;
				}
				//move rectangles to the left put code in here.
				if(isRunning){
					pickle.setTranslateX(pickle.getTranslateX() - speed);	
					if(pickle.getTranslateX() <= 0){
						js.setScore(js.getScore() + 1);
						t.setFill(Color.WHITE);
						t.setFont(Font.font(java.awt.Font.SANS_SERIF, 90));
						String score = Integer.toString(js.getScore());
						t.setText(score);
						
						pickleSpawn = rand.nextInt(700) + 600;
						pickle.setTranslateX(pickleSpawn);

						if(js.getScore() >= 10){
							speed = 13;
							t.setFill(Color.RED);

						}
						if(js.getScore() >= 30){
							speed = 14;
							t.setFill(Color.CORAL);
						}
						if(js.getScore() >= 40){
							speed = 15;
							t.setFill(Color.YELLOW);
						}
						if(js.getScore() >= 50){
							speed = 16;
							t.setFill(Color.LIMEGREEN);
						}
						if(js.getScore() >= 60){
							speed = 17;
							t.setFill(Color.MEDIUMBLUE);
						}
						if(js.getScore() >= 70){
							speed = 18;
							t.setFill(Color.DARKVIOLET);
						}
						if(js.getScore() >= 80){
							speed = 19;
							t.setFill(Color.DARKTURQUOISE);
						}
						if(js.getScore() >= 100){
							speed = 20;
							t.setFill(Color.CRIMSON);
							
						}
					}
				}
			}
		};
		timer.start();
		timeline.play();
	}
	
	public static void stopGame(){
		song.stop();
		timeline.stop();
		timeline = new Timeline();
		timer.stop();
		isRunning = false;
		js.setScore(0);
	}
	
	public static void restartGame() throws Exception{
		run();
	}
	
	public static void pauseJumper(){
		timeline.pause();
		timer.stop();
	}
	
	public static void resumeJumper(){
		ArcadeView.setScene(gameScene , "Llama Run");
		timeline.play();
		timer.start();
	}
		
	
}
