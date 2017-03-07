package models;

import java.util.Random;

import game.NeublastersEngine;
import interfaces.Collidable;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class NeuEnemy implements Collidable {
	private double x;
	private double y;
	private double speed;
	private Image image = new Image("neuImages/enemy1Thrust.png");
	private Rectangle bounds;
	private boolean isDestroyed;
	private int destroyFrame = 30;
	private int bulletCountDown;
	private double points;

	public NeuEnemy(double x, double y, double speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		bounds = new Rectangle(x, y, image.getWidth(), image.getHeight());
		Random rand = new Random();
		bulletCountDown = rand.nextInt(110) + 10;
	}

	public int getBulletCountDown() {
		return bulletCountDown;
	}

	public void setBulletCountDown(int bulletCountDown) {
		this.bulletCountDown = bulletCountDown;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setX(double x) {
		this.x = x;
		bounds.setX(x);
	}

	public void setY(double y) {
		this.y = y;
		bounds.setY(y);
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
		bounds.setX(x);
	}
	
	public void seek(double target) {
		
	}

	public double getTarget(SpaceShip ship) {
		return 0;
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
	
	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	public void destroy() {
		isDestroyed = true;
		points = NeublastersEngine.addScore();

	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public int getDestroyFrame() {
		return destroyFrame;
	}

	public void setDestroyFrame(int destroyFrame) {
		this.destroyFrame = destroyFrame;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public void fire() {
		NeuBullet bullet = new NeuBullet(x + image.getWidth() - 10,
		y + image.getHeight() / 2, speed * 4);
		NeublastersEngine.getEnemyBullets().add(bullet);
	}

	public void tick() {
		bulletCountDown--;
	}
	
}
