package models;

import game.SpaceInvaders;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Enemy {

	private final Group img;
	private ImageView iv;
	private Image imgSrc;
	private double x = 0;
	private double y = 0;
	private double width = 25;
	private double height = 25;
	
	public Enemy(double x, double y, Image image) {
		setX(x);
		setY(y);
		
		imgSrc = image;
		iv = new ImageView();
		iv.setImage(imgSrc);
		iv.setFitWidth(getWidth());
		iv.setFitHeight(getHeight());
		img = new Group();
		img.getChildren().add(iv);
		img.setLayoutX(getX());
		img.setLayoutY(getY());
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
		}
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		if (img != null) {
			img.setLayoutY(this.y);
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
		imgSrc = img;
	}
	
	public void playDeathAnimation() {
		Timeline timeline = new Timeline(new KeyFrame(
		        Duration.millis(100),
		        ae -> timerTick()));
		Image i = new Image("file:spaceInvaders/images/Explode.png");
		setImgSrc(i);
		ImageView iv = new ImageView(i);
		iv.setFitWidth(getWidth());
		iv.setFitHeight(getHeight());
		setIv(iv);
		img.getChildren().clear();
		img.getChildren().add(getIv());
		img.setLayoutX(getX());
		img.setLayoutY(getY());
		timeline.play();
	}
	
	private void timerTick() {
		SpaceInvaders.entities.getChildren().remove(this.getImg());
		SpaceInvaders.enemies.remove(this);
	}
}
