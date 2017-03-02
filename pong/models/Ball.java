package models;

import java.util.Random;

import game.PongEngine;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Ball {
	static Rectangle ball;
	double x;
	double y;
	double width;
	double height;
	static double dX = 0;
	static double dY = 0;
	static double countDown = 0;

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

	public void setdX(double newDX) {
		newDX = dX;
	}

	public double getdY() {
		return dY;
	}

	public void setdY(double newDY) {
		newDY = dY;
	}

	public static void reset() {
		ball.setLayoutX(0);
		ball.setLayoutY(0);
		dY = 0;
		dX = 0;
	}

	public void getDirection() {
		if (ball.getBoundsInParent().intersects(PongEngine.getBottomWall().getBoundsInParent())) {
			if (dY > 0) {
				dY *= -1;
			}
		}
		if (ball.getBoundsInParent().intersects(ScoreBoard.getWall().getBoundsInParent())) {
			if (dY < 0) {
				dY *= -1;
			}
		}
		if (ball.getBoundsInParent().intersects(PongEngine.getLeftPlayer().getPaddle().getBoundsInParent())) {
			double curY = PongEngine.getLeftPlayer().getPaddle().getLayoutY();
			double colY = ball.getLayoutY();
			double hitLocation = colY - curY - 50;
			if (hitLocation > 50)
				hitLocation = 50;
			if (hitLocation < 0 - 50)
				hitLocation = -50;
			getPaddleBounceLeft(hitLocation);
		}
		if (ball.getBoundsInParent().intersects(PongEngine.getRightPlayer().getPaddle().getBoundsInParent())) {
			double curY = PongEngine.getRightPlayer().getPaddle().getLayoutY();
			double colY = ball.getLayoutY();
			double hitLocation = colY - curY - 40;
			if (hitLocation > 50)
				hitLocation = 50;
			if (hitLocation < 0 - 50)
				hitLocation = -50;
			getPaddleBounceRight(hitLocation);
		}
	}

	private void getPaddleBounceRight(double hitLocation) {
		if (hitLocation > -8 && hitLocation < 8) {
			dY = 0;
		} else {
			dY = hitLocation / 50;
		}
		dX = -1;

		// double height = PongEngine.getRightPlayer().getPaddle().getHeight();
		// double m;
		// m = hitLocation / (height * 4);
		// double curSlope = dY / dX;
		// double slope = (curSlope * Math.pow(m, 2) - curSlope + (2 * m)) / (0
		// - Math.pow(m, 2) + 1 + (2 * m * curSlope));
		// slope = Math.atan(slope);
		// if(slope > 0 - (2 * Math.PI / 3) && slope < 0) {
		// slope = 0 - 2 * Math.PI / 3;
		// }
		// if(slope < 2 * Math.PI / 3 && slope > 0) {
		// slope = 2 * Math.PI / 3;
		// }
		// dY = Math.sin(slope);
		// dX = 0 - 0.5 + Math.cos(slope);

		// if (hitLocation < height / 2 + (height / 10) && hitLocation > height
		// / 2 - (height / 10)) {
		// m = 0;
		// }
		// if(hitLocation < height / 2 + (2 * height / 10) && hitLocation >
		// height / 2 + (height / 10)) {
		// dX = -0.8;
		// dY -= 0.2;
		// }
		// if(hitLocation > height / 2 - (2 * height / 10) && hitLocation <
		// height / 2 + (2 * height / 10)) {
		// dX = -0.8;
		// dY += 0.2;
		// }
		// if(hitLocation < height / 2 + (3 * height / 10) && hitLocation >
		// height / 2 + (2 * height / 10)) {
		// dX = -0.6;
		// dY -= 0.4;
		// }
		// if(hitLocation > height / 2 - (3 * height / 10) && hitLocation <
		// height / 2 + (2 * height / 10)) {
		// dX = -0.6;
		// dY += 0.4;
		// }
		// if(hitLocation < height / 2 - (4 * height / 10)) {
		// dX = -0.4;
		// dY -= 0.6;
		// }
		// if(hitLocation > height / 2 + (4 * height / 10)) {
		// dX = -0.4;
		// dY += 0.6;
		// }
	}

	private void getPaddleBounceLeft(double hitLocation) {
		if (hitLocation > -8 && hitLocation < 8) {
			dY = 0;
		} else {
			dY = hitLocation / 50;
		}
		dX = 1;

		// double height = PongEngine.getLeftPlayer().getPaddle().getHeight();
		// double m;
		// m = hitLocation * (height * 4);
		// double curSlope = dY / dX;
		// double slope = (curSlope * Math.pow(m, 2) - curSlope + (2 * m)) / (0
		// - Math.pow(m, 2) + 1 + (2 * m * curSlope));
		// slope = Math.atan(slope);
		// if(slope > Math.PI / 3 && slope < Math.PI) {
		// slope = Math.PI / 3;
		// }
		// if(slope < 0 - (Math.PI / 3) && slope > Math.PI) {
		// slope = 0 - Math.PI / 3;
		// }
		// dY = Math.sin(slope);
		// dX = 0.5 + Math.cos(slope);

		// if (hitLocation < height / 2 + (height / 10) && hitLocation > height
		// / 2 - (height / 10)) {
		// dX = -1;
		// dY = 0;
		// }
		// if(hitLocation < height / 2 + (2 * height / 10) && hitLocation >
		// height / 2 + (height / 10)) {
		// dX = 0.8;
		// dY -= 0.2;
		// }
		// if(hitLocation > height / 2 - (2 * height / 10) && hitLocation <
		// height / 2 + (2 * height / 10)) {
		// dX = 0.8;
		// dY += 0.2;
		// }
		// if(hitLocation < height / 2 + (3 * height / 10) && hitLocation >
		// height / 2 + (2 * height / 10)) {
		// dX = 0.6;
		// dY -= 0.4;
		// }
		// if(hitLocation > height / 2 - (3 * height / 10) && hitLocation <
		// height / 2 + (2 * height / 10)) {
		// dX = 0.6;
		// dY += 0.4;
		// }
		// if(hitLocation < height / 2 - (4 * height / 10)) {
		// dX = 0.4;
		// dY -= 0.6;
		// }
		// if(hitLocation > height / 2 + (4 * height / 10)) {
		// dX = 0.4;
		// dY += 0.6;
		// }
		//
	}

	public void move(double speed) {
		ball.setLayoutX(ball.getLayoutX() + (speed * dX));
		ball.setLayoutY(ball.getLayoutY() + (speed * dY));
	}

	public void countDown() {
		if (countDown == 0) {
			countDown = 60;
		}
		countDown--;
		System.out.println(countDown);
		if (dX == 0 && countDown == 0) {
			Random rand = new Random();
			if (rand.nextBoolean()) {
				dX = -1;
			} else {
				dX = 1;
			} 
		}
	}

}
