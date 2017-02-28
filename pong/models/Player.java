package models;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Player {
	private int score;
	private Rectangle paddle;

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
	
	public void incrementScore() {
		score++;
	}

	public Rectangle getPaddle() {
		return paddle;
	}

	public void setPaddle(Rectangle paddle) {
		this.paddle = paddle;
	}

	public double getY() {
		return paddle.getLayoutY();
	}

	public void move(double speed) {
		paddle.setLayoutY(paddle.getLayoutY() + speed);
	}

}
