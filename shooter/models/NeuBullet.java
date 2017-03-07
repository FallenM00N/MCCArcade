package models;

import interfaces.Collidable;
import javafx.scene.shape.Rectangle;

public class NeuBullet implements Collidable{
	private double x;
	private double y;
	private final double SPEED;
	private final double width;
	private final double height;
	private Rectangle bounds;
	
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
		width = 9;
		height = 5;
		bounds = new Rectangle(x, y, width, height);
	}
	
	public NeuBullet(double x, double y, double width, double height, double SPEED) {
		this.x = x;
		this.y = y;
		this.SPEED = SPEED;
		this.width = width;
		this.height = height;
		bounds = new Rectangle(x, y, width, height);
	}
	
	public void move() {
		x += SPEED;
		bounds.setX(x);
	}
	
	public void seek(NeuEnemy enemy) {
		double target = enemy.getY();
		if(y > target) {
			y -= SPEED / 6;
		} else if(y < target) {
			y += SPEED / 6;
		}
		bounds.setY(y);
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

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public boolean collides(Collidable object) {
		boolean collides = false;
		Rectangle otherBounds = object.getBounds();
		if(otherBounds.getBoundsInParent().intersects(bounds.getBoundsInParent())) {
			collides = true;
		}
		return collides;
	}
	
}
