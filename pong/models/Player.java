package models;

import game.PongEngine;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Player {

	private int score;
	private Rectangle paddle;
	private double x;
	private double y;
	private double width;
	private double height;
	private double speed = 10;
	private boolean touchingTop;
	private boolean touchingBottom;
	

	public boolean isTouchingTop() {
		return touchingTop;
	}

	public void setTouchingTop(boolean touchingTop) {
		this.touchingTop = touchingTop;
	}

	public boolean isTouchingBottom() {
		return touchingBottom;
	}

	public void setTouchingBottom(boolean touchingBottom) {
		this.touchingBottom = touchingBottom;
	}

	public Player(double x, double y, double width, double height) {
		paddle = new Rectangle(x, y, width, height);
		paddle.setFill(Paint.valueOf("#fff"));
		paddle.toFront();
		score = 0;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Rectangle getPaddle() {
		return paddle;
	}

	public void setPaddle(Rectangle paddle) {
		this.paddle = paddle;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		paddle.setLayoutX(getX());
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		paddle.setLayoutY(getY());
		if(y >= PongEngine.getHeight() - 100) {
			touchingBottom = true;
		}else {
			touchingBottom = false;
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

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
