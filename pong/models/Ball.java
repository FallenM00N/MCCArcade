package models;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Ball {
	Rectangle ball;
	double x;
	double y;
	double width;
	double height;
	double dX;
	double dY;
	
	public Ball(double x, double y, double width) {
		ball = new Rectangle(x, y, width, width);
		ball.setFill(Paint.valueOf("#fff"));
	}
	
	public Rectangle getBall() {
		return ball;
	}
	public void setBall(Rectangle ball) {
		this.ball = ball;
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
	
}
