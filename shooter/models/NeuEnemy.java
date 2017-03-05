package models;

public class NeuEnemy {
	private double x;
	private double y;
	private double speed;

	public NeuEnemy(double x, double y, double speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
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

}
