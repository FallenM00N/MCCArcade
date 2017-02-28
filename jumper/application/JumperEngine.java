package application;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

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
import javafx.scene.shape.Rectangle;



public class JumperEngine extends Jumper{
	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;
	private static boolean isJumping = false;

	private static Scene gameScene;
	private static boolean isRunning = false;
	private static Image image;
	private static ImageView llama;
	private static Group root;
	private static Timer timer = new Timer();
	private static Timeline timeline = new Timeline();
	private static Jumper j = new Jumper();
	
	private static int yJumpMotion;
	private static AudioClip sound;
	
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
		sound = new AudioClip(new File(musicFile).toURI().toString());		
			sound.play();
        
	}
	
	public static void createJumper(){
		image = new Image("file:jumper/models/llama.png");
		llama = new ImageView();
		llama.setImage(image);
		llama.setFitWidth(140);
		llama.setFitHeight(160);
		llama.setLayoutX(150);
		llama.setLayoutY(255);
		root.getChildren().add(llama);
	}
	
	public static void jump(){
		//llama.setTranslateY(yJumpMotion); 
		yJumpMotion = 150;//adjust jumpheight
		if(isRunning){
			llama.setTranslateY(llama.getTranslateY() - yJumpMotion);
		}
	}
	
	public static void createKeyListener(){
		gameScene.setOnKeyPressed(event -> {
			
			if (event.getCode().equals(KeyCode.SPACE) && isRunning) {
				
				if(!isJumping){
					jump();
					System.out.println("Jump");
					//donothing
				}
			}
			else if (event.getCode().equals(KeyCode.ESCAPE) && isRunning) {
				//for pause later
				pauseJumper();
				j.pause();
				
			}
			
		});
	}
	
	
	public static void createJumperContent(){
		createBackground();
		createJumper();
	}
	
	public static void startGame(){
		isRunning = true;
		createJumperContent();
		createKeyListener();
		timer.schedule(new UpdateHandler(llama), 16, 16);
		timeline.play();
	}
	
	private static class UpdateHandler extends TimerTask{

		private ImageView llama;
		

		public UpdateHandler(ImageView llama){
			this.llama = llama;
		}
		
		@Override
		public void run() {
//			System.out.println(llama.getTranslateY()); 
			if(llama.getTranslateY()<0){ //if llama is in the air
				isJumping = true;
				llama.setTranslateY(llama.getTranslateY() + 10);//bring back llama to ground
			} 
			else{
				isJumping = false;
			}
			//move rectangles to the left put code in here.
		}
		
	}
	
	public static void pauseJumper(){
		timeline.pause();
	}
	
	public static void resumeJumper(){
		ArcadeView.setScene(gameScene , "Llama Run");
		timeline.play();
	}
	
	
	
}
