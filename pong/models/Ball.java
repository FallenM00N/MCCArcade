package models;

import java.util.Random;

import game.PongEngine;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Ball {
	static Rectangle ball;
	double x;
	double y;
	double width;
	double height;
	double dX = -1;
	double dY = 0;
	
	public Ball(double x, double y, double width) {
		ball = new Rectangle(x, y, width, width);
		ball.setFill(Paint.valueOf("#fff"));
	}

	public Rectangle getBall() {
		return ball;
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

	public double getdX() {
		return dX;
	}

	public void setdX(double dX) {
		this.dX = dX;
	}

	public double getdY() {
		return dY;
	}

	public void setdY(double dY) {
		this.dY = dY;
	}

	public static void reset() {
		ball.setLayoutX(0);
		ball.setLayoutY(0);
	}

	public void getDirection() {
		if (ball.getBoundsInParent().intersects(PongEngine.getBottomWall().getBoundsInParent())) {
			dY = dY * -1;
		}
		if (ball.getBoundsInParent().intersects(ScoreBoard.getWall().getBoundsInParent())) {
			dY = dY * -1;
		}
		if (ball.getBoundsInParent().intersects(PongEngine.getLeftPlayer().getPaddle().getBoundsInParent())) {
			Shape collisionArea = Shape.intersect(ball, PongEngine.getLeftPlayer().getPaddle());
			double curY = PongEngine.getLeftPlayer().getPaddle().getLayoutY();
			double colY = collisionArea.getLayoutY();
			double hitLocation = colY - curY;
			getPaddleBounceLeft(hitLocation);
		}
		if (ball.getBoundsInParent().intersects(PongEngine.getRightPlayer().getPaddle().getBoundsInParent())) {
			
		}
	}

	private void getPaddleBounceLeft(double hitLocation) {
		Random rand = new Random();
		double height = PongEngine.getLeftPlayer().getPaddle().getHeight();
		if (hitLocation < height / 2 + (height / 10) && hitLocation > height / 2 - (height / 10)) {
			dX = -1;
			dY = 0;
		}
		if(hitLocation < height / 2 + (2 * height / 10) && hitLocation > height / 2 + (height / 10)) {
			dX = 0.8;
			dY -= 0.2;
		}
		if(hitLocation > height / 2 - (2 * height / 10) && hitLocation < height / 2 + (2 * height / 10)) {
			dX = 0.8;
			dY += 0.2;
		}
		if(hitLocation < height / 2 + (3 * height / 10) && hitLocation > height / 2 + (2 * height / 10)) {
			dX = 0.6;
			dY -= 0.4;
		}
		if(hitLocation > height / 2 - (3 * height / 10) && hitLocation > height / 2 + (2 * height / 10)) {
			dX = 0.6;
			dY += 0.4;
		}
		if(hitLocation < height / 2 - (4 * height / 10)) {
			dX = 0.4;
			dY -= 0.6;
		}
		if(hitLocation > height / 2 + (4 * height / 10)) {
			dX = 0.4;
			dY += 0.6;
		}
		
	}

	public void move(double speed) {
		ball.setLayoutX(ball.getLayoutX() + (speed * dX));
		ball.setLayoutY(ball.getLayoutY() + (speed * dY));
	}

}
