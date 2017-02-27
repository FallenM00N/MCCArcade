package game;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import models.Barrier;
import models.Bullet;
import models.Enemy;

public class MovementHandler implements Runnable {

	private Enemy[][] enemies = new Enemy[5][10];
	private int shotLimit = 1000;
	private int lastShot = 0;
	private int direction = 5;
	private int timeLimit = 501;
	private int time = 0;
	private int lastSpawn = 0;
	private Thread thread;
	private Timeline timeline;
	private Random rand = new Random();
	
	public MovementHandler() {
		ArrayList<Enemy> e = SpaceInvaders.enemies;
		
		for (int i = 0; i < e.size(); i++) {
			enemies[i / 10][i % 10] = e.get(i);
		}
		
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(1),
		        ae -> timerTick()));
		
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	private void timerTick() {
		time++;
		if (time - lastSpawn >= timeLimit) {
			moveEnemies();
			lastSpawn = time;
		}
		
		moveBullets();
		
		if (time - lastShot >= shotLimit) {
			enemyShoot();
			lastShot = time;
		}
	}
	
	private void enemyShoot() {
		ArrayList<Enemy> e = SpaceInvaders.enemies;
		int x = 0;
		boolean valid = true;
		
		do {
			if (e.size() > 10) {
				x = rand.nextInt(10);
			}
			else {
				x = rand.nextInt(e.size());
			}
			
			if (x > e.size()) {
				valid = false;
			}
			
			double posX = e.get(x).getX();
			double posY = e.get(x).getY();
			
			for (int i = 0; i < e.size(); i++) {
				if ((e.get(i).getX() == posX && i > 10 && e.size() > 10) ||
						(e.get(i).getX() == posX && i < e.size())) {
					if (e.get(i).getY() > posY) {
						x = i;
					}
				}
			}
		}while(!valid);
		
		Enemy enemy = e.get(x);
		Bullet b = new Bullet(
				enemy.getX() + enemy.getWidth() / 2,
				enemy.getY() + enemy.getHeight(), false);
		SpaceInvaders.entities.getChildren().add(b.getBullet());
		b.setX(0);
		b.setY(0);
		SpaceInvaders.bullets.add(b);
	}
	
	private void moveEnemies() {
		ArrayList<Enemy> e = SpaceInvaders.enemies;
		for (int i = 0; i < e.size(); i++) {
			if (e.get(i).getX() + e.get(i).getWidth() >= SpaceInvaders.gameScene.getWidth() - 5 &&
					direction > 0 || direction < 0 &&
					e.get(i).getX() <= 5) {
				direction = -direction;
				if (timeLimit > 100) {
					timeLimit -= 50;
				}
				else if (timeLimit > 30) {
					timeLimit -= 10;
				}
				for (int j = 0; j < e.size(); j++) {
					e.get(j).setY(e.get(j).getY() + 10);
				}
			}
		}
		
		for (int i = 0 ; i < e.size(); i++) {
			e.get(i).setX(e.get(i).getX() + direction);
		}
	}
	
	private void moveBullets() {
		ArrayList<Enemy> e = SpaceInvaders.enemies;
		ArrayList<Barrier> a = SpaceInvaders.barriers;
		ArrayList<Bullet> b = SpaceInvaders.bullets;
		models.Character p = SpaceInvaders.player;
		for (int i = 0; i < b.size(); i++) {
			if (b.get(i).getY() > 400 && b.get(i).isMovingUp()) {
				b.get(i).setY(5);
			}
			if (b.get(i).isMovingUp()) {
				b.get(i).setY(b.get(i).getY() - .2);
			}
			else {
				b.get(i).setY(b.get(i).getY() + .2);
			}
			
			for (int x = 0; x < e.size(); x++) {
				if (i < b.size() && x < e.size() &&
						b.get(i).getBullet().getBoundsInParent().intersects(e.get(x).getImg().getBoundsInParent())
						&& b.get(i).isMovingUp()) {
					killEnemy(b.get(i), e.get(x));
				}
			}
			
			for (int x = 0; x < a.size(); x++) {
				if (i < b.size() && x < a.size() &&
						b.get(i).getBullet().getBoundsInParent().intersects(a.get(x).getImg().getBoundsInParent())) {
					damageBarrier(b.get(i), a.get(x));
				}
			}
			
			if (i < b.size() &&
					b.get(i).getBullet().getBoundsInParent().intersects(p.getImg().getBoundsInParent())) {
				loseLife(1);
				SpaceInvaders.entities.getChildren().remove(b.get(i).getBullet());
				b.remove(i);
			}
			
			if (i < b.size() && b.get(i).getY() < -410) {
				SpaceInvaders.entities.getChildren().remove(b.get(i).getBullet());
				b.remove(i);
			}
		}
	}
	
	private void damageBarrier(Bullet b, Barrier a) {
		a.takeDamage(16);
		SpaceInvaders.entities.getChildren().remove(b.getBullet());
		SpaceInvaders.bullets.remove(b);
	}
	
	private void killEnemy(Bullet b, Enemy e) {
		SpaceInvaders.score += 100 + (500 - (timeLimit - 1));
		String f = Integer.toString(SpaceInvaders.score);
		SpaceInvaders.scoreString = SpaceInvaders.scoreString.substring(0, SpaceInvaders.scoreString.length() - f.length());
		SpaceInvaders.scoreString += f;
		SpaceInvaders.scorel.setText("SCORE: " + SpaceInvaders.scoreString);
		
		SpaceInvaders.entities.getChildren().remove(b.getBullet());
		SpaceInvaders.entities.getChildren().remove(e.getImg());
		SpaceInvaders.bullets.remove(b);
		SpaceInvaders.enemies.remove(e);
	}
	
	private void loseLife(int livesLost) {
		SpaceInvaders.player.loseLife(1);
		SpaceInvaders.livesl.setText("LIVES: " + SpaceInvaders.player.getLives());
	}
	
	public void stopTimer() {
		timeline.stop();
	}
	
	public void pauseTimer() {
		timeline.pause();
	}
	
	public void resumeTimer() {
		timeline.play();
	}
}
