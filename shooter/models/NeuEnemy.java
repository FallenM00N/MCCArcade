package models;

import javafx.scene.image.Image;

public class NeuEnemy {
	private double x;
	private double y;
	private double speed;
	private Image image = new Image("neuImages/enemy1NoThrust.png");

	public NeuEnemy(double x, double y, double speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void moveStraight() {
		x = getX() + speed;
	}
	
	public void seek(double target) {
		
	}

	public double getTarget(SpaceShip ship) {
		return 0;
	}
	
}
