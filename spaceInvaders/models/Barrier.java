package models;

import java.io.File;

import game.SpaceInvaders;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Barrier {
	private final Group img;
	private ImageView iv;
	private Image imgSrc;
	private double x = 0;
	private double y = 0;
	private double width = 80;
	private double height = 40;
	private int hitPoints = 80;
	private Rectangle pBar;
	private Rectangle pBarEmpty;
	
	public Barrier(double x, double y) {
		setX(x);
		setY(y);
		
		imgSrc = new Image("file:spaceInvaders/images/Barrier.png");
		iv = new ImageView();
		iv.setImage(imgSrc);
		iv.setFitWidth(getWidth());
		iv.setFitHeight(getHeight());
		img = new Group();
		img.getChildren().add(iv);
		img.setLayoutX(getX());
		img.setLayoutY(getY());
		
		pBarEmpty = new Rectangle(width, 10, Paint.valueOf("rgba(0,60,255,.6)"));
		img.getChildren().add(pBarEmpty);
		pBarEmpty.setLayoutX(0);
		pBarEmpty.setLayoutY(-15);
		
		pBar = new Rectangle(width, 10, Paint.valueOf("rgba(255,0,20,.6)"));
		img.getChildren().add(pBar);
		pBar.setLayoutX(0);
		pBar.setLayoutY(-15);
	}
	
	public void takeDamage(int damage) {
		setHitPoints(getHitPoints() - damage);
		pBar.setWidth(pBar.getWidth() - damage);
		if (getHitPoints() <= 0) {
			playDeathAnimation();
		}
	}
	
	public void playDeathAnimation() {
		Timeline timeline = new Timeline(new KeyFrame(
		        Duration.millis(100),
		        ae -> timerTick()));
		Image i = new Image("file:spaceInvaders/images/BarrierExplode.png");
		setImgSrc(i);
		ImageView iv = new ImageView(i);
		iv.setFitWidth(getWidth());
		iv.setFitHeight(getHeight());
		setIv(iv);
		img.getChildren().clear();
		img.getChildren().add(getIv());
		img.setLayoutX(getX());
		img.setLayoutY(getY());
		
		String musicFile = "spaceInvaders/audio/Boom.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		
		timeline.play();
	}
	
	private void timerTick() {
		SpaceInvaders.entities.getChildren().remove(this.getImg());
		SpaceInvaders.barriers.remove(this);
	}

	public Group getImg() {
		return img;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		if (img != null) {
			img.setLayoutX(this.x);
			pBar.setLayoutX(getX());
		}
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		if (img != null) {
			img.setLayoutY(this.y);
			pBar.setLayoutY(getY());
		}
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public ImageView getIv() {
		return iv;
	}
	
	public void setIv(ImageView iv) {
		this.iv = iv;
	}

	public Image getImgSrc() {
		return imgSrc;
	}
	
	public void setImgSrc(Image img) {
		this.imgSrc = img;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}
}
