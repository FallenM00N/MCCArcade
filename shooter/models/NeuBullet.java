package models;

public class NeuBullet {
	private double x;
	private double y;
	private final double SPEED;
	
	public NeuBullet(double x, double y, double SPEED) {
		this.x = x;
		this.y = y;
		this.SPEED = SPEED;
	}
	
	public void move() {
		x += SPEED;
	}
	
	public void seek(NeuEnemy enemy) {
		double target = enemy.getY();
		if(y > target) {
			y -= SPEED / 6;
		} else if(y < target) {
			y += SPEED / 6;
		}
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
	
}
