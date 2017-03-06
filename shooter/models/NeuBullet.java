package models;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class NeuBullet {
	private double x;
	private double y;
	private final double SPEED;
	private final double width;
	private final double height;
	
	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public NeuBullet(double x, double y, double SPEED) {
		this.x = x;
		this.y = y;
		this.SPEED = SPEED;
		width = 5;
		height = 3;
	}
	
	public NeuBullet(double x, double y, double width, double height, double SPEED) {
		this.x = x;
		this.y = y;
		this.SPEED = SPEED;
		this.width = width;
		this.height = height;
	}
	
	public void move() {
		x += SPEED;
	}
	
	public void seek(NeuEnemy enemy) {
		double target = enemy.getY();
		if(y > target) {
			y -= SPEED / 6;
		} else if(y < target) {
			y += SPEED / 6;
		}
	}
	
	public void getTarget() {
		//TODO: for each enemy, if distance < MAX_TARGETING seek(enemy); break;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getSPEED() {
		return SPEED;
	}
	
}
