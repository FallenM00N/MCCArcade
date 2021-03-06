package models;

import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import application.Main;
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
			playSound("3");
		}
		if (ball.getBoundsInParent().intersects(ScoreBoard.getWall().getBoundsInParent())) {
			if (dY < 0) {
				dY *= -1;
			}
			playSound("4");
		}
		if (ball.getBoundsInParent().intersects(PongEngine.getLeftPlayer().getPaddle().getBoundsInParent())) {
			double curY = PongEngine.getLeftPlayer().getPaddle().getLayoutY();
			double colY = ball.getLayoutY();
			double hitLocation = colY - curY - 40;
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
		playSound("1");

	}

	private void getPaddleBounceLeft(double hitLocation) {
		if (hitLocation > -8 && hitLocation < 8) {
			dY = 0;
		} else {
			dY = hitLocation / 50;
		}
		dX = 1;
		playSound("2");
		
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
		if (dX == 0 && countDown == 0) {
			Random rand = new Random();
			if (rand.nextBoolean()) {
				dX = -1;
			} else {
				dX = 1;
			} 
			dY = rand.nextDouble() / 3;
		}
	}
	
	public static synchronized void playSound(String file) {
		  new Thread(new Runnable() {
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
		        Main.class.getResource("/pongSounds/sound" + file + ".wav"));
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}

}
