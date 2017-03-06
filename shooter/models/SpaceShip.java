package models;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class SpaceShip {
	private double x;
	private double y;
	private double speed;
	private ArrayList<NeuBullet> bullets = new ArrayList<>();
	private Image image = new Image("neuImages/neuBlasterNoThrust.png");
	
	public Image getImage() {
		return image;
	}

	public void setImage(String url) {
		this.image = new Image("neuImages/" + url);
	}

	public SpaceShip(double x, double y, double speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
	public void move (double dX, double dY) {
		x += dX * speed;
		y += dY * speed;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public ArrayList<NeuBullet> getBullets() {
		return bullets;
	}

	public void setBullets(ArrayList<NeuBullet> bullets) {
		this.bullets = bullets;
	}
	
	
}
