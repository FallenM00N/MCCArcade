package models;

import game.PongEngine;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ScoreBoard {

	private int small = 15;
	private int big = 50;
	private static final int HEIGHT = 160;
	private static Rectangle wall;
	private Group player1Score;
	private Group player2Score;
	private Group scoreBoard = new Group();
	private Rectangle[] rects1;
	private Rectangle[] rects2;

	public ScoreBoard() {
		getHR();
		createPlayer1Rects();
		createPlayer2Rects();
		getScoreDisplay(0, 1);
		getScoreDisplay(0, 2);
		scoreBoard.getChildren().add(player1Score);
		scoreBoard.getChildren().add(getDivider());
		scoreBoard.getChildren().add(player2Score);
		scoreBoard.getChildren().add(wall);
	}

	public static int getHeight() {

		return HEIGHT;
	}

	public Group getScoreBoard() {
		return scoreBoard;
	}

	public void getScoreDisplay(int score, int player) {
		Rectangle[] playerGroup;
		if (player == 1) {
			playerGroup = rects1;
		} else {
			playerGroup = rects2;
		}
		switch (score) {
		case 0:
			showRects(playerGroup, 0, 1, 2, 4, 5, 6);
			break;
		case 1:
			showRects(playerGroup, 2, 5);
			break;
		case 2:
			showRects(playerGroup, 0, 2, 3, 4, 6);
			break;
		case 3:
			showRects(playerGroup, 0, 2, 3, 5, 6);
			break;
		case 4:
			showRects(playerGroup, 1, 2, 3, 5);
			break;
		case 5:
			showRects(playerGroup, 0, 1, 3, 5, 6);
			break;
		case 6:
			showRects(playerGroup, 0, 1, 3, 4, 5, 6);
			break;
		case 7:
			showRects(playerGroup, 0, 2, 5);
			break;
		case 8:
			showRects(playerGroup, 0, 1, 2, 3, 4, 5, 6);
			break;
		case 9:
			showRects(playerGroup, 0, 1, 2, 3, 5, 6);
			break;
		}
	}

	private void showRects(Rectangle[] rects, int... id) {
		for (Rectangle r : rects) {
			r.setFill(Paint.valueOf("rgba(0,0,0,0)"));
		}
		for (int i = 0; i < id.length; i++) {
			rects[id[i]].setFill(Paint.valueOf("#fff"));
		}
	}

	private Rectangle[] createRects() {
		Rectangle[] rects = new Rectangle[] { new Rectangle(0, 0, big, small), new Rectangle(0, 0, small, big),
				new Rectangle(big - small, 0, small, big), new Rectangle(0, big, big, small),
				new Rectangle(0, big, small, big), new Rectangle(big - small, big, small, big),
				new Rectangle(0, big + big, big, small) };
		return rects;
	}

	public void createPlayer1Rects() {
		player1Score = new Group();
		rects1 = createRects();
		player1Score.getChildren().addAll(rects1);
		player1Score.setLayoutX(PongEngine.getWidth() / 2 - 80);
		player1Score.setLayoutY(20);
	}

	public void createPlayer2Rects() {
		player2Score = new Group();
		rects2 = createRects();
		player2Score.getChildren().addAll(rects2);
		player2Score.setLayoutX(PongEngine.getWidth() / 2 + 30);
		player2Score.setLayoutY(20);
	}

	public Rectangle getDivider() {
		Rectangle divider = new Rectangle(25, 15);
		divider.setFill(Paint.valueOf("#fff"));
		divider.setX(PongEngine.getWidth() / 2 - 15);
		divider.setY(70);
		return divider;
	}

	private void getHR() {
		wall = new Rectangle(0, HEIGHT, PongEngine.getWidth(), 10);
		wall.setFill(Paint.valueOf("#fff"));
	}

	public static Rectangle getWall() {
		return wall;
	}

	public void updateScore(int left, int right) {
		getScoreDisplay(left, 1);
		getScoreDisplay(right, 2);
	}
}
