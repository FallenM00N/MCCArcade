package models;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Character {
	private int lives = 3;
	private final Group img;
	private ImageView iv;
	private Image imgSrc;
	private double x = 0;
	private double y = 0;
	private double width = 35;
	private double height = 35;
	
	public Character(double x, double y) {
		setX(x);
		setY(y);
		
		imgSrc = new Image("file:spaceInvaders/images/Spaceship.png");
		iv = new ImageView();
		iv.setImage(imgSrc);
		iv.setFitWidth(getWidth());
		iv.setFitHeight(getHeight());
		img = new Group();
		img.getChildren().add(iv);
		img.setLayoutX(getX());
		img.setLayoutY(getY());
	}
	
	public void loseLife(int livesLost) {
		lives -= livesLost;
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
			img.setLayoutX(getX());
		}
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		if (img != null) {
			img.setLayoutY(getY());
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

	public Image getImgSrc() {
		return imgSrc;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}
}
