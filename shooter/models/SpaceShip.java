package models;

import java.util.ArrayList;

import game.Neublasters;
import game.NeublastersEngine;
import interfaces.Collidable;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;

public class SpaceShip implements Collidable{
	private double x;
	private double y;
	private double speed;
	private ArrayList<NeuBullet> bullets = new ArrayList<>();
	private Image image = new Image("neuImages/neuBlasterNoThrust.png");
	Rectangle bounds;
	private double score = 0;
	private int lives = 3;
	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

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
		bounds = new Rectangle(x, y, getImage().getHeight(), getImage().getWidth());
	}
	
	public void move (double dX, double dY) {
		x += dX * speed;
		y += dY * speed;
		bounds.setX(x);
		bounds.setY(y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		bounds.setX(x);
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		bounds.setY(y);
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

	public void loseLife() {
		lives--;
		if(lives == 0) {
			NeublastersEngine.getTimer().stop();
			System.out.println(Neublasters.getOverScene().getRoot().getChildrenUnmodifiable());
			Neublasters.showScene(Neublasters.getOverScene(), "Game Over");
		}
	}
	
}
