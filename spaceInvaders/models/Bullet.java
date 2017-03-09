package models;

import game.SpaceInvaders;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Bullet {

	private Rectangle bullet;
	private double x = 0;
	private double y = 0;
	private double width = 3.5;
	private double height = 7;
	private boolean movingUp = true;
	
	public Bullet(double x, double y, boolean movingUp) {
		setX(x);
		setY(y);
		this.movingUp = movingUp;
		
		if (isMovingUp()) {
			if (SpaceInvaders.player.getFireSpeed() < 500) {
				bullet = new Rectangle(this.width, this.height, Paint.valueOf("#B04"));
			}
			else {
				bullet = new Rectangle(this.width, this.height, Paint.valueOf("#0B4"));
			}
		}
		else {
			bullet = new Rectangle(this.width, this.height, Paint.valueOf("#BBB"));
		}
		
		bullet.setX(this.x);
		bullet.setY(this.y);
	}
	
	public Rectangle getBullet() {
		return bullet;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
		if (bullet != null) {
			bullet.setLayoutX(getX());
		}
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
		if (bullet != null) {
			bullet.setLayoutY(getY());
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
	public boolean isMovingUp() {
		return movingUp;
	}
	public void toggleDirection() {
		this.movingUp = !this.movingUp;
	}
}
